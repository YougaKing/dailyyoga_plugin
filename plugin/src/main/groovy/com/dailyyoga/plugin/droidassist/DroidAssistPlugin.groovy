package com.dailyyoga.plugin.droidassist

import com.android.build.gradle.AppExtension
import com.dailyyoga.plugin.droidassist.util.Logger
import org.gradle.api.Plugin
import org.gradle.api.Project

class DroidAssistPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        DroidAssistExtension extension = project.extensions.create("droidAssist", DroidAssistExtension)

        if (extension.enable) {
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            appExtension.registerTransform(new DroidAssistTransform(project, extension))
        } else {
            Logger.error("------------您已关闭了工信部整改插件--------------")
        }

    }
}