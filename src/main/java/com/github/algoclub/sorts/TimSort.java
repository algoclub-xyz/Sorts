package com.github.algoclub.sorts;

import java.util.Comparator;
import java.util.Stack;

public class TimSort<T> {

    /**
     * Information about current run
     */
    private record Run(int index, int length) {}

    private static final int MIN_RUN_LENGTH = 64;
    private static final int GALLOP_THRESHOLD = 7;

    private final T[] array;
    private final Comparator<T> comparator;

    private final Stack<Run> stack = new Stack<>();

    TimSort(T[] array, Comparator<T> comparator) {
        this.array = array;
        this.comparator = comparator;
    }

    /**
     * Sorts the array
     * with the default comparator
     * (natural order comparator)
     */
    static <T> void sort(T[] array) {
        sort(array, null);
    }

    static <T> void sort(T[] array, Comparator<T> comparator) {
        if (comparator == null) {
            comparator = (a, b) -> {
                try {
                    return ((Comparable<T>) a).compareTo(b);
                } catch (ClassCastException ignored) {
                    throw new IllegalArgumentException("Comparator should be provided or items should implement Comparable");
                }
            };
        }

        TimSort<T> algo = new TimSort<>(array, comparator);
        algo.sort();
    }

    /**
     * Trying to merge
     * entities in the stack
     */
    private void collapse() {
        while (stack.size() > 1) {
            Run x = stack.pop();
            Run y = stack.pop();

            if (y.length <= x.length) {
                merge(x, y);
                continue;
            }

            if (stack.isEmpty()) {
                stack.push(y);
                stack.push(x);
                break;
            }

            Run z = stack.pop();

            if (z.length <= x.length + y.length) {
                if (x.length < z.length) {
                    stack.add(z);
                    merge(x, y);
                } else {
                    merge(z, y);
                    stack.add(x);
                }
                continue;
            }

            stack.push(z);
            stack.push(y);
            stack.push(x);
            break;
        }
    }

    /**
     * Leaves only one
     * entity in the stack
     * and trying to merge everything
     * together
     */
    private void forceCollapse() {
        while (stack.size() > 1) {
            Run x = stack.pop();
            Run y = stack.pop();

            if (stack.isEmpty()) {
                merge(x, y);
                continue;
            }

            Run z = stack.pop();

            if (Math.abs(z.length - y.length) < Math.abs(y.length - x.length)) {
                merge(z, y);
                stack.add(x);
            } else {
                stack.add(z);
                merge(x, y);
            }
        }
    }

    /**
     * Merged two following runs
     * a should follow b or vice versa
     */
    private void merge(Run a, Run b) {
        Run minEntry;
        Run maxEntry;

        if (a.index < b.index) {
            minEntry = a;
            maxEntry = b;
        } else {
            minEntry = b;
            maxEntry = a;
        }

        T[] copy = (T[]) new Object[minEntry.length];

        System.arraycopy(array, minEntry.index, copy, 0, copy.length);

        int result = minEntry.index;

        int left = 0;
        int right = 0;

        int lcount = 0;
        int rcount = 0;

        while (left < copy.length || right < maxEntry.length) {
            T lval = left < copy.length ? copy[left] : null;
            T rval = right < maxEntry.length ? array[maxEntry.index + right] : null;

            if (lval == null || (rval != null && comparator.compare(rval, lval) < 0)) {
                array[result] = rval;
                right++;

                lcount = 0;
                rcount++;
            } else {
                array[result] = lval;
                left++;

                rcount = 0;
                lcount++;
            }

            result++;


            if (rval != null && lcount >= GALLOP_THRESHOLD) {
                lcount = 0;

                int nextIndex = gallop(copy, left, copy.length, rval);

                for (int k = left; k < nextIndex; k++) {
                    array[result] = copy[k];

                    left++;
                    result++;
                }
            }

            if (lval != null && rcount >= GALLOP_THRESHOLD) {
                rcount = 0;

                int nextIndex = gallop(array, maxEntry.index + right, maxEntry.index + maxEntry.length, lval);

                for (int k = maxEntry.index + right; k < nextIndex; k++) {
                    array[result] = array[k];

                    right++;
                    result++;
                }
            }
        }

        Run newEntry = new Run(minEntry.index, a.length + b.length);
        stack.push(newEntry);
    }

    private void sort() {
        int minRun = getMinRun();

        int index = 0;

        while (index < array.length) {
            int runLength = calculateRunAndReverseIfNeeded(index, array.length);

            if (runLength < minRun) {
                int leftElements = array.length - (index + runLength);
                runLength += Math.min(leftElements, minRun - runLength);
            }

            binaryInsertionSort(index, index + runLength);
            stack.add(new Run(index, runLength));
            collapse();

            index += runLength;
        }

        forceCollapse();
    }

    /**
     * Trying to find the last index of the element
     * in the given array
     * that would be smaller than the given target
     */
    private int gallop(T[] a, int low, int high, T target) {
        int left = low;
        int right = high;

        int lastKnownIndex = -1;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            if (comparator.compare(a[middle], target) < 0) {
                lastKnownIndex = Math.max(lastKnownIndex, middle + 1);
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return lastKnownIndex;
    }

    private int calculateRunAndReverseIfNeeded(int low, int high) {
        if (low + 1 == high) {
            return 1;
        }

        int index = low + 1;

        // descending order
        if (comparator.compare(array[index], array[index - 1]) < 0) {
            while (index < high && comparator.compare(array[index], array[index - 1]) < 0) {
                index++;
            }

            reverse(low, index);
        } else {
            // ascending order
            while (index < high && comparator.compare(array[index], array[index - 1]) >= 0) {
                index++;
            }
        }

        return (index - low);
    }

    /**
     * Reverses array
     */
    private void reverse(int low, int high) {
        while (low < high) {
            T temp = array[low];
            array[low] = array[high];
            array[high] = temp;

            low++;
            high--;
        }
    }

    /**
     * Calculates min run position
     */
    private int getMinRun() {
        int n = array.length;

        if (n < MIN_RUN_LENGTH) {
            return n;
        }

        int offset = 0;

        while (n > 64) {
            offset |= (n & 1);
            n = n >> 1;
        }

        return n + offset;
    }

    /**
     * O(n lg n) comparisons
     * O(n^2) runtime complexity
     */
    private void binaryInsertionSort(int low, int high) {
        for (int i = low + 1; i < high; i++) {
            T value = array[i];

            int left = low;
            int right = i - 1;

            int insertionIndex = i;

            while (left <= right) {
                int middle = left + (right - left) / 2;

                // value is smaller than array's element
                if (comparator.compare(value, array[middle]) < 0) {
                    // we will take
                    // the place of this element
                    insertionIndex = middle;
                    right = middle - 1;
                } else {
                    // value is bigger or equal to the array's element
                    left = middle + 1;
                }
            }

            for (int j = i; j > insertionIndex; j--) {
                array[j] = array[j - 1];
            }

            array[insertionIndex] = value;
        }
    }

}
