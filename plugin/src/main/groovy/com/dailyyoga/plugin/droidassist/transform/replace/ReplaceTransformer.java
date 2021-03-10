package com.dailyyoga.plugin.droidassist.transform.replace;

import com.dailyyoga.plugin.droidassist.transform.ExprExecTransformer;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 9:49 AM
 * @description:
 */
public abstract class ReplaceTransformer extends ExprExecTransformer {

    @Override
    public String getCategoryName() {
        return "Replace";
    }
}
