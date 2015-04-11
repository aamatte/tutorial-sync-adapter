package com.example.andres.myapplication.Persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by andres on 02-04-15.
 */
public final class DatabaseManagement {
    public DatabaseManagement() {
    }

    public static abstract class Students implements BaseColumns {
        // TABLE STUDENTS
        public static final String TABLE_NAME = "STUDENTS";
        public static final String COLUMN_NAME_STUDENT_NAMES = "names";
        public static final String COLUMN_NAME_FIRST_LASTNAME = "firstlastname";
        public static final String COLUMN_NAME_SECOND_LASTNAME = "secondlastname";


        public static final String TEXT_TYPE = " TEXT";
        public static final String COMMA_SEP = ",";


        public static final String SQL_CREATE_STUDENTS_TABLE =
                "CREATE TABLE " + Students.TABLE_NAME + " (" +
                        Students._ID + " INTEGER PRIMARY KEY," +
                        Students.COLUMN_NAME_STUDENT_NAMES + TEXT_TYPE + COMMA_SEP +
                        Students.COLUMN_NAME_FIRST_LASTNAME + TEXT_TYPE + COMMA_SEP +
                        Students.COLUMN_NAME_SECOND_LASTNAME + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_STUDENTS =
                "DROP TABLE IF EXISTS " + Students.TABLE_NAME;





        public static class StudentsDbHelper extends SQLiteOpenHelper {

            public static final int DATABASE_VERSION = 1;
            public static final String DATABASE_NAME = "Students.db";

            private static StudentsDbHelper sInstance;

            private StudentsDbHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            public static synchronized StudentsDbHelper getInstance(Context context) {

                if (sInstance == null) {
                    sInstance = new StudentsDbHelper(context.getApplicationContext());
                }
                return sInstance;
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_CREATE_STUDENTS_TABLE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL(SQL_DELETE_STUDENTS);
                onCreate(db);
            }



        }
    }
}






