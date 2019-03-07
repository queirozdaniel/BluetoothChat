package com.danielqueiroz.bluetoothchat;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaPareadosActivity extends BluetoothCheckActivity {

    private List<BluetoothDevice> listaPareados;
    private ListView listViewPareados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pareados);

        listViewPareados = (ListView) findViewById(R.id.list_vew_pareados);

    }

    @Override
    protected void onResume() {
        super.onResume();

        listaPareados = new ArrayList<BluetoothDevice>(btfAdapter.getBondedDevices());
        List<String> nomes = new ArrayList<>();

        for (BluetoothDevice d : listaPareados){
            nomes.add(d.getName()+" - " + d.getAddress());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, nomes);

        listViewPareados.setAdapter(arrayAdapter);

        listViewPareados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = listaPareados.get(position);
                String msg = device.getName()+" - " + device.getAddress();
                Toast.makeText(ListaPareadosActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });

    }


}
