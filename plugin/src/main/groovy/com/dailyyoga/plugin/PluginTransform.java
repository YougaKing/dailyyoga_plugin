package com.dailyyoga.plugin;

import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.AppExtension;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Set;

import javassist.ClassPool;
import javassist.NotFoundException;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2018/04/26 12:13
 * @description:
 */
public class PluginTransform extends Transform implements Plugin<Project> {
    private Project mProject;

    @Override
    public void apply(Project project) {
        mProject = project;
        project.getLogger().error("========================");
        project.getLogger().error("PluginTransform apply ()");
        project.getLogger().error("========================");

        //AppExtension就是build.gradle中android{...}这一块
        AppExtension android = project.getExtensions().getByType(AppExtension.class);
        //注册一个Transform
        android.registerTransform(this);
        TransformPipelineFactory.create(mProject);
    }


    @Override
    public String getName() {
        return "PluginTransform";
    }


    //需要处理的数据类型，有两种枚举类型
    //CLASSES和RESOURCES，CLASSES代表处理的java的class文件，RESOURCES代表要处理java的资源
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }


    //    指Transform要操作内容的范围，官方文档Scope有7种类型：
    //
    //    EXTERNAL_LIBRARIES        只有外部库
    //    PROJECT                       只有项目内容
    //    PROJECT_LOCAL_DEPS            只有项目的本地依赖(本地jar)
    //    PROVIDED_ONLY                 只提供本地或远程依赖项
    //    SUB_PROJECTS              只有子项目。
    //    SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
    //    TESTED_CODE                   由当前变量(包括依赖项)测试的代码
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    ;

    //指明当前Transform是否支持增量编译
    @Override
    public boolean isIncremental() {
        return false;
    }

    //    Transform中的核心方法，
    //    inputs中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
    //    outputProvider 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);
        long startTime = System.currentTimeMillis();

        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        try {
            inputs.forEach(transformInput -> {


                transformInput.getDirectoryInputs().forEach(directoryInput -> {

                    File dest = outputProvider.getContentLocation(directoryInput.getName(),
                            directoryInput.getContentTypes(),
                            directoryInput.getScopes(),
                            Format.DIRECTORY);
                    try {
                        ClassPool.getDefault().appendClassPath(directoryInput.getFile().getAbsolutePath());

                        TransformPipelineFactory.directoryInputs(directoryInput);

                        FileUtils.copyDirectory(directoryInput.getFile(), dest);
                    } catch (IOException | NotFoundException e) {
                        e.printStackTrace();
                    }
                });

                transformInput.getJarInputs().forEach(jarInput -> {

                    File dest = outputProvider.getContentLocation(jarInput.getName() + DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath()),
                            jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);

                    TransformPipelineFactory.jarInputs(jarInput, dest);
                });

            });

            TransformPipelineFactory.process();

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            mProject.getLogger().error("error:" + sw.toString());
        }
        mProject.getLogger().error("PluginTransform cast :" + (System.currentTimeMillis() - startTime) / 1000 + " secs");
    }
}