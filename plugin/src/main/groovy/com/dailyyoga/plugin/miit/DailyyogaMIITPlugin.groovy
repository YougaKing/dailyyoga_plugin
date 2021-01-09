package com.dailyyoga.plugin.miit

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

class DailyyogaMIITPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        Instantiator ins = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator)
        def args = [ins] as Object[]
        DailyyogaMIITExtension extension = project.extensions.create("dailyyogaMIIT", DailyyogaMIITExtension, args)


        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        DailyyogaMIITTransformHelper transformHelper = new DailyyogaMIITTransformHelper(extension, appExtension)

        appExtension.registerTransform(new DailyyogaMIITTransform(transformHelper))
    }
}