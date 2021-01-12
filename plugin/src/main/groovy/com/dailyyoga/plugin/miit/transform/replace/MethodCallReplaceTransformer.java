package com.dailyyoga.plugin.miit.transform.replace;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

public class MethodCallReplaceTransformer extends ReplaceTransformer {

    @Override
    public String getName() {
        return "MethodCallReplaceTransformer";
    }

    @Override
    protected String getTransformType() {
        return TRANSFORM_EXPR;
    }

    @Override
    protected String getExecuteType() {
        return METHOD_CALL;
    }

    @Override
    protected boolean execute(CtClass inputClass, String inputClassName, MethodCall methodCall) throws CannotCompileException, NotFoundException {
        return super.execute(inputClass, inputClassName, methodCall);
    }
}
