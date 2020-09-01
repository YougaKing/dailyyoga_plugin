package com.dailyyoga.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.dailyyoga.plugin.net.NetTransformPipeline;

import org.gradle.api.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 20:12
 * @description:
 */
public class TransformPipelineFactory {

    private static List<TransformPipeline> transformPipelines = new ArrayList<>();

    public static void create(Project project) {
        transformPipelines.add(new NetTransformPipeline(project));
    }


    public static void directoryInputs(DirectoryInput directoryInput) {
        for (TransformPipeline transformPipeline : transformPipelines) {
            transformPipeline.directoryInputs(directoryInput);
        }
    }

    public static void jarInputs(JarInput jarInput, File dest) {
        for (TransformPipeline transformPipeline : transformPipelines) {
            transformPipeline.jarInputs(jarInput, dest);
        }
    }

    public static void process() {
        for (TransformPipeline transformPipeline : transformPipelines) {
            transformPipeline.process();
        }
    }
}
