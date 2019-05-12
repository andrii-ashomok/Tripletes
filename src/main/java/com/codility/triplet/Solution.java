package com.codility.triplet;

import java.util.*;

public class Solution {

    public int solutionBruteForce(int[] A) {
        Set<Integer> result = new HashSet();
        for (int x = 0; x < A.length; x++) {
            if (x == A.length-1)
                break;

            if (A[x] < A[x+1])
                continue;

            for (int y = x + 1; y < A.length; y++) {
                if (y == A.length-1)
                    break;

                if (A[x] > A[y]) {
                    if (A[y] > A[y+1])
                        continue;

                    int p = A[x];
                    int q = A[y];

                    for (int z = y + 1; z < A.length; z++) {

                        if (A[z] > q) {
                            int r = A[z];

                            int d1 = p - q;
                            int d2 = r - q;
                            int res;
                            if (d1 < d2)
                                res = d1;
                            else
                                res = d2;

                            result.add(res);

                            System.out.printf("(%s, %s, %s) A[%s, %s]: {%s, %s} and A[%s, %s]: {%s, %s}. Result: %s",
                                    x, y, z, x, x + 1, p, q, y, z, q, r, res);
                            System.out.println();
                        }

                        if (z < A.length - 1 && A[z] > A[z + 1])
                            break;
                    }
                }

                if (A[y] < A[y+1])
                    break;
            }

        }

        if (result.isEmpty())
            return -1;

        return result.stream().max(Integer::compareTo).get();
    }

    public int solution(int[] A) {
        List<Element> decreaseSequences = new ArrayList<>(A.length);
        Map<Integer, Integer> increaseSequences = new HashMap<>(A.length);
        Set<Integer> result = new HashSet<>();
        boolean flag = false;
        boolean isDecrease = false;
        boolean isIncrease = false;
        for (int x = 0; x < A.length; x++) {
            if (x == A.length-1) {
                if (isIncrease)
                    increaseSequences.put(x, A[x]);
                if (isDecrease)
                    decreaseSequences.add(new Element(x, A[x]));

                break;
            }

            if (A[x] > A[x+1]) {
                flag = true;
                decreaseSequences.add(new Element(x, A[x]));

                if (isIncrease)
                    increaseSequences.put(x, A[x]);

                isDecrease = true;
                isIncrease = false;
            } else if (flag) {
                increaseSequences.put(x, A[x]);

                if (isDecrease)
                    decreaseSequences.add(new Element(x, A[x]));

                isDecrease = false;
                isIncrease = true;
            }

        }

        for (int i = 0; i < decreaseSequences.size(); i++) {
            calcQDepth(result, increaseSequences, decreaseSequences, i);
        }

        if (result.isEmpty())
            return -1;

        return result.stream().max(Integer::compareTo).orElse(-1);
    }

    private void calcQDepth(Set<Integer> result, Map<Integer, Integer> increaseSequences, List<Element> decreaseSequences, int currentPosition) {
        Element P = decreaseSequences.get(currentPosition);

        ++currentPosition;
        if (currentPosition >= decreaseSequences.size())
            return;

        Element Q = decreaseSequences.get(currentPosition);

        if (P.getValue() < Q.getValue())
            return;

        int ind1 = Q.getIndex();
        processR(result, increaseSequences, P, Q, ind1);

        while (true) {
            ++currentPosition;
            if (currentPosition >= decreaseSequences.size())
                return;

            Element Q1 = decreaseSequences.get(currentPosition);
            if (Objects.nonNull(Q1) && Q1.getIndex() - Q.getIndex() == 1) {
                Q = decreaseSequences.get(currentPosition);
                int index = Q.getIndex();
                processR(result, increaseSequences, P, Q, ++index);
            } else
                break;
        }

    }

    private void processR(Set<Integer> result, Map<Integer, Integer> increaseSequences, Element P, Element Q, int index) {
        if (increaseSequences.containsKey(Q.getIndex() + 1)) {
            while (true) {
                Optional<Integer> optionalInteger = calcRDepth(increaseSequences, P, Q, ++index);
                if (optionalInteger.isPresent()) {
                    result.add(optionalInteger.get());
                } else
                    break;

            }
        }
    }

    private Optional<Integer> calcRDepth(Map<Integer, Integer> increaseSequences, Element P, Element Q, int index) {
        if (increaseSequences.containsKey(index)) {
            int R = increaseSequences.get(index);

            if (Q.getValue() > R)
                return Optional.empty();

            int d1 = P.getValue() - Q.getValue();
            int d2 = R - Q.getValue();
            int res;
            if (d1 < d2)
                res = d1;
            else
                res = d2;

            System.out.printf("(%s, %s, %s) A[%s, %s]: {%s, %s} and A[%s, %s]: {%s, %s}. Result: %s",
                    P.getIndex(), Q.getIndex(), index, P.getIndex(), Q.getIndex(),
                    P.getValue(), Q.getValue(), Q.getIndex(), index, Q.getValue(), R, res);
            System.out.println();

            return Optional.of(res);
        }

        return Optional.empty();
    }

    private class Element {
        private int index;
        private int value;

        public Element(int index, int value) {
            this.index = index;
            this.value = value;
        }

        public int getIndex() {
            return index;
        }

        public int getValue() {
            return value;
        }
    }

}
