package in.acara.admin;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
    WebView webview,SideMenu;
    ImageView MenuBtn, ScanBtn;
    ConstraintLayout OpenSideBg;
    private int requestCode=1;
    String content,adminstat,Sendurl,mobilenum;
    SharedPreferences sharedPreferences;
    Boolean OpenSide = false;
    Animation slideLeftAnimation,slideRightAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();

        sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
        adminstat=sharedPreferences.getString("adminstat","");
        mobilenum=sharedPreferences.getString("mobnum","");
        ScanBtn = (ImageView) findViewById(R.id.scan);

      //  Toast.makeText(this, Sendurl, Toast.LENGTH_SHORT).show();
        slideLeftAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.left);
        slideRightAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.right);

        OpenSideBg = findViewById(R.id.OpenSideBg);
        OpenSideBg.setVisibility(View.GONE);
        webview = (WebView)findViewById(R.id.webview);
        SideMenu = (WebView)findViewById(R.id.SideMenu);


        if(adminstat.equalsIgnoreCase("on")) {
            if (ContextCompat.checkSelfPermission(WebActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(WebActivity.this, new String[]{Manifest.permission.CAMERA}, requestCode);
            }



            Sendurl = "https://acara.in/admin/pages/"+getIntent().getStringExtra("LogTry");
        }else
        {
            ScanBtn.setVisibility(View.GONE);
            Sendurl="https://app.acara.in/appindex.php";
        }

        webview.setWebViewClient(new WebClient());
        SideMenu.setWebViewClient(new WebClient2());
        WebSettings set = webview.getSettings();
        WebSettings setS = SideMenu.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);
        setS.setJavaScriptEnabled(true);
        setS.setBuiltInZoomControls(false);
        webview.loadUrl(Sendurl);


        webview.setVerticalScrollBarEnabled(false);
        webview.setBackgroundColor(0x00000000);
        webview.getSettings().setAppCachePath("/data/data/"+ getPackageName() +"/cache");
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheMaxSize(1024*1024*8);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
      //  webview.addJavascriptInterface(new WebAppInterface(this), "AndroidInterface");
        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);




        webview.setWebChromeClient(new WebChromeClient()
        {public void onProgressChanged(WebView view, int progress)
        {

            findViewById(R.id.loaderLayout).setVisibility(View.VISIBLE);

            // Return the app name after finish loading
            if(progress == 100)

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {

                findViewById(R.id.loaderLayout).setVisibility(View.GONE);
            webview.startAnimation(slideLeftAnimation);

                    }
                }, 800);
        }
        });

        if(adminstat.equalsIgnoreCase("on")) { SideMenu.loadUrl("https://acara.in/admin/pages/SideMenuApp.php");}else
        {}

        SideMenu.setWebChromeClient(new WebChromeClient());


        MenuBtn=findViewById(R.id.menuBtn);
        MenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(OpenSide)
               {

                   SideMenu.startAnimation(slideRightAnimation);
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {

                           OpenSideBg.setVisibility(View.GONE);
                       }
                   }, 500);

                   webview.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View v, MotionEvent event) {
                           return false;
                       }
                   });
                   OpenSide=false;
               }
               else
               {
                   OpenSideBg.setVisibility(View.VISIBLE);
                   SideMenu.startAnimation(slideLeftAnimation);
                   webview.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View v, MotionEvent event) {
                           return true;
                       }
                   });
                   OpenSide=true;
               }

            }


        });



        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent lin = new Intent(WebActivity.this,QrScan.class);
                startActivity(lin);
                finish();


            }


        });


        OpenSideBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    SideMenu.startAnimation(slideRightAnimation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            OpenSideBg.setVisibility(View.GONE);
                        }
                    }, 500);

                    webview.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    OpenSide=false;

            }


        });


    }


    @Override
    public void onBackPressed() {


        if (webview.canGoBack()) {

        webview.goBack();

        } else {
            super.onBackPressed();
        }
    }




    class WebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains("BackToQRScaner")) {

                Toast.makeText(WebActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent lin = new Intent(WebActivity.this,QrScan.class);
                startActivity(lin);
                finish();

            }

            if (url.contains("logout.php")) {

                Intent lin = new Intent(WebActivity.this,LoginActivity.class);
                startActivity(lin);
                finish();

                sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uname", ""); // Storing string
                editor.putString("pswd", ""); // Storing string
                editor.commit();

            }

            else if(url.contains("confirm.php"))
            {

                String data = url.substring(33);


            //    Toast.makeText(WebActivity.this, data, Toast.LENGTH_SHORT).show();
                //view.loadUrl(url+mobilenum);
                Intent lin = new Intent(WebActivity.this,EventsActivity.class).putExtra("Data", data);
                startActivity(lin);

            }

            else if(url.contains("appregister.php"))
            {

                //Toast.makeText(WebActivity.this, url, Toast.LENGTH_SHORT).show();
                view.loadUrl(url+mobilenum);
            }
           else if(url.contains("login.php"))
            {
                Intent lin = new Intent(WebActivity.this,LoginActivity.class);
                startActivity(lin);
                finish();

                sharedPreferences = getSharedPreferences("acaraadmin",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("uname", ""); // Storing string
                editor.putString("pswd", ""); // Storing string
                editor.commit();

                Toast.makeText(WebActivity.this, "Login Error! Try Again.", Toast.LENGTH_SHORT).show();
            }
           else if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                view.reload();
                return true;
            }
            else {

                view.loadUrl(url);

            }

            return true;
        }



    }

    class WebClient2 extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {



            if (url.contains("sidemenu.in/")) {

              content =  url.replaceAll("sidemenu.in/", "");

                webview.loadUrl(content);

                SideMenu.startAnimation(slideRightAnimation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        OpenSideBg.setVisibility(View.GONE);
                    }
                }, 500);

                webview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                OpenSide=false;
            }

           return true;
        }
    }

}
