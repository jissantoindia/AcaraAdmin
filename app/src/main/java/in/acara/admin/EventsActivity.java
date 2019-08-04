package in.acara.admin;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.Spliterator;

public class EventsActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    String payeeAddress = "jiss@paytm";
    String payeeName = "Jiss Anto Shah";
    String transactionNote = "Test for Deeplinking";
    String amount = "1";
    String currencyUnit = "INR";
    String dataUsr;
    WebView webview;
    WebView newView;
    Button Bclose,mbtn;
    LinearLayout jisspay;
    TextView Cname,Cmobile,Cemail,Ccollege,Cdepart,Camt,Creg,Cename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getSupportActionBar().setTitle("Make Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        jisspay = findViewById(R.id.jisspay);
        webview = findViewById(R.id.webview2);


        dataUsr = getIntent().getStringExtra("Data");
        Toast.makeText(this, dataUsr, Toast.LENGTH_SHORT).show();
      //  webview.loadUrl(dataUsr+"200");
        String dataArr [] = dataUsr.split("&");
        String data0[] = dataArr [0].split("=");
        String data1[] = dataArr [1].split("=");
        String data2[] = dataArr [2].split("=");
        String data3[] = dataArr [3].split("=");
        String data4[] = dataArr [4].split("=");
        String data5[] = dataArr [5].split("=");
        String data6[] = dataArr [6].split("=");
        String data7[] = dataArr [7].split("=");
        String data8[] = dataArr [8].split("=");
        String data9[] = dataArr [9].split("=");
        Cename = findViewById(R.id.cename);
        Cname = findViewById(R.id.cname);
        Cmobile = findViewById(R.id.cmobile);
        Cemail = findViewById(R.id.cemail);

        Ccollege = findViewById(R.id.ccollege);
        Cdepart = findViewById(R.id.cdepart);
        Camt = findViewById(R.id.camt);
        Creg = findViewById(R.id.creg);
        data1[1] = data1[1].replace("+", " ");
        data5[1] = data5[1].replace("+", " ");
        data6[1] = data6[1].replace("%40", "@");
        data8[1] = data8[1].replace("+", " ");
        data9[1] = data9[1].replace("+", " ");
        Camt.setText("You have to pay ₹"+data0[1]);
        Cename.setText("EVENT NAME: "+data1[1]);
        Cname.setText("NAME: "+data5[1]);
        Cmobile.setText("MOBILE: "+data7[1]);
        Cemail.setText("EMAIL: "+data6[1]);
        Ccollege.setText("COLLEGE: "+data8[1]);
        Cdepart.setText("DEPARTMENT: "+data9[1]);
        Creg.setText("REGISTRATION ID: "+data3[1]);






        Button btnSubmit = (Button) findViewById(R.id.submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("upi://pay?pa="+payeeAddress+"&pn="+payeeName+"&tn="+transactionNote+
                        "&am="+amount+"&cu="+currencyUnit);
                Log.d(TAG, "onClick: uri: "+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(intent,1);
            }
        });

        Button btnInsta = (Button) findViewById(R.id.insta);

        btnInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent lin = new Intent(EventsActivity.this,AppActivity.class).putExtra("Data", dataUsr+"200");
                startActivity(lin);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "onActivityResult: requestCode: "+requestCode);
        Log.d(TAG, "onActivityResult: resultCode: "+resultCode);
        //txnId=UPI20b6226edaef4c139ed7cc38710095a3&responseCode=00&ApprovalRefNo=null&Status=SUCCESS&txnRef=undefined
        //txnId=UPI608f070ee644467aa78d1ccf5c9ce39b&responseCode=ZM&ApprovalRefNo=null&Status=FAILURE&txnRef=undefined

        if(data!=null) {
            Log.d(TAG, "onActivityResult: data: " + data.getStringExtra("response"));
            String res = data.getStringExtra("response");
            String search = "SUCCESS";
            if (res.toLowerCase().contains(search.toLowerCase())) {
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }



}
