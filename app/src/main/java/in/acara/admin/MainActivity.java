package in.acara.admin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ImageView adbtn;
    String unamestr,pswdstr,adminstat="off",mobilenum;
    private   int requestCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        adbtn=findViewById(R.id.adminbtn);
        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
        unamestr=sharedPreferences.getString("uname","");
        mobilenum=sharedPreferences.getString("mobnum","");
        pswdstr=sharedPreferences.getString("pswd","");
        adminstat=sharedPreferences.getString("adminstat","off");




        adbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adminstat.equalsIgnoreCase("off")) {
                    sharedPreferences = getSharedPreferences("acaraadmin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("adminstat", "on"); // Storing string
                    editor.commit();

                    Toast.makeText(MainActivity.this, "Restart the App for Admin Login", Toast.LENGTH_LONG).show();

                }else
                {
                    sharedPreferences = getSharedPreferences("acaraadmin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("adminstat", "off"); // Storing string
                    editor.commit();

                    Toast.makeText(MainActivity.this, "Restart the App for User Login", Toast.LENGTH_LONG).show();


                }



            }
        });

        if(adminstat.equalsIgnoreCase("off")) {
            adbtn.setImageDrawable(getDrawable(R.drawable.adminoff));

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if(mobilenum.length()>9) {
                        Intent intent = new Intent(MainActivity.this, WebActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Intent intent = new Intent(MainActivity.this, MnumActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 3000);

        }else{
            adbtn.setImageDrawable(getDrawable(R.drawable.admin));

            loginCheck();
        }
    }





    public void loginCheck(){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                AdminLogin();

            }
        }, 5000);

    }

    public  void AdminLogin()
    {
        if (unamestr.length() > 0 && pswdstr.length() > 0) {

            String sendurl = "?username=" + unamestr + "&password=" + pswdstr;

            Intent i = new Intent(MainActivity.this, WebActivity.class).putExtra("LogTry", sendurl);
            startActivity(i);
            finish();


        } else {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        // This method will be executed once the timer is over


    }
}
