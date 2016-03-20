package com.example.laurynas.authenti;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.authenti.intservice.extra.USER";
    public final static String EXTRA_PASS = "com.authenti.intservice.extra.PASS";
    private String token = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //        fab.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                        .setAction("Action", null).show();
        //            }
        //        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void postMessage(String email, String password) {

        if (email.equals("") || password.equals("")) {
            return;
        }
        RequestParams params = new RequestParams();

        // set our JSON object

        params.put("email", email);
        params.put("password", password);
//            params.setForceMultipartEntityContentType(true);


        AsyncHttpClient client = new AsyncHttpClient();

        final Intent tent = new Intent(this, SendDetails.class);


        client.post("https://noauth.herokuapp.com/api/login", params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {


                    String username = response.getString("email");
                    String name = response.getString("name");

                    if (getToken().equals(" ")) {
                        String token = response.getString("token");
                        int mode = Activity.MODE_PRIVATE;
                        SharedPreferences mySharedPreferences;
                        mySharedPreferences = getSharedPreferences("NoAuth", mode);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("Token", token);
                        editor.commit();
                    } else {
                        token = getToken();
                    }

                    tent.putExtra(EXTRA_MESSAGE, username);
                    tent.putExtra(EXTRA_PASS, name);

                    //  Log.d("Response: ", username+name);
                    //  startService(tent);

                } catch (Exception e) {

                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("fail: ", errorResponse.toString());
            }
        });
    }

    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.Text1);
        String email = editText.getText().toString();
        EditText pass = (EditText) findViewById(R.id.editText);
        String password = pass.getText().toString();
        postMessage(email, password);




    }

    public String getToken() {
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences;
        mySharedPreferences = getSharedPreferences("NoAuth", mode);


        String mString;

        mString = mySharedPreferences.getString("Token", " ");

        return mString;
    }
    // MyResponseHandler h = new MyResponseHandler();
    //class MyResponseHandler extends
}


//  Intent intent = new Intent(this, DisplayMessageActivity.class);
// Intent tent = new Intent(this, SendDetails.class);
//   tent.setAction("com.example.laurynas.authenti.action.FOO");

//  tent.putExtra(EXTRA_MESSAGE, email);

//   tent.putExtra(EXTRA_PASS, password);
//  startService(tent);
//        startActivity(intent);