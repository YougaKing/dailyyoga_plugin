package com.dailyyoga.plugin.net.xiaomi;

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
public class XiaoMiJar extends InjectJar {

    public XiaoMiJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/xiaomi/push/mpcd/job/h.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod g = ctClass.getDeclaredMethod("g");
        mProject.getLogger().error("g:" + g);

        g.setBody(injectMethodBody(g.getLongName()));

        return ctClass;
    }

}
