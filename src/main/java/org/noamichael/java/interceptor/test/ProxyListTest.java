package org.noamichael.java.interceptor.test;

import java.util.ArrayList;
import java.util.Collection;
import org.noamichael.java.interceptor.api.EndPoint;

/**
 *
 * @author Michael
 * @param <T>
 */
@EndPoint
public class ProxyListTest<T> extends ArrayList<T> {

    public ProxyListTest(int initialCapacity) {
        super(initialCapacity);
    }

    public ProxyListTest() {
    }

    public ProxyListTest(Collection c) {
        super(c);
    }
    
}
