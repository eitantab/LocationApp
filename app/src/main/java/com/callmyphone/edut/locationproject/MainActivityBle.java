package com.callmyphone.edut.locationproject;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivityBle extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private final static String TAG = MainActivityBle.class.getSimpleName();
    public static String dbValue="";
    public static String dbValue2="";
    public static final int REQUEST_ENABLE_BT = 1;
    public static final int BTLE_SERVICES = 2;

    private HashMap<String, BTLE_Device> mBTDevicesHashMap;
    private ArrayList<BTLE_Device> mBTDevicesArrayList;
    private ListAdapter_BTLE_Devices adapter;
    private ListView listView;

    private Button btn_Scan;

    private BroadcastReceiver_BTState mBTStateUpdateReceiver;
    private Scanner_BTLE mBTLeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ble);



        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Utils.toast(getApplicationContext(), "BLE not supported");
            finish();
        }

        mBTStateUpdateReceiver = new BroadcastReceiver_BTState(getApplicationContext());
        mBTLeScanner = new Scanner_BTLE(this, 5000, -75);

        mBTDevicesHashMap = new HashMap<>();
        mBTDevicesArrayList = new ArrayList<>();

        adapter = new ListAdapter_BTLE_Devices(this, R.layout.btle_device_list_item, mBTDevicesArrayList);

        listView = new ListView(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        btn_Scan = (Button) findViewById(R.id.btn_scan);
        ((ScrollView) findViewById(R.id.scrollView)).addView(listView);
        findViewById(R.id.btn_scan).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onResume() {
        super.onResume();

//        registerReceiver(mBTStateUpdateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();

//        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mBTStateUpdateReceiver);
        stopScan();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        Toast.makeText(getApplicationContext(),"MAINACTIVITIY=BLE:"+ data.getStringExtra("EXTRA_NAME"), Toast.LENGTH_LONG).show();
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
//                Utils.toast(getApplicationContext(), "Thank you for turning on Bluetooth");
            }
            else if (resultCode == RESULT_CANCELED) {
                Utils.toast(getApplicationContext(), "Please turn on Bluetooth");
            }
        }
        else if (requestCode == BTLE_SERVICES) {
            // Do something
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

         final String bleAddress = mBTDevicesArrayList.get(position).getAddress();

       // Toast.makeText(this, "blaaaaaaaaaaa"+bleAddress, Toast.LENGTH_SHORT).show();

        //just for adding the device to db. after delete..
        FirebaseDatabase database = FirebaseDatabase.getInstance();
         DatabaseReference myRef = database.getReference();
        final boolean[] flag = {false};
        ValueEventListener dbName = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {
                    Object dbKey = child.getKey();
                    dbValue=(String) child.getValue();
                    Log.d("inDataChange:value", (String) dbKey);
                    Log.d("inDataChange:bleAddress",""+bleAddress );
                   if(dbKey.equals(""+bleAddress) &&flag[0]==false)
                   {
                       //Toast.makeText(getApplicationContext(), "HAIM!!"+dbValue, Toast.LENGTH_LONG).show();

                       flag[0] =true;
                       Intent intentMaps = new Intent(MainActivityBle.this, GPSLocationActivity.class);
                       intentMaps.putExtra("type", 1);
                       intentMaps.putExtra("SCANRESULT",dbValue );
                       startActivity(intentMaps);

                   }


                }
                if(flag[0])
                {
                    //Toast.makeText(getApplicationContext(), "EXIST!!", Toast.LENGTH_LONG).show();
//                    Handler handler = new Handler();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            //resultLocation.setText("your location is: " + contents);
//                            Intent intentMaps = new Intent(MainActivityBle.this, GPSLocationActivity.class);
//                            intentMaps.putExtra("type", 1);
//                            intentMaps.putExtra("SCANRESULT",dbValue );
//                            startActivity(intentMaps);
//                        }
//                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "BLE dose not exist in Data Base!!!", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "Try finding location with different technology", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





 //********
//        Utils.toast(context, "List Item clicked");

        // do something with the text views and start the next activity.

        stopScan();

//       long time= System.currentTimeMillis();
//       while ((time+8000)>System.currentTimeMillis()){}
//
//
//        String name = mBTDevicesArrayList.get(position).getName();
//        String address = mBTDevicesArrayList.get(position).getAddress();


        /*Intent intent = new Intent(this, Activity_BTLE_Services.class);
        intent.putExtra(Activity_BTLE_Services.EXTRA_NAME, name);
        intent.putExtra(Activity_BTLE_Services.EXTRA_ADDRESS, address);
        startActivityForResult(intent, BTLE_SERVICES);*/

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_scan:
                Utils.toast(getApplicationContext(), "Scan Button Pressed");

                if (!mBTLeScanner.isScanning()) {
                    startScan();
                }
                else {
                    stopScan();
                }

                break;
            default:
                break;
        }

    }

    public void addDevice(BluetoothDevice device, int rssi) {

        String address = device.getAddress();
        if (!mBTDevicesHashMap.containsKey(address)) {
            BTLE_Device btleDevice = new BTLE_Device(device);
            btleDevice.setRSSI(rssi);

            mBTDevicesHashMap.put(address, btleDevice);
            mBTDevicesArrayList.add(btleDevice);
        }
        else {
            mBTDevicesHashMap.get(address).setRSSI(rssi);
        }

        adapter.notifyDataSetChanged();
    }

    public void startScan(){
        btn_Scan.setText("Scanning...");

        mBTDevicesArrayList.clear();
        mBTDevicesHashMap.clear();

        mBTLeScanner.start();
    }

    public void stopScan() {
        btn_Scan.setText("Scan Again");

        mBTLeScanner.stop();
    }
}
