package com.github.algoclub.pattern_matching;

public class SimpleMatcher implements PatternMatcher {

    @Override
    public int find(String origin, String pattern) {
        int p = 0;
        int count = 0;

        while (p <= origin.length() - pattern.length()) {
            boolean patternFound = true;

            for (int i = 0; i < pattern.length(); i++) {
                if (origin.charAt(p + i) != pattern.charAt(i)) {
                    patternFound = false;
                    break;
                }
            }

            if (patternFound) {
                count++;
            }

            p++;
        }

        return count;
    }
}
