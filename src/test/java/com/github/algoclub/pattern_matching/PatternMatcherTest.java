package com.github.algoclub.pattern_matching;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class PatternMatcherTest {

    public static Stream<Arguments> values() {
        return Stream.of(
                arguments(
                        "mississippi",
                        "issi",
                        2
                ),
                arguments(
                        "AGCCATGAT",
                        "GCC",
                        1
                ),
                arguments(
                        "helloworld",
                        "l",
                        3
                ),
                arguments(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        "er",
                        3
                ),
                arguments(
                        "aaaaaaaaaaa",
                        "aa",
                        10
                )
        );
    }

    @Nested
    @DisplayName("Simple matcher tests")
    class SimpleMatcherTest {
        private final SimpleMatcher simpleMatcher = new SimpleMatcher();

        @ParameterizedTest
        @MethodSource("com.github.algoclub.pattern_matching.PatternMatcherTest#values")
        public void testThatSortingWorksFine(String origin, String pattern, int expectedCount) {
            assertEquals(simpleMatcher.find(origin, pattern), expectedCount);
        }
    }

    @Nested
    @DisplayName("Rabin-Karp matcher tests")
    class RabinCarpMatcherTest {
        private final RabinKarpMatcher rabinKarpMatcher = new RabinKarpMatcher();

        @ParameterizedTest
        @MethodSource("com.github.algoclub.pattern_matching.PatternMatcherTest#values")
        public void testThatSortingWorksFine(String origin, String pattern, int expectedCount) {
            assertEquals(rabinKarpMatcher.find(origin, pattern), expectedCount);
        }
    }

}
