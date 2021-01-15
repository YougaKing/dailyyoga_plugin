package com.dailyyoga.plugin.miit.hook;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 1/11/21 2:13 PM
 * @description:
 */
public class MethodHookTest {

    @Test
    public void methodCall() {

        File file = new File("/Users/youga/StudioProjects/android_public/dailyyoga_plugin/plugin/MIITMethodTransform.class");
        try {
            ClassPool.getDefault().appendClassPath(file.getAbsolutePath());

            System.out.println("methodCall");

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(new FileInputStream(file), false);
            if (ctClass.isFrozen()) ctClass.defrost();
            ctClass.stopPruning(true);

            CtMethod invoke = ctClass.getDeclaredMethod("invoke");
            invoke.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    System.out.println("getClassName:" + m.getClassName() + "--getMethodName:" + m.getMethodName());
                    if (m.getClassName().equals("java.lang.reflect.Method")) {
                        try {
                            CtMethod ctMethod = m.getMethod();
                            System.out.println("命中:" + ctMethod.toString());

                            for (int i = 0; i < ctMethod.getParameterTypes().length; i++) {
                                CtClass ctClass = ctMethod.getParameterTypes()[i];
                                System.out.println("命中:" + i + "--" + ctClass.getName());
                            }
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            File newFile = new File("/Users/youga/StudioProjects/android_public/dailyyoga_plugin/plugin/MIITMethodTransformNew.class");
            FileOutputStream outputStream = new FileOutputStream(newFile);
            outputStream.write(ctClass.toBytecode());
        } catch (CannotCompileException | IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }
}