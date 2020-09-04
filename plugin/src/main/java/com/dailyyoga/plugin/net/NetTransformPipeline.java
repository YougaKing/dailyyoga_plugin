package com.dailyyoga.plugin.net;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.gradle.AppExtension;
import com.dailyyoga.plugin.TransformPipeline;
import com.dailyyoga.plugin.net.alipay.AlipayJar;
import com.dailyyoga.plugin.net.amap.AmapJar;
import com.dailyyoga.plugin.net.hyphenate.HyphenateJar;
import com.dailyyoga.plugin.net.lebo.LeBoJar;
import com.dailyyoga.plugin.net.mob.MobJar;
import com.dailyyoga.plugin.net.qiyukf.QiyukfJar;
import com.dailyyoga.plugin.net.sensors.SensorsJar;
import com.dailyyoga.plugin.net.tendcloud.TendCloudJar;
import com.dailyyoga.plugin.net.uppay.UppayJar;
import com.dailyyoga.plugin.net.xiaomi.XiaoMiJar;

import org.apache.commons.io.FileUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 20:11
 * @description:
 */
public class NetTransformPipeline extends TransformPipeline {


    private List<InjectJar> mInjectJarList = new ArrayList<>();
    private AppExtension mAndroid;
    private boolean mAppendAndroidJar;

    public NetTransformPipeline(Project project) {
        super(project);
        mInjectJarList.add(new LeBoJar(mProject));
        mInjectJarList.add(new AlipayJar(mProject));
        mInjectJarList.add(new AmapJar(mProject));
        mInjectJarList.add(new MobJar(mProject));
        mInjectJarList.add(new QiyukfJar(mProject));
        mInjectJarList.add(new SensorsJar(mProject));
        mInjectJarList.add(new TendCloudJar(mProject));
        mInjectJarList.add(new XiaoMiJar(mProject));
        mInjectJarList.add(new HyphenateJar(mProject));
        mInjectJarList.add(new UppayJar(mProject));

        mAndroid = project.getExtensions().getByType(AppExtension.class);
    }

    @Override
    public void directoryInputs(DirectoryInput directoryInput) {

    }

    @Override
    public void jarInputs(JarInput jarInput, File dest) {
        try {
            boolean matching = false;
            for (InjectJar injectJar : mInjectJarList) {
                if (isTargetJar(jarInput.getFile(), injectJar)) {
                    matching = true;
                    mProject.getLogger().error(jarInput.getName() + "-" + jarInput.getFile().getAbsolutePath());
                    injectJar.createInjectClass(jarInput, dest);
                }
            }

            if (!matching) {
                try {
                    ClassPool.getDefault().appendClassPath(jarInput.getFile().getAbsolutePath());
                    FileUtils.copyFile(jarInput.getFile(), dest);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process() {
        appendAndroidJar();

        for (InjectJar injectJar : mInjectJarList) {
            injectJar.process();
        }
    }

    private void appendAndroidJar() {
        try {
            if (mAppendAndroidJar) return;
            //android.jar
            ClassPool.getDefault().appendClassPath(mAndroid.getBootClasspath().get(0).toString());
            mAppendAndroidJar = true;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isTargetJar(File file, InjectJar injectJar) throws IOException {
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> enumeration = jarFile.entries();

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = enumeration.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }
            String entryName = jarEntry.getName();

            if (injectJar.isTargetClass(entryName)) {
                mProject.getLogger().error("isTargetClass:" + entryName);
                return true;
            }
        }
        jarFile.close();
        return false;
    }

}
