package com.dailyyoga.plugin.net;

import java.io.File;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/9/2 02:39
 * @description:
 */
public class InjectRealJar {

    protected File mOriginFile;
    protected File mTempFile;
    protected File mDestFile;

    public InjectRealJar(File originFile, File tempFile, File destFile) {
        mOriginFile = originFile;
        mTempFile = tempFile;
        mDestFile = destFile;
    }

    public boolean unAvailable(){
        return mOriginFile == null || mTempFile == null || mDestFile == null;
    }
}
