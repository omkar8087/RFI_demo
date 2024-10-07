package com.ob.rfi;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ob.rfi.AdapterRfi.CreateAdapterRfi;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


/*************************************************************************************************
 * Proj. Name       : RFI 
 *
 * Class Name       : RfiQuestionSelect
 *
 * Method Name : OnCreate
 *
 * Arguments passed : none
 *
 * Return Type      : none
 *
 * Synopsis         : show Questions in Listview
 *
 * Done By          : Balaji
 **************************************************************************************************/

 

public class RfiQuestionSelect extends CustomTitle{
	
	 
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
	private boolean flag;

	/*************************************************************************************************
	 * Proj. Name       : RFI 
	 *
	 * Class Name       : RfiQuestionSelect
	 *
	 * Method Name : OnCreate
	 *
	 * Arguments passed : none
	 *
	 * Return Type      : none
	 *
	 * Synopsis         : show Questions in Listview
	 *
	 * Done By          : Balaji
	 **************************************************************************************************/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rfi_question_select);
		
		
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

	@Nullable
	@Override
	public ActionBar getSupportActionBar() {
		return super.getSupportActionBar();
	}

	public void setListviewdata(){
		
		scrol_text.setText(db.selectedSchemeName+" / "+db.selectedclientname+"/"+db.selectedChecklistName);
		
		
		
		 
		
		
		//String whereClause="Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"'";
				String whereClause="Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"' AND NODE_Id='"+db.selectedBuildingId+
						"' AND user_id='"+db.userId+"'";
				
				//PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id
				
				Cursor cursor = db.select("question", "distinct(PK_question_id),QUE_Des,QUE_SequenceNo,QUE_Type",whereClause, null, null, null, null);
		
				String[] q_id=new String[cursor.getCount()];
				String[] q_text=new String[cursor.getCount()];
				db.questionCount= String.valueOf(cursor.getCount());
				System.out.println("question count in maker screen..."+cursor.getCount());
				int count=0;
				if(cursor.moveToFirst()){ 
					do{
						/*Qdataid[cursor.getPosition()] = Integer.parseInt(cursor.getString(0));
						questionData[cursor.getPosition()] = cursor.getString(1);*/
						
						question_seq_id=cursor.getString(cursor.getColumnIndex("QUE_SequenceNo"));
						question_type=cursor.getString(cursor.getColumnIndex("QUE_Type"));
						current_ques_id=cursor.getString(cursor.getColumnIndex("PK_question_id"));
						
						InsertDefaultAnswer();
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
				
				CreateAdapterRfi adapter = new CreateAdapterRfi(RfiQuestionSelect.this, q_id, q_text,coverage);	
				listview.setAdapter(adapter);
				
				listview.setDividerHeight(4);
		
//Setting value  to adapter class
				
			
				if(!db.LastSelectedListItem.equalsIgnoreCase("")){
					
					
					listview.setSelection(Integer.parseInt(db.LastSelectedListItem));
					listview.setFocusableInTouchMode(true);
					db.LastSelectedListItem="";
				}
				
				
				 
				
				
	}  
	 
	public void InsertDefaultAnswer(){
		if(!SelectQuestion.insertFlag){
		Intent int1=getIntent();
		String coverage=int1.getStringExtra("coverage");
		
		//saving answer
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String rfi_date=df.format(new Date());
		 
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String rfi_date1=df1.format(new Date());
		String rfidata = "'"+db.selectedClientId + "','"+db.selectedSchemeId+ "','"+db.selectedlevelId+ "','"+db.selectedBuildingId+ "','"+db.selectedFloorId+ "','"+
				db.selectedUnitId + "','"+db.selectedSubUnitId+ "','"+db.selectedElementId+ "','"+db.selectedSubElementId+ "','"+coverage + "','"
				
				+ "1" + "','"
				+ "" + "','" + "" + "','" 
				+ ""+"','" +""+ "','" +""+"','" + db.selectedrfiId + "','"
						+ rfi_date  + "','"+ current_ques_id+ "','" +question_seq_id+ "','" + question_type
				+ "','" + db.selectedNodeId + "','" + db.selectedChecklistId + "','" +db.selectedGroupId+ "','"+" "+ "','"+rfi_date1+"','"+drawing+ "','" 
						+"0"+ "','"+db.userId + "'";
		
		
		
		db.insert(
				"Answer",
				"CL_Id,PRJ_Id,Level_int,Structure_Id,Stage_Id,"+
				"Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,Coverage_str,ans_text,remark,snap1,snap2,snap3,snap4,Rfi_id,dated," +
				"FK_question_id,FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id, Fk_Grp_ID,checker_remark,check_date,drawing_no,answerFlag,user_id",
				rfidata);
			System.out.println(" default answer data inserted ================="+rfidata);
		
				
		
	}
	
	}
	
	public void nevigateHomeScreen() {
		/*System.out.println("question Count: " + db.questionCount);
		System.out.println("submit Question count: " + db.questionSubmitCount);
		if (!db.questionCount.equalsIgnoreCase(db.questionSubmitCount)) {
			AlertDialog alertDialog1 = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
			alertDialog1.setCancelable(false);
			alertDialog1.setMessage("All Question Answers mandatory..");
			alertDialog1.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//getScreenToBeDisplayed();
				}
			});
			alertDialog1.show();
		} */
			AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
			alertDialog.setCancelable(false);
			alertDialog.setMessage("Do you want to save this RFI?");
			alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					db.fromCreate = true;

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

					SelectQuestion.destruct.finish();
					finish();
					Intent int1 = new Intent(RfiQuestionSelect.this, SelectQuestion.class);
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

		db.delete("Rfi_New_Create", "user_id='" + db.userId + "'");		
		db.insert("Rfi_New_Create", "FK_rfi_Id,user_id","'"+SelectQuestion.nval+ "','"+db.userId+"'");
		System.out.println("inserted id data================="+SelectQuestion.nval);
			cleanData();
			db.closeDb();
			finish();
			//startActivity(new Intent(RfiQuestionire.this,HomeScreen.class));
			//SelectQuestion.destruct.finish();
			SelectQuestion.slectionclass.finish();
			Intent inte1=new Intent(RfiQuestionSelect.this,SelectQuestion.class);
			inte1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(inte1);
					
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
		nevigateHomeScreen();
		
		
	}
	
}
