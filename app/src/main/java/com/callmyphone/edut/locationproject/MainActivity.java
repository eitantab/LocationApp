package com.callmyphone.edut.locationproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {


    Button btnQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intentQr = new Intent(MainActivity.this, QrActivity.class);
//        startActivity(intentQr);
        /*btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    public void btnGps(View view) {
        Intent intentGPS = new Intent(this, GPSLocationActivity.class);
        startActivity(intentGPS);
    }

    public void btnBle(View view) {
        Intent intentBLE = new Intent(this, MainActivityBle.class);
        startActivity(intentBLE);

        //**
        final int result = 1;
        intentBLE.putExtra("callingActivity", "MainActivity");
        startActivityForResult(intentBLE,result);
    }


    public void btnQr(View view) {
        Intent intentQr = new Intent(MainActivity.this, QrActivity.class);
        startActivity(intentQr);
    }

    //**
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(this,data.getStringExtra("DeviceName"),Toast.LENGTH_LONG); //DeviceName is like UsersName
        Toast.makeText(getApplicationContext(), data.getStringExtra("DeviceName"),
                Toast.LENGTH_LONG).show();
    }
}
