package com.ob.rfi;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ob.rfi.db.RfiDatabase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.SparseBooleanArray; 
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class mockupScreen extends CustomTitle {

	private Vibrator vibrator;
	private Spinner schemeSpin;
	private Spinner buildingSpin;
	private Spinner tradeSpin;
 
	private Spinner contractorspin;
	private Spinner approvespin;
	private Button okBtn; 
	private Button backBtn;
	private RfiDatabase db;

	private String[] schemId;
	private String[] tradeId;

	private String[] bldgId;
	private SparseBooleanArray selecteditem;
	private String[] contractorId;
	private String[] contractorId1;
	private String[] contractorNewId;
	private String[] contractor;
	private String[] contid;

	private String[] m_approve;
	private ListView clientlist;
	private ListView cont_foremn;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter arrayAdapter;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter arrayAdapter1;
	private ScrollView sv;
	private EditText editmock;
	private EditText location;
	private EditText remark;
	String tempFilename;
	File sdImageMainDirectory;
	String[] snaps = new String[] { "", "" };
	String approve1 =""; 
	private int m_imageCount = 0;
	private Button imageButton;
	private String timestamp;
	private String ansDate;
	//private CheckBox selectallCheckBox;
	//private TextView selectAllLable;
	private String mockup_Date;
	private String errorMessage;
	private Spinner supervisorspin;
	private Spinner foremanspin;
	private Spinner clientspin;
	private String[] supervisorId;
	private String[] supervisor;
	private String[] foremanId;
	private String[] foreman;
	private String[] clientId;
	private String[] client;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mockup);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		//selectAllLable = (TextView) findViewById(R.id.clientpersontext);
		editmock = (EditText) findViewById(R.id.mockup_given_by);
		location = (EditText) findViewById(R.id.mockup_location);

		remark = (EditText) findViewById(R.id.mockup_remark);
		editmock.setFocusableInTouchMode(true);
		editmock.requestFocus();
		location.setFocusableInTouchMode(true);
		location.requestFocus();
		remark.setFocusableInTouchMode(true);
		remark.requestFocus();

		sv = (ScrollView) findViewById(R.id.scroll);
		// sv.scrollTo(0, sv.getBottom());
		sv.scrollTo(5, 10);

		schemeSpin = (Spinner) findViewById(R.id.mockup_select_scheme);
		buildingSpin = (Spinner) findViewById(R.id.mockup_select_building);
		tradeSpin = (Spinner) findViewById(R.id.mockup_select_trade);

		contractorspin = (Spinner) findViewById(R.id.mockup_contractor);
		approvespin = (Spinner) findViewById(R.id.mockup_approve);
		
		supervisorspin = (Spinner) findViewById(R.id.mockup_supervisor);
		foremanspin = (Spinner) findViewById(R.id.mockup_forman);
		clientspin= (Spinner) findViewById(R.id.mockup_client);
		
		
		
		//superspin = (Spinner) findViewById(R.id.mockup_approve);

		//clientlist = (ListView) findViewById(R.id.client_person);
		//cont_foremn = (ListView) findViewById(R.id.contractor_foreman);
		//cont_foremn.setVisibility(View.INVISIBLE);
		//clientlist.setVisibility(View.INVISIBLE);

		//selectallCheckBox = (CheckBox) findViewById(R.id.chk_clientp);
		//selectallCheckBox.setVisibility(View.INVISIBLE);
		okBtn = (Button) findViewById(R.id.mockup_select_submit);
		backBtn = (Button) findViewById(R.id.mockup_select_Back);

		init();
		db = new RfiDatabase(getApplicationContext());
		if (db.userId.equalsIgnoreCase("")) {
			logout();
		} else {
			setSchemeSpinnerData();
		}

		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (validation()) {
					displayErrorDialog("Error", errorMessage);
				} else {
					savetodatabase();
					finish();
					startActivity(new Intent(mockupScreen.this,
							HomeScreen.class));
				}
			}
		});

	}

	private void logout() {
		finish();
		Toast.makeText(mockupScreen.this, "Session expired... Please login.",
				Toast.LENGTH_SHORT).show();
		db.closeDb();
		Intent logout = new Intent(mockupScreen.this, LoginScreen.class);
		logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		db.copyDatabase(getApplicationContext(), "CqraDatbase.db");
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
					buildingSpin.setClickable(true);
					// foremanspin.setSelection(0);
					// foremanspin.setClickable(false);
					contractorspin.setSelection(0);
					contractorspin.setClickable(false);
					tradeSpin.setSelection(0);
					tradeSpin.setClickable(false);
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false);
					

					// ClientPerson(schemId[position - 1]);
					setBulidngSpinnerData(schemId[position - 1]);
				} else {
					db.selectedSchemeId = "";
					db.selectedBuildingId = "";
					buildingSpin.setSelection(0);
					buildingSpin.setClickable(false);
					// foremanspin.setSelection(0);
					// foremanspin.setClickable(false);
					contractorspin.setSelection(0);
					contractorspin.setClickable(false);
					tradeSpin.setSelection(0);
					tradeSpin.setClickable(false);
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false);

				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setBulidngSpinnerData(final String schemeid) {
		String where = "b.FK_Scheme_ID = q.proj_id AND b.Bldg_ID=q.bldg_id AND q.proj_id='"
				+ db.selectedSchemeId
				+ "' "
				+ "AND q.user_id='"
				+ db.userId
				+ "' AND b.user_id='" + db.userId + "'";

		Cursor cursor = db.select("Building as b, question as q",
				" distinct(q.bldg_id), b.Bldg_Name,b.No_Floor", where, null,
				null, null, null);

		bldgId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];

		items[0] = "--Select--";
		if (cursor.moveToFirst()) {
			do {
				bldgId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);

			} while (cursor.moveToNext());
		} else {
			items[0] = "Building(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buildingSpin.setAdapter(adapter);

		buildingSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position > 0) {
					db.selectedBuildingId = bldgId[position - 1];

					System.out.println("selecteed bdg id"
							+ db.selectedBuildingId);

					setTradeSpinnerData(schemeid, bldgId[position - 1]);

					contractorspin.setSelection(0);
					contractorspin.setClickable(false);
					tradeSpin.setSelection(0);
					tradeSpin.setClickable(true);
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false);

				} else {
					db.selectedBuildingId = "";
					db.selectedChecklistId = "";
					db.selectedSubGroupId = "";

					tradeSpin.setSelection(0);
					tradeSpin.setClickable(false);

					contractorspin.setSelection(0);
					contractorspin.setClickable(false);
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false);

				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setTradeSpinnerData(final String schemid,
			final String bulidingid) {
		/*String where = "c.Fk_Trade_Id=t.PK_Trade_ID AND tm.trade_id=t.PK_Trade_ID AND c.FK_Scheme_ID='"
				+ schemid + "' AND c.Fk_Building_Id='" + bulidingid
				+ "' AND c.user_id='" + db.userId
				+ "' AND t.user_id=c.user_id ";*/
		
		String where = "c.Fk_Trade_Id=t.PK_Trade_ID AND tm.trade_id=t.PK_Trade_ID AND tm.trade_id=c.Fk_Trade_Id AND tm.floor_id=c.Fk_Floor_Id AND c.FK_Scheme_ID='"
				+ schemid + "' AND c.Fk_Building_Id='" + bulidingid
				 
				+ "' AND c.user_id='" + db.userId 
				+ "' AND t.user_id=c.user_id ";
		
		System.out.println("where for trade===== ->" + where);
		Cursor cursor = db.select("Trade as t,Checklist as c,trade_match as tm",
				"distinct(t.PK_Trade_ID),t.Trade_Name", where, null, null,
				null,"t.Trade_Name");

		tradeId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {
 
			do {
				tradeId[cursor.getPosition()] = cursor.getString(0);
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
					db.selectedTradeId = tradeId[position - 1];

					setContractorSpinnerData(schemid, bulidingid,
							tradeId[position - 1]);
					contractorspin.setSelection(0);
					contractorspin.setClickable(true);
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false);

				} else {
					contractorspin.setSelection(0);
					contractorspin.setClickable(false);
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false);

				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setContractorSpinnerData(final String schemeid,
			final String buidingid, final String tradeid) {
		
		
		String whereNew = "scheme_Id='" + schemeid + "' And structure_id='"
				+ buidingid + "'And trade_id='" + tradeid + "' AND csf.user_id='"
				+ db.userId + "'";
		
		System.out.println("contractor New query=======================>" + whereNew);
		
		Cursor cursorNew = db.select("contractor_sf as csf",
				"distinct(csf.contractor_id), csf.contractor_name", whereNew, null,
				null, null, null);
		
		
		contractorNewId = new String[cursorNew.getCount()];  
		String[] itemsNew = new String[cursorNew.getCount() + 1];
		itemsNew[0] = "--Select--";
		if (cursorNew.moveToFirst()) {

			do {
				db.contr = true;
				contractorNewId[cursorNew.getPosition()] = cursorNew.getString(0);
				System.out.println(" new id="+cursorNew.getString(0).toString());
				itemsNew[cursorNew.getPosition() + 1] = cursorNew.getString(1);
				System.out.println("New Name="+cursorNew.getString(1).toString());
			} while (cursorNew.moveToNext());
		} else {
			itemsNew[0] = "Contractor not allocated";
			db.contractor_flag = true;
			db.contractor_flag = true;
		}

		if (cursorNew != null && !cursorNew.isClosed()) {
			cursorNew.close();
		}
		
		
		

		String where = "scheme_Id='" + schemeid + "' And structure_id='"
				+ buidingid + "'And trade_id='" + tradeid + "' AND s.user_id='"
				+ db.userId + "'";
		
		String where1 = "scheme_Id='" + schemeid + "' And structure_id='"
				+ buidingid + "'And trade_id='" + tradeid + "' AND f.user_id='"
				+ db.userId + "'";
		/*
		 * + "' AND q.trade_id=.trade_id AND c.user_id='" + db.userId +
		 * "' AND q.scheme_Id=c.scheme_Id AND q.structure_id=c.structure_id ";
		 */

		System.out.println("contractor query=======================>" + where);

		Cursor cursor = db.select("scheme_superviser as s",
				"distinct(s.contractor_id), s.contractor_name", where, null,
				null, null, null);
		
		
		Cursor cursor2 = db.select("scheme_foreman as f",
				"distinct(f.contractor_id), f.contractor_name", where1, null,
				null, null, null);
		
		System.out.println("heyyyyyyyyyy=="+cursor2.getCount());
		
		contractorId = new String[cursor.getCount()];  
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				db.contr = true;
				contractorId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			items[0] = "Contractor not allocated";
			db.contractor_flag = true;
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		
		
		
		contractorId1 = new String[cursor2.getCount()]; 
		String[] items1 = new String[cursor2.getCount() + 1];
		items1[0] = "--Select--";
		if (cursor2.moveToFirst()) {

			do {
				//db.contr = true;
				
				
				contractorId1[cursor2.getPosition()] = cursor2.getString(0);
				items1[cursor2.getPosition() + 1] = cursor2.getString(1);
			} while (cursor2.moveToNext());
		} else {
			items1[0] = "Contractor not allocated";
			db.contractor_flag = true;
		}

		if (cursor2 != null && !cursor2.isClosed()) {
			cursor2.close();
		}

		System.out.println("query=="+items1.length);
		
		String[] st=findUnion(items,items1);
		Set<String> mySet = new HashSet<String>(Arrays.asList(st));
	
		
		List<String> yourList = new ArrayList();  
	    yourList.addAll(mySet); 
	    Collections.reverse(yourList); 
	    
	    String [] countries = yourList.toArray(new String[yourList.size()]);
		//mySet.remove("--Select--");
	    String[] Citems3=countries;	
	    
	    
		String[] GPXFILES1 = mySet.toArray(new String[0]);
		String[] dj=GPXFILES1;
	//	String[] Citems3=GPXFILES1;
		
		String[] sp=findUnion(contractorId,contractorId1);
		Set<String> mySet1 = new HashSet<String>(Arrays.asList(sp));
		
		
		
		
		
		String[] GPXFILES11 = mySet1.toArray(new String[0]);
		 contid=GPXFILES11;
		 
		
		 
		 
		// List<String> stringList = new ArrayList<String>(Arrays.asList(dj));
		 
		 
		 
		
		
		System.out.println("array size=="+Citems3);
		//contid=findUnion(contractorId,contractorId1);
		
		System.out.println("array size id=="+contid);
		
		

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, itemsNew);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		contractorspin.setAdapter(adapter);

		contractorspin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position > 0) {
					contractorspin.setClickable(true);
					db.selectedContractorId = contractorNewId[position - 1];
					 setSupervisorSpinnerData(schemeid, buidingid, tradeid,contractorId[position - 1]);
					 setForemanData(schemeid, buidingid, tradeid,contractorId[position - 1]);
					 setClientData(schemeid, buidingid, tradeid,contractorId[position - 1]);
					setapprovespinnerdata();
					approvespin.setSelection(0);
					approvespin.setClickable(true);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(true);
					foremanspin.setSelection(0);
					foremanspin.setClickable(true);
					clientspin.setSelection(0);
					clientspin.setClickable(true);
					
					/*
					 * setSubgrpSpinnerData(db.selectedContractorId);
					 * 
					 * 
					 * tradeSpin.setClickable(true); setSupervisorSpinnerData();
					 * setForemanData();
					 */} else {
					db.selectedContractorId = "";
					approvespin.setSelection(0);
					approvespin.setClickable(false);
					supervisorspin.setSelection(0);
					supervisorspin.setClickable(false);
					foremanspin.setSelection(0);
					foremanspin.setClickable(false);
					clientspin.setSelection(0);
					clientspin.setClickable(false); 
					
					/*
					 * db.selectedSubGroupId = ""; subgrpSpin.setSelection(0);
					 * subgrpSpin.setClickable(false);
					 */}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	
	private void setSupervisorSpinnerData(String schemeid, String buidingid,
			final String tradeid, String contractor_id) {
		/*
		 * String where = "scheme_id='" + db.selectedSchemeId +
		 * "' AND structure_id='" + db.selectedBuildingId
		 * +"' AND trade_id='"+tradeid+"' AND user_id='"+db.userId+"'";
		 */
 
		
		
		
		String where = "scheme_id='" + schemeid + "' AND structure_id='"
				+ buidingid + "' AND trade_id='" + tradeid
				+ "' AND contractor_id='" + contractor_id + "' AND user_id='"
				+ db.userId + "'";
		System.out.println(" Supervisor where ->" + where);

		Cursor cursor = db.select("scheme_superviser as sp",
				"distinct(sp.supervisor_id),sp.supervisor_name", where, null,
				null, null, null);

		supervisorId = new String[cursor.getCount() + 3];
		supervisor = new String[cursor.getCount() + 3];

		supervisor[0] = "--select--";
		supervisor[1] = "Not present";
		supervisorId[1] = "0";
		
		
		supervisor[2] = "Approved";
		supervisorId[2] = "1";
		db.selectedSupervisorId = "1";
		
		
		/*if(db.supervisor_flag)
		{
			db.supervisor_flag = false;
			System.out.println("flag set");
			 
		}*/
		
		if (cursor.moveToFirst()) {
 
			do {
				supervisorId[cursor.getPosition() + 3] = cursor.getString(0);
				
				supervisor[cursor.getPosition() + 3] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			supervisor[1] = "Supervisor not allocated";
			supervisorId[1] = "0";
			db.selectedSupervisorId = "0";
			/*db.checkalert_s = true;
			db.supervisor_flag = true;*/

		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}

		
		/*if(supervisor[2].contains("Approved") ||supervisor[1].contains("Supervisor not allocated"))
		{
			db.supervisor_flag = false;
		}*/
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, supervisor);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		supervisorspin.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		supervisorspin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> aview, View view,
					int position, long rowid) {
				
				if (position >= 1) {

					db.selectedSupervisorId = supervisorId[position];
					db.selectedSupervisor = supervisor[position];
					db.selectedsuprvsname=supervisorspin.getSelectedItem().toString();
					System.out.println("selected supervisor id="+supervisorId[position]+" "+supervisor[position]);
				} else {
					db.selectedSupervisorId = "";
					
					db.selectedsuprvsname=supervisorspin.getSelectedItem().toString();
					// disableList(false);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	
	private void setForemanData(String schemeid, String buidingid,
			final String tradeid, String contractor_id) {

		
		
		int myNum = 0;

		try {
		    myNum = Integer.parseInt(contractor_id.toString());
		    myNum=myNum+1;
		} catch(NumberFormatException nfe) {
		   System.out.println("Could not parse " + nfe);
		} 
		
		 String cont_id=String.valueOf(myNum);
		
		
		String where = "scheme_id='" + schemeid + "' AND structure_id='"
				+ buidingid + "' AND trade_id='" + tradeid
				+ "' AND contractor_id='" + contractor_id + "' AND user_id='"
				+ db.userId + "'";

		// String where = "scheme_id='" + db.selectedSchemeId +
		// "' AND structure_id='"
		// + db.selectedBuildingId
		// +"' AND trade_id='"+db.selectedTradeId+"' AND user_id='"+db.userId+"'";
		System.out.println(" foreman where -==========>" + where);
		Cursor cursor = db.select("scheme_foreman as fm",
				"distinct(fm.foreman_id),fm.foreman_name", where, null, null,
				null, null);

		foremanId = new String[cursor.getCount() + 3];
		foreman = new String[cursor.getCount() + 3];

		foreman[0] = "--select--";
		foreman[1] = "Not present";
		foremanId[1] = "0";

		
		foreman[2] = "Approved";
		foremanId[2] = "1";
		db.selectedForemanId = "1";
		
		
		
		
		if (cursor.moveToFirst()) {

			do {
				System.out.println("nnnnnnnnnnnnnnn");
				foremanId[cursor.getPosition() + 3] = cursor.getString(0);
				foreman[cursor.getPosition() + 3] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			foreman[1] = "Foreman not allocated";
			foremanId[1] = "0";
			db.selectedForemanId = "0";
			db.checkalert_f = true;
			db.foreman_flag = true;
		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, foreman);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		foremanspin.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		foremanspin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> aview, View view,
					int position, long rowid) {
				System.out.println("position" + position);
				if (position >= 1) {
						
					
					db.selectedforemanname=foremanspin.getSelectedItem().toString();
					db.selectedForemanId = foremanId[position];
					db.selectedForeman = foreman[position];
					db.isforman=true;
					// fillChecklist();
					// checkedAll(true);
					// checkedAll(false);
					// selectallCheckBox.setChecked(false);
				} else {
					db.selectedForemanId = "";
					db.selectedforemanname=foremanspin.getSelectedItem().toString();
					// disableList(false);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	
	
	
	
	
	private void setClientData(String schemeid, String buidingid,
			final String tradeid, String contractor_id) {

		
		
		
		
		String where = "scheme_id='" + schemeid + "' AND structure_id='"
				+ buidingid + "' AND trade_id='" + tradeid +"' AND user_id='"
						+ db.userId + "'";

		System.out.println("cleint where -==========>" + where);
		Cursor cursor = db.select("client_staff as ct",
				"distinct(ct.client_staf_id),ct.client_name", where, null, null,
				null, null);

		clientId = new String[cursor.getCount() + 3];
		client = new String[cursor.getCount() + 3];

		client[0] = "--select--";
		client[1] = "Not present";
		clientId[1] = "0";

		
		client[2] = "Approved";
		clientId[2] = "1";
		db.selectedForemanId = "1";
		
		
		if (cursor.moveToFirst()) {

			do {
				System.out.println("nnnnnnnnnnnnnnn");
				clientId[cursor.getPosition() + 3] = cursor.getString(0);
				client[cursor.getPosition() + 3] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			client[1] = "Foreman not allocated";
			clientId[1] = "0";
			db.selectedForemanId = "0";
			/*db.checkalert_f = true;
			db.foreman_flag = true;*/
		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, client);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		clientspin.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		clientspin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> aview, View view,
					int position, long rowid) {
				System.out.println("position" + position);
				if (position >= 1) {
						
					
					db.selectedclientname=clientspin.getSelectedItem().toString();
					db.selectedClientId = clientId[position];
					db.selectedClient = client[position];
					db.isclient=true;             //for red and green alert
					
			} else {
					db.selectedClientId = "";
					db.selectedclientname=clientspin.getSelectedItem().toString();
					
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	
	

	private String[] unionArrays(String[] items, String[] items1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public static String[] findUnion(String[] items, String[] items1) {
		 
        //
        //TreeSet<String> hashedArray = new TreeSet<String>();
        ArrayList<String> cont=new ArrayList<String>();
        for (String entry : items) {
        	cont.add(entry);
        }
 
        for (String entry : items1) {
        	cont.add(entry);
        }
 
        return cont.toArray(new String[0]);
    }
	

	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setForemanData(String schemeid, String buidingid,
			final String tradeid, String contractor_id) {

		String where = "scheme_id='" + schemeid + "' AND structure_id='"
				+ buidingid + "' AND trade_id='" + tradeid
				+ "' AND contractor_id='" + contractor_id + "' AND user_id='"
				+ db.userId + "'";

		System.out.println("where -==========>" + where);
		Cursor cursor = db.select("scheme_superviser as fm",
				"fm.supervisor_name", where, null, null, null, null);

		String[] items = new String[cursor.getCount() + 1];
		String[] items1 = new String[cursor.getCount() + 1];
		if (cursor.moveToFirst()) {
			do {
				items[cursor.getPosition() + 0] = cursor.getString(0);
			} while (cursor.moveToNext());
		} else {
			items[0] = "foreman not available";
		}

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < items.length; ++i) {
			list.add(items[i]);
			System.out.println("print1");
		}

		String[] approve = { "Yes", "No" };
		arrayAdapter1 = new ArrayAdapter(this,
		android.R.layout.simple_list_item_multiple_choice, approve);
		cont_foremn.setAdapter(arrayAdapter1);
		cont_foremn.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

	}*/

/*	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void ClientPerson(String schemeid) {
		String where = "scheme_id='" + schemeid + "'";
		Cursor cursor = db.select("client as c", "c.client_name", where, null,
				null, null, null);

		String[] items = new String[cursor.getCount() + 1];

		if (cursor.moveToFirst()) {

			do {

				items[cursor.getPosition() + 0] = cursor.getString(0);
			} while (cursor.moveToNext());
		} else {
			items[0] = "CList(s) not available";
		}

		cursor.close();
		
		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < items.length; ++i) {
			list.add(items[i]);
		}

		System.out.println("client" + items.length);
		arrayAdapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_multiple_choice, list);
		clientlist.setAdapter(arrayAdapter);
		clientlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		// Helper.getListViewSize(clientlist);
		// Helper.setListViewHeightBasedOnChildren(clientlist);

	}
*/
	public void setapprovespinnerdata() {
		String[] approve = { "Yes", "No" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, approve);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		approvespin.setAdapter(adapter);

		approvespin.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
		
				if (position > 0) {		
					approve1="N";					
				} else {
					approve1="Y";		
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void savetodatabase() { 

		
		DateFormat df = new DateFormat();
		mockup_Date = (String) df.format("yyyy-MM-dd", new Date());
		String m_given = editmock.getText().toString();
		String loc = location.getText().toString();
		String rem = remark.getText().toString();
		String str1="m_";
		
		
		
		
		String com = str1.concat(snaps[0]);
		 String com1 = str1.concat(snaps[1]);
		System.out.println("com=================================="+com);
		String mockupdata = "'" + db.selectedSchemeId + "','"
				+ db.selectedBuildingId + "','" + db.selectedTradeId + "','"
				+ db.selectedContractorId    + "','" + db.selectedSupervisorId + "','"
						+ db.selectedForemanId  + "','"+ db.selectedClientId+ "','" + m_given + "','" + loc
				+ "','" + rem + "','" + approve1 + "','" +snaps[0] + "','"
				+ snaps[1] + "','" + mockup_Date + "','" + db.userId + "'";
		db.insert(
				"mockuptable",
				"scheme_Id,structure_id,trade_id,contractor_id,supervisor_id,foreman_id,client_staf_id,m_given,location,remark,approve,image1,image2,m_date,user_id",
				mockupdata);

	} 

	public void init() {

		Calendar c1 = Calendar.getInstance();
		timestamp = c1.get(Calendar.DAY_OF_MONTH) + "/"
				+ (c1.get(Calendar.MONTH) + 1) + "/" + c1.get(Calendar.YEAR)
				+ " " + c1.get(Calendar.HOUR_OF_DAY) + ":"
				+ c1.get(Calendar.MINUTE) /*+ ":" + c1.get(Calendar.SECOND)*/;

		imageButton = (Button) findViewById(R.id.m_capture);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (m_imageCount < 2) {
					try {
						String str1="m_"; 
						
						
						File root = new File(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "CQRA");
						root.mkdirs();
						Date d = new Date();
						String imgName = db.userId + "-" + db.selectedSchemeId
								+ "-" + db.selectedBuildingId + "-"
								+ db.selectedTradeId + "-"
								+ db.selectedContractorId + "_" + m_imageCount
								+ "_" + d.getTime() + ".jpg";
						String desc = str1.concat(imgName);
						snaps[m_imageCount] = desc;
						sdImageMainDirectory = new File(root, desc);
						tempFilename = getNextFileName().getAbsolutePath();
						startCameraActivity();
					} catch (Exception e) {
						e.printStackTrace();
						displayDialog("Error", "File not saved");
					}
				} else { 
					vibrator.vibrate(500);
					Toast.makeText(getApplicationContext(),
							"Limit for maximum(2) images is reached",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		
		imagCount();
		
		
		
		

		/*selectallCheckBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						String lable = "";
						lable = isChecked ? "Deselect All" : "Select All";
						selectAllLable.setText("Client Person" + "\n" + lable);
					}
				});

		selectallCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkedAll(selectallCheckBox.isChecked());
			}
		});*/

		/*clientlist.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long rowid) {
				selecteditem = clientlist.getCheckedItemPositions();
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
*/
	}

	void hideKeyboard() {
		LinearLayout mainlayout = (LinearLayout) findViewById(R.id.questionLayout);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), 0);
	}
	public void imagCount()
	{
		m_imageCount = 0;
		int snapslength = snaps.length;
		for (int i = 0; i < snapslength; i++) {
			if (!snaps[i].equals(null) && !snaps[i].equals(""))
				m_imageCount++;
		}
		imageButton.setText("Capture Image(" + m_imageCount + "/2)");
		
	}
	
	private boolean validation() {
		// TODO Auto-generated method stub
		boolean validate = true;

		if (schemeSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Project";
			System.out.println("validation");
		} else if (buildingSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Structure";
		} else if (tradeSpin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Trade";
		} else if (contractorspin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Contractor";
		}else if (supervisorspin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Supervisor";
		}else if (foremanspin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Forman";
		}else if (clientspin.getSelectedItemPosition() == 0) {
			errorMessage = "Please Select Client";
		} else if (editmock.getText().length() == 0) {
			editmock.setError("Please Enter Mockup Given By");
			errorMessage = "Please Enter Mockup Given By";
		} else if (location.getText().length() == 0) {
			location.setError("Please Enter location");
			errorMessage = "Please Enter location";
		} else if (remark.getText().length() == 0) {
			remark.setError("Please Enter Remark");
			errorMessage = "Please Enter Remark";
		} /*else if (!db.apporve_count_flag) {
			errorMessage = "Please Select Appove";
		}*/ else if (m_imageCount == 0) {
			errorMessage = "Please Attach Atleast one Image";
		}  else
			validate = false; 

		System.out.println("validation");

		return validate;
	}

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

	public void startCameraActivity() {

		/*
		 * tempFilename = System.currentTimeMillis()+".jpg"; //you can give any
		 * name to your image file String mPathImage =
		 * Environment.getExternalStorageDirectory()+ "/" + tempFilename; File
		 * file = new File(mPathImage);
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new
		// File(tempFilename)));
		startActivityForResult(intent, 1);
	} 
   
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (resultCode) {
		case 0:
			break;
		case -1:
			Bitmap bm = null;
			/*ProcessBitmap pb=new ProcessBitmap();
			pb.execute();*/
		 try {
				m_imageCount++;
				imageButton.setText("Capture Image(" + m_imageCount + "/2)");
				Bundle b = data.getExtras();
				bm = (Bitmap) b.get("data");

				float aspect = (float)bm.getWidth() / bm.getHeight();
				int width = 720;
				int height = (int) (width/aspect);

				// int height = (int) (width / aspect);
				bm = Bitmap.createScaledBitmap(bm, width, height, true);

				FontMetrics fm = new FontMetrics();
				Paint mpaint = new Paint();
		        mpaint.setColor(Color.WHITE);
		        mpaint.setStyle(Style.FILL);
				
				
				
				// bm = BitmapFactory.decodeFile(tempFilename, null);
				FileOutputStream out = new FileOutputStream(
						sdImageMainDirectory);
				// Write text on bitmap
				Canvas canvas = new Canvas(bm);

				Paint paint = new Paint();
				paint.setTextSize(22);
				paint.setColor(Color.RED); // Text Color
				paint.setStrokeWidth(24); // Text Size
				paint.setXfermode(new PorterDuffXfermode(
						PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
				// some more settings...

				canvas.drawRect(5, 5,200, 60, mpaint);
				canvas.drawBitmap(bm, 0, 0, paint);
				canvas.drawText(timestamp, 9, 40, paint);
				
				/*canvas.drawBitmap(bm, 0, 0, paint);
				canvas.drawText(timestamp, 10, 20, paint);*/

				bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
				bm.recycle();

			} catch (OutOfMemoryError ome) {
				ome.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
			break;
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mockup_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			switch (item.getItemId()) {
			case R.id.m_viewImage:
				showImage();
				System.out.println("picture========================");
				return true;
			}
		} catch (Exception aE) {
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * launch new Activity that show taken images from device camera.
	 * 
	 */
	
	
	private void showImage() {

		if (m_imageCount < 1) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					mockupScreen.this, R.style.MyAlertDialogStyle);
			builder.setMessage("Images not available");
			builder.setNegativeButton(" Ok ",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
				 			dialog.dismiss();
						}
					});
			builder.create();
			builder.show();

		} else {
			List<String> list = new ArrayList<String>();
			for (String str : snaps) {
				if (!TextUtils.isEmpty(str)) {
					list.add(str);
					System.out.println("images+++++++++" + str);
				}
			}
			Intent i = new Intent(mockupScreen.this, ViewImagesActivity.class);
			i.putExtra("data", list.toArray(new String[] {}));
			System.out.println("String Activity+++++++++");
			startActivity(i);
		}
	}
	
	
	
	public static int[] unionArrays(int[]... arrays)
    {
        int maxSize = 0;
        int counter = 0;

        for(int[] array : arrays) maxSize += array.length;
        int[] accumulator = new int[maxSize];

        for(int[] array : arrays)
            for(int i : array)
                if(!isDuplicated(accumulator, counter, i))
                    accumulator[counter++] = i;

        int[] result = new int[counter];
        for(int i = 0; i < counter; i++) result[i] = accumulator[i];

        return result;
    }
	
	public static boolean isDuplicated(int[] array, int counter, int value)
    {
        for(int i = 0; i < counter; i++) if(array[i] == value) return true;
        return false;
    }
	
	
	
	
	private File getNextFileName()

	{
		File root = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "CQRA" + File.separator+"temp"); 
		root.mkdirs();
		String filename = System.currentTimeMillis() + ".jpg";
		
		String str1="m_"; 
		String desc = str1.concat(filename);
		System.out.println("file in getnextfile==============================>"+desc);
		File file = new File(root, filename);
		System.out.println("FILENAME+++" + file.getAbsolutePath());
		return file;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		db.closeDb();
		Intent i = new Intent(mockupScreen.this, HomeScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);

	}

	/*private void disableList(boolean isenable) {
		if (isenable) {
			selectallCheckBox.setEnabled(isenable);
			clientlist.setEnabled(isenable);
		} else {
			clientlist.setAdapter(new ArrayAdapter<String>(mockupScreen.this,
					android.R.layout.simple_list_item_1,
					(new String[] { "NO CheckList" })));
			clientlist.setEnabled(isenable);
			selectallCheckBox.setChecked(isenable);
			selectallCheckBox.setEnabled(isenable);
		}
	}
*/
	/*private void checkedAll(boolean isChecked) {
		if (clientlist.isEnabled()) {
			int count = clientlist.getAdapter().getCount() > 0 ? clientlist
					.getAdapter().getCount() : 0;
			int i = 0;
			while (count > i) {
				clientlist.setItemChecked(i, isChecked);
				i++;
			}
		}
	}

*/	
	
	
	
	
	
	
	protected void displayDialog(final String title, final String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alertDialog.show();
	}

}
