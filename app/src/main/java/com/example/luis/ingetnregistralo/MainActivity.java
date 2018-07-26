package com.example.luis.ingetnregistralo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static int SPLAH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo=cm.getActiveNetworkInfo();
                if(netInfo!=null){
                    SharedPreferences preferencia=getSharedPreferences("agenda", Context.MODE_PRIVATE);
                    if(preferencia.contains("email")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent j=new Intent(MainActivity.this,HomeActivity.class);
                                startActivity(j);
                                finish();
                            }
                        },SPLAH_TIME_OUT);
                    }else{
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent j=new Intent(MainActivity.this,LoginActivity.class);
                                startActivity(j);
                                finish();
                            }
                        },SPLAH_TIME_OUT);

                    }

                }else{
                    Toast.makeText(MainActivity.this,"No tiene acceso a Internet",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        },SPLAH_TIME_OUT);
    }
}
