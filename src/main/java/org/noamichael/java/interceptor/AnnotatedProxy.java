package org.noamichael.java.interceptor;

import org.noamichael.java.interceptor.api.AnnotationInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 * @param <T>
 */
public class AnnotatedProxy<T> implements InvocationHandler {

    private final List<AnnotationInterceptor> annotationInterceptors;
    private final T object;

    public AnnotatedProxy(T object) {
        this.object = object;
        this.annotationInterceptors = new ArrayList<>();
    }

    public static <T> T newInstanace(T object, Class<? extends AnnotationInterceptor>... interceptors) {
        AnnotatedProxy<T> annotatedProxy = new AnnotatedProxy<>(object);
        annotatedProxy.addInterceptor(interceptors);
        List<Class<?>> interfaces = new ArrayList();
        getInterfaces(object.getClass(), interfaces);
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), interfaces.toArray(new Class<?>[0]), annotatedProxy);
    }

    private static void getInterfaces(Class c, List<Class<?>> interfaces) {
        if (c.getSuperclass() != null) {
            getInterfaces(c.getSuperclass(), interfaces);
        }
        for (Class<?> intrface : c.getInterfaces()) {
            if (!interfaces.contains(intrface)) {
                interfaces.add(intrface);
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        doPreInvoke(method);
        Object result = method.invoke(object, args);
        doPostInvoke(method);
        return result;
    }

    private void doPostInvoke(Method method) {
        try {
            Method implMethod = object.getClass().getMethod(method.getName(), method.getParameterTypes());
            annotationInterceptors.stream().forEach((interceptor) -> {
                if (implMethod.isAnnotationPresent(interceptor.getSupportedInterceptor())) {
                    interceptor.postInvoke(implMethod.getAnnotation(interceptor.getSupportedInterceptor()));
                }
            });
        } catch (Exception e) {
        }
    }

    private void doPreInvoke(Method method) {
        try {
            Method implMethod = object.getClass().getMethod(method.getName(), method.getParameterTypes());
            annotationInterceptors.stream().forEach((interceptor) -> {
                if (implMethod.isAnnotationPresent(interceptor.getSupportedInterceptor())) {
                    interceptor.preInvoke(implMethod.getAnnotation(interceptor.getSupportedInterceptor()));
                }
            });
        } catch (Exception e) {
        }
    }

    public void addInterceptor(Class<? extends AnnotationInterceptor> interceptor) {
        if (interceptor == null) {
            return;
        }
        try {
            AnnotationInterceptor annotationInterceptor = interceptor.getConstructor().newInstance();
            this.annotationInterceptors.add(annotationInterceptor);
        } catch (Exception ex) {
            Logger.getLogger(AnnotatedProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addInterceptor(Class<? extends AnnotationInterceptor>... interceptors) {
        for (Class<? extends AnnotationInterceptor> c : interceptors) {
            addInterceptor(c);
        }
    }

}
