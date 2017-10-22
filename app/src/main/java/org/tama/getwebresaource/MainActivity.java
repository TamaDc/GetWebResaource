package org.tama.getwebresaource;


import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    ///DECLARASI
    TextView FinalySource;
    Spinner spin;
    EditText URL;
    ArrayAdapter<CharSequence> GetSpinner;
    ProgressBar LoadURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin=(Spinner)findViewById(R.id.SetSpinner);
        URL=(EditText)findViewById(R.id.SetURL);
        FinalySource=(TextView)findViewById(R.id.SourceCode);
        LoadURL=(ProgressBar)findViewById(R.id.ProgressBar);

        GetSpinner=ArrayAdapter.createFromResource(this,R.array.model,android.R.layout.simple_spinner_item);
        GetSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(GetSpinner);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread paranThread, Throwable paranThrowable) {
                LoadURL.setVisibility(View.GONE);
                Log.e("Error URL" + Thread.currentThread().getStackTrace()[2],paranThrowable.getLocalizedMessage());
            }
        });
        if (getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Really exit").setMessage("Are you sure ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();
            }
        })
                .setNegativeButton("No",null);
        AlertDialog alert=builder.create();
        alert.show();
    }
    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new GetSource(this,args.getString("url_link"));
    }
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        LoadURL.setVisibility(View.GONE);
        FinalySource.setText(data);

    }
    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    public void getSourceCode(View view) {
        String link_url, protokol, url;
        protokol=spin.getSelectedItem().toString();
        url=URL.getText().toString();

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),inputMethodManager.HIDE_NOT_ALWAYS);

        if(!url.isEmpty()){
            if(url.contains(".")){
                if(CekConnect()){
                    FinalySource.setText("");
                    LoadURL.setVisibility(view.VISIBLE);
                    link_url=protokol+url;

                    Bundle bundle =new Bundle();
                    bundle.putString("url_link",link_url);
                    getSupportLoaderManager().restartLoader(0,bundle,this);
                }
                else{
                    Toast.makeText(this,"Check Connection Internet Please",Toast.LENGTH_SHORT).show();
                    FinalySource.setText("No Connection Internet");
                }
            }else{
                FinalySource.setText("valid URL");
            }
        }
        else{
            FinalySource.setText("URL Empty");
        }
    }

    // Cek Koneksi Pada Internet ada atau Tidak
    boolean CekConnect(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null &&networkInfo.isConnected();
    }

}