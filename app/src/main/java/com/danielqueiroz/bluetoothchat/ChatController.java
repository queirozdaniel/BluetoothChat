package com.danielqueiroz.bluetoothchat;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChatController {

    private BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private ChatListener listener;
    private  boolean running;

    public interface ChatListener{
        public void onMessageReceived(String msg);
    }

    public ChatController(BluetoothSocket socket, ChatListener listener ) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.listener = listener;
        this.running = true;
    }

    public void start(){
        new Thread() {
            @Override
            public void run(){
                super.run();

                running = true;

                byte[] bytes = new byte[1024];
                int tamanho;

                while (running){
                    try{
                        tamanho = inputStream.read(bytes);
                        String msg = new String(bytes, 0, tamanho);
                        listener.onMessageReceived(msg);
                    }catch (Exception e){

                    }
                }
            }
        }.start();
    }

    public void sendMessage(String msg) throws  IOException{
        if (outputStream != null){
            outputStream.write(msg.getBytes());
        }
    }

    public void stop(){
        running = false;
        try {
            if (socket != null){
                socket.close();
            }
            if (inputStream != null){
                inputStream.close();
            }
            if (outputStream != null){
                outputStream.close();
            }

        }catch (Exception e){}
    }

}
