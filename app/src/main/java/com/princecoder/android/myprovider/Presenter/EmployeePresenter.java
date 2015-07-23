package com.princecoder.android.myprovider.Presenter;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import com.princecoder.android.myprovider.IView;
import com.princecoder.android.myprovider.data.EmployeeContract;

public class EmployeePresenter implements IEmployeePresenter {

  public IView mHome;
  Context mContext;

  public EmployeePresenter(IView home, Context c){
      this.mHome=home;
      this.mContext=c;
  }

    @Override
    public void addEmployee(String name, String title) {
        // Add an Employee
        ContentValues values = new ContentValues();

        values.put(EmployeeContract.EmployeeEntry.COLUMN_NAME,
                name);
        values.put(EmployeeContract.EmployeeEntry.COLUMN_TITLE, title);

        mContext.getContentResolver().insert(
                EmployeeContract.EmployeeEntry.CONTENT_URI, values);

        // Notify the user that the data has been added
        Toast.makeText(mContext,
                "New Employee added !!!", Toast.LENGTH_LONG).show();

        // reset the fields
        mHome.setEdtName("");
        mHome.setEdtPosition("");
    }

    /**
     *
     * @return a CursorLoader containing the list of employees
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public CursorLoader getEmployeeCursor(){
        //String URL = EmployeeProvider.URL;
        Uri EmployeeURI = EmployeeContract.EmployeeEntry.CONTENT_URI;
        return new CursorLoader(
                mContext,
                EmployeeURI,
                null,
                null, null, EmployeeContract.EmployeeEntry.COLUMN_NAME
        );
    }
}
