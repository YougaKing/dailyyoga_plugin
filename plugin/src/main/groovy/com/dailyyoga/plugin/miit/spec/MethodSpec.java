package com.dailyyoga.plugin.miit.spec;


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
        return parameters == null ? new String[0] : parameters;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public static MethodSpec create(String declaring,
                                    String returnType,
                                    String name,
                                    boolean isStatic) {
        return create(
                declaring,
                returnType,
                name,
                null,
                isStatic);
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
        if (str == null || str.length() == 0) {
            return new String[0];
        }
        return str.split(",");
    }
}
