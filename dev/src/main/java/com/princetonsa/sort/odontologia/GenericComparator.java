package com.princetonsa.sort.odontologia;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.axioma.util.log.Log4JManager;

public class GenericComparator<T> implements Comparator<T> {
    public static final String DEFAULT_METHOD_NAME = "getSequenceNo";   //default method name.
    private String methodName;

    public GenericComparator() {
        this(DEFAULT_METHOD_NAME);
    }

    public GenericComparator(String methodName) {
        this.methodName = methodName;
    }

    /*
     * return value should be Comparable.
     */
    public int compare(T o1, T o2) {
        int result = -2; //error code
        Comparable s1 = (Comparable)getValueOfMethodAsObject(o1, methodName);
        Comparable s2 = (Comparable)getValueOfMethodAsObject(o2, methodName);
        if (s1 != null && s2 != null) {
            result = s1.compareTo(s2);
        } else if (s1 != null) {
            result = 1;
        } else if (s2 != null) {
            result = -1;
        } else {
            result = 0;
        }
        return result;
    }

    /*
     * getter and setter.
     */
    

    public String getMethodName() {
        if (methodName == null) {
            return DEFAULT_METHOD_NAME;
        }
        return methodName;
    }

    /*
     * Utility method.
     */
    public static final <E> Object getValueOfMethodAsObject(final E obj, final String methodName) {
        Object result = null;
        try {
            final Class objClazz = obj.getClass();
            Method getDiscriminatorMethod = null;
            
                getDiscriminatorMethod = objClazz.getMethod(methodName, new Class[] { });
               
                 result = getDiscriminatorMethod.invoke(obj, new Object[] { });
                
            
        } catch (Exception e) {
            Log4JManager.error("GenericComparator - getValueOfMethodAsObject controlled exception");
        }
        return result;
    }
}