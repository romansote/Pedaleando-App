package com.example.roman.examenandro.Login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roman.examenandro.Account.HomeActivity;
import com.example.roman.examenandro.Account.PedirAyudaActivity;
import com.example.roman.examenandro.Conexion;
import com.example.roman.examenandro.R;
import com.example.roman.examenandro.Register.RegisterStep1;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.LocationServices;

public class LoginActivity extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtPass;
    private Button btnIngresar;
    private Button btnRegistrar;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ocultamos barra de titulo;
        getSupportActionBar().hide();

        // inicializamos componentes
        this.loadComponents();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle datos = this.getIntent().getExtras();

        if(datos!= null){
            Toast.makeText(this,R.string.Registro_exitoso,Toast.LENGTH_LONG).show();
            txtUser.setText((String) datos.get("user"));
            txtPass.requestFocus();
        }

    }

    private void loadComponents(){
        txtUser = (EditText) findViewById(R.id.txtUsuario);
        txtPass = (EditText) findViewById(R.id.txtPsw);
        btnIngresar = (Button) findViewById(R.id.btnLogin);
        btnRegistrar = (Button) findViewById(R.id.btnRegistro);
        loginButton = (LoginButton) findViewById(R.id.login_button);
    }

    public void irARegistro(View v){

        Intent intent = new Intent(this,RegisterStep1.class);

        startActivity(intent);

    }

    /**
     * Metodo que verifica los datos ingresados por el login
     * Verdadero: los datos introducidos son correctos
     * Falso: los datos introducidos son incorrectos.
     * @param usuario
     * @param contraseña
     * @return
     */
    private boolean checkLogin(String usuario, String contraseña){
        boolean resultado;
        Conexion conexion = new Conexion(this,"Usuario",null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from Usuario where usuario ='"+usuario+"' and contraseña = '"+contraseña+"'",null);
        if(cursor.moveToFirst()){
            Log.d("usuario id", cursor.getString(0));
            resultado = true;
        }else{
            resultado = false;
        }
        return resultado;
    }

    public void login(View view){
        if(checkLogin(txtUser.getText().toString(),txtPass.getText().toString()) == false){
            Toast.makeText(this,R.string.Error_login_incorrecto, Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       if(requestCode == 1){
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {



            } else {
                Toast.makeText(LoginActivity.this,R.string.error_permiso,Toast.LENGTH_LONG);


            }
        }
    }
}
