package com.dailyyoga.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager

class DailyyogaTransform extends Transform {

    public static final String VERSION = "1.1.5"

    DailyyogaTransformHelper transformHelper

    DailyyogaTransform(DailyyogaTransformHelper transformHelper) {
        this.transformHelper = transformHelper
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

    private void beforeTransform(TransformInvocation transformInvocation) {
        //打印提示信息
        Logger.printCopyright()
        Logger.setDebug(transformHelper.extension.debug)
        transformHelper.onTransform()
        println("[SensorsAnalytics]: 是否开启多线程编译:${!transformHelper.disableSensorsAnalyticsMultiThread}")
        println("[SensorsAnalytics]: 是否开启增量编译:${!transformHelper.disableSensorsAnalyticsIncremental}")
        println("[SensorsAnalytics]: 此次是否增量编译:$transformInvocation.incremental")
        println("[SensorsAnalytics]: 是否在方法进入时插入代码:${transformHelper.isHookOnMethodEnter}")

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
}