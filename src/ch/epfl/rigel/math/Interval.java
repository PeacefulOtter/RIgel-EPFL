package ch.epfl.rigel.math;

/**
 * Interval abstract class
 */
public abstract class Interval
{
    // the lower bound
    private final double lowerBound;
    // the upper bound
    private final double upperBound;

    protected Interval( double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * @return the lower bound
     */
    public double low() {
        return lowerBound;
    }

    /**
     * @return the upper bound
     */
    public double high() {
        return upperBound;
    }

    /**
     * @return the interval size
     */
    public double size() {
        return upperBound - lowerBound;
    }

    /**
     * Check if some value is inside the interval or not
     * @param v
     * @return boolean
     */
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
