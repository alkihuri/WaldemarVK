package com.example.alkihuri.waldemarvk;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.*;

import java.net.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.widget.EditText;

public class auth extends AppCompatActivity {
    String token , user_id;
    public void  ZapRos (String url)
    {
        new MyTask().execute(url).toString();


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        WebView wb = (WebView) findViewById(R.id.web);
        WebSettings webSettings = wb.getSettings();
        webSettings.setJavaScriptEnabled(true);
        SimpleWebViewClient webViewClient = new SimpleWebViewClient();
        wb.setWebViewClient(webViewClient);
        wb.loadUrl("https://oauth.vk.com/authorize?client_id=5213947&redirect_uri=oauth.vk.com/blank.html&display=mobile&scope=4098&&response_type=token&v=5.58");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebView wb = (WebView) findViewById(R.id.web);
                token = wb.getUrl().toString().split("=")[1].toString().split("&")[0];
                Snackbar.make(view, "Получаю токен...", Snackbar.LENGTH_LONG).setAction("Action", null).show();


                wb.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auth, menu);
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

    public void selectFriendclick(MenuItem item) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Выбор друга");
        alert.setMessage("Введите ID");

// Set an EditText view to get user input
         final EditText input = new EditText(auth.this);
        alert.setView(input);
        input.setText("242907119");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Editable value = input.getText();
                user_id = value.toString();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    public void sendMsgClick(MenuItem item) {
        ///ОТПРАВКА СООБЩЕНИЯ

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Отпрвка сообщения ");
        alert.setMessage("Введите сообщение ");

// Set an EditText view to get user input
        final EditText input = new EditText(this);

        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Editable value = input.getText();
                ZapRos("https://api.vk.com/method/messages.send?user_id="+ user_id +"&message=" +  value.toString()   + "&access_token="+ token + "&v=5.59");
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

    public void LastChatClick(MenuItem item) {
        /// выбор id по последнему чату 
    }

    public class SimpleWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }


    //// асинктаск
    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(params[0]).openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            ////парсинг
            AlertDialog.Builder builder = new AlertDialog.Builder(auth.this);
            builder.setTitle("Запрос отправлен")
                    .setMessage(result)

                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}


