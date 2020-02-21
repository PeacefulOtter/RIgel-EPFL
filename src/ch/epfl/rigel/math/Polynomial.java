package ch.epfl.rigel.math;

import java.sql.Array;
import java.util.Arrays;
import java.util.Locale;

public final class Polynomial
{
    private double[] coefficients;

    private Polynomial( double coefficientN, double... coefficients )
    {
        int len = coefficients.length;
        this.coefficients = new double[ len + 1 ];
        this.coefficients[ 0 ] = coefficientN;
        System.arraycopy( coefficients, 0, this.coefficients, 1, len );
    }

    public static Polynomial of( double coefficientN, double... coefficients )
    {
        if ( coefficientN == 0 ) { throw new IllegalArgumentException(); }
        return new Polynomial( coefficientN, coefficients );
    }

    double at( double x )
    {
        int len = coefficients.length;
        if ( len == 1 || x == 0 )
        {
            return coefficients[ len - 1 ];
        }

        double value = coefficients[ 0 ];

        System.out.println(Arrays.toString(this.coefficients));
        for ( int i = 1; i < len; i++ )
        {
            System.out.println(coefficients[i]);
            value = coefficients[ i ] + x * value;
            System.out.println(value);
        }
        return value;
    }

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
