package com.danielqueiroz.bluetoothchat;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BluetoothCheckActivity extends AppCompatActivity {

    protected BluetoothAdapter btfAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_check);

        btfAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btfAdapter == null ){
            Toast.makeText(this,"Bluetooth indisponivel",
                    Toast.LENGTH_LONG).show();
            finish();
        }

        if (btfAdapter.isEnabled()){
            Toast.makeText(this,"Bluetooth Ligado ;) ",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (btfAdapter.isEnabled()){
            Toast.makeText(this,"Bluetooth foi Ligado ;) ",
                    Toast.LENGTH_LONG).show();
        }

    }
}
