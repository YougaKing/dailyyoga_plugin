package com.dailyyoga.plugin.miit.transform;


import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadTypeException;
import com.dailyyoga.plugin.miit.spec.MethodSpec;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.expr.MethodCall;

public abstract class SourceTargetTransformer extends Transformer {

    private MethodSpec sourceSpec;
    private MethodSpec targetSpec;

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

    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            String name,
            String signature,
            boolean declared)
            throws NotFoundException {
        return isMatchSourceMethod(insnClass, true, name, signature, declared);
    }

    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            boolean checkClass,
            String name,
            String signature,
            boolean declared)
            throws NotFoundException {
        return isMatchSourceMethod(insnClass, checkClass, name, signature, null, declared);
    }

    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            boolean checkClass,
            String name,
            String signature,
            CtMethod method,
            boolean declared)
            throws NotFoundException {
        if (method != null && sourceSpec.isAnnotation()) {
            return method.hasAnnotation(getSourceDeclaringClassName());
        }
        boolean match = true;
        do {
            if (!name.equals(sourceSpec.getName())
                    || !signature.equals(sourceSpec.getSignature())) {
                match = false;
                break;
            }

            if (checkClass) {
                boolean matchClass = false;
                if (sourceSpec.isExtend()) {
                    CtClass sourceClass = getSourceClass();
                    for (CtClass itf : tryGetInterfaces(insnClass)) {
                        if (itf == sourceClass) {
                            matchClass = true;
                            break;
                        }
                    }
                    if (!matchClass) {
                        matchClass = insnClass.subclassOf(sourceClass);
                    }
                } else {
                    matchClass = insnClass.getName().equals(getSourceDeclaringClassName());
                }
                if (!matchClass) {
                    match = false;
                    break;
                }
            }

            if (sourceSpec.getKind() == SourceSpec.Kind.METHOD) {
                CtMember member = getSourceMember(declared);
                boolean visible = member.visibleFrom(insnClass);
                if (!visible) {
                    match = false;
                    break;
                }
            }

        } while (false);
        return match;
    }

    protected String replaceInstrument(
            String sourceClassName,
            MethodCall methodCall,
            String statement)
            throws CannotCompileException {
        String replacement = getReplaceStatement(sourceClassName, methodCall, statement);
        try {
            String s = replacement.replaceAll("\n", "");
            methodCall.replace(s);
        } catch (CannotCompileException e) {
            Logger.error("Replace method call instrument error with statement: "
                    + statement + "\n", e);
            throw new DroidAssistBadStatementException(e);
        }
        return replacement;
    }
}
