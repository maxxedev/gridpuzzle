package com.example.gridpuzzle;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class LineTest {

    @Test
    public void testGetters() throws Exception {
        Line line = new Line(1, 5);
        assertThat(line.getSlope()).isEqualTo(1);
        assertThat(line.getIntercept()).isEqualTo(5);
        assertThat(line.getNumPoints()).isEqualTo(0);
        assertThat(line.getPoints()).isEmpty();
        assertThat(line.isVerticalLine()).isFalse();

        line.addPoint(new ImmutablePoint(0, 5));
        assertThat(line.getNumPoints()).isEqualTo(1);
        assertThat(line.getPoints()).containsOnly(new ImmutablePoint(0, 5));
    }

    @Test
    public void testVerticalLine() throws Exception {
        Line line = Line.newVerticalLine(3);
        assertThat(line.getIntercept()).isEqualTo(3);
        assertThat(line.isVerticalLine()).isTrue();
    }

    @Test
    public void testEqualsHashCode() {
        EqualsVerifier.simple()
                .forClass(Line.class)
                .withIgnoredFields("points")
                .verify();
    }

    @Test
    public void testToString() {
        Line line = new Line(1, 5);
        line.addPoint(new ImmutablePoint(0, 5));

        assertThatNoException().isThrownBy(line::toString);
        assertThat(line.toString())
                .contains("slope=1", "intercept=5", "(0, 5)");
    }
}
