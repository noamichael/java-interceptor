package org.noamichael.java.interceptor.test;

import java.util.List;
import org.noamichael.java.interceptor.Container;
import org.noamichael.java.interceptor.api.Proxy;

/**
 *
 * @author Michael
 */
public class Main {

    @Proxy
    private List list;
    @Proxy
    private Car car;

    public static void main(String[] args) {
        Main main = new Main();
        Container.getCurrentInstance().startApplication(main);
        System.out.println(main.list.add(main));
        while (main.car.hasGas()) {
            main.car.start();
            main.car.stop();
        }

    }
}
