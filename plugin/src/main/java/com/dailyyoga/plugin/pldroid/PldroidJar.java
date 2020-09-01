package com.dailyyoga.plugin.pldroid;


import com.android.build.api.transform.JarInput;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.NotFoundException;

public class PldroidJar {

    private File pldroidJarOriginFile;
    private File pldroidJarTempFile;
    private File pldroidJarDestFile;

    PldroidJar(File pldroidJarOriginFile, File pldroidJarTempFile, File pldroidJarDestFile) {
        this.pldroidJarOriginFile = pldroidJarOriginFile;
        this.pldroidJarTempFile = pldroidJarTempFile;
        this.pldroidJarDestFile = pldroidJarDestFile;
    }


    public void process(Project project) throws NotFoundException, CannotCompileException, IOException {
        PldroidInject.processJar(pldroidJarOriginFile, pldroidJarTempFile, project);

        FileUtils.copyFile(pldroidJarTempFile, pldroidJarDestFile);
        pldroidJarTempFile.delete();
    }

    static PldroidJar create(JarInput jarInput, File dest) {
        String jarName = jarInput.getFile().getName();
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4);
        }
        File pldroidJarOriginFile = jarInput.getFile();
        File pldroidJarTempFile = new File(dest.getParent() + File.separator + jarName + ".jar");
        File pldroidJarDestFile = dest;

        PldroidJar pldroidJar = new PldroidJar(pldroidJarOriginFile, pldroidJarTempFile, pldroidJarDestFile);
        //避免上次的缓存被重复插入
        if (pldroidJarTempFile.exists()) {
            pldroidJarTempFile.delete();
        }
        return pldroidJar;
    }


}