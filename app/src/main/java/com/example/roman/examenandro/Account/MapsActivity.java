package com.example.roman.examenandro.Account;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.roman.examenandro.Conexion;
import com.example.roman.examenandro.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private GoogleApiClient apiClient;
    private Double latitud;
    private Double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initGoogleApi();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        // Establishes default camera zoom
        mMap.setMinZoomPreference(12.0f);
        // set button location
        setLocationButton(true);
        // set Style on maps activity
        setStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        setDataOnMap();
    }

        public void setDataOnMap(){
            Conexion conexion = new Conexion(this,"Peticiones",null,1);
            SQLiteDatabase db = conexion.getWritableDatabase();
            Cursor cursor = db.rawQuery("Select * from Peticiones",null);
            Log.d("columnas",cursor.getColumnCount()+"");

            while(cursor.moveToNext()){

                Double _lat = Double.valueOf(cursor.getString(2));
                Double _long = Double.valueOf(cursor.getString(3));
                LatLng loc = new LatLng(_lat,_long);

                if(cursor.getString(5).equals("Falla Mecanica")){
                    setLocation(cursor.getString(5).toString(),cursor.getString(4).toString(),loc,R.drawable.p1,cursor.getString(0));
                }else if(cursor.getString(5).equals("Problema fisico")){
                    setLocation(cursor.getString(5).toString(),cursor.getString(4).toString(),loc,R.drawable.p2,cursor.getString(0));
                }

            }
            db.close();
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                @SuppressWarnings("MissingPermission")
                Location lastLocation =
                        LocationServices.FusedLocationApi.getLastLocation(apiClient);

                getLocation(lastLocation);



            } else {
                Toast.makeText(MapsActivity.this, R.string.error_permiso, Toast.LENGTH_LONG);
            }
        }
    }

    private void getLocation(Location loc) {
        if (loc != null) {
           this.latitud=loc.getLatitude();
           this.longitud= loc.getLongitude();
            LatLng loca = new LatLng(Double.valueOf(this.latitud), Double.valueOf(this.longitud));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loca));

        }
    }

    public void initGoogleApi() {

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void setStyle(MapStyleOptions style){mMap.setMapStyle(style);}

    private LatLng getCurrentLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        LatLng userLocation = null;
        Location location = service.getLastKnownLocation(provider);
        if(location != null) {
            userLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }
        else{
            userLocation = new LatLng(0,0);
        }
        return userLocation;
    }

    private void setLocation(final String title, final String description, LatLng latLng, int drawable, final String id){

        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.fromResource(drawable)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle(title)
                        .setMessage(description)
                        .setPositiveButton("si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finalizarAyuda(id);

                            }
                        });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  Toast.makeText(context, "prueba", Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        });
    }

    public void finalizarAyuda(String id){
        Conexion conexion = new Conexion(this,"Peticiones",null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        int rows = db.delete("Peticiones","id="+id,null);
            if(rows > 0){
                Toast.makeText(MapsActivity.this,R.string.Solicitud_ok,Toast.LENGTH_LONG).show();
                mMap.clear();
                setDataOnMap();

            }
        }

    private void setLocationButton(boolean status){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(status);
        } else {
            Toast.makeText(MapsActivity.this, R.string.error_permissions, Toast.LENGTH_SHORT).show();
            Toast.makeText(MapsActivity.this,""+mMap.getCameraPosition().zoom,Toast.LENGTH_LONG).show();
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

            getLocation(lastLocation);
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
     //   Toast.makeText(MapsActivity.this,"si", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }


}
