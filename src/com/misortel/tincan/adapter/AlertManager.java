package com.tincan.tincan.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class AlertManager {
	public AlertManager() {
		super();
	}

	public void showAlert(Context context, String message) {
		AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setMessage(message);
		alert.show();
	}

	public void showAlert(Context context, String title, String message,
			Boolean stats) {
		AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setTitle(title);
		alert.setMessage(message);
		alert.show();
	}

	public void showToast(Context context, String message, int duration) {
		Toast.makeText(context, message, duration).show();
	}
	
	public AlertDialog showAlert(Context context, String title, String message) {
		AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setTitle(title);
		alert.setMessage(message);
		return alert;
	}
}