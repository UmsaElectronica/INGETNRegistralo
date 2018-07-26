package com.example.luis.ingetnregistralo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends Activity {
        private ImageButton testbtn,clasebtn,varbtn,docbtn,elimbtn,salirbtn,cerrarbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        testbtn = (ImageButton) findViewById(R.id.testbtn);
        clasebtn = (ImageButton) findViewById(R.id.clasebtn);
        varbtn = (ImageButton) findViewById(R.id.varbtn);
        docbtn = (ImageButton) findViewById(R.id.docbtn);
        elimbtn = (ImageButton) findViewById(R.id.elimbtn);
        salirbtn = (ImageButton) findViewById(R.id.salirbtn);
        cerrarbtn = (ImageButton) findViewById(R.id.cerrarbtn);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            Toast.makeText(HomeActivity.this, "No tiene acceso a Internet", Toast.LENGTH_LONG).show();
            finish();
        } else {

            testbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent a = new Intent(HomeActivity.this, ExamActivity.class);
                    startActivity(a);
                }
            });

            clasebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent b = new Intent(HomeActivity.this, clasActivity.class);
                    startActivity(b);
                }
            });

            varbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent c = new Intent(HomeActivity.this, VarActivity.class);
                    startActivity(c);
                }
            });

            docbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent d = new Intent(HomeActivity.this, DocuActivity.class);
                    startActivity(d);
                }
            });

            elimbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent e = new Intent(HomeActivity.this, MiscomActivity.class);
                    startActivity(e);
                }
            });

            salirbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }
            });
            cerrarbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences preferencia = getSharedPreferences("agenda", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencia.edit();
                    editor.remove("email");
                    editor.remove("contra");
                    editor.commit();
                    Intent f = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(f);
                    finish();
                }
            });

        }
    }
}
