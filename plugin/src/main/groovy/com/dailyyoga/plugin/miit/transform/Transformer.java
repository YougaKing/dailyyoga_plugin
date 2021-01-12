package com.dailyyoga.plugin.miit.transform;


import com.dailyyoga.plugin.miit.ex.DailyyogaMIITNotFoundException;
import com.dailyyoga.plugin.miit.spec.ClassFilterSpec;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public abstract class Transformer {

    public ClassPool classPool;
    public ClassFilterSpec classFilterSpec = new ClassFilterSpec();

    //Transformer name
    public String getName() {
        return "Transformer";
    }

    //Category name that transformer belongs to
    public String getCategoryName() {
        return "Transformer";
    }

    public String getPrettyName() {
        return "Transformer";
    }

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

    public ClassPool getClassPool() {
        return classPool;
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

    //Get all interfaces of the specified class
    protected CtClass[] tryGetInterfaces(CtClass inputClass) {
        try {
            return inputClass.getInterfaces();
        } catch (NotFoundException e) {
            String msg = "Cannot find interface " + e.getMessage() + " in " + inputClass.getName();
            throw new DailyyogaMIITNotFoundException(msg);
        }
    }

    //Get all declared methods of the specified class
    protected CtMethod[] tryGetDeclaredMethods(CtClass inputClass) {
        try {
            return inputClass.getDeclaredMethods();
        } catch (Exception e) {
            String msg = "Cannot get declared methods " + " in " + inputClass.getName();
            throw new DailyyogaMIITNotFoundException(msg);
        }
    }
}
