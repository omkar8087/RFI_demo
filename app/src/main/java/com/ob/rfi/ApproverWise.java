package com.ob.rfi;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.ob.rfi.db.RfiDatabase;

public class ApproverWise extends Activity
{

	private LinearLayout mainparent;
	private LinearLayout mainchld;
	private RfiDatabase db;
	private TextView subheadng;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.approver_wise);
		
		db = new RfiDatabase(getApplicationContext());
		
		subheadng=(TextView)findViewById(R.id.sub_headng_id);
		 
		if (db.selectedBuildingId.equalsIgnoreCase("")) {
			subheadng.setText(db.selectedclientname + "/"
					+ db.selectedSchemeName);
		} else {
			subheadng.setText(db.selectedclientname + "/"
					+ db.selectedSchemeName + "/" + db.selectedStructureName);
		}
		
		subheadng.setSingleLine();
		subheadng.setSelected(true);
		subheadng.setEllipsize(TruncateAt.MARQUEE);
		setTableValue();
		 
	}
	public void setTableValue(){
		
		 
		System.out.println("in  the calling table");
		mainparent= (LinearLayout)findViewById(R.id.mainparent);
		mainchld= (LinearLayout)findViewById(R.id.mainchild);
		  
		
		TableLayout tl = new TableLayout(getApplicationContext());		
		tl.setLayoutParams(new ActionBar.LayoutParams(LayoutParams.FILL_PARENT
				, LayoutParams.WRAP_CONTENT));
		
		
		
		System.out.println("midddle");
		TableRow title = new TableRow(getApplicationContext());
		TableRow total = new TableRow(getApplicationContext());
		TextView TVCheckListName = new TextView(getApplicationContext());
		TextView TVpending = new TextView(getApplicationContext());
	
		
		//title.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));        
	
		
		TVCheckListName.setWidth(150);		
		TVpending.setWidth(150);		  
		
		
		TVCheckListName.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		TVpending.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		
		
		TVCheckListName.setGravity(Gravity.CENTER);
		TVpending.setGravity(Gravity.CENTER);
		
		
		TVCheckListName.setTextColor(Color.WHITE);
		TVpending.setTextColor(Color.WHITE);
		 
		 
		TVCheckListName.setText("Approver"); 
		TVpending.setText("Total RFI");
		
		
		title.addView(TVCheckListName);
		title.addView(TVpending);
		android.view.ViewGroup.LayoutParams params = TVCheckListName.getLayoutParams();
		params.height = LayoutParams.MATCH_PARENT;
		TVpending.setLayoutParams(params);
		 
		android.view.ViewGroup.LayoutParams params1 = TVCheckListName.getLayoutParams();
		params.height = LayoutParams.MATCH_PARENT;
		TVCheckListName.setLayoutParams(params1);
		
		
		
		mainparent.addView(title);
		System.out.println("last");
		int i=0;

		//fetch data from database
		String where=null;
		if(!(db.selectedBuildingId.equalsIgnoreCase("0") || db.selectedBuildingId.equalsIgnoreCase(""))){
			where = "ClientId='"
				+ db.selectedClientId
				+ "' "
				+ "AND ProjectId ='"
				+ db.selectedSchemeId
				+ "' AND StructureId ='" + db.selectedBuildingId + "'";
			System.out.println("building id.................");
			System.out.println("Selcted building id................."+db.selectedBuildingId);
		}else{
			where = "ClientId='"
					+ db.selectedClientId
					+ "' "
					+ "AND ProjectId ='"
					+ db.selectedSchemeId + "'";
			System.out.println("no building id");
		}
		/*Cursor cursor = db.select("ApproverWise",
				"distinct(ApproverUserId),ApproverUserName,TotalRFICount", where, null, null,
				null,null);*/
		Cursor cursor = db.select("ApproverWise", 
				"ApproverUserId,ApproverUserName,SUM(TotalRFICount)", where, null, "ApproverUserId",
				null,"ApproverUserName");
		
		
		if (cursor.moveToFirst()) {
			do {	 
				// Create a TableRow and give it an ID
				TableRow tr = new TableRow(getApplicationContext());
				TextView TVch = new TextView(getApplicationContext());
				TextView TVpnd = new TextView(getApplicationContext());
				TextView TVcomp = new TextView(getApplicationContext());      
				TextView TVtot = new TextView(getApplicationContext());
				i++;
				System.out.println("in the loop");
				
				
				TVch.setWidth(400);
				TVpnd.setWidth(300);
				
				//single line marquee
				TVch.setSingleLine();
				TVch.setSelected(true);
				TVch.setEllipsize(TruncateAt.MARQUEE);
		        
				//set margeen
				
				
		         
				TVch.setBackgroundResource(R.drawable.table_row_data_backgrnd);
				TVpnd.setBackgroundResource(R.drawable.table_row_data_backgrnd);
				
				TVch.setTextColor(Color.BLACK);
				TVpnd.setTextColor(Color.BLACK);
				
				TVch.setPadding(2, 5, 0, 5);
				TVpnd.setPadding(50, 5, 0, 5);
				//TVtot.setPadding(left, top, right, bottom)
				//setting text from databse to field
				 
				TVch.setText(cursor.getString(1)); 
				TVpnd.setText(cursor.getString(2));
				
				tr.addView(TVch);
				tr.addView(TVpnd);
				
				tl.addView(tr, new TableLayout.LayoutParams( ActionBar.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			}while (cursor.moveToNext());
			mainchld.addView(tl);
		}else{
			TableRow trnoRec = new TableRow(getApplicationContext());
			TextView TVTextvieNoRec = new TextView(getApplicationContext());
			
			TVTextvieNoRec.setWidth(400);
			
			TVTextvieNoRec.setSingleLine();
			TVTextvieNoRec.setSelected(true);
			TVTextvieNoRec.setEllipsize(TruncateAt.MARQUEE);	
			
			TVTextvieNoRec.setBackgroundResource(R.drawable.table_row_data_backgrnd);
			
			TVTextvieNoRec.setTextColor(Color.BLACK);
			
			TVTextvieNoRec.setPadding(2, 5, 0, 5);
			
			TVTextvieNoRec.setText("RFI Not Available.");
			
			trnoRec.addView(TVTextvieNoRec); 
			
			tl.addView(trnoRec, new TableLayout.LayoutParams( ActionBar.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			mainchld.addView(tl);
			System.out.println("Records not available");
		}
		
		

		
		
		
		
	}
	
	 @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	//super.onBackPressed();
	    	finish();
	    	Intent int1=new Intent(ApproverWise.this,DashBoardSelection.class);
	    	startActivity(int1);		
	    	
	    }
}
