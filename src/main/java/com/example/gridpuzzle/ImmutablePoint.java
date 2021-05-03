package com.example.gridpuzzle;

public class ImmutablePoint {
    public final int x;
    public final int y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return (13 * x) + (7 * y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ImmutablePoint)) {
            return false;
        }
        ImmutablePoint other = (ImmutablePoint) obj;
        return Double.compare(x, other.x) == 0
                && Double.compare(y, other.y) == 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
