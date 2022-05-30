package ro.pub.cs.systems.pdsd.practicaltest02;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientAsyncTask extends AsyncTask<String, String, Void> {
    private TextView clientInfoTextView;

    public ClientAsyncTask(TextView clientInfoTextView) {
        this.clientInfoTextView = clientInfoTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            String serverAddress = params[0];
            String serverPort = params[1];
            String word = params[2];

            Socket socket = new Socket(serverAddress, Integer.parseInt(serverPort));

            Log.d(Constants.TAG, "[CLIENTASYNCTASK] word = " + word);

            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(word);
            printWriter.flush();

            BufferedReader bufferedReader = Utilities.getReader(socket);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                publishProgress(line);
            }
            socket.close();
        } catch (Exception exception) {
            Log.e(Constants.TAG, "[CLIENTASYNCTASK] An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // - reset the content of the serverMessageTextView
        clientInfoTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        // - append the content to serverMessageTextView
        clientInfoTextView.append(progress[0] + "\n");
    }

    @Override
    protected void onPostExecute(Void result) {}
}
