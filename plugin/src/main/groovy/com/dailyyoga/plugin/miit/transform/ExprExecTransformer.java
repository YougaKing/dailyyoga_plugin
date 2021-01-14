package com.dailyyoga.plugin.miit.transform;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 1/12/21 2:50 PM
 * @description:
 */
public abstract class ExprExecTransformer extends SourceTargetTransformer {

    protected static final String METHOD_CALL = "MethodCall";

    protected static final String METHOD = "method";

    public static final String TRANSFORM_EXEC = "exec";
    public static final String TRANSFORM_EXPR = "expr";

    class Editor extends ExprEditor {
        CtBehavior behavior;
        AtomicBoolean modified;
    }

    protected abstract String getExecuteType();

    protected Set<String> getExtraExecuteTypes() {
        return Sets.newHashSet();
    }

    protected abstract String getTransformType();

    @Override
    public String getPrettyName() {
        StringBuilder name = new StringBuilder(getCategoryName() + " [");

        String disposeType = getExecuteType();

        if (METHOD_CALL.equals(disposeType)) {
            name.append("method");
        }

        if (METHOD.equals(disposeType)) {
            name.append("method");
        }

        //expr
        String transformType = getTransformType();
        if (TRANSFORM_EXPR.equals(transformType)) {
            name.append(" call");
        }
        //exec
        if (TRANSFORM_EXEC.equals(getTransformType())) {
            name.append(" exec");
        }
        name.append("]");
        return name.toString();
    }

    @Override
    protected final boolean onTransform(
            CtClass inputClass,
            String inputClassName)
            throws NotFoundException, CannotCompileException {
        //expr
        if (TRANSFORM_EXPR.equals(getTransformType())) {
            return onTransformExpr(inputClass, inputClassName);
        }
        //exec
        if (TRANSFORM_EXEC.equals(getTransformType())) {
            return onTransformExec(inputClass, inputClassName);
        }
        return false;
    }

    private boolean onTransformExpr(
            CtClass inputClass,
            String inputClassName)
            throws NotFoundException, CannotCompileException {

        if (!filterClass(inputClass, inputClassName)) {
            return false;
        }
        if (!execute(inputClass, inputClassName)) {
            return false;
        }

        final Set<String> executeTypes = getExtraExecuteTypes();
        executeTypes.add(getExecuteType());
        final AtomicBoolean modified = new AtomicBoolean(false);
        Editor editor = new Editor() {

            @Override
            public void edit(MethodCall call) throws CannotCompileException {
                if (executeTypes.contains(METHOD_CALL)) {
                    boolean disposed;
                    try {
                        disposed = execute(inputClass, inputClassName, call);
                    } catch (NotFoundException e) {
                        String msg = e.getMessage() + " for input class " + inputClassName;
                        throw new CannotCompileException(msg, e);
                    }
                    modified.set(modified.get() | disposed);
                }
            }
        };

        CtMethod[] declaredMethods = tryGetDeclaredMethods(inputClass);
        for (CtMethod method : declaredMethods) {
            if (instrument(method, editor)) {
                modified.set(true);
            }
        }
        return modified.get();
    }

    private boolean onTransformExec(
            CtClass inputClass,
            String inputClassName)
            throws NotFoundException, CannotCompileException {

        if (!filterClass(inputClass, inputClassName)) {
            return false;
        }
        if (!isMatchSourceClass(inputClass)) {
            return false;
        }
        if (!execute(inputClass, inputClassName)) {
            return false;
        }

        boolean modified = false;
        Set<String> executeTypes = getExtraExecuteTypes();
        executeTypes.add(getExecuteType());
        if (executeTypes.contains(METHOD)) {
            CtMethod[] declaredMethods = tryGetDeclaredMethods(inputClass);
            for (CtMethod method : declaredMethods) {
                if (Modifier.isAbstract(method.getModifiers())) {
                    continue;
                }
                if (execute(inputClass, inputClassName, method)) {
                    modified = true;
                }
            }
        }

        return modified;
    }

    private boolean instrument(CtBehavior behavior, Editor editor) throws CannotCompileException {
        editor.modified = new AtomicBoolean(false);
        editor.behavior = behavior;
        behavior.instrument(editor);
        return editor.modified.get();
    }

    protected boolean filterClass(
            CtClass inputClass,
            String inputClassName)
            throws NotFoundException, CannotCompileException {
        return true;
    }

    protected boolean execute(
            CtClass inputClass,
            String inputClassName)
            throws CannotCompileException, NotFoundException {
        return true;
    }

    protected boolean execute(
            CtClass inputClass,
            String inputClassName,
            CtMethod method)
            throws CannotCompileException, NotFoundException {
        return false;
    }

    protected boolean execute(
            CtClass inputClass,
            String inputClassName,
            MethodCall methodCall)
            throws CannotCompileException, NotFoundException {
        return false;
    }
}