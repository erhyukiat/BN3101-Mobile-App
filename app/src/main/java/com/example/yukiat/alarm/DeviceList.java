package com.example.yukiat.alarm;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends AppCompatActivity {

    ListView deviceList;
    Button getDeviceList;

    private BluetoothAdapter myBluetooth = null;
    Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        Toast.makeText(getApplicationContext(), "Click on the Paired Devices button at the lower " +
                "left corner to retrieve device list", Toast.LENGTH_LONG).show();

        deviceList = (ListView)findViewById(R.id.deviceList);
        getDeviceList = (Button)findViewById(R.id.getDeviceList);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if(myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available",
                    Toast.LENGTH_LONG).show();
            finish();
        } else if(!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        getDeviceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });
    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size() > 0) {
            for(BluetoothDevice bt : pairedDevices) {
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No paired bluetooth devices found",
                    Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        //Method called when the device from the list is clicked.
        deviceList.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.
            OnItemClickListener() {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3) {
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent i = new Intent(DeviceList.this, SyncAlarm.class);

            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };

    public void toMain(View view) {
        Intent i = new Intent(DeviceList.this, MainActivity.class);
        startActivity(i);
    }
}
