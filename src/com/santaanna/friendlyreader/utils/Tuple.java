package com.santaanna.friendlyreader.utils;

/**
 * This is a simple tuple class that can hold a pair of values.
 * @author Robin Keskisarkka
 */
public class Tuple <START,END> {
    public final Object T1;
    public final Object T2;
    
    /**
     * Set the START and END value of a tuple.
     * @param T1
     * @param T2 
     */
    public Tuple(Object t1, Object t2) {
        T1 = t1;
        T2 = t2;
    }
}

