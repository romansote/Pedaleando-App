package com.example.roman.examenandro.Account;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.roman.examenandro.Conexion;
import com.example.roman.examenandro.R;
import com.example.roman.examenandro.Register.RegisterStep1;
import com.example.roman.examenandro.Register.RegisterStep2;

public class BuscarActivity extends AppCompatActivity {

    private EditText nombre,rut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        this.initComponents();
    }

    public void initComponents(){

        nombre = (EditText) findViewById(R.id.txtNombre);
        rut = (EditText) findViewById(R.id.txtRut);

    }

    public void consultar(View view) {
        if (this.rut.getText().length() > 0) {
            Conexion conexion = new Conexion(this, "Usuario", null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            Cursor cursor = db.rawQuery("Select * from Usuario where rut ='" + this.rut.getText() + "'", null);
            Log.d("columnas", cursor.getColumnCount() + "");

           if(cursor.moveToNext()) {
                try {

                    Intent intent = new Intent(BuscarActivity.this,DatosActivity.class);
                    intent.putExtra("nombre", cursor.getString(1));
                    intent.putExtra("apellido", cursor.getString(4));
                    intent.putExtra("rut", cursor.getString(6));

                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this,"no se encontraron datos", Toast.LENGTH_LONG).show();
                }
            }
            db.close();
        }else {
            Toast.makeText(this,"Debe igresar el rut de la persona a buscar", Toast.LENGTH_LONG).show();
        }


    }


    public void buscar(View view){

    }

    public void limpiarCampos(View view){
        this.nombre.setText("");
        this.rut.setText("");
        this.nombre.requestFocus();
    }


}
