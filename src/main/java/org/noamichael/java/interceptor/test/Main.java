package org.noamichael.java.interceptor.test;

import java.util.List;
import org.noamichael.java.interceptor.Registrar;
import org.noamichael.java.interceptor.api.Proxy;

/**
 *
 * @author Michael
 */
public class Main {
    @Proxy
    private TestInterface testInterface;
    @Proxy
    private List list;

    public static void main(String[] args) {
        Main main = new Main();
        Registrar.getCurrentInstance().startApplication(main);
        System.out.println(main.testInterface);
        System.out.println(main.list.add(main));
        
        
    }
}
