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

public class VarActivity extends Activity {
    private ImageButton homebtn,cerrarbtn;
    private WebView varwv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_var);
        homebtn=(ImageButton)findViewById(R.id.homebtn);
        cerrarbtn=(ImageButton)findViewById(R.id.cerrarbtn);
        varwv=(WebView)findViewById(R.id.webView4);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(VarActivity.this,HomeActivity.class);
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
                Intent f=new Intent(VarActivity.this,LoginActivity.class);
                startActivity(f);
                finish();
            }
        });

        SharedPreferences preferencia=getSharedPreferences("agenda", Context.MODE_PRIVATE);
        String user=preferencia.getString("email","default");
        String correo="?usuario="+user;
        WebSettings webSettings=varwv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        varwv.loadUrl("http://200.7.161.55/sistcomunicados/webservices/planVar.php"+correo);
        varwv.setWebViewClient(new WebViewClient());
        varwv.setWebViewClient(new MyApp1());
        varwv.setWebChromeClient(new WebChromeClient());
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
