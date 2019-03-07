package com.danielqueiroz.bluetoothchat;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

public class BluetoothChatClientActivity extends BluetoothCheckActivity implements ChatController.ChatListener{

    protected final UUID uuid = UUID.fromString("baad2da0-e0a0-4f74-aadc-db46e95f1a04");
    protected BluetoothDevice device;
    protected ChatController chat;
    protected EditText editMsg;
    protected TextView textMsgs;
    protected Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_chat_client);

        btnEnviar = (Button) findViewById(R.id.enviar);
        textMsgs = (TextView) findViewById(R.id.textMsgs);
        editMsg = (EditText) findViewById(R.id.editMsg);

        device = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        try{
            if (device != null){
                getSupportActionBar().setTitle("Conectado: "+ device.getName());
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();

                chat = new ChatController(socket, this);
                chat.start();

                btnEnviar.setEnabled(true);
            }

        } catch (Exception e){}

    }

    public void onClickEnviarMsg(View v){
        try{
            String msgEnviada = editMsg.getText().toString();
            chat.sendMessage(msgEnviada);
            editMsg.setText("");
            String txt = textMsgs.getText().toString();
            textMsgs.setText(txt+ "\nenviado: " +msgEnviada);
        } catch (Exception e){}

    }

    @Override
    public void onMessageReceived(final String msg) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        String txt = textMsgs.getText().toString();
                        textMsgs.setText(txt+ "\nrecebido: " + msg);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (chat != null){
            chat.stop();
        }

    }
}
