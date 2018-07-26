package com.example.luis.ingetnregistralo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EncodingUtils;
import cz.msebera.android.httpclient.util.EntityUtils;

public class RegistrarActivity extends Activity {

    EditText etnombre,etcorreo,etpass,etrpass,etapellido;
    Button btreg,btinicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registrar);


        etnombre=(EditText)findViewById(R.id.realname);
        etapellido=(EditText)findViewById(R.id.apellido);
        etcorreo=(EditText)findViewById(R.id.nick);
        etpass=(EditText)findViewById(R.id.pass);
        etrpass=(EditText)findViewById(R.id.rpass);
        btreg=(Button)findViewById(R.id.registrar);
        btinicio=(Button)findViewById(R.id.inicio);
        btinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(RegistrarActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etnombre.getText().toString().equals("")){
                    Toast.makeText(RegistrarActivity.this,"Complete Todos los Espacios",Toast.LENGTH_LONG).show();
                    etnombre.requestFocus();
                 }else{
                    if(etcorreo.getText().toString().equals("")){
                        Toast.makeText(RegistrarActivity.this,"Complete Todos los Espacios",Toast.LENGTH_LONG).show();
                        etcorreo.requestFocus();
                    }else{
                        if(etpass.getText().toString().equals(etrpass.getText().toString())){
                            Toast.makeText(RegistrarActivity.this,"El registro puede demorar unos minutos...",Toast.LENGTH_LONG).show();
                            enviardatos();
                        }else{
                            Toast.makeText(RegistrarActivity.this,"Las Contraseñas tienen que ser iguales",Toast.LENGTH_LONG).show();

                        }

                    }
                }
            }
        });

    }
    public void enviardatos() {
        String nombre = etnombre.getText().toString();
        String apellido = etapellido.getText().toString();
        String correo = etcorreo.getText().toString();
        String pass = etpass.getText().toString();
        String rpass = etrpass.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("nombre", nombre);
        params.put("apellido", apellido);
        params.put("nick", correo);
        params.put("pass", pass);
        params.put("rpass", rpass);

        String url = "http://200.7.161.55/sistcomunicados/webservices/insertarregistro.php";



        client.post(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200) {
                    String resultado = new String(responseBody);
                    Toast.makeText(RegistrarActivity.this, resultado, Toast.LENGTH_LONG).show();
                    etnombre.setText("");
                    etapellido.setText("");
                    etcorreo.setText("");
                    etpass.setText("");
                    etrpass.setText("");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(RegistrarActivity.this, "Falló Registro!!!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
