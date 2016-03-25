package com.example.robbieginsburg.assignment1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView time1, activity1;
    private TextView time2, activity2;
    private TextView time3, activity3;
    private TextView time4, activity4;
    private TextView time5, activity5;
    private TextView time6, activity6;
    private TextView time7, activity7;
    private TextView time8, activity8;
    private TextView time9, activity9;
    private TextView time10, activity10;

    private Button start;

    private boolean press = false;

    BoundedService.MyBinder binder_;
    BoundedService myService;
    Boolean connected = false;

    private Handler myHandler = new Handler();

    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //start the bounded service
        Intent myIntent = new Intent(this, BoundedService.class);
        bindService(myIntent, mConnection, BIND_AUTO_CREATE);
        //bounded service is started

        start = (Button) findViewById(R.id.buttonStart);
        start.setOnClickListener(this);

        time1 = (TextView) findViewById(R.id.time1);
        activity1 = (TextView) findViewById(R.id.activity1);

        time2 = (TextView) findViewById(R.id.time2);
        activity2 = (TextView) findViewById(R.id.activity2);

        time3 = (TextView) findViewById(R.id.time3);
        activity3 = (TextView) findViewById(R.id.activity3);

        time4 = (TextView) findViewById(R.id.time4);
        activity4 = (TextView) findViewById(R.id.activity4);

        time5 = (TextView) findViewById(R.id.time5);
        activity5 = (TextView) findViewById(R.id.activity5);

        time6 = (TextView) findViewById(R.id.time6);
        activity6 = (TextView) findViewById(R.id.activity6);

        time7 = (TextView) findViewById(R.id.time7);
        activity7 = (TextView) findViewById(R.id.activity7);

        time8 = (TextView) findViewById(R.id.time8);
        activity8 = (TextView) findViewById(R.id.activity8);

        time9 = (TextView) findViewById(R.id.time9);
        activity9 = (TextView) findViewById(R.id.activity9);

        time10 = (TextView) findViewById(R.id.time10);
        activity10 = (TextView) findViewById(R.id.activity10);


        // the rest of the code in onCreate() pertains to the file that the data will be written in

        // specifies tha path to where the file will be created/stored
        // in this case, the file will be created/stored in the root directory
        File filePath = Environment.getExternalStorageDirectory();

        // name the file assignment.txt
        file = new File(filePath, "assignment1_Robert-Ginsburg_Asen-Radov.txt");

        // checks to see if the file already exists in the user's system, at the specified location
        if (file.exists()) {
            // if it does, clear all the data already in it
            try {
                PrintWriter writer = new PrintWriter(file);
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // if it doesn't, create the file
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        myHandler.removeCallbacks(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.buttonStart:

                // this is ensures that the start button can only be clicked once for an effect
                if(!press){
                    press = true;

                    // deletes the data that has already been collected by the service
                    // the service has been collecting data since it was created in onCreate()
                    // this deletes all that data when the start button is pushed
                    myService.setBlank();

                    // start the runnable class that will keep running until the app is done
                    myHandler.post(new Update());
                }

        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder_=(BoundedService.MyBinder)service;
            myService = binder_.getService();
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };

    private class Update implements Runnable {

        @Override
        public void run() {

            //I was getting NullPointerExceptions that were crashing the program
            //It is okay for the program to get one, I just don't want it to crash the program
            //To fix the program crashing, I add a try/catch that keeps the program running
            // when there is a NullPointerException

            //if there is a null pointer exception, ignore it
            try{
                // resets all the times and activities when the start button is pushed
                // this is needed because the service is started upon the app being opened,
                //   which means that data was being collected before the start button was pushed
                time1.setText((myService.getTime1()));
                activity1.setText((myService.getActivity1()));

                time2.setText((myService.getTime2()));
                activity2.setText((myService.getActivity2()));

                time3.setText((myService.getTime3()));
                activity3.setText((myService.getActivity3()));

                time4.setText((myService.getTime4()));
                activity4.setText((myService.getActivity4()));

                time5.setText((myService.getTime5()));
                activity5.setText((myService.getActivity5()));

                time6.setText((myService.getTime6()));
                activity6.setText((myService.getActivity6()));

                time7.setText((myService.getTime7()));
                activity7.setText((myService.getActivity7()));

                time8.setText((myService.getTime8()));
                activity8.setText((myService.getActivity8()));

                time9.setText((myService.getTime9()));
                activity9.setText((myService.getActivity9()));

                time10.setText((myService.getTime10()));
                activity10.setText((myService.getActivity10()));

                // begin code for writing to external storage
                String externalStorage = Environment.getExternalStorageState();

                // this checks to see if external storage is available
                // if it is, then write to it
                if (Environment.MEDIA_MOUNTED.equals(externalStorage)) {

                        try{

                        // the content that will be written to the file on external storage
                        String content = myService.getTime1() + " " + myService.getActivity1() + "\n";

                        // this is the code that writes the content to the file on the
                        // external device
                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                        fileOutputStream.write(content.getBytes());
                        fileOutputStream.close();
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // if external storage is not available, write this Toast message
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "External Storage Is Not Available", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            //do nothing and keep running the program
            catch (NullPointerException e){
                e.printStackTrace();
            }

            // tells the runnable to run again every two minutes
            // it tells the app to update the UI and write to the file every 2 minutes
            // value is multiplied by 1000 because the function takes time in milliseconds
            myHandler.postDelayed(this, myService.getTwoMinutesInSeconds() * 1000);
        }
    }
}
