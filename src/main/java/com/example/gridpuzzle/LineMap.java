package com.example.gridpuzzle;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class LineMap {

    private final Map<Line, Line> lines = new HashMap<>();

    public void track(Line line) {
        lines.put(line, line);
    }

    public Line findTracked(Line line) {
        return lines.get(line);
    }

    public Line findTrackedOrDefault(Line line) {
        return lines.getOrDefault(line, line);
    }

    public LineMap createShallowCopy() {
        LineMap lineMap = new LineMap();
        lineMap.lines.putAll(this.lines);
        return lineMap;
    }

    public Set<Line> getLines() {
        return Collections.unmodifiableSet(lines.keySet());
    }

    public Set<ImmutablePoint> getAllPoints() {
        return lines.keySet()
                .stream()
                .map(Line::getPoints)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
