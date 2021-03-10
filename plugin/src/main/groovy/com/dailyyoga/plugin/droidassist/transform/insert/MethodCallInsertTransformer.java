package com.dailyyoga.plugin.droidassist.transform.insert;

import com.dailyyoga.plugin.droidassist.util.Logger;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 9:45 AM
 * @description:
 */
public class MethodCallInsertTransformer extends InsertTransformer {

    @Override
    protected String getExecuteType() {
        return METHOD_CALL;
    }

    @Override
    protected String getTransformType() {
        return TRANSFORM_EXPR;
    }

    @Override
    public String getName() {
        return "MethodCallInsertTransformer";
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
        int line = methodCall.getLineNumber();
        if (!target.endsWith(";")) target = target + ";";

        String before = isAsBefore() ? target : "";
        String after = isAsAfter() ? target : "";

        String proceed = isVoidSourceReturnType() ? "$proceed($$);" : "$_ =$proceed($$);";

        String statement = before + proceed + after;

        String replacement = replaceInstrument(inputClassName, methodCall, statement);

        if (isAsBefore()) {
            Logger.warning(getPrettyName() + " insert before call by: " + replacement
                    + " at " + inputClassName + ".java" + ":" + line);
        }

        if (isAsAfter()) {
            Logger.warning(getPrettyName() + " insert after call by: " + replacement
                    + " at " + inputClassName + ".java" + ":" + line);
        }
        return true;
    }
}
