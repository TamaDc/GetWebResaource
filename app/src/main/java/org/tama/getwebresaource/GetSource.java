package org.tama.getwebresaource;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Asus A455L on 22/10/2017.
 */

public class GetSource extends AsyncTaskLoader<String> {

    private String URL;

    public GetSource(Context context, String URL_Link) {
        super(context);
        this.URL=URL_Link;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        InputStream inputStream;

        try{
            URL url=new URL(URL);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            inputStream=connection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder=new StringBuilder();
            String line="";

            while((line=bufferedReader.readLine()) !=null){
                stringBuilder.append(line +"\n");
            }

            bufferedReader.close();
            inputStream.close();
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Errors";
    }
}
