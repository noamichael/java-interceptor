package org.noamichael.java.interceptor.test;

import java.io.Serializable;
import org.noamichael.java.interceptor.api.EndPoint;

/**
 *
 * @author Michael
 */
@EndPoint
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
