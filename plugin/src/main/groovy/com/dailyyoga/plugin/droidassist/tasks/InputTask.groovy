package com.dailyyoga.plugin.droidassist.tasks

import com.android.build.api.transform.QualifiedContent
import com.android.utils.FileUtils
import com.dailyyoga.plugin.droidassist.DroidAssistContext
import com.dailyyoga.plugin.droidassist.DroidAssistExecutor.BuildContext
import com.dailyyoga.plugin.droidassist.ex.DroidAssistBadStatementException
import com.dailyyoga.plugin.droidassist.ex.DroidAssistException
import com.dailyyoga.plugin.droidassist.ex.DroidAssistNotFoundException
import com.dailyyoga.plugin.droidassist.util.IOUtils
import com.dailyyoga.plugin.droidassist.util.Logger
import javassist.CannotCompileException
import javassist.NotFoundException

/**
 * Interface to process QualifiedContent.
 *
 * <p> It provides the ability to handle classes, see {@link #executeClass}
 */
abstract class InputTask<T extends QualifiedContent> implements Runnable {

    public static final String DOT_CLASS = ".class"
    public static final String DOT_JAR = ".jar"

    DroidAssistContext context
    BuildContext buildContext
    TaskInput<T> taskInput
    File temporaryDir

    static class TaskInput<T> {
        T input
        File dest
        boolean incremental
    }

    InputTask(
            DroidAssistContext context,
            BuildContext buildContext,
            TaskInput<T> taskInput) {
        this.context = context
        this.buildContext = buildContext
        this.taskInput = taskInput
        temporaryDir = ensureTemporaryDir()
    }

    @Override
    final void run() {
        try {
            execute()
        } catch (DroidAssistException e) {
            throw e
        } catch (Throwable e) {
            throw new DroidAssistException("Execution failed for " +
                    "input:${IOUtils.getPath(taskInput.input.file)}", e)
        }
    }

    abstract void execute()

    abstract String getInputType()

    File ensureTemporaryDir() {
        def dir = new File(
                "${buildContext.temporaryDir}/" +
                        "${inputType}/" +
                        "${taskInput.input.name.replace(":", "-")}")
        FileUtils.cleanOutputDir(dir)
        return dir
    }

    boolean executeClass(String className, File directory) {

        buildContext.totalCounter.incrementAndGet()
        def inputClass = null
        def transformers = context.transformers.findAll {
            it.classAllowed(className)
        }

        if (transformers.isEmpty()) {
            return false
        }

        inputClass = context.classPool.getOrNull(className)
        if (inputClass == null) {
            return false
        }

        transformers.each {
            try {
                it.performTransform(inputClass, className)
            } catch (NotFoundException e) {
                throw new DroidAssistNotFoundException(
                        "Transform failed for class: ${className}" +
                                " with not found exception: ${e.cause?.message}", e)
            } catch (CannotCompileException e) {
                throw new DroidAssistBadStatementException(
                        "Transform failed for class: ${className} " +
                                "with compile error: ${e.cause?.message}", e)
            } catch (Throwable e) {
                throw new DroidAssistException(
                        "Transform failed for class: ${className} " +
                                "with error: ${e.cause?.message}", e)
            }
        }

        if (inputClass.modified) {
            buildContext.affectedCounter.incrementAndGet()
            inputClass.writeFile(directory.absolutePath)
            return true
        }
        return false
    }
}
