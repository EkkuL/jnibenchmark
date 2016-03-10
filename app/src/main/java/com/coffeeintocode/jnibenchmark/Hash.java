package com.coffeeintocode.jnibenchmark;

import android.util.SparseArray;

import java.util.HashMap;

public class Hash{
    public void createHash(int length, String[] values){
        Integer[] keys = new Integer[length];
        int index = 10;

        HashMap<Integer, String> hash= new HashMap<Integer, String>();

        for(int i= 0; i < keys.length; i++){
            hash.put(keys[i], values[i]);
            index += 10;
        }
    }
}
