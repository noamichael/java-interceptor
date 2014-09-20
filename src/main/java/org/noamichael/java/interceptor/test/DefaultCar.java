package org.noamichael.java.interceptor.test;

import org.noamichael.java.interceptor.api.EndPoint;
import org.noamichael.java.interceptor.api.Proxy;

/**
 *
 * @author Michael
 */
@EndPoint
public class DefaultCar implements Car {

    private int gas = 100;
    
    @Proxy
    private ProxyListTest test;

    @RequiresGas(50)
    @Override
    public void start() {
        System.out.println("List worked: " + test != null);
        if (gas > 0) {
            System.out.println("Starting the car");
        } else {
            System.out.println("Can't start car: Out of gas.");
        }
    }

    @Override
    public void stop() {
        System.out.println("Stopping the car");
    }

    @Override
    public int getGas() {
        return gas;
    }

    @Override
    public void addGas(int gas) {
        if (gas + this.gas > 100) {
            this.gas = 100;
        } else {
            this.gas += gas;
        }
    }

    @Override
    public void useGas(int gas) {
        this.gas -= gas;
    }

    @Override
    public boolean hasGas() {
        return gas > 0;
    }

}
