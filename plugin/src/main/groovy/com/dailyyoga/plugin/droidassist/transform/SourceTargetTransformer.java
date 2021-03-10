package com.dailyyoga.plugin.droidassist.transform;


import com.dailyyoga.plugin.droidassist.ex.DroidAssistBadStatementException;
import com.dailyyoga.plugin.droidassist.ex.DroidAssistException;
import com.dailyyoga.plugin.droidassist.spec.SourceSpec;
import com.dailyyoga.plugin.droidassist.util.ClassUtils;
import com.dailyyoga.plugin.droidassist.util.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMember;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.AnnotationImpl;
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

    private Class getAnnotationClass() {
        if (annotationClass == null) {
            try {
                annotationClass = getSourceClass().toClass();
            } catch (CannotCompileException | NotFoundException e) {
                try {
                    annotationClass = classPool.getClassLoader().loadClass(getSourceDeclaringClassName());
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return annotationClass;
    }

    protected boolean isVoidSourceReturnType() throws NotFoundException {
        return Descriptor.getReturnType(sourceSpec.getSignature(), classPool) == CtClass.voidType;
    }

    boolean isMatchSourceClass(CtClass insnClass) throws NotFoundException {
        if (sourceSpec.isAnnotation()) {
            return true;
        }
        boolean match;
        Boolean anInterface = isInterface(insnClass);
        if (anInterface == null || anInterface) {
            return false;
        }
        match = insnClass.getName().equals(getSourceDeclaringClassName());
        return match;
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
                boolean matchClass;
                matchClass = insnClass.getName().equals(getSourceDeclaringClassName());
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
                    + statement + "\n" + "replacement:" + replacement + "\n", e);
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
                Pattern.quote("$where"), Matcher.quoteReplacement("\"" + where + "\""));
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

    protected String getReplaceStatement(String sourceClassName, CtMethod method, String statement) {
        statement = replaceAnnotationStatement(method, statement);
        MethodInfo methodInfo = method.getMethodInfo();
        int line = methodInfo.getLineNumber(0);
        String name = method.getName();
        ClassFile classFile2 = method.getDeclaringClass().getClassFile2();
        String fileName = classFile2 == null ? null : classFile2.getSourceFile();
        return getReplaceStatement(statement, null, line, name, sourceClassName, fileName);
    }

    private String replaceAnnotationStatement(CtBehavior behavior, String statement) {
        if (!sourceSpec.isAnnotation() || annotationTargetMembers == null || annotationTargetMembers.isEmpty()) {
            return statement;
        }
        try {
            Object proxy = behavior.getAnnotation(getAnnotationClass());
            if (proxy != null) {
                AnnotationImpl impl = (AnnotationImpl) Proxy.getInvocationHandler(proxy);
                for (Method member : annotationTargetMembers) {
                    Object invoke = impl.invoke(proxy, member, null);
                    statement = statement.replaceAll(
                            Pattern.quote("$" + member.getName()), Matcher.quoteReplacement(invoke.toString()));
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new DroidAssistException(throwable);
        }
        return statement;
    }
}
