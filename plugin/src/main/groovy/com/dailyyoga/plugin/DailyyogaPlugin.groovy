package com.dailyyoga.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator
import org.gradle.invocation.DefaultGradle

class DailyyogaPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        Instantiator ins = ((DefaultGradle) project.getGradle()).getServices().get(Instantiator)
        def args = [ins] as Object[]
        DailyyogaExtension extension = project.extensions.create("dailyyoga", DailyyogaExtension, args)


        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        DailyyogaTransformHelper transformHelper = new DailyyogaTransformHelper(extension, appExtension)

        appExtension.registerTransform(new DailyyogaTransform(transformHelper))
    }
}