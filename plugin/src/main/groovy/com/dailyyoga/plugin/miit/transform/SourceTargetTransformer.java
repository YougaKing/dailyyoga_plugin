package com.dailyyoga.plugin.miit.transform;


import com.dailyyoga.plugin.miit.spec.SourceSpec;

import java.lang.reflect.Method;
import java.util.Set;

import javassist.CtClass;
import javassist.CtMember;
import javassist.NotFoundException;

public abstract class SourceTargetTransformer extends Transformer {

    private String target;
    private String source;
    private SourceSpec sourceSpec;

    private CtClass sourceClass;
    private String sourceDeclaringClassName;
    private CtMember sourceMember;
    private CtClass sourceReturnType;

    private Class annotationClass;
    private Set<Method> annotationTargetMembers;

    public SourceTargetTransformer setSource(String source, String kind, boolean extend) {
        this.source = source;
        sourceSpec = SourceSpec.fromString(source, kind, extend);
        return this;
    }

    public SourceTargetTransformer setTarget(String target) {
        target = target.trim();
        this.target = target;
        return this;
    }

    public String getSource() {
        return source;
    }

    protected String getSourceDeclaringClassName() {
        if (sourceDeclaringClassName == null) {
            sourceDeclaringClassName = sourceSpec.getDeclaringClassName();
        }
        return sourceDeclaringClassName;
    }

    protected CtClass getSourceClass() throws NotFoundException {
        if (sourceClass == null) {
            sourceClass = classPool.getCtClass(getSourceDeclaringClassName());
        }
        return sourceClass;
    }

    boolean isMatchSourceClass(CtClass insnClass) throws NotFoundException {
        if (sourceSpec.isAnnotation()) {
            return true;
        }
        boolean match = false;
        Boolean anInterface = isInterface(insnClass);
        if (anInterface == null || anInterface) {
            return false;
        }
        if (sourceSpec.isExtend()) {
            CtClass sourceClass = getSourceClass();
            for (CtClass itf : tryGetInterfaces(insnClass)) {
                if (itf == sourceClass) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                match = insnClass.subclassOf(sourceClass);
            }
        } else {
            match = insnClass.getName().equals(getSourceDeclaringClassName());
        }
        return match;
    }
}
