package com.coffeeintocode.jnibenchmark;

public class Fibonacci{
    public static long fibo(final int n)
    {
        return (n < 2) ? n : fibo(n - 1) + fibo(n - 2);
    }
}
