package com.dailyyoga.plugin.net.lebo;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 20:45
 * @description:
 */
public class LeBoJar extends InjectJar {

    public LeBoJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/hpplay/common/utils/DeviceUtil.class".equals(entryName) ||
                "com/hpplay/sdk/source/common/utils/HapplayUtils.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        if (ctClass.getName().endsWith("DeviceUtil")) {
            CtClass[] params = new CtClass[]{
                    pool.get(String.class.getName())
            };

            CtMethod getMacAddr = ctClass.getDeclaredMethod("getMacAddr", params);
            mProject.getLogger().error("getMacAddr:" + getMacAddr);

            getMacAddr.setBody(injectMethodBody(getMacAddr.getLongName()));
        } else {
            CtMethod getMac = ctClass.getDeclaredMethod("getMac");
            mProject.getLogger().error("getMac:" + getMac);

            getMac.setBody(injectMethodBody(getMac.getLongName()));
        }
        return ctClass;
    }
}
