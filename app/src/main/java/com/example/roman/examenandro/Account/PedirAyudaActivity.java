package com.example.roman.examenandro.Account;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roman.examenandro.Conexion;
import com.example.roman.examenandro.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;

public class PedirAyudaActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    /** Componentes */
    private EditText txtDescripcion;
    private Spinner comboTipoProblema;
    private TextView tvLatitud;
    private TextView tvLongitud;
    private Button btnEnviar;
    private String latitud;
    private String longitud;
    ActionBar actionBar;

    /** objetos */
    private GoogleApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedir_ayuda);

        // ocultamos barra de titulo;
        getSupportActionBar().hide();

        initComponents();
        loadTipoDescripcion();
        initGoogleApi();
    }


    public void loadTipoDescripcion() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.TipoCaida, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        comboTipoProblema.setAdapter(adapter);
    }

    public void initGoogleApi() {

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void initComponents() {

        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        comboTipoProblema = (Spinner) findViewById(R.id.comboTipoProblema);
        tvLatitud = (TextView) findViewById(R.id.tvLatitud);
        tvLongitud = (TextView) findViewById(R.id.tvLongitud);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);

    }


    private void updateUI(Location loc) {
        if (loc != null) {
            tvLatitud.setText("Latitud: " + String.valueOf(loc.getLatitude()));
            tvLongitud.setText("Longitud: " + String.valueOf(loc.getLongitude()));
            this.latitud = String.valueOf(loc.getLatitude());
            this.longitud =  String.valueOf(loc.getLongitude());
        } else {
            tvLatitud.setText("Latitud: (desconocida)");
            tvLongitud.setText("Longitud: (desconocida)");
        }
    }

    private void insertar(View view) {

        Conexion conexion = new Conexion(this, "Peticiones", null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_usuario", "1");
        values.put("latitud", latitud);
        values.put("longitud", longitud);
        values.put("descripcion", txtDescripcion.getText().toString());
        values.put("tipo", comboTipoProblema.getSelectedItem().toString());
        values.put("estado", "1");
        long res = db.insert("Peticiones", null, values);

        if(res >0){
            Toast.makeText(PedirAyudaActivity.this,R.string.Solicitud_exito, Toast.LENGTH_LONG).show();
            this.limpiarCampos();
        }else{
            Toast.makeText(PedirAyudaActivity.this,R.string.error, Toast.LENGTH_LONG).show();
        }

        db.close();
    }

    private void limpiarCampos(){
        txtDescripcion.setText("");
        comboTipoProblema.setSelection(0);
    }

    public void pedirAyuda(View view) {
        if (txtDescripcion.getText().length() > 0) {
            if (comboTipoProblema.getSelectedItemId() > 0) {
                insertar(view);
            } else {
                Toast.makeText(this, R.string.error_comboEmpty, Toast.LENGTH_SHORT).show();
                comboTipoProblema.requestFocus();
            }
        } else {
            Toast.makeText(this, R.string.error_descpEmpty, Toast.LENGTH_SHORT).show();
            txtDescripcion.requestFocus();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);



            } else {
                Toast.makeText(PedirAyudaActivity.this, R.string.error_permiso, Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(PedirAyudaActivity.this,R.string.con_failer, Toast.LENGTH_LONG).show();
    }

}
