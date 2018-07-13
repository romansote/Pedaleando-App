package com.example.roman.examenandro.Register;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roman.examenandro.Conexion;
import com.example.roman.examenandro.Login.LoginActivity;
import com.example.roman.examenandro.R;

public class RegisterStep2 extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtPass;
    private EditText txtpass2;
    private Button btnLimpiar;
    private Button btnRegistrar;
    private EditText txtCorreo;
    private EditText txtRut;
    private Bundle datos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_step2);

        // ocultamos la barra de titulo
        getSupportActionBar().hide();

        // inicializamos objetos
        initComponents();

        datos = this.getIntent().getExtras();

        consultar();


        }

    private void initComponents(){
        txtUser = (EditText) findViewById(R.id.txtUsuario);
        txtPass = (EditText) findViewById(R.id.txtContraseña);
        txtpass2 = (EditText)findViewById(R.id.txtContraseña2);
        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistro);
        txtRut = (EditText) findViewById(R.id.txtRut);


    }

    public void Registro(View view){
                if(txtUser.getText().length()>0){
                    if(txtPass.getText().length()>0){
                        if(txtpass2.getText().length()>0){
                            if(!txtPass.getText().equals(txtpass2.getText())){
                                if(txtRut.getText().length()>0){
                                    this.insertar(view);
                                    Intent intent = new Intent(RegisterStep2.this,LoginActivity.class);
                                    intent.putExtra("user",txtUser.getText().toString());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(this,R.string.Error_rutEmpty,Toast.LENGTH_SHORT).show();
                                    txtRut.requestFocus();
                                }
                            }else{
                                Toast.makeText(this,R.string.Error_passDiferente,Toast.LENGTH_SHORT).show();
                                txtpass2.requestFocus();
                            }
                        }else{
                            Toast.makeText(this,R.string.Error_passEmpty,Toast.LENGTH_SHORT).show();
                            txtpass2.requestFocus();
                        }
                    }else{
                        Toast.makeText(this,R.string.Error_passEmpty,Toast.LENGTH_SHORT).show();
                        txtPass.requestFocus();
                    }
                }else{
                    Toast.makeText(this,R.string.Error_userEmpty,Toast.LENGTH_SHORT).show();
                    txtUser.requestFocus();
                }

    }

    public void limpiar(View view) {
        txtRut.setText("");
        txtUser.setText("");
        txtPass.setText("");
        txtpass2.setText("");
        txtRut.requestFocus();
    }

    public void insertar(View view){
        Conexion conexion = new Conexion(this,"Usuario",null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario",txtUser.getText().toString());
        values.put("contraseña",txtPass.getText().toString());
        values.put("nombre",datos.get("nombre").toString());
        values.put("apellido",datos.get("apellido").toString());
        values.put("correo",datos.get("correo").toString());
        values.put("rut",txtRut.getText().toString());
        values.put("caracteristicas","empty");
        db.insert("Usuario",null,values);
        db.close();
    }

    public void consultar(){
        Conexion conexion = new Conexion(this,"Usuario",null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Usuario",null);
        Log.d("columnas",cursor.getColumnCount()+"");

        while(cursor.moveToNext()){
           try {
               Log.d("id:", cursor.getString(0));
               Log.d("usuario:", cursor.getString(1));
               Log.d("contraseña:", cursor.getString(2));
               Log.d("nombre:", cursor.getString(3));
               Log.d("apellido:", cursor.getString(4));
               Log.d("correo:", cursor.getString(5));
               Log.d("rut:", cursor.getString(6));
               Log.d("caracteristicas", cursor.getString(7));
           }catch (Exception e){
               Log.d("Error","esta vacio"+e);
           }
        }
        db.close();
    }


}
