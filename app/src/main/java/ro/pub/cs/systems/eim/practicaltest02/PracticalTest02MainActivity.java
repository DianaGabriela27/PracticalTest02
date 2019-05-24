package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText editTextOra;
    EditText editTextMinute;
    EditText editTextPort;

    Button setButton;
    Button resetButton;
    Button pollButton;

    TextView textViewServerResponse;

    private ServerThread serverThread;

    private HashMap<String, String> hashMap = new HashMap<>();

    private ResetButtonClickListener resetButtonClickListener = new ResetButtonClickListener();

    private class ResetButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            hashMap.remove(editTextOra.getText().toString());
            Log.d("DSADSADAS", "Alarm removed: ");
        }
    }

    private PollButtonClickListener pollButtonClickListener = new PollButtonClickListener();

    private class NISTCommunicationAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String dayTimeProtocol = null;
            try {
                Socket socket = new Socket("time.nist.gov", 13);
                BufferedReader bufferedReader = Utilities.getReader(socket);
                bufferedReader.readLine();
                dayTimeProtocol = bufferedReader.readLine();
                Log.d("DSADSADAS", "The server returned: " + dayTimeProtocol);
            } catch (UnknownHostException unknownHostException) {
                Log.d("DSADSADAS", unknownHostException.getMessage());

            } catch (IOException ioException) {
                Log.d("DSADSADAS", ioException.getMessage());
            }
            return dayTimeProtocol;
        }

        @Override
        protected void onPostExecute(String result) {

            String[] temp = result.split(" ");
            Log.d("DSADSADAS", "The server returned: " + temp[2]);
            String[] temp2 = temp[2].split(":");

            Log.d("DSADSADAS", "The server returned hour " + temp2[0]);
            Log.d("DSADSADAS", "The server returned minutes " + temp2[1]);

            if(Integer.parseInt(editTextOra.getText().toString()) < Integer.parseInt(temp2[0]))
                Log.d("DSADSADAS", "EXPIRED ALARM");

            //daytimeProtocolTextView.setText(result);
        }
    }

    private SetButtonClickListener setButtonClickListener = new SetButtonClickListener();
    private class SetButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.d("DSADSADAS", "Alarm added ");
            hashMap.put(editTextOra.getText().toString(), editTextMinute.getText().toString());
        }
    }

    private class PollButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            NISTCommunicationAsyncTask nistCommunicationAsyncTask = new NISTCommunicationAsyncTask();
            nistCommunicationAsyncTask.execute();

        }
    }




        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_practical_test02_main);

            editTextOra = (EditText) findViewById(R.id.editTextOra);
            editTextMinute = (EditText) findViewById(R.id.editTextMinute);
            editTextPort = (EditText) findViewById(R.id.editTextPort);

            setButton = (Button) findViewById(R.id.buttonSet);
            resetButton = (Button) findViewById(R.id.buttonReset);
            pollButton = (Button) findViewById(R.id.buttonPoll);

            textViewServerResponse = (TextView) findViewById(R.id.textViewServerResponse);

            setButtonClickListeners();

            //serverAction.addTextChangedListener(serverTextContentWatcher);

        }

        private void setButtonClickListeners() {

            setButton.setOnClickListener(setButtonClickListener);
            resetButton.setOnClickListener(resetButtonClickListener);
            pollButton.setOnClickListener(pollButtonClickListener);
        }
    }


