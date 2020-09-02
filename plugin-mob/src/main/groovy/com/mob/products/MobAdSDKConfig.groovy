package com.mob.products

import com.mob.ConfigCreator
import com.mob.products.mobadsdk.MobAdDevInfo

class MobAdSDKConfig extends MobProductConfig {

    ConfigCreator config
    int versionCode
    boolean auto  = true
    MobAdDevInfo infos

    void setConfigCreator(ConfigCreator config) {
        this.config = config
        infos = new MobAdDevInfo()
    }

    def methodMissing(String name, Object args) {
        if ("devInfo".equalsIgnoreCase(name) || "devInfos".equalsIgnoreCase(name)
                || "platform".equalsIgnoreCase(name) || "platforms".equalsIgnoreCase(name)
                || "plat".equalsIgnoreCase(name) || "plats".equalsIgnoreCase(name)
                || "info".equalsIgnoreCase(name) || "infos".equalsIgnoreCase(name)) {
            infos.configCreator = config
            infos.sdkVersion = versionCode
            infos.closure = args[0]
        } else if("provider".equalsIgnoreCase(name) || "Provider".equalsIgnoreCase(name)
                || "providers".equalsIgnoreCase(name) || "Providers".equalsIgnoreCase(name)){
            this.auto = Boolean.valueOf(String.valueOf(args[0]))
            if(!auto){
                deleteConfig()
            }
            infos.setProvider(auto)

        } else if("version".equalsIgnoreCase(name)){
            String ver = null
            if (args == null) {
                ver = null
            } else if (args.length == 0) {
                ver = null
            } else {
                ver = args[0] == null ? null : String.valueOf(args[0])
            }
            versionCode = getVerCode(ver)
            if(versionCode != 0 && versionCode < 20003){
                deleteConfig()
            }
        }
        return super.methodMissing(name, args)
    }
    
    Set getPermission() {
        return [
                "android.permission.REQUEST_INSTALL_PACKAGES",
                "android.permission.WAKE_LOCK"
        ]
    }

    public void addManifestConfig(){
        String applicationId = '${applicationId}'
        config.directToAdd.add("""
            <provider
                xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                android:name="com.mob.adsdk.MobADFileProvider"
                android:authorities="${applicationId}.MobADFileProvider"
                android:exported="false"
                android:grantUriPermissions="true">
                <meta-data
                    android:name="android.support.FILE_PROVIDER_PATHS"
                    android:resource="@xml/mobad_file_paths" />
            </provider>
        """)
    }

    private void deleteConfig(){
        for (Object o : config.directToAdd){
            if(o != null && o.toString().contains("MobADFileProvider")){
                config.directToAdd.remove(o)
            }
        }
    }

    private int getVerCode(String ver){
        if (ver == null || "+".equals(ver)) {
            return 0
        } else {
            int code = 0
            String[] parts = ver.split("\\.")
            for (int i = 0; i < 3; i++) {
                code = code * 100 + Integer.parseInt(parts[i])
            }
            return code
        }
    }
}