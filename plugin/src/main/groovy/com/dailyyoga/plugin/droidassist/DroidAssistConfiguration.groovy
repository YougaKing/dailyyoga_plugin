package com.dailyyoga.plugin.droidassist

import com.dailyyoga.plugin.droidassist.spec.MethodSpec
import com.dailyyoga.plugin.droidassist.transform.Transformer
import com.dailyyoga.plugin.droidassist.util.Logger
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
                    return new com.dailyyoga.plugin.droidassist.transform.replace.MethodCallReplaceTransformer()
                }
        }

        return transformers
    }

    def sourceTargetTransformerNodeHandler = {
        kind, node, transformerFeather ->
            com.dailyyoga.plugin.droidassist.transform.SourceTargetTransformer transformer = transformerFeather.call()
            if (node.Method.size() != 2) {
                throw new IllegalArgumentException("source and target must not null in node ${node}")
            }
            node.Method.each {
                method ->
                    String isStatic = method.@isStatic
                    String type = method.@type
                    String declaring = method.Declaring.text().trim()
                    String returnType = method.ReturnType.text().trim()
                    String name = method.Name.text().trim()
                    String parameters = method.Parameters ? method.Parameters.text().trim() : ""

                    MethodSpec methodSpec
                    if (parameters) {
                        methodSpec = MethodSpec.create(declaring, returnType, name, parameters, isStatic);
                    } else {
                        methodSpec = MethodSpec.create(declaring, returnType, name, isStatic);
                    }

                    Logger.info("methodSpec:" + methodSpec)

                    transformer.setMethod(type, methodSpec)
            }
            String nodeString = "in node ${node}"
            transformer.checkSourceTarget(nodeString)
            def excludes = []
            node.Filter.Exclude.each { excludes.add(it.text()) }

            if (!Boolean.valueOf(node.Filter.@ignoreGlobalExcludes[0])) {
                excludes.addAll(globalExcludes)
            }
            transformer.classFilterSpec.addExcludes(excludes)
            transformers.add(transformer)
    }
}
