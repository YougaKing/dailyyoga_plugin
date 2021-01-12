package com.dailyyoga.plugin.miit.transform.replace;

import com.dailyyoga.plugin.miit.transform.ExprExecTransformer;

public abstract class ReplaceTransformer extends ExprExecTransformer {

    @Override
    public String getName() {
        return "ReplaceTransformer";
    }

    @Override
    public String getCategoryName() {
        return "Replace";
    }

}
