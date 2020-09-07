package com.dailyyoga.plugin.net;

import com.android.build.api.transform.JarInput;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 22:09
 * @description:
 */
public abstract class InjectJar {

    protected Project mProject;
    protected List<InjectRealJar> mInjectRealJars = new ArrayList<>();

    public InjectJar(Project project) {
        mProject = project;
    }

    public abstract boolean isTargetClass(String entryName);

    public abstract CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException;

    public void processJar(InjectRealJar realJar) throws NotFoundException, IOException, CannotCompileException {
        JarFile jarFile = new JarFile(realJar.mOriginFile);
        Enumeration enumeration = jarFile.entries();
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(realJar.mTempFile));
        //用于保存
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement();
            String entryName = jarEntry.getName();
            ZipEntry zipEntry = new ZipEntry(entryName);
            InputStream inputStream = jarFile.getInputStream(jarEntry);

            //插桩class
            if (isTargetClass(entryName)) {
                //class文件处理
                mProject.getLogger().error(entryName);

                jarOutputStream.putNextEntry(zipEntry);

                ClassPool pool = ClassPool.getDefault();
                CtClass ctClass = pool.makeClass(inputStream, false);
                if (ctClass.isFrozen()) ctClass.defrost();
                ctClass.stopPruning(true);
                ctClass = injectTargetClass(ctClass);

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

    public void process() throws Exception{
        for (InjectRealJar realJar : mInjectRealJars) {
            process(realJar);
        }
    }

    public void process(InjectRealJar realJar) throws Exception {
            if (realJar.unAvailable()) return;
            processJar(realJar);
            if (realJar.mTempFile.getCanonicalPath().equals(realJar.mDestFile.getCanonicalPath())) {
                return;
            }
            FileUtils.copyFile(realJar.mTempFile, realJar.mDestFile);
            realJar.mTempFile.delete();
    }

    public void createInjectClass(JarInput jarInput, File dest) {
        String jarName = jarInput.getFile().getName();
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4);
        }
        File originFile = jarInput.getFile();

        try {
            ClassPool.getDefault().appendClassPath(originFile.getAbsolutePath());
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        File tempFile = new File(dest.getParent() + File.separator + jarName + ".jar");
        File destFile = dest;

        //避免上次的缓存被重复插入
        if (tempFile.exists()) {
            tempFile.delete();
        }
        InjectRealJar realJar = new InjectRealJar(originFile, tempFile, destFile);
        mInjectRealJars.add(realJar);
    }

    public String injectMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.getNetworkInterfaces(\"" + args + "\");";
    }

    public String injectGetMacAddrMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.getMacAddr($1,\"" + args + "\");";
    }

    public String injectGetMacMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.getMac(\"" + args + "\");";
    }

    public String injectListNetworkHardwareMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.listNetworkHardware(\"" + args + "\");";
    }

    public String injectGetLocalIpInfoMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.getLocalIpInfo(\"" + args + "\");";
    }

    public String injectGetDeviceInfoMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.getDeviceInfo(\"" + args + "\");";
    }

    public String injectOnCreateMethodBody(String args) {
        return "return com.dailyyoga.plugin.net.NetworkInterfaceTransform.onCreate(\"" + args + "\");";
    }

    public String injectInitMethodBody(String args) {
        return "com.dailyyoga.plugin.net.NetworkInterfaceTransform.init(\"" + args + "\");";
    }
}
