package com.example.roman.examenandro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

/**
 * Created by roman on 23-06-17.
 */

public class Conexion extends SQLiteOpenHelper {

    public static final String TABLA_USUARIOS = "Create table Usuario(id integer primary key, usuario text, contraseña text, nombre text, apellido text,correo text, rut text,caracteristicas text)";
    public static final String TABLA_PETICION = "Create table Peticiones(id integer primary key, id_usuario text, latitud text, longitud text, descripcion text, tipo text, estado text)";

   // public static final String SQL_CREATE_TABLE = "CREATE `usuario` VARCHAR(100) NOT NULL , `contrasOT NULL , `apellido` VARCHAR(100) NOT NULL , cas` VARCHAR(100) NOT NULL, PRIMARY KEY (`id`)) ENGINE = InnoDB;)";

    public Conexion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_USUARIOS);
        db.execSQL(TABLA_PETICION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists Usuarios");
        //db.execSQL("Create table Usuarios(id integer primary key, usuario text, contraseña text, nombre text, apellido text,correo text, rut text,caracteristicas text)");
    }




}
