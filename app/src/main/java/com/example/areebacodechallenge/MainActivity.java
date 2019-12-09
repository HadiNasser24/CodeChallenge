package com.example.areebacodechallenge;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private static final String TAG = "kkkkkkkkkkkkkkkk";
    private long backPressedTime;
    private boolean back=true;
    static BluetoothAdapter mBluetoothAdapter;
   static public ArrayList<BluetoothDevice> BTDevices,btArray = new ArrayList<>();
   static public DeviceListAdapter DeviceListAdapter;
   static ListView NewDevices,pairedDevicesList;
   ImageView backGround;
   ConstraintLayout mainLayout;
   LinearLayout Title,Discover,OnOff,Scan,Pair,Head;
   ImageButton btnOnOff;

   static final int STATE_lISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED= 4;
    boolean btnBackground=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();
        StartAnimation();

    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: called.");
        super.onDestroy();
        unregisterReceiver(BroadcastReceivers.BroadcastReceiver1);
        unregisterReceiver(BroadcastReceivers.BroadcastReceiver2);
        unregisterReceiver(BroadcastReceivers.BroadcastReceiver3);
        unregisterReceiver(BroadcastReceivers.BroadcastReceiver4);
        mBluetoothAdapter.cancelDiscovery();
    }

    //Initialization
    public void Initialize(){
        mainLayout=(ConstraintLayout)findViewById(R.id.mainLayout);
        backGround=(ImageView) findViewById(R.id.backGround);
        Title=(LinearLayout) findViewById(R.id.title);
        OnOff=(LinearLayout) findViewById(R.id.OnOff);
        Discover=(LinearLayout) findViewById(R.id.Discover);
        Scan=(LinearLayout) findViewById(R.id.Scan);
        Pair=(LinearLayout) findViewById(R.id.Pair);
        Head=(LinearLayout) findViewById(R.id.head);
        btnOnOff=(ImageButton) findViewById(R.id.btnOnOff);
        pairedDevicesList=(ListView)findViewById(R.id.pairedDevices);
        NewDevices = (ListView) findViewById(R.id.newDevices);
        BTDevices = new ArrayList<>();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if(mBluetoothAdapter.isEnabled()){
            OnOff.setBackgroundColor(getResources().getColor(R.color.enableDisable));
            btnOnOff.setBackgroundColor(getResources().getColor(R.color.enableDisable));
            btnBackground=true;
        }else{
            OnOff.setBackgroundColor(getResources().getColor(R.color.white));
            btnOnOff.setBackgroundColor(getResources().getColor(R.color.white));
            btnBackground=false;
        }

        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(BroadcastReceivers.BroadcastReceiver3, discoverDevicesIntent);

        DeviceListAdapter = new DeviceListAdapter(getApplicationContext(), R.layout.device_adapter_view, BTDevices);
        NewDevices.setAdapter(DeviceListAdapter);

        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(BroadcastReceivers.BroadcastReceiver4,filter);

        NewDevices.setOnItemClickListener(MainActivity.this);

        Set<BluetoothDevice> bt =mBluetoothAdapter.getBondedDevices();

        if(bt.size()>0){
            for(BluetoothDevice device:bt){
                btArray.add(device);
            }
            DeviceListAdapter adapter=new DeviceListAdapter(getApplicationContext(), R.layout.device_adapter_view,btArray );
            pairedDevicesList.setAdapter(adapter);
        }


    }

    //Animation
    public void StartAnimation(){

        backGround.animate().translationY(-1150).setDuration(1000).setStartDelay(3500);
        Title.animate().scaleXBy(1.2f).scaleYBy(1.2f).translationY(-1150).setDuration(1000).setStartDelay(3500);
        OnOff.animate().scaleX(1f).scaleY(1f).setDuration(600).setStartDelay(4000);
        Discover.animate().scaleX(1f).scaleY(1f).setDuration(600).setStartDelay(4200);
        Scan.animate().scaleX(1f).scaleY(1f).setDuration(600).setStartDelay(4400);
        Pair.animate().scaleX(1f).scaleY(1f).setDuration(600).setStartDelay(4600);
        Head.animate().translationX(0).setDuration(600).setStartDelay(4200);


    }


    public void enableDisableBT(View view) {

        if(mBluetoothAdapter == null){
            Log.i(TAG, "enableDisableBT: Does not have BT capabilities.");
            return;
        }

        //setting background color
        if(!btnBackground){
            OnOff.setBackgroundColor(getResources().getColor(R.color.enableDisable));
            btnOnOff.setBackgroundColor(getResources().getColor(R.color.enableDisable));
            btnBackground=true;
        }else{
            OnOff.setBackgroundColor(getResources().getColor(R.color.white));
            btnOnOff.setBackgroundColor(getResources().getColor(R.color.white));
            btnBackground=false;
        }


        if(!mBluetoothAdapter.isEnabled()){
            Log.i(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(BroadcastReceivers.BroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdapter.isEnabled()){
            Log.i(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(BroadcastReceivers.BroadcastReceiver1, BTIntent);
        }
    }

    public void btnEnableDisable_Discover(View view) {
        Log.i(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(BroadcastReceivers.BroadcastReceiver2,intentFilter);

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Scan(View view) {
        Log.i(TAG, "btnDiscover: Starting discovery.");
        //check BT permissions in manifest
       // checkBTPermissions();

        mBluetoothAdapter.startDiscovery();

        Log.i(TAG, "btnDiscover: Looking for devices...");
        Toast.makeText(getApplicationContext(),"Looking for devices...",Toast.LENGTH_SHORT).show();
        mainLayout.animate().translationX(-800).setDuration(500);
        NewDevices.animate().translationX(0).setDuration(500);
        back=false;
        }




    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {

        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        mBluetoothAdapter.cancelDiscovery();
        Log.i(TAG,"You clicked on a device");
        String deviceName=BTDevices.get(position).getName();
        String deviceAddress=BTDevices.get(position).getAddress();

        Log.i(TAG,"Device Name: "+deviceName);
        Log.i(TAG,"Device Address: "+deviceAddress);


        Log.i(TAG,"Trying to pair with "+deviceName);
        if(BTDevices.get(position).getBondState()== BluetoothDevice.BOND_BONDED){
            Log.i(TAG,"Already Paired");
            Toast.makeText(getApplicationContext(),"Already Paired",Toast.LENGTH_SHORT).show();
        }else {
            BTDevices.get(position).createBond();
        }
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime+2000>System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }else if(!back){
            back=true;
            mainLayout.animate().translationX(0).setDuration(500);
            NewDevices.animate().translationX(800).setDuration(500);
            pairedDevicesList.animate().translationX(800).setDuration(500);
        }else{
            Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime=System.currentTimeMillis();
    }



    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case STATE_lISTENING:
                    Log.i(TAG,"Listening");
                    break;
                case STATE_CONNECTING:
                    Log.i(TAG,"Connecting");
                    break;
                case STATE_CONNECTED:
                    Log.i(TAG,"Connected");
                    break;
                case STATE_CONNECTION_FAILED:
                    Log.i(TAG,"Connection Failed");

                    break;

            }
            return false;
        }
    });

    public void PairedDevices(View view) {
        back=false;
        btArray.clear();
        Set<BluetoothDevice> bt =mBluetoothAdapter.getBondedDevices();
        if(bt.size()>0){
            for(BluetoothDevice device:bt){
                btArray.add(device);
            }
            DeviceListAdapter adapter=new DeviceListAdapter(getApplicationContext(), R.layout.device_adapter_view,btArray );
            pairedDevicesList.setAdapter(adapter);
        }
        mainLayout.animate().translationX(-800).setDuration(500);
        pairedDevicesList.animate().translationX(0).setDuration(500);
    }
}

