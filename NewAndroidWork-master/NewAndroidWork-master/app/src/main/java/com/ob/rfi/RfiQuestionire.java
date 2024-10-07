package com.ob.rfi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ob.rfi.db.RfiDatabase;






import android.R.array;
import android.text.Spanned;
import android.text.TextUtils;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class RfiQuestionire extends CustomTitleDuplicate{
	private RfiDatabase db;
	private LinearLayout parentLayout;
	private Vibrator vibrator;
	private String timestamp;
	private boolean backPressed=false;
	private boolean firstTime=false;
	static int imageCount = 0;
	private Cursor questionCursor;
	private int Qdata=0;
	private ArrayList<String> arrayList=null;
	private int totalcount;
	private int max;
	private AlphaAnimation anim;
	RadioButton[] radioButtons; 
	private EditText remark;  
	private static Button imageButton;
	
	String [] snaps = new String[] {"",""};
	
	String [] temp_snaps = new String[] {"",""};
	
	private Button next;
	private String answer;
	private String current_ques_id;
	private String question_seq_id;
	private String question_type;
	private String rfi_id="1";
	private File sdImageMainDirectory;
	private String coveragestring;
	private Cursor questionAnswerCursor;
	private String previousAns;
	private String previousRem;
	private boolean isanwered;
	private String TempsnapLoacl1;
	private String TempsnapLoacl2;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rfi_question);
	
		db = new RfiDatabase(getApplicationContext());
		
		parentLayout = (LinearLayout)findViewById(R.id.ParentLayout);
		vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		Button back = (Button)findViewById(R.id.backward);
		
		Button home = (Button)findViewById(R.id.Home_button);
		
		db.setSpinner = false;
		Intent intentdata=getIntent();
		coveragestring=intentdata.getStringExtra("coverage");
		
		Calendar c1 = Calendar.getInstance();      
		int datetime=c1.get(Calendar.MONTH)+1;
		System.out.println("month reeeeeeeeeeeee-----------"+datetime);
		timestamp = c1.get(Calendar.DAY_OF_MONTH) +"/"+ datetime +"/"+ c1.get(Calendar.YEAR) +" "+ c1.get(Calendar.HOUR_OF_DAY) +":"+ c1.get(Calendar.MINUTE) +":"+ c1.get(Calendar.SECOND);
		 
		if(!backPressed){
			firstTime = true;
			//	NcCount++;
		}
		arrayList = new ArrayList<String>(); 
		
		
		
		
		System.out.println("selected node "+db.selectedBuildingId);
		
		//String whereClause="Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"'";
		String whereClause="Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"' AND NODE_Id='"+db.selectedBuildingId+
				"' AND user_id='"+db.userId+"'";
		
		//PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id
		
		Cursor cursor = db.select("question", "distinct(PK_question_id),QUE_Des,QUE_SequenceNo,QUE_Type",whereClause, null, null, null, null);
		 
		if(cursor.moveToFirst()){ 
			do{
				/*Qdataid[cursor.getPosition()] = Integer.parseInt(cursor.getString(0));
				questionData[cursor.getPosition()] = cursor.getString(1);*/
				
				System.out.println("question data-------"+cursor.getString(0).toString());
				arrayList.add(cursor.getString(0).toString());
			}while(cursor.moveToNext());
		}else
		{
			System.out.println("not question-----------------");
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		
		totalcount=arrayList.size();
		max=totalcount;
		
		current_ques_id=arrayList.get(Qdata).toString();
		
		
		
		//---------------------------------------------
		imageButton = (Button)findViewById(R.id.capture);
		//----------back button--------
		back.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				backPressed = true;
				 
				if(Qdata != 0){
					Qdata--;
					/*Cursor cursorNcNo = db.select("Answer", "Nc_No",whereClause+" AND FK_Question_ID='"+
							questionData[Qdata]+"' AND Nc_No<>0", null, null, null, null);
					if(cursorNcNo.moveToFirst()){
						NcCount = Integer.parseInt(cursorNcNo.getString(0));
						if(NcCount>0){
							updateNcCount(getNcCount()-1);*/
						//	db.update("Answer", "Nc_No=0",whereClause+" AND FK_Question_ID='"+questionData[Qdata]+"'");										

					/*	}


					}
					cursorNcNo.close();
	*/



					getQuestionData(Qdata,arrayList.get(Qdata).toString());  

				} else {
					showMassage("You are at first question.");
				}
			}
		}); 
		
		
		
		
		
		
		imageButton.setOnClickListener(new OnClickListener() {
			private Object tempFilename;

			@Override
			public void onClick(View v) {
				System.out.println("imagae count========="+imageCount);
				if(imageCount < 2){ 
					
					try {  
						File root = new File(Environment.getExternalStorageDirectory()
								+ File.separator + "RFI" + File.separator);
						root.mkdirs();
						Date d = new Date();
						String g=db.selectedrfiId.replace(" ","_");
						String imageName = g+"_"+db.selectedNodeId.trim()+"_"+db.selectedChecklistId.trim()+"_"+
						db.selectedGroupId.trim()+"_"+db.userId.trim()+"_"+current_ques_id+"_"+question_seq_id+"_"+question_type
								+ "_" + d.getTime()+"_" + imageCount+".jpg";
						//snaps[imageCount] = imageName;
						
						/*if(!snaps[0].equalsIgnoreCase("")){
							snaps[1] = imageName;
							System.out.println("first cinduition.........");
						}else*/
						if (!snaps[0].equalsIgnoreCase("") && !snaps[1].equalsIgnoreCase("")){
							snaps[0] = imageName;
							System.out.println("second.. cinduition.........");	
						} else if(!snaps[0].equalsIgnoreCase("")){
							snaps[1] = imageName;
							System.out.println("first cinduition.........");
						}
						
						else{
							System.out.println("third cinduition.........");
							snaps[imageCount] = imageName;
						}
						
						
						System.out.println("Image Name "+imageName);
						sdImageMainDirectory = new File(root, imageName);
						tempFilename = getNextFileName().getAbsolutePath();
						startCameraActivity();
						
						
					
					} 
					catch (Exception e) {}
				}else{
					vibrator.vibrate(500);
					Toast.makeText(getApplicationContext(), "Limit for maximum(2) images is reached", Toast.LENGTH_SHORT).show();
				}
			}
		}); 
		
		
		next = (Button)findViewById(R.id.forwards);
		
		try{
		next.setOnClickListener(new OnClickListener() {

			
	 
			

			@Override
			public void onClick(View arg0) {
				backPressed = false;
				answer = "";
				//questionData[Qdata]="";
				
				for(int i=0;i<radioButtons.length;i++){
					if(radioButtons[i].isChecked()){
						answer = String.valueOf(radioButtons[i].getId());
						System.out.println("selected answer==="+answer);

					}
				}
				if(answer.equals(""))
					showMassage("Please select the answer");
				else{
					current_ques_id=arrayList.get(Qdata).toString();
					saveAnswer();
					remark.setText("Remarks");
					imageCount=0;
					
					if(++Qdata==max)
					{
						
					System.out.println("last ans----------");
					setalert();
					
					}
					else
					{
						--Qdata;
					getQuestionData(++Qdata,arrayList.get(Qdata).toString());
					}
					//getQuestionData(++Qdata,arrayList.get(Qdata).toString());
					
				}
			}
		});
		
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("message="+e.getMessage());
		}
		
		
		
		//----------Home button
		
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			nevigateHomeScreen();
			}
		});
		
		
		
		
		
		inticomponent();
		getQuestionData(Qdata,arrayList.get(Qdata).toString());
		System.out.println("dddddddddddddddddddddd");
		
		
		
		
		 
		
	} 
	
	public void deleteAnswer(){
		System.out.println("answer deleted...");
		db.delete("Answer", "Rfi_id='" + db.selectedrfiId + "'");
	}
	
	public void nevigateHomeScreen(){
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setCancelable(false);
		alertDialog.setMessage("Do you want to Exit?");
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				deleteAnswer();
				SelectQuestion.destruct.finish();
				finish();
			Intent int1=new Intent(RfiQuestionire.this,HomeScreen.class);
			int1.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(int1); 
					}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	
 
	} 
	
	protected void setalert() {
		// TODO Auto-generated method stub
	imageButton.setClickable(false);
	AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
	builder.setCancelable(false);
	builder.setMessage("Inspection completed for "+db.selectedChecklistName+".");
	
	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			
	//		SaveCreatedRfi();
			getScreenToBeDisplayed(); 
		}
	});
	builder.create();
	builder.show();
	} 
	
