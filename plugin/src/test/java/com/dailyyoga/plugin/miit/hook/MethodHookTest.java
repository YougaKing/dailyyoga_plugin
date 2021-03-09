package com.dailyyoga.plugin.miit.hook;

import com.dailyyoga.plugin.droidassist.ex.DroidAssistBadStatementException;
import com.dailyyoga.plugin.droidassist.util.Logger;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    private MethodSpec sourceSpec;
    private MethodSpec targetSpec;

    @Test
    public void methodCall() {

        String[] parameters = new String[]{
                "java.lang.Object",
                "java.lang.Object[]"
        };
        sourceSpec = new MethodSpec(
                "java.lang.reflect.Method",
                "java.lang.Object",
                "invoke",
                parameters
        );

        parameters = new String[]{
                "java.lang.String",
                "java.lang.reflect.Method",
                "java.lang.Object",
                "java.lang.Object[]"
        };

        targetSpec = new MethodSpec(
                "com.dailyyoga.plugin.miit.hook.MethodHookTest",
                "java.lang.Object",
                "invoke",
                parameters
        );

        checkSourceTarget("");

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
                    if (m.getClassName().equals(getSource().getDeclaring())
                            && m.getMethodName().equals(getSource().getName())) {
                        try {
                            CtMethod ctMethod = m.getMethod();
                            System.out.println("命中:" + m.where().getLongName());

                            for (int i = 0; i < ctMethod.getParameterTypes().length; i++) {
                                CtClass ctClass = ctMethod.getParameterTypes()[i];
                                System.out.println("参数:" + i + "--" + ctClass.getName());
                            }

                            String replacement = replaceInstrument(m.where().getLongName(), m);

                            System.out.println("replacement:" + replacement);
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

    public void checkSourceTarget(String node) {
        if (getSource() == null) {
            throw new IllegalArgumentException("Empty source " + node);
        }
        if (getTarget() == null) {
            throw new IllegalArgumentException("Empty target " + node);
        }
        if (getSource().isStatic() != getTarget().isStatic()) {
            throw new IllegalArgumentException("Different isStatic source/target " + node);
        }
        if (!getSource().getReturnType().equals(getTarget().getReturnType())) {
            throw new IllegalArgumentException("Different returnType source/target " + node);
        }
        int dValue = Math.abs(getSource().isStatic() ? -1 : -2);
        int result = Math.abs(getSource().getParameters().length - getTarget().getParameters().length);
        int offset = result - dValue;

        System.out.println("checkSourceTarget:" + getSource().getParameters().length + "--"
                + getTarget().getParameters().length
                + "--dValue:" + dValue
                + "--result:" + result
                + "--offset:" + offset
        );

        if (offset != 0) {
            throw new IllegalArgumentException("Different parameters source/target " + node);
        }
        if (!String.class.getName().equals(getTarget().getParameters()[0])) {
            throw new IllegalArgumentException("Target parameters [0] must " + String.class.getName() + " " + node);
        }
        for (int i = 0; i < getSource().getParameters().length; i++) {
            String sourceParameter = getSource().getParameters()[i];
            String targetParameter = getTarget().getParameters()[i + dValue];
            if (!sourceParameter.equals(targetParameter)) {
                throw new IllegalArgumentException("Different parameters source/target [" + i + "] " + node);
            }
        }
    }

    protected String replaceInstrument(
            String callMethodName,
            MethodCall methodCall) throws NotFoundException {

        String statement = getTarget().getName();
        String replacement = getReplaceStatement(callMethodName, methodCall);
        try {
            String s = replacement.replaceAll("\n", "");
            methodCall.replace(s);
        } catch (CannotCompileException e) {
            Logger.error("Replace method call instrument error with statement: " + statement + "\n", e);
            throw new DroidAssistBadStatementException(e);
        }
        return replacement;
    }

    protected String getReplaceStatement(
            String callMethodName,
            MethodCall methodCall) throws NotFoundException {

        StringBuilder builder = new StringBuilder();
        builder.append(getTarget().getDeclaring())
                .append(".")
                .append(getTarget().getName())
                .append("(\"")
                .append(callMethodName)
                .append("\"");

        if (!getSource().isStatic()) {
            builder.append(",")
                    .append("$0");
        }
        for (int i = 0; i < getSource().getParameters().length; i++) {
            builder.append(",")
                    .append("$")
                    .append(i + 1);
        }
        builder.append(");");
        return getReplaceStatement(builder.toString());
    }

    private String getReplaceStatement(String content) {
        return "{ $_ = " + content + "}";
    }

    public MethodSpec getSource() {
        return sourceSpec;
    }

    public MethodSpec getTarget() {
        return targetSpec;
    }

    public static Object invoke(String preArgs, Method method, Object obj, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}