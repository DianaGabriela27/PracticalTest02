package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;

    private TextView serverTextEditText;

    private HashMap<InetAddress, Boolean> hashMap = new HashMap<>();

    public ServerThread(TextView serverTextEditText) {
        this.serverTextEditText = serverTextEditText;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v("TAGAPLICATIE", "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e("TAGAPLICATIE", "An exception has occurred: " + ioException.getMessage());
        }
        Log.v("TAGAPLICATIE", "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(13, 50, InetAddress.getByName("utcnist.colorado.edu"));
            while (isRunning) {
                Socket socket = serverSocket.accept();
                Log.v("TAGAPLICATIE", "accept()-ed: " + socket.getInetAddress());
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket, serverTextEditText);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e("TAGAPLICATIE", "An exception has occurred: " + ioException.getMessage());

        }
    }
}
