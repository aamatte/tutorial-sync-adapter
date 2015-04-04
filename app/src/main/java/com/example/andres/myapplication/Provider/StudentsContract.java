package com.example.andres.myapplication.Provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andres on 04-04-15.
 */
public final class StudentsContract {
    public static final String AUTHORITY = "com.example.andres.myapplication.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
            "/vnd.com.example.andres.provider.students";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
            "/vnd.com.example.andres.provider.students";

    public static final class Columns implements BaseColumns{

        private Columns(){}

        public static final Uri CONTENT_URI = Uri.withAppendedPath(StudentsContract.CONTENT_URI, "/students");

        public static final String NAMES = "names";
        public static final String FIRST_LASTNAME = "firstlastname";
        public static final String SECOND_LASTNAME = "secondlastname";

        public static final String[] PROJECTION_ALL = {_ID, NAMES, FIRST_LASTNAME, SECOND_LASTNAME};

        public static final String DEFAULT_SORT_ORDER = FIRST_LASTNAME + " ASC";


    }

}
