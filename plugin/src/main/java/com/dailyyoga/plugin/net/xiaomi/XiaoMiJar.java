package com.dailyyoga.plugin.net.xiaomi;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 22:05
 * @description:
 */
public class XiaoMiJar extends InjectJar {

    public XiaoMiJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/xiaomi/push/mpcd/job/h.class".equals(entryName) ||
                "com/mi/milink/sdk/base/os/info/DeviceDash.class".equals(entryName) ||
                "com/xiaomi/accountsdk/hasheddeviceidlib/HardwareInfo.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        if (ctClass.getName().endsWith("DeviceDash")) {
            CtMethod getMacFromHardware = ctClass.getDeclaredMethod("getMacFromHardware");
            mProject.getLogger().error("g:" + getMacFromHardware);

            getMacFromHardware.setBody(injectMethodBody(getMacFromHardware.getLongName()));
        } else if (ctClass.getName().endsWith("HardwareInfo")) {
            CtMethod a = ctClass.getDeclaredMethod("a");
            mProject.getLogger().error("g:" + a);

            a.setBody(injectMethodBody(a.getLongName()));
        } else if (ctClass.getName().endsWith("h")) {
            CtMethod g = ctClass.getDeclaredMethod("g");
            mProject.getLogger().error("g:" + g);

            g.setBody(injectMethodBody(g.getLongName()));
        }
        return ctClass;
    }

}
