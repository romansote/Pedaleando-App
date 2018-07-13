package com.example.roman.examenandro.Register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roman.examenandro.R;

public class RegisterStep1 extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtApellido;
    private EditText txtCorreo;
    private Button btnSiguiente;
    private Button btnlimpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // inicializamos objetos
        initComponents();

        // ocultamos la barra de titulo
        getSupportActionBar().hide();

    }

    private void initComponents(){
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        btnlimpiar = (Button) findViewById(R.id.btnLimpiar);
        btnSiguiente  =(Button) findViewById(R.id.btnSiguiente);
    }


    public void goStep2(View view){
        if(txtNombre.getText().length()>0){
            if(txtApellido.getText().length()>0){
                if(txtCorreo.getText().length()>0){
                    Intent intent = new Intent(RegisterStep1.this,RegisterStep2.class);
                    intent.putExtra("nombre",txtNombre.getText().toString());
                    intent.putExtra("apellido",txtApellido.getText().toString());
                    intent.putExtra("correo",txtCorreo.getText().toString());
                    Log.d("nombre",txtNombre.getText().toString());
                    Log.d("apellido",txtApellido.getText().toString());
                    Log.d("correo",txtCorreo.getText().toString());
                    startActivity(intent);
                }else{
                    Toast.makeText(this,R.string.Error_EmailEmpty,Toast.LENGTH_SHORT).show();
                    txtCorreo.requestFocus();
                }
            }else{
                Toast.makeText(this,R.string.Error_ApellidoEmpty,Toast.LENGTH_SHORT).show();
                txtApellido.requestFocus();
            }
        }else{
            Toast.makeText(this,R.string.Error_userEmpty,Toast.LENGTH_SHORT).show();
            txtNombre.requestFocus();
        }
    }

    public void limpiarCampos(View view){
         txtNombre.setText("");
         txtApellido.setText("");
         txtCorreo.setText("");

    }
}
