package com.dailyyoga.plugin.miit.spec;


import java.util.ArrayList;
import java.util.List;

/**
 * This is a parser for custom aop pointcut expression.
 * <p>
 * Such as <pre>int android.util.Log.d(java.lang.String,java.lang.String)</pre>
 */
public class MethodSpec {

    public static final String SOURCE = "Source";
    public static final String TARGET = "Target";

    private String declaring;
    private String returnType;
    private String name;
    private String[] parameters;
    private boolean isStatic;

    public MethodSpec(String declaring, String returnType, String name, String[] parameters) {
        this(declaring, returnType, name, parameters, false);
    }

    public MethodSpec(String declaring, String returnType, String name, String[] parameters, boolean isStatic) {
        this.declaring = declaring;
        this.returnType = returnType;
        this.name = name;
        this.parameters = parameters;
        this.isStatic = isStatic;
    }

    public String getDeclaring() {
        return declaring;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public String[] getParameters() {
        return parameters;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public static MethodSpec create(String declaring,
                                    String returnType,
                                    String name,
                                    String parameters,
                                    boolean isStatic) {

        String[] paramTypeNames = parseParamTypeNames(parameters);

        return new MethodSpec(
                declaring,
                returnType,
                name,
                paramTypeNames,
                isStatic);
    }

    private static String[] parseParamTypeNames(String str) {
        if (str.length() == 0) {
            return new String[0];
        }
        List<String> l = new ArrayList<>();
        int start = 0;
        while (true) {
            int i = str.indexOf(',', start);
            if (i == -1) {
                l.add(str.substring(start).trim());
                break;
            }
            l.add(str.substring(start, i).trim());
            start = i + 1;
        }
        return l.toArray(new String[l.size()]);
    }
}
