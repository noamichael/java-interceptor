package org.noamichael.java.interceptor.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.noamichael.java.interceptor.api.MethodInterceptor;

/**
 *
 * @author Michael
 */
@Target({ElementType.METHOD})
@Documented
@MethodInterceptor
@Retention(RetentionPolicy.RUNTIME)
public @interface TestAnnotation {}
