/*
Lagrange.java

Length 1 polynomial – 0th degree, length 2 polynomial - 1st degree, etc.
0th term – highest degree term

*/

import java.util.*;
import java.io.*;

public class Lagrange {
    public static void main (String [] args) {
        /*
        float[] a = {1, 1};
        float[] b = {1, 2};
        float[] c = {1, 3};
        float[] d = multiplyPolynomials(multiplyPolynomials(a, b), c);
        System.out.println(Arrays.toString(d));
        */

        Scanner inFile = null;

        try {
            inFile = new Scanner(new File("values.txt"));
        }
        catch (IOException ex) {
            System.out.println(ex);
        }

        int degree = Integer.parseInt(inFile.nextLine());
        float[] xs = new float[degree];
        float[] ys = new float[degree];

        String[] line = new String[2];
        String setpoints = "";

        for (int i = 0; i < degree; i++) {
            line = inFile.nextLine().split(" ");
            xs[i] = Float.parseFloat(line[0]);
            ys[i] = Float.parseFloat(line[1]);
            setpoints += "(" + xs[i] + ", " + ys[i] + ")";
            if (i != degree - 1) {
                setpoints += ", ";
            }
        }

        float[] result = getPolynomial(xs, ys);

        createHTML(convertToPolynomial(result), setpoints);

    }

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

    // This version of "addPolynomials" only works when both polynomials
    // are of the same degree, which is the case with our usage
    // but not always in the real world
    public static float[] addPolynomials (float[] a, float[] b) {
        int degree = a.length;
        float[] c = new float[degree];
        for (int i = 0; i < degree; i ++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static float[] getPolynomial (float[] xs, float[] ys) {
        int degree = xs.length;
        float[][] deltas = new float[degree][degree];
        float [] result = new float[degree];

        for (int i = 0; i < degree; i ++) {
            deltas[i] = getDeltaPolynomial(xs, i);
        }

        for (int i = 0; i < degree; i ++) {
            System.out.println(Arrays.toString(deltas[i]));
            result = addPolynomials(result, scalePoly(deltas[i], ys[i]));
        }

        return result;
    }

    public static float[] getDeltaPolynomial (float[] xs, int xpos) {
        float[] poly = {1};
        float denom = 1;
        for (int i = 0; i < xs.length; i ++) {
            if (i != xpos) {
                float[] currentTerm = {1, -xs[i]};
                denom *= xs[xpos] - xs[i];
                poly = multiplyPolynomials(poly, currentTerm);
            }
        }
        return scalePoly(poly, 1/denom);
    }

    public static float[] scalePoly (float[] a, float k) {
        float [] b = new float[a.length];
        for (int i = 0; i < a.length; i ++) {
            b[i] = a[i]*k;
        }
        return b;
    }

    // Taken from a different polynomial manipulation program I wrote

    // Takes the set of coefficients and converts it to a standard looking polynomial
    public static String convertToPolynomial(float[] coefs) {
        String s = "";
        int degree = coefs.length;
        for (int i = 0; i < degree; i ++) {
            if (coefs[i] > 0 && s != "") {
                s += "  +  ";
            }
            if (coefs[i] < 0) {
                coefs[i]*= -1;
                s += "  -  ";
            }
            if (coefs[i] != 0) {
                if (coefs[i] == (int) coefs[i]) {
                    if (coefs[i] != 1 || (coefs[i] == 1 && degree-i-1 == 0)) {
                        s += Integer.toString((int) coefs[i]);
                    }
                }
                else {
                    s += Float.toString(coefs[i]).substring(0, 4);
                }
            }
            if (coefs[i] != 0) {
                if (degree-i-1 == 1) {
                    s += "x";
                }
                else if (degree-i-1 > 1) {
                    s += "x^" + Integer.toString(degree-i-1);
                }
            }

        }
        return "f(x) = " + s;
    }

    public static String writeLatex (float[] coefs) {
        int degree = coefs.length;
        String document = "\\documentclass[20pt]{article}\n" +
        "\\usepackage{extsizes}" +
        "\\thispagestyle{empty}" +
        "\\begin{document}\n\\begin{Large}\n$$";
        String eq = convertToPolynomial(coefs);
        document += eq + "$$\n\\end{Large}\n\\end{document}";
        return document;
    }

    public static void createHTML (String poly, String setpoints) {
        String document = "<!DOCTYPE html PUBLIC" +
                "\n<html>\n<head>\n<title>lagrange-interpolation</title>" +
                "<script type='text/x-mathjax-config'>" +
                "MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});" +
                "\n</script>\n<script type='text/javascript'\nsrc=" +
                "'http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML'>" +
                "\n</script>\n</head>\n<body>\n<h1><center>Lagrange Interpolator</center></h1>" +
                "\n<h4><center><br><br>The polynomial that fits the set of points </center></h4>";

        document += "$$" + setpoints + "$$<br> <center>is</center>";
        document += "$$" + poly + "$$ \n</body>\n</html>";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("result.html", "UTF-8");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        writer.print(document);
        writer.close();

        Runtime p = Runtime.getRuntime();
        try {
            p.exec("open result.html");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

}