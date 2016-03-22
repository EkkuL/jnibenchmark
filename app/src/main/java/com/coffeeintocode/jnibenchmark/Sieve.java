package com.coffeeintocode.jnibenchmark;

import java.util.ArrayList;
import java.util.List;

public class Sieve {
    public static List<Integer> runSieveJava(int upperBound) {

        List<Integer> primes = new ArrayList<Integer>();

        int upperBoundSquareRoot = (int) Math.sqrt(upperBound);
        boolean[] isComposite = new boolean[upperBound + 1];
        for (int m = 2; m <= upperBoundSquareRoot; m++) {
            if (!isComposite[m]) {
                primes.add(m);
                for (int k = m * m; k <= upperBound; k += m)
                    isComposite[k] = true;
            }
        }
        for (int m = upperBoundSquareRoot; m <= upperBound; m++)
            if (!isComposite[m])
                primes.add(m);

        return primes;
    }
}
