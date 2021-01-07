package com.dailyyoga.plugin.net.weibo;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 1/7/21 8:46 PM
 * @description:
 */
public class WeiboJar extends InjectJar {

    public WeiboJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/weibo/ssosdk/MfpBuilder.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod getMacAddr = ctClass.getDeclaredMethod("getMacAddr");
        mProject.getLogger().error("getMacFromHardware:" + getMacAddr);

        getMacAddr.setBody(injectMethodBody(getMacAddr.getLongName()));

        return ctClass;
    }

}