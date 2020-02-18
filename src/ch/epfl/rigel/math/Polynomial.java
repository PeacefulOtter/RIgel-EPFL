package ch.epfl.rigel.math;

import java.util.Locale;

public final class Polynomial
{
    private double[] coefficients;

    private Polynomial( double coefficientN, double... coefficients )
    {
        int len = coefficients.length;
        this.coefficients = new double[ len + 1 ];
        System.arraycopy( coefficients, 0, this.coefficients, 0, len );
        this.coefficients[ len ] = coefficientN;
    }

    Polynomial of( double coefficientN, double... coefficients )
    {
        if ( coefficientN == 0 ) { throw new IllegalArgumentException(); }
        return new Polynomial( coefficientN, coefficients );
    }

    double at( double x )
    {
        return 0;
    }

    @Override
    public String toString()
    {
        String polynomial = "";
        int exponent = coefficients.length;
        for ( double coef : coefficients )
        {
            if ( coef > 0 ) {
                polynomial += "+";
            } else if ( coef == 0 )
            {
                continue;
            }
            polynomial += String.format(Locale.ROOT, "%sX^(%s)", coef, exponent);
            exponent--;
        }
        System.out.println(polynomial);
        return polynomial;
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
