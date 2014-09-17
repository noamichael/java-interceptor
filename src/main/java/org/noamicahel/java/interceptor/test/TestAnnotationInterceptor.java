package org.noamicahel.java.interceptor.test;

import org.noamicahel.java.interceptor.api.AnnotationInterceptor;

/**
 *
 * @author Michael
 */
public class TestAnnotationInterceptor implements AnnotationInterceptor<TestAnnotation> {

    @Override
    public void preInvoke(TestAnnotation annotation) {
        System.out.println("Logic before invoke." + annotation.toString());
    }

    @Override
    public void postInvoke(TestAnnotation annotation) {
        System.out.println("Logic after invoke." + annotation.toString());
    }

    @Override
    public Class<TestAnnotation> getSupportedInterceptor() {
        return TestAnnotation.class;
    }

}
