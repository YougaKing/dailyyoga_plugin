package com.dailyyoga.plugin.miit

import com.dailyyoga.plugin.miit.spec.MethodSpec
import com.dailyyoga.plugin.miit.transform.SourceTargetTransformer
import com.dailyyoga.plugin.miit.transform.Transformer
import com.dailyyoga.plugin.miit.transform.replace.MethodCallReplaceTransformer
import com.dailyyoga.plugin.miit.util.Logger
import com.dailyyoga.plugin.miit.util.XmlErrorHandler
import org.gradle.api.Project

class DailyyogaMIITConfiguration {

    Project project

    def globalExcludes = []
    def transformers = new ArrayList<Transformer>()

    def METHOD = "METHOD"

    DailyyogaMIITConfiguration(Project project) {
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
            if (node.Method.size() != 2) {
                throw new IllegalArgumentException("source and target must not null in node ${node}")
            }
            node.Method.each {
                method ->
                    boolean isStatic = method.@isStatic ?: "true"
                    String type = method.@type

                    String declaring = method.Declaring.text().trim()
                    String returnType = method.ReturnType.text().trim()
                    String name = method.Name.text().trim()
                    String parameters = method.Parameters ? method.Parameters.text().trim() : ""

                    Logger.info "method:{isStatic:" + isStatic +
                            ",  type:" + type +
                            ",  declaring:" + declaring +
                            ",  returnType:" + returnType +
                            ",  name:" + name +
                            ",  parameters:" + parameters + "}"

                    MethodSpec methodSpec
                    if (parameters) {
                        methodSpec = MethodSpec.create(declaring, returnType, name, parameters, isStatic);
                    } else {
                        methodSpec = MethodSpec.create(declaring, returnType, name, isStatic);
                    }
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
