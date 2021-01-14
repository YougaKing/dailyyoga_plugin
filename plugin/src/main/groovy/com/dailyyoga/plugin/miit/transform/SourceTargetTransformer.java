package com.dailyyoga.plugin.miit.transform;


import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadStatementException;
import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadTypeException;
import com.dailyyoga.plugin.miit.spec.MethodSpec;
import com.dailyyoga.plugin.miit.util.Logger;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

public abstract class SourceTargetTransformer extends Transformer {

    private MethodSpec sourceSpec;
    private MethodSpec targetSpec;

    public SourceTargetTransformer setMethod(String type,
                                             String declaring,
                                             String returnType,
                                             String name,
                                             String parameters,
                                             boolean isStatic) {
        if (MethodSpec.SOURCE.equals(type)) {
            sourceSpec = MethodSpec.create(declaring, returnType, name, parameters, isStatic);
        } else if (MethodSpec.TARGET.equals(type)) {
            targetSpec = MethodSpec.create(declaring, returnType, name, parameters, isStatic);
        } else {
            throw new DailyyogaMIITBadTypeException("Bad type :" + type);
        }
        return this;
    }

    public MethodSpec getSource() {
        return sourceSpec;
    }

    public MethodSpec getTarget() {
        return targetSpec;
    }

    private String getSourceDeclaringClassName() {
        return sourceSpec.getDeclaring();
    }

    private String getSourceMethodName() {
        return sourceSpec.getName();
    }

    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            String name)
            throws NotFoundException {

        boolean matchClass = insnClass.getName().equals(getSourceDeclaringClassName());
        if (!matchClass) {
            return false;
        }

        return name.equals(getSourceMethodName());
    }

    protected String replaceInstrument(
            String sourceClassName,
            MethodCall methodCall)
            throws CannotCompileException {

        String statement = getTarget().getName();
        String replacement = getReplaceStatement(sourceClassName, methodCall);
        try {
            String s = replacement.replaceAll("\n", "");
            methodCall.replace(s);
        } catch (CannotCompileException e) {
            Logger.error("Replace method call instrument error with statement: " + statement + "\n", e);
            throw new DailyyogaMIITBadStatementException(e);
        }
        return replacement;
    }

    protected String getReplaceStatement(
            String sourceClassName,
            MethodCall methodCall) {
        return "";
    }
}
