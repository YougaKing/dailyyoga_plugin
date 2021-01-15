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
                                             MethodSpec methodSpec) {
        if (MethodSpec.SOURCE.equals(type)) {
            sourceSpec = methodSpec;
        } else if (MethodSpec.TARGET.equals(type)) {
            targetSpec = methodSpec;
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

    boolean isMatchSourceClass(CtClass insnClass) throws NotFoundException {
        Boolean anInterface = isInterface(insnClass);
        if (anInterface == null || anInterface) {
            return false;
        }
        return true;
    }

    protected String replaceInstrument(
            String sourceClassName,
            MethodCall methodCall)
            throws CannotCompileException, NotFoundException {

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
            MethodCall methodCall) throws NotFoundException {

        StringBuilder builder = new StringBuilder();
        builder.append(getTarget().getDeclaring())
                .append(".")
                .append(getTarget().getName())
                .append("(\"");
        builder.append(methodCall.getMethod().getLongName());

        if (!getSource().isStatic()) {
            builder.append(",")
                    .append("$0");
        }
        for (int i = 0; i < getSource().getParameters().length; i++) {
            builder.append(",")
                    .append("$")
                    .append(i + 1);
        }
        builder.append("\");");
        return getReplaceStatement(builder.toString());
    }

    private String getReplaceStatement(String content) {
        return "{ $_ = " + content + "}";
    }
}
