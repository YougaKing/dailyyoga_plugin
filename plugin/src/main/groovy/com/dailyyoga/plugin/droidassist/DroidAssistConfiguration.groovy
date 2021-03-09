package com.dailyyoga.plugin.droidassist


import com.dailyyoga.plugin.droidassist.transform.SourceTargetTransformer
import com.dailyyoga.plugin.droidassist.transform.Transformer
import com.dailyyoga.plugin.droidassist.transform.replace.MethodCallReplaceTransformer
import com.dailyyoga.plugin.droidassist.util.XmlErrorHandler
import org.gradle.api.Project

class DroidAssistConfiguration {

    Project project

    def globalExcludes = []
    def transformers = new ArrayList<Transformer>()

    def METHOD = "METHOD"

    DroidAssistConfiguration(Project project) {
        this.project = project
    }

    List<Transformer> parse(File file) {
        XmlParser xmlParser = new XmlParser(true, true, true)
        xmlParser.setErrorHandler(new XmlErrorHandler())
        Node configs = xmlParser.parse(file)

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

            def excludes = []
            node.Filter.Exclude.each { excludes.add(it.text()) }

            if (!Boolean.valueOf(node.Filter.@ignoreGlobalExcludes[0])) {
                excludes.addAll(globalExcludes)
            }
            transformer.classFilterSpec.addExcludes(excludes)
            transformers.add(transformer)
    }
}
