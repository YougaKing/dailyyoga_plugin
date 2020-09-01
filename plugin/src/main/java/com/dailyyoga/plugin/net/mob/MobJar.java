package com.dailyyoga.plugin.net.mob;

import com.dailyyoga.plugin.net.InjectJar;

import org.gradle.api.Project;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/9/1 09:12
 * @description:
 */
public class MobJar extends InjectJar {

    public MobJar(Project project) {
        super(project);
    }

    @Override
    public boolean isTargetClass(String entryName) {
        return "com/mob/tools/utils/DeviceHelper.class".equals(entryName) ||
                "com/mob/MobProvider.class".equals(entryName) ||
                "cn/sharesdk/framework/utils/ShareSDKFileProvider.class".equals(entryName);
    }

    @Override
    public CtClass injectTargetClass(CtClass ctClass) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();

        mProject.getLogger().error("ctClass.getName():" + ctClass.getName());

        if (ctClass.getName().endsWith("DeviceHelper")) {

            CtClass[] params = new CtClass[]{
                    pool.get(String.class.getName())
            };
            CtMethod getHardwareAddressFromShell = ctClass.getDeclaredMethod("getHardwareAddressFromShell", params);
            mProject.getLogger().error("getHardwareAddressFromShell:" + getHardwareAddressFromShell);
            getHardwareAddressFromShell.setBody(injectMethodBody(getHardwareAddressFromShell.getLongName()));

            CtMethod getCurrentNetworkHardwareAddress = ctClass.getDeclaredMethod("getCurrentNetworkHardwareAddress");
            mProject.getLogger().error("getCurrentNetworkHardwareAddress:" + getCurrentNetworkHardwareAddress);
            getCurrentNetworkHardwareAddress.setBody(injectMethodBody(getCurrentNetworkHardwareAddress.getLongName()));

            CtMethod listNetworkHardware = ctClass.getDeclaredMethod("listNetworkHardware");
            mProject.getLogger().error("listNetworkHardware:" + listNetworkHardware);
            listNetworkHardware.setBody(injectListNetworkHardwareMethodBody(listNetworkHardware.getLongName()));

            CtMethod getLocalIpInfo = ctClass.getDeclaredMethod("getLocalIpInfo");
            mProject.getLogger().error("getLocalIpInfo:" + getLocalIpInfo);
            getLocalIpInfo.setBody(injectGetLocalIpInfoMethodBody(getLocalIpInfo.getLongName()));

        } else if (ctClass.getName().endsWith("MobProvider")) {
            CtMethod onCreate = ctClass.getDeclaredMethod("onCreate");
            mProject.getLogger().error("onCreate:" + onCreate);
            onCreate.setBody(injectOnCreateMethodBody(onCreate.getLongName()));
        } else {
            CtMethod onCreate = ctClass.getDeclaredMethod("onCreate");
            mProject.getLogger().error("onCreate:" + onCreate);
            onCreate.setBody(injectOnCreateMethodBody(onCreate.getLongName()));
        }
        return ctClass;

    }
}