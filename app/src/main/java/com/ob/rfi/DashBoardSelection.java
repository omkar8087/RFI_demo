package com.ob.rfi;

import java.util.Calendar;

import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class DashBoardSelection extends CustomTitle{
	private Spinner clientspin;
	private Spinner projspin;
	private Spinner structspin;
	private TextView textFromdate;
	private TextView textTodate;
	private Button okbtn;
	private Button cancelbtn;
	private int year;
	private int month;
	private int day;
	static final int DATE_PICKER_ID = 1111; 
	static final int DATE_PICKER_ID1 = 2222; 
	
	private RfiDatabase db;
	private String method;
	private String[] param;
	private String[] value;
	private int requestid;
	private String errorMessage;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dashboard_selection);
		
		//getSupportActionBar().setTitle("RFI  DashBoard");
		 
		//datepicker
		final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
		
		
		
		//
		clientspin = (Spinner) findViewById(R.id.dash_client_id);
		projspin = (Spinner) findViewById(R.id.dash_project_id);
		structspin = (Spinner) findViewById(R.id.dash_structure);
		/*textFromdate=(TextView)findViewById(R.id.dash_fromDate);
		textTodate=(TextView)findViewById(R.id.dash_toDate);*/
		okbtn=(Button)findViewById(R.id.ok_button_id);
		cancelbtn=(Button)findViewById(R.id.cancel_button_id);
		 
		db = new RfiDatabase(getApplicationContext());
		
		okbtn.setOnClickListener(new OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Authinticate()){
					displayErrorDialog("Error", errorMessage);
				}else
				{
				Intent i =new Intent( DashBoardSelection.this, TabRfiActivity.class);
				System.out.println("homescreen---------");
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);}
				
			}
		});
		
		cancelbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();	
			}
		});
		
		
		
		
		/*//setting current date to textview
		textFromdate.setText(day+"/"+month+"/"+year);
		textTodate.setText(day+"/"+month+"/"+year);
		
		
		textFromdate.setOnClickListener(new OnClickListener() {
			 
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
 
            } 
 
        });
		textTodate.setOnClickListener(new OnClickListener() {
			 
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID1);
 
            } 
 
        });
		
		*/
		
		if(isDataAvialable("CheckListWise"))
		{
			
			setClientData();
			
			System.out.println("data present........");
		}else
		{
			updateData();
		}
		 
		
	}
	
	
	
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
	
	
		
		public boolean Authinticate(){
			 
			boolean validate = true;
			if (clientspin.getSelectedItemPosition() == 0) {
				errorMessage = "Please Select Client.";
				
			} else if (projspin.getSelectedItemPosition() == 0) {
				errorMessage = "Please Select Project.";
			}else
				validate = false; 
			
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
	 @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_PICKER_ID:{
	             
	            // open datepicker dialog.
	            // set date picker for current date
	            // add pickerListener listner to date picker
	            return new DatePickerDialog(this, pickerListener, year, month,day);
	        }
	        case DATE_PICKER_ID1:{
	             
	            // open datepicker dialog.
	            // set date picker for current date
	            // add pickerListener listner to date picker
	            return new DatePickerDialog(this, pickerListener1, year, month,day);
	        }
	        
	        
	        } 
	        return null;
	    } 
	
	 public void updateData() {
			//requestid = 1;
			method = "getDashboard";
			/*param = new String[] { "userID","userRole" };
			value = new String[] { db.userId,"maker" };*/
			callService();
		}
	 
	 public void updateProjectData() {
			requestid = 1;
			method = "getDetails";
			param = new String[] { "userID","userRole" };
			value = new String[] { db.userId,"maker" };
			callService();
		}
	 
	 
	 
	 
	 protected void callService() {
			Webservice service = new Webservice(DashBoardSelection.this, network_available, "Loading.. Please wait..",
					method, param, value);
			service.setdownloadListener(new downloadListener(){
				@Override
				public void dataDownloadedSuccessfully(String data) {
					
					
					System.out.println("success data");
						saveData(data);
						
						
						
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
	 
	 
	 
	 public void saveData(String data) { 
			if(data.equalsIgnoreCase("No Record Found")){
				displayDialog("No Record Found", "(Note: You may not have the updated data.)");
			}else{
	  
				try{
					String[] tabledata = data.split("\\$"); 

					System.out.println("data========="+tabledata.toString());
					
					String column = "ClientId,ClientName,ProjectId,ProjectName,StructureId,StructureName," +
					"CheckListId,CheckListName,PendingRFICount,CompleteRFICount,TotalRFICount,user_id";//project
					saveToDatabase("CheckListWise", column, tabledata[0],true,11);
	 
	
					
					column = "ClientId,ClientName,ProjectId,ProjectName,StructureId,StructureName," +
					"MakerUserId,MakerUserName,TotalRFICount,user_id";
					saveToDatabase("MakerWise", column, tabledata[1],true,9);
					 
				
					column = "ClientId,ClientName,ProjectId,ProjectName,StructureId,StructureName," +
					"CheckerUserId,CheckerUserName,TotalRFICount,user_id";
							saveToDatabase("CheckerWise", column, tabledata[2],true,9);

				
					column = "ClientId,ClientName,ProjectId,ProjectName,StructureId,StructureName" +
					",ApproverUserId,ApproverUserName,TotalRFICount,user_id";
						saveToDatabase("ApproverWise", column, tabledata[3],true,9);
					
					
					column = "ClientId,ClientName,ProjectId ,ProjectName,StructureId,StructureName," +
					"RepresentingId,Representingname,MakeRFICount,CheckedRFICount,ApprovedRFICount,user_id";
							saveToDatabase("ContractorWise", column, tabledata[4],true,11);	
					
							
					setClientData();
				}catch(Exception e ){
					String TAG="RFI DashBoardSelection";
					Log.d(TAG,e.toString());
					e.printStackTrace();
					flushData(); 
					displayDialog("No Record Found", "Sufficient Data is not available.");
				}
			}
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
			int a=0;
			String[] rowdata = respose.split("\\|");
			for (int len = 0; len < rowdata.length; len++) {
				String[] singlerow = rowdata[len].split("~");
				StringBuffer values = new StringBuffer("'");
				for (int i = 0; i < ( singlerow.length - 1); i++) {
					values.append( singlerow[i] + "','");
					a++;
				}
				values.append( singlerow[ singlerow.length - 1 ] + "'");
				if(adduserId)
					values.append( ",'" + db.userId + "'");
				if(a>1){
					
				
				if( !existingData.contains(values.toString()))
					db.insert(table, columns, values.toString());
				System.out.println("colum="+table+"table value==="+values.toString());
			}
			} 

		}
	 
	 
	 private void flushData() {
			db.delete("CheckListWise", "user_id='" + db.userId + "'");
			db.delete("MakerWise", "user_id='" + db.userId + "'");
			db.delete("CheckerWise", "user_id='" + db.userId + "'");
			db.delete("ApproverWise", "user_id='" + db.userId + "'");
			db.delete("ContractorWise", "user_id='" + db.userId + "'");
			/*db.delete("subgroup", "user_id='" + db.userId + "'");
			db.delete("answare", "user_id='" + db.userId + "'");
			db.delete("floor", "user_id='" + db.userId + "'");
			db.delete("question_heading", "user_id='" + db.userId + "'");
			db.delete("question_group", "user_id='" + db.userId + "'");
			db.delete("question", "user_id='" + db.userId + "'");
			db.delete("userMaster", "Pk_User_ID='" + db.userId + "'");
			db.delete("card_details","user_id='" + db.userId + "'");
			db.delete("alert_details", "user_id='" + db.userId + "'");
			db.delete("scheme_foreman", "user_id='" + db.userId + "'");
			db.delete("scheme_superviser", "user_id='" + db.userId + "'");
			db.delete("mockuptable", "user_id='" + db.userId + "'");
			db.delete("inspection_log", "user_id='" + db.userId + "'");
			db.delete("trade_match", "user_id='" + db.userId + "'");
			db.delete("contractor_sf", "user_id='" + db.userId + "'");
			*/
			// db.update("sqlite_sequence", "seq=0", "name = 'question'");
			
			System.out.println("data flush+++++++++++++++++++++++++++++++++++++++++++++++++");
			//deleteImages();
		}
	 
	 
	 private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear,
	                int selectedMonth, int selectedDay) {
	             
	            year  = selectedYear;
	            month = selectedMonth;
	            day   = selectedDay;
	 
	            // Show selected date
	            textFromdate.setText(new StringBuilder().append(day)
	                    .append("/").append(month + 1).append("/").append(year)
	                    .append(" "));
	     
	           }
	        };
	        
	        private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
	   		 
		        // when dialog box is closed, below method will be called.
		        @Override
		        public void onDateSet(DatePicker view, int selectedYear,
		                int selectedMonth, int selectedDay) {
		             
		            year  = selectedYear;
		            month = selectedMonth;
		            day   = selectedDay;
		 
		            // Show selected date
		            textTodate.setText(new StringBuilder().append(day)
		                    .append("/").append(month + 1).append("/").append(year)
		                    .append(" "));
		     
		           }
		        };
		        
	        
	        
	        
	        
	        
	        
	private String[] clientId;
	private String[] client;
	private String[] schemId;
	private String[] bldgId;
	        
	        
	        
	        
	        
	        
	        
	        
	private void setClientData() {
		Cursor cursoruni = db.UNIONNew1("CheckListWise","distinct(ClientId),ClientName",null, "MakerWise", "distinct(ClientId),ClientName",
				null, "CheckerWise", "distinct(ClientId),ClientName", null, "ApproverWise", "distinct(ClientId),ClientName",
				null, "ContractorWise", "distinct(ClientId),ClientName", null);
		
		
		clientId = new String[cursoruni.getCount()];
		client = new String[cursoruni.getCount() + 1];
		client[0] = "--select--";
		
		if (cursoruni.moveToFirst()) {

			do {
				System.out.println("counteddddddddddd");
				clientId[cursoruni.getPosition()] = cursoruni.getString(0);

				System.out.println("name of client--"+cursoruni.getString(1));
				client[cursoruni.getPosition() + 1] = cursoruni.getString(1);
			} while (cursoruni.moveToNext());
		} else {
			client[0] = "Client not allocated"; 

		}

		if (cursoruni != null && !cursoruni.isClosed()) {

			cursoruni.close();
		}
	
		
		String where1 = "user_id='" + db.userId + "'";

		// Client_ID TEXT,Clnt_Name TEXT,CL_Dispaly_Name TEXT


		/*System.out.println("cleint where -==========>" + where1);
		Cursor cursor = db.select("Client as ct",
				"distinct(ct.Client_ID),ct.Clnt_Name", where1, null, null, null,
				null);

		clientId = new String[cursor.getCount()];
		client = new String[cursor.getCount() + 1];

		client[0] = "--select--";

		if (cursor.moveToFirst()) {

			do {
				System.out.println("nnnnnnnnnnnnnnn");
				clientId[cursor.getPosition()] = cursor.getString(0);
				client[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			client[0] = "Client not allocated";

		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}
*/
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

					db.selectedclientname = clientspin.getSelectedItem()
							.toString();
					System.out.println("client selected not present");
					db.selectedClientId = clientId[position - 1];
					setSchemeSpinnerData(clientId[position - 1]);
					db.selectedclientname=clientspin.getSelectedItem().toString();
					projspin.setClickable(true);
					projspin.setSelection(0);
					// db.selectedClient = client[position];

				} else {
					db.selectedClientId = "";
					db.selectedclientname="";
					projspin.setClickable(false);
					projspin.setSelection(0);
				
 
				} 
			}  
 
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}
	        
	        //set project spinner data
	        
	        private void setSchemeSpinnerData(String Cl_ID) {
	    		
	        	String where = "ClientId='" + db.selectedClientId + "'";
	        	
	        	Cursor cursoruni = db.UNIONNew("CheckListWise","distinct(ProjectId),ProjectName", where , "MakerWise", "distinct(ProjectId),ProjectName",
	    				where, "CheckerWise", "distinct(ProjectId),ProjectName", where, "ApproverWise", "distinct(ProjectId),ProjectName",
	    				where, "ContractorWise", "distinct(ProjectId),ProjectName", where);
	        	
	        	
	        	schemId = new String[cursoruni.getCount()];
	    		String[] items = new String[cursoruni.getCount() + 1];
	    		items[0] = "--Select--";
	        	
	        	
	        	
	    		if (cursoruni.moveToFirst()) {

	    			do {
	    				schemId[cursoruni.getPosition()] = cursoruni.getString(0);
	    				items[cursoruni.getPosition() + 1] = cursoruni.getString(1);
	    			} while (cursoruni.moveToNext());
	    		} else {
	    			items[0] = "Scheme(s) not available";
	    		}

	    		if (cursoruni != null && !cursoruni.isClosed()) {
	    			cursoruni.close();
	    		}
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	    	/*	String where = "s.Scheme_Cl_Id = c.Client_ID AND s.Scheme_Cl_Id='"+db.selectedClientId+"' AND s.user_id='" + db.userId
	    				+ "'";
	    		Cursor cursor = db.select("Scheme as s, Client as c",
	    				"distinct(s.PK_Scheme_ID), s.Scheme_Name", where, null, null, null,
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
*/
	    		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    				android.R.layout.simple_spinner_item, items);
	    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    		projspin.setAdapter(adapter);

	    		projspin.setOnItemSelectedListener(new OnItemSelectedListener() {

	    			public void onItemSelected(AdapterView<?> aview, View view,
	    					int position, long rowid) {
	    				if (position > 0) {
	    					db.selectedSchemeId = schemId[position - 1];
	    					setBulidngSpinnerData(schemId[position - 1]);
	    					db.selectedSchemeName=projspin.getSelectedItem().toString();
	    					structspin.setClickable(true);
	    					structspin.setSelection(0);
	    				} else {
	    					db.selectedSchemeId = "";
	    					db.selectedSchemeName="";
	    					structspin.setClickable(false);
	    					structspin.setSelection(0);

	    				}
	    			}

	    			public void onNothingSelected(AdapterView<?> arg0) {
	    			}
	    		});
	    	}
	        
	        
	        //seting building spinner data 
	        
	        private void setBulidngSpinnerData(final String schemeid) {
	    		
	    		
	        	String where = "ClientId='" + db.selectedClientId + "'AND ProjectId='" 
	    				+ db.selectedSchemeId +"'";
	        	
	        	Cursor cursoruni = db.UNIONNew("CheckListWise","distinct(StructureId),StructureName", where , "MakerWise", "distinct(StructureId),StructureName",
	    				where, "CheckerWise", "distinct(StructureId),StructureName", where, "ApproverWise", "distinct(StructureId),StructureName",
	    				where, "ContractorWise", "distinct(StructureId),StructureName", where);
	        	
	    		bldgId = new String[cursoruni.getCount()];
	    		String[] items = new String[cursoruni.getCount() + 1];
	    		items[0] = "--Select--";
	        	
	    		if (cursoruni.moveToFirst()) {
	    			do {
	    				bldgId[cursoruni.getPosition()] = cursoruni.getString(0);
	    				items[cursoruni.getPosition() + 1] = cursoruni.getString(1);

	    			} while (cursoruni.moveToNext());
	    		} else {
	    			items[0] = "Building(s) not available";
	    		}

	    		if (cursoruni != null && !cursoruni.isClosed()) {
	    			cursoruni.close();
	    		}
	        	
	        	
	    		
	    	/*	String where = "b.Build_scheme_id = s.PK_Scheme_ID AND b.Build_scheme_id='"
	    				+ db.selectedSchemeId
	    				+ "' "
	    				+ "AND s.user_id='" 
	    				+ db.userId 
	    				+ "' AND b.user_id='" + db.userId + "'";

	    		Cursor cursor = db.select("Building as b, Scheme as s",
	    				" distinct(b.Bldg_ID), b.Bldg_Name", where, null,
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
*/
	    		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    				android.R.layout.simple_spinner_item, items);
	    		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    		structspin.setAdapter(adapter);

	    		structspin.setOnItemSelectedListener(new OnItemSelectedListener() {

	    			public void onItemSelected(AdapterView<?> arg0, View arg1,
	    					int position, long arg3) {
	    				if (position > 0) {
	    					db.selectedBuildingId = bldgId[position - 1];
	    					System.out.println("selecteed bdg id"
	    							+ db.selectedBuildingId);
	    					db.selectedStructureName=structspin.getSelectedItem().toString();

	    				} else {
	    					db.selectedBuildingId = "";
	    					db.selectedStructureName="";
	    					
	    				}
	    			}

	    			public void onNothingSelected(AdapterView<?> arg0) {
	    			}
	    		});
	    	}
	    	
	        
	      

	
	        
	        
	        
	        
	        @Override
	        public void onBackPressed() {
	        	// TODO Auto-generated method stub
	        //	super.onBackPressed();
	        	finish();
	        	Intent i =new Intent( DashBoardSelection.this, HomeScreen.class);
				System.out.println("homescreen---------");
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
	        }
}
