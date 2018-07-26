package com.example.luis.ingetnregistralo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends Activity {
    private EditText et1,et2;
    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        et1=(EditText)findViewById(R.id.mail);
        et2=(EditText)findViewById(R.id.pass);
        btn1=(Button)findViewById(R.id.button);
        btn2=(Button)findViewById(R.id.button2);
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        if(netInfo==null){
            Toast.makeText(LoginActivity.this,"No tiene acceso a Internet",Toast.LENGTH_LONG).show();
            finish();
        }else {


            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (et1.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Ingrese Correo", Toast.LENGTH_SHORT).show();
                        et1.requestFocus();
                    } else {
                        validar();
                    }
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent k = new Intent(LoginActivity.this, RegistrarActivity.class);
                    startActivity(k);

                }
            });
        }
     }

    private void validar(){
        String email=et1.getText().toString();
        String contra=et2.getText().toString();
        AsyncHttpClient client=new AsyncHttpClient();
        String url1="http://200.7.161.55/sistcomunicados/webservices/login.php";
        String parametros="?mail="+email+"&pass="+contra;
        client.post(url1 + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                    String respuesta=new String(responseBody);

                    if(respuesta.equals("  ")) {
                        SharedPreferences preferencias=getSharedPreferences("agenda", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferencias.edit();
                        editor.putString("email", et1.getText().toString());
                        editor.putString("contra", et2.getText().toString());
                        editor.commit();
                        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(LoginActivity.this,"Falló Login",Toast.LENGTH_LONG).show();
            }
        });
    }

}
