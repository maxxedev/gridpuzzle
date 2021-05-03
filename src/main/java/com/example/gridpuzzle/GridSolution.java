package com.example.gridpuzzle;

import java.util.Collection;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

public class GridSolution {

    // column value at row. array[row] = column
    private final int[] row2Column;
    private final LineMap lineMap;

    GridSolution(int[] row2Column, LineMap lineMap) {
        this.row2Column = row2Column;
        this.lineMap = lineMap;
    }

    public Collection<ImmutablePoint> getPoints() {
        return lineMap.getAllPoints()
                .stream()
                .sorted(comparingDouble((ImmutablePoint point) -> point.x))
                .collect(toList());
    }

    int[] getArray() {
        return row2Column;
    }

    LineMap getLineMap() {
        return lineMap;
    }

}
