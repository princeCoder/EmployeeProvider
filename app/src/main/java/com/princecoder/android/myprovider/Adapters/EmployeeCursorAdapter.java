package com.princecoder.android.myprovider.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.princecoder.android.myprovider.R;
import com.princecoder.android.myprovider.data.EmployeeContract;


public class EmployeeCursorAdapter extends CursorAdapter {

    private LayoutInflater mInflater;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public EmployeeCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        // TODO Auto-generated constructor stub
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.getName().setText(cursor.getString(holder.getNameIndex()));
        holder.getPosition().setText(cursor.getString(holder.getPositionIndex()));
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup
            parent) {
        View view = mInflater.inflate
                (R.layout.contacts_list_item, null);

        ViewHolder holder = new ViewHolder(view, cursor);
        // Save data for each single view element


        view.setTag(holder);
        return view;
    }

}

/**
 * View holder for optimize the creation of the listview rows
 */
class ViewHolder {

    private TextView mName;
    private TextView mPosition;
    private int mNameIndex;
    private int mPositionIndex;

    public ViewHolder(View view, Cursor cursor) {


        this.mName = (TextView) view.findViewById(R.id.employee_name);
        this.mPosition = (TextView) view.findViewById(R.id.employee_position);
        this.mNameIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_NAME);
        this.mPositionIndex = cursor.getColumnIndex(EmployeeContract.EmployeeEntry.COLUMN_TITLE);

    }

    public TextView getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName.setText(name);
    }

    public TextView getPosition() {
        return mPosition;
    }

    public void setPosition(String position) {
        this.mPosition.setText(position);
    }

    public int getNameIndex() {
        return mNameIndex;
    }

    public int getPositionIndex() {
        return mPositionIndex;
    }

}