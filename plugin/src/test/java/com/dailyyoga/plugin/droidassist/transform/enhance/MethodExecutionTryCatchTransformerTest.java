package com.dailyyoga.plugin.droidassist.transform.enhance;

import com.dailyyoga.plugin.droidassist.DroidAssistConfigurationTest;
import com.dailyyoga.plugin.droidassist.DroidAssistContext.DailyyogaMIITClassPool;
import com.dailyyoga.plugin.droidassist.transform.Transformer;
import com.dailyyoga.plugin.droidassist.util.Logger;
import com.dailyyoga.plugin.droidassist.util.ZipUtils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 3/10/21 5:31 PM
 * @description:
 */
public class MethodExecutionTryCatchTransformerTest {

    @Test
    public void execute() {
        DroidAssistConfigurationTest configurationTest = new DroidAssistConfigurationTest();

        for (Transformer transformer : configurationTest.parse()) {
            if (transformer instanceof MethodExecutionTryCatchTransformer) {
                execute((MethodExecutionTryCatchTransformer) transformer);
            }
        }
    }

    public void execute(MethodExecutionTryCatchTransformer transformer) {
        DailyyogaMIITClassPool classPool = new DailyyogaMIITClassPool();
        transformer.setClassPool(classPool);

        File liteavsdkJarFile = new File("../app/libs/liteavsdk.jar");
        classPool.appendClassPath(liteavsdkJarFile);

        File temporaryDir = new File("../build/");
        Logger.init(Logger.LEVEL_CONSOLE, temporaryDir.getAbsoluteFile());

        ZipUtils.collectAllClassesFromJar(liteavsdkJarFile).forEach(className -> {
            if ("com.tencent.liteav.basic.log.TXCLog".equals(className)) {
                System.out.println("className:" + className);
                executeClass(classPool, transformer, className, temporaryDir);
            }
        });
    }

    boolean executeClass(DailyyogaMIITClassPool classPool,
                         MethodExecutionTryCatchTransformer transformer,
                         String className, File directory) {

        CtClass inputClass = classPool.getOrNull(className);
        if (inputClass == null) {
            return false;
        }
        try {
            boolean written = transformer.performTransform(inputClass, className);
            System.out.println("written:" + written + "--directory:" + directory.getAbsolutePath());
            inputClass.writeFile(directory.getAbsolutePath());
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}