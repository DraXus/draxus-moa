/*
 *    DoubleVector.java
 *    Copyright (C) 2010 RWTH Aachen University, Germany
 *    @author Reidl (moa@cs.rwth-aachen.de)
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package moa.clusterers.clustree.util;

public class DoubleVector {

    /**
     * Adds the second array to the first array element by element. The arrays
     * must have the same length.
     * @param a1 Vector to which the second vector is added.
     * @param a2 Vector to be addded. This vector does not change.
     */
    public static void addVectors(double[] a1, double[] a2) {
        assert (a1 != null);
        assert (a2 != null);
        assert (a1.length == a2.length) : "Adding two arrays of different "
                + "length";

        for (int i = 0; i < a1.length; i++) {
            a1[i] += a2[i];
        }
    }

    /**
     * Copies an array of doubles.
     * @param a a vector (double array)
     * @return
     */
    public static double[] copyVector(double[] a) {
        final int length = a.length;
        double[] res = new double[length];
        /*for (int i = 0; i < res.length; i++) {
        res[i] = a[i];
        }*/
        System.arraycopy(a, 0, res, 0, length);
        return res;
    }

    /**
     * Normalizes the given vector.
     * @param a A vector (double array)
     */
    public static void normalizeVector(double[] a) {
        double length = calculateLength(a);
        for (int i = 0; i < a.length; i++) {
            a[i] /= length;
        }
    }

    /**
     * Scales the vector a by the factor f.
     * @param a A vector (double array)
     * @param f the Scale factor
     */
    public static void multiplyVector(double[] a, double f) {
        for (int i = 0; i < a.length; i++) {
            a[i] *= f;
        }
    }

    /**
     * Calculates the length of the given vector
     * @param a A vector (double array)
     */
    public static double calculateLength(double[] a) {
        double length = 0.0;
        for (int i = 0; i < a.length; i++) {
            length += a[i] * a[i];
        }
        return Math.sqrt(length);
    }

    /**
     * Calculates the euclidean distance between two given points.
     * Both double arrays must have the same number of elements,
     * otherwise an assertion fails.
     *
     * @param a1    The first point
     * @param a2   The second point
     * @return  The distance between both points
     */
    public static double calculateDistance(final double[] a1, final double[] a2) {
        assert (a1.length == a2.length);
        double distance = 0.0;
        int length = a1.length;
        for (int i = 0; i < length; i++) {
            double d = a1[i] - a2[i];
            distance += d * d;
        }

        return Math.sqrt(distance);
    }


    /**
     * @param vector
     * @return true if the vector has zero length
     */
    public static boolean isZero( double[] vector ) {
	for ( int i = 0; i < vector.length; i++ ) {
	    if ( vector[i] != 0.0 )
		return false;
	}
	return true;
    }

    /**
     * Adds a constant to all entries of this vector
     * @param vector
     * @param c
     */
    public static void addConstant(double[] vector, double c) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] += c;
        }
    }
}
