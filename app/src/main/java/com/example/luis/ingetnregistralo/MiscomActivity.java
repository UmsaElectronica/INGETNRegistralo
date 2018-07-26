package com.example.luis.ingetnregistralo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

public class MiscomActivity extends Activity {
    private ImageButton homebtn,cerrarbtn;
    private WebView miscowv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miscom);
        homebtn=(ImageButton)findViewById(R.id.homebtn);
        cerrarbtn=(ImageButton)findViewById(R.id.cerrarbtn);
        miscowv=(WebView)findViewById(R.id.webView5);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(MiscomActivity.this,HomeActivity.class);
                startActivity(a);
                
            }
        });

        cerrarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferencia=getSharedPreferences("agenda", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferencia.edit();
                editor.remove("email");
                editor.remove("contra");
                editor.commit();
                Intent f=new Intent(MiscomActivity.this,LoginActivity.class);
                startActivity(f);
                finish();

            }
        });

        SharedPreferences preferencia=getSharedPreferences("agenda", Context.MODE_PRIVATE);
        String user=preferencia.getString("email","default");
        String correo="?usuario="+user;
        WebSettings webSettings=miscowv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        miscowv.loadUrl("http://200.7.161.55/sistcomunicados/webservices/misCom.php"+correo);
        miscowv.setWebViewClient(new WebViewClient());
        miscowv.setWebViewClient(new MyApp1());
        miscowv.setWebChromeClient(new WebChromeClient());
    }
    public class MyApp1 extends WebViewClient {
        public boolean shouldOverateUrlLoading(WebView view,String url){
            if(Uri.parse(url).getHost().endsWith("google.com")){
                return false;
            }
            Intent j=new Intent(Intent.ACTION_VIEW,Uri.parse(url));
            view.getContext().startActivity(j);
            return true;
        }
    }
}
