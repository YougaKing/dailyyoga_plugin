package com.dailyyoga.plugin.droidassist

import com.android.build.api.transform.Context
import com.android.build.api.transform.TransformInput
import com.dailyyoga.plugin.droidassist.ex.DroidAssistException
import com.dailyyoga.plugin.droidassist.transform.Transformer
import com.dailyyoga.plugin.droidassist.util.IOUtils
import com.dailyyoga.plugin.droidassist.util.Logger
import javassist.ClassPool
import org.gradle.api.Project

import java.util.stream.Collectors
import java.util.stream.Stream

class DroidAssistContext {

    Context context
    Project project
    DailyyogaMIITClassPool classPool
    DroidAssistExtension extension
    Collection<TransformInput> referencedInputs
    Collection<Transformer> transformers

    DroidAssistContext(
            Context context,
            Project project,
            DroidAssistExtension extension,
            Collection<TransformInput> referencedInputs) {
        this.context = context
        this.project = project
        this.extension = extension
        this.referencedInputs = referencedInputs
    }

    def configure() {
        try {
            createClassPool()
        } catch (Throwable e) {
            throw new DroidAssistException("Failed to create class pool", e)
        }

        transformers = loadConfiguration()
    }

    def createClassPool() {
        classPool = new DailyyogaMIITClassPool()
        classPool.appendBootClasspath(project.android.bootClasspath)

        def dirStream = referencedInputs
                .parallelStream()
                .flatMap { it.directoryInputs.parallelStream() }
                .filter { it.file.exists() }

        def jarStream = referencedInputs
                .parallelStream()
                .flatMap { it.jarInputs.parallelStream() }
                .filter { it.file.exists() }

        Stream.concat(dirStream, jarStream).forEach {
            Logger.info("Append classpath: ${IOUtils.getPath(it.file)}")
            classPool.appendClassPath(it.file)
        }
    }

    def loadConfiguration() {
        def transformers = extension.configFiles
                .parallelStream()
                .flatMap {
                    try {
                        def list = new DroidAssistConfiguration(project).parse(it)
                        return list.stream().peek {
                            transformer ->
                                transformer.setClassPool(classPool)
                                transformer.setAbortOnUndefinedClass(extension.abortOnUndefinedClass)
                                transformer.check()
                        }
                    } catch (Throwable e) {
                        throw new DroidAssistException("Unable to load configuration," +
                                " unexpected exception occurs when parsing config file:$it, " +
                                "What went wrong:\n${e.message}", e)
                    }
                }//parse each file
                .collect(Collectors.toList())

        Logger.info("Dump transformers:")
        transformers.each {
            Logger.info("transformer: $it")
        }
        return transformers
    }

    class DailyyogaMIITClassPool extends ClassPool {

        DailyyogaMIITClassPool() {
            super(true)
            childFirstLookup = true
        }

        void appendBootClasspath(Collection<File> paths) {
            paths.stream().parallel().forEach {
                appendClassPath(it)
                Logger.info "Append boot classpath: ${IOUtils.getPath(it)}"
            }
        }

        void appendClassPath(File path) {
            appendClassPath(path.absolutePath)
        }
    }
}
