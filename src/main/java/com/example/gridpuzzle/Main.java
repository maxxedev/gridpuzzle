package com.example.gridpuzzle;

import org.apache.commons.lang3.time.StopWatch;

import java.io.PrintStream;
import java.util.Comparator;

public class Main {

    private PrintStream printStream = System.out;

    public static void main(String... args) {
        new Main().run();
    }

    public void setPrintStream(PrintStream ps) {
        this.printStream = ps;
    }

    public void run() {
        int gridSize = Integer.getInteger("puzzle.gridSize", 8);

        printStream.printf("Solving for size=%s ...%n", gridSize);

        StopWatch stopwatch = StopWatch.createStarted();
        GridSolver solver = new GridSolver(gridSize);
        GridSolution solution = solver.solve();
        printStream.println("Found solution in " + stopwatch);

        showSolution(solution);
    }

    private void showSolution(GridSolution solution) {
        printStream.println("Points:");
        Comparator<ImmutablePoint> comparing = Comparator.comparingInt((ImmutablePoint point) -> point.x)
                .thenComparingInt(point -> point.y);
        solution.getPoints()
                .stream()
                .sorted(comparing)
                .forEach(printStream::println);
        printStream.println();

        printStream.println("Grid:");
        TextDrawer textPrinter = new TextDrawer(printStream);
        textPrinter.draw(solution);

        if (Boolean.getBoolean("puzzle.showUi")) {
            printStream.println("Displaying ui graph ...");
            GraphDrawer drawer = new GraphDrawer(solution);
            drawer.draw();
        }
    }
}