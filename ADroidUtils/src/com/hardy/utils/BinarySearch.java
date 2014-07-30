
package com.hardy.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import android.annotation.SuppressLint;

import com.hardy.utils.BinarySearch.Searchable;

/**
 * Class that performs Binary search on the supplied list of objects of type T.
 * Call the search method with the value to search in the passed List;
 * @author Hardik 
 * @param <T> The toString() method of the generic type class should be overridden and should return "term to be searched on"
 * <i>e.g name field of the class</i>
 */
public class BinarySearch<T extends Searchable> {
    List<T> mList;

    Comparator<String> mComparator = new Comparator<String>() {

        @SuppressLint("DefaultLocale")
        public int compare(String object1, String object2) {
            object1 = object1.toLowerCase();
            object2 = object2.toLowerCase();

            String temp = object1.substring(0, Math.min(object2.length(), object1.length()));
            return temp.compareTo(object2);

        }
    };

    Comparator<T> sort = new Comparator<T>() {
        public int compare(T lhs, T rhs) {
            if (lhs == null || rhs == null) {
                return 1;
            }
            return lhs.getSearchableString().compareToIgnoreCase(rhs.getSearchableString());
        };
    };

    /**
     * Create Search list of item and comparator
     * 
     * @param list
     */
    public BinarySearch(List<T> list) {
        mList = list;

        sort();
    }

    private void sort() {
        Collections.sort(mList, sort);
    }

    /**
     * Searches the string in the list and returns a sub list with given prefix
     * @param prefixString - The string to search
     * @return Sub list of the results matched.
     */
    public List<T> search(String prefixString) {

        int first = findFirstIndex(0, mList.size() - 1, prefixString);

        if (-1 == first) {
            return new Vector<T>();
        }

        int last = findLastIndex(0, mList.size() - 1, prefixString);

        Vector<T> result = new Vector<T>(mList.subList(first, last + 1));

        return result;
    }

    private int findFirstIndex(int start, int end, String prefixString) {
        int size = end - start;

        if (size < 0) {
            return -1;
        }

        if (size == 0) {
            if (mComparator.compare((mList.get(start)).getSearchableString(), prefixString) == 0) {
                return start;
            }
            else {
                return -1;
            }
        }
        else {
            int half = (start + end) / 2;
            T halfItem = mList.get(half);
            String halfItemString = (halfItem).getSearchableString();
            int result = mComparator.compare(halfItemString, prefixString);

            if (result > 0) {
                // Search in first half
                return findFirstIndex(start, half - 1, prefixString);
            }
            else if (result < 0) {
                // Search in second half
                return findFirstIndex(half + 1, end, prefixString);
            }
            else /* if (result == 0) */{
                // Search in first half
                return findFirstIndex(start, half, prefixString);
            }
        }

    }

    /**
     * Find last index
     * 
     * @param array
     * @param prefixString
     * @return
     */
    private int findLastIndex(int start, int end, String prefixString) {
        int size = end - start;
        if (size < 0) {
            return -1;
        }

        if (size == 0) {
            if (mComparator.compare(mList.get(start).getSearchableString(), prefixString) == 0) {
                return start;
            }
            else {
                return -1;
            }
        }
        else {
            int half = (start + end + 1) / 2;

            T halfItem = mList.get(half);
            String halfItemString = halfItem.getSearchableString();
            int result = mComparator.compare(halfItemString, prefixString);

            if (result > 0) {
                // Search in first half
                return findLastIndex(start, half - 1, prefixString);
            }
            else if (mComparator.compare(halfItemString, prefixString) < 0) {
                // Search in second half
                return findLastIndex(half + 1, end, prefixString);
            }
            else /* if (mComparator.compare(halfItem, prefixString) == 0) */{
                // Search in second half
                return findLastIndex(half, end, prefixString);
            }
        }
    }

    public interface Searchable {
        public String getSearchableString();
    }
}
