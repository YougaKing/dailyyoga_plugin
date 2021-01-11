/*
 * Created by wangzhuozhou on 2015/08/12.
 * Copyright 2015－2020 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

        boolean disableDailyyogaMIITPlugin = false
        boolean disableDailyyogaMIITMultiThreadBuild = false
        boolean disableDailyyogaMIITIncrementalBuild = false
        Properties properties = new Properties()
        if (project.rootProject.file('gradle.properties').exists()) {
            properties.load(project.rootProject.file('gradle.properties').newDataInputStream())
            disableDailyyogaMIITPlugin = Boolean.parseBoolean(properties.getProperty("dailyyogaMIIT.disablePlugin", "false"))
            disableDailyyogaMIITMultiThreadBuild = Boolean.parseBoolean(properties.getProperty("dailyyogaMIIT.disableMultiThreadBuild", "false"))
            disableDailyyogaMIITIncrementalBuild = Boolean.parseBoolean(properties.getProperty("dailyyogaMIIT.disableIncrementalBuild", "false"))
        }
        if (!disableDailyyogaMIITPlugin) {
            AppExtension appExtension = project.extensions.findByType(AppExtension.class)
            DailyyogaMIITTransformHelper transformHelper = new DailyyogaMIITTransformHelper(extension, appExtension)
            transformHelper.disableDailyyogaMIITIncremental = disableDailyyogaMIITIncrementalBuild
            transformHelper.disableDailyyogaMIITMultiThread = disableDailyyogaMIITMultiThreadBuild
            transformHelper.isHookOnMethodEnter = isHookOnMethodEnter
            appExtension.registerTransform(new DailyyogaMIITTransform(transformHelper))
        } else {
            Logger.error("------------您已关闭了工信部整改插件--------------")
        }

    }
}