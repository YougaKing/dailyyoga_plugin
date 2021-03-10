package com.dailyyoga.plugin.droidassist.transform;


import com.dailyyoga.plugin.droidassist.ex.DroidAssistNotFoundException;
import com.dailyyoga.plugin.droidassist.spec.ClassFilterSpec;
import com.dailyyoga.plugin.droidassist.util.Logger;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public abstract class Transformer {

    protected ClassPool classPool;
    protected ClassFilterSpec classFilterSpec = new ClassFilterSpec();
    protected boolean abortOnUndefinedClass = false;

    //Transformer name
    public abstract String getName();

    //Category name that transformer belongs to
    public abstract String getCategoryName();

    public abstract String getPrettyName();

    protected abstract boolean onTransform(
            CtClass inputClass,
            String inputClassName)
            throws NotFoundException, CannotCompileException;

    public boolean performTransform(
            CtClass inputClass,
            String className)
            throws NotFoundException, CannotCompileException {

        inputClass.stopPruning(true);
        if (inputClass.isFrozen()) {
            inputClass.defrost();
        }
        beforeTransform();
        return onTransform(inputClass, className);
    }

    protected void beforeTransform() {
    }

    public boolean classAllowed(String className) {
        return classFilterSpec.classAllowed(className);
    }

    public Transformer setClassPool(ClassPool classPool) {
        this.classPool = classPool;
        return this;
    }

    public boolean isAbortOnUndefinedClass() {
        return abortOnUndefinedClass;
    }

    public Transformer setAbortOnUndefinedClass(boolean abortOnUndefinedClass) {
        this.abortOnUndefinedClass = abortOnUndefinedClass;
        return this;
    }

    public void check() {
    }

    //Get class in the class pool
    protected CtClass tryGetClass(String className, String loc) {
        CtClass ctClass = classPool.getOrNull(className);
        if (ctClass == null) {
            String msg = "cannot find " + className + " in " + loc;
            if (abortOnUndefinedClass) {
                throw new DroidAssistNotFoundException(msg);
            } else {
                Logger.warning(msg);
            }
        } else {
            return ctClass;
        }
        return null;
    }

    protected Boolean isInterface(CtClass inputClass) {
        try {
            return inputClass.isInterface();
        } catch (Exception ignore) {
            return null;
        }
    }

    //Get all declared methods of the specified class
    protected CtMethod[] tryGetDeclaredMethods(CtClass inputClass) {
        try {
            return inputClass.getDeclaredMethods();
        } catch (Exception e) {
            String msg = "Cannot get declared methods " + " in " + inputClass.getName();
            throw new DroidAssistNotFoundException(msg);
        }
    }
}
