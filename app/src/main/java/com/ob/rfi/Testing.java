package com.ob.rfi;

import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

public class Testing extends CustomTitle{

	private String method;
	private String[] param;
	private String[] value;
	private RfiDatabase db;




	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		db = new RfiDatabase(getApplicationContext());
		
		updateData();
	}
	
	
	
	
	public void updateData() {
		//requestid = 1;
		method = "getListForModify";
		param = new String[] { "userID" };
		value = new String[] { db.userId };
		callService(); 
	}  
	
	protected void callService() {
		Webservice service = new Webservice(Testing.this, network_available, "Loading.. Please wait..",
				method, param, value);
		service.setdownloadListener(new downloadListener(){
			@Override
			public void dataDownloadedSuccessfully(String data) {
				//if(requestid == 1)
				
				
				System.out.println("success data====="+data);
				//	saveData(data);

				
			} 
			@Override
			public void dataDownloadFailed() {
				displayDialog("Error", "Problem in connection.");
			}
			@Override
			public void netNotAvailable() {
				displayDialog("Error", "No network connection.");				
			}
		});
		service.execute("");
	}
	
	
	
	
	
	protected void displayDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		if (message.equals("Problem in connection.")) {
			alertDialog.setButton("Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alertDialog.setButton2("Try again",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					//retry();
				}
			});
		} else {
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		alertDialog.show();
	}
	
	
	
	

	
}
