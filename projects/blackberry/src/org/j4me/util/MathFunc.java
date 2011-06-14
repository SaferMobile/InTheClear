package org.j4me.util;

/**
 * Implements the methods which are in the standard J2SE's <code>Math</code> class,
 * but are not in in J2ME's.
 * <p>
 * The following methods are still missing from the implementation:
 * <ul>
 *  <li><code>public static double exp (double a)</code>
 *  <li><code>public static double log (double a)</code>
 *  <li><code>public static double pow (double a, double b)</code>
 *  <li><code>public static double random ()</code>
 *  <li><code>public static double rint()</code>
 * </ul>
 * 
 * @see java.lang.Math
 */
public final class MathFunc
{
	/**
	 * Constant for PI divided by 2.
	 */
	private static final double PIover2 = Math.PI / 2;

	/**
	 * Constant for PI divided by 4.
	 */
	private static final double PIover4 = Math.PI / 4;

	/**
	 * Constant for PI divided by 6.
	 */
	private static final double PIover6 = Math.PI / 6;

	/**
	 * Constant for PI divided by 12.
	 */
	private static final double PIover12 = Math.PI / 12;

	/**
	 * Constant used in the <code>atan</code> calculation.
	 */
	private static final double ATAN_CONSTANT = 1.732050807569;

	/**
	 * Returns the arc cosine of an angle, in the range of 0.0 through <code>Math.PI</code>.
	 * Special case:
	 * <ul>
	 *  <li>If the argument is <code>NaN</code> or its absolute value is greater than 1,
	 *      then the result is <code>NaN</code>.
	 * </ul>
	 * 
	 * @param a - the value whose arc cosine is to be returned.
	 * @return the arc cosine of the argument.
	 */
	public static double acos (double a)
	{
		// Special case.
		if ( Double.isNaN(a) || Math.abs(a) > 1.0 )
		{
			return Double.NaN;
		}
		
		// Calculate the arc cosine.
		double aSquared = a * a;
		double arcCosine = atan2( Math.sqrt(1 - aSquared), a );
		return arcCosine;
	}

	/**
	 * Returns the arc sine of an angle, in the range of <code>-Math.PI/2</code> through
	 * <code>Math.PI/2</code>.  Special cases:
	 * <ul>
	 *  <li>If the argument is <code>NaN</code> or its absolute value is greater than 1,
	 *      then the result is <code>NaN</code>.
	 *  <li>If the argument is zero, then the result is a zero with the same sign
	 *      as the argument.
	 * </ul>
	 * 
	 * @param a - the value whose arc sine is to be returned.
	 * @return the arc sine of the argument.
	 */
	public static double asin (double a)
	{
		// Special cases.
		if ( Double.isNaN(a) || Math.abs(a) > 1.0 )
		{
			return Double.NaN;
		}

		if ( a == 0.0 )
		{
			return a;
		}
		
		// Calculate the arc sine.
		double aSquared = a * a;
		double arcSine = atan2( a, Math.sqrt(1 - aSquared) );
		return arcSine;
	}

	/**
	 * Returns the arc tangent of an angle, in the range of <code>-Math.PI/2</code>
	 * through <code>Math.PI/2</code>.  Special cases:
	 * <ul>
	 *  <li>If the argument is <code>NaN</code>, then the result is <code>NaN</code>.
	 *  <li>If the argument is zero, then the result is a zero with the same 
	 *      sign as the argument.
	 * </ul>
	 * <p>
	 * A result must be within 1 ulp of the correctly rounded result.  Results
	 * must be semi-monotonic.
	 * 
	 * @param a - the value whose arc tangent is to be returned. 
	 * @return the arc tangent of the argument.
	 */
	public static double atan (double a)
	{
		// Special cases.
		if ( Double.isNaN(a) )
		{
			return Double.NaN;
		}
		
		if ( a == 0.0 )
		{
			return a;
		}
		
		// Compute the arc tangent.
		boolean negative = false;
		boolean greaterThanOne = false;
		int i = 0;
		
		if ( a < 0.0 )
		{
			a = -a;
			negative = true;
		}
		
		if ( a > 1.0 )
		{
			a = 1.0 / a;
			greaterThanOne = true;
		}
		
		double t;
		
		for ( ; a > PIover12; a *= t )
		{
			i++;
			t = a + ATAN_CONSTANT;
			t = 1.0 / t;
			a *= ATAN_CONSTANT;
			a--;
		}
		
		double aSquared = a * a;
		
		double arcTangent = aSquared + 1.4087812;
		arcTangent = 0.55913709 / arcTangent;
		arcTangent += 0.60310578999999997;
		arcTangent -= 0.051604539999999997 * aSquared;
		arcTangent *= a;
		
		for ( ; i > 0; i-- )
		{
			arcTangent += PIover6;
		}
		
		if ( greaterThanOne )
		{
			arcTangent = PIover2 - arcTangent;
		}
		
		if ( negative )
		{
			arcTangent = -arcTangent;
		}
		
		return arcTangent;
	}
	
