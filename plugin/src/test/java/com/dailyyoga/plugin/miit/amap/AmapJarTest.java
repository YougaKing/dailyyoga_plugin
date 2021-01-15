package com.dailyyoga.plugin.miit.amap;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Enumeration;

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
public class AmapJarTest {

    @Test
    public void methodCall() {

        File file = new File("/Users/youga/StudioProjects/android_public/dailyyoga_plugin/plugin/NetworkInterfaceTransform.class");
        try {
            ClassPool.getDefault().appendClassPath(file.getAbsolutePath());
            ClassPool.getDefault().appendClassPath(NetworkInterfaceTransform.class.getName());

            System.out.println("methodCall");

            ClassPool pool = ClassPool.getDefault();
            CtClass ctClass = pool.makeClass(new FileInputStream(file), false);
            if (ctClass.isFrozen()) ctClass.defrost();
            ctClass.stopPruning(true);

            CtClass[] params = new CtClass[]{
                    ClassPool.getDefault().get(String.class.getName())
            };
            CtMethod listNetworkHardware = ctClass.getDeclaredMethod("listNetworkHardware", params);
            listNetworkHardware.instrument(new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    System.out.println("getClassName:" + m.getClassName() + "--getMethodName:" + m.getMethodName());
                    if (m.getClassName().equals("java.net.NetworkInterface")) {
                        try {
                            CtMethod ctMethod = m.getMethod();
                            System.out.println("命中:" + ctMethod.toString());

                            if (ctMethod.getName().equals("getNetworkInterfaces")
                                    && ctMethod.getModifiers() == Modifier.STATIC
                                    && (ctMethod.getParameterTypes().length == 0)
                                    && ctMethod.getReturnType() == ClassPool.getDefault().get(Enumeration.class.getName())) {
                                m.replace("{ $_ = " + injectMethodBody(listNetworkHardware.getLongName()) + "}");
                            } else if (ctMethod.getName().equals("getName")
                                    && (ctMethod.getParameterTypes().length == 0)
                                    && ctMethod.getReturnType() == ClassPool.getDefault().get(String.class.getName())) {

                                m.replace("{ $_ = " + getName(listNetworkHardware.getLongName()) + "}");
                            }
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
//            listNetworkHardware.setBody("{ return null ;}");

            File newFile = new File("/Users/youga/StudioProjects/android_public/dailyyoga_plugin/plugin/NetworkInterfaceTransformNew.class");
            FileOutputStream outputStream = new FileOutputStream(newFile);
            outputStream.write(ctClass.toBytecode());
        } catch (CannotCompileException | IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    public String injectMethodBody(String args) {
        return "com.dailyyoga.plugin.net.amap.NetworkInterfaceTransform.getNetworkInterfaces(\"" + args + "\");";
    }

    public String getName(String args) {
        return "com.dailyyoga.plugin.net.amap.NetworkInterfaceTransform.getName(\"" + args + "\",$0);";
    }
}