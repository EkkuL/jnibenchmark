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
import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final Sieve sieve = new Sieve();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button sieveButton = (Button) findViewById(R.id.runsieve);
        sieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.jni_msgView)).setText("Running sieve!");
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


        Button piButton = (Button) findViewById(R.id.runpi);
        piButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("Tag", "Calculating values");
                List<String> results = new ArrayList<String>();
                String run = "";

                int[] boundaries = {1, 10, 100, 500 , 1000, 5000};
                for ( int boundary : boundaries) {
                    for ( int j = 0; j < 50; ++j) {
                            Log.i("Tag", "Boundary: " + Integer.toString(boundary));
                            run = "JAVA;" + Integer.toString(boundary) + ";";
                            long start_java = System.currentTimeMillis();

                            //Call method here

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

                writeToFile(listString, "pi_log.txt");
                ((TextView) findViewById(R.id.jni_msgView)).setText(" ran!");
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




}
