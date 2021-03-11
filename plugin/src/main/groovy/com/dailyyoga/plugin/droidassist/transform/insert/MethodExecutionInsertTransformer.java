package com.dailyyoga.plugin.droidassist.transform.insert;

import com.dailyyoga.plugin.droidassist.util.Logger;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 11:11 AM
 * @description:
 */
public class MethodExecutionInsertTransformer extends InsertTransformer {

    @Override
    public String getName() {
        return "MethodExecutionInsertTransformer";
    }

    @Override
    protected String getTransformType() {
        return TRANSFORM_EXEC;
    }

    @Override
    protected String getExecuteType() {
        return METHOD;
    }

    @Override
    protected boolean execute(
            CtClass inputClass,
            String inputClassName,
            CtMethod method)
            throws CannotCompileException, NotFoundException {

        String name = method.getName();
        String signature = method.getSignature();

        if (!isMatchSourceMethod(inputClass, false, name, signature, method, true)) {
            return false;
        }

        String target = getTarget();
        target = getReplaceStatement(inputClassName, method, target);
        count++;

        if (isAsBefore() && isJournal()) {
            method.insertBefore(target);
            Logger.warning(getPrettyName() + " by before: " + target
                    + " at " + inputClassName + ".java" + ":" + name);
        }
        if (isAsAfter() && isJournal()) {
            method.insertAfter(target);
            Logger.warning(getPrettyName() + " by after: " + target
                    + " at " + inputClassName + ".java" + ":" + name);
        }
        return true;
    }
}