package com.dailyyoga.plugin.droidassist.transform.enhance;


import com.dailyyoga.plugin.droidassist.spec.SourceSpec;
import com.dailyyoga.plugin.droidassist.transform.ExprExecTransformer;

import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 9:43 AM
 * @description:
 */
public abstract class TryCatchTransformer extends ExprExecTransformer {
    private String exception;
    private CtClass exceptionClass;

    @Override
    public String getCategoryName() {
        return "TryCatch";
    }

    protected String getException() {
        if (exception == null || exception.trim().equals("")) {
            exception = " java.lang.Exception";
        }
        return SourceSpec.Type.forName(exception).getName();
    }

    protected CtClass getExceptionClass() throws NotFoundException {
        if (exceptionClass == null) {
            exceptionClass = classPool.get(getException());
        }
        return exceptionClass;
    }

    public TryCatchTransformer setException(String exception) {
        this.exception = exception;
        return this;
    }
}