public void inticomponent() {
	//Button back = (Button)findViewById(R.id.backward);
	anim = new AlphaAnimation(0.1F, 1.F);
	anim.setDuration(500); 
	anim.setRepeatMode(Animation.RESTART);
	anim.setRepeatCount(0);
	
	/*imageButton = (Button)findViewById(R.id.capture);
	//----------back button--------
	back.setOnClickListener(new OnClickListener() {
		

		@Override
		public void onClick(View v) {
			backPressed = true;
			 
			if(Qdata != 0){
				Qdata--;
				Cursor cursorNcNo = db.select("Answer", "Nc_No",whereClause+" AND FK_Question_ID='"+
						questionData[Qdata]+"' AND Nc_No<>0", null, null, null, null);
				if(cursorNcNo.moveToFirst()){
					NcCount = Integer.parseInt(cursorNcNo.getString(0));
					if(NcCount>0){
						updateNcCount(getNcCount()-1);
					//	db.update("Answer", "Nc_No=0",whereClause+" AND FK_Question_ID='"+questionData[Qdata]+"'");										

					}


				}
				cursorNcNo.close();




				getQuestionData(Qdata,arrayList.get(Qdata).toString());  

			} else {
				showMassage("You are at first question.");
			}
		}
	}); 
	
	
	
	
	
	
	imageButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(imageCount < 3){ 
				
				try { 
					File root = new File(Environment.getExternalStorageDirectory()
							+ File.separator + "RFI" + File.separator);
					root.mkdirs();
					String imageName = db.selectedNodeId.trim()+"_"+db.selectedChecklistId.trim()+"_"+
					db.selectedGroupId.trim()+"_"+db.userId.trim()+"_"+current_ques_id+"_"+question_seq_id+"_"+question_type+"_"+imageCount+".jpg";
					snaps[imageCount] = imageName;
					System.out.println("Image Name "+imageName);
					sdImageMainDirectory = new File(root, imageName);
				
					startCameraActivity();
					
					
				
				} 
				catch (Exception e) {}
			}else{
				vibrator.vibrate(500);
				Toast.makeText(getApplicationContext(), "Limit for maximum(2) images is reached", Toast.LENGTH_SHORT).show();
			}
		}
	}); 
	
	
	next = (Button)findViewById(R.id.forwards);
	next.setOnClickListener(new OnClickListener() {

		
 
		

		@Override
		public void onClick(View arg0) {
			backPressed = false;
			answer = "";
			//questionData[Qdata]="";
			
			for(int i=0;i<radioButtons.length;i++){
				if(radioButtons[i].isChecked()){
					answer = String.valueOf(radioButtons[i].getId());

				}
			}
			if(answer.equals(""))
				showMassage("Please select the answer");
			else{
				current_ques_id=arrayList.get(Qdata).toString();
				saveAnswer();
				
				getQuestionData(++Qdata,arrayList.get(Qdata).toString());
				
			}
		}
	});*/
	
	
	
	
}	
	
	
	private void getQuestionData(int sr,String Q_id) {
		try{ 
		questionCursor = null;
		imageCount=0;
		isanwered=false;
		TempsnapLoacl1="";
		TempsnapLoacl2="";
		System.out.println("q___________----iddddddddddd"+Q_id);
		
		String whereClause12="PK_question_id='"+Q_id+
				"' AND Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"
				+db.selectedGroupId+"' AND NODE_Id='"+db.selectedBuildingId+"' AND user_id='"+db.userId+"'";
		
		//Cursor cursor = db.select("question", "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type",whereClause, null, null, null, null);
		System.out.println("total size=============----"+max);
		
		//check if answer previously
		
		
		
		/*String whereClause121="FK_question_id='"+Q_id+
				"' AND Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"
				+db.selectedGroupId+"' AND user_id='"+db.userId+"'";*/
		
		String whereClause121="FK_question_id='"+Q_id+
				"' AND Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"
				+db.selectedGroupId+"' AND  Rfi_id='"+db.selectedrfiId+"' AND user_id='"+db.userId+"'";
		
		questionAnswerCursor = db.select("Answer", "ans_text,remark,snap1,snap2",whereClause121, null, null, null, null);
		int cnt=0;
		if(questionAnswerCursor.moveToFirst()){
			do{
				isanwered=true;
			System.out.println("question data....");
			System.out.println("answer...."+questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("ans_text")));
			System.out.println("remark...."+questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("remark")));
			System.out.println("snap1...."+questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap1")));
			System.out.println("snap2...."+questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap2")));
			cnt++;
			
			
			previousAns=questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("ans_text"));
			previousRem=questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("remark"));
			TempsnapLoacl1=questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap1"));
			TempsnapLoacl2=questionAnswerCursor.getString(questionAnswerCursor.getColumnIndex("snap2"));
			
			
			}while(questionAnswerCursor.moveToNext());
		} 
		//Storing already answered images
		
		if(cnt>0){
		if(!TempsnapLoacl1.equalsIgnoreCase("")){
			//temp_snaps[0]=TempsnapLoacl1;
			snaps[0]=TempsnapLoacl1;
		}else{
			snaps[0]="";
		}
		if(!TempsnapLoacl2.equalsIgnoreCase("")){
			//temp_snaps[1]=TempsnapLoacl2;
			snaps[1]=TempsnapLoacl2;
		}else{
			snaps[1]="";
		}
		}
		
		
		if(sr < max){
			System.out.println("where clause----"+whereClause12);
			questionCursor = db.select("question", "distinct(PK_question_id),QUE_Des,QUE_SequenceNo,QUE_Type",whereClause12, null, null, null, null);
				questionCursor.moveToNext();
				
				
				if(questionCursor.moveToFirst()){
					do{
						System.out.println("calling----------------");
						TextView questionText = (TextView) findViewById(R.id.questiontext);
						
						question_seq_id=questionCursor.getString(questionCursor.getColumnIndex("QUE_SequenceNo"));
						question_type=questionCursor.getString(questionCursor.getColumnIndex("QUE_Type"));
						questionText.setText("Q."+(sr+1)+") "+questionCursor.getString(questionCursor.getColumnIndex("QUE_Des")));
						questionText.setAnimation(anim);
						questionText.startAnimation(anim); 
						SingleChoiceLayout(); 
						
						System.out.println("question data-------"+questionCursor.getString(0).toString());
						arrayList.add(questionCursor.getString(0).toString());
					}while(questionCursor.moveToNext());
				}else
				{
					Qdata++;  
					System.out.println("not question-----------------");
				}

				/*if (questionCursor != null && !questionCursor.isClosed()) {
					questionCursor.close();
				}
*/
				
				
				
				
				
				
				
				
				
				
				 
				
			/*if(questionCursor.moveToNext()){
				System.out.println("calling----------------");
				TextView questionText = (TextView) findViewById(R.id.questiontext);
				
				question_seq_id=questionCursor.getString(questionCursor.getColumnIndex("QUE_SequenceNo"));
				question_type=questionCursor.getString(questionCursor.getColumnIndex("QUE_Type"));
				questionText.setText("Q."+(sr+1)+") "+questionCursor.getString(questionCursor.getColumnIndex("QUE_Des")));
				questionText.setAnimation(anim);
				questionText.startAnimation(anim); 
				SingleChoiceLayout(); 
			}else{
				Qdata++;  
			}*/
		} else { 

			if (db.selectedChecklistId.equalsIgnoreCase("") || db.selectedSchemeId.equalsIgnoreCase("") 
					|| db.selectedBuildingId.equalsIgnoreCase("")) {
				logout();
			} else {
				imageButton.setClickable(false);
				AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
				builder.setCancelable(false);
				builder.setMessage("Inspection completed for "+db.selectedChecklistName+".");
				
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						SaveCreatedRfi();
						getScreenToBeDisplayed(); 
					}
				});
				builder.create();
				builder.show();
			}
		}
		}catch(Exception e){
			System.out.println("combination error");
		}
	}
	
	
	
	
	public void SaveCreatedRfi()
	{
		
		
	//	Rfi_check(rfi_no TEXT,user_id TEXT)");
		
		String rfidata = "'" + db.selectedrfiId + "','"+ db.userId + "'";
		db.insert("Rfi_New_Create","FK_rfi_Id,user_id",rfidata);
		System.out.println("data inserted rfi created==="+rfidata);
		
	}
	
	
	
	
	
	private void SingleChoiceLayout() { 

			parentLayout.removeAllViews();  
			LinearLayout childLayout = new LinearLayout(this);
			childLayout.setOrientation(LinearLayout.VERTICAL);
			childLayout.setGravity(Gravity.CENTER);
			ScrollView scroll = new ScrollView(this);

			
			RadioGroup radioGroup = new RadioGroup(this);
			radioGroup.setOrientation(LinearLayout.VERTICAL);  
			radioGroup.setFocusable(true);
			radioGroup.setFocusableInTouchMode(true);
			radioGroup.setGravity(Gravity.LEFT);

			//String[] options = questionCursor.getString(questionCursor.getColumnIndex("QUE_Des")).split("~");

			String[] options = {"Conformity","Non Conformity","N/A"};
			
			radioButtons = new RadioButton[options.length];
			for(int i=0; i<options.length; i++){

				radioButtons[i] = new RadioButton(getApplicationContext());
				radioButtons[i].setText(options[i]);
				radioButtons[i].setTextColor(Color.WHITE);
				radioButtons[i].setChecked(false);
				radioButtons[i].setId(i);
				radioButtons[i].setTag(i);
				
				if(options[i].toString().trim().equalsIgnoreCase(db.selectedClient.trim())){
					radioButtons[i].setChecked(false);
				}
				remark = new EditText(getApplicationContext());
				remark.setHint("Remarks");
				remark.setTextColor(Color.WHITE);
				remark.setHintTextColor(Color.WHITE);
				radioGroup.addView(radioButtons[i]);
				imageButton.setVisibility(View.VISIBLE);
				//direction.setVisibility(View.VISIBLE);
 
				/*snaps = new String[]{"",""}; 
				imageCount=0;
				imageButton.setText("Capture Image("+imageCount+"/2)");*/
				
				
				/*radioButtons[i].setOnClickListener(new OnClickListener() {

					@Override 
					public void onClick(View v) {

						RadioButton rb =(RadioButton)v;
						//checkNc(rb);

					}
				}); */
			}



		InputFilter filter = new InputFilter() {
			public CharSequence filter(CharSequence source, int start, int end,
									   Spanned dest, int dstart, int dend) {
				char[] chars = {'\'','"','@','/','$','&','?','!','*','<','>','~'};
				for (int i = start; i < end; i++) {
					if (new String(chars).contains(String.valueOf(source.charAt(i)))) {
						return "";
					}
				}
				return null;
			}
		};
			remark.setFilters(new InputFilter[]{filter});

			String QUE_Type = questionCursor.getString(questionCursor.getColumnIndex("QUE_Type"));

			childLayout.addView(radioGroup);
			
			childLayout.addView(remark);
			scroll.addView(childLayout);
			
			
			parentLayout.addView(scroll);  
			
			/*ans_text TEXT,remark TEXT,snap1 TEXT,snap2 TEXT" +
			",Rfi_id TEXT,dated TEXT,FK_question_id TEXT, " +
			"FK_QUE_SequenceNo Text,FK_QUE_Type TEXT, FK_NODE_Id TEXT,Fk_CHKL_Id TEXT, Fk_Grp_ID TEXT,user_id TEXT)
*/
			// Check Database if Question was answered previously or not
			
			String whereClause1="FK_question_id='"+current_ques_id+
					"' AND Fk_CHKL_Id='"+db.selectedChecklistId+"' AND Fk_Grp_ID='"+db.selectedGroupId+"' AND user_id='"+db.userId+"'";
			
			
			/*Cursor cursor = db.select("Answer", "ans_text,remark,snap1,snap2", whereClause1,
					null, null, null, null);*/

			String ans = "";
			String remarks = "";
			int myNum = 0;

			try {
			    myNum = Integer.parseInt(previousAns);
			} catch(NumberFormatException nfe) {
			   System.out.println("Could not parse " + nfe);
			} 
			

			if(isanwered){
				remark.setText(previousRem);
				radioButtons[myNum].setChecked(true);
			}else{
				radioButtons[1].setChecked(true);
				remark.setText(remarks);
			}
			
			
			TextView surveyDetail = (TextView)findViewById(R.id.roomTypeText); 
			surveyDetail.setText(db.selectedSchemeName+" / "+
					db.selectedclientname+"/"+db.selectedChecklistName);
			int length = surveyDetail.getText().toString().length();
			if(length > 42){
				Animation anim = new TranslateAnimation(5, -((length-42)*7), 0, 0);
				anim.setDuration(6000); 
				anim.setRepeatMode(Animation.RESTART);
				anim.setRepeatCount(Animation.INFINITE);
				surveyDetail.setAnimation(anim);
				int screenWidth = length*7+5;
				surveyDetail.setLayoutParams(new LinearLayout.LayoutParams( screenWidth, LayoutParams.WRAP_CONTENT));
			}
			surveyDetail.requestFocus();

			//((TextView)findViewById(R.id.roundNoText)).setText("CheckListName : "+db.selectedChecklistName);
			((TextView)findViewById(R.id.tradeText)).setText("Question.Type : "+QUE_Type);
		
	
	
	}
	
	
	 
	protected void saveAnswer() { 
		
		firstTime = false; 
		backPressed = false;
		

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String rfi_date=df.format(new Date());
		System.out.println("images are :::"+snaps[0]+snaps[1]);		
		//questionData[Qdata]="";
		String img3="";
		String img4="";
		
		
		String rfidata = "'"+db.selectedClientId + "','"+db.selectedSchemeId+ "','"+db.selectedlevelId+ "','"+db.selectedBuildingId+ "','"+db.selectedFloorId+ "','"+
				db.selectedUnitId + "','"+db.selectedSubUnitId+ "','"+db.selectedElementId+ "','"+db.selectedSubElementId+ "','"+coveragestring + "','"
				
				+ answer + "','"
				+ remark.getText().toString() + "','" + snaps[0] + "','" 
				+ snaps[1]+"','" +img3+ "','" +img4+"','" + db.selectedrfiId + "','"
						+ rfi_date  + "','"+ current_ques_id+ "','" +question_seq_id+ "','" + question_type
				+ "','" + db.selectedNodeId + "','" + db.selectedChecklistId + "','" +db.selectedGroupId+ "','"+" "+ "','"+ db.userId + "'";
		
		
		
		
		
		
		String whereClause1="Rfi_id='"+db.selectedrfiId+"' AND FK_question_id='"+current_ques_id +"' AND user_id='"+db.userId+"'"; 
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
		
		if(!isanswerd)
		{
			db.insert(
					"Answer",
					"CL_Id,PRJ_Id,Level_int,Structure_Id,Stage_Id,"+
					"Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,Coverage_str,ans_text,remark,snap1,snap2,snap3,snap4,Rfi_id,dated," +
					"FK_question_id,FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id, Fk_Grp_ID,checker_remark,user_id",
					rfidata);
				System.out.println("inserted================="+rfidata);
			
			
		}else
		{
			db.update("Answer", "ans_text='"
					+ answer + "',remark='"+ remark.getText().toString()+"',snap1='"+snaps[0]+"',snap2='"+snaps[1]+"',snap3='"+img3+"',snap4='"+img4+"'", "Rfi_id='" + db.selectedrfiId
					+ "' AND CL_Id=" + db.selectedClientId + " AND PRJ_Id="
					+ db.selectedSchemeId + " AND Structure_Id="
					+ db.selectedBuildingId + " AND FK_question_id='"
					+ current_ques_id + "'");
			  
			
			System.out.println("updated================="+rfidata);
			
		}
		
		  
		
		
		
		snaps = new String [] {"",""};
		imageCount=0;
		System.out.println("image count in saveanswer================="+imageCount);
		imageButton.setText("Capture Image(0/2)");
		remark.setText("");
		 
	}
	
	
	protected void getScreenToBeDisplayed() {  

		db.delete("Rfi_New_Create", "user_id='" + db.userId + "'");		
		db.insert("Rfi_New_Create", "FK_rfi_Id,user_id","'"+SelectQuestion.nval+ "','"+db.userId+"'");
		System.out.println("inserted id data================="+SelectQuestion.nval);
			cleanData();
			db.closeDb();
			finish();
			//startActivity(new Intent(RfiQuestionire.this,HomeScreen.class));
			SelectQuestion.slectionclass.finish();
			Intent inte1=new Intent(RfiQuestionire.this,HomeScreen.class);
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
	
	
	
	
	
	
	
	
	
	
	public void startCameraActivity() {
		//		Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
		//		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, 1);
	} 
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	
		 
		System.out.println(" after onresume................");
		switch (resultCode) {
		case 0: 
	 			
			System.out.println(" image discrdedddddddddddddddd................");
			break;
		case -1:
			Bitmap bm = null;
			try {
				System.out.println("image saved ...............");
				imageCount++;
				imageButton.setText("Capture Image("+imageCount+"/2)");
				//				bm = Media.getBitmap(Questionnaire.this.getContentResolver(), Uri.parse(data.toURI()));
				Bundle b = data.getExtras(); 
				bm = (Bitmap) b.get("data");
				FileOutputStream out = new FileOutputStream(sdImageMainDirectory);
				float aspect = (float)bm.getWidth() / bm.getHeight();
				int width = 720;
				int height = (int) (width/aspect);
				FontMetrics fm = new FontMetrics();
				Paint mpaint = new Paint();
		        mpaint.setColor(Color.WHITE);
		        mpaint.setStyle(Style.FILL);
				
				
				bm = Bitmap.createScaledBitmap(bm, width, height, true);
				
				// Write text on bitmap
				Canvas canvas = new Canvas(bm);
				
	            Paint paint = new Paint();
	            paint.setTextSize(20);
	            paint.setColor(Color.RED); // Text Color
	            paint.setStrokeWidth(13); // Text Size
	            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
	            // some more settings...
	            canvas.drawRect(5, 5,200, 60, mpaint);
	            canvas.drawBitmap(bm, 0, 0, paint);
	            canvas.drawText(timestamp, 10, 20, paint);
				
				bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
				//				bm = getResizedBitmap(bm, 150);
				bm.recycle();

			} catch (FileNotFoundException e) {
				e.printStackTrace(); 
			} catch (OutOfMemoryError ome){
				ome.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	public void checkNc(RadioButton v){

		if(v.isChecked()){
			remark.setText(v.getText());
			if(v.getId()==3 ||v.getId()==0){	
			
				snaps = new String[]{"",""}; 
				imageCount=0;
				imageButton.setText("Capture Image("+imageCount+"/2)");
				//imageButton.setVisibility(View.GONE);
				
			}else{
			//	showMassage("Please note down NC no");
				imageButton.setVisibility(View.VISIBLE);
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	protected void showMassage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton(" Ok ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create();
		builder.show();
	}
	
	
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.questionnaire_menu, menu);
		return true;
	}
 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			System.out.println("item id=="+item.getItemId());
			switch (item.getItemId()) {
			case R.id.viewImage:
				showImage();
				return true;
			}
		} catch (Exception aE) {
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/*private void showImage() {

		if (imageCount < 1) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					RfiQuestionire.this);
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
			Intent i = new Intent(RfiQuestionire.this, ViewImagesActivity.class);
			i.putExtra("data", list.toArray(new String[] {}));
			System.out.println("String Activity+++++++++");
			startActivity(i);
		}
	}
	*/
	
	
	
	
	
	
	private void showImage() {
if (imageCount < 1 ) {
		if (snaps[0].equalsIgnoreCase("")) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					RfiQuestionire.this, R.style.MyAlertDialogStyle);
			builder.setMessage("Images not available");
			builder.setNegativeButton(" Ok ",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create();
			builder.show();

		}
		
		
		else {
			List<String> list = new ArrayList<String>();
			if(!temp_snaps[1].equalsIgnoreCase("") && !snaps[0].equalsIgnoreCase("")){
				list.add(temp_snaps[1]);
				list.add(snaps[0]);
				
			}else if(!temp_snaps[0].equalsIgnoreCase("") | !temp_snaps[1].equalsIgnoreCase("")){
				for (String str : temp_snaps) {
					if (!TextUtils.isEmpty(str)) {
						list.add(str);
						System.out.println("images+++++++++" + str);
					}
				}
				
			}
			else
			{ 
			
			for (String str : snaps) {
				if (!TextUtils.isEmpty(str)) {
					list.add(str);
					System.out.println("images+++++++++" + str);
				}
			}
			}
			Intent i = new Intent(RfiQuestionire.this, ViewImagesActivity.class);
			i.putExtra("data", list.toArray(new String[] {}));
			System.out.println("String Activity+++++++++");
			startActivity(i);
		}
}	
	}
	
	
	private File getNextFileName()

	{
		File root = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "RFI" + File.separator+"temp"); 
		root.mkdirs();
		String filename = System.currentTimeMillis() + ".jpg";
		
		/*String str1="m_"; 
		String desc = str1.concat(filename);*/
		//System.out.println("file in getnextfile==============================>"+desc);
		File file = new File(root, filename);
		System.out.println("FILENAME+++" + file.getAbsolutePath());
		return file;
	}
	 
	
	@Override 
	public void onBackPressed() {}
	 
	
	
	private void logout() {
		//System.out.println(">>>>>> proj Id ="+db.selectedProjectId+">>>>> bldg id =="+db.selectedBuildingId);
		finish();
		Toast.makeText(RfiQuestionire.this, "Session expired... Please login.", Toast.LENGTH_SHORT).show();
		Intent logout = new Intent(RfiQuestionire.this, LoginScreen.class);
		logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(logout);
	}

	
	
	
	 
	}
