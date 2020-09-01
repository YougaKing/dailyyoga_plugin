package com.dailyyoga.plugin.net.amap;

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
public class AmapJar extends InjectJar {

    public AmapJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/loc/p.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod e = ctClass.getDeclaredMethod("e");
        mProject.getLogger().error("e:" + e);
        e.setBody(injectMethodBody(e.getLongName()));

        return ctClass;
    }

}
