package com.example.gridpuzzle;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class GridSolverTest {

    @Test
    public void testSolve() throws Exception {
        GridSolver gridSolver = new GridSolver(8);
        GridSolution solution = gridSolver.solve();
        assertThat(solution.getArray())
                .containsExactly(2, 4, 7, 3, 0, 6, 1, 5);

        assertThat(solution.getPoints())
                .hasToString("[(0, 4), (1, 6), (2, 0), (3, 3), (4, 1), (5, 7), (6, 5), (7, 2)]");
    }

    @Test
    public void testSolveNonRandom() {
        GridSolver gridSolver = new GridSolver(8);

        Set<String> uniqueSolutions = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            GridSolution solution = gridSolver.solve();
            uniqueSolutions.add(solution.getPoints().toString());
            if (uniqueSolutions.size() > 1) {
                break;
            }
        }

        assertThat(uniqueSolutions)
                .hasSize(1);
    }

    @Test
    public void testSolveRandom() {

        GridSolver gridSolver = new GridSolver(8);
        gridSolver.setPreferRandomPlacement(true);

        Set<String> uniqueSolutions = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            GridSolution solution = gridSolver.solve();
            uniqueSolutions.add(solution.getPoints().toString());
            if (uniqueSolutions.size() > 1) {
                break;
            }
        }

        assertThat(uniqueSolutions)
                .hasSizeGreaterThan(1);
    }
}
