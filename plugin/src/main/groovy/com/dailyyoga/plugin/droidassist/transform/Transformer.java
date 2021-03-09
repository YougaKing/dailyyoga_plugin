package com.dailyyoga.plugin.droidassist.transform;


import com.dailyyoga.plugin.droidassist.ex.DroidAssistNotFoundException;
import com.dailyyoga.plugin.droidassist.spec.ClassFilterSpec;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public abstract class Transformer {

    public ClassPool classPool;
    public com.dailyyoga.plugin.droidassist.spec.ClassFilterSpec classFilterSpec = new ClassFilterSpec();

    //Transformer name
    public abstract String getName();

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

    public void check() {
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
