package com.princecoder.android.myprovider.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


//This class represent the contract between The content resolver and the content Provider
public class EmployeeContract {

    //The Scheme
    public static final String SCHEME = "content://";

    // The content Authoirity
    public static final String CONTENT_AUTHORITY = "com.princecoder.android.myprovider";

    // The base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + "" + CONTENT_AUTHORITY);

    //Path to the table Employee
    // This help the provider to know where to find the table employee
    // It will be in our case something like content://com.princecoder.android.myprovider/employee
    public static final String PATH_EMPLOYEE = "employee";


    public static class EmployeeEntry implements BaseColumns {

        // I define the base location of the table employee using an Uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EMPLOYEE).build();


        // To query multiple employee
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEE;

        //To query one row in employee
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EMPLOYEE;

        // The table name
        public static final String TABLE_NAME = "employee";

        // Employee name
        public static final String COLUMN_NAME = "name";

        // Employee title
        public static final String COLUMN_TITLE = "title";

        //Get Employee with Name
        public static Uri getEmployeeWithName(String name) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_NAME, name).build();
        }

        //Get Employee with ID
        public static Uri getEmployeeWithId(String id) {
            return CONTENT_URI.buildUpon().appendQueryParameter(_ID, id).build();

        }


    }


}
