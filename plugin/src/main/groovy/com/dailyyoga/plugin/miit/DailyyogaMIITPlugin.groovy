package com.dailyyoga.plugin.miit

import com.android.build.gradle.AppExtension
import com.dailyyoga.plugin.miit.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

class DailyyogaMIITPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        DailyyogaMIITExtension extension = project.extensions.create("dailyyogaMIIT", DailyyogaMIITExtension)

        if (extension.enable) {
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            appExtension.registerTransform(new DailyyogaMIITTransform(project, extension))
        } else {
            Logger.error("------------您已关闭了工信部整改插件--------------")
        }

    }
}