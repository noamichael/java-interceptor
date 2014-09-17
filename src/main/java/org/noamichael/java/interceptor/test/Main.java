package org.noamichael.java.interceptor.test;

import org.noamichael.java.interceptor.AnnotatedProxy;
import org.noamichael.java.interceptor.Registrar;

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
