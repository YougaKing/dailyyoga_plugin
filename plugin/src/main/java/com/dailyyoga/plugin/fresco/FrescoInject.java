package com.dailyyoga.plugin.fresco;


import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2018/04/26 12:13
 * @description:
 */
public class FrescoInject {

    static void processJar(File originFile, File tempFile, Project project) throws NotFoundException, IOException, CannotCompileException {
        ClassPool.getDefault().appendClassPath(originFile.getAbsolutePath());
        JarFile jarFile = new JarFile(originFile);
        Enumeration enumeration = jarFile.entries();
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tempFile));
        //用于保存
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement();
            String entryName = jarEntry.getName();
            ZipEntry zipEntry = new ZipEntry(entryName);
            InputStream inputStream = jarFile.getInputStream(jarEntry);

            //插桩class
            if (FrescoTransformPipeline.isSimpleDraweeViewClass(entryName)) {
                //class文件处理
                project.getLogger().error(entryName);

                jarOutputStream.putNextEntry(zipEntry);

                ClassPool pool = ClassPool.getDefault();
                CtClass ctClass = pool.makeClass(inputStream, false);
                ctClass = injectSimpleDraweeViewClass(ctClass, project);

                jarOutputStream.write(ctClass.toBytecode());
            } else {
                jarOutputStream.putNextEntry(zipEntry);
                jarOutputStream.write(IOUtils.toByteArray(inputStream));
            }
            jarOutputStream.closeEntry();
        }
        //结束
        jarOutputStream.close();
        jarFile.close();
        ClassPool.getDefault().clearImportedPackages();
    }


    private static CtClass injectSimpleDraweeViewClass(CtClass simpleDraweeView, Project project) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        if (simpleDraweeView.isFrozen()) simpleDraweeView.defrost();
        simpleDraweeView.stopPruning(true);

        CtClass[] params = new CtClass[]{
                pool.get("android.content.Context"),
                pool.get("android.util.AttributeSet")
        };

        CtMethod init = simpleDraweeView.getDeclaredMethod("init", params);
        project.getLogger().error("init:" + init);
        init.insertBefore("cdn.youga.instrument.MediaPlayerInstrument.setAVOptions(\\$1);");

        return simpleDraweeView;
    }
}