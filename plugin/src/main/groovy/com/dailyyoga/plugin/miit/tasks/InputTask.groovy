package com.dailyyoga.plugin.miit.tasks

import com.android.build.api.transform.QualifiedContent
import com.android.utils.FileUtils
import com.dailyyoga.plugin.miit.DailyyogaMIITContext
import com.dailyyoga.plugin.miit.DailyyogaMIITExecutor.BuildContext
import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadStatementException
import com.dailyyoga.plugin.miit.ex.DailyyogaMIITException
import com.dailyyoga.plugin.miit.ex.DailyyogaMIITNotFoundException
import com.dailyyoga.plugin.miit.util.IOUtils
import com.dailyyoga.plugin.miit.util.Logger
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

    DailyyogaMIITContext context
    BuildContext buildContext
    TaskInput<T> taskInput
    File temporaryDir

    static class TaskInput<T> {
        T input
        File dest
        boolean incremental
    }

    InputTask(
            DailyyogaMIITContext context,
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
            Logger.debug("execute ${inputType}: ${IOUtils.getPath(taskInput.input.file)}")
            execute()
        } catch (DailyyogaMIITException e) {
            throw e
        } catch (Throwable e) {
            throw new DailyyogaMIITException("Execution failed for " +
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

//        Logger.info("executeClass: { className:" + className + ",directory:" + directory.getAbsolutePath() + "}")

        inputClass = context.classPool.getOrNull(className)
        if (inputClass == null) {
            return false
        }

        Logger.debug("executeClass: { className:" + className + ",inputClass:" + inputClass + "}")

        transformers.each {
            try {
                it.performTransform(inputClass, className)
            } catch (NotFoundException e) {
                throw new DailyyogaMIITNotFoundException(
                        "Transform failed for class: ${className}" +
                                " with not found exception: ${e.cause?.message}", e)
            } catch (CannotCompileException e) {
                throw new DailyyogaMIITBadStatementException(
                        "Transform failed for class: ${className} " +
                                "with compile error: ${e.cause?.message}", e)
            } catch (Throwable e) {
                throw new DailyyogaMIITException(
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
