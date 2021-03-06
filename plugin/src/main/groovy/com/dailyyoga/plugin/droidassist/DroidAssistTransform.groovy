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
package com.dailyyoga.plugin.droidassist

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.dailyyoga.plugin.droidassist.transform.SourceTargetTransformer
import com.dailyyoga.plugin.droidassist.transform.Transformer
import com.dailyyoga.plugin.droidassist.util.GradleUtils
import com.dailyyoga.plugin.droidassist.util.Logger
import com.dailyyoga.plugin.droidassist.util.TimingLogger
import com.google.common.collect.Lists
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.function.Consumer
import java.util.stream.Stream

class DroidAssistTransform extends Transform {

    Project project
    DroidAssistExtension gradleExtension

    DroidAssistTransform(Project project, DroidAssistExtension extension) {
        this.project = project
        this.gradleExtension = extension
    }

    @Override
    String getName() {
        return "droidAssist"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    Set<? super QualifiedContent.Scope> getReferencedScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return gradleExtension.incremental
    }

    /**
     * Magic here, Changes to the plugin config file will trigger a non incremental execution
     */
    @Override
    Collection<SecondaryFile> getSecondaryFiles() {
        Objects.requireNonNull(gradleExtension.config)
        return Lists.newArrayList(
                SecondaryFile.nonIncremental(project.files(gradleExtension.config)))
    }


    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        try {
            def logLevel = gradleExtension.logLevel
            def logDir = gradleExtension.logDir ?: project.file("${project.buildDir}/outputs/logs")

            def variant = transformInvocation.getContext().variantName

            Logger.init(logLevel < 0 ? Logger.LEVEL_CONSOLE : logLevel, logDir, variant)

            onTransform(
                    transformInvocation.getContext(),
                    transformInvocation.getInputs(),
                    transformInvocation.getReferencedInputs(),
                    transformInvocation.getOutputProvider(),
                    transformInvocation.isIncremental())
        } catch (Throwable e) {
            Logger.error("Build failed with an exception: ${e.cause?.message}", e)
            e.fillInStackTrace()
            throw e
        } finally {
            Logger.close()
        }
    }

    /**
     * When dailyyogaMIIT is enable, process files and write them to an output folder
     *
     * <p> {@link DroidAssistExecutor#execute} process files specifically
     */
    void onTransform(
            Context gradleContext,
            Collection<TransformInput> inputs,
            Collection<TransformInput> referencedInputs,
            TransformOutputProvider outputProvider,
            boolean isIncremental)
            throws IOException, TransformException, InterruptedException {

        Logger.info("Transform start, " +
                "enable:${gradleExtension.enable}, " +
                "incremental:${isIncremental}")

        // If dailyyogaMIIT is disable, just copy the input folder to the output folder
        if (!gradleExtension.enable) {
            outputProvider.deleteAll()
            def dirStream = inputs
                    .parallelStream()
                    .flatMap { it.directoryInputs.parallelStream() }
                    .filter { it.file.exists() }

            def jarStream = inputs
                    .parallelStream()
                    .flatMap { it.jarInputs.parallelStream() }
                    .filter { it.file.exists() }

            Stream.concat(dirStream, jarStream).forEach {
                def copy = it.file.isFile() ? "copyFile" : "copyDirectory"
                FileUtils."$copy"(
                        it.file,
                        GradleUtils.getTransformOutputLocation(outputProvider, it))
            }
            return
        }


        def start = System.currentTimeMillis()
        Logger.info("extension: ${gradleExtension}")
        def timingLogger = new TimingLogger("Timing", "execute")

        //Delete output folder and reprocess files, when it is not incremental
        if (!isIncremental) {
            outputProvider.deleteAll()
            timingLogger.addSplit("delete output")
        }

        def context =
                new DroidAssistContext(
                        gradleContext,
                        project,
                        gradleExtension,
                        referencedInputs)
        context.configure()
        timingLogger.addSplit("configure context")

        def executor =
                new DroidAssistExecutor(
                        context,
                        outputProvider,
                        isIncremental)
        timingLogger.addSplit("create executor")

        //Execute all input classed with byte code operation transformers
        executor.execute(inputs)
        timingLogger.addSplit("execute inputs")

        timingLogger.dumpToLog()

        context.transformers.forEach(new Consumer<Transformer>() {
            @Override
            void accept(Transformer transformer) {
                SourceTargetTransformer targetTransformer = (SourceTargetTransformer) transformer;
                Logger.info(targetTransformer.source.replaceAll("\n", "").replaceAll(" +", " ") +
                        " count:${targetTransformer.count}")
            }
        })

        Logger.debug("Transform end, " +
                "input classes count:${executor.classCount}, " +
                "affected classes:${executor.affectedCount}, " +
                "time use:${System.currentTimeMillis() - start} ms")
    }
}