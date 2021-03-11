package com.dailyyoga.plugin.droidassist.transform.replace;

import com.dailyyoga.plugin.droidassist.util.Logger;

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
    protected String getExecuteType() {
        return METHOD_CALL;
    }

    @Override
    protected String getTransformType() {
        return TRANSFORM_EXPR;
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

        String insnClassName = methodCall.getClassName();
        String insnName = methodCall.getMethodName();
        String insnSignature = methodCall.getSignature();

        CtClass insnClass = tryGetClass(insnClassName, inputClassName);
        if (insnClass == null) {
            return false;
        }

        if (!isMatchSourceMethod(insnClass, insnName, insnSignature, false)) {
            return false;
        }

        String target = getTarget();
        String replacement = replaceInstrument(inputClassName, methodCall, target);
        count++;

        if (isJournal()) {
            Logger.warning(getPrettyName() + " by: " + replacement
                    + " at " + inputClassName + ".java" + ":" + methodCall.getLineNumber());
        }
        return true;
    }
}
