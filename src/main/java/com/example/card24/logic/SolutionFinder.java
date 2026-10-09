package com.example.card24.logic;

import com.example.card24.model.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Finds a valid Card 24 expression via exhaustive search.
 */
public final class SolutionFinder {

    private static final double TARGET = 24.0;
    private static final double EPSILON = 1e-6;

    /**
     * Arithmetic operations available for solution generation.
     */
    private enum Operator {
        ADD("+"),
        SUBTRACT("-"),
        MULTIPLY("*"),
        DIVIDE("/");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }
    }

    /**
     * Attempts to find a valid expression for the given four cards.
     *
     * @param cards list of four cards
     * @return first found expression producing 24, or empty if none exists
     */
    public Optional<String> findSolution(List<Card> cards) {
        if (cards == null || cards.size() != 4) {
            return Optional.empty();
        }

        int[] values = cards.stream().mapToInt(Card::getValue).toArray();
        List<int[]> permutations = generatePermutations(values);

        for (int[] perm : permutations) {
            for (Operator op1 : Operator.values()) {
                for (Operator op2 : Operator.values()) {
                    for (Operator op3 : Operator.values()) {
                        Optional<String> solved = testAllParenthesizations(perm, op1, op2, op3);
                        if (solved.isPresent()) {
                            return solved;
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

    /**
     * Tests the 5 binary-tree parenthesization patterns for 4 operands.
     *
     * @param v permuted card values
     * @param a first operator
     * @param b second operator
     * @param c third operator
     * @return solution expression if one pattern yields 24
     */
    private Optional<String> testAllParenthesizations(int[] v, Operator a, Operator b, Operator c) {
        // 1) ((x a y) b z) c w
        var r1 = apply(v[0], v[1], a);
        if (r1.isPresent()) {
            var r2 = apply(r1.get(), v[2], b);
            if (r2.isPresent()) {
                var r3 = apply(r2.get(), v[3], c);
                if (is24(r3)) {
                    return Optional.of("((" + v[0] + a.symbol + v[1] + ")" + b.symbol + v[2] + ")" + c.symbol + v[3]);
                }
            }
        }

        // 2) (x a (y b z)) c w
        var p1 = apply(v[1], v[2], b);
        if (p1.isPresent()) {
            var p2 = apply(v[0], p1.get(), a);
            if (p2.isPresent()) {
                var p3 = apply(p2.get(), v[3], c);
                if (is24(p3)) {
                    return Optional.of("(" + v[0] + a.symbol + "(" + v[1] + b.symbol + v[2] + "))" + c.symbol + v[3]);
                }
            }
        }

        // 3) x a ((y b z) c w)
        var q1 = apply(v[1], v[2], b);
        if (q1.isPresent()) {
            var q2 = apply(q1.get(), v[3], c);
            if (q2.isPresent()) {
                var q3 = apply(v[0], q2.get(), a);
                if (is24(q3)) {
                    return Optional.of(v[0] + a.symbol + "((" + v[1] + b.symbol + v[2] + ")" + c.symbol + v[3] + ")");
                }
            }
        }

        // 4) x a (y b (z c w))
        var s1 = apply(v[2], v[3], c);
        if (s1.isPresent()) {
            var s2 = apply(v[1], s1.get(), b);
            if (s2.isPresent()) {
                var s3 = apply(v[0], s2.get(), a);
                if (is24(s3)) {
                    return Optional.of(v[0] + a.symbol + "(" + v[1] + b.symbol + "(" + v[2] + c.symbol + v[3] + "))");
                }
            }
        }

        // 5) (x a y) b (z c w)
        var t1 = apply(v[0], v[1], a);
        var t2 = apply(v[2], v[3], c);
        if (t1.isPresent() && t2.isPresent()) {
            var t3 = apply(t1.get(), t2.get(), b);
            if (is24(t3)) {
                return Optional.of("(" + v[0] + a.symbol + v[1] + ")" + b.symbol + "(" + v[2] + c.symbol + v[3] + ")");
            }
        }

        return Optional.empty();
    }

    /**
     * Applies a binary operator while safely rejecting division by zero.
     *
     * @param left left operand
     * @param right right operand
     * @param op arithmetic operator
     * @return result if valid, otherwise empty when dividing by zero
     */
    private Optional<Double> apply(double left, double right, Operator op) {
        return switch (op) {
            case ADD -> Optional.of(left + right);
            case SUBTRACT -> Optional.of(left - right);
            case MULTIPLY -> Optional.of(left * right);
            case DIVIDE -> {
                if (Math.abs(right) < EPSILON) {
                    yield Optional.empty();
                }
                yield Optional.of(left / right);
            }
        };
    }

    /**
     * Checks whether an optional numeric result is effectively equal to 24.
     *
     * @param value optional computed value
     * @return true when value exists and is within epsilon of 24
     */
    private boolean is24(Optional<Double> value) {
        return value.isPresent() && Math.abs(value.get() - TARGET) <= EPSILON;
    }

    /**
     * Generates all operand orderings, including duplicates when values repeat.
     *
     * @param values four card values
     * @return list of permutations to test
     */
    private List<int[]> generatePermutations(int[] values) {
        List<int[]> output = new ArrayList<>();
        permute(values, 0, output);
        return output;
    }

    /**
     * Recursively enumerates permutations by in-place swapping.
     *
     * @param arr current values array
     * @param index fixed prefix boundary
     * @param output permutation collector
     */
    private void permute(int[] arr, int index, List<int[]> output) {
        if (index == arr.length) {
            output.add(Arrays.copyOf(arr, arr.length));
            return;
        }

        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            permute(arr, index + 1, output);
            swap(arr, index, i);
        }
    }

    /**
     * Swaps two positions in an integer array.
     *
     * @param arr target array
     * @param i first index
     * @param j second index
     */
    private void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
