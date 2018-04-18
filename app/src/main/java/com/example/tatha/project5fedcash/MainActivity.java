package com.example.tatha.project5fedcash;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;

import static java.lang.Thread.sleep;

import com.example.tatha.Project5Common.IMyAidlInterface;

public class MainActivity extends Activity implements View.OnClickListener {

    Spinner sp;
    int position =0;
    int a;
    IMyAidlInterface mIRemoteService;
    EditText year_1, date_2, wdays_2, year_3;
    TextView tv;
    Button submit1;
    ArrayList<String> list = new ArrayList<String>();
    private boolean mIsBound;
    private int[] ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = findViewById(R.id.spinner);
        year_1 = findViewById(R.id.etYear1);
        year_3 = findViewById(R.id.etYear3);
        date_2 = findViewById(R.id.etDate2);
        wdays_2 = findViewById(R.id.etNumber2);
        submit1 = findViewById(R.id.bSubmit1);
        tv = findViewById(R.id.test);

        //adding list with api calls to populate the spinner
        list.add("Monthly Cash");
        list.add("Daily Cash");
        list.add("Yearly Average");

        //hiding the keyboard after clicking done on keyboard
        year_1.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        //hiding the keyboard after clicking done on keyboard
        wdays_2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        //hiding the keyboard after clicking done on keyboard
        year_3.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

        //setting on click listener for submit button
        submit1.setOnClickListener(this);

        //creating an adapter and setting it to spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

        //on item selected listener
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                position = pos;
                //based on the item selected
                //specific widgets are hidden and other are made visible
                switch (position) {


                    case 0:
                        year_1.setVisibility(View.VISIBLE);
                        year_3.setVisibility(View.GONE);
                        date_2.setVisibility(View.INVISIBLE);
                        wdays_2.setVisibility(View.INVISIBLE);

                        break;
                    case 1:
                        year_1.setVisibility(View.INVISIBLE);
                        year_3.setVisibility(View.GONE);
                        date_2.setVisibility(View.VISIBLE);
                        wdays_2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        year_1.setVisibility(View.INVISIBLE);
                        year_3.setVisibility(View.VISIBLE);
                        date_2.setVisibility(View.INVISIBLE);
                        wdays_2.setVisibility(View.INVISIBLE);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        //creating an intent to bind service
        Intent intent = new Intent(IMyAidlInterface.class.getName());
        //getting package names and class names
        ResolveInfo info= getPackageManager().resolveService(intent,Context.BIND_AUTO_CREATE);
        intent.setComponent(new ComponentName(info.serviceInfo.packageName,info.serviceInfo.name));
        mIsBound= bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsBound) {
            //creating an intent to bind service
            Intent intent = new Intent(IMyAidlInterface.class.getName());
            //getting package names and class names
            ResolveInfo info= getPackageManager().resolveService(intent,Context.BIND_AUTO_CREATE);
            intent.setComponent(new ComponentName(info.serviceInfo.packageName,info.serviceInfo.name));
            mIsBound= bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);

        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this.mConnection);
    }


    void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

    @Override
    public void onClick(View view) {
        //if bounded starting the worker thread
        if (mIsBound) {
            Thread t1 = new Thread(new runService(position));
            t1.start();
        }
        else if(!mIsBound){
            Toast.makeText(getApplicationContext(),"Service is not bound",Toast.LENGTH_LONG).show();
        }

    }

    //creating service connection class
    private final ServiceConnection mConnection = new ServiceConnection() {
        //when service connected getting reference of the binder
        //making mIsBound true
        public void onServiceConnected(ComponentName className, IBinder iservice) {

            mIRemoteService = IMyAidlInterface.Stub.asInterface(iservice);

            mIsBound = true;

        }

        //when service is disconnected
        //making service null and mIsBound flase
        public void onServiceDisconnected(ComponentName className) {

            mIRemoteService = null;

            mIsBound = false;

        }
    };

    //worker thread for making api calls
    class runService implements Runnable {
        int p;
        //needs the position of which api call was clicked
        runService(int pos){
            p=pos;
        }

        @Override
        public void run() {
            //if service is bound
            if(mIsBound){
                try {


                    switch (p) {
                        case 0:
                            String result1="";

                            final int s1 =Integer.parseInt(year_1.getText().toString());
                            //year should be between 2006 and 2016
                            if(s1>2005 && s1<2017) {
                                //making api call
                                ab = mIRemoteService.monthlyCash(s1);
                                //appending all the values to a string
                                for (int i = 0; i < ab.length; i++) {
                                    result1 += ab[i] + "\n";

                                }
                                final String finalResult = result1;
                                //using UI thread to start intent and sending the results
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {


                                        Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                                        i.putExtra("input", "MonthlyCash " + s1);
                                        i.putExtra("out", "MonthlyCashResult " + finalResult);
                                        startActivity(i);
                                    }
                                });
                            }
                            else{
                                //if invalid values entered making a toast
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            break;
                        case 1:
                            final int[] b;
                            final String[] date;
                            String result2 = "";
                            //getting user input
                            final String s2 = date_2.getText().toString();
                            final String s3 = wdays_2.getText().toString();
                            int day, month, year, wdays;
                            //using split to get individual values from entire date
                            date=s2.split("/");

                            day = Integer.parseInt(date[1]);
                            month = Integer.parseInt(date[0]);
                            year = Integer.parseInt(date[2]);
                            wdays = Integer.parseInt(s3);
                            //validating date
                            if(year>2005 && year<2017 && month>0 && month<13 && day>0 && day<31 && wdays>4 && wdays<26) {
                                //making the api call
                                b = mIRemoteService.dailyCash(day, month, year, wdays);

                                //appending all the values to a string
                                for (int i = 0; i < b.length; i++) {
                                    result2 += b[i] + " ";

                                }
                                final String finalResult2 = result2;
                                //using ui thread to start intent and send values
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                                        i.putExtra("input", "DailyCash  " + s2 + " " + s3);
                                        i.putExtra("out", "MonthlyCashResult " + finalResult2);
                                        startActivity(i);
                                    }
                                });
                            }
                            else{
                                //if invalid values entered making a toast
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            break;
                        case 2:
                            final int c;
                            final int s4= Integer.parseInt(year_3.getText().toString());
                            //validating input
                            if(s4>2005 && s4<2017) {

                                c = mIRemoteService.yearlyAvg((s4));
                                //using ui thread to create intent and send results
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent i = new Intent(MainActivity.this, ResultsActivity.class);
                                        i.putExtra("input", "YearlyAverage  " + s4);
                                        i.putExtra("out", "YearlyAverageResult " + c);
                                        startActivity(i);
                                    }
                                });
                            }else{
                                //if invalid values entered making a toast
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                            break;

                    }
                }catch (RemoteException re){}
            }

            else{
            }

        }
    }



}