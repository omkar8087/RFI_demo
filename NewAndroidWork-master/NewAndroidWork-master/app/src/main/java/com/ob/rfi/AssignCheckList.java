package com.ob.rfi;

import java.util.Timer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ob.rfi.R;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

@SuppressWarnings("static-access")
public class AssignCheckList extends CustomTitle {

	public static final String TAG = "AssignCheckList"; 
	private Spinner schemeSpin;
	private Spinner buildingSpin;
	private Spinner floorSpin;
	private Spinner tradeSpin;
	private ListView checkList;
	private TextView selectAllLable;
	private CheckBox selectallCheckBox;
	private RfiDatabase db;
	private String[] schemeId;
	private String[] buildingId;
	private String[] floorId;
	private String[] tradId;
	private String[] checklistId;
	private SparseBooleanArray selecteditem;
	private String errorMessage;
	String method;
	String[] param;
	String[] value; 
	int requestid = 0;
	private Button submitButton;

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.checklist_assign);
		//title.setText("CQRA Check list");

		selectAllLable = (TextView) findViewById(R.id.select_all_text);
		selectallCheckBox = (CheckBox) findViewById(R.id.check_all);
		submitButton = (Button) findViewById(R.id.submit_button);
		Button cancelButton = (Button) findViewById(R.id.cancl_button);
		schemeSpin = (Spinner) findViewById(R.id.scheme_spinner);
		buildingSpin = (Spinner) findViewById(R.id.building_spinner);
		floorSpin = (Spinner) findViewById(R.id.floor_spinner);
		tradeSpin = (Spinner) findViewById(R.id.trade_spinner);
		checkList = (ListView) findViewById(R.id.check_list_selection);
		db = new RfiDatabase(getApplicationContext());

		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				selecteditem=checkList.getCheckedItemPositions();
				if (validateScreen()) {
					displayDialog("Error", errorMessage);
				}else
					downloadCheckList();
				insertTradeMatch();
			}
		});

		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			} 
		});

		selectallCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				String lable = "";
				lable = isChecked ? "Deselect All" : "Select All";
				selectAllLable.setText(lable);
			}
		});

		selectallCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkedAll(selectallCheckBox.isChecked());
			}
		});

		checkList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long rowid) {
				selecteditem = checkList.getCheckedItemPositions();
				if (selectallCheckBox.isChecked()) {
					if (!selecteditem.valueAt(index)) {
						selectallCheckBox.setChecked(false);
					}
				} else {
					if (selecteditem.indexOfValue(false) < 0) {
						selectallCheckBox.setChecked(true);
					}
				}
			}
		});

		if(!db.userId.equalsIgnoreCase("")) {
		if(isDataAvialable("checklist")){
			setSchemeSpinnerData();
		}else {
			updateData();
		}
		disableList(false);
		} else {
			logout();
		}
	}

	public void insertTradeMatch() {
		// TODO Auto-generated method stub

		System.out.println("In insertTradeMatch===");

		String TM_data = "'" + db.selectedTradeId + "','"+ db.selectedFloorId + "','" + db.userId + "'";
		db.insert("trade_match","trade_id,floor_id,user_id ", TM_data);
		System.out.println("TM data====="+TM_data.toString());
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(db.userId.equalsIgnoreCase("")) {
			//logout();
		}
	}
	
	private void logout() {
		finish();
		Toast.makeText(AssignCheckList.this, "Session expired... Please login.", Toast.LENGTH_SHORT).show();
		Intent logout = new Intent(AssignCheckList.this, LoginScreen.class);
		logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(logout);
	}


	/**
	 * Checking table is empty or not
	 * @param table
	 * @return
	 */
	private boolean isDataAvialable(String table) {
		
		Cursor cursor = db.select(table, "count(*) as noitem", "user_id='"+db.userId+"'", null, null, null, null);
		if (cursor.moveToFirst()) {
			if (Integer.parseInt(cursor.getString(0)) > 0) {
				Log.d(table, cursor.getString(0));
				cursor.close();
				return true;
			} else {
				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Check all element in list 
	 * @param isChecked
	 */
	private void checkedAll(boolean isChecked) {
		if (checkList.isEnabled()) {
			int count = checkList.getAdapter().getCount() > 0 ? checkList.getAdapter().getCount() : 0;
			int i = 0;
			while (count > i) {
				checkList.setItemChecked(i, isChecked);
				i++;
			}
		}
	}

	/**
	 * Update data form network 
	 */
	public void updateData() {
		requestid = 1;
		method = "getDetails";
		param = new String[] { "user_id" };
		value = new String[] { db.userId };
		callService();
	}

	public void saveData(String data) {
		if(data.equalsIgnoreCase("No Record Found")){
			displayDialog("No Record Found", "(Note: You may not have the updated data.)");
		}else{
 
			try{
				String[] tabledata = data.split("\\$"); 

				String column = "PK_scheme_ID, Scheme_Name, user_id";//project
				saveToDatabase("Scheme", column, tabledata[0],true,3);

				column = "FK_Scheme_ID, Bldg_ID, Bldg_Name,No_Floor,user_id";//Structure or building
				saveToDatabase("Building", column, tabledata[1],true,5);
				
				column = "FK_Scheme_Id,Fk_Building_Id,floor_Id, floor_Name, user_id";//floor or stage
				saveToDatabase("floor", column, tabledata[2],true,5);

				/*column = "FK_Scheme_Id,Fk_Building_Id,Fk_Trade_Id, PK_checklist_Id, Checklist_Name,Fk_Floor_Id, user_id"; //checklist
				saveToDatabase("Checklist", column, tabledata[3],true,5);*/
 
				column = "PK_Trade_ID, Trade_Name,PK_Scheme_ID,FK_Building_Id,FK_floor_ID,user_id";//trade
				saveToDatabase("Trade", column, tabledata[3],true,6);

				column = "checklist_id, subgroup_id, subgroup_name, user_id"; //subgroup
				saveToDatabase("subgroup", column, tabledata[4],true,4);

				column = "q_group_id, q_group_name, user_id";
				saveToDatabase("question_group", column, tabledata[5],true,3);//question group
				
				System.out.println("Qoestion heading data ==== >>>>> "+ tabledata[6].toString());
				if(!tabledata[7].equalsIgnoreCase("NONE")){
					column = "q_heading_id,q_heading_text, user_id";
					saveToDatabase("question_heading", column, tabledata[6],true,3);
				} 
				 
				if(!tabledata[8].equalsIgnoreCase("NS")){
					column = "scheme_Id, structure_id, contractor_id,supervisor_id,trade_id,supervisor_name,contractor_name,user_id";
					saveToDatabase("scheme_superviser", column, tabledata[8],true,7);
					
					column = "scheme_Id, structure_id,contractor_id,s_f_id,trade_id,s_f_name,contractor_name,user_id";
					saveToDatabase("contractor_sf", column, tabledata[8],true,7);
					System.out.println("tablellllllllllllllllllllllll");
				}
				           
				if(!tabledata[9].equalsIgnoreCase("NF")){
					column = "scheme_Id, structure_id, contractor_id,foreman_id,trade_id,foreman_name,contractor_name,user_id";
					saveToDatabase("scheme_foreman", column, tabledata[9],true,7);
					
					column = "scheme_Id, structure_id,contractor_id,s_f_id,trade_id,s_f_name,contractor_name,user_id";
					saveToDatabase("contractor_sf", column, tabledata[9],true,7);
					System.out.println("table lenth=========="+column.length()
							);
				} 
				
				//client data
				column = "scheme_Id,structure_id,trade_id,client_staf_id,client_name,user_id";
				saveToDatabase("client_staff", column, tabledata[10],true,5);
				
				System.out.println("client data=========="+column.length());

				/*column = "q_type_id,q_type_text,q_type_desc, user_id";
				saveToDatabase("question_type", column, tabledata[7],true,4);

			 	column = "severity_id,mild,moderate,severe,very_severe,exstream, user_id";
				saveToDatabase("severity", column, tabledata[8],true,7);*/
 
				String tempdata="0~Other";
				column = "q_heading_id,q_heading_text, user_id";
				saveToDatabase("question_heading", column, tempdata,true,3); 

				setSchemeSpinnerData();
			}catch(Exception e ){
				Log.d(TAG,e.toString());
				e.printStackTrace();
				flushData();
				displayDialog("No Record Found", "Sufficient Data is not available.");
			}
		}
	}

	private void flushData() {
		db.delete("Checklist", "user_id='"+db.userId+"'");
		db.delete("Scheme", "user_id='"+db.userId+"'");
		db.delete("Building", "user_id='"+db.userId+"'");
		db.delete("Trade", "user_id='"+db.userId+"'");
		db.delete("subgroup", "user_id='"+db.userId+"'");
		db.delete("floor", "user_id='"+db.userId+"'");
		//db.delete("question_type", "user_id='"+db.userId+"'");
		db.delete("question_heading", "user_id='"+db.userId+"'");
		db.delete("question_group", "user_id='"+db.userId+"'");
		db.delete("question", "user_id='"+db.userId+"'");
		db.delete("userMaster", "Pk_User_ID='"+db.userId+"'");
		db.update("sqlite_sequence", "seq=0", "name = 'question'");
	}

	public void saveToDatabase(String table, String columns, String respose, boolean adduserId, int colCnt) {
		Cursor cursor = db.select(table, columns, "user_id='"+db.userId+"'", null, null, null, null);
		String existingData = "";
		if (cursor.moveToFirst()) {
			for (int i = 0; i < colCnt; i++) {
				existingData += "'"+cursor.getString(i)+"',";
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		String[] rowdata = respose.split("\\|");
		for (int len = 0; len < rowdata.length; len++) {
			String[] singlerow = rowdata[len].split("~");
			StringBuffer values = new StringBuffer("'");
			for (int i = 0; i < ( singlerow.length - 1); i++) {
				values.append( singlerow[i] + "','");
			}
			values.append( singlerow[ singlerow.length - 1 ] + "'");
			if(adduserId)
				values.append( ",'" + db.userId + "'");

			if( !existingData.contains(values.toString()))
				db.insert(table, columns, values.toString());
		}

	}

	
	
	
	
	
	
	public void saveToDatabaseContra(String table, String columns, String respose, boolean adduserId, int colCnt) {
		Cursor cursor = db.select(table, columns, "user_id='"+db.userId+"'", null, null, null, null);
		String existingData = "";
		if (cursor.moveToFirst()) {
			for (int i = 0; i < colCnt; i++) {
				existingData += "'"+cursor.getString(i)+"',";
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		String[] rowdata = respose.split("\\|");
		for (int len = 0; len < rowdata.length; len++) {
			String[] singlerow = rowdata[len].split("~");
			StringBuffer values = new StringBuffer("'");
			for (int i = 0; i < ( singlerow.length - 1); i++) {
				values.append( singlerow[i] + "','");
			}
			values.append( singlerow[ singlerow.length - 1 ] + "'");
			if(adduserId)
				values.append( ",'" + db.userId + "'");

			if( !existingData.contains(values.toString()))
				db.insert(table, columns, values.toString());
		}

	}
	
	public void saveToDatabaseSuperviser(String table, String columns, String respose, boolean adduserId, int colCnt) {
		Cursor cursor = db.select(table, columns, "user_id='"+db.userId+"'", null, null, null, null);
		String existingData = "";
		if (cursor.moveToFirst()) {
			for (int i = 0; i < colCnt; i++) {
				existingData += "'"+cursor.getString(i)+"',";
			}
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		String[] rowdata = respose.split("\\|");
		for (int len = 0; len < rowdata.length; len++) {
			String[] singlerow = rowdata[len].split("~");
			StringBuffer values = new StringBuffer("'");
			for (int i = 0; i < ( singlerow.length - 1); i++) {
				values.append( singlerow[i] + "','");
			}
			values.append( singlerow[ singlerow.length - 1 ] + "'");
			if(adduserId)
				values.append( ",'" + db.userId + "'");

			if( !existingData.contains(values.toString()))
				db.insert(table, columns, values.toString());
		}

	}

	
	
	
	public void saveQuestions(String table, String col, String respose, boolean adduserId) {
		String[] rowdata = respose.split("\\|");
		String columns = col;
		for (int len = 0; len < rowdata.length; len++) {
			String[] singlerow = rowdata[len].split("~");
			int i;
			StringBuffer values = new StringBuffer("'"+db.selectedSchemeId+"','"+db.selectedBuildingId+"','"+
					db.selectedFloorId+"',");
			for (i = 0; i < ( singlerow.length - 1); i++) {
				
				values.append(DatabaseUtils.sqlEscapeString(singlerow[i]) + ",");
			}
			values.append( "'"+singlerow[ singlerow.length - 1 ] + "','");
			values.append( db.userId + "'");
			System.out.println("row data column="+values.toString());
			db.insert(table, columns, values.toString());
		}
	}

	
	
	
	/**********************my code*******************************************/
	
	public void saveChecklist1(String table, String col, String respose, boolean adduserId) {
		String[] rowdata = respose.split("\\|");
		String columns = col;
		
	//	db.delete("Checklist", "user_id='" + db.userId + "'");
		for (int len = 0; len < rowdata.length; len++) {
			String[] singlerow = rowdata[len].split("~");
			int i;
			/*StringBuffer values = new StringBuffer("'"+db.selectedSchemeId+"','"+db.selectedBuildingId+"','"+
					db.selectedTradeId+"',");
					
			*/
			StringBuffer values = new StringBuffer();
			for (i = 0; i < ( singlerow.length - 1); i++) {
				System.out.println("imserting");
				values.append(DatabaseUtils.sqlEscapeString(singlerow[i]) + ",");
			}
			values.append( "'"+singlerow[ singlerow.length - 1 ] + "','");
			values.append( db.userId + "'");
			
		System.out.println("before insert");
			db.insert(table, columns, values.toString());
			System.out.println("after insert");
		}
	}

	
	
	
	 
	
	
	
	
	
	
	
	
	
	protected void displayDialog(final String title, final String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);

		if(message.equals("Problem in connection.")){
			alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			alertDialog.setButton2("Try again", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					retry();
				}
			}); 
		} else {
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					if(title.equalsIgnoreCase("No Record Found")){
						onBackPressed();
					}
				}
			});
		}
		alertDialog.show();
	}

	protected void retry() {
		callService();
	}

	private void setSchemeSpinnerData() { 
		Cursor cursor = db.select("Scheme", "distinct(PK_Scheme_ID),Scheme_Name", "user_id='"+db.userId+"'", null, null, null, null);
		schemeId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {
			do {
				schemeId[cursor.getPosition()] = cursor.getString(cursor
						.getColumnIndex("PK_Scheme_ID"));
				items[cursor.getPosition() + 1] = cursor.getString(cursor
						.getColumnIndex("Scheme_Name"));
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

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Log.d("POSTOINN",""+position);
				if (position > 0) {
					db.selectedSchemeId = schemeId[position-1];
					setBuildSpinnerData();
				} else {
					buildingSpin.setSelection(0);
					buildingSpin.setEnabled(false);
					floorSpin.setSelection(0);
					floorSpin.setEnabled(false);
					tradeSpin.setSelection(0);
					tradeSpin.setEnabled(false);
					disableList(false);
				}
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}

	protected void setBuildSpinnerData() {
		Cursor cursor = db.select( "Building", "distinct(Bldg_ID),Bldg_Name", "FK_Scheme_ID='"
				+ db.selectedSchemeId + "' AND user_id='"+db.userId+"'", null, null, null, null);
		buildingId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {
			do {
				buildingId[cursor.getPosition()] = cursor.getString(cursor
						.getColumnIndex("Bldg_ID"));
				items[cursor.getPosition() + 1] = cursor.getString(cursor
						.getColumnIndex("Bldg_Name"));
			} while (cursor.moveToNext());
		} else {
			items[0] = "Building(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AssignCheckList.this, android.R.layout.simple_spinner_item,	items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingSpin.setAdapter(adapter);

		buildingSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position > 0) {
					db.selectedBuildingId = buildingId[position-1];
					setFloorSpinnerData();
//					fillChecklist();
//					checkedAll(true);
//					checkedAll(false);
//					selectallCheckBox.setChecked(false);
				} else {
					floorSpin.setSelection(0);
					floorSpin.setEnabled(false);
					disableList(false);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		buildingSpin.setEnabled(true);
	}
	
	protected void setFloorSpinnerData() {
		Cursor cursor = db.select( "floor", "distinct(floor_Id),floor_Name", "FK_Scheme_ID='"
			+ db.selectedSchemeId + "'AND FK_Building_ID='" + db.selectedBuildingId + 
			"' AND user_id='"+db.userId+"'", null, null, null, null);
		floorId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {
			do {
				floorId[cursor.getPosition()] = cursor.getString(cursor
						.getColumnIndex("floor_Id"));
				items[cursor.getPosition() + 1] = cursor.getString(cursor
						.getColumnIndex("floor_Name"));
			} while (cursor.moveToNext());
		} else {
			items[0] = "Stages(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				AssignCheckList.this, android.R.layout.simple_spinner_item,	items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		floorSpin.setAdapter(adapter);

		floorSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position > 0) {
					db.selectedFloorId = floorId[position-1];
					setTradeSpinnerData();
					tradeSpin.setEnabled(true);
					callchecklist();
				} else {
					tradeSpin.setSelection(0);
					tradeSpin.setEnabled(false);
					disableList(false);
				
				}
			}
 
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		floorSpin.setEnabled(true); 
	} 
	
	
	private void setTradeSpinnerData() {
		String where = "b.Bldg_ID=t.FK_Building_Id AND f.floor_Id=t.FK_floor_ID AND t.PK_Scheme_ID='" + db.selectedSchemeId + "' AND t.Fk_Building_Id='"
		+ db.selectedBuildingId + "' AND t.Fk_Floor_Id='"+db.selectedFloorId+"' AND t.user_id='"+db.userId+"'" ;
		
		System.out.println("where1 ->"+where);
		Cursor cursor = db.select("Trade as t,Building as b,floor as f", "distinct(t.PK_Trade_ID),t.Trade_Name",
				where, null, null, null,"t.Trade_Name");
		
		//db.select(TABLE_NAME, COLUMNS, WHERE, SELECTION_ARGS, GROUP_BY, HAVING, OREDER_BY)
		
		
		System.out.println("cursor length"+cursor.getCount());
		/*String where = "c.Fk_Trade_Id=t.PK_Trade_ID AND c.FK_Scheme_ID='" + db.selectedSchemeId + "' AND c.Fk_Building_Id='"
				+ db.selectedBuildingId + "' AND c.Fk_Floor_Id='"+db.selectedFloorId+"' AND c.user_id='"+db.userId + "' AND t.user_id=c.user_id ";
				System.out.println("where ->"+where);
			
		Cursor cursor = db.select( "Trade", "distinct(t.PK_Trade_ID),t.Trade_Name", "Bldg_ID='"
				+ db.selectedBuildingId + "'AND floor_Id='" + db.selectedFloorId + 
				"' AND user_id='"+db.userId+"'", null, null, null, null);*/
		 
		
		 
		
		
		
		/*System.out.println("lenth of the cursor==========="+cursor.getCount());*/
		
		
		
				/*String where = "PK_Scheme_ID='"
				+ db.selectedSchemeId + "' AND user_id='"+db.userId+"'";
				Cursor cursor = db.select("Trade as t", "distinct(t.PK_Trade_ID),t.Trade_Name",
						where, null, null, null, null);*/
		
		tradId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				tradId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			items[0] = "Trade(s) not available";
		}
 
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tradeSpin.setAdapter(adapter);

		tradeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> aview, View view,
					int position, long rowid) {
				if (position > 0) {
					 				
					db.selectedTradeId = tradId[position-1];
					System.out.println("trade id selected======="+db.selectedTradeId);
		/*******************call service with trade id**********************************************/
					//callchecklist();
					fillChecklist();
					checkedAll(true);
					checkedAll(false);
					selectallCheckBox.setChecked(false);
					
				} else {
					disableList(false);
				}
				
			}

			

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	
	
	
public void loadlist() {
	
	Timer t = new Timer();
		
}
	
	
	
	
	
	
	
	
	
	public void fillChecklist() {

		String where ="c.FK_Scheme_ID='"+ db.selectedSchemeId + "' AND c.Fk_Building_Id='"+ db.selectedBuildingId +
			"' AND Fk_Floor_Id='"+db.selectedFloorId+"' AND Fk_Trade_Id='"+db.selectedTradeId+"' AND c.user_id='"+db.userId+"' AND c.PK_checklist_Id not IN " +
			"(select distinct(checklist_id) from question where proj_id='"+	db.selectedSchemeId+
			"' AND bldg_id='"+db.selectedBuildingId+"' AND floor_id='"+db.selectedFloorId+"' AND user_id='"+db.userId+"'  ) ";
//		System.out.println(" where == "+ where);

		Cursor cursor = db.select("Checklist as c", "distinct(c.PK_checklist_Id), c.checklist_Name", 
				where, null, null, null, null);
		checklistId = new String[cursor.getCount()];
		String[] item = new String[cursor.getCount()];

		if (cursor.moveToFirst()) {
			do {
				checklistId[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("PK_checklist_Id"));
				item[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex("Checklist_Name"));

			} while (cursor.moveToNext());
		}else{
			disableList(false);
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (item.length > 0) {
			checkList.setAdapter(new ArrayAdapter<String>(this,	android.R.layout.simple_list_item_multiple_choice, item));
			checkList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			disableList(true);
			checkedAll(true);
			checkedAll(false);
		}
	}  

	protected boolean validateScreen() {
		boolean validate = true;
		if (schemeSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Project";
		} else if (buildingSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Structure";
		} else if (floorSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Stage";
		} else if (!checkList.isEnabled()) {
			errorMessage = "No Checklist found";
		} else if (selecteditem.size() < 1 || selecteditem.indexOfValue(true)< 0) {
			errorMessage = "Please Select Checklist ";
		} else
			validate = false;

		return validate;
	}

	protected synchronized void callService() {
		Webservice service = new Webservice(AssignCheckList.this, network_available, "Loading.. Please wait..",
				method, param, value);
		service.setdownloadListener(new downloadListener(){
			@Override
			public void dataDownloadedSuccessfully(String data) {
				if(requestid == 1)
					saveData(data);

				if(requestid == 2)
					saveCheckList((String)data);
				
				/*if(requestid == 4)
					saveCheckListtable((String)data); 
					System.out.println("hiiiiiiiiiiiii");*/
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


	
	
	protected void callService1() {
		Webservice service = new Webservice(AssignCheckList.this, network_available, "Loading.. Please wait..",
				method, param, value);
		service.setdownloadListener(new downloadListener(){
			@Override
			public void dataDownloadedSuccessfully(String data) {
				
				//db.delete("Checklist", "user_id='" + db.userId + "'");
				if(requestid == 4)
					saveCheckListtable((String)data); 
					System.out.println("hiiiiiiiiiiiii");
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void downloadCheckList() {
		StringBuilder selecteChecklistid = new StringBuilder();
		for (int i = 0; i < selecteditem.size(); i++) {
			if(selecteditem.get(i))
				selecteChecklistid.append(checklistId[(int)i]+"," );
		}
		selecteChecklistid.deleteCharAt(selecteChecklistid.length()-1);
		Log.d(TAG,selecteChecklistid.toString());
//		String schemeSelected = schemeId[schemeSpin.getSelectedItemPosition() - 1];
//		String buildingSelected = buildingId[buildingSpin.getSelectedItemPosition() - 1];
		requestid = 2;
		method = "questionDetails";
		param = new String[] { "scheme_id","building_id","floor_id","checklist_id" };
		System.out.println("1="+db.selectedSchemeId+" 2="+db.selectedBuildingId+" 3= "+
				db.selectedFloorId+" 4="+selecteChecklistid.toString());
		value = new String[] { db.selectedSchemeId, db.selectedBuildingId, db.selectedFloorId, 
				selecteChecklistid.toString() };
		callService();
	}

	
	
	
	public void callchecklist() {
		StringBuilder selecteChecklistid = new StringBuilder();	
		String trade_id=db.selectedTradeId;
	
		Log.d(TAG,selecteChecklistid.toString());
		requestid = 4;
		method = "Checklist";  //name should be change
		param = new String[] { "scheme_id","building_id","floor_id","user_id" };
		System.out.println("1="+db.selectedSchemeId+" 2="+db.selectedBuildingId+" 3= "+
				db.selectedFloorId);
		value = new String[] { db.selectedSchemeId, db.selectedBuildingId, db.selectedFloorId, 
				db.userId };
		callService1();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void saveCheckList(String response) {
		if (!response.trim().equals("NONE")) {
			String column = "proj_id, bldg_id,floor_id, checklist_id, q_id, q_subgrp_id, q_grp_id, q_type_id, q_heading_id, " +
				"q_severity_id, q_text, q_min_observation, completed_observations, opt1, opt2, opt3, opt4, opt5, " +
				"opt6,user_id" ;
			saveQuestions("question", column, response, true);
			
			System.out.println("question data+"+response);
			finish();
			Intent i =new Intent(AssignCheckList.this, HomeScreen.class);
			i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
		}
	}
	
	
	public void saveCheckListtable(String response) {
		if (!response.trim().equals("NONE")) {
			String column = "FK_Scheme_Id,Fk_Building_Id,Fk_Trade_Id, PK_checklist_Id, " +
			"Checklist_Name, Fk_Floor_Id, user_id";
			/*saveQuestions("question", column, response, true);*/
			
			
			column = "FK_Scheme_Id,Fk_Building_Id,Fk_Trade_Id, PK_checklist_Id, Checklist_Name,Fk_Floor_Id, user_id"; //checklist
			/*saveToDatabase("Checklist", column, response,true);
			
			savec("question", column, response, true);*/
			saveChecklist1("Checklist", column, response, true);
			
			System.out.println("in checklist table response"+response);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "No CheckList Available", Toast.LENGTH_LONG).show();
		}
		
	}
	

	private void disableList(boolean isenable) {
		if (isenable) {
			selectallCheckBox.setEnabled(isenable);
			checkList.setEnabled(isenable);
		} else {
			checkList.setAdapter(new ArrayAdapter<String>(AssignCheckList.this,   
					android.R.layout.simple_list_item_1, (new String[] { "NO CheckList" })));
			checkList.setEnabled(isenable);
			selectallCheckBox.setChecked(isenable);
			selectallCheckBox.setEnabled(isenable);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		Intent i =new Intent(AssignCheckList.this, HomeScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	@Override
	protected void onDestroy() {
		if(db!=null){
			db.closeDb();
		}
		super.onDestroy();
	}
}
