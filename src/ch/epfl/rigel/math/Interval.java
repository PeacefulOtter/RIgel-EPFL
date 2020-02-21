package ch.epfl.rigel.math;

public abstract class Interval
{
    private final double lowerBound;
    private final double upperBound;

    protected Interval( double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double low() {
        return lowerBound;
    }

    public double high() {
        return upperBound;
    }

    public double size() {
        return upperBound - lowerBound;
    }

    public abstract boolean contains( double v );

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals( Object object ) {
        throw new UnsupportedOperationException();
    }

}
