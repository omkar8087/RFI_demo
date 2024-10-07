package com.ob.rfi;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ob.rfi.AdapterRfi.CreateAdapterRfi;
import com.ob.rfi.AdapterRfi.UpdateAdaptorFfi;
import com.ob.rfi.db.RfiDatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class UpdateRfiScroll extends CustomTitle{
	private ListView listview;
	private RfiDatabase db;
	private TextView scrol_text;
	private String question_seq_id;
	private String question_type;
	private String current_ques_id;
	private TextView dr_text;
	private TextView drwng_text;
	
	
	public String coverageGlobal;
	private SharedPreferences checkPreferences;
	private Editor editor;
	private String coverage;
	private String drawing;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_rfi_scroll);
				
		listview=(ListView)findViewById(R.id.rfi_question_listview_id);
		scrol_text=(TextView)findViewById(R.id.scrolling_text_id);
		drwng_text=(TextView)findViewById(R.id.drawng_id);
		
		
		
		
		
		
		
		
		
		checkPreferences=getSharedPreferences("RFI_File",MODE_PRIVATE);
		coverage=checkPreferences.getString("coverage","");
		drawing=checkPreferences.getString("drawing","");
		
		
		
		drwng_text.setText(coverage+"/"+drawing);
		
		db=new RfiDatabase(getApplicationContext());
		setListviewdata();
	}
	
	public void setListviewdata(){
		
		scrol_text.setText(db.selectedSchemeName+" / "+db.selectedclientname+"/"+db.selectedChecklistName);
		
		
		
		 
		
		
		//String whereClause="Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"'";
				String whereClause="q.PK_question_id=r.QUE_Id AND "+"r.RFI_Id='"+db.selectedrfiId+"'  AND  r.CHKL_Id='"+db.selectedChecklistId+"' AND r.GRP_Id='"+db.selectedGroupId+"' AND q.NODE_Id='"+db.selectedBuildingId+
						"' AND q.user_id='"+db.userId+"'";
			//	RFI_Id
				//PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id
				
			//	Cursor cursor = db.select("question", "distinct(PK_question_id),QUE_Des,QUE_SequenceNo,QUE_Type",whereClause, null, null, null, null);
				Cursor cursor = db.select("question as q,Rfi_update_details r", "distinct(q.PK_question_id),q.QUE_Des,q.QUE_SequenceNo,q.QUE_Type,r.Coverage,r.Answer " +
						",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4,r.RFI_Id",whereClause, null, null, null, null);
				String[] q_id=new String[cursor.getCount()];
				String[] q_text=new String[cursor.getCount()];
				int count=0;
				
				System.out.println("cursor lenght---------"+cursor.getCount());
				if(cursor.moveToFirst()){ 
					do{
						/*Qdataid[cursor.getPosition()] = Integer.parseInt(cursor.getString(0));
						questionData[cursor.getPosition()] = cursor.getString(1);*/
						
						question_seq_id=cursor.getString(cursor.getColumnIndex("QUE_SequenceNo"));
						question_type=cursor.getString(cursor.getColumnIndex("QUE_Type"));
						current_ques_id=cursor.getString(cursor.getColumnIndex("PK_question_id"));
						 System.out.println("calling method.........");
					//r.Coverage,r.Answer " +",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4	
						
						InsertDefaultAnswer(cursor.getString(4).toString(),cursor.getString(5).toString(),cursor.getString(6).toString(),
								cursor.getString(7).toString()
								,cursor.getString(8).toString(),cursor.getString(9).toString(),cursor.getString(10).toString(),
								cursor.getString(0).toString());
						System.out.println("question data-------"+cursor.getString(0).toString());
						q_id[count]=cursor.getString(0).toString();
						q_text[count]=cursor.getString(1).toString();
						count++;
					System.out.println("IN CUrsor---id"+cursor.getString(0).toString());
					System.out.println("IN CUrsor---Question"+cursor.getString(1).toString());
					}while(cursor.moveToNext());
				}else  
				{ 
					System.out.println("not question-----------------");
				}

				if (cursor != null && !cursor.isClosed()) {
					cursor.close();
				}
				
				
				
				
			//Setting value  to adapter class
				
				UpdateAdaptorFfi adapter = new UpdateAdaptorFfi(UpdateRfiScroll.this, q_id, q_text,coverage);	
				listview.setAdapter(adapter);
				
				listview.setDividerHeight(4);
		
				//Setting value  to adapter class
				
				
				if(!db.LastSelectedListItem.equalsIgnoreCase("")){
					
					
					listview.setSelection(Integer.parseInt(db.LastSelectedListItem));
					listview.setFocusableInTouchMode(true);
					db.LastSelectedListItem="";
				}
				
				
				 
				
				
	}  
	 ////r.Coverage,r.Answer " +",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4	
	public void InsertDefaultAnswer(String Coverage1, String Answer1, String Remark1, String Image11, String Image21, String Image31, String Image41, String que_id){
		
		
		
		String whereClause1="Rfi_id='"+db.selectedrfiId +"' AND FK_question_id='"+que_id+"' AND user_id='"+db.userId+"'"; 
		Cursor cursor1 = db.select("Answer", "FK_question_id",whereClause1, null, null, null, null);
		boolean isanswerd=false;
		//check for update question
		if(cursor1.moveToFirst()){ 
			do{
				isanswerd=true;
				
			}while(cursor1.moveToNext());
		}else 
		{
			System.out.println("not question-----------------");
		}

		if (cursor1 != null && !cursor1.isClosed()) {
			cursor1.close();
		}
		
		System.out.println("flaggggggggggg-----------------"+isanswerd);
		
		if(!isanswerd){
		Intent int1=getIntent();
		String coverage=int1.getStringExtra("coverage"); 
		
		//saving answer 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String rfi_date=df.format(new Date());
		
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String rfi_date1=df1.format(new Date());
		
		String tempAns="";
		
		if(Answer1.equalsIgnoreCase("Conformity"))
		{
			tempAns="0";
		}
		else if(Answer1.equalsIgnoreCase("Non Conformity")){
			tempAns="1";
		}else{
			tempAns="2";	
		}
		
		
		
		
		
		 
		
		
		String rfidata = "'"+db.selectedClientId + "','"+db.selectedSchemeId+ "','"+db.selectedlevelId+ "','"+db.selectedBuildingId+ "','"+db.selectedFloorId+ "','"+
				db.selectedUnitId + "','"+db.selectedSubUnitId+ "','"+db.selectedElementId+ "','"+db.selectedSubElementId+ "','"+Coverage1 + "','"
				
				+ tempAns + "','"
				+ ""+ "','" + "" + "','" 
				+""+"','" +""+ "','" +""+"','" + db.selectedrfiId + "','"
						+ rfi_date  + "','"+ current_ques_id+ "','" +question_seq_id+ "','" + question_type
				+ "','" + db.selectedNodeId + "','" + db.selectedChecklistId + "','" +db.selectedGroupId+ "','"+" "+ "','"+rfi_date1+"','"+drawing+ "','" + 
						"0"+"','"+db.userId + "'";
		
		
		    
		db.insert(
				"Answer",
				"CL_Id,PRJ_Id,Level_int,Structure_Id,Stage_Id,"+
				"Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,Coverage_str,ans_text,remark,snap1,snap2,snap3,snap4,Rfi_id,dated," +
				"FK_question_id,FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id, Fk_Grp_ID,checker_remark,check_date,drawing_no,answerFlag,user_id",
				rfidata);
			System.out.println(" default answer data inserted ================="+rfidata);
		
				
		
	}
	
	}
	
	public void nevigateHomeScreen(){
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setCancelable(false);
		alertDialog.setMessage("Do you want to save this RFI?");
		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				getScreenToBeDisplayed();
				/*SelectQuestion.destruct.finish();
				finish();
				Intent int1=new Intent(getApplicationContext(),SelectQuestion.class);
				int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				finish();
				startActivity(int1);*/
			
					}
		});
		alertDialog.setButton2("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				deleteAnswer();
				
			//	SelectQuestion.destruct.finish();
				finish();
			Intent int1=new Intent(UpdateRfiScroll.this,UpdateRfi.class);
			int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(int1); 
				
				//dialog.dismiss();
			}
		});
		
		alertDialog.setButton3("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		alertDialog.show();
	
 
	} 
	
	public void deleteAnswer(){
		System.out.println("answer deleted...");
		db.delete("Answer", "Rfi_id='" + db.selectedrfiId + "'");
	}
	
	protected void getScreenToBeDisplayed() {  
		        
		        updateRfiTable();
				cleanData();
				db.closeDb();
				//UpdateRfi.updateclass.finish();
				finish();
				 
				//startActivity(new Intent(Update_Questionire.this,HomeScreen.class));
				Intent inte1=new Intent(UpdateRfiScroll.this,UpdateRfi.class);
				inte1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			        startActivity(inte1);
		         
			}
	
	public void updateRfiTable()
	{
		db.update("Rfi_update_details", "status_device='" + "complete'","user_id1='"+db.userId+"' AND RFI_Id='" + db.selectedrfiId + "'" );
	}
	
  
	private void cleanData() {
		db.selectedSchemeId="";
		db.selectedSchemeName="";
		db.selectedclientname="";  
		db.selectedClientId="";
		db.selectedBuildingId="";
		db.selectedFloorId="";
		db.selectedUnitId="";
		db.selectedSubUnitId="";
		db.selectedElementId="";
		db.selectedSubElementId="";
		db.selectedChecklistId="" ;
		db.selectedChecklistName="" ;
		db.selectedGroupId="" ;
		
	}
	
	@Override
	public void onBackPressed() { 
		// TODO Auto-generated method stub
	//	super.onBackPressed();
		//nevigateHomeScreen(); 
		finish();
		Intent int1=new Intent(UpdateRfiScroll.this,UpdateRfi.class);
		int1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(int1);
		
	}
	
}
