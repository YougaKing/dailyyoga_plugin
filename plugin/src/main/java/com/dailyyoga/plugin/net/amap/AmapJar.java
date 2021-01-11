package com.dailyyoga.plugin.net.amap;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

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

        CtMethod[] ctMethods = ctClass.getDeclaredMethods("e");
        ctMethods.toString();

        for (CtMethod ctMethod : ctMethods) {
            if (ctMethod.getReturnType() == pool.get(String.class.getName()) &&
                    (ctMethod.getParameterTypes() == null || ctMethod.getParameterTypes().length == 0)) {
                CtMethod e = ctMethod;
                mProject.getLogger().error("e:" + e);
//                e.setBody(injectMethodBody(e.getLongName()));
                e.instrument(new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        mProject.getLogger().error("getClassName:" + m.getClassName() + "--getMethodName:" + m.getMethodName());
                        if (m.getClassName().equals("java.net.NetworkInterface") && m.getMethodName().equals("getMethodName")) {
                            m.replace("{ $_ = " + injectMethodBody(e.getLongName()) + "}");
                        }
                    }
                });
            }
        }

        return ctClass;
    }

}
