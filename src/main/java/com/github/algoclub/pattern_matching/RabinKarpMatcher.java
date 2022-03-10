package com.github.algoclub.pattern_matching;

public final class RabinKarpMatcher implements PatternMatcher {

    // module, should be big enough
    private final static int Q = (int) Math.pow(2, 25);
    // alphabet size
    private final static int D = 256;

    public int find(String origin, String pattern) {
        if (pattern.length() == 0) {
            return 0;
        }

        int counter = 0;

        long patternHashCode = 0;
        int h = 1;

        for (int i = 0; i < pattern.length(); i++) {
            patternHashCode = (patternHashCode * D + pattern.charAt(i)) % Q;
            if (i > 0) h = (h * D) % Q;
        }

        long originHashCode = 0;

        for (int i = 0; i < Math.min(origin.length(), pattern.length()); i++) {
            originHashCode = (originHashCode * D + origin.charAt(i)) % Q;
        }


        if ((originHashCode == patternHashCode) && equals(origin, 0, pattern)) {
            counter++;
        }

        for (int i = pattern.length(); i < origin.length(); i++) {
            originHashCode = (((originHashCode - origin.charAt(i - pattern.length()) * h) * D + origin.charAt(i)) % Q + Q) % Q;

            if (originHashCode == patternHashCode && equals(origin, i - pattern.length() + 1, pattern)) {
                counter++;
            }
        }

        return counter;
    }

    private boolean equals(String origin, int offset, String pattern) {
        if (origin.length() - offset < pattern.length()) {
            return false;
        }

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) != origin.charAt(i + offset)) {
                return false;
            }
        }

        return true;
    }

}
