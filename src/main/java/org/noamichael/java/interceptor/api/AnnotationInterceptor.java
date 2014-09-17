package org.noamichael.java.interceptor.api;

import java.lang.annotation.Annotation;

/**
 *
 * @author Michael
 * @param <T>
 */
public interface AnnotationInterceptor<T extends Annotation> {

    public void preInvoke(T annotation);

    public void postInvoke(T annotation);
    
    public Class<T> getSupportedInterceptor();
    
}
