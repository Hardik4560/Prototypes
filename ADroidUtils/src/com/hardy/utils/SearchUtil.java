package com.hardy.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchUtil<T>{
    
    private static final String TAG = "SearchUtil<T>";

    /**
     * 
     * @param list sorted list in which the element is suppose to be inserted (internal function uses binarySearch)
     * @param searckKey
     * @param comparator
     * @return the array based index position where an element can be inserted
     */
    public int getPositionToInsertInSortedList(List<T> list, T searckKey, Comparator<T> comparator){
        int foundPosition = Collections.binarySearch(list, searckKey, comparator);
        return foundPosition >= 0 ? foundPosition : (-foundPosition) - 1;        
    }
    
    public int getPositionToUpdateInSortedList(List<T> list, T searckKey, Comparator<T> comparator){
    	return getPositionToInsertInSortedList(list, searckKey, comparator);        
    }
    
    /**
     * 
     * @param list
     * @param element
     * @param comparator
     * @return the index where the element was found in the list or -1 if not found in the list.
     */
    public int findElementPositionInSortedList(List<T> list, T element, Comparator<T> comparator){
        int foundPosition = Collections.binarySearch(list, element, comparator);
        return foundPosition >= 0 ? foundPosition : -1;        
    }
}