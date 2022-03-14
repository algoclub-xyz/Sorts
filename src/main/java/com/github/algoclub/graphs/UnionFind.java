package com.github.algoclub.graphs;

import java.util.HashMap;
import java.util.Map;

public final class UnionFind {

    private final Map<Integer, Integer> parents = new HashMap<>();
    private final Map<Integer, Integer> sizes = new HashMap<>();

    public int find(int a) {
        if (!parents.containsKey(a)) {
            parents.put(a, a);
            sizes.put(a, 1);
            return a;
        }

        if (parents.get(a) == a) {
            return a;
        }

        int parent = find(parents.get(a));
        parents.put(a, parent);
        return parent;
    }

    public boolean union(int a, int b) {
        int parentA = find(a);
        int parentB = find(b);

        if (parentA == parentB) {
            return false;
        }

        int sizeA = sizes.get(parentA);
        int sizeB = sizes.get(parentB);

        if (sizeA > sizeB) {
            int t = parentA;
            parentA = parentB;
            parentB = t;
        }

        parents.put(parentA, parentB);
        sizes.put(parentB, sizeA + sizeB);
        return true;
    }

}
