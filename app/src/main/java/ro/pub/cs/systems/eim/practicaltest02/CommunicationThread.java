package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread extends Thread {

    private Socket socket;
    private TextView serverTextEditText;

    public CommunicationThread(Socket socket, TextView serverTextEditText) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
    }

    @Override
    public void run() {
        try {
            Log.v("TAGAPLICATIE", "Connection opened to " + socket.getLocalAddress() + ":" + socket.getLocalPort()+ " from " + socket.getInetAddress());
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(serverTextEditText.getText().toString());
            socket.close();
            Log.v("TAGAPLICATIE", "Connection closed");
        } catch (IOException ioException) {
            Log.e("TAGAPLICATIE", "An exception has occurred: " + ioException.getMessage());

        }
    }
}
