package com.dailyyoga.plugin.droidassist.util

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.TransformOutputProvider

class GradleUtils {

    static File getTransformOutputLocation(
            TransformOutputProvider provider,
            QualifiedContent content) {

        if (content instanceof JarInput) {
            return provider.
                    getContentLocation(
                            content.name,
                            content.contentTypes,
                            content.scopes,
                            Format.JAR)
        }
        if (content instanceof DirectoryInput) {
            return provider.
                    getContentLocation(
                            content.name,
                            content.contentTypes,
                            content.scopes,
                            Format.DIRECTORY)
        }
        return null
    }
}