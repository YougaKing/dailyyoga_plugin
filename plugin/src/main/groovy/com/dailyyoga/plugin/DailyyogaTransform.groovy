package com.dailyyoga.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor

import java.util.concurrent.Callable

class DailyyogaTransform extends Transform {

    public static final String VERSION = "1.1.5"

    private DailyyogaTransformHelper transformHelper
    private WaitableExecutor waitableExecutor
    private URLClassLoader urlClassLoader

    DailyyogaTransform(DailyyogaTransformHelper transformHelper) {
        this.transformHelper = transformHelper
        if (!transformHelper.disableDailyyogaMultiThread) {
            waitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()
        }
    }

    @Override
    String getName() {
        return "DailyyogaMIITAutoAssist"
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
    boolean isIncremental() {
        return !transformHelper.disableDailyyogaIncremental
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        beforeTransform(transformInvocation)
        transformClass(transformInvocation.context, transformInvocation.inputs, transformInvocation.outputProvider, transformInvocation.incremental)
        afterTransform()
    }

    private void transformClass(Context context, Collection<TransformInput> inputs, TransformOutputProvider outputProvider, boolean isIncremental)
            throws IOException, TransformException, InterruptedException {

        long startTime = System.currentTimeMillis()
        if (!isIncremental) {
            outputProvider.deleteAll()
        }

        //遍历输入文件
        inputs.each { TransformInput input ->
            //遍历 jar
            input.jarInputs.each { JarInput jarInput ->
                if (waitableExecutor) {
                    waitableExecutor.execute(new Callable<Object>() {
                        @Override
                        Object call() throws Exception {
                            forEachJar(isIncremental, jarInput, outputProvider, context)
                            return null
                        }
                    })
                } else {
                    forEachJar(isIncremental, jarInput, outputProvider, context)
                }
            }

            //遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                if (waitableExecutor) {
                    waitableExecutor.execute(new Callable<Object>() {
                        @Override
                        Object call() throws Exception {
                            forEachDirectory(isIncremental, directoryInput, outputProvider, context)
                            return null
                        }
                    })
                } else {
                    forEachDirectory(isIncremental, directoryInput, outputProvider, context)
                }
            }
        }
        if (waitableExecutor) {
            waitableExecutor.waitForTasksWithQuickFail(true)
        }
        println("[SensorsAnalytics]: 此次编译共耗时:${System.currentTimeMillis() - startTime}毫秒")
    }

    private void beforeTransform(TransformInvocation transformInvocation) {
        //打印提示信息
        Logger.printCopyright()
        Logger.setDebug(transformHelper.extension.debug)
        transformHelper.onTransform()
        println("[SensorsAnalytics]: 是否开启多线程编译:${!transformHelper.disableDailyyogaMultiThread}")
        println("[SensorsAnalytics]: 是否开启增量编译:${!transformHelper.disableDailyyogaIncremental}")
        println("[SensorsAnalytics]: 此次是否增量编译:$transformInvocation.incremental")

        traverseForClassLoader(transformInvocation)
    }

    private void afterTransform() {
        try {
            if (urlClassLoader != null) {
                urlClassLoader.close()
                urlClassLoader = null
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    private void traverseForClassLoader(TransformInvocation transformInvocation) {
        def urlList = []
        def androidJar = transformHelper.androidJar()
        urlList << androidJar.toURI().toURL()
        transformInvocation.inputs.each { transformInput ->
            transformInput.jarInputs.each { jarInput ->
                urlList << jarInput.getFile().toURI().toURL()
            }

            transformInput.directoryInputs.each { directoryInput ->
                urlList << directoryInput.getFile().toURI().toURL()
            }
        }
        def urlArray = urlList as URL[]
        urlClassLoader = new URLClassLoader(urlArray)
        transformHelper.urlClassLoader = urlClassLoader
        checkRNState()
    }
}