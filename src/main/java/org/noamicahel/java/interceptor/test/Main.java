package org.noamicahel.java.interceptor.test;

import org.noamicahel.java.interceptor.AnnotatedProxy;
import org.noamicahel.java.interceptor.Registrar;

/**
 *
 * @author Michael
 */
public class Main {

    public static void main(String[] args) {
        TestInterface testInterface = AnnotatedProxy.newInstanace(new TestClass(), TestAnnotationInterceptor.class);
        testInterface.test();
        Registrar.getCurrentInstance();
    }
}