	/**
	 * Converts rectangular coordinates (x, y) to polar (r, <i>theta</i>).  This method
	 * computes the phase <i>theta</i> by computing an arc tangent of y/x in the range
	 * of <i>-pi</i> to <i>pi</i>.  Special cases:
	 * <ul>
	 *  <li>If either argument is <code>NaN</code>, then the result is <code>NaN</code>.
	 *  <li>If the first argument is positive zero and the second argument is
	 *      positive, or the first argument is positive and finite and the second
	 *      argument is positive infinity, then the result is positive zero.
	 *  <li>If the first argument is negative zero and the second argument is
	 *      positive, or the first argument is negative and finite and the second
	 *      argument is positive infinity, then the result is negative zero.
	 *  <li>If the first argument is positive zero and the second argument is 
	 *      negative, or the first argument is positive and finite and the second
	 *      argument is negative infinity, then the result is the <code>double</code> value 
	 *      closest to <i>pi</i>.
	 *  <li>If the first argument is negative zero and the second argument is 
	 *      negative, or the first argument is negative and finite and the second
	 *      argument is negative infinity, then the result is the <code>double</code> value
	 *      closest to <i>-pi</i>.
	 *  <li>If the first argument is positive and the second argument is positive
	 *      zero or negative zero, or the first argument is positive infinity and
	 *      the second argument is finite, then the result is the <code>double</code> value 
	 *      closest to <i>pi</i>/2.
	 *  <li>If the first argument is negative and the second argument is positive
	 *      zero or negative zero, or the first argument is negative infinity and
	 *      the second argument is finite, then the result is the <code>double</code> value
	 *      closest to <i>-pi</i>/2.
	 *  <li>If both arguments are positive infinity, then the result is the double
	 *      value closest to <i>pi</i>/4.
	 *  <li>If the first argument is positive infinity and the second argument is
	 *      negative infinity, then the result is the double value closest to 3*<i>pi</i>/4.
	 *  <li>If the first argument is negative infinity and the second argument is
	 *      positive infinity, then the result is the double value closest to -<i>pi</i>/4.
	 *  <li>If both arguments are negative infinity, then the result is the double
	 *      value closest to -3*<i>pi</i>/4.
	 * </ul>
	 * <p>
	 * A result must be within 2 ulps of the correctly rounded result.  Results
	 * must be semi-monotonic.
	 * 
	 * @param y - the ordinate coordinate
	 * @param x - the abscissa coordinate 
	 * @return the <i>theta</i> component of the point (r, <i>theta</i>) in polar
	 *   coordinates that corresponds to the point (x, y) in Cartesian coordinates.
	 */
	public static double atan2 (double y, double x)
	{
		// Special cases.
		if ( Double.isNaN(y) || Double.isNaN(x) )
		{
			return Double.NaN;
		}
		else if ( Double.isInfinite(y) )
		{
			if ( y > 0.0 ) // Positive infinity
			{
				if ( Double.isInfinite(x) )
				{
					if ( x > 0.0 )
					{
						return PIover4;
					}
					else
					{
						return 3.0 * PIover4;
					}
				}
				else if ( x != 0.0 )
				{
					return PIover2;
				}
			}
			else  // Negative infinity
			{
				if ( Double.isInfinite(x) )
				{
					if ( x > 0.0 )
					{
						return -PIover4;
					}
					else
					{
						return -3.0 * PIover4;
					}
				}
				else if ( x != 0.0 )
				{
					return -PIover2;
				}
			}
		}
		else if ( y == 0.0 )
		{
			if ( x > 0.0 )
			{
				return y;
			}
			else if ( x < 0.0 )
			{
				return Math.PI;
			}
		}
		else if ( Double.isInfinite(x) )
		{
			if ( x > 0.0 )  // Positive infinity
			{
				if ( y > 0.0 )
				{
					return 0.0;
				}
				else if ( y < 0.0 )
				{
					return -0.0;
				}
			}
			else  // Negative infinity
			{
				if ( y > 0.0 )
				{
					return Math.PI;
				}
				else if ( y < 0.0 )
				{
					return -Math.PI;
				}
			}
		}
		else if ( x == 0.0 )
		{
			if ( y > 0.0 )
			{
				return PIover2;
			}
			else if ( y < 0.0 )
			{
				return -PIover2;
			}
		}
		

		// Implementation a simple version ported from a PASCAL implementation:
		//   http://everything2.com/index.pl?node_id=1008481
		
		double arcTangent;
		
		// Use arctan() avoiding division by zero.
		if ( Math.abs(x) > Math.abs(y) )
		{
			arcTangent = atan(y / x);
		}
		else
		{
			arcTangent = atan(x / y); // -PI/4 <= a <= PI/4

			if ( arcTangent < 0 )
			{
				arcTangent = -PIover2 - arcTangent; // a is negative, so we're adding
			}
			else
			{
				arcTangent = PIover2 - arcTangent;
			}
		}
		
		// Adjust result to be from [-PI, PI]
		if ( x < 0 )
		{
			if ( y < 0 )
			{
				arcTangent = arcTangent - Math.PI;
			}
			else
			{
				arcTangent = arcTangent + Math.PI;
			}
		}
		
		return arcTangent;
	}

