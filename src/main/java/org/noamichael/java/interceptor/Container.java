package org.noamichael.java.interceptor;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.noamichael.java.interceptor.api.AnnotationInterceptor;
import org.noamichael.java.interceptor.api.EndPoint;
import org.noamichael.java.interceptor.api.MethodInterceptor;
import org.noamichael.java.interceptor.api.Proxy;
import org.noamichael.utils.se.ClassPathScanner;
import org.noamichael.utils.se.ClassPathScanner.ScannerSearchResult;
import org.noamichael.utils.se.ObjectUtil;

/**
 *
 * @author Michael
 */
public final class Container {

    private static final Container CURRENT_INSTANCE = new Container();

    private Object main;

    public static Container getCurrentInstance() {
        return CURRENT_INSTANCE;
    }

    private Container() {

    }

    public void startApplication(Object main) {
        this.main = main;
        injectMainProxies();
    }

    private void injectMainProxies() {
        injectProxies(main);
    }
    
    public void injectProxies(Object instance) {
        List<InterceptorBinding> bindings = findInterceptorBindings();
        List<ScannerSearchResult> searchResults = ClassPathScanner.scanProjectForAnnotation(Proxy.class, ElementType.FIELD);
        for (ScannerSearchResult result : searchResults) {
            List<Field> fields = (List<Field>) result.getResult();
            for (Field f : fields) {
                Predicate<Class<?>> isEndpoint = (clazz) -> f.getType().isAssignableFrom(clazz) && !clazz.isInterface() && clazz.isAnnotationPresent(EndPoint.class);
                List<ScannerSearchResult> endPoints = ClassPathScanner.scanForClasses(isEndpoint);
                if (!endPoints.isEmpty()) {
                    try {
                        f.setAccessible(true);
                        if (f.getType().isInterface()) {
                            Class<?> endPointClass = endPoints.get(0).getResultClass();
                            Class<? extends AnnotationInterceptor<?>>[] endPointInterceptors = getInterceptorsForEndpoint(endPointClass, bindings);
                            Object proxy = ContainerInvocationHandler.newInstanace(ObjectUtil.newUnknownInstance(endPointClass), endPointInterceptors);
                            try {
                                instance.getClass().getDeclaredField(f.getName());
                                f.set(instance, proxy);
                            } catch (NoSuchFieldException e) {
                                //ignore this;
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {
                            try {
                                instance.getClass().getDeclaredField(f.getName());
                                f.set(instance, ObjectUtil.newUnknownInstance(endPoints.get(0).getResultClass()));
                            } catch (NoSuchFieldException e) {
                                //ignore this;
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }

    private Class<? extends AnnotationInterceptor<?>>[] getInterceptorsForEndpoint(Class<?> clazz, List<InterceptorBinding> bindings) {
        Class<? extends AnnotationInterceptor<?>>[] array = (Class<? extends AnnotationInterceptor<?>>[]) new Class<?>[bindings.size()];
        int index = 0;
        for (InterceptorBinding binding : bindings) {
            for (Method m : clazz.getDeclaredMethods()) {
                m.setAccessible(true);
                if (m.isAnnotationPresent(binding.getAnnotation())) {
                    array[index] = binding.getAnnotationInterceptor();
                    index++;
                }
            }
        }
        if (index < array.length) {
            Class<? extends AnnotationInterceptor<?>>[] smallerArray = (Class<? extends AnnotationInterceptor<?>>[]) new Class<?>[index + 1];
            System.arraycopy(array, 0, smallerArray, 0, index + 1);
            return smallerArray;
        }
        return array;
    }

    private List<InterceptorBinding> findInterceptorBindings() {
        List<InterceptorBinding> finalBindings = new ArrayList();
        Predicate<Class<?>> isInterceptorHandler = (clazz)
                -> clazz.isAnnotationPresent(MethodInterceptor.class)
                && AnnotationInterceptor.class.isAssignableFrom(clazz);
        List<ScannerSearchResult> interceptors = ClassPathScanner.scanForClasses(isInterceptorHandler);
        for (ScannerSearchResult result : interceptors) {
            AnnotationInterceptor a = (AnnotationInterceptor) ObjectUtil.newUnknownInstance((Class<?>) result.getResult());
            finalBindings.add(new InterceptorBinding(a.getSupportedInterceptor(), (Class<? extends AnnotationInterceptor<?>>) a.getClass()));
        }

        return finalBindings;
    }

    private class InterceptorBinding {

        private final Class<? extends Annotation> annotation;
        private final Class<? extends AnnotationInterceptor<?>> annotationInterceptor;

        public InterceptorBinding(Class<? extends Annotation> annotation, Class<? extends AnnotationInterceptor<?>> annotationInterceptor) {
            this.annotation = annotation;
            this.annotationInterceptor = annotationInterceptor;
        }

        /**
         * @return the annotation
         */
        public Class<? extends Annotation> getAnnotation() {
            return annotation;
        }

        /**
         * @return the annotationInterceptor
         */
        public Class<? extends AnnotationInterceptor<?>> getAnnotationInterceptor() {
            return annotationInterceptor;
        }
    }
}
