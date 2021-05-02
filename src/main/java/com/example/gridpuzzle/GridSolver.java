package com.example.gridpuzzle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Given an n*n grid, this places n points with follow constraints:
 *    <li>only 1 point may appear in a horizontal, vertical, and diagonal lines.
 *    <li>at most 2 points may appear in any other straight lines
 */
public class GridSolver {

    /*
     * General idea is to treat the constraints as follows:
     *    - only 1 point may appear for a line with slope 1 and -1 (diagonal lines)
     *    - only 1 point may appear for a line with slope 0 (horizontal line)
     *    - only 1 point may appear for a vertical line
     *    - at most 2 points may appear for lines with any other slope.
     *  First three are "standard" lines and last one is a "special" line.
     *    
     *  We start inspecting each row, and each column, recursively inspecting each point by checking if
     *  it is valid. That is the point does not violate constraints given lines that we already keep track.
     *  
     *  To test if a candidate point is viable or valid, then we create a new or lookup existing standard lines.
     *  We also create new or lookup existing special lines by forming pairs with all existing points.
     *  
     *  If those candidate lines do not violate the constraints, then start tracking those lines
     *  and/or update the point count on the lines.
     *  
     *  Once we've placed a valid point in each row, then by construction, we are done.
     */

    private static final int MAX_POINTS_PER_STD_LINE = 1;
    private static final int MAX_POINTS_PER_SPECIAL_LINE = 2;

    // set to true to general random solutions each run
    private boolean preferRandomPlacement = Boolean.getBoolean("puzzle.preferRandomPlacement");
    private final Random random = new Random();

    private int gridSize;

    // column value at row. array[row] = column
    private int[] row2Column;

    public GridSolver(int gridSize) {
        this.gridSize = gridSize;
    }

    public void setPreferRandomPlacement(boolean prefer) {
        this.preferRandomPlacement = prefer;
    }

    public GridSolution solve() {
        this.row2Column = new int[gridSize];
        return placePoints(0, new LineMap())
                .orElseThrow(() -> new IllegalStateException("Cannot find a valid solution"));
    }

    private Optional<GridSolution> placePoints(int row, LineMap lineMap) {
        if (row == row2Column.length) {
            return Optional.of(new GridSolution(row2Column, lineMap));
        }

        int columnStart = getColumnStart(row);
        IntStream columnIndexStream = IntStream.iterate(columnStart, value -> value + 1)
                .limit(row2Column.length)
                .map(columnIndex -> columnIndex % row2Column.length);

        return columnIndexStream.mapToObj(column -> placePointAtCell(column, row, lineMap))
                .filter(Optional::isPresent)
                .findFirst()
                .orElse(Optional.empty());
    }

    private Optional<GridSolution> placePointAtCell(int column, int row, LineMap lineMap) {
        LineMap linesCopy = lineMap.createShallowCopy();
        boolean isValid = validate(column, row, linesCopy);

        if (isValid) {
            // for this row, this column/point is valid.
            row2Column[row] = column;

            // ... now try to find solutions starting at next the row
            Optional<GridSolution> subSolution = placePoints(row + 1, linesCopy);
            if (subSolution.isPresent()) {
                return subSolution;
            }
        }

        return Optional.empty();
    }

    private int getColumnStart(int row) {
        if (preferRandomPlacement) {
            return random.nextInt(row2Column.length);
        }

        // start inspecting at row+2 because row and row+1 values will definitely cause conflicts.
        return (row + 2) % row2Column.length;
    }

    private static boolean validate(int column, int row, LineMap lineMap) {
        Line ascLine = createLine(column, row, 1, lineMap);
        Line descLine = createLine(column, row, -1, lineMap);
        Line flatLine = createLine(column, row, 0, lineMap);
        Line verticalLine = createVerticalLine(column, lineMap);

        boolean canAddPointToLines = isValidStdLine(ascLine)
                && isValidStdLine(descLine)
                && isValidStdLine(flatLine)
                && isValidStdLine(verticalLine)
                && addSpecialLines(column, row, lineMap);

        if (canAddPointToLines) {
            ImmutablePoint point = new ImmutablePoint(column, row);

            ascLine.addPoint(point);
            descLine.addPoint(point);
            flatLine.addPoint(point);
            verticalLine.addPoint(point);

            lineMap.track(ascLine);
            lineMap.track(descLine);
            lineMap.track(flatLine);
            lineMap.track(verticalLine);

            // special lines already tracked by now
        }

        return canAddPointToLines;
    }

    private static boolean isValidStdLine(Line stdLine) {
        return stdLine.getNumPoints() < MAX_POINTS_PER_STD_LINE;
    }

    private static boolean addSpecialLines(int column, int row, LineMap existingLineMap) {
        Set<ImmutablePoint> existingPoints = existingLineMap.getAllPoints();

        Map<Line, ImmutablePoint> otherPoints = new HashMap<>();
        for (ImmutablePoint existingPoint : existingPoints) {
            int divisor = column - existingPoint.x;

            // slope = (y2 - y1) / (x2 - x1)
            // divisor should not be null because vertical lines are already treated by the caller
            double slope = (row - existingPoint.y) / ((double) divisor);

            Line candidateLine = createLine(column, row, slope, existingLineMap);
            if (candidateLine.getNumPoints() >= MAX_POINTS_PER_SPECIAL_LINE) {
                // already at max. can't place anymore points.
                return false;
            }

            // keep a ref to existingPoint, with which this (column, row) point has just formed a line.
            otherPoints.put(candidateLine, existingPoint);
        }

        // now that we know this point does not conflict with any other line, start tracking
        ImmutablePoint thisPoint = new ImmutablePoint(column, row);
        otherPoints.entrySet()
                .forEach(entry -> {
                    Line candidateLine = entry.getKey();
                    ImmutablePoint otherPoint = entry.getValue();

                    candidateLine.addPoint(otherPoint);
                    candidateLine.addPoint(thisPoint);
                    existingLineMap.track(candidateLine);
                });

        return true;
    }

    private static Line createLine(double column, double row, double slope, LineMap lineMap) {
        double intercept = findIntercept(column, row, slope);
        Line line = new Line(slope, intercept);
        return lineMap.findTrackedOrDefault(line);
    }

    public static Line createVerticalLine(int column, LineMap lineMap) {
        Line line = Line.newVerticalLine(column);
        return lineMap.findTrackedOrDefault(line);
    }

    // y = m*x + b
    // row = m*column + b
    // b = row - (m * column)
    private static double findIntercept(double column, double row, double slope) {
        return row - (slope * column);
    }

}