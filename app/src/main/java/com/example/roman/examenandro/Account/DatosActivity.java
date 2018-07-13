package com.example.roman.examenandro.Account;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.roman.examenandro.R;

public class DatosActivity extends AppCompatActivity {

    private TextView nombre,apellido,rut;
    private Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        this.initComponents();

        datos = this.getIntent().getExtras();

        this.nombre.setText(datos.get("nombre").toString());
        this.apellido.setText(datos.get("rut").toString());
        this.rut.setText(datos.get("apellido").toString());
    }

    public void initComponents(){
        nombre = (TextView) findViewById(R.id.tvNombre);
        apellido = (TextView) findViewById(R.id.tvApellido);
        rut = (TextView) findViewById(R.id.tvRut);
    }

}
