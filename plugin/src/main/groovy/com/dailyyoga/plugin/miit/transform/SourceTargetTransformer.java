package com.dailyyoga.plugin.miit.transform;


import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadTypeException;
import com.dailyyoga.plugin.miit.spec.MethodSpec;

import java.lang.reflect.Method;
import java.util.Set;

import javassist.CtClass;
import javassist.CtMember;
import javassist.NotFoundException;

public abstract class SourceTargetTransformer extends Transformer {

    private MethodSpec sourceSpec;
    private MethodSpec targetSpec;

    private CtClass sourceClass;
    private String sourceDeclaringClassName;
    private CtMember sourceMember;
    private CtClass sourceReturnType;

    private Class annotationClass;
    private Set<Method> annotationTargetMembers;

    public SourceTargetTransformer setMethod(String type,
                                             String declaring,
                                             String returnType,
                                             String name,
                                             String parameters,
                                             boolean isStatic) {
        if (MethodSpec.SOURCE.equals(type)) {
            sourceSpec = MethodSpec.create(declaring, returnType, name, parameters, isStatic);
        } else if (MethodSpec.TARGET.equals(type)) {
            targetSpec = MethodSpec.create(declaring, returnType, name, parameters, isStatic);
        } else {
            throw new DailyyogaMIITBadTypeException("Bad type :" + type);
        }
        return this;
    }

    public MethodSpec getSourceSpec() {
        return sourceSpec;
    }

    public MethodSpec getTargetSpec() {
        return targetSpec;
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
