package com.example.areebacodechallenge;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class BroadcastReceivers {

    private static final String TAG = "kkkkkkkkkkkkkkkk";

    //For btnOnOff
    static final BroadcastReceiver BroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(MainActivity.mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, MainActivity.mBluetoothAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.i(TAG, "onReceive: STATE OFF");

                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.i(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.i(TAG, "mBroadcastReceiver1: STATE ON");

                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.i(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

     //For Discover
    static final BroadcastReceiver BroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.i(TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.i(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.i(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.i(TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.i(TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };


    static BroadcastReceiver BroadcastReceiver3 = new BroadcastReceiver() {
        boolean founded=false;
        @Override
        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();
            Log.i(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                for(int i=0;i<MainActivity.BTDevices.size();i++){
                    if(MainActivity.BTDevices.get(i)==device){
                        founded=true;
                    }
                }
                if(!founded) {
                    MainActivity.BTDevices.add(device);
                }
                Log.i(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                MainActivity.DeviceListAdapter.notifyDataSetChanged();
                MainActivity.NewDevices.setAdapter(MainActivity.DeviceListAdapter);
            }
        }
    };

    static BroadcastReceiver BroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //Bonded Already
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    Log.i(TAG,"BOND_BOUNDED");
                }

                //creating a bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING){
                    Log.i(TAG,"BOND_BOUNDING");
                }

                //breaking a bond
                if(mDevice.getBondState() == BluetoothDevice.BOND_NONE){
                    Log.i(TAG,"BOND_NONE");
                }

            }
        }
    };
}
