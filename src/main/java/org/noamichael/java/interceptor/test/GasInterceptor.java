package org.noamichael.java.interceptor.test;

import org.noamichael.java.interceptor.api.AnnotationInterceptor;
import org.noamichael.java.interceptor.api.MethodInterceptor;

/**
 *
 * @author Michael
 */
@RequiresGas
@MethodInterceptor
public class GasInterceptor implements AnnotationInterceptor<RequiresGas> {

    
    @Override
    public void preInvoke(RequiresGas annotation, Object instance) throws Exception {
        if (instance instanceof Car) {
            Car car = (Car) instance;
            System.out.println("Current gas: " + car.getGas());
        }
    }

    @Override
    public void postInvoke(RequiresGas annotation, Object instance) throws Exception {
        if (instance instanceof Car) {
            Car car = (Car) instance;
            System.out.println(String.format("Using %d gas units", annotation.value()));
            car.useGas(annotation.value());

        }
    }

    @Override
    public Class<RequiresGas> getSupportedInterceptor() {
        return RequiresGas.class;
    }
}
