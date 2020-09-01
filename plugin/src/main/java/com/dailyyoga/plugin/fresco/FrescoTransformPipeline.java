package com.dailyyoga.plugin.fresco;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.dailyyoga.plugin.TransformPipeline;

import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 19:57
 * @description:
 */
public class FrescoTransformPipeline extends TransformPipeline {

    private FrescoJar mFrescoJar;

    public FrescoTransformPipeline(Project project) {
        super(project);
    }

    @Override
    public void directoryInputs(DirectoryInput directoryInput) {

    }

    @Override
    public void jarInputs(JarInput jarInput, File dest) {
        try {
            if (isFrescoJar(jarInput.getFile())) {
                mProject.getLogger().error(jarInput.getName() + "-" + jarInput.getFile().getAbsolutePath());
                mFrescoJar = FrescoJar.create(jarInput, dest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process() {
        if (mFrescoJar != null) mFrescoJar.process(mProject);
    }

    public static boolean isFrescoJar(File file) throws IOException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> enumeration = jarFile.entries();

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }
            String entryName = jarEntry.getName();

            if (isSimpleDraweeViewClass(entryName)) {
                return true;
            }
        }
        jarFile.close();
        return false;
    }

    public static boolean isSimpleDraweeViewClass(String name) {
        //只处理需要的class文件
        return "com/facebook/drawee/view/SimpleDraweeView.class" == name;
    }

}
