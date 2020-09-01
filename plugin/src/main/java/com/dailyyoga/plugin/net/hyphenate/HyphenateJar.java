package com.dailyyoga.plugin.net.hyphenate;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/9/1 09:12
 * @description:
 */
public class HyphenateJar extends InjectJar {

    public HyphenateJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/hyphenate/chat/EMClient.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod getDeviceInfo = ctClass.getDeclaredMethod("getDeviceInfo");
        mProject.getLogger().error("getDeviceInfo:" + getDeviceInfo);
        getDeviceInfo.setBody(injectGetDeviceInfoMethodBody(getDeviceInfo.getLongName()));

        return ctClass;

    }

}