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

import com.google.common.collect.Lists

class DailyyogaMIITExtension {
    boolean enable = true
    int logLevel
    List<File> configFiles = Lists.newArrayList()
    File logDir
    boolean incremental = true

    List<String> includes = Lists.newArrayList()
    List<String> excludes = Lists.newArrayList()

    void exclude(String... filter) {
        excludes.addAll(filter)
    }

    void include(String... filter) {
        includes.addAll(filter)
    }

    void config(File... file) {
        configFiles.addAll(file)
    }

    List<File> getConfig() {
        return configFiles
    }

    @Override
    String toString() {
        StringBuilder excludeBuilder = new StringBuilder()
        int length = exclude.size()
        for (int i = 0; i < length; i++) {
            excludeBuilder.append("'").append(exclude.get(i)).append("'")
            if (i != length - 1) {
                excludeBuilder.append(",")
            }
        }

        StringBuilder includeBuilder = new StringBuilder()
        length = include.size()
        for (int i = 0; i < length; i++) {
            includeBuilder.append("'").append(include.get(i)).append("'")
            if (i != length - 1) {
                includeBuilder.append(",")
            }
        }
        return "\n{" +
                "\n    enable=" + enable +
                "\n    logLevel=" + logLevel +
                "\n    config=" + configFiles +
                "\n    logDir=" + logDir +
                "\texclude=[" + excludeBuilder.toString() + "]" + "\n" +
                "\tinclude=[" + includeBuilder.toString() + "]" + "\n" +
                '\n}'

    }
}

