package com.coffeeintocode.jnibenchmark;

public class Matrix {
    //Multiplies a square matrix. Both matrices must be size x size.
    public static int[][] multiplication (int size,
                                          int[][] m1, int[][] m2){
        int m3[][] =new int[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                int val = 0;
                for (int k=0; k<size; k++) {
                    val += m1[i][k] * m2[k][j];
                }
                m3[i][j] = val;
            }
        }
        return(m3);
    }

    public static int[][] create (int size) {
        int count = 1;
        int m[][] = new int[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                m[i][j] = count++;
            }
        }
        return(m);
    }
}
