package com.ob.rfi;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ob.rfi.db.RfiDatabase;

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

public class MakerWiseList extends Activity
{

	private LinearLayout mainparent;
	private LinearLayout mainchld;
	private RfiDatabase db;
	private TextView subheadng;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maker_wise_list);
		 
		db = new RfiDatabase(getApplicationContext());
		
		subheadng=(TextView)findViewById(R.id.sub_headng_id);
		
		if(RfiDatabase.selectedBuildingId.equalsIgnoreCase(""))
		{
			subheadng.setText(RfiDatabase.selectedclientname+"/"+RfiDatabase.selectedSchemeName);
		}else{
			subheadng.setText(RfiDatabase.selectedclientname+"/"+RfiDatabase.selectedSchemeName+"/"+RfiDatabase.selectedStructureName);	
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
		tl.setLayoutParams(new ActionBar.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT
				, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		System.out.println("midddle");
		TableRow title = new TableRow(getApplicationContext());
		TableRow total = new TableRow(getApplicationContext());
		TextView TVCheckListName = new TextView(getApplicationContext());
		TextView TVpending = new TextView(getApplicationContext());
		
		
		
		//title.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));        
	
		
		TVCheckListName.setWidth(400);
		TVpending.setWidth(300);
		
		
		TVCheckListName.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		TVpending.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		
		
		TVCheckListName.setGravity(Gravity.CENTER);
		TVpending.setGravity(Gravity.CENTER);
		
		
		TVCheckListName.setTextColor(Color.WHITE);
		TVpending.setTextColor(Color.WHITE);
		
		
		TVCheckListName.setText("Maker");
		TVpending.setText("Total RFI");
		
		
		title.addView(TVCheckListName);
		title.addView(TVpending);
		android.view.ViewGroup.LayoutParams params = TVCheckListName.getLayoutParams();
		params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		TVpending.setLayoutParams(params);
		 
		android.view.ViewGroup.LayoutParams params1 = TVCheckListName.getLayoutParams();
		params.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
		TVCheckListName.setLayoutParams(params1);
		
		
		
		mainparent.addView(title);
		System.out.println("last");
		int i=0;

		//fetch data from database
		String where=null;
		if(!(RfiDatabase.selectedBuildingId.equalsIgnoreCase("0") || RfiDatabase.selectedBuildingId.equalsIgnoreCase(""))){
			where = "ClientId='"
				+ RfiDatabase.selectedClientId
				+ "' "
				+ "AND ProjectId ='"
				+ RfiDatabase.selectedSchemeId
				+ "' AND StructureId ='" + RfiDatabase.selectedBuildingId + "'";
			System.out.println("building id.................");
			System.out.println("Selcted building id................."+RfiDatabase.selectedBuildingId);
		}else{
			where = "ClientId='"
					+ RfiDatabase.selectedClientId
					+ "' " 
					+ "AND ProjectId ='"
					+ RfiDatabase.selectedSchemeId + "'";
			System.out.println("no building id");
		}
		/*Cursor cursor = db.select("MakerWise",
				"distinct(MakerUserId),MakerUserName,TotalRFICount", where, null, null,
				null,null);
		*/
		Cursor cursor = db.select("MakerWise", 
				"MakerUserId,MakerUserName,SUM(TotalRFICount)", where, null, "MakerUserId",
				null,"MakerUserName");
		
		//SELECT pk_id, SUM(total) as sum FROM cqratest GROUP BY pk_id
		
		//db.select(TABLE_NAME, COLUMNS, WHERE, SELECTION_ARGS, GROUP_BY, HAVING, OREDER_BY)
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
				
				tl.addView(tr, new TableLayout.LayoutParams( android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			}while (cursor.moveToNext());
			mainchld.addView(tl);
		}else{
			TableRow trnoRec = new TableRow(getApplicationContext());
			TextView TVTextvieNoRec = new TextView(getApplicationContext());
			
			TVTextvieNoRec.setWidth(300);		
			
			TVTextvieNoRec.setSingleLine();
			TVTextvieNoRec.setSelected(true);
			TVTextvieNoRec.setEllipsize(TruncateAt.MARQUEE);	
			
			TVTextvieNoRec.setBackgroundResource(R.drawable.table_row_data_backgrnd);
			
			TVTextvieNoRec.setTextColor(Color.BLACK);
			
			TVTextvieNoRec.setPadding(2, 5, 0, 5);
			
			TVTextvieNoRec.setText("RFI Not Available.");
			
			trnoRec.addView(TVTextvieNoRec); 
			
			tl.addView(trnoRec, new TableLayout.LayoutParams( android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			
			mainchld.addView(tl);
			System.out.println("Records not available");
		}
		
		

		
		
		
		
	}
	 @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
	    	//super.onBackPressed();
	    	finish();
	    	Intent int1=new Intent(MakerWiseList.this,DashBoardSelection.class);
	    	startActivity(int1);		
	    	
	    }
	
	
}
