package com.dailyyoga.plugin.miit

import com.dailyyoga.plugin.miit.transform.SourceTargetTransformer
import com.dailyyoga.plugin.miit.transform.Transformer
import com.dailyyoga.plugin.miit.transform.replace.MethodCallReplaceTransformer
import org.gradle.api.Project


class DailyyogaMIITConfiguration {

    Project project

    def globalIncludes = []
    def globalExcludes = []
    def transformers = new ArrayList<Transformer>()

    def METHOD = "METHOD"

    DailyyogaMIITConfiguration(Project project) {
        this.project = project
    }

    List<Transformer> parse(File file) {
        Node configs = new XmlParser(true, true, true).parse(file)

        configs.Global.Filter.Include.each { globalIncludes.add(it.text()) }
        configs.Global.Filter.Exclude.each { globalExcludes.add(it.text()) }

        configs.Replace.MethodCall.each {
            node ->
                sourceTargetTransformerNodeHandler(METHOD, node) {
                    return new MethodCallReplaceTransformer()
                }
        }

        return transformers
    }

    def sourceTargetTransformerNodeHandler = {
        kind, node, transformerFeather ->
            SourceTargetTransformer transformer = transformerFeather.call()
            def extend = node.Source.@extend[0] ?: "true"
            transformer.setSource(node.Source.text().trim(), kind, Boolean.valueOf(extend))
            transformer.setTarget(node.Target.text().trim())

            if (transformer.getSource() == '') {
                throw new IllegalArgumentException("Empty source in node ${node}")
            }
            if (transformer.getTarget() == '') {
                throw new IllegalArgumentException("Empty target in node ${node}")
            }

            def includes = [], excludes = []

            node.Filter.Include.each { includes.add(it.text()) }
            node.Filter.Exclude.each { excludes.add(it.text()) }

            if (!Boolean.valueOf(node.Filter.@ignoreGlobalIncludes[0])) {
                includes.addAll(globalIncludes)
            }
            if (!Boolean.valueOf(node.Filter.@ignoreGlobalExcludes[0])) {
                excludes.addAll(globalExcludes)
            }

            transformer.classFilterSpec.addIncludes(includes)
            transformer.classFilterSpec.addExcludes(excludes)
            transformers.add(transformer)
    }
}
