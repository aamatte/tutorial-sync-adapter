package com.example.andres.myapplication.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.example.andres.myapplication.Persistence.DatabaseContract;
import com.example.andres.myapplication.Persistence.StudentsDbHelper;

/*
   Clase que extiende ContentProvider y que interactua con la base de datos
 */
public class StudentsProvider extends ContentProvider {

    public static final int STUDENT_LIST = 1;
    public static final int STUDENT_ID = 2;

    private static final UriMatcher sUriMatcher;

    static{

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
            URI para todos los estudiantes.
            Se setea que cuando se pregunta a UriMatcher por la URI:
            content://com.example.andres.myapplication.provider/students
            se devuelva un entero con el valor de 1.
         */
        sUriMatcher.addURI(StudentsContract.AUTHORITY, "students", STUDENT_LIST);

        /*
            URI para un estudiante.
            Se setea que cuando se pregunta a UriMatcher por la URI:
            content://com.example.andres.myapplication.provider/students/#
            se devuelva un entero con el valor de 2.
         */
        sUriMatcher.addURI(StudentsContract.AUTHORITY, "students/#", STUDENT_ID);

    }

    /*
        Instancia de StudentsDbHelper para interactuar con la base de datos
     */
    private StudentsDbHelper mDbHelper;

    public StudentsProvider() { }

    @Override
    public boolean onCreate() {
        mDbHelper = StudentsDbHelper.getInstance(getContext());
        return true;
    }

    /*
        Llamado para borrar una o más filas de una tabla
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rows = 0;

        switch (sUriMatcher.match(uri)) {

            case STUDENT_LIST:
                // Se borran todas las filas
                rows = db.delete(DatabaseContract.Students.TABLE_NAME, null, null);
                break;
            case STUDENT_ID:
                // Se borra la fila del ID seleccionado
                rows = db.delete(DatabaseContract.Students.TABLE_NAME, selection, selectionArgs);
        }
        // Se retorna el número de filas eliminadas
        return rows;
    }

    /*
        Se determina el MIME Type del dato o conjunto de datos al que apunta la URI
     */
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case STUDENT_LIST:
                return StudentsContract.URI_TYPE_STUDENT_DIR;
            case STUDENT_ID:
                return StudentsContract.URI_TYPE_STUDENT_ITEM;
            default:
                return null;
        }
    }

    /*
        Inserta nuevo estudiante
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(DatabaseContract.Students.TABLE_NAME, null, values);

        // Le avisa a los observadores
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    /*
        Retorna el o los datos que se le pida de acuerdo a la URI
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)){

            // Se pide la lista completa de estudiantes
            case STUDENT_LIST:

                // Si no hay un orden especificado,
                // lo ordenamos de manera ascendente de acuerdo a lo que diga el contrato
                if (sortOrder == null || TextUtils.isEmpty(sortOrder)) sortOrder = StudentsContract.StudentsColumns.DEFAULT_SORT_ORDER;
                break;

            // Se pide un estudiante en particular
            case STUDENT_ID:
                // Se adjunta la ID del estudiante selecciondo en el filtro de la seleccion
                if (selection == null)
                    selection = "";
                selection = selection + "_ID = " + uri.getLastPathSegment();
                break;

            // La URI que se recibe no está definida
            default:
                throw new IllegalArgumentException(
                        "Unsupported URI: " + uri);
        }

        Cursor cursor = db.query(DatabaseContract.Students.TABLE_NAME,
                                 projection,
                                 selection,
                                 selectionArgs,
                                 null,
                                 null,
                                 sortOrder);
        // Se retorna un cursor sobre el cual se debe iterar para obtener los datos
        return cursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // No se implemento un update
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
