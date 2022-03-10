package com.github.algoclub.trees;

public final class SegmentTree {

    private final int[] array;
    private final int[] tree;

    SegmentTree(int[] array) {
        this.array = new int[array.length];
        this.tree = new int[array.length * 2];

        for (int i = 0; i < array.length; i++) {
            add(i, array[i]);
        }
    }

    int get(int from, int to) {
        checkIndex(from);
        checkIndex(to);

        int left = from + array.length;
        int right = to + array.length;

        int sum = 0;

        while (left <= right) {
            if (left % 2 == 1) {
                sum += tree[left];
                left++;
            }

            if (right % 2 == 0) {
                sum += tree[right];
                right--;
            }

            left /= 2;
            right /= 2;
        }

        return sum;
    }

    void set(int index, int newValue) {
        checkIndex(index);

        int diff = newValue - array[index];
        add(index, diff);
    }

    void add(int index, int diff) {
        checkIndex(index);

        int i = index + array.length;

        array[index] += diff;
        tree[i] += diff;

        for (int j = i / 2; j >= 1; j /= 2) {
            tree[j] = tree[2 * j] + tree[2 * j + 1];
        }
    }

    private void checkIndex(int index) {
        if (index >= array.length || index < 0) {
            throw new IllegalArgumentException(
                    String.format("The given index %d is out of array bounds %d", index, array.length));
        }
    }

}
