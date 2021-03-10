package com.dailyyoga.plugin.droidassist.transform.insert;

import com.dailyyoga.plugin.droidassist.transform.ExprExecTransformer;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 9:43 AM
 * @description:
 */
public abstract class InsertTransformer extends ExprExecTransformer {

    private boolean asBefore = false;
    private boolean asAfter = false;

    @Override
    public String getCategoryName() {
        return "Insert";
    }

    public boolean isAsBefore() {
        return asBefore;
    }

    public InsertTransformer setAsBefore(boolean asBefore) {
        this.asBefore = asBefore;
        return this;
    }

    public boolean isAsAfter() {
        return asAfter;
    }

    public InsertTransformer setAsAfter(boolean asAfter) {
        this.asAfter = asAfter;
        return this;
    }
}
