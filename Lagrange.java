// Lagrange.java

import java.util.*;

public class Lagrange {
    public static void main (String [] args) {
        /*
        float[] a = {1, 1};
        float[] b = {1, 2};
        float[] c = {1, 3};
        float[] d = multiplyPolynomials(multiplyPolynomials(a, b), c);
        System.out.println(Arrays.toString(d));
        */
        float[] a = {1, 4, 9};
        float[] b = {2, 3};
        System.out.println(Arrays.toString(multiplyPolynomials(a, b)));
    }

    // Length 1 polynomial – 0th degree
    // Length 2 polynomial - 1st degree, etc.
    // 0th term – highest degree term
    public static float[] multiplyPolynomials (float[] a, float[] b) {
        int degA = a.length;
        int degB = b.length;
        float[] result = new float[degA + degB - 1];
        for (int i = 0; i < degA; i ++) {
            for (int j = 0; j < degB; j ++) {
                result[i + j] += a[i]*b[j];
            }
        }
        return result;
    }

}