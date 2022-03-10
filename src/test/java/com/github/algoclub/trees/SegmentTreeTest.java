package com.github.algoclub.trees;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class SegmentTreeTest {

    public static Stream<Arguments> values() {
        return Stream.of(
                arguments(
                        //          0  1  2  3  4
                        new int[] { 5, 1, 7, 9, 3 },
                        new Request[] {
                                new QueryRequest(0, 4, 25),
                                new QueryRequest(0, 1, 6),
                                new QueryRequest(0, 0, 5),
                                new AddRequest(0, 1),
                                new QueryRequest(0, 4, 26),
                                new QueryRequest(1, 4, 20),
                                new AddRequest(2, 1),
                                new QueryRequest(0, 4, 27)
                        }
                ),
                arguments(
                        //          0   1   2    3  4
                        new int[] { -4, 4, 17, -17, 5 },
                        new Request[] {
                                new QueryRequest(0, 4, 5),
                                new SetRequest(4, 11),
                                new QueryRequest(4, 4, 11),
                                new QueryRequest(0, 3, 0),
                                new AddRequest(2, 1),
                                new QueryRequest(0, 3, 1)
                        }
                )
        );
    }

    private interface Request {
        void apply(SegmentTree tree);
    }

    private static class AddRequest implements Request {

        final int index;
        final int diff;

        public AddRequest(int index, int diff) {
            this.index = index;
            this.diff = diff;
        }

        @Override
        public void apply(SegmentTree tree) {
            tree.add(index, diff);
        }
    }

    private static class SetRequest implements Request {

        final int index;
        final int value;

        public SetRequest(int index, int value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public void apply(SegmentTree tree) {
            tree.set(index, value);
        }
    }

    private static class QueryRequest implements Request {

        final int left;
        final int right;
        final int expectedValue;

        public QueryRequest(int left, int right, int expectedValue) {
            this.left = left;
            this.right = right;
            this.expectedValue = expectedValue;
        }

        @Override
        public void apply(SegmentTree tree) {
            assertEquals(expectedValue, tree.get(left, right), String.format("Query left: %d right: %d", left, right));
        }
    }

    @ParameterizedTest
    @MethodSource("values")
    public void testThatSortingWorksFine(int[] array, Request[] requests) {
        SegmentTree segmentTree = new SegmentTree(array);

        for (var request: requests) {
            request.apply(segmentTree);
        }
    }

}
