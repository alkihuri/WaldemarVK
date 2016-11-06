package com.example.alkihuri.waldemarvk;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class auth extends AppCompatActivity {


    String token, user_id, user_id_name , key   ;
    String[] friends;




        public static String xor_encrypt(String message, String key){
            try {
                if (message==null || key==null ) return null;

                char[] keys=key.toCharArray();
                char[] mesg=message.toCharArray();
                BASE64Encoder encoder = new BASE64Encoder();

                int ml=mesg.length;
                int kl=keys.length;
                char[] newmsg=new char[ml];

                for (int i=0; i<ml; i++){
                    newmsg[i]=(char)(mesg[i]^keys[i%kl]);
                }
                mesg=null;
                keys=null;
                String temp = new String(newmsg);
                return new String(new BASE64Encoder().encodeBuffer(temp.getBytes()));
            }
            catch ( Exception e ) {
                return null;
            }
        }


        public static String xor_decrypt(String message, String key){
            try {
                if (message==null || key==null ) return null;
                BASE64Decoder decoder = new BASE64Decoder();
                char[] keys=key.toCharArray();
                message = new String(decoder.decodeBuffer(message));
                char[] mesg=message.toCharArray();

                int ml=mesg.length;
                int kl=keys.length;
                char[] newmsg=new char[ml];

                for (int i=0; i<ml; i++){
                    newmsg[i]=(char)(mesg[i]^keys[i%kl]);
                }
                mesg=null; keys=null;
                return new String(newmsg);
            }
            catch ( Exception e ) {
                return null;
            }
        }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private GoogleApiClient client;


        public String crypt(String text, String keyWord)
        {
            try {
                return URLEncoder.encode(xor_encrypt(text,keyWord), "UTF-8") ;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return text;
        }

        public String decrypt(String  crtext, String keyWord)
        {
             return xor_decrypt(crtext,keyWord);
            //return crtext;
        }


    public void GetHistory(String url) {
        new GetHistory().execute(url);
    }

    public void SendMessage(String url) {
        new SendMessage().execute(url);
    }

    public void GetFriend(String url) {
        new GetFriend().execute(url);
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    public void byidclick(MenuItem item) {
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
        GetHistory("https://api.vk.com/method/messages.getHistory?offset=0&count=5&user_id=" + user_id + "&access_token=" + token + "&v=5.59");
        // alert.show();
    }

    public void selFclick(MenuItem item) {


        GetFriend("https://api.vk.com/method/friends.get?offset=0&order=hints&fields=city&access_token=" + token + "&v=5.59");


    }

    public void getkeyclick(MenuItem item) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Получение ключа");
        alert.setMessage("Введите  ключ ");
// Set an EditText view to get user input
        final EditText input = new EditText(auth.this);
        alert.setView(input);
        alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Editable value = input.getText();
                key = value.toString();
                // Do something with value!o
            }
        });

        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("auth Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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

    class SendMessage extends AsyncTask<String, Void, String> {

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
            GetHistory("https://api.vk.com/method/messages.getHistory?offset=0&count=5&user_id=" + user_id + "&access_token=" + token + "&v=5.59");
        }
    }

    //// асинктаск
    class GetHistory extends AsyncTask<String, Void, String> {

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


            String messegesCount = null;
            String posts = "";
            super.onPostExecute(result);
            ////парсинг
            try {

                JSONObject obj = new JSONObject(result);
                messegesCount = obj.getJSONObject("response").getString("count");
                JSONArray arr = obj.getJSONObject("response").getJSONArray("items");

                for (int i = 0; i < arr.length(); i++) {
                    String msg = arr.getJSONObject(i).getString("body");
                    if (msg.contains(":CRMSGWLDMR:")) {

                        msg = decrypt(msg.replace(":CRMSGWLDMR:", "") , key).replace("+"," ");

                    }

                    // give a timezone reference for formating (see comment at the bottom
                    if (arr.getJSONObject(i).getString("from_id").equals(user_id))
                        posts += "Собеседник:" + msg + "\n";
                    else
                        posts += "Я:" + msg + "\n";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (result.contains("count")) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(auth.this);
                if (!user_id_name.equals(null))
                    builder.setTitle("Историялда с " + user_id_name);
                else
                    builder.setTitle("Историялда с " + user_id);
                builder.setMessage(
                        "Всего сообщений" + messegesCount + "\n" + posts
                );
                // Set an EditText view to get user input
                final EditText input = new EditText(auth.this);
                builder.setView(input);
                builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        /////шифрование
                        Editable value = input.getText();
                        try {
                            String s = URLEncoder.encode((String.valueOf(value)), "UTF-8");
                            SendMessage("https://api.vk.com/method/messages.send?user_id=" + user_id + "&message=:CRMSGWLDMR:" + crypt(s, key) + "&access_token=" + token + "&v=5.59");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                // alert.cancel();
            }


        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 55:

                //final String[] mCatsName ={"Васька", "Рыжик", "Мурзик"};

                AlertDialog.Builder builder = new AlertDialog.Builder(auth.this);
                builder.setTitle("Выбираем друга"); // заголовок для диалога

                builder.setItems(friends, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),
                                "Выбранный друг: " + friends[item],
                                Toast.LENGTH_SHORT).show();
                        user_id = friends[item].split("id")[1].toString().split(":")[0].toString();
                        user_id_name = friends[item].split("\n")[1].toString().split("<<")[0].toString();

                    }
                });
                builder.setCancelable(false);
                return builder.create();

            default:
                return null;
        }
    }

    class GetFriend extends AsyncTask<String, Void, String> {

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
            String friendsCount = null;
            String posts = "";


            super.onPostExecute(result);

            try {
                int i;
                JSONObject obj = new JSONObject(result);
                friendsCount = obj.getJSONObject("response").getString("count");
                JSONArray arr = obj.getJSONObject("response").getJSONArray("items");
                friends = new String[Integer.parseInt(friendsCount)];
                for (i = 0; i < arr.length(); i++) {
                    friends[i] =
                            "id" + arr.getJSONObject(i).getString("id")
                                    + ":" + '\n' + arr.getJSONObject(i).getString("first_name")
                                    + " " + arr.getJSONObject(i).getString("last_name")
                                    + " <<" + arr.getJSONObject(i).getString("online") + ">>";
                    friends[i] = friends[i].replace("<<1>>", " online");
                    friends[i] = friends[i].replace("<<0>>", " offline");

                }


                showDialog(55);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


}