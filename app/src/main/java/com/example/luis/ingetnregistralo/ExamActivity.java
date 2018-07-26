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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ExamActivity extends Activity {
    private ImageButton homebtn,cerrarbtn;
    private WebView examwv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        homebtn=(ImageButton)findViewById(R.id.homebtn);
        cerrarbtn=(ImageButton)findViewById(R.id.cerrarbtn);
        examwv=(WebView)findViewById(R.id.webView3);

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(ExamActivity.this,HomeActivity.class);
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
                Intent f=new Intent(ExamActivity.this,LoginActivity.class);
                startActivity(f);
                finish();

            }
        });


        SharedPreferences preferencia=getSharedPreferences("agenda", Context.MODE_PRIVATE);
        String user=preferencia.getString("email","default");
        String correo="?usuario="+user;
        WebSettings webSettings=examwv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        examwv.loadUrl("http://200.7.161.55/sistcomunicados/webservices/planExam.php"+correo);
        examwv.setWebViewClient(new WebViewClient());
        examwv.setWebViewClient(new MyApp());
        examwv.setWebChromeClient(new WebChromeClient());
    }
    public void onBackPressed(){
        if(examwv.canGoBack()){
            examwv.goBack();
            finish();
        }
        else
        {
            super.onBackPressed();
        }
    }
    public class MyApp extends WebViewClient {
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
