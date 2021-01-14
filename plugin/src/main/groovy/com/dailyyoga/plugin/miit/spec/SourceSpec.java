package com.dailyyoga.plugin.miit.spec;



import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadStatementException;
import com.dailyyoga.plugin.miit.ex.DailyyogaMIITBadTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a parser for custom aop pointcut expression.
 * <p>
 * Such as <pre>int android.util.Log.d(java.lang.String,java.lang.String)</pre>
 */
public class SourceSpec {
    private Type type;
    private Type declaring;
    private String name;
    private Type[] parameters;
    private Kind kind;
    private String signature;
    private boolean extend;
    private boolean annotation;

    public SourceSpec(Kind kind, Type type, Type declaring, String name, Type[] parameters) {
        this(kind, type, declaring, name, parameters, false);
    }

    public SourceSpec(Kind kind, Type type, Type declaring, String name, Type[] parameters, boolean annotation) {
        this.kind = kind;
        this.type = type;
        this.declaring = declaring;
        this.name = name;
        this.parameters = parameters;
        this.annotation = annotation;
        if (type != null) {
            this.signature = typesToSignature(type, parameters, true);
        }
    }

    public SourceSpec setExtend(boolean extend) {
        this.extend = extend;
        return this;
    }

    public boolean isExtend() {
        return extend;
    }

    public boolean isAnnotation() {
        return annotation;
    }

    public Type getType() {
        return type;
    }

    public Type getDeclaring() {
        return declaring;
    }

    public String getName() {
        return name;
    }

    public Type[] getParameters() {
        return parameters;
    }

    public Kind getKind() {
        return kind;
    }

    public String getSignature() {
        return signature;
    }

