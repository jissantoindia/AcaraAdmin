package in.acara.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String unamestr,pswdstr;
    EditText Uname,Pwd;
    Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        Uname = findViewById(R.id.user);
        Pwd = findViewById(R.id.pwd);
        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
        unamestr=sharedPreferences.getString("uname","");
        pswdstr=sharedPreferences.getString("pswd","");

        loginbtn = findViewById(R.id.login);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int  NameLen = Uname.length();
                int  Pwdleng = Pwd.length();

                if (NameLen > 2 && Pwdleng > 2){

                    sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("uname", Uname.toString()); // Storing string
                    editor.putString("pswd", Pwd.getText().toString()); // Storing string
                    editor.commit();
                    String sendurl = "app_sin_android.php?username="+Uname.getText().toString()+"&password="+Pwd.getText().toString();

                    Intent i = new Intent(LoginActivity.this, WebActivity.class).putExtra("LogTry", sendurl);
                    startActivity(i);
                    finish();
                }

                else
                {
                    if (NameLen > 2)



                    Toast.makeText(LoginActivity.this, "Some field is empty", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
