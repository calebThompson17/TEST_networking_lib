package com.example.mylibrary;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author lynn
 *
 */
public class DownloadTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "DownloadTask";
    private static final int BUFFER_SIZE = 8096;
//    MainActivity myActivity;

    // 1 second
    private static final int TIMEOUT = 1000;
    private String myQuery = "";

    protected DownloadTask() {

    }

    //
    /**
     * @param name
     * @param value
     * @return this allows you to build a safe URL with all spaces and illegal
     *         characters URLEncoded usage mytask.setnameValuePair("param1",
     *         "value1").setnameValuePair("param2",
     *         "value2").setnameValuePair("param3", "value3")....
     */
    public DownloadTask setnameValuePair(String name, String value) {
        try {
            if (name.length() != 0 && value.length() != 0) {

                // if 1st pair that include ? otherwise use the joiner char &
                if (myQuery.length() == 0)
                    myQuery += "?";
                else
                    myQuery += "&";

                myQuery += name + "=" + URLEncoder.encode(value, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    @Override
    protected String doInBackground(String... params) {
        // site we want to connect to
        String myURL = params[0];

        try {
            URL url = new URL(myURL + myQuery);

            // this does no network IO
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // can further configure connection before getting data
            // cannot do this after connected
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            // this opens a connection, then sends GET & headers

            // wrap in finally so that stream bis is sure to close
            // and we disconnect the HttpURLConnection
            BufferedReader in = null;
            try {
                connection.connect();

                // lets see what we got make sure its one of
                // the 200 codes (there can be 100 of them
                // http_status / 100 != 2 does integer div any 200 code will = 2
                int statusCode = connection.getResponseCode();
                if (statusCode / 100 != 2) {
                    Log.e(TAG, "Error-connection.getResponseCode returned "
                            + Integer.toString(statusCode));
                    return null;
                }

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()), 8096);

                // the following buffer will grow as needed
                String myData;
                StringBuffer sb = new StringBuffer();

                while ((myData = in.readLine()) != null) {
                    sb.append(myData);
                }
                return sb.toString();

            } finally {
                // close resource no matter what exception occurs
                in.close();
                connection.disconnect();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {

    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onCancelled(java.lang.Object)
     */
    @Override
    protected void onCancelled() {
        // TODO Auto-generated method stub
        super.onCancelled();
    }



    // void doPost() throws MalformedURLException, IOException
    // {
    //
    // String url = "http://example.com";
    // String charset = "UTF-8";
    // String param1 = "value1";
    // String param2 = "value2";
    // // ...
    // //URL encode the name value pairs to send
    // String query = String.format("param1=%s&param2=%s",
    // URLEncoder.encode(param1, charset), URLEncoder.encode(param2, charset));
    //
    // //open connection to "http://example.com
    // HttpURLConnection httpConnection = (HttpURLConnection) new
    // URL(url).openConnection();
    //
    // //tell it we want to POST data
    // httpConnection.setRequestMethod("POST");
    // //:
    // //we will POST query string to server using this stream
    // //note the & seperator but we do not need ?
    // OutputStream output = null;
    // try {
    // output = httpConnection.getOutputStream();
    // output.write(query.getBytes(charset));
    // } finally {
    // //close output stream
    // }
    // //see what server has to say about our query
    // InputStream response = httpConnection.getInputStream();
    // }

    protected String demo_class() {
        try {
            URL url = new URL("http://www.tetonsoftware.com/pets/pets.json");

            // this does no network IO
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // can further configure connection before getting data
            // cannot do this after connected
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);

            BufferedReader in = null;
            try {
                // this opens a connection, then sends GET & headers
                connection.connect();

                // 200 codes mean it went well, otherwise fail
                int statusCode = connection.getResponseCode();
                if (statusCode >200 && statusCode <300) {
                    Log.e(TAG, "Error-connection.getResponseCode returned "
                            + Integer.toString(statusCode));
                    return null;
                }

                in = new BufferedReader(new InputStreamReader(connection.getInputStream()), BUFFER_SIZE);

                // the following buffer will grow as needed
                String myData;
                StringBuffer sb = new StringBuffer();

                while ((myData = in.readLine()) != null) {
                    sb.append(myData);
                }
                return sb.toString();   //THIS IS THE HTML FROM THE PAGE

            } finally {
                // close resource no matter what exception occurs
                in.close();
                connection.disconnect();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }
};