    public String getDeclaringClassName() {
        return declaring.getPackageName() + "." + declaring.getClassName();
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

    private static String typesToSignature(
            Type returnType,
            Type[] paramTypes,
            boolean eraseGenerics) {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        for (Type paramType : paramTypes) {
            if (eraseGenerics) {
                buf.append(paramType.getErasureSignature());
            } else {
                buf.append(paramType.getSignature());
            }
        }
        buf.append(")");
        if (eraseGenerics) {
            buf.append(returnType.getErasureSignature());
        } else {
            buf.append(returnType.getSignature());
        }
        return buf.toString();
    }


    public static SourceSpec fromString(String source, String kind, boolean extend) {
        SourceSpec sourceSpec;
        if (source.contains("/") || source.contains(";")) {
            throw new DailyyogaMIITBadTypeException("Bad type :" + source);
        } else if (source.startsWith("@")) {
            throw new DailyyogaMIITBadTypeException("Bad type :" + source);
        } else {
            sourceSpec = fromJavaPattern(source, kind);
        }
        sourceSpec.setExtend(extend);
        return sourceSpec;
    }

    private static SourceSpec fromJavaPattern(String source, String kind) {
        Kind kd = Kind.valueOf(kind);
        if (kd == Kind.METHOD) {
            checkMethodSource(source);
            return methodFromString(source);
        }
        throw new DailyyogaMIITBadTypeException("Bad type :" + source);
    }

    private static SourceSpec methodFromString(String str) {
        str = str.trim();
        int len = str.length();
        int i = 0;
        while (Character.isWhitespace(str.charAt(i))) i++;

        int start = i;
        while (i < len && !Character.isWhitespace(str.charAt(i))) i++;

        String returnStatement;
        if (i == len) {
            i = 0;
            returnStatement = "";
        } else {
            returnStatement = str.substring(start, i);
        }
        Type returnTy;
        Type declaringTy;
        String[] paramTypeNames;
        String name;

        returnTy = Type.forName(returnStatement);
        start = i;
        i = str.indexOf('(', i);
        i = str.lastIndexOf('.', i);
        declaringTy = Type.forName(str.substring(start, i).trim());

        start = ++i;
        i = str.indexOf('(', i);
        name = str.substring(start, i).trim();
        start = ++i;
        i = str.indexOf(')', i);

        if (name.equals("<init>")) {
            throw new DailyyogaMIITBadTypeException("Bad type :" + str);
        }
        paramTypeNames = parseParamTypeNames(str.substring(start, i).trim());

        Type[] paramTys = Type.forNames(paramTypeNames);
        return new SourceSpec(
                Kind.METHOD,
                returnTy,
                declaringTy,
                name,
                paramTys);
    }

    private static void checkMethodSource(String source) {
        String returnTypeEx = "\\s*(boolean|byte|char|double|float|int|long|short|void|([A-Za-z0-9_$]+\\.)*[A-Za-z0-9_$]+)(\\[\\])?";
        String declaringEx = "([A-Za-z0-9_$]+\\.)*[A-Za-z0-9_$]+\\.[A-Za-z0-9_$]+";
        String paramEx = "\\(\\s*((boolean|byte|char|double|float|int|long|short|void|([A-Za-z0-9_$]+\\.)*[A-Za-z0-9_$]+)(\\[\\])?\\s*,?)*\\)\\s*";
        String validMethod = "A valid method expression should be [returnType packageName.className.methodName(paramType,paramType)]";
        if (!source.matches(returnTypeEx + "\\s+" + declaringEx + paramEx)) {
            String reason;
            {
                Pattern pattern = Pattern.compile(returnTypeEx + "\\s+");
                Matcher matcher = pattern.matcher(source);
                boolean hasReturnTypeEx = matcher.find();
                if (!hasReturnTypeEx) {
                    reason = "Return type for the method is missing";
                    throwBadStatementException("Invalid java method expression: [" + source + "], " +
                            "reason: " + reason + ", " + validMethod);
                }
            }
            {
                Pattern pattern = Pattern.compile(paramEx);
                Matcher matcher = pattern.matcher(source);
                boolean hasParamEx = matcher.find();
                if (!hasParamEx) {
                    reason = "Parameters for the method is missing";
                    throwBadStatementException("Invalid java method expression: [" + source + "], " +
                            "reason: " + reason + " , " + validMethod);
                }
            }

            throwBadStatementException("Invalid java method expression: [" + source + "]" + ", " + validMethod);
        }
    }

    private static void throwBadStatementException(String msg) {
        throw new DailyyogaMIITBadStatementException(msg);
    }

    public enum Kind {
        METHOD
    }

    @SuppressWarnings("WeakerAccess")
    public static class Type {
        private String signature;
        private String signatureErasure;
        private String packageName;
        private String className;

        public static final Type[] NONE = new Type[0];

        public static final Type BOOLEAN = forPrimitiveType("Z");
        public static final Type BYTE = forPrimitiveType("B");
        public static final Type CHAR = forPrimitiveType("C");
        public static final Type DOUBLE = forPrimitiveType("D");
        public static final Type FLOAT = forPrimitiveType("F");
        public static final Type INT = forPrimitiveType("I");
        public static final Type LONG = forPrimitiveType("J");
        public static final Type SHORT = forPrimitiveType("S");
        public static final Type VOID = forPrimitiveType("V");

        Type(String signature) {
            this.signature = signature;
            this.signatureErasure = signature;
        }

        Type(String signature, String signatureErasure) {
            this.signature = signature;
            this.signatureErasure = signatureErasure;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "signature='" + signature + '\'' +
                    ", signatureErasure='" + signatureErasure + '\'' +
                    ", packageName='" + getPackageName() + '\'' +
                    ", className='" + getClassName() + '\'' +
                    '}';
        }

        public String getSignature() {
            return signature;
        }

        public String getErasureSignature() {
            if (signatureErasure == null) {
                return signature;
            }
            return signatureErasure;
        }

        public String getPackageName() {
            if (packageName == null) {
                String name = getName();
                int angly = name.indexOf('<');
                if (angly != -1) {
                    name = name.substring(0, angly);
                }
                int index = name.lastIndexOf('.');
                if (index == -1) {
                    packageName = "";
                } else {
                    packageName = name.substring(0, index);
                }
            }
            return packageName;
        }

        public String getName() {
            return signatureToName(signature);
        }

        public String getClassName() {
            if (className == null) {
                String name = getName();
                if (name.contains("<")) {
                    name = name.substring(0, name.indexOf("<"));
                }
                int index = name.lastIndexOf('.');
                if (index == -1) {
                    className = name;
                } else {
                    className = name.substring(index + 1);
                }
            }
            return className;
        }

        public static Type forName(String name) {
            return forSignature(nameToSignature(name));
        }

        public static Type[] forNames(String[] names) {
            Type[] ret = new Type[names.length];
            for (int i = 0, len = names.length; i < len; i++) {
                ret[i] = Type.forName(names[i]);
            }
            return ret;
        }

        public static Type forPrimitiveType(String signature) {
            return new Type(signature);
        }

        public static Type forSignature(String signature) {
            assert !(signature.startsWith("L") && signature.contains("<"));
            switch (signature.charAt(0)) {
                case 'B':
                    return Type.BYTE;
                case 'C':
                    return Type.CHAR;
                case 'D':
                    return Type.DOUBLE;
                case 'F':
                    return Type.FLOAT;
                case 'I':
                    return Type.INT;
                case 'J':
                    return Type.LONG;
                case 'L':
                    return createTypeFromSignature(signature);
                case 'S':
                    return Type.SHORT;
                case 'V':
                    return Type.VOID;
                case 'Z':
                    return Type.BOOLEAN;
                case '[':
                    return createTypeFromSignature(signature);
                default:
                    throw new DailyyogaMIITBadTypeException("Bad type signature " + signature);
            }
        }

        private static String nameToSignature(String name) {
            int len = name.length();
            if (len < 8) {
                if (name.equals("int")) return "I";
                if (name.equals("void")) return "V";
                if (name.equals("long")) return "J";
                if (name.equals("boolean")) return "Z";
                if (name.equals("double")) return "D";
                if (name.equals("float")) return "F";
                if (name.equals("byte")) return "B";
                if (name.equals("short")) return "S";
                if (name.equals("char")) return "C";
                if (name.equals("?")) return name;
            }
            if (len == 0) {
                throw new DailyyogaMIITBadTypeException("Bad type name: " + name);
            }
            if (name.endsWith("[]")) {
                return "[" + nameToSignature(name.substring(0, name.length() - 2));
            }

            if (name.charAt(0) == '[') {
                return name.replace('.', '/');
            }

            String packageName;
            int angly = name.indexOf('<');
            if (angly != -1) {
                name = name.substring(0, angly);
            }
            int index = name.lastIndexOf('.');
            if (index == -1) {
                packageName = "";
            } else {
                packageName = name.substring(0, index);
            }
            if (packageName.equals("")) {
                try {
                    String jlang = "java.lang." + name;
                    Class.forName(jlang);
                    name = jlang;
                } catch (ClassNotFoundException ignore) {
                }
            }
            return "L" + name.replace('.', '/') + ';';
        }

        public static Type createTypeFromSignature(String signature) {
            char firstChar = signature.charAt(0);
            if (firstChar == '[') {
                int dims = 0;
                while (signature.charAt(dims) == '[') {
                    dims++;
                }
                Type componentType = createTypeFromSignature(signature.substring(dims));
                return new Type(
                        signature,
                        signature.substring(0, dims) +
                                componentType.getErasureSignature());
            } else if (signature.length() == 1) {
                switch (firstChar) {
                    case 'V':
                        return Type.VOID;
                    case 'Z':
                        return Type.BOOLEAN;
                    case 'B':
                        return Type.BYTE;
                    case 'C':
                        return Type.CHAR;
                    case 'D':
                        return Type.DOUBLE;
                    case 'F':
                        return Type.FLOAT;
                    case 'I':
                        return Type.INT;
                    case 'J':
                        return Type.LONG;
                    case 'S':
                        return Type.SHORT;
                    default:
                        return null;
                }
            } else if (firstChar == 'L') {
                int leftAngleBracket = signature.indexOf('<');

                if (leftAngleBracket == -1) {
                    return new Type(signature);
                }
            }
            return new Type(signature);
        }

        private static String signatureToName(String signature) {
            int length = signature.length();
            switch (signature.charAt(0)) {
                case 'B':
                    return "byte";
                case 'C':
                    return "char";
                case 'D':
                    return "double";
                case 'F':
                    return "float";
                case 'I':
                    return "int";
                case 'J':
                    return "long";
                case 'L':
                    return signature.substring(1, length - 1).replace('/', '.');
                case 'S':
                    return "short";
                case 'V':
                    return "void";
                case 'Z':
                    return "boolean";
                case '[':
                    return signatureToName(signature.substring(1, length)) + "[]";
                default:
                    throw new DailyyogaMIITBadTypeException("Bad type signature: " + signature);
            }
        }

        static Object[] signatureToTypes(String sig) {
            boolean hasParameters = sig.charAt(1) != ')';
            if (hasParameters) {
                List<Type> l = new ArrayList<>();
                int i = 1;
                boolean hasAnyAnglies = sig.indexOf('<') != -1;
                while (true) {
                    char c = sig.charAt(i);
                    if (c == ')') {
                        break;
                    }
                    int start = i;
                    while (c == '[') {
                        c = sig.charAt(++i);
                    }
                    if (c == 'L' || c == 'P') {
                        int nextSemicolon = sig.indexOf(';', start);
                        int firstAngly = (hasAnyAnglies ? sig.indexOf('<', start) : -1);
                        if (!hasAnyAnglies || firstAngly == -1 || firstAngly > nextSemicolon) {
                            i = nextSemicolon + 1;
                            l.add(Type.forSignature(sig.substring(start, i)));
                        } else {
                            boolean endOfSigReached = false;
                            int posn = firstAngly;
                            int genericDepth = 0;
                            while (!endOfSigReached) {
                                switch (sig.charAt(posn)) {
                                    case '<':
                                        genericDepth++;
                                        break;
                                    case '>':
                                        genericDepth--;
                                        break;
                                    case ';':
                                        if (genericDepth == 0) {
                                            endOfSigReached = true;
                                        }
                                        break;
                                    default:
                                }
                                posn++;
                            }
                            i = posn;
                            l.add(Type.forSignature(sig.substring(start, i)));
                        }
                    } else {
                        i++;
                        l.add(Type.forSignature(sig.substring(start, i)));
                    }
                }
                Type[] paramTypes = l.toArray(new Type[l.size()]);
                Type returnType = Type.forSignature(sig.substring(i + 1, sig.length()));
                return new Object[]{returnType, paramTypes};
            } else {
                Type returnType = Type.forSignature(sig.substring(2));
                return new Object[]{returnType, Type.NONE};
            }
        }
    }
}
