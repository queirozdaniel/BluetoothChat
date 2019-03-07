package com.danielqueiroz.bluetoothchat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaDispositivosActivity extends BluetoothCheckActivity {

    private ListView listViewDispositivos;
    private List<BluetoothDevice> listaDispositivos;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dispositivos);

        listViewDispositivos = (ListView) findViewById(R.id.dispositivos);

        if (btfAdapter != null){
            listaDispositivos = new ArrayList<>(btfAdapter.getBondedDevices());

            this.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
            this.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            this.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (btfAdapter != null){
            if (btfAdapter.isDiscovering()){
                btfAdapter.cancelDiscovery();
            }

            btfAdapter.startDiscovery();
            progressBar = (ProgressBar) findViewById(R.id.progresso);
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        private int count;

        @Override
        public void onReceive(Context context, Intent intent) {
            String acao = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(acao)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.w("FOUND", "Primeiro ...");

                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                    listaDispositivos.add(device);
                    Toast.makeText(context, "Encontrou! + " + device.getName(), Toast.LENGTH_LONG).show();
                    count++;
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(acao)){
                count = 0;
                Log.w("FOUND", "Segundo ...");
                Toast.makeText(context, "Busca iniciada", Toast.LENGTH_LONG).show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(acao)){
                Log.w("FOUND", "Terceiro ...");

                Toast.makeText(context, "Busca Finalizada! " +count + " dispositivos encontrados", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(ProgressBar.GONE);
                atualizarLista();
            }
        }
    };

    private void atualizarLista(){
        List<String> nomes = new ArrayList<>();
        for (BluetoothDevice device : listaDispositivos){
            boolean pareado = device.getBondState() == BluetoothDevice.BOND_BONDED;
            nomes.add(device.getName() +" - "+ device.getAddress() + (pareado ? " - pareado" : " - novo"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, nomes);
        listViewDispositivos.setAdapter(adapter);
        listViewDispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = listaDispositivos.get(position);
                String msg = device.getName()+" - " + device.getAddress();
                Toast.makeText(ListaDispositivosActivity.this, msg, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ListaDispositivosActivity.this, BluetoothChatClientActivity.class);
                intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (btfAdapter != null){
            btfAdapter.cancelDiscovery();
            this.unregisterReceiver(receiver);
        }
    }

}