	/**
	 * Returns the closest <code>int</code> to the argument. The 
	 * result is rounded to an integer by adding 1/2, taking the 
	 * floor of the result, and casting the result to type <code>int</code>. 
	 * In other words, the result is equal to the value of the expression:
	 * <p>
	 * <pre>(int)Math.floor(a + 0.5f)</pre>
	 * <p>
	 * Special cases:
	 * <ul>
	 *  <li>If the argument is NaN, the result is 0.
	 *  <li>If the argument is negative infinity or any value less than or 
	 *      equal to the value of <code>Integer.MIN_VALUE</code>, the result is 
	 *      equal to the value of <code>Integer.MIN_VALUE</code>. 
	 *  <li>If the argument is positive infinity or any value greater than or 
	 *      equal to the value of <code>Integer.MAX_VALUE</code>, the result is 
	 *      equal to the value of <code>Integer.MAX_VALUE</code>.
	 * </ul> 
	 *
	 * @param  a - a floating-point value to be rounded to an integer.
	 * @return the value of the argument rounded to the nearest <code>int</code> value.
	 */
	public static int round (float a)
	{
		return (int)Math.floor( a + 0.5f );
	}

	/**
	 * Returns the closest <code>long</code> to the argument. The result 
	 * is rounded to an integer by adding 1/2, taking the floor of the 
	 * result, and casting the result to type <code>long</code>. In other 
	 * words, the result is equal to the value of the expression:
	 * <p>
	 * <pre>(long)Math.floor(a + 0.5d)</pre>
	 * <p>
	 * Special cases:
	 * <ul>
	 *  <li>If the argument is NaN, the result is 0.
	 *  <li>If the argument is negative infinity or any value less than or 
	 *      equal to the value of <code>Long.MIN_VALUE</code>, the result is 
	 *      equal to the value of <code>Long.MIN_VALUE</code>. 
	 *  <li>If the argument is positive infinity or any value greater than or 
	 *      equal to the value of <code>Long.MAX_VALUE</code>, the result is 
	 *      equal to the value of <code>Long.MAX_VALUE</code>.
	 * </ul> 
	 *
	 * @param a - a floating-point value to be rounded to a <code>long</code>.
	 * @return the value of the argument rounded to the nearest <code>long</code> value.
	 */
	public static long round (double a)
	{
		return (long)Math.floor( a + 0.5 );
	}
}
