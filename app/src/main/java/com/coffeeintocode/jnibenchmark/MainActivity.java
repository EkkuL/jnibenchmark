package com.coffeeintocode.jnibenchmark;

import android.app.ActivityManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coffeeintocode.jnibenchmark.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Sieve sieve = new Sieve();
    private final Random random = new Random();
    private final Heapsort heapsort = new Heapsort();
    private final Matrix matrix = new Matrix();
    private final Strcat strcat = new Strcat();
    private final Nestedloop nestedloop = new Nestedloop();
    private final Hash hash = new Hash();
    private final Ackermann ackermann = new Ackermann();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button heapsizeButton = (Button) findViewById(R.id.heapsize);
        heapsizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long memory = Runtime.getRuntime().maxMemory();
                ((TextView) findViewById(R.id.jni_msgView)).setText("Heapsize: " + Long.toString(memory));
            }
        });
        /* Onclick listener for running Sieve of Eratosthenes */
        Button sieveButton = (Button) findViewById(R.id.runsieve);
        sieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> results = new ArrayList<String>();
                int[] boundaries = {1, 10, 100, 500, 1000, 5000, 10000};
                for (int i : boundaries) {
                    int boundary = 1000 * i;
                    Log.i("TAG", "Boundary: " + Integer.toString(boundary));
                    String run = "";

                    //Running Sieve on JNI
                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long run_avg = 0;

                    for (int j = 0; j < 50; ++j) {
                        long start_jni = System.currentTimeMillis();
                        int[] primes = runSieve(boundary);
                        long end_jni = System.currentTimeMillis();
                        run_avg += end_jni - start_jni;
                    }

                    run_avg = run_avg / 50;
                    run = run + Long.toString(run_avg);
                    results.add(run);

                    //Running Sieve on Java
                    run = "Java;" + Integer.toString(boundary) + ";";
                    run_avg = 0;

                    for (int j = 0; j < 50; ++j) {
                        long start_java = System.currentTimeMillis();
                        List<Integer> primes_java = sieve.runSieveJava(boundary);
                        long end_java = System.currentTimeMillis();
                        run_avg += end_java - start_java;
                    }
                    run_avg = run_avg / 50;
                    run = run + Long.toString(run_avg);
                    results.add(run);
                }

                String listString = listToString(results);

                writeToFile(listString, "sieve_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Sieve run!");
            }
        });

        /* Onclick listener for generating random doubles */

        Button randomButton = (Button) findViewById(R.id.runrandom);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Tag", "Calculating Random values");

                //Store results
                List<String> results = new ArrayList<String>();
                String run = ""; //Each run Boundary:boundary;time -format

                int[] boundaries = {10000, 50000, 100000, 500000, 1000000, 5000000};
                for (int boundary : boundaries) {
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    long start_java = System.currentTimeMillis();
                    for (int j = 0; j < boundary; ++j) {
                        random.getRandom(10000.0);
                    }
                    long end_java = System.currentTimeMillis();
                    run = run + Long.toString(end_java - start_java);
                    results.add(run);

                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long start_jni = System.currentTimeMillis();
                    for (int j = 0; j < boundary; ++j) {
                        getRandom(10000.0);
                    }
                    long end_jni = System.currentTimeMillis();
                    run = run + Long.toString(end_jni - start_jni);
                    results.add(run);

                }

                // Write results to a file
                String listString = listToString(results);

                writeToFile(listString, "random_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Random ran!");
            }
        });

        /* Onclick listener for running heapsort on double array */
        Button heapButton = (Button) findViewById(R.id.runheap);
        heapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Tag", "Running heapsort");

                List<String> results = new ArrayList<String>();
                String run = "";

                int[] boundaries = {10000, 50000, 100000, 500000, 1000000};
                for (int boundary : boundaries) {
                    double[] ar = (double[]) Array.newInstance(double.class, boundary + 1);
                    for (int i = 1; i <= boundary; i++) {
                        ar[i] = random.getRandom(1.00);
                    }

                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                    run = "JNI;" + Integer.toString(boundary) + ";";

                    long start_jni = System.currentTimeMillis();
                    for (int i = 0; i < 10; ++i) {
                        heapSort(boundary, ar);
                    }
                    long end_jni = System.currentTimeMillis();
                    run = run + Long.toString((end_jni - start_jni) / 10);
                    results.add(run);

                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    long start_java = System.currentTimeMillis();
                    for (int i = 0; i < 10; ++i) {
                        heapsort.heapsort(boundary, ar);
                    }
                    long end_java = System.currentTimeMillis();
                    run = run + Long.toString((end_java - start_java) / 10);
                    results.add(run);
                }

                String listString = listToString(results);

                writeToFile(listString, "heapsort_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Heapsort ran!");
            }
        });

        Button mmButton = (Button) findViewById(R.id.runmm);
        mmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //For storing results
                List<String> results = new ArrayList<String>();
                String run = "";

                Log.i("Tag", "Running matrix mult");

                int[] boundaries = {10, 50, 100, 500};

                for (int boundary : boundaries) {
                    //Create matrices for multiplication
                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long run_avg = 0;
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));

                    for (int i = 0; i < 10; ++i) {
                        long start_jni = System.currentTimeMillis();

                        matrixMult(boundary);

                        long end_jni = System.currentTimeMillis();
                        run_avg += end_jni - start_jni;
                    }
                    run = run + Long.toString(run_avg / 10);
                    results.add(run);
                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    run_avg = 0;
                    for (int i = 0; i < 10; ++i) {

                        long start_java = System.currentTimeMillis();

                        int[][] m1 = matrix.create(boundary);
                        int[][] m2 = matrix.create(boundary);
                        matrix.multiplication(boundary, m1, m2);

                        long end_java = System.currentTimeMillis();
                        run_avg += end_java - start_java;
                    }
                    run = run + Long.toString(run_avg);
                    results.add(run);

                }

                String listString = listToString(results);

                writeToFile(listString, "mmult.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Matrix mult ran!");

            }
        });

        Button sc = (Button) findViewById(R.id.runstrcat);
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> results = new ArrayList<String>();
                String run = "";

                Log.i("Tag", "Running string concat");

                int[] boundaries = {10000, 50000, 100000, 500000, 1000000};

                for (int boundary : boundaries) {
                    //Create matrices for multiplication
                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long run_avg = 0;
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));

                    for (int i = 0; i < 10; ++i) {
                        long start_jni = System.currentTimeMillis();

                        helloCat(boundary);

                        long end_jni = System.currentTimeMillis();
                        run_avg += end_jni - start_jni;
                    }
                    run = run + Long.toString(run_avg / 10);
                    results.add(run);
                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    run_avg = 0;
                    for (int i = 0; i < 10; ++i) {

                        long start_java = System.currentTimeMillis();

                        strcat.helloCat(boundary);

                        long end_java = System.currentTimeMillis();
                        run_avg += end_java - start_java;
                    }
                    run = run + Long.toString(run_avg);
                    results.add(run);

                }

                String listString = listToString(results);
                writeToFile(listString, "strcat_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("String concat ran!");
            }
        });

        Button nl = (Button) findViewById(R.id.runnl);
        nl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> results = new ArrayList<String>();
                String run = "";

                Log.i("Tag", "Running nested loop");

                int[] boundaries = range(1, 27, 2);

                for (int boundary : boundaries) {
                    //Create matrices for multiplication
                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long run_avg = 0;
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));

                    for (int i = 0; i < 10; ++i) {
                        long start_jni = System.currentTimeMillis();

                        nestedloop(boundary);

                        long end_jni = System.currentTimeMillis();
                        run_avg += end_jni - start_jni;
                    }
                    run = run + Long.toString(run_avg / 10);
                    results.add(run);
                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    run_avg = 0;
                    for (int i = 0; i < 10; ++i) {

                        long start_java = System.currentTimeMillis();

                        nestedloop.run(boundary);

                        long end_java = System.currentTimeMillis();
                        run_avg += end_java - start_java;
                    }
                    run = run + Long.toString(run_avg);
                    results.add(run);

                }

                String listString = listToString(results);
                writeToFile(listString, "nestedloop_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Nested loop ran!");
            }

        });

        Button hb = (Button) findViewById(R.id.runhash);
        hb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> results = new ArrayList<String>();
                String run = "";

                Log.i("Tag", "Running hash");

                int[] boundaries = range(1000, 50000, 1000);

                for (int boundary : boundaries) {

                    //Create string array of random doubles.
                    String[] values = new String[boundary];

                    for ( int i = 0; i < boundary; ++i){
                        double value = getRandom(1.00);
                        values[i] = Double.toString(value);
                    }

                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long run_avg = 0;
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));

                    for (int i = 0; i < 10; ++i) {
                        long start_jni = System.currentTimeMillis();

                        createHash( boundary, values);

                        long end_jni = System.currentTimeMillis();
                        run_avg += end_jni - start_jni;
                    }
                    run = run + Long.toString(run_avg / 10);
                    results.add(run);
                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    run_avg = 0;
                    for (int i = 0; i < 10; ++i) {

                        long start_java = System.currentTimeMillis();

                        hash.createHash(boundary, values);

                        long end_java = System.currentTimeMillis();
                        run_avg += end_java - start_java;
                    }
                    run = run + Long.toString(run_avg);
                    results.add(run);

                }

                String listString = listToString(results);
                writeToFile(listString, "hash.log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Hash ran!");
            }
        });

        Button ackerb = (Button) findViewById(R.id.runacker);
        ackerb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> results = new ArrayList<String>();
                String run = "";

                Log.i("Tag", "Running Ackermann");

                int[] boundaries1 = range(1, 10, 1);
                int[] boundaries2 = range(1, 4, 1);
                for (int boundary1 : boundaries2) {
                    for (int boundary2 : boundaries1){
                    run = "JNI;" + Integer.toString(boundary1) + ";" + Integer.toString(boundary2) + ";";
                    long run_avg = 0;
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary1) + ";" + Integer.toString(boundary2));

                    for (int i = 0; i < 10; ++i) {
                        long start_jni = System.currentTimeMillis();

                        ackermann(boundary1, boundary2);

                        long end_jni = System.currentTimeMillis();
                        run_avg += end_jni - start_jni;
                    }
                    run = run + Long.toString(run_avg / 10);
                    results.add(run);
                    if(boundary2 < 3) {
                        run = "JAVA;" + Integer.toString(boundary1) + ";" + Integer.toString(boundary2) + ";";
                        run_avg = 0;
                        for (int i = 0; i < 10; ++i) {

                            long start_java = System.currentTimeMillis();

                            ackermann.ack(BigInteger.valueOf(boundary1), BigInteger.valueOf(boundary2));

                            long end_java = System.currentTimeMillis();
                            run_avg += end_java - start_java;
                        }
                        run = run + Long.toString(run_avg);
                        results.add(run);
                    }
                    }

                }

                String listString = listToString(results);
                writeToFile(listString, "ackermann_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Ackermann ran!");
            }
        });


    }

    public int[] range(int start, int end, int step) {
        int n = (int) Math.ceil((end-start)/(double)step);
        int[] arange = new int[n];
        for (int i = 0; i < n; i++)
            arange[i] = i*step+start;
        return arange;
    }


    private String listToString(List<String> list){
        String listString = "";

        for (String s : list)
        {
            listString += s + "\n";
        }
        return listString;
    }

    private void writeToFile(String data, String filename) {

        String filePath = getApplicationContext().getFilesDir().getPath().toString() + "/" + filename;
        File file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, MODE_APPEND);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            outputStream.write(data.getBytes());
            Log.i("Test", "Writing done to: " + file.getCanonicalPath());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        System.loadLibrary("benchmark-jni");
    }
    public native int[] runSieve(int upperBound);

    public native double getRandom(double max);

    public native double[] heapSort(int arraysize, double[] arr);

    public native void matrixMult(int size);

    public native String helloCat(int n);

    public native int nestedloop(int n);

    public native void createHash(int n, String[] values);

    public native int ackermann(int m, int n);
}

