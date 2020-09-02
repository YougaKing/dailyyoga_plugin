package com.mob.products.mobadsdk

import com.mob.ConfigCreator
import com.mob.DObject

class MobAdDevInfo extends DObject{

    static Set platforms
    ConfigCreator config
    int versionCode
    boolean isAuto = true
    //GG和GDT平台是否存在，0不存在，1存在广告点通，2存在GG， 3都存在
    int ggGDTStatus = 0

    static {
        platforms = [
                "GDT", "CSJ", "KS", "BD", "GG"
        ]
    }

    void setConfigCreator(ConfigCreator config) {
        this.config = config
    }

    void setSdkVersion(int versionCode) {
        this.versionCode = versionCode
    }

    void setProvider(boolean isAuto) {
        this.isAuto = isAuto
    }

    def methodMissing(String name, def args) {
        String plat = platforms.find {
            return name.equalsIgnoreCase(it)
        }
        if (plat != null) {
            MobAdInfoNode info = new MobAdInfoNode()
            info.closure = args[0]
            addActivity(plat, info)
            addDependency(plat, info)
        }
    }

    private void addActivity(String name, MobAdInfoNode info) {
        if (!isAuto) {
            return;
        }
        String applicationId = '${applicationId}'
        String libraryVersion = info.Version
        int pluginVerCode = getPluginVerCode(libraryVersion)
        String gdtProvider = """
                    <provider
                        xmlns:android="http://schemas.android.com/apk/res/android"
                        xmlns:tools="http://schemas.android.com/tools"
                        android:name="android.support.v4.content.FileProvider"
                        android:authorities="${applicationId}.fileprovider"
                        android:exported="false"
                        android:grantUriPermissions="true">
                        <meta-data
                            android:name="android.support.FILE_PROVIDER_PATHS"
                            android:resource="@xml/mobad_file_paths" />
                    </provider>
                """
        if ((versionCode != 0 && versionCode < 20003) ||
                ((versionCode == 0 || versionCode >= 20003) && "GDT".equals(name) && isEnable(info))) {
            if (ggGDTStatus == 2 || ggGDTStatus == 3) {
                //如果集成了GG，则需要更新为androidx
                ggGDTStatus = 3
                updateGDT(applicationId, gdtProvider)
            } else {
                ggGDTStatus = 1
                config.directToAdd.add(gdtProvider)
            }
        }
        if ("CSJ".equals(name) && isEnable(info)) {
            config.directToAdd.add("""
                    <provider
                        xmlns:android="http://schemas.android.com/apk/res/android"
                        xmlns:tools="http://schemas.android.com/tools"
                        android:name="com.bytedance.sdk.openadsdk.TTFileProvider"
                        android:authorities="${applicationId}.TTFileProvider"
                        android:exported="false"
                        android:grantUriPermissions="true">
                        <meta-data
                            android:name="android.support.FILE_PROVIDER_PATHS"
                            android:resource="@xml/mobad_file_paths" />
                    </provider>
            """)
            config.directToAdd.add("""
                    <provider
                        xmlns:android="http://schemas.android.com/apk/res/android"
                        xmlns:tools="http://schemas.android.com/tools"
                        android:name="com.bytedance.sdk.openadsdk.multipro.TTMultiProvider"
                        android:authorities="${applicationId}.TTMultiProvider"
                        android:exported="false" />
            """)
        }
        if ("BD".equals(name) && isEnable(info)) {
            if (pluginVerCode == 0 || pluginVerCode > 50803) {
                config.directToAdd.add("""
                <provider
                     xmlns:android="http://schemas.android.com/apk/res/android"
                     xmlns:tools="http://schemas.android.com/tools"
                     android:name="com.baidu.mobads.openad.BdFileProvider"
                     android:authorities="${applicationId}.bd.provider"
                     android:exported="false"
                     android:grantUriPermissions="true">
                         <meta-data
                             android:name="android.support.FILE_PROVIDER_PATHS"
                             android:resource="@xml/mobad_file_paths" />
                </provider>
            """)
            } else {
                config.directToAdd.add("""
                    <provider
                         xmlns:android="http://schemas.android.com/apk/res/android"
                         xmlns:tools="http://schemas.android.com/tools"
                         android:name="com.baidu.mobads.openad.FileProvider"
                         android:authorities="${applicationId}.bd.provider"
                         android:exported="false"
                         android:grantUriPermissions="true">
                             <meta-data
                                 android:name="android.support.FILE_PROVIDER_PATHS"
                                 android:resource="@xml/mobad_file_paths" />
                    </provider>
            """)
            }
        }

        if ("GG".equals(name) && isEnable(info)) {
            if (ggGDTStatus == 1 || ggGDTStatus == 3) {
                //已经集成了gdt，需要更新广点通配置为androidx
                ggGDTStatus = 3
                updateGDT(applicationId, gdtProvider)
            } else {
                ggGDTStatus = 2
            }
            config.applicationMetaData.put("com.google.android.gms.ads.APPLICATION_ID", "${info.AppId}")
        }
    }

    private void updateGDT(String applicationId, String gdtProvider) {
        //同时存在GG和GDT
        //先移除android
        config.directToAdd.remove(gdtProvider)
        //再添加androidx
        config.directToAdd.add("""
            <provider
                xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:tools="http://schemas.android.com/tools"
                android:name="androidx.core.content.FileProvider"
                android:authorities="${applicationId}.fileprovider"
                android:exported="false"
                android:grantUriPermissions="true">
                <meta-data
                    android:name="android.support.FILE_PROVIDER_PATHS"
                    android:resource="@xml/mobad_file_paths" />
            </provider>
        """)
    }

    private void addDependency(String platform, MobAdInfoNode info) {
        String libraryName;
        String libraryVersion = info.Version
        if(libraryVersion == null || libraryVersion == "" || libraryVersion == "null"){
            libraryVersion = "+"
        }
        if ("GG".equalsIgnoreCase(platform)) {
            if (isEnable(info)) {
                addDependency('com.google.android.gms:play-services-ads:19.1.0')
            }
        } else if(platforms.contains(platform) && isEnable(info)){
            libraryName = 'com.mob.ad.plugins:' + platform + ':' + libraryVersion + '@aar'
            addDependency(libraryName)
        }
        if("CSJ".equals(platform) && isEnable(info)){
            addDependency('pl.droidsonroids.gif:android-gif-drawable:1.2.6')
        }
    }

    private void addDependency(String libraryName) {
        String version = config.gradlePluginVersion
        if (version == null || version.startsWith("3.")) {
            config.project.dependencies {
                add('implementation', libraryName)
            }
        } else {
            config.project.dependencies {
                add('compile', libraryName)
            }
        }
    }

    private boolean isEnable(MobAdInfoNode info){
        if(info.Enable == null || info.Enable == "" || info.Enable == "null"){
            return true
        } else if("false".equalsIgnoreCase(String.valueOf(info.Enable))){
            return false
        }
        return true
    }

    private int getPluginVerCode(String ver){
        if (ver == null || "+".equals(ver)) {
            return 0
        } else {
            int code = 0
            ver = ver.replaceAll("\\.", "")
            char[] c = ver.toCharArray()
            for (int i = 0; i < 3; i++) {
                code = code * 100 + Integer.parseInt(String.valueOf(c[i]))
            }
            return code
        }
    }
}
