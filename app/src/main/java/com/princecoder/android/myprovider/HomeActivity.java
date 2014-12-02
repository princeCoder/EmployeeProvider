package com.princecoder.android.myprovider;

import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.princecoder.android.myprovider.Adapters.EmployeeCursorAdapter;
import com.princecoder.android.myprovider.Presenter.EmployeePresenter;

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
public class HomeActivity extends ActionBarActivity implements android.view.View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, IView {


    //Ui elements
    private ListView mListview;
    private Button mBtnAdd;
    private EditText mEdtName;
    private EditText mEdtPosition;

    //Adapter
    private EmployeeCursorAdapter mAdapter;

    //Presenter of the Application
    private EmployeePresenter mEmployeePresenter;

    //Context of the Application
    private Context mContext;

    //Loaders
    private final int mLoader = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext=getApplicationContext();

        //Initialize the presenter
        mEmployeePresenter = new EmployeePresenter(this,mContext);

        //get UI elements
        getUiElements();

        //Initialize the adapter
        mAdapter = new EmployeeCursorAdapter(mContext, null, 0);

        //Initialize loader
        getLoaderManager().initLoader(mLoader, null, this);
    }

    //get UI elements
    private void getUiElements() {
        mListview = (ListView) findViewById(R.id.listviewItem);
        mEdtName = (EditText) findViewById(R.id.txtName);
        mEdtPosition = (EditText) findViewById(R.id.txtTitle);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(android.view.View v) {
        mEmployeePresenter.addEmployee(getEdtName().getText().toString(), getEdtPosition().getText().toString());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mEmployeePresenter.getEmployeeCursor();
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


    // Getters
    public EditText getEdtPosition() {
        return mEdtPosition;
    }

    public ListView getListview() {
        return mListview;
    }

    public EditText getEdtName() {
        return mEdtName;
    }


    @Override
    public void setEdtPosition(String value) {
        this.mEdtPosition.setText(value);
    }

    @Override
    public void setEdtName(String value) {
        this.mEdtName.setText(value);
    }
}
