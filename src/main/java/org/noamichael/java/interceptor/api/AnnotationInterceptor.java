package org.noamichael.java.interceptor.api;

import java.lang.annotation.Annotation;

/**
 *
 * @author Michael
 * @param <T>
 */
public interface AnnotationInterceptor<T extends Annotation> {

    public void preInvoke(T annotation, Object instance) throws Exception;

    public void postInvoke(T annotation, Object instance) throws Exception;

    public Class<T> getSupportedInterceptor();

}
