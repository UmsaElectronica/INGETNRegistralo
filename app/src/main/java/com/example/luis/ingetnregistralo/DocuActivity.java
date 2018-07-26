package com.example.luis.ingetnregistralo;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class DocuActivity extends Activity {
    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = DocuActivity.class.getSimpleName();
    private static final int MY_PERMISO = 1;
    private String selectedFilePath;
    private String SERVER_URL = "http://www.electronica.umsa.bo/sistcomunicados/webservices/UploadToServer.php";
    private ImageButton homebtn,cerrarbtn;
    private Button subirbtn;
    private TextView tvpath;
    private EditText etPlannedDate,ettitulo;
    private Spinner etsigla;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ImageView ivpdf,ivimg;
    ProgressDialog dialog;

    private AsyncHttpClient spin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docu);
        homebtn=(ImageButton)findViewById(R.id.homebtn);
        cerrarbtn=(ImageButton)findViewById(R.id.cerrarbtn);
        etPlannedDate =(EditText)findViewById(R.id.etPlannedDate);
        ettitulo=(EditText)findViewById(R.id.titulo);
        etsigla=(Spinner)findViewById(R.id.sigla);
        ivpdf=(ImageView)findViewById(R.id.imageView3);
        ivimg=(ImageView)findViewById(R.id.imageView4);
        subirbtn=(Button)findViewById(R.id.upload);
        tvpath=(TextView)findViewById(R.id.path);





        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(DocuActivity.this,HomeActivity.class);
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
                Intent f=new Intent(DocuActivity.this,LoginActivity.class);
                startActivity(f);
                finish();

            }
        });

        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(DocuActivity.this,android.R.style.Theme_Holo_Dialog,mDateSetListener,year,month,day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month=month +1;
                Log.d(TAG,"onDataset:date: " + year +"/"+month+"/"+day  );
                String date=year+"-"+month+"-"+day;
                etPlannedDate.setText(date);
            }
        };
    }

    public void onClick(View v) {
        if(v== ivpdf){
            if (ActivityCompat.checkSelfPermission(DocuActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(DocuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new SweetAlertDialog(DocuActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Atención")
                            .setContentText("Debes otorgar permisos para esta acción.")
                            .setConfirmText("Solicitar permiso")
                            .setCancelText("Cancelar")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    ActivityCompat.requestPermissions(DocuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            MY_PERMISO);
                                }
                            })
                            .show();
                } else {
                    ActivityCompat.requestPermissions(DocuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISO);
                }
            }else {
                //on attachment icon click
                showFileChooserpdf();
               }

        }
        if(v==ivimg){
            if (ActivityCompat.checkSelfPermission(DocuActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(DocuActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    new SweetAlertDialog(DocuActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Atención")
                            .setContentText("Debes otorgar permisos para esta acción.")
                            .setConfirmText("Solicitar permiso")
                            .setCancelText("Cancelar")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.cancel();
                                    ActivityCompat.requestPermissions(DocuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            MY_PERMISO);
                                }
                            })
                            .show();
                } else {
                    ActivityCompat.requestPermissions(DocuActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISO);
                }
            }else {
                //on attachment icon click
                showFileChooserimg();
            }

        }
        if(v== subirbtn){
            if(ettitulo.getText().toString().equals("")){
             Toast.makeText(DocuActivity.this,"Por favor ingrese título",Toast.LENGTH_LONG).show();
                ettitulo.requestFocus();
            }else{
                if(String.valueOf(etsigla.getSelectedItem()).equals("Seleccionar Materia...")){
                 Toast.makeText(DocuActivity.this,"Por favor seleccione materia",Toast.LENGTH_LONG).show();
                    etsigla.requestFocus();
                }else {
                    if(etPlannedDate.getText().toString().equals("")){
                        Toast.makeText(DocuActivity.this,"Fecha incorrecta",Toast.LENGTH_LONG).show();
                        etPlannedDate.requestFocus();
                    }else {
                        //on upload button Click
                        String e=selectedFilePath.substring(selectedFilePath.lastIndexOf(".")+1);
                        if(e.equals("pdf")||e.equals("jpg")||e.equals("png")||e.equals("gif")) {
                            if (selectedFilePath != null) {

                                dialog = ProgressDialog.show(DocuActivity.this, "", "Enviando Archivo...", true);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //creating new thread to handle Http Operations
                                        uploadFile(selectedFilePath);
                                    }
                                }).start();
                            } else {
                                Toast.makeText(DocuActivity.this, "Por favor seleccione archivo", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(DocuActivity.this,"Archivos permitidos:pdf, jpg, png y gif",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }

        }
    }

    private void showFileChooserpdf() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("application/pdf");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Seleccionar archivo.."),PICK_FILE_REQUEST);
    }
    private void showFileChooserimg() {

        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("image/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent,"Seleccionar archivo.."),PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == PICK_FILE_REQUEST){
                if(data == null){
                    //no data present
                    return;
                }


                Uri selectedFileUri = data.getData();
                selectedFilePath = FilePath.getPath(this,selectedFileUri);

                Log.i(TAG,"Ruta de archivo seleccionado:" + selectedFilePath);

                if(selectedFilePath != null && !selectedFilePath.equals("")){
                    tvpath.setText(selectedFilePath);
                }else{
                    Toast.makeText(this,"No se puede cargar archivo",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //android upload file to server
    public int uploadFile(final String selectedFilePath){

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length-1];

        if (!selectedFile.isFile()){
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvpath.setText("No existe archivo: " + selectedFilePath);
                }
            });
            return 0;
        }else{

            try{
                String titulo=ettitulo.getText().toString();
                String sigla=String.valueOf(etsigla.getSelectedItem());
                String fecha=etPlannedDate.getText().toString();
                SharedPreferences preferencia=getSharedPreferences("agenda", Context.MODE_PRIVATE);
                String user=preferencia.getString("email","default");
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("Accept-Charset","UTF-8");
                connection.setRequestProperty("uploaded_file",selectedFilePath);




                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"sigla\"" + lineEnd + lineEnd + sigla + lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"titulo\"" + lineEnd + lineEnd + titulo + lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"fecha\"" + lineEnd + lineEnd + fecha + lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"usuario\"" + lineEnd + lineEnd + user + lineEnd);

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\""+lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);


                serverResponseCode = connection.getResponseCode();
                final String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Servidor dice: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if(serverResponseCode == 200){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String resultado=new String(serverResponseMessage);
                            Toast.makeText(DocuActivity.this,"Archivo cargado:"+resultado,Toast.LENGTH_LONG).show();
                            ettitulo.setText("");
                            etPlannedDate.setText("");
                            tvpath.setText("");
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DocuActivity.this,"Archivo inexistente",Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(DocuActivity.this, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(DocuActivity.this, "No se puede leer el archivo", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            return serverResponseCode;
        }

    }

}
