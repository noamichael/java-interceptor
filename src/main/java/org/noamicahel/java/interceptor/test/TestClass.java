package org.noamicahel.java.interceptor.test;

import java.io.Serializable;

/**
 *
 * @author Michael
 */
public class TestClass implements TestInterface, Serializable {

    @TestAnnotation
    @Override
    public void test() {
        System.out.println("Inside method");
    }
    
    public void otherMethod(){
        System.out.println("Other method");
    }
}
