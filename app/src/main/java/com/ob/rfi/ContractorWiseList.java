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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;

import com.ob.rfi.db.RfiDatabase;

public class ContractorWiseList extends Activity
{

	private LinearLayout mainparent;
	private LinearLayout mainchld;
	private RfiDatabase db;
	private TextView subheadng;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contractor_wise);
		
		db = new RfiDatabase(getApplicationContext());
		 
		subheadng=(TextView)findViewById(R.id.sub_headng_id);
		
		
		if(db.selectedBuildingId.equalsIgnoreCase(""))
		{
			subheadng.setText(db.selectedclientname+"/"+db.selectedSchemeName);
		}else{
			subheadng.setText(db.selectedclientname+"/"+db.selectedSchemeName+"/"+db.selectedStructureName);	
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
		tl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT
				, LayoutParams.WRAP_CONTENT));
		System.out.println("midddle");
		TableRow title = new TableRow(getApplicationContext());
		TableRow total = new TableRow(getApplicationContext());
		TextView TVrepresntngName = new TextView(getApplicationContext());
		TextView TVMakeRFI = new TextView(getApplicationContext());
		TextView TVCheckedRFI = new TextView(getApplicationContext());
		TextView TVApprovedRFI = new TextView(getApplicationContext());
		
		
		//title.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT));        
	
		
		TVrepresntngName.setWidth(300);
		TVMakeRFI.setWidth(200);
		TVCheckedRFI.setWidth(250);
		TVApprovedRFI.setWidth(200);


		
		TVrepresntngName.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		TVMakeRFI.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		TVCheckedRFI.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		TVApprovedRFI.setBackgroundResource(R.drawable.table_border_rect_backgrnd);
		
		TVrepresntngName.setGravity(Gravity.CENTER);
		TVMakeRFI.setGravity(Gravity.CENTER);
		TVCheckedRFI.setGravity(Gravity.CENTER);
		TVApprovedRFI.setGravity(Gravity.CENTER);
		
		TVrepresntngName.setTextColor(Color.WHITE);
		TVMakeRFI.setTextColor(Color.WHITE);
		TVCheckedRFI.setTextColor(Color.WHITE);
		TVApprovedRFI.setTextColor(Color.WHITE);
		
		TVrepresntngName.setText("Representing Name");
		TVMakeRFI.setText("Make RFI");
		TVCheckedRFI.setText("Checked RFI");
		TVApprovedRFI.setText("Approved RFI");
		
		title.addView(TVrepresntngName);
		title.addView(TVMakeRFI);
		title.addView(TVCheckedRFI);
		title.addView(TVApprovedRFI);
		/*android.view.ViewGroup.LayoutParams params = TVCheckListName.getLayoutParams();
		params.height = LayoutParams.MATCH_PARENT;
		TVpending.setLayoutParams(params);
		 
		android.view.ViewGroup.LayoutParams params1 = TVCheckListName.getLayoutParams();
		params.height = LayoutParams.MATCH_PARENT;
		TVCheckListName.setLayoutParams(params1);*/
		
		
		
		mainparent.addView(title);
		System.out.println("last");
		int i=0;
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
		
		//fetch data from database
		/*Cursor cursor = db.select("ContractorWise",
				"distinct(RepresentingId),Representingname,MakeRFICount,CheckedRFICount,ApprovedRFICount", where, null, null,
				null,null);*/
		Cursor cursor = db.select("ContractorWise", 
				"RepresentingId,Representingname,SUM(MakeRFICount),SUM(CheckedRFICount),SUM(ApprovedRFICount)", where, null, "RepresentingId",
				null,"Representingname");
		
		
		
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
				
				
				TVch.setWidth(300);
				TVpnd.setWidth(200);
				TVcomp.setWidth(250);
				TVtot.setWidth(200);

				
				//single line marquee
				TVch.setSingleLine();
				TVch.setSelected(true);
				TVch.setEllipsize(TruncateAt.MARQUEE);
		        
				//set margeen
				
				
		         
				TVch.setBackgroundResource(R.drawable.table_row_data_backgrnd);
				TVpnd.setBackgroundResource(R.drawable.table_row_data_backgrnd);
				TVcomp.setBackgroundResource(R.drawable.table_row_data_backgrnd);
				TVtot.setBackgroundResource(R.drawable.table_row_data_backgrnd);
				
				TVch.setTextColor(Color.BLACK);
				TVpnd.setTextColor(Color.BLACK);
				TVcomp.setTextColor(Color.BLACK);
				TVtot.setTextColor(Color.BLACK);
				
				
				TVch.setPadding(2, 5, 0, 5);
				TVpnd.setPadding(50, 5, 0, 5);
				TVcomp.setPadding(50, 5, 0, 5);
				TVtot.setPadding(50, 5, 0, 5);
				
				//TVtot.setPadding(left, top, right, bottom)
				//setting text from databse to field
				 
				TVch.setText(cursor.getString(1)); 
				TVpnd.setText(cursor.getString(2));
				TVcomp.setText(cursor.getString(3)); 
				TVtot.setText(cursor.getString(4));
				
				//if webservice then it need to change mean order should be correct while taking values database 
				/*TVch.setText(cursor.getString(1)); 
				TVpnd.setText(cursor.getString(3));
				TVcomp.setText(cursor.getString(2)); 
				TVtot.setText(cursor.getString(4));*/
				
				
				tr.addView(TVch);
				tr.addView(TVpnd);
				tr.addView(TVcomp);
				tr.addView(TVtot);
				
				tl.addView(tr, new TableLayout.LayoutParams( ActionBar.LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
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
	    	Intent int1=new Intent(ContractorWiseList.this,DashBoardSelection.class);
	    	startActivity(int1);		
	    	
	    }
}
