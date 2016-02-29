package com.coffeeintocode.jnibenchmark;

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
import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Sieve sieve = new Sieve();
    private final Random random = new Random();
    private final Heapsort heapsort = new Heapsort();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Onclick listener for running Sieve of Eratosthenes */
        Button sieveButton = (Button) findViewById(R.id.runsieve);
        sieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> results = new ArrayList<String>();
                int[] boundaries = {1, 10, 100, 500 , 1000, 5000, 10000, 20000};
                for(int i : boundaries) {
                    int boundary = 1000*i;
                    Log.i("TAG", "Boundary: " + Integer.toString(boundary));
                    String run = "";
                    for ( int j = 0; j < 50; ++j) {
                        run =  "JNI;" + Integer.toString(boundary) + ";";
                        long start_jni = System.currentTimeMillis();

                        int[] primes = runSieve(boundary);

                        long end_jni = System.currentTimeMillis();
                        run = run + Long.toString(end_jni-start_jni) + ";";
                        results.add(run);
                    }

                    for ( int j = 0; j < 50; ++j) {
                        run =  "Java;" + Integer.toString(boundary) + ";";
                        long start_java = System.currentTimeMillis();

                        List<Integer> primes_java = sieve.runSieveJava(boundary);

                        long end_java = System.currentTimeMillis();
                        run = run + Long.toString(end_java-start_java) + ";";
                        results.add(run);
                    }
                }
                String listString = "";

                for (String s : results)
                {
                    listString += s + "\n";
                }

                writeToFile(listString, "sieve_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Sieve run!");
            }
        });

        /* Onclick listener for generating random doubles */

        Button randomButton = (Button) findViewById(R.id.runrandom);
        randomButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("Tag", "Calculating Random values");

                //Store results
                List<String> results = new ArrayList<String>();
                String run = ""; //Each run Boundary:boundary;time -format

                int[] boundaries = {10000, 50000, 100000, 500000, 1000000, 5000000, 10000000, 50000000};
                for ( int boundary : boundaries) {
                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                    run = "JAVA;" + Integer.toString(boundary) + ";";
                    long start_java = System.currentTimeMillis();
                    for ( int j = 0; j < boundary; ++j) {
                        random.getRandom(10000.0);
                    }
                    long end_java = System.currentTimeMillis();
                    run = run + Long.toString(end_java - start_java);
                    results.add(run);

                    Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                    run = "JNI;" + Integer.toString(boundary) + ";";
                    long start_jni = System.currentTimeMillis();
                    for ( int j = 0; j < boundary; ++j) {
                        getRandom(10000.0);
                    }
                    long end_jni = System.currentTimeMillis();
                    run = run + Long.toString(end_jni - start_jni);
                    results.add(run);

                }

                // Write results to a file
                String listString = "";

                for (String s : results)
                {
                    listString += s + "\n";
                }

                writeToFile(listString, "random_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Random ran!");
            }
        });

        /* Onclick listener for running heapsort on double array */
        Button heapButton = (Button) findViewById(R.id.runheap);
        heapButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("Tag", "Running heapsort");

                List<String> results = new ArrayList<String>();
                String run = "";

                int[] boundaries = {10000, 50000, 100000, 500000, 1000000, 5000000};
                for ( int boundary : boundaries) {
                    double []ar = (double[]) Array.newInstance(double.class, boundary + 1);
                    for (int i=1; i<=boundary; i++) {
                        ar[i] = random.getRandom(1.00);
                    }
                    for( int i = 0; i < 10; ++i) {
                        Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                        run = "JNI;" + Integer.toString(boundary) + ";";
                        long start_jni = System.currentTimeMillis();

                        heapSort(boundary, ar);

                        long end_jni = System.currentTimeMillis();
                        run = run + Long.toString(end_jni - start_jni);
                        results.add(run);

                        run = "JAVA;" + Integer.toString(boundary) + ";";
                        long start_java = System.currentTimeMillis();

                        heapsort.heapsort(boundary, ar);

                        long end_java = System.currentTimeMillis();
                        run = run + Long.toString(end_java - start_java);
                        results.add(run);
                    }

                }

                String listString = "";

                for (String s : results)
                {
                    listString += s + "\n";
                }

                writeToFile(listString, "heapsort_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText("Heapsort ran!");

            }
        });

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
}

