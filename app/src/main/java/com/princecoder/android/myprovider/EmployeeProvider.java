package com.princecoder.android.myprovider;

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
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by prinzlyngotoum on 12/1/14.
 */
public class EmployeeProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.princecoder.android.myprovider";
    public static final String URL = "content://" + PROVIDER_NAME + "/employee";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String POSITION = "position";


    private static HashMap<String, String> mMapProjection;

    static final int EMPLOYEE = 1;
    static final int EMPLOYEE_ID = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "employee", EMPLOYEE);
        uriMatcher.addURI(PROVIDER_NAME, "employee/#", EMPLOYEE_ID);
    }


    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        EmployeeDatabaseHelper dbHelper = new EmployeeDatabaseHelper(context);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(EmployeeDatabaseHelper.EMPLOYEE_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case EMPLOYEE:
                qb.setProjectionMap(mMapProjection);
                break;
            case EMPLOYEE_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on employee names
             */
            sortOrder = NAME;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            /**
             * Get all employee records
             */
            case EMPLOYEE:
                return "vnd.android.cursor.dir/vnd.com.princecoder.employee";
            /**
             * Get a particular employee
             */
            case EMPLOYEE_ID:
                return "vnd.android.cursor.item/vnd.com.princecoder..employee";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new employee record
         */
        long rowID = db.insert(EmployeeDatabaseHelper.EMPLOYEE_TABLE_NAME, "", values);
        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case EMPLOYEE:
                count = db.delete(EmployeeDatabaseHelper.EMPLOYEE_TABLE_NAME, selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(EmployeeDatabaseHelper.EMPLOYEE_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case EMPLOYEE:
                count = db.update(EmployeeDatabaseHelper.EMPLOYEE_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case EMPLOYEE_ID:
                count = db.update(EmployeeDatabaseHelper.EMPLOYEE_TABLE_NAME, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
