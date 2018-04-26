package com.spbpu.hackaton.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static String SERVER_IP = "192.168.3.234";
    private static final int SERVER_PORT = 27015;

    String message = new String("");

    EditText editMessage;
    EditText editIP;
    Button btnSendMessage;
    Button btnChangeIP;
    Button btnConnect;
    TextView textMsgFromServer;

    private Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMessage = findViewById(R.id.editMessage);
        editIP = findViewById(R.id.editServerIP);

        btnSendMessage = findViewById(R.id.btnSendMessage);
        btnChangeIP = findViewById(R.id.btnChangeServerIP);
        btnConnect = findViewById(R.id.btnConnectToServer);

        btnSendMessage.setEnabled(false);

        textMsgFromServer = findViewById(R.id.txtMessage);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (socket == null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            openConnection();
                        }
                    }).start();
                }
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = editMessage.getText().toString();
                editMessage.setText("");

                try {
                    sentData(message.getBytes());
                } catch (Exception e) {
                    textMsgFromServer.setText("Sending message error");
                }
            }
        });

        btnChangeIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeConnection();
                SERVER_IP = editIP.getText().toString();
                editIP.setText("");
                socket = null;
            }
        });
    }

    public void openConnection() {
        closeConnection();

        try {
            InetAddress serverAdr = InetAddress.getByName(SERVER_IP);
            socket = new Socket(serverAdr, SERVER_PORT);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    btnSendMessage.setEnabled(true);
                }
            });

        } catch (UnknownHostException e) {
            Log.d("Dasha", e.getMessage());
        } catch (IOException e) {
            Log.d("Dasha", e.getMessage());
        }
    }

    public void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnSendMessage.setEnabled(false);
                    }
                });

            } catch (Exception e) {
                textMsgFromServer.setText("Unable close socket");
            }
        }
    }

    public void sentData(byte[] data) {
        if (socket == null || socket.isClosed()) {
            return;
        }

        try {
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            textMsgFromServer.setText("Unable to sent message (IOException)");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }
}
