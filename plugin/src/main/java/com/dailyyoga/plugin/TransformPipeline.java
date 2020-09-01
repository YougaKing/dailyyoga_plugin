package com.dailyyoga.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;

import org.gradle.api.Project;

import java.io.File;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2020/8/31 19:42
 * @description:
 */
public abstract class TransformPipeline {

    protected Project mProject;

    public TransformPipeline(Project project) {
        this.mProject = project;
    }

    public abstract void directoryInputs(DirectoryInput directoryInput);

    public abstract void jarInputs(JarInput jarInput, File dest);

    public abstract void process();
}
