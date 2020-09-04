package com.dailyyoga.plugin.net.uppay;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: zhaojiaxing@gmail.com
 * @created on: 2020/9/4 22:05
 * @description:
 */
public class UppayJar extends InjectJar {

    public UppayJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/unionpay/mobile/android/utils/f.class".equals(entryName) ||
                "com/unionpay/utils/e.class".equals(entryName) ;
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());
        if (ctClass.getName().endsWith("f")) {
            CtClass[] params = new CtClass[]{
                    pool.get(String.class.getName())
            };

            CtMethod a = ctClass.getDeclaredMethod("a", params);
            mProject.getLogger().error("f.a:" + a);
            a.setBody(injectMethodBody(a.getLongName()));
        } else {
            CtClass[] params = new CtClass[]{
                    pool.get(String.class.getName())
            };

            CtMethod a = ctClass.getDeclaredMethod("a", params);
            mProject.getLogger().error("e.a:" + a);
            a.setBody(injectMethodBody(a.getLongName()));
        }
        return ctClass;
    }

}
