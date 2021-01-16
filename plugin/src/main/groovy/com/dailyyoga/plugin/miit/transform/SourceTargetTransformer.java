package com.dailyyoga.plugin.miit.transform;


import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadStatementException;
import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadTypeException;
import com.dailyyoga.plugin.miit.spec.MethodSpec;
import com.dailyyoga.plugin.miit.util.Logger;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
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

    public void checkSourceTarget(String node) {
        if (getSource() == null) {
            throw new IllegalArgumentException("Empty source " + node);
        }
        if (getTarget() == null) {
            throw new IllegalArgumentException("Empty target " + node);
        }
        if (getSource().isStatic() != getTarget().isStatic()) {
            throw new IllegalArgumentException("Different isStatic source/target " + node);
        }
        if (!getSource().getReturnType().equals(getTarget().getReturnType())) {
            throw new IllegalArgumentException("Different returnType source/target " + node);
        }
        int dValue = getSource().isStatic() ? -1 : -2;
        if (getSource().getParameters().length - getTarget().getParameters().length != dValue) {
            throw new IllegalArgumentException("Different parameters source/target " + node);
        }
        if (!String.class.getName().equals(getTarget().getParameters()[0])) {
            throw new IllegalArgumentException("Target parameters [0] must " + String.class.getName() + " " + node);
        }
        for (int i = 0; i < getSource().getParameters().length; i++) {
            String sourceParameter = getSource().getParameters()[i];
            String targetParameter = getTarget().getParameters()[i + 1];
            if (!sourceParameter.equals(targetParameter)) {
                throw new IllegalArgumentException("Different parameters source/target [" + i + "] " + node);
            }
        }
    }

    boolean isMatchSourceClass(CtClass insnClass) throws NotFoundException {
        Boolean anInterface = isInterface(insnClass);
        if (anInterface == null || anInterface) {
            return false;
        }
        return true;
    }

    protected boolean isMatchSourceMethod(MethodCall methodCall)
            throws NotFoundException {
        if (!methodCall.getClassName().equals(getSource().getDeclaring())) {
            return false;
        }
        if (!methodCall.getMethodName().equals(getSource().getName())) {
            return false;
        }
        CtMethod method = methodCall.getMethod();
        if (!method.getReturnType().getName().equals(getSource().getReturnType())) {
            return false;
        }
        CtClass[] parameterTypes = method.getParameterTypes() == null ? new CtClass[0] : method.getParameterTypes();
        if (parameterTypes.length != getSource().getParameters().length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].getName().equals(getSource().getParameters()[i])) {
                return false;
            }
        }
        return true;
    }

    protected String replaceInstrument(
            String callMethodName,
            MethodCall methodCall) throws NotFoundException {

        String statement = getTarget().getName();
        String replacement = getReplaceStatement(callMethodName, methodCall);
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
            String callMethodName,
            MethodCall methodCall) throws NotFoundException {

        StringBuilder builder = new StringBuilder();
        builder.append(getTarget().getDeclaring())
                .append(".")
                .append(getTarget().getName())
                .append("(\"")
                .append(callMethodName)
                .append("\"");

        if (!getSource().isStatic()) {
            builder.append(",")
                    .append("$0");
        }
        for (int i = 0; i < getSource().getParameters().length; i++) {
            builder.append(",")
                    .append("$")
                    .append(i + 1);
        }
        builder.append(");");
        return getReplaceStatement(builder.toString());
    }

    private String getReplaceStatement(String content) {
        return "{ $_ = " + content + "}";
    }
}
