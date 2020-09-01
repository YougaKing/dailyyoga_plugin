package com.dailyyoga.plugin.fresco;

import com.android.build.api.transform.JarInput;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class FrescoJar {

    private File frescoJarOriginFile;
    private File frescoJarTempFile;
    private File frescoJarDestFile;

    FrescoJar(File frescoJarOriginFile, File frescoJarTempFile, File frescoJarDestFile) {
        this.frescoJarOriginFile = frescoJarOriginFile;
        this.frescoJarTempFile = frescoJarTempFile;
        this.frescoJarDestFile = frescoJarDestFile;
    }


    public void process(Project project) {

        try {
            FrescoInject.processJar(frescoJarOriginFile, frescoJarTempFile, project);
            FileUtils.copyFile(frescoJarTempFile, frescoJarDestFile);
            frescoJarTempFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static FrescoJar create(JarInput jarInput, File dest) {
        String jarName = jarInput.getFile().getName();
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4);
        }
        File frescoJarOriginFile = jarInput.getFile();
        File frescoJarTempFile = new File(dest.getParent() + File.separator + jarName + ".jar");
        File frescoJarDestFile = dest;

        FrescoJar frescoJar = new FrescoJar(frescoJarOriginFile, frescoJarTempFile, frescoJarDestFile);
        //避免上次的缓存被重复插入
        if (frescoJarTempFile.exists()) {
            frescoJarTempFile.delete();
        }
        return frescoJar;
    }


}