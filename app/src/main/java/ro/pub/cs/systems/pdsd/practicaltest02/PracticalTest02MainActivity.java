package ro.pub.cs.systems.pdsd.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PracticalTest02MainActivity extends AppCompatActivity {


    private ServerThread serverThread;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            EditText serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
            int serverPort = Integer.parseInt(serverPortEditText.getText().toString());
            serverThread = new ServerThread(serverPort);
            serverThread.startServer();
        }
    }
    private ClientButtonClickListener clientButtonClickListener = new ClientButtonClickListener();
    private class ClientButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            TextView clientInfoTextView = (TextView)findViewById(R.id.client_info_text_view);
            EditText clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
            EditText clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
            EditText clientWordEditText = (EditText)findViewById(R.id.client_word_edit_text);

            ClientAsyncTask clientAsyncTask = new ClientAsyncTask(clientInfoTextView);
            clientAsyncTask.execute(
                    clientAddressEditText.getText().toString(),
                    clientPortEditText.getText().toString(),
                    clientWordEditText.getText().toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        EditText serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        serverPortEditText.setText(String.format("%d", Constants.SERVER_PORT));

        EditText clientAddressEditText = (EditText)findViewById(R.id.client_address_edit_text);
        clientAddressEditText.setText("localhost");

        EditText clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        clientPortEditText.setText(String.format("%d", Constants.SERVER_PORT));

        EditText clientWordEditText = (EditText)findViewById(R.id.client_word_edit_text);
        clientWordEditText.setText("hello");

        Button serverConnectButton = (Button)findViewById(R.id.server_connect_button);
        serverConnectButton.setOnClickListener(connectButtonClickListener);

        Button searchDefinitionButton = (Button)findViewById(R.id.search_definition_button);
        searchDefinitionButton.setOnClickListener(clientButtonClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}