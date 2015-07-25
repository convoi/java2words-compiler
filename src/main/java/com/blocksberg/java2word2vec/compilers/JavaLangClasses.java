package com.blocksberg.java2word2vec.compilers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * List of java.lang classes, which need not be imported.
 *
 * @author jh
 */
public class JavaLangClasses {
    private static final Map<String, String> javaLangTypes = new HashMap<>();

    static {

        javaLangTypes.put("Appendable", "java.lang.Appendable");
        javaLangTypes.put("AutoCloseable", "java.lang.AutoCloseable");
        javaLangTypes.put("CharSequence", "java.lang.CharSequence");
        javaLangTypes.put("Cloneable", "java.lang.Cloneable");
        javaLangTypes.put("Comparable", "java.lang.Comparable");
        javaLangTypes.put("Iterable", "java.lang.Iterable");
        javaLangTypes.put("Readable", "java.lang.Readable");
        javaLangTypes.put("Runnable", "java.lang.Runnable");
        javaLangTypes.put("Thread.UncaughtExceptionHandler", "java.lang.Thread.UncaughtExceptionHandler");
        javaLangTypes.put("Boolean", "java.lang.Boolean");
        javaLangTypes.put("Byte", "java.lang.Byte");
        javaLangTypes.put("Character", "java.lang.Character");
        javaLangTypes.put("Character.Subset", "java.lang.Character.Subset");
        javaLangTypes.put("Character.UnicodeBlock", "java.lang.Character.UnicodeBlock");
        javaLangTypes.put("Class", "java.lang.Class");
        javaLangTypes.put("ClassLoader", "java.lang.ClassLoader");
        javaLangTypes.put("ClassValue", "java.lang.ClassValue");
        javaLangTypes.put("Compiler", "java.lang.Compiler");
        javaLangTypes.put("Double", "java.lang.Double");
        javaLangTypes.put("Enum", "java.lang.Enum");
        javaLangTypes.put("Float", "java.lang.Float");
        javaLangTypes.put("InheritableThreadLocal", "java.lang.InheritableThreadLocal");
        javaLangTypes.put("Integer", "java.lang.Integer");
        javaLangTypes.put("Long", "java.lang.Long");
        javaLangTypes.put("Math", "java.lang.Math");
        javaLangTypes.put("Number", "java.lang.Number");
        javaLangTypes.put("Object", "java.lang.Object");
        javaLangTypes.put("Package", "java.lang.Package");
        javaLangTypes.put("Process", "java.lang.Process");
        javaLangTypes.put("ProcessBuilder", "java.lang.ProcessBuilder");
        javaLangTypes.put("ProcessBuilder.Redirect", "java.lang.ProcessBuilder.Redirect");
        javaLangTypes.put("Runtime", "java.lang.Runtime");
        javaLangTypes.put("RuntimePermission", "java.lang.RuntimePermission");
        javaLangTypes.put("SecurityManager", "java.lang.SecurityManager");
        javaLangTypes.put("Short", "java.lang.Short");
        javaLangTypes.put("StackTraceElement", "java.lang.StackTraceElement");
        javaLangTypes.put("StrictMath", "java.lang.StrictMath");
        javaLangTypes.put("String", "java.lang.String");
        javaLangTypes.put("StringBuffer", "java.lang.StringBuffer");
        javaLangTypes.put("StringBuilder", "java.lang.StringBuilder");
        javaLangTypes.put("System", "java.lang.System");
        javaLangTypes.put("Thread", "java.lang.Thread");
        javaLangTypes.put("ThreadGroup", "java.lang.ThreadGroup");
        javaLangTypes.put("ThreadLocal", "java.lang.ThreadLocal");
        javaLangTypes.put("Throwable", "java.lang.Throwable");
        javaLangTypes.put("Void", "java.lang.Void");
        javaLangTypes.put("Character.UnicodeScript", "java.lang.Character.UnicodeScript");
        javaLangTypes.put("ProcessBuilder.Redirect.Type", "java.lang.ProcessBuilder.Redirect.Type");
        javaLangTypes.put("Thread.State", "java.lang.Thread.State");
        javaLangTypes.put("ArithmeticException", "java.lang.ArithmeticException");
        javaLangTypes.put("ArrayIndexOutOfBoundsException", "java.lang.ArrayIndexOutOfBoundsException");
        javaLangTypes.put("ArrayStoreException", "java.lang.ArrayStoreException");
        javaLangTypes.put("ClassCastException", "java.lang.ClassCastException");
        javaLangTypes.put("ClassNotFoundException", "java.lang.ClassNotFoundException");
        javaLangTypes.put("CloneNotSupportedException", "java.lang.CloneNotSupportedException");
        javaLangTypes.put("EnumConstantNotPresentException", "java.lang.EnumConstantNotPresentException");
        javaLangTypes.put("Exception", "java.lang.Exception");
        javaLangTypes.put("IllegalAccessException", "java.lang.IllegalAccessException");
        javaLangTypes.put("IllegalArgumentException", "java.lang.IllegalArgumentException");
        javaLangTypes.put("IllegalMonitorStateException", "java.lang.IllegalMonitorStateException");
        javaLangTypes.put("IllegalStateException", "java.lang.IllegalStateException");
        javaLangTypes.put("IllegalThreadStateException", "java.lang.IllegalThreadStateException");
        javaLangTypes.put("IndexOutOfBoundsException", "java.lang.IndexOutOfBoundsException");
        javaLangTypes.put("InstantiationException", "java.lang.InstantiationException");
        javaLangTypes.put("InterruptedException", "java.lang.InterruptedException");
        javaLangTypes.put("NegativeArraySizeException", "java.lang.NegativeArraySizeException");
        javaLangTypes.put("NoSuchFieldException", "java.lang.NoSuchFieldException");
        javaLangTypes.put("NoSuchMethodException", "java.lang.NoSuchMethodException");
        javaLangTypes.put("NullPointerException", "java.lang.NullPointerException");
        javaLangTypes.put("NumberFormatException", "java.lang.NumberFormatException");
        javaLangTypes.put("ReflectiveOperationException", "java.lang.ReflectiveOperationException");
        javaLangTypes.put("RuntimeException", "java.lang.RuntimeException");
        javaLangTypes.put("SecurityException", "java.lang.SecurityException");
        javaLangTypes.put("StringIndexOutOfBoundsException", "java.lang.StringIndexOutOfBoundsException");
        javaLangTypes.put("TypeNotPresentException", "java.lang.TypeNotPresentException");
        javaLangTypes.put("UnsupportedOperationException", "java.lang.UnsupportedOperationException");
        javaLangTypes.put("AbstractMethodError", "java.lang.AbstractMethodError");
        javaLangTypes.put("AssertionError", "java.lang.AssertionError");
        javaLangTypes.put("BootstrapMethodError", "java.lang.BootstrapMethodError");
        javaLangTypes.put("ClassCircularityError", "java.lang.ClassCircularityError");
        javaLangTypes.put("ClassFormatError", "java.lang.ClassFormatError");
        javaLangTypes.put("Error", "java.lang.Error");
        javaLangTypes.put("ExceptionInInitializerError", "java.lang.ExceptionInInitializerError");
        javaLangTypes.put("IllegalAccessError", "java.lang.IllegalAccessError");
        javaLangTypes.put("IncompatibleClassChangeError", "java.lang.IncompatibleClassChangeError");
        javaLangTypes.put("InstantiationError", "java.lang.InstantiationError");
        javaLangTypes.put("InternalError", "java.lang.InternalError");
        javaLangTypes.put("LinkageError", "java.lang.LinkageError");
        javaLangTypes.put("NoClassDefFoundError", "java.lang.NoClassDefFoundError");
        javaLangTypes.put("NoSuchFieldError", "java.lang.NoSuchFieldError");
        javaLangTypes.put("NoSuchMethodError", "java.lang.NoSuchMethodError");
        javaLangTypes.put("OutOfMemoryError", "java.lang.OutOfMemoryError");
        javaLangTypes.put("StackOverflowError", "java.lang.StackOverflowError");
        javaLangTypes.put("ThreadDeath", "java.lang.ThreadDeath");
        javaLangTypes.put("UnknownError", "java.lang.UnknownError");
        javaLangTypes.put("UnsatisfiedLinkError", "java.lang.UnsatisfiedLinkError");
        javaLangTypes.put("UnsupportedClassVersionError", "java.lang.UnsupportedClassVersionError");
        javaLangTypes.put("VerifyError", "java.lang.VerifyError");
        javaLangTypes.put("VirtualMachineError", "java.lang.VirtualMachineError");
        javaLangTypes.put("Deprecated", "java.lang.Deprecated");
        javaLangTypes.put("FunctionalInterface", "java.lang.FunctionalInterface");
        javaLangTypes.put("Override", "java.lang.Override");
        javaLangTypes.put("SafeVarargs", "java.lang.SafeVarargs");
        javaLangTypes.put("SuppressWarnings", "java.lang.SuppressWarnings");

    }

    public static final Map<String, String> JAVA_LANG_TYPES = Collections.unmodifiableMap(javaLangTypes);
}
