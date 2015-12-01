package com.princetonsa.sort.odontologia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortUtility {
    /*
     * Sorted the collection of object including getSequenceNo method.
     */
    public static final <T> List<T> orderList(final Collection<T> aCollection) {
        return new ArrayList(orderSet(aCollection));
    }

    public static final <T> Set<T> orderSet(final Collection<T> aCollection) {
        return orderSetBy(aCollection, GenericComparator.DEFAULT_METHOD_NAME);
    }

    /*
     * Sorted the collection of object including given method name.
     */
    public static final <T> List<T> orderListBy(final Collection<T> aCollection, final String methodName) {
    	
    	return new ArrayList(orderSetBy(aCollection, methodName));
    }

    public static final <T> Set<T> orderSetBy(final Collection<T> aCollection, final String methodName) {
        SortedSet<T> sortedSet = new TreeSet<T>(new GenericComparator<T>(methodName));
        sortedSet.addAll(aCollection);
        return sortedSet;
    }
}