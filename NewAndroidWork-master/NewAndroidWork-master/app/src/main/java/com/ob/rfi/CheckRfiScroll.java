package com.ob.rfi;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ob.rfi.AdapterRfi.CheckAdapterRfi;

import com.ob.rfi.db.RfiDatabase;

public class CheckRfiScroll extends CustomTitle {
	private ListView listview;
	private RfiDatabase db;
	private TextView scrol_text;
	private String question_seq_id;
	private String question_type;
	private String current_ques_id;
	private TextView dr_text;
	private TextView drwng_text;

	int flag = 0;

	public String coverageGlobal;
	private SharedPreferences checkPreferences;
	private Editor editor;
	private String coverage;
	private String drawing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_rfi_scroll);

		listview = (ListView) findViewById(R.id.rfi_question_listview_id);
		scrol_text = (TextView) findViewById(R.id.scrolling_text_id);
		drwng_text = (TextView) findViewById(R.id.drawng_id);

		/*
		 * listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
		 * {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) { // TODO Auto-generated method stub if(flag
		 * == 0) { flag = 1; String
		 * whereClause="q.PK_question_id=r.QUE_Id AND "+
		 * "r.RFI_Id='"+db.selectedrfiId
		 * +"'  AND  r.CHKL_Id='"+db.selectedChecklistId
		 * +"' AND r.GRP_Id='"+db.selectedGroupId
		 * +"' AND q.NODE_Id='"+db.selectedBuildingId+
		 * "' AND q.user_id='"+db.userId+"'"; Cursor cursor =
		 * db.select("question as q,Check_update_details r",
		 * "distinct(q.PK_question_id),q.QUE_Des,q.QUE_SequenceNo,q.QUE_Type,r.Coverage,r.Answer "
		 * +
		 * ",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4,r.RFI_Id",whereClause,
		 * null, null, null, null); if(cursor.moveToFirst()){ do {
		 * InsertDefaultAnswer
		 * (cursor.getString(4).toString(),cursor.getString(5)
		 * .toString(),cursor.getString(6).toString(),
		 * cursor.getString(7).toString()
		 * ,cursor.getString(8).toString(),cursor.
		 * getString(9).toString(),cursor.getString(10).toString(),
		 * cursor.getString(0).toString());
		 * System.out.println("question data-------"
		 * +cursor.getString(0).toString());
		 * System.out.println("RFI  ID-------"+db.selectedrfiId); }
		 * while(cursor.moveToNext()); Toast.makeText(CheckRfiScroll.this,
		 * "flag = 0...so data inserted", Toast.LENGTH_LONG).show(); }
		 * 
		 * } else { Toast.makeText(CheckRfiScroll.this,
		 * "flag = 1...so no data inserted", Toast.LENGTH_LONG).show(); } } });
		 */

		checkPreferences = getSharedPreferences("RFI_File", MODE_PRIVATE);
		coverage = checkPreferences.getString("coverage", "");
		drawing = checkPreferences.getString("drawing", "");

		drwng_text.setText(coverage + "/" + drawing);
		drwng_text.setClickable(false);

		db = new RfiDatabase(getApplicationContext());
		setListviewdata();
		
	}

	public void setListviewdata() {

		scrol_text.setText(db.selectedSchemeName + " / "
				+ db.selectedclientname + "/" + db.selectedChecklistName);

		// String
		// whereClause="Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"'";
	/*	String whereClause = "q.PK_question_id=r.QUE_Id AND " + "r.RFI_Id='"
				+ db.selectedrfiId + "'  AND  r.CHKL_Id='"
				+ db.selectedChecklistId + "' AND r.GRP_Id='"
				+ db.selectedGroupId + "' AND q.NODE_Id='"
				+ db.selectedBuildingId + "' AND q.user_id='" + db.userId + "'";*/



		String whereClause = "q.PK_question_id=r.QUE_Id AND " + "r.RFI_Id='"
				+ db.selectedrfiId + "'  AND  r.CHKL_Id='"
				+ db.selectedChecklistId + "' AND r.GRP_Id='"
				+ db.selectedGroupId + "' AND q.NODE_Id='"
				+ db.selectedBuildingId + "' AND q.user_id='" + db.userId + "'";




		Cursor cursor = db
				.select("question as q,Check_update_details r",
						"distinct(q.PK_question_id),q.QUE_Des,q.QUE_SequenceNo,q.QUE_Type,r.Coverage,r.Answer "
								+ ",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4,r.RFI_Id",
						whereClause, null, null, null, null);
		String[] q_id = new String[cursor.getCount()];
		String[] q_text = new String[cursor.getCount()];
		int count = 0;

		System.out.println("cursor lenght---------" + cursor.getCount());
		db.questionCount= String.valueOf(cursor.getCount());
		System.out.println("Question Count is: "+cursor.getCount());
		if (cursor.moveToFirst()) {
			do {
				/*
				 * Qdataid[cursor.getPosition()] =
				 * Integer.parseInt(cursor.getString(0));
				 * questionData[cursor.getPosition()] = cursor.getString(1);
				 */

				question_seq_id = cursor.getString(cursor
						.getColumnIndex("QUE_SequenceNo"));
				question_type = cursor.getString(cursor
						.getColumnIndex("QUE_Type"));
				current_ques_id = cursor.getString(cursor
						.getColumnIndex("PK_question_id"));
				System.out.println("calling method.........");
				// r.Coverage,r.Answer
				// " +",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4

				System.out.println("Inside setListViewData Method");
				System.out.println("Cursor Count : " + cursor.getCount());
				System.out.println("Cursor Column Copunt : " + cursor.getColumnCount());
				System.out.println("Cursor Data : ");
				for (int i = 0; i < cursor.getCount(); i++) {
					for (int j = 0; j < cursor.getColumnCount(); j++) {
						System.out.println("Cursor(" + i + " + Column(" + j + ") = " + cursor.getString(j));
					}
				}
				
				InsertDefaultAnswer(cursor.getString(4).toString(), cursor
						.getString(5).toString(), cursor.getString(6)
						.toString(), cursor.getString(7).toString(), cursor
						.getString(8).toString(), cursor.getString(9)
						.toString(), cursor.getString(10).toString(), cursor
						.getString(0).toString());
				System.out.println("question data-------"
						+ cursor.getString(0).toString());
				System.out.println("RFI  ID-------" + db.selectedrfiId);

				q_id[count] = cursor.getString(0).toString();
				q_text[count] = cursor.getString(1).toString();
				count++;
				System.out.println("IN CUrsor---id"
						+ cursor.getString(0).toString());
				System.out.println("IN CUrsor---Question"
						+ cursor.getString(1).toString());
			} while (cursor.moveToNext());
		} else {
			System.out.println("not question-----------------");
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		// Setting value to adapter class

		CheckAdapterRfi adapter = new CheckAdapterRfi(CheckRfiScroll.this,
				q_id, q_text, coverage);
		listview.setAdapter(adapter);

		listview.setDividerHeight(4);
		
		//Setting value  to adapter class
		
		
		if(!db.LastSelectedListItem.equalsIgnoreCase("")){
			
			
			listview.setSelection(Integer.parseInt(db.LastSelectedListItem));
			listview.setFocusableInTouchMode(true);
			db.LastSelectedListItem="";
		}

	}

	// //r.Coverage,r.Answer " +",r.Remark,r.Image1,r.Image2,r.Image3,r.Image4
	public void InsertDefaultAnswer(String Coverage1, String Answer1,
			String Remark1, String Image11, String Image21, String Image31,
			String Image41, String que_id) {

		String whereClause1 = "Rfi_id='" + db.selectedrfiId
				+ "' AND FK_question_id='" + que_id + "' AND user_id='"
				+ db.userId + "'";
		Cursor cursor1 = db.select("CheckAnswer", "FK_question_id",
				whereClause1, null, null, null, null);
		System.out.println("Inside InsertDefaultAnswer Method.....");
		System.out.println("Cursor Count : " + cursor1.getCount());
		System.out.println("Cursor Column Count : " + cursor1.getColumnCount());
		System.out.println("Cursor Data : ");
	/*	for (int i = 0; i < cursor1.getCount(); i++) {
			for (int j = 0; j < cursor1.getColumnCount(); j++) {
				System.out.println("Cursor(" + i + " + Column(" + j + ") = " + cursor1.getString(j));
			}
		} */
		boolean isanswerd = false;
		// check for update question
		if (cursor1.moveToFirst()) {
			System.out.println("Curosr.moveToFirst() : " + cursor1.moveToFirst());
			do {
				isanswerd = true;

			} while (cursor1.moveToNext());
		} else {
			System.out.println("not question-----------------");
		}

		if (cursor1 != null && !cursor1.isClosed()) {
			cursor1.close();
		}

		if (!isanswerd) {
			Intent int1 = getIntent();
			String coverage = int1.getStringExtra("coverage");

			// saving answer
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String rfi_date = df.format(new Date());

			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String rfi_date1 = df1.format(new Date());

			String rfidata = "'" + db.selectedClientId + "','"
					+ db.selectedSchemeId + "','" + db.selectedlevelId + "','"
					+ db.selectedBuildingId + "','" + db.selectedFloorId
					+ "','" + db.selectedUnitId + "','" + db.selectedSubUnitId
					+ "','" + db.selectedElementId + "','"
					+ db.selectedSubElementId + "','" + Coverage1 + "','"

					+ " " + "','" + "" + "','" + "" + "','" + "" + "','" + ""
					+ "','" + "" + "','" + db.selectedrfiId + "','" + rfi_date
					+ "','" + current_ques_id + "','" + question_seq_id + "','"
					+ question_type + "','" + db.selectedNodeId + "','"
					+ db.selectedChecklistId + "','" + db.selectedGroupId
					+ "','" + db.selectedWorkTypeId + "','" + rfi_date1 + "','"
					+

					"0" + "','" + db.userId + "','0'";

			db.insert(
					"CheckAnswer",
					"CL_Id,PRJ_Id,Level_int,Structure_Id,Stage_Id,"
							+ "Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,Coverage_str,ans_text,remark,snap1,snap2,snap3,snap4,Rfi_id,dated,"
							+ "FK_question_id,FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id, Fk_Grp_ID,workType_id,check_date,answerFlag,user_id,isAnswered",
					rfidata);
			System.out
					.println(" default answer data inserted ================="
							+ rfidata);

			/*
			 * "CL_Id,PRJ_Id,Level_int,Structure_Id,Stage_Id,"+
			 * "Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,Coverage_str,ans_text,remark,snap1,snap2,snap3,snap4,Rfi_id,dated,"
			 * +
			 * "FK_question_id,FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id, Fk_Grp_ID,workType_id,user_id"
			 * , rfidata);
			 */

		}
		
		checkAnswer(que_id);

	}

	public void nevigateHomeScreen() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setCancelable(false);
		alertDialog.setMessage("Do you want to save this RFI?");
		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				getScreenToBeDisplayed();
				/*
				 * SelectQuestion.destruct.finish(); finish(); Intent int1=new
				 * Intent(getApplicationContext(),SelectQuestion.class);
				 * int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); finish();
				 * startActivity(int1);
				 */

			}
		});
		alertDialog.setButton2("NO", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				deleteAnswer();

				// SelectQuestion.destruct.finish();
				finish();
				Intent int1 = new Intent(CheckRfiScroll.this, CheckFRI.class);
				int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(int1);

				// dialog.dismiss();
			}
		});

		alertDialog.setButton3("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		alertDialog.show();

	}

	
	
	
	
	
	public void checkAnswer(String que_id){
		
		CheckQuestionireScroll check=new CheckQuestionireScroll();
		boolean flag=check.questionFlag;
		
		
		if(flag){
			
			check.questionFlag = false;
			
			//setting background colour
			String whereClause121="FK_question_id='"+que_id+
					"' AND Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"
					+db.selectedGroupId+"' AND  Rfi_id='"+db.selectedrfiId+"' AND user_id='"+db.userId+"'";
			
			Cursor questionAnswerCursor = db.select("CheckAnswer", "ans_text",whereClause121, null, null, null, null);
			
			

			if (questionAnswerCursor.moveToFirst()) {
				do {

					System.out.println("checkscroll....");
					System.out.println("answer text...."
							+ questionAnswerCursor
									.getString(questionAnswerCursor
											.getColumnIndex("ans_text")));

					if (questionAnswerCursor.getString(
							questionAnswerCursor.getColumnIndex("ans_text"))
							.equalsIgnoreCase("0")) {
						System.out.println("ok--------------");
					} else {
						// update checkanswer table

						
						db.update("CheckAnswer", "ans_text='"
								+ "1" +"'", "Rfi_id=" + db.selectedrfiId
								+ " AND CL_Id=" + db.selectedClientId + " AND PRJ_Id="
								+ db.selectedSchemeId + " AND Structure_Id="
								+ db.selectedBuildingId + " AND FK_question_id='"
								+ que_id + "'");

						
						

					}

				} while (questionAnswerCursor.moveToNext());
			}

			
			
			
			
			
			
			
		}else{
			System.out.println("Question Flag = " + check.questionFlag);
		}
		
	}
	
	
	
	
	
	public void deleteAnswer() {
		System.out.println("answer deleted...");
		db.delete("CheckAnswer", "Rfi_id='" + db.selectedrfiId + "'");
	}

	protected void getScreenToBeDisplayed() {

		updateRfiTable();
		cleanData();
		db.closeDb();
		// UpdateRfi.updateclass.finish();
		finish();

		// startActivity(new Intent(Update_Questionire.this,HomeScreen.class));
		Intent inte1 = new Intent(CheckRfiScroll.this, CheckFRI.class);
		inte1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(inte1);

	}

	public void updateRfiTable() {
		db.update("Check_update_details", "status_device='" + "complete'",
				"user_id1='" + db.userId + "' AND RFI_Id='" + db.selectedrfiId
						+ "'");
	}

	private void cleanData() {
		db.selectedSchemeId = "";
		db.selectedSchemeName = "";
		db.selectedclientname = "";
		db.selectedClientId = "";
		db.selectedBuildingId = "";
		db.selectedFloorId = "";
		db.selectedUnitId = "";
		db.selectedSubUnitId = "";
		db.selectedElementId = "";
		db.selectedSubElementId = "";
		db.selectedChecklistId = "";
		db.selectedChecklistName = "";
		db.selectedGroupId = "";

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		// nevigateHomeScreen();
		finish();
		Intent int1 = new Intent(getApplicationContext(), CheckFRI.class);
		startActivity(int1);

	}

}
