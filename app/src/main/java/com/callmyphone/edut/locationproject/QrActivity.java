package com.callmyphone.edut.locationproject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class QrActivity extends AppCompatActivity {



    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static final int QR_SCANNER = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);

        openQrScanner();

    }


    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


    private void openQrScanner() {



        try {

            Intent intentQr = new Intent("com.google.zxing.client.android.SCAN");
            intentQr.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intentQr, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                //get string result of qr scanner
                final String contents = data.getStringExtra("SCAN_RESULT");
                System.out.println(contents);
                Log.d("loc", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111"+contents);

                //open maps activity with data location



                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //resultLocation.setText("your location is: " + contents);
                        Intent intentMaps = new Intent(QrActivity.this, GPSLocationActivity.class);
                        intentMaps.putExtra("type", QR_SCANNER);
                        intentMaps.putExtra("SCANRESULT", contents);
                        startActivity(intentMaps);
                    }
                });
            }
            if(resultCode == RESULT_CANCELED){
                //handle cancel
                Log.d("loc", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
            }
        }

    }










}
