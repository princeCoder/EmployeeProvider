package com.princecoder.android.myprovider.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.princecoder.android.myprovider.data.EmployeeContract.EmployeeEntry;

import java.util.HashMap;


public class EmployeeProvider extends ContentProvider {

    private static final int EMPLOYEE = 1;
    private static final int EMPLOYEE_ID = 2;
    private static final int EMPLOYEE_NAME = 3;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static HashMap<String, String> mMapProjection;
    private Context mContext;
    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;


    static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = EmployeeContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, EmployeeContract.PATH_EMPLOYEE, EMPLOYEE);
        matcher.addURI(authority, EmployeeContract.PATH_EMPLOYEE + "/*", EMPLOYEE_NAME);
        matcher.addURI(authority, EmployeeContract.PATH_EMPLOYEE + "/#", EMPLOYEE_ID);
        return matcher;
    }



    @Override
    public boolean onCreate() {
        mContext = getContext();
        EmployeeDatabaseHelper dbHelper = new EmployeeDatabaseHelper(mContext);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(EmployeeEntry.TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case EMPLOYEE:
                qb.setProjectionMap(mMapProjection);
                break;
            case EMPLOYEE_ID:
                qb.appendWhere(EmployeeEntry._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            /**
             * By default sort on employee names
             */
            sortOrder = EmployeeEntry.COLUMN_NAME;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(mContext.getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            //Get all employee records
            case EMPLOYEE:
                return EmployeeEntry.CONTENT_TYPE;
            //Get a particular employee
            case EMPLOYEE_ID:
                return EmployeeEntry.CONTENT_ITEM_TYPE;
            case EMPLOYEE_NAME:
                return EmployeeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new employee record
         */
        long rowID = db.insert(EmployeeEntry.TABLE_NAME, "", values);
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(EmployeeEntry.CONTENT_URI, rowID);
            mContext.getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;

        switch (sUriMatcher.match(uri)) {
            case EMPLOYEE:
                count = db.delete(EmployeeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(EmployeeEntry.TABLE_NAME, EmployeeEntry._ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;

        switch (sUriMatcher.match(uri)) {
            case EMPLOYEE:
                count = db.update(EmployeeEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                count = db.update(EmployeeEntry.TABLE_NAME, values, EmployeeEntry._ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        mContext.getContentResolver().notifyChange(uri, null);
        return count;
    }
}
