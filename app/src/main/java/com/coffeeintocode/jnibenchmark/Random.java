package com.coffeeintocode.jnibenchmark;

public class Random {

    public static final long IM = 139968;
    public static final long IA = 3877;
    public static final long IC = 29573;

    public static long last = 42;
    public static double getRandom(double max) {
        return( max * (last = (last * IA + IC) % IM) / IM );
    }
}
