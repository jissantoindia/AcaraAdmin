package in.acara.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class MnumActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String mobilenum,acaraid,usemail,uscollege,usname;
    Button btn;
    EditText editmobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnum);
        getSupportActionBar().hide();
        btn = findViewById(R.id.button);
        editmobile = findViewById(R.id.Mobile);
        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
        mobilenum=sharedPreferences.getString("mobnum","");
        acaraid=sharedPreferences.getString("acaraid","");
        usemail=sharedPreferences.getString("usemail","");
        uscollege=sharedPreferences.getString("uscollege","");
        usname=sharedPreferences.getString("usname","");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setText("Fetching");
                btn.setEnabled(false);
                editmobile.setEnabled(false);
                new SendPostRequest().execute();
                findViewById(R.id.progresslay1).setVisibility(View.VISIBLE);
            }
        });


    }


    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){


        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL(getString(R.string.UserHandler)); // here is your URL path
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("mobile", editmobile.getText().toString());
                postDataParams.put("action", "getUser");



                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(10000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                Log.v("backgroud",e.toString());

                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(MnumActivity.this, result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject jsn=new JSONObject(result);

                String name=jsn.getString("name");
                String mobile=jsn.getString("mobile");
                String acrid=jsn.getString("acaraid");
                String email=jsn.getString("email");
                String college=jsn.getString("college");


                if(mobile.length()>9){


                    Toast.makeText(MnumActivity.this, "Welcome "+name, Toast.LENGTH_LONG).show();

                    if(name.equalsIgnoreCase("New User"))
                    {
                        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mobnum", mobile); // Storing string
                        editor.putString("acaraid", acrid); // Storing string
                        editor.commit();

                        Intent i = new Intent(MnumActivity.this, WebActivity.class);
                        startActivity(i);
                        finish();}else
                    {
                        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mobnum", mobile); // Storing string
                        editor.putString("acaraid", acrid); // Storing string
                        editor.putString("uscollege", college); // Storing string
                        editor.putString("usemail", email); // Storing string
                        editor.putString("usname", name); // Storing string
                        editor.commit();

                        Intent i = new Intent(MnumActivity.this, WebActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
                else {
                    btn.setText("Login");
                    btn.setEnabled(true);
                    editmobile.setEnabled(true);
                    Toast.makeText(MnumActivity.this, "Please Check your Mobile Number. ", Toast.LENGTH_LONG).show();
                    findViewById(R.id.progresslay1).setVisibility(View.GONE);
                }



            }catch (Exception e){

                btn.setText("Try Again");
                btn.setEnabled(true);
                editmobile.setEnabled(true);
                findViewById(R.id.progresslay1).setVisibility(View.GONE);
              //  Toast.makeText(MnumActivity.this, "Connection Error !", Toast.LENGTH_SHORT).show();
                Log.e("json exp",e.toString());

            }


            /*Intent i = new Intent(registerActivity.this, loginActivity.class);
            startActivity(i);
            finish();*/
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    public void connecToServer(){
    }


}
