package com.example.gridpuzzle;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Line {

    private final double slope;
    private final double intercept;
    private final Set<ImmutablePoint> points = new HashSet<>();

    public static Line newVerticalLine(double column) {
        return new Line(Double.POSITIVE_INFINITY, column);
    }

    public Line(double slope, double intercept) {
        this.slope = slope;
        this.intercept = intercept;
    }

    public boolean isVerticalLine() {
        return Double.isInfinite(slope);
    }

    public double getSlope() {
        return slope;
    }

    public double getIntercept() {
        return intercept;
    }

    public int getNumPoints() {
        return points.size();
    }

    public Set<ImmutablePoint> getPoints() {
        return Collections.unmodifiableSet(points);
    }

    public void addPoint(ImmutablePoint point) {
        this.points.add(point);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Line)) {
            return false;
        }
        Line other = (Line) obj;
        return Double.compare(slope, other.slope) == 0
                && Double.compare(intercept, other.intercept) == 0;
    }

    @Override
    public int hashCode() {
        return (int) ((7 * slope) + (13 * intercept));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}