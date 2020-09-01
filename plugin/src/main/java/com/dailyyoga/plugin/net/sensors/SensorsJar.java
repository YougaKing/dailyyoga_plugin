package com.dailyyoga.plugin.net.sensors;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 22:05
 * @description:
 */
public class SensorsJar extends InjectJar {

    public SensorsJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/sensorsdata/analytics/android/sdk/util/SensorsDataUtils.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod getMacAddressByInterface = ctClass.getDeclaredMethod("getMacAddressByInterface");
        mProject.getLogger().error("getMacAddressByInterface:" + getMacAddressByInterface);

        getMacAddressByInterface.setBody(injectMethodBody(getMacAddressByInterface.getLongName()));

        return ctClass;
    }

}
