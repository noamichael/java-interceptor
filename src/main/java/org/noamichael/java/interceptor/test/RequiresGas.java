package org.noamichael.java.interceptor.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.noamichael.java.interceptor.api.MethodInterceptor;

/**
 *
 * @author Michael
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@MethodInterceptor
public @interface RequiresGas {
    int value() default 10;
}
