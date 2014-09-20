package org.noamichael.java.interceptor.test;

/**
 *
 * @author Michael
 */
public interface Car {

    public void start();

    public void stop();

    public int getGas();

    public void addGas(int gas);

    public void useGas(int gas);

    public boolean hasGas();
}
