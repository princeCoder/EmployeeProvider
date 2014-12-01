package com.princecoder.android.myprovider;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.princecoder.android.myprovider.Adapters.EmployeeCursorAdapter;

/**
 * @author prinzlyngotoum
 *         <p/>
 *         This App consume data from a content provider named EmployeeProvider that you will find the code in the same package.
 *         I use that content to add a new Employee in the database.
 *         <p/>
 *         We also have a listview which display the list of employee present in the database. For that, I use an optimized Custom CursorAdapter
 *         which bind data in the listview using a cursor loader. really important because whenever I add a new employee, the loader get notified
 *         and we receive the data imediatelly in the listview.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Home extends ActionBarActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {


    ListView mListview;
    Button mBtnAdd;
    EditText mEdtName;
    EditText mEdtPosition;
    EmployeeCursorAdapter mAdapter;

    //Loaders
    private final int mLoader = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mListview = (ListView) findViewById(R.id.listviewItem);
        mEdtName = (EditText) findViewById(R.id.txtName);
        mEdtPosition = (EditText) findViewById(R.id.txtTitle);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);

        mAdapter = new EmployeeCursorAdapter(getApplicationContext(), null, 0);

        //Initialize loader
        getLoaderManager().initLoader(mLoader, null, this);
    }

    @Override
    public void onClick(View v) {

        // Add an Employee
        ContentValues values = new ContentValues();

        values.put(EmployeeProvider.NAME,
                ((EditText) findViewById(R.id.txtName)).getText().toString());

        values.put(EmployeeProvider.POSITION,
                ((EditText) findViewById(R.id.txtTitle)).getText().toString());
        getContentResolver().insert(
                EmployeeProvider.CONTENT_URI, values);

        // Notify the user that the data has been added
        Toast.makeText(getBaseContext(),
                "New Employee added !!!", Toast.LENGTH_LONG).show();

        // reset the fields

        mEdtPosition.setText("");
        mEdtName.setText("");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String URL = EmployeeProvider.URL;
        Uri EmployeeURI = Uri.parse(URL);
        return new CursorLoader(
                this,
                EmployeeURI,
                null,
                null, null, EmployeeProvider.NAME
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new EmployeeCursorAdapter(getApplicationContext(), data, 0);
        mListview.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
