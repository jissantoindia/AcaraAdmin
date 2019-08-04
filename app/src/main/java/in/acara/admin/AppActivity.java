package in.acara.admin;

import android.os.Message;
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
import android.widget.Toast;

import com.instamojo.android.Instamojo;

public class AppActivity extends AppCompatActivity {

    WebView webview;
    WebView newView;
    Button Bclose, mbtn;
    LinearLayout jisspay;
    String dataUsr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        getSupportActionBar().setTitle("Make Payment");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        jisspay = findViewById(R.id.jisspay);
        webview = findViewById(R.id.webview2);
        dataUsr = getIntent().getStringExtra("Data");
        webview.setWebViewClient(new newWebClient());

        WebSettings set = webview.getSettings();
        webview.loadUrl("https://app.acara.in/appconfirm.php?"+dataUsr);
        webview.loadUrl("javascript:(function(){"+
                "l=document.getElementById('btnSubmit');"+
                "e=document.createEvent('HTMLEvents');"+
                "e.initEvent('click',true,true);"+
                "l.dispatchEvent(e);"+
                "})()");
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(false);

        webview.setBackgroundColor(0x00000000);
        webview.setVerticalScrollBarEnabled(false);
        webview.getSettings().setAppCachePath("/data/data/" + getPackageName() + "/cache");
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webview.loadUrl("javascript:(function(){document.getElementById('btnSubmit').click();})()");

        webview.getSettings().setSupportMultipleWindows(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);


                if (newProgress >= 100) {
                    jisspay.setVisibility(View.GONE);


                } else {
                    jisspay.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                webview.removeAllViews();
                newView = new WebView(AppActivity.this);
                newView.setWebViewClient(new WebViewClient());
                newView.setWebChromeClient(new ACWebchromeClient());

                newView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                newView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                WebSettings set = newView.getSettings();
                set.setJavaScriptEnabled(true);
                //newView.addJavascriptInterface(new PaymentInterface(MainActivity.this), "paymentInterface");

                webview.addView(newView);
                Bclose.setVisibility(View.VISIBLE);
                Bclose.bringToFront();
                mbtn.setVisibility(View.VISIBLE);


                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newView);
                resultMsg.sendToTarget();
                //  Toast.makeText(MainActivity.this, resultMsg.toString(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    class newWebClient extends WebViewClient {


       /* public boolean shouldOverrideUrlLoading(WebView view, String url) {


            return true;
        }*/
    }


    public class ACWebchromeClient extends WebChromeClient {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);


            if (newProgress >= 100) {
                jisspay.setVisibility(View.GONE);


            } else {
                jisspay.setVisibility(View.VISIBLE);


            }
        }

    }
}
