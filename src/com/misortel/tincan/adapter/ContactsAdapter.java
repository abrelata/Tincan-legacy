package com.misortel.tincan.adapter;

import java.util.List;

import com.misortel.tincan.Contacts;
import com.misortel.tincan.R;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactsAdapter extends ArrayAdapter<Contacts>{
	
	private Activity activity;
	private List<Contacts> contact;
	private Contacts obj;
	private int row;
	
	public ContactsAdapter(Activity activity, int row, List<Contacts> contact) {
		super(activity, row, contact);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.contact = contact;
		this.row = row;		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if ((contact == null) || ((position + 1) > contact.size()))
			return view;

		obj = contact.get(position);

		holder.tvName = (TextView) view.findViewById(R.id.tvName);
		holder.tvPhoneNo = (TextView) view.findViewById(R.id.tvPhone);

		if (holder.tvName != null && null != obj.getName()
				&& obj.getName().trim().length() > 0) {
			holder.tvName.setText(Html.fromHtml(obj.getName()));
		}
		if (holder.tvPhoneNo != null && null != obj.getPhoneNo()
				&& obj.getPhoneNo().trim().length() > 0) {
			holder.tvPhoneNo.setText(Html.fromHtml(obj.getPhoneNo()));
		}
	
		return view;	
	}
	
	public class ViewHolder {
		public TextView tvName, tvPhoneNo;
	}
}
