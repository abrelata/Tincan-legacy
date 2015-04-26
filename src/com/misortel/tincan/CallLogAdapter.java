package com.tincan.tincan;

import android.content.Context;
import android.database.Cursor;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CallLogAdapter extends BaseAdapter{
	private Context mContext;
	Cursor cursor;
	
	private CallLogAdapter(Context context, Cursor cursor){
		super();
		mContext = context;
		this.cursor = cursor;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.logs_row, null);
		
		cursor.moveToPosition(position);
		
		//String _name = cursor.getString(cursor.getColumnIndex(arg0))
		return view;
	}
	
}
