package in.acara.admin;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


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

public class UserUpdate extends AppCompatActivity {

    String mobilenum,acaraid;
    SharedPreferences sharedPreferences;
    TextView DisMob,DisAcrId;
    EditText Name,College,Email;
    Button SaveInfo;
    LinearLayout LoaderLay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_update);
        getSupportActionBar().hide();

        DisMob = findViewById(R.id.DisMobileusr);
        DisAcrId = findViewById(R.id.DisAcridusr);
        Name = findViewById(R.id.Nameusr);
        College = findViewById(R.id.Collegeusr);
        Email =  findViewById(R.id.Emailusr);
        SaveInfo = findViewById(R.id.userUpdateBtn);
        LoaderLay = findViewById(R.id.progresslay1);

        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
        mobilenum=sharedPreferences.getString("mobnum","");
        acaraid=sharedPreferences.getString("acaraid","");

        DisMob.setText("Mobile: "+mobilenum);
        DisAcrId.setText("Acara ID: "+acaraid);

        SaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                URL url = new URL(getString(R.string.Update)); // here is your URL path
                JSONObject postDataParams = new JSONObject();

                postDataParams.put("acaraid", acaraid);
                postDataParams.put("name", Name.getText().toString());
                postDataParams.put("email", Email.getText().toString());
                postDataParams.put("college", College.getText().toString());
                //postDataParams.put("action", "UpdateUsrInfo");



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

            Toast.makeText(UserUpdate.this, result, Toast.LENGTH_SHORT).show();

            try {
                JSONObject jsn=new JSONObject(result);

                String Stat=jsn.getString("status");


                    if(Stat.equalsIgnoreCase("done"))
                    {
                        Intent i = new Intent(UserUpdate.this, AppActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        findViewById(R.id.progresslay1).setVisibility(View.GONE);
                        Toast.makeText(UserUpdate.this, "Something went Wrong!", Toast.LENGTH_SHORT).show();
                    }



            }catch (Exception e){

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
