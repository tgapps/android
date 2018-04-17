package org.aspectj.runtime.reflect;

import java.util.Hashtable;
import java.util.StringTokenizer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.SourceLocation;

public final class Factory {
    private static Object[] NO_ARGS = new Object[0];
    static /* synthetic */ Class class$java$lang$ClassNotFoundException;
    static Hashtable prims = new Hashtable();
    int count = 0;
    String filename;
    Class lexicalClass;
    ClassLoader lookupClassLoader;

    static java.lang.Class makeClass(java.lang.String r1, java.lang.ClassLoader r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.aspectj.runtime.reflect.Factory.makeClass(java.lang.String, java.lang.ClassLoader):java.lang.Class
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = "*";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x000a;
    L_0x0008:
        r0 = 0;
        return r0;
    L_0x000a:
        r0 = prims;
        r0 = r0.get(r3);
        r0 = (java.lang.Class) r0;
        if (r0 == 0) goto L_0x0015;
    L_0x0014:
        return r0;
    L_0x0015:
        if (r4 != 0) goto L_0x001e;
    L_0x0017:
        r1 = java.lang.Class.forName(r3);	 Catch:{ ClassNotFoundException -> 0x001c }
        return r1;	 Catch:{ ClassNotFoundException -> 0x001c }
    L_0x001c:
        r1 = move-exception;	 Catch:{ ClassNotFoundException -> 0x001c }
        goto L_0x0024;	 Catch:{ ClassNotFoundException -> 0x001c }
    L_0x001e:
        r1 = 0;	 Catch:{ ClassNotFoundException -> 0x001c }
        r1 = java.lang.Class.forName(r3, r1, r4);	 Catch:{ ClassNotFoundException -> 0x001c }
        return r1;
        r2 = class$java$lang$ClassNotFoundException;
        if (r2 != 0) goto L_0x0032;
        r2 = "java.lang.ClassNotFoundException";
        r2 = class$(r2);
        class$java$lang$ClassNotFoundException = r2;
        goto L_0x0034;
        r2 = class$java$lang$ClassNotFoundException;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.aspectj.runtime.reflect.Factory.makeClass(java.lang.String, java.lang.ClassLoader):java.lang.Class");
    }

    static {
        prims.put("void", Void.TYPE);
        prims.put("boolean", Boolean.TYPE);
        prims.put("byte", Byte.TYPE);
        prims.put("char", Character.TYPE);
        prims.put("short", Short.TYPE);
        prims.put("int", Integer.TYPE);
        prims.put("long", Long.TYPE);
        prims.put("float", Float.TYPE);
        prims.put("double", Double.TYPE);
    }

    static /* synthetic */ Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    public Factory(String filename, Class lexicalClass) {
        this.filename = filename;
        this.lexicalClass = lexicalClass;
        this.lookupClassLoader = lexicalClass.getClassLoader();
    }

    public StaticPart makeSJP(String kind, Signature sig, int l) {
        int i = this.count;
        this.count = i + 1;
        return new StaticPartImpl(i, kind, sig, makeSourceLoc(l, -1));
    }

    public static JoinPoint makeJP(StaticPart staticPart, Object _this, Object target) {
        return new JoinPointImpl(staticPart, _this, target, NO_ARGS);
    }

    public static JoinPoint makeJP(StaticPart staticPart, Object _this, Object target, Object arg0) {
        return new JoinPointImpl(staticPart, _this, target, new Object[]{arg0});
    }

    public static JoinPoint makeJP(StaticPart staticPart, Object _this, Object target, Object[] args) {
        return new JoinPointImpl(staticPart, _this, target, args);
    }

    public MethodSignature makeMethodSig(String modifiers, String methodName, String declaringType, String paramTypes, String paramNames, String exceptionTypes, String returnType) {
        int i;
        int modifiersAsInt = Integer.parseInt(modifiers, 16);
        Class declaringTypeClass = makeClass(declaringType, this.lookupClassLoader);
        StringTokenizer st = new StringTokenizer(paramTypes, ":");
        int numParams = st.countTokens();
        Class[] paramTypeClasses = new Class[numParams];
        int i2 = 0;
        for (i = 0; i < numParams; i++) {
            paramTypeClasses[i] = makeClass(st.nextToken(), r0.lookupClassLoader);
        }
        st = new StringTokenizer(paramNames, ":");
        numParams = st.countTokens();
        String[] paramNamesArray = new String[numParams];
        for (i = 0; i < numParams; i++) {
            paramNamesArray[i] = st.nextToken();
        }
        StringTokenizer st2 = new StringTokenizer(exceptionTypes, ":");
        int numParams2 = st2.countTokens();
        Class[] exceptionTypeClasses = new Class[numParams2];
        while (true) {
            int i3 = i2;
            if (i3 < numParams2) {
                exceptionTypeClasses[i3] = makeClass(st2.nextToken(), r0.lookupClassLoader);
                i2 = i3 + 1;
            } else {
                return new MethodSignatureImpl(modifiersAsInt, methodName, declaringTypeClass, paramTypeClasses, paramNamesArray, exceptionTypeClasses, makeClass(returnType, r0.lookupClassLoader));
            }
        }
    }

    public SourceLocation makeSourceLoc(int line, int col) {
        return new SourceLocationImpl(this.lexicalClass, this.filename, line);
    }
}
