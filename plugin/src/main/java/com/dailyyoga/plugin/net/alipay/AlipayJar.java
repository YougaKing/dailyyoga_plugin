package com.dailyyoga.plugin.net.alipay;

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
public class AlipayJar extends InjectJar {

    public AlipayJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/alipay/security/mobile/module/deviceinfo/b.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod b = ctClass.getDeclaredMethod("B");
        mProject.getLogger().error("b:" + b);

        b.setBody(injectMethodBody(b.getLongName()));

        return ctClass;
    }

}
