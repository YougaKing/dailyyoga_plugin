package com.dailyyoga.plugin.droidassist


import com.dailyyoga.plugin.droidassist.transform.SourceTargetTransformer
import com.dailyyoga.plugin.droidassist.transform.Transformer
import com.dailyyoga.plugin.droidassist.transform.enhance.MethodCallTryCatchTransformer
import com.dailyyoga.plugin.droidassist.transform.enhance.MethodExecutionTryCatchTransformer
import com.dailyyoga.plugin.droidassist.transform.enhance.TryCatchTransformer
import com.dailyyoga.plugin.droidassist.transform.insert.MethodCallInsertTransformer
import com.dailyyoga.plugin.droidassist.transform.insert.MethodExecutionInsertTransformer
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

        //insert
        //
        //before call
        configs.Insert.BeforeMethodCall.each {
            node ->
                sourceTargetTransformerNodeHandler(METHOD, node) {
                    return new MethodCallInsertTransformer()
                            .setAsBefore(true)
                }
        }

        //before execution
        configs.Insert.BeforeMethodExecution.each {
            node ->
                sourceTargetTransformerNodeHandler(METHOD, node) {
                    return new MethodExecutionInsertTransformer()
                            .setAsBefore(true)
                }
        }

        //add
        //addcatch
        configs.Enhance.TryCatchMethodCall.each {
            node ->
                addCatchTransformerNodeHandler(METHOD, node) {
                    return new MethodCallTryCatchTransformer()
                }
        }
        configs.Enhance.TryCatchMethodExecution.each {
            node ->
                addCatchTransformerNodeHandler(METHOD, node) {
                    return new MethodExecutionTryCatchTransformer()
                }
        }

        return transformers
    }

    def sourceTargetTransformerNodeHandler = {
        kind, node, transformerFeather ->
            SourceTargetTransformer transformer = transformerFeather.call()
            def journal = node.@journal ?: "true"
            transformer.setJournal(Boolean.parseBoolean(journal))

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

    def addCatchTransformerNodeHandler = {
        kind, node, transformerFeather ->
            TryCatchTransformer transformer = transformerFeather.call()
            def journal = node.@journal ?: "true"
            transformer.setJournal(Boolean.parseBoolean(journal))

            def extend = node.Source.@extend[0] ?: "true"
            transformer.setSource(node.Source.text().trim(), kind, Boolean.parseBoolean(extend))
            transformer.setException(node.Exception.text().trim())
            transformer.setTarget(node.Target.text().trim())

            if (transformer.getSource() == '') {
                throw new IllegalArgumentException("Empty source in node ${node}")
            }
            if (transformer.getTarget() == '') {
                throw new IllegalArgumentException("Empty target in node ${node}")
            }

            def excludes = []
            node.Filter.Exclude.each { excludes.add(it.text()) }
            excludes.addAll(globalExcludes)

            transformer.classFilterSpec.addExcludes(excludes)
            transformers.add(transformer)
    }
}
