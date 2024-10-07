package com.ob.rfi;

import java.util.Date;


import com.ob.rfi.db.RfiDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DprScreen extends CustomTitle {

	private String[] schemId;
	
	
	private Spinner schemeSpin;
	private EditText corridor;
	private EditText stage;
	private EditText location;
	private EditText description;
	private EditText quantity;
	private EditText observation;
	private EditText remark;
	
	private Button okBtn;
	private Button nextBtn;

	private String errorMessage;
	private RfiDatabase db;

	private String dpr_Date;
	private String d_stage;
	private String d_structure;
	private String d_location;
	private String d_qunt;
	private String d_desc;
	private String d_obsr;
	private String d_remark;
	private String d_Date;


	private TextView sr_no;


	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dpr_screen);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		
		/************object from dpr_screen*************************/
		sr_no=(TextView)findViewById(R.id.dpr_sr);
		schemeSpin = (Spinner) findViewById(R.id.dpr_select_scheme);
		corridor = (EditText) findViewById(R.id.dpr_structure);
		stage = (EditText) findViewById(R.id.dpr_stage);
		location = (EditText) findViewById(R.id.dpr_location);
		description= (EditText) findViewById(R.id.dpr_description);
		quantity = (EditText) findViewById(R.id.dpr_quantity);
		observation = (EditText) findViewById(R.id.dpr_observation);
		remark = (EditText) findViewById(R.id.dpr_remark);
		
		
		okBtn=(Button)findViewById(R.id.dpr_select_submit);
		nextBtn=(Button)findViewById(R.id.dpr_select_next);
		
		/*******************database object**************************/
		db = new RfiDatabase(getApplicationContext());
		if (db.userId.equalsIgnoreCase("")) {
			logout();
		} else {
			setSchemeSpinnerData();
		}
		
		
		
		/**************next button action*************/
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!dpr_validation())
				{
					Log.d("Validation", "Error");
				}
				else
				{
				savetodatabase();
				refresh();
				finish();
				startActivity(new Intent(DprScreen.this,
						DprScreen.class));
				}
			}
		});
		
		  
		/**************submit button action*************/
		
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!dpr_validation())
				{
					Log.d("Validation", "Error");
				}
				else
				{
				savetodatabase();
				finish();
				startActivity(new Intent(DprScreen.this,
						HomeScreen.class));
				}
			}
		});
		
	}

	
	
	
	public boolean dpr_validation()
	{
		boolean validate = false;
		
		if (schemeSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Project";	
			displayErrorDialog("Error",errorMessage);
			}
	else if(corridor.getText().length()==0){
		corridor.setError("Please Enter Corridor or Structure"); 
		}
	else if(stage.getText().length()==0){
		stage.setError("Please Enter Stage"); 
		}
	else if(location.getText().length()==0){
		location.setError("Please Enter Location");
		}
	else if(description.getText().length()==0){
		description.setError("Please Enter Description"); 
		}	
	else if(quantity.getText().length()==0){
		quantity.setError("Please Enter Quantity");
		}
	else if(observation.getText().length()==0){
		observation.setError("Please Enter observation"); 
		}
	else if(remark.getText().length()==0){
		remark.setError("Please Enter Remark"); 
		}	
	else	
		validate =true;
		System.out.println("validation");
		return validate;
	}
	
	
	
	
private void refresh() {
	// TODO Auto-generated method stub
	
	schemeSpin.setSelection(0);
	 corridor.setText("");
	 stage.setText("");
	 location.setText("");
	 description.setText("");
	 quantity.setText("");
	 observation.setText("");
	 remark.setText("");

}
	
	
	
	
	@SuppressWarnings("deprecation")
	public void displayErrorDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}
	
	
	private void logout() {
		finish();
		Toast.makeText(DprScreen.this, "Session expired... Please login.",
				Toast.LENGTH_SHORT).show();
		db.closeDb();
		Intent logout = new Intent(DprScreen.this, LoginScreen.class);
		logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//CqraDatabase.copyDatabase(getApplicationContext(), "CqraDatbase.db");
		startActivity(logout);
	}

	
	
	
	private void setSchemeSpinnerData() {
		String where = "s.PK_Scheme_ID = q.proj_id AND s.user_id='" + db.userId
				+ "'";
		Cursor cursor = db.select("Scheme as s, question as q",
				"distinct(q.proj_id), s.Scheme_Name", where, null, null, null,
				null);

		schemId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				schemId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			items[0] = "Scheme(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		schemeSpin.setAdapter(adapter);

		schemeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> aview, View view,
					int position, long rowid) {
				if (position > 0) {
					db.selectedSchemeId = schemId[position - 1];
					
				} else {
					db.selectedSchemeId = "";
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	
	
	

private void savetodatabase() {
	// TODO Auto-generated method stub
	
	DateFormat df = new DateFormat();
	d_Date = (String) df.format("yyyy-MM-dd", new Date());
	
	d_stage=stage.getText().toString();
	d_structure=corridor.getText().toString();
	d_location=location.getText().toString();
	d_desc=description.getText().toString();
	d_qunt=quantity.getText().toString();
	d_obsr=observation.getText().toString();
	d_remark=remark.getText().toString();
	
	
			String dpr_data = "'" + db.selectedSchemeId + "','"
					+ d_structure + "','" + d_stage + "','"
					+ d_location + "','" +d_desc+ "','" + d_qunt
					+ "','" + d_obsr + "','" +d_remark + "','" +d_Date+ "','"
					 + db.userId + "'";
	db.insert("dpr_table",
		"dpr_scheme_Id,dpr_structure_name,dpr_stage,dpr_location,dpr_description,dpr_quantity,dpr_obsrvtn,dpr_remark,dpr_date,dpr_user_id",	dpr_data);			
			
			
}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
		Intent backscreen=new Intent(DprScreen.this,HomeScreen.class);
		startActivity(backscreen);
		
	//	super.onBackPressed();
	}
	
}
