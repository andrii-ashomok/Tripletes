package com.codility.triplet;

import org.testng.annotations.Test;

@Test
public class SolutionTest {

    private Solution solution = new Solution();
    private final static int[] A = new int[]{0, 1, 3, -2, 0, 1, 0, -3, 2, 3};

    @Test
    public void testBruteForce() {
        int r = solution.solutionBruteForce(A);
        assert r == 4;
    }

    @Test
    public void testSolution() {
        for (int value : A) System.out.print(value + " ");

        System.out.println();
        int r = solution.solution(A);
        assert r == 4;
    }

}
