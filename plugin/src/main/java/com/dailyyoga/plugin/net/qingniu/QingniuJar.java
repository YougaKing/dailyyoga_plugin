package com.dailyyoga.plugin.net.qingniu;

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
public class QingniuJar extends InjectJar {

    public QingniuJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/qingniu/scale/utils/MacUtils.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod getMacFromHardware = ctClass.getDeclaredMethod("getMacFromHardware");
        mProject.getLogger().error("getMacFromHardware:" + getMacFromHardware);

        getMacFromHardware.setBody(injectMethodBody(getMacFromHardware.getLongName()));

        return ctClass;
    }

}