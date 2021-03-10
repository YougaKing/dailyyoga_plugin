package com.dailyyoga.plugin.droidassist.transform;


import com.dailyyoga.plugin.droidassist.ex.DroidAssistBadStatementException;
import com.dailyyoga.plugin.droidassist.spec.SourceSpec;
import com.dailyyoga.plugin.droidassist.util.ClassUtils;
import com.dailyyoga.plugin.droidassist.util.Logger;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.expr.MethodCall;

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

    public String getTarget() {
        return target;
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

    private CtMember getSourceMember(boolean declared) throws NotFoundException {
        CtMember sourceMember = null;
        CtClass sourceClass = getSourceClass();
        String name = sourceSpec.getName();
        String signature = sourceSpec.getSignature();
        if (sourceSpec.getKind() == SourceSpec.Kind.METHOD) {
            sourceMember = declared ? ClassUtils.getDeclaredMethod(sourceClass, name, signature)
                    : ClassUtils.getMethod(sourceClass, name, signature);
        }
        return sourceMember;
    }

    protected boolean isVoidSourceReturnType() throws NotFoundException {
        return Descriptor.getReturnType(sourceSpec.getSignature(), classPool) == CtClass.voidType;
    }

    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            String name,
            String signature,
            boolean declared)
            throws NotFoundException {
        return isMatchSourceMethod(insnClass, true, name, signature, declared);
    }

    @SuppressWarnings("ConstantConditions")
    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            boolean checkClass,
            String name,
            String signature,
            boolean declared)
            throws NotFoundException {
        return isMatchSourceMethod(insnClass, checkClass, name, signature, null, declared);
    }

    @SuppressWarnings("ConstantConditions")
    protected boolean isMatchSourceMethod(
            CtClass insnClass,
            boolean checkClass,
            String name,
            String signature,
            CtMethod method,
            boolean declared)
            throws NotFoundException {
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
                    // TODO: 3/9/21
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

    protected String getReplaceStatement(
            String sourceClassName,
            MethodCall methodCall,
            String statement) {
        String where = methodCall.where().getLongName();
        int line = methodCall.getLineNumber();
        String name = methodCall.getMethodName();
        String fileName = methodCall.getFileName();
        return getReplaceStatement(statement, where, line, name, sourceClassName, fileName);
    }

    private String getReplaceStatement(
            String statement,
            String where,
            int line,
            String name,
            String className,
            String fileName) {
        fileName = fileName == null ? "unknown" : fileName;
        className = className == null ? "unknown" : className;
        name = name == null ? "unknown" : name;

        statement = statement.replaceAll(
                Pattern.quote("$where"), Matcher.quoteReplacement(where));

        statement = statement.replaceAll(
                Pattern.quote("$class"), Matcher.quoteReplacement(className));
        statement = statement.replaceAll(
                Pattern.quote("$line"), Matcher.quoteReplacement(String.valueOf(line)));
        statement = statement.replaceAll(
                Pattern.quote("$name"), Matcher.quoteReplacement(name));
        statement = statement.replaceAll(
                Pattern.quote("$file"), Matcher.quoteReplacement(fileName));
        return statement;
    }
}
