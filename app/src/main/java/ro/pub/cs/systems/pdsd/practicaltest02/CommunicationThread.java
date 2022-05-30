package ro.pub.cs.systems.pdsd.practicaltest02;

import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class CommunicationThread extends Thread {
    private Socket socket;
    private ServerThread serverThread;

    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "[COMMUNICATION THREAD] Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            BufferedReader bufferedReader = Utilities.getReader(socket);

            String word = bufferedReader.readLine();

            Log.d(Constants.TAG, "[COMMUNICATION THREAD] word = " + word);

            if (word == null || word.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
            HttpClient httpClient = new DefaultHttpClient();

            Log.d(Constants.TAG, Constants.WEB_SERVICE_ADDRESS + "/" + word);
            HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + "/" + word);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String pageSource = httpClient.execute(httpGet, responseHandler);
            if (pageSource == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                return;
            }
            JSONArray content = new JSONArray(pageSource);
            Log.d(Constants.TAG, content.toString());
            JSONObject meanings = content.getJSONObject(0);
            Log.d(Constants.TAG, meanings.toString());
            JSONArray meaning = meanings.getJSONArray("meanings");
            JSONObject tmp = meaning.getJSONObject(0);
            JSONObject tmp2 = tmp.getJSONArray("definitions").getJSONObject(0);
            String result = tmp2.getString("definition");

            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(result);
            printWriter.flush();
            Log.d(Constants.TAG, "[COMMUNICATION THREAD] Result sent to client: " + result);
            socket.close();
            Log.v(Constants.TAG, "[COMMUNICATION THREAD] Connection closed");
        } catch (Exception exception) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + exception.getMessage());
            if (Constants.DEBUG) {
                exception.printStackTrace();
            }
        }
    }
}
