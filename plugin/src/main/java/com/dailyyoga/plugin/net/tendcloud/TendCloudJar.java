package com.dailyyoga.plugin.net.tendcloud;

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
public class TendCloudJar extends InjectJar {

    public TendCloudJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/tendcloud/tenddata/q.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        CtMethod[] ctMethods = ctClass.getDeclaredMethods("b");
        for (CtMethod ctMethod : ctMethods) {
            if (ctMethod.getReturnType() == pool.get(String.class.getName()) &&
                    (ctMethod.getParameterTypes() == null || ctMethod.getParameterTypes().length == 0)) {
                CtMethod b = ctMethod;
                mProject.getLogger().error("b:" + b);
                b.setBody(injectMethodBody(b.getLongName()));
            }
        }

        return ctClass;
    }

}
