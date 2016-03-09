package com.coffeeintocode.jnibenchmark;

public class Strcat {
    StringBuffer helloCat(int n){
        StringBuffer str = new StringBuffer();

        for(int i = 0; i<n;i++)
        {
            str.append("hello\n");
        }
        return str;
    }
}
