package com.dailyyoga.plugin.pldroid;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.dailyyoga.plugin.TransformPipeline;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 20:00
 * @description:
 */
public class PldroidTransformPipeline extends TransformPipeline {

    private PldroidJar mPldroidJar;

    public PldroidTransformPipeline(Project project) {
        super(project);
    }

    @Override
    public void directoryInputs(DirectoryInput directoryInput) {

    }

    @Override
    public void jarInputs(JarInput jarInput, File dest) {
        try {
            if (isPldroidJar(jarInput.getFile())) {
                mProject.getLogger().error(jarInput.getName() + "-" + jarInput.getFile().getAbsolutePath());
                mPldroidJar = PldroidJar.create(jarInput, dest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process() {
        if (mPldroidJar != null) {
            try {
                mPldroidJar.process(mProject);
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (CannotCompileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isPldroidJar(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> enumeration = jarFile.entries();

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }
            String entryName = jarEntry.getName();

            if (isMediaPlayerClass(entryName)) {
                return true;
            }
        }
        jarFile.close();
        return false;
    }

    public static boolean isMediaPlayerClass(String name) {
        //只处理需要的class文件
        return "com/qiniu/qplayer/mediaEngine/MediaPlayer.class" == name;
    }
}
