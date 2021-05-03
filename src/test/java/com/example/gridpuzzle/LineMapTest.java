package com.example.gridpuzzle;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineMapTest {

    @Test
    public void testTrack() throws Exception {
        LineMap lineMap = new LineMap();
        Line line = new Line(1, 0);

        lineMap.track(line);
        assertThat(lineMap.findTracked(line)).isSameAs(line);
        assertThat(lineMap.findTrackedOrDefault(line)).isSameAs(line);
    }

    @Test
    public void testCopy() {
        LineMap lineMap = new LineMap();
        Line line = new Line(1, 0);

        lineMap.track(line);

        LineMap copy = lineMap.createShallowCopy();
        assertThat(copy.getLines()).isEqualTo(lineMap.getLines());
    }

    @Test
    public void testGetAllPoints() {
        LineMap lineMap = new LineMap();
        Line line1 = new Line(1, 0);
        ImmutablePoint point1 = new ImmutablePoint(0, 0);
        line1.addPoint(point1);

        Line line2 = new Line(1, 1);
        ImmutablePoint point2 = new ImmutablePoint(0, 1);
        line2.addPoint(point2);

        lineMap.track(line1);
        lineMap.track(line2);
        assertThat(lineMap.getAllPoints())
                .containsExactly(point1, point2);
    }
}
