package com.dailyyoga.plugin.miit.transform.replace;

import com.dailyyoga.plugin.miit.transform.ExprExecTransformer;
import com.dailyyoga.plugin.miit.util.Logger;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

public class MethodCallReplaceTransformer extends ExprExecTransformer {

    @Override
    public String getName() {
        return "MethodCallReplaceTransformer";
    }

    @Override
    protected String getExecuteType() {
        return METHOD_CALL;
    }

    @Override
    protected boolean execute(
            CtClass inputClass,
            String inputClassName,
            MethodCall methodCall)
            throws CannotCompileException, NotFoundException {
        if (methodCall.isSuper()) {
            return false;
        }

        if (!isMatchSourceMethod(methodCall)) {
            return false;
        }

        String replacement = replaceInstrument(methodCall);
        Logger.warning(getName() + " by: " + replacement
                + " at " + inputClassName + ".java" + ":" + methodCall.getLineNumber());
        return true;
    }
}
