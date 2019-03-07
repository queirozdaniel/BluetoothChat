package com.danielqueiroz.bluetoothchat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.danielqueiroz.bluetoothchat.ChatController.ChatListener;

import java.util.UUID;

public class BluetoothChatServidorActivity extends BluetoothChatClientActivity implements ChatController.ChatListener{

    protected final UUID uuid = UUID.fromString("baad2da0-e0a0-4f74-aadc-db46e95f1a04");
    protected BluetoothServerSocket serverSocket;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ChatThread().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null){
                serverSocket.close();
            }
        } catch (Exception e){

        }

    }

    class ChatThread extends Thread {
        @Override
        public void run(){
            super.run();

            try {
                serverSocket = btfAdapter.listenUsingInsecureRfcommWithServiceRecord("Icora", uuid);
                BluetoothSocket socket = serverSocket.accept();

                if (socket != null){
                    final BluetoothDevice device = socket.getRemoteDevice();
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    getSupportActionBar().setTitle("Conectado: "+ device.getName());
                                    btnEnviar.setEnabled(true);
                                    Toast.makeText(getBaseContext(), "Conectou "+ device.getName(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );

                    chat = new ChatController(socket, BluetoothChatServidorActivity.this);
                }

            } catch (Exception e){
                running = false;
            }

        }
    }



}
