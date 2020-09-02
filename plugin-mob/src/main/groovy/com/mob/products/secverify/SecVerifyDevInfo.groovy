package com.mob.products.secverify

import com.mob.ConfigCreator
import com.mob.DObject

class SecVerifyDevInfo extends DObject {

    static Set platforms
    ConfigCreator config
    String metaValue
    int sdkVersion
    Object lastConf

    static {
        platforms = [
                "CTCC", "CUCC", "CMCC"
        ]
    }

    void setConfigCreator(ConfigCreator config) {
        this.config = config
    }

    void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion
    }

    String getMetaValue(){
        return metaValue
    }

    def methodMissing(String name, def args) {
        String plat = platforms.find {
            return name.equalsIgnoreCase(it)
        }
        if (plat != null) {
            SecVerifyInfoNode info = new SecVerifyInfoNode()
            info.closure = args[0]
            addActivity(plat, info)
            addDependency(plat, info)
        }
    }

    private void addActivity(String name, SecVerifyInfoNode info) {
        if(sdkVersion != 0 && sdkVersion < 20005) {
            return
        }
        if(metaValue == null){
            metaValue = name
        } else{
            metaValue = metaValue + "," + name
        }
        if(lastConf != null){
            config.directToAdd.remove(lastConf)
        }
        config.directToAdd.add("""
				<meta-data
					xmlns:android="http://schemas.android.com/apk/res/android"
                    xmlns:tools="http://schemas.android.com/tools" 
					android:name="Mob-Disable"
            		android:value="${metaValue}"/>
			""")
        lastConf = config.directToAdd.getAt(config.directToAdd.size() - 1)
    }

    private void addDependency(String platform, SecVerifyInfoNode info) {
        if(sdkVersion != 0 && sdkVersion < 20005) {
            return
        }
        if ("CTCC".equalsIgnoreCase(platform) && isEnable(info)) {
            deleteDependencies(platform)
        }
        if ("CUCC".equalsIgnoreCase(platform) && isEnable(info)) {
            deleteDependencies(platform)
        }
        if("CMCC".equalsIgnoreCase(platform) && isEnable(info)) {
            deleteDependencies(platform)
        }
    }

    private void deleteDependencies(String platform) {
        if ("CTCC".equalsIgnoreCase(platform)) {
            if(sdkVersion == 20005) {
                config.project.configurations.all {
                    exclude group: 'com.mob.sec.dependency', module: 'ctcc'
                }
            } else{
                config.project.configurations.all {
                    exclude group: 'com.mob.sec.plugins', module: 'SecPlugins-Ctcc'
                }
            }
        }
        if ("CUCC".equalsIgnoreCase(platform)) {
            if(sdkVersion == 20005) {
                config.project.configurations.all {
                    exclude group: 'com.mob.sec.dependency', module: 'cucc'
                }
            } else{
                config.project.configurations.all {
                    exclude group: 'com.mob.sec.plugins', module: 'SecPlugins-Cucc'
                }
            }
        }
        if("CMCC".equalsIgnoreCase(platform)) {
            if(sdkVersion == 20005) {
                config.project.configurations.all {
                    exclude group: 'com.mob.sec.dependency', module: 'cmcc'
                }
            } else{
                config.project.configurations.all {
                    exclude group: 'com.mob.sec.plugins', module: 'SecPlugins-Cmcc'
                }
            }
        }
    }

    private boolean isEnable(SecVerifyInfoNode info){
        if(info.Enable == null || info.Enable == "" || info.Enable == "null"){
            return true
        } else if("false".equalsIgnoreCase(String.valueOf(info.Enable))){
            return false
        } else{
            return true;
        }
        return true
    }
}

