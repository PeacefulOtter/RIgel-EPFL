package ch.epfl.rigel.math;

public final class Polynomial
{
    // store the polynomial coefficient
    private double[] coefficients;

    /**
     * Build the polynomial using the coefficients and the last coefficient (N)
     * @param coefficientN : last coefficient
     * @param coefficients : all the other coefficients
     */
    private Polynomial( double coefficientN, double... coefficients )
    {
        int len = coefficients.length;
        this.coefficients = new double[ len + 1 ];
        this.coefficients[ 0 ] = coefficientN;
        System.arraycopy( coefficients, 0, this.coefficients, 1, len );
    }

    /**
     * Creates a new Polynomial with the given coefficients
     * @param coefficientN : last coefficient
     * @param coefficients : all the other coefficients
     * @return the polynomial
     */
    public static Polynomial of( double coefficientN, double... coefficients )
    {
        if ( coefficientN == 0 ) { throw new IllegalArgumentException(); }
        return new Polynomial( coefficientN, coefficients );
    }

    /**
     * Give the value of the polynomial at a certain x
     * @param x
     * @return the value at x
     */
    public double at( double x )
    {
        int len = coefficients.length;
        if ( len == 1 || x == 0 )
        {
            return coefficients[ len - 1 ];
        }

        double value = coefficients[ 0 ];

        for ( int i = 1; i < len; i++ )
        {
            value = coefficients[ i ] + x * value;
        }
        return value;
    }

    /**
     * Build a string to display the polynomial in a "readable" way
     * @return the polynomial string
     */
    @Override
    public String toString()
    {
        int len = coefficients.length;
        StringBuilder polynomial = new StringBuilder();

        for ( int i = 0; i < len; i++ )
        {
            double coef = coefficients[ i ];
            if ( coef == 0 )
            {
                continue;
            } else if ( coef < 0 ) {
                polynomial.append( "-" );
                coef = Math.abs( coef );
            } else if ( coef > 0 && i > 0 )
            {
                polynomial.append( "+" );
            }

            if ( coef != 1.0 )
            {
                polynomial.append( coef );
            }

            if ( i < len - 1 )
            {
                polynomial.append( "x" );
            }
            if ( i < len-2 )
            {
                polynomial.append( "^"+(len-i-1) );
            }

        }
        return polynomial.toString();
    }

    @Override
    public boolean equals( Object o )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode()
    {
        throw new UnsupportedOperationException();
    }
}
