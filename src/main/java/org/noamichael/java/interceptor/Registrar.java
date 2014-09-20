package org.noamichael.java.interceptor;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.noamichael.java.interceptor.api.AnnotationInterceptor;
import org.noamichael.java.interceptor.api.EndPoint;
import org.noamichael.java.interceptor.api.Proxy;
import org.noamichael.java.interceptor.test.ProxyListTest;
import org.noamichael.utils.se.ClassPathScanner;
import org.noamichael.utils.se.ClassPathScanner.ScannerSearchResult;
import org.noamichael.utils.se.ObjectUtil;

/**
 *
 * @author Michael
 */
public class Registrar {

    private static final List<FutureProxy> FUTURE_PROXIES = new ArrayList();
    private static final Registrar CURRENT_INSTANCE = new Registrar();
    

    private Object main;

    public static Registrar getCurrentInstance() {
        return CURRENT_INSTANCE;
    }

    private Registrar() {

    }

    public void startApplication(Object main) {
        this.main = main;
        readProxies();
    }

    private void readProxies() throws RuntimeException {
        List<ScannerSearchResult> searchResults = ClassPathScanner.scanProjectForAnnotation(Proxy.class, ElementType.FIELD);
        for (ScannerSearchResult result : searchResults) {
            List<Field> fields = (List<Field>) result.getResult();
            for (Field f : fields) {
                Predicate<Class> isEndpoint = (clazz) -> f.getType().isAssignableFrom(clazz) && !clazz.isInterface() && clazz.isAnnotationPresent(EndPoint.class);
                List<ScannerSearchResult> classes = ClassPathScanner.scanForClasses(isEndpoint);
                System.out.println(classes);
                if (!classes.isEmpty()) {
                    try {
                        f.setAccessible(true);
                        Object proxy = AnnotatedProxy.newInstanace(ObjectUtil.newUnknownInstance(classes.get(0).getResultClass()), (Class<? extends AnnotationInterceptor>) null);
                        System.out.println("Proxy: " + proxy);
                        f.set(main, proxy);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
        System.out.println(searchResults);
    }

    public class FutureProxy {

        private String name;
        private String clazz;

        public FutureProxy() {
        }

        public FutureProxy(String name, String clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the clazz
         */
        public String getClazz() {
            return clazz;
        }

        /**
         * @param clazz the clazz to set
         */
        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            return "FutureProxy{" + "name=" + name + ", clazz=" + clazz + '}';
        }

    }

}
