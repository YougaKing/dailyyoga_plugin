package com.dailyyoga.plugin.net.qiyukf;

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
public class QiyukfJar extends InjectJar {

    public QiyukfJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/qiyukf/unicorn/f/a/f/v.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod a = ctClass.getDeclaredMethod("a");
        mProject.getLogger().error("a:" + a);

        a.setBody(injectMethodBody(a.getLongName()));

        return ctClass;
    }

}
