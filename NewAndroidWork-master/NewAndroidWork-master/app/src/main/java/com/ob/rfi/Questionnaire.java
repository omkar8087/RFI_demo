package com.ob.rfi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ob.rfi.db.RfiDatabase;

@SuppressWarnings({ "static-access" })
public class Questionnaire extends CustomTitle {

	private RfiDatabase db;
	private Cursor questionCursor;
	RadioButton[] radioButton;
	private String answer;
	private Vibrator vibrator;
	File sdImageMainDirectory;
	String[] snaps = new String[] { "", "" };
	private int imageCount = 0;
	private Button imageButton;
	private EditText remark;
	private String qType;
	private int completed_observation;
	private String qustionid = "";
	// boolean isupdate;
	boolean dialogResult;  
	String tempFilename;
	private static final String LOG_TAG = "QUESTIONAIRE";
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.question);
		vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		
		RfiDatabase.obs_taken=false;
		
		db = new RfiDatabase(getApplicationContext());
		// isupdate = isDataAvialable(db.selectedQuestionId);
		initComponent();
		if (db.userId.equalsIgnoreCase("")) {
			logout();
		} else {
			getQuestionData();
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		save.requestFocus();
	}

	@Override
	protected void onResume() {
		super.onResume();
		//RfiDatabase.obs_taken=false;
		if (db.userId.equalsIgnoreCase("")) {
			//logout();
		}
	}

	private void logout() {
		finish();
		Toast.makeText(Questionnaire.this, "Session expired... Please login.",
				Toast.LENGTH_SHORT).show();
		Intent logout = new Intent(Questionnaire.this, LoginScreen.class);
		logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(logout);
	}

	/**
	 * Start camera Activity and for Capturing Image.
	 */
	public void startCameraActivity() {

		/*
		 * tempFilename = System.currentTimeMillis()+".jpg"; //you can give any
		 * name to your image file String mPathImage =
		 * Environment.getExternalStorageDirectory()+ "/" + tempFilename; File
		 * file = new File(mPathImage);
		 */

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(tempFilename)));
		startActivityForResult(intent, 1);
	}   

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case 0:
			break;
		case -1:
			Bitmap bm = null;
			/*ProcessBitmap pb=new ProcessBitmap();
			pb.execute();*/
		 try {
			 System.out.println("imagecount===========================>"+imageCount);
				imageCount++;
				imageButton.setText("Capture Image(" + imageCount + "/2)");
				Bundle b = data.getExtras();
				bm = (Bitmap) b.get("data");

				
				FontMetrics fm = new FontMetrics();
				Paint mpaint = new Paint();
		        mpaint.setColor(Color.WHITE);
		        mpaint.setStyle(Style.FILL);
				
				
				
				float aspect = (float)bm.getWidth() / bm.getHeight();
				int width = 720;
				int height = (int) (width/aspect);

				// int height = (int) (width / aspect);
				bm = Bitmap.createScaledBitmap(bm, width, height, true);

				// bm = BitmapFactory.decodeFile(tempFilename, null);
				FileOutputStream out = new FileOutputStream(
						sdImageMainDirectory);
				System.out.println("save file==============================>");
				// Write text on bitmap 
				Canvas canvas = new Canvas(bm);

				Paint paint = new Paint();
				paint.setTextSize(22);
				paint.setColor(Color.RED); // Text Color
				paint.setStrokeWidth(24); // Text Size
				paint.setXfermode(new PorterDuffXfermode(
						PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
				// some more settings...

				/*canvas.drawBitmap(bm, 0, 0, paint);
				canvas.drawText(timestamp, 10, 20, paint);*/
				
				canvas.drawRect(5, 5,200, 60, mpaint);
				
				System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
				canvas.drawBitmap(bm, 0, 0, paint);
				canvas.drawText(timestamp, 9, 40, paint);

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

	private class ProcessBitmap extends AsyncTask<String, String, String> {

		private ProgressDialog progress_;
		static final int MEGHPIXEL = 3145728;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress_ = new ProgressDialog(Questionnaire.this);
			progress_.setIndeterminate(true);
			progress_.setTitle("Please Wait");
			progress_.setMessage(" Processing Image...");
			progress_.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress_.setCancelable(false);
			progress_.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			File source = new File(tempFilename);
	
			if (source != null && source.exists()) {
				paintText();
				source.delete();
			}

			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (progress_.getWindow() != null) {
				progress_.dismiss();
			}

			// in case we had an error...
			if (!result.contains("Ok")) {
				Toast.makeText(Questionnaire.this,
						"There was an error: " + result, Toast.LENGTH_SHORT)
						.show();
				return;
			}

		}

		private int calculateInSampleSize(BitmapFactory.Options bmOptions) {
			int inSampleSize = 1;

			BitmapFactory.decodeFile(tempFilename, bmOptions);
			inSampleSize = Math
					.round((float) (bmOptions.outHeight * bmOptions.outWidth)
							/ (float) MEGHPIXEL);
			return inSampleSize;
		}

		Bitmap decodeSampledBitmapFromFile() {

			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(tempFilename, options);
		 
		}

		private void paintText() {
			try {
				Bitmap bm = decodeSampledBitmapFromFile();
				FileOutputStream out;

				out = new FileOutputStream(sdImageMainDirectory);

				// Write text on bitmap
				Canvas canvas = new Canvas(bm);

				Paint paint = new Paint();
				paint.setTextSize(20);
				paint.setColor(Color.RED); // Text Color
				paint.setStrokeWidth(15); // Text Size
				paint.setXfermode(new PorterDuffXfermode(
						PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
				// some more settings...

				canvas.drawBitmap(bm, 0, 0, paint);
				canvas.drawText(timestamp, 10, 20, paint);

				bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
				bm.recycle();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void hideKeyboard() {
		LinearLayout mainlayout = (LinearLayout) findViewById(R.id.questionLayout);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mainlayout.getWindowToken(), 0);
	}

	/** 
	 * Save answer
	 */
	protected void saveAnswer() {
		//setalertdata();
		//inspection_log();
		db.setSpinner=true;
		String ansDate = "";
		String remarktext = "";
		db.inspectionflag=true;
		db.isanswered=true;
		DateFormat df = new DateFormat();
		ansDate = (String) df.format("yyyy-MM-dd", new Date());
		remarktext = remark.getText().toString();
	
		// if (isupdate) {
		// db.update("answare", "completed_observations='"
		// + copletedobservation + "', answer='" + answer
		// + "',remark='" + remarktext + "',image1='" + snaps[0] +
		// "',image2='" + snaps[1] + "',ans_date='" + ansDate + "', floor_id='"
		// +
		// db.selectedFloor + "'", "question_id='" + db.selectedQuestionId +
		// "'");
		// } else {
		String data = db.selectedQuestionId + ",'" + db.selectedSchemeId
				+ "','" + db.selectedBuildingId + "','" + db.selectedFloorId
				+ "','" + db.selectedChecklistId + "','"
				+ completed_observation + "','" + answer + "','" + remarktext
				+ "','" + snaps[0] + "','" + snaps[1] + "','" + ansDate + "','"
				+ db.userId + "','" + db.selectedSubGroupId + "','"
				+ db.selectedGroupId + "','" + db.selectedTradeId + "'";
		// System.out.println(data);
		db.insert( 
				"answare",
				"question_id,proj_id,bldg_id,floor_id,checklist_id,completed_observations,answer,"
						+ " remark, image1,image2,ans_date,user_id, sub_group_id,group_id,trade_id",
				data);

		// System.out.println(" save answer === "+ db.selectedQuestionId);
		/*db.update("question", "completed_observations='"
				+ completed_observation + "'", "proj_id=" + db.selectedSchemeId
				+ " AND bldg_id=" + db.selectedBuildingId + " AND floor_id="
				+ db.selectedFloorId + " AND checklist_id="
				+ db.selectedChecklistId + " AND PK_question_id='"
				+ db.selectedQuestionId + "'");*/
		 
		db.update("question", "completed_observations='"
				+ completed_observation + "'", "proj_id=" + db.selectedSchemeId  
				+ " AND bldg_id=" + db.selectedBuildingId + " AND floor_id="
				+ db.selectedFloorId + " AND checklist_id="
				+ db.selectedChecklistId + " AND PK_question_id='"
				+ db.selectedQuestionId + "'");
		// }
		System.out.println("selected bdg id at update time"+db.selectedBuildingId);
		db.selectedDirections = "";

		finish();
		Intent i = new Intent(Questionnaire.this, SelectionScreen.class);
		i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);

	}

	
	
	/*public void setalertdata()
	{
		db.setSpinner=true;
		DateFormat df = new DateFormat();
		String alertDate = (String) df.format("yyyy-MM-dd", new Date());
		
		 
		 
		String alert_s="S";
		String alert_f="F";  
		
		System.out.println("alertsssss"+db.checkalert_s+db.checkalert_f);
		
		if (db.selectedSupervisorId.equals("0") && !db.checkalert_s) {
			String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_s+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
			System.out.println("s inserted**************************8********");
			
			
		}	
		
		if (db.selectedForemanId.equals("0") && !db.checkalert_f) {
			String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_f+"','"+db.userId+ "'";
				db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
				System.out.println("f inserted****************************************************8");
				
		}
		
		  
	}*/
	
	
	/*public void inspection_log()
	{
		System.out.println("inspection");
		DateFormat df = new DateFormat();
		String InspectionDate = (String) df.format("yyyy-MM-dd", new Date());
		String i_data="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+db.selectedContractorId+"','"+InspectionDate+"','"+db.userId+ "'";
		db.insert("inspection_log","scheme_Id,structure_id,trade_id,contractor_id,Inspection_date,user_id",i_data);
		
	}
	*/
	
	
	
	
	
	
	
	
	/**
	 * @param message
	 *            prompt user to answere is not seletcted
	 */
	protected void answerNotSelected(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setNegativeButton(" Ok ",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create();
		builder.show();
	}

	/**
	 * collect data from question table
	 */
	private void getQuestionData() {
		questionCursor = null;
		imageCount = 0;
		String where;
		// if (isupdate) {
		// where = " a.question_id=q.PK_question_id AND q.PK_question_id='"
		// + db.selectedQuestionId + "' AND a.floor_id='"
		// + db.selectedFloor + "'";
		// } else

		where = " q.PK_question_id='" + db.selectedQuestionId + "'";
		questionCursor = db
				.select("question q  left outer join  answare a",
						"q.q_type_id, q.q_severity_id, q.q_id,q_text, q.opt1, q.opt2, q.opt3, q.opt4, q.opt5, q.opt6, "
								+ "a.answer, a.remark,a.image1, a.image2, q.completed_observations",
						where, null, null, null, null);
		Log.d("Count>>>>>>>>>>>>>>>>>", "" + questionCursor.getCount());

		if (questionCursor.moveToNext()) {
			qType = questionCursor.getString(questionCursor
					.getColumnIndex("q_type_id"));
			TextView questionText = (TextView) findViewById(R.id.questiontext);
			questionText.setText("-> "
					+ questionCursor.getString(questionCursor
							.getColumnIndex("q_text")));
			// if (isupdate) {
			// copletedobservation =
			// Integer.parseInt(questionCursor.getString(questionCursor
			// .getColumnIndex("completed_observations")));
			// } else {

			completed_observation = Integer.parseInt(questionCursor
					.getString(questionCursor
							.getColumnIndex("completed_observations"))) + 1;

			questionText.startAnimation(anim);
			optionsLayout();
		}
	}

	EditText input;
	private AlphaAnimation anim;
	private LayoutInflater inflater;
	private EditText nValue;
	private EditText mValue;
	private Button save;
	private String timestamp;

	/**
	 * initialize all component
	 */
	private void initComponent() {

		Calendar c1 = Calendar.getInstance();
		timestamp = c1.get(Calendar.DAY_OF_MONTH) + "/"
				+ (c1.get(Calendar.MONTH)+1) + "/" + c1.get(Calendar.YEAR) + " "
				+ c1.get(Calendar.HOUR_OF_DAY) + ":" + c1.get(Calendar.MINUTE)
				/*+ ":" + c1.get(Calendar.SECOND)*/;

		Button cancel = (Button) findViewById(R.id.backward);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		}); 

		imageButton = (Button) findViewById(R.id.capture);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (imageCount < 2) {
					try {
						
						File root = new File(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ "CQRA");
						root.mkdirs(); 
						Date d = new Date();
						String imgName = db.userId + "-" + db.selectedSchemeId
								+ "-" + db.selectedBuildingId + "-"
								+ db.selectedFloorId + "-"
								+ db.selectedChecklistId + "-"
								+ completed_observation + "-" + qustionid + "_"
								+ imageCount + "_" + d.getTime() + ".jpg";

						snaps[imageCount] = imgName;
						sdImageMainDirectory = new File(root, imgName);						 
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

		save = (Button) findViewById(R.id.forwards);
		save.setOnClickListener(new OnClickListener() {
			
			
			
			
			@Override
			public void onClick(View arg0) {
				answer = "";
				String msg = "";
				RfiDatabase.obs_taken=true;
				
				if (qType.equals("1") || qType.equals("2")) {
					for (int i = 0; i < radioButton.length; i++) {
						if (radioButton[i].isChecked()) {
							answer = String.valueOf(radioButton[i].getTag());
							if (i == 0 && qType.equals("1")) {
								ncSelcted();
								return;
							}
							if (qType.equals("2")) {
								answer += "_" + radioButton[i].getId();
								if (i != 0) {
									ncSelcted();
									return;
								}
							}
						} else {
							msg = "Please select atleast one  answer.";
						}
					}

				} else if (qType.equals("3")) {
					String nval = nValue.getText().toString().trim();
					String mval = mValue.getText().toString().trim();
					if (!nval.equals("") && !mval.equals("")) {
						if (validateNbyM(mval, nval)) {
							answer = "";
							msg = "N-value should be less than or equal to M-value.";
						} else {
							answer = nval + "#" + mval;
						}
					} else {
						msg = nval.equals("") ? "Please enter N-value."
								: "Please enter M-value.";
					}

				} else if (qType.equals("4")) {
					String nval = nValue.getText().toString().trim();
					if (!nval.equals("")) {
						answer = nval + "";
					} else {
						msg = "Please enter N-value";
					}

				} else if (qType.equals("5") || qType.equals("6")) {
					String nval = nValue.getText().toString().trim();
					String mval = mValue.getText().toString().trim();
					if (!nval.equals("")) {
						answer = nval + "#" + mval;
					} else {
						msg = "Please enter N-value.";
					}
				}

				String rmrk = remark.getText().toString();
				if (rmrk.contains("'") || rmrk.contains("\"")
						|| rmrk.contains("~") || rmrk.contains("|")) {
					answer = "";
					msg = "Remark should not contain ' \" ~ | ";
				}

				if (answer.equals(""))
					answerNotSelected(msg);
				else {
					saveAnswer();
				}
			}
		});

		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		anim = new AlphaAnimation(0.1F, 1.F);
		anim.setDuration(500);
		anim.setRepeatMode(Animation.RESTART);
		anim.setRepeatCount(0);
	}
 
	/**
	 * Set Option check box dynamic layout depends on Question type.
	 * 
	 */
	private void optionsLayout() { 

		ScrollView parentLayout = (ScrollView) findViewById(R.id.ParentLayout);
		View view = null;
		// String answerStr = null == questionCursor.getString(questionCursor
		// .getColumnIndex("answer")) ? "" : questionCursor
		// .getString(questionCursor.getColumnIndex("answer"));

		System.out.println(">>>>>>>>>>>>>>>>>>>>> QTYPE===" + qType + "==");
		/*
		 * Single choice layout for Yes/No type questions
		 */ 
		if (qType.trim().contains("1")) {
			parentLayout.removeAllViews();

			view = inflater.inflate(R.layout.single_choice_layout, null);
			RadioGroup radioGroup = (RadioGroup) view
					.findViewById(R.id.radioGroup);
			radioButton = new RadioButton[2];

			radioButton[0] = new RadioButton(getApplicationContext());
			radioButton[0].setTextColor(Color.WHITE);
			radioButton[0].setChecked(false);
			radioButton[0].setTag("Yes");
			radioButton[0].setText("Non-Conformity");

			radioButton[1] = new RadioButton(getApplicationContext());
			radioButton[1].setTextColor(Color.WHITE);
			radioButton[1].setChecked(false);
			radioButton[1].setTag("No");
			radioButton[1].setText("Conformity");

			radioGroup.addView(radioButton[0]);
			radioGroup.addView(radioButton[1]);

			// if (answerStr.equals(questionCursor.getString (
			// questionCursor.getColumnIndex("opt1"))))
			// radioButton[0].setChecked(true);
			// else if (answerStr.equals(questionCursor.getString (
			// questionCursor.getColumnIndex("opt2"))))
			// radioButton[1].setChecked(true);

			remark = (EditText) view.findViewById(R.id.remark);
			parentLayout.addView(view);
			radioButton[0].requestFocus();
		}

		/*
		 * Single choice layout for MCQ type questions
		 */
		else if (qType.trim().contains("2")) {

			String optionsWithNull = "";
			String optionsWithoutNull = "";
			for (int i = 1; i < 7; i++) {
				String tempString = questionCursor.getString(questionCursor
						.getColumnIndex("opt" + i));
				if (!tempString.equalsIgnoreCase("NONE")) {
					optionsWithoutNull += tempString + ",";
				}
				optionsWithNull += tempString + ",";
			}

			view = inflater.inflate(R.layout.single_choice_layout, null);
			RadioGroup radioGroup = (RadioGroup) view
					.findViewById(R.id.radioGroup);

			// String[] option = optionsWithoutNull.split(",");
			String[] allOptions = optionsWithNull.split(",");
			int len = allOptions.length;
			radioButton = new RadioButton[len];
			for (int i = 0; i < len; i++) {
				// if (!questionCursor.getString(i).equalsIgnoreCase("NONE")) {
				radioButton[i] = new RadioButton(getApplicationContext());
				radioButton[i].setText(allOptions[i]);
				radioButton[i].setTextColor(Color.WHITE);
				radioButton[i].setId(i);
				radioButton[i].setTag(allOptions[i]);
				// System.out.println(" option === "+allOptions[i]);
				if (!allOptions[i].equalsIgnoreCase("NONE")) {
					radioGroup.addView(radioButton[i]);
					// System.out.println(" OPtion added .......");
				}

				/*
				 * if question is MCQ you have pass Option Text as well selected
				 * index of option following if block does that
				 */
				// if (answerStr.split("_").length == 2
				// && option[i].equalsIgnoreCase(answerStr.split("_")[0])) {
				// radioButton[i].setChecked(true);
				// } else {
				// radioButton[i].setChecked(false);
				// }
				// }
			}

			remark = (EditText) view.findViewById(R.id.remark);
			parentLayout.addView(view);
			radioButton[0].requestFocus();
		}

		/*
		 * Layout for n/m type questions
		 */
		else if (qType.trim().contains("3")) {
			view = inflater.inflate(R.layout.nbym_layout, null);
			nValue = (EditText) view.findViewById(R.id.nBymValue1);
			mValue = (EditText) view.findViewById(R.id.nBymValue2);
			remark = (EditText) view.findViewById(R.id.remark);
			// if (!answerStr.equals("") && answerStr.contains("#")) {
			// String[] ans = answerStr.split("#");
			// nValue.setText(ans[0]);
			// mValue.setText(ans[1]);
			// }
			parentLayout.addView(view);
		} 

		/*
		 * Layout for n type questions
		 */
		else if (qType.trim().contains("4")) {
			view = inflater.inflate(R.layout.nbym_layout, null);
			nValue = (EditText) view.findViewById(R.id.nBymValue1);
			mValue = (EditText) view.findViewById(R.id.nBymValue2);
			remark = (EditText) view.findViewById(R.id.remark);
			mValue.setVisibility(View.GONE);
			// nValue.setText(answerStr);
			parentLayout.addView(view);
		} else if (qType.trim().contains("5") || qType.trim().contains("6")) {
			view = inflater.inflate(R.layout.nbym_layout, null);
			nValue = (EditText) view.findViewById(R.id.nBymValue1);
			mValue = (EditText) view.findViewById(R.id.nBymValue2);
			mValue.setText(questionCursor.getString(questionCursor
					.getColumnIndex("opt1")));
			mValue.setEnabled(false);
			mValue.setClickable(false);
			mValue.setFocusable(false);
			remark = (EditText) view.findViewById(R.id.remark);
			parentLayout.addView(view);
		}

		/*
		 * String remarkTxt = null == questionCursor.getString(questionCursor //
		 * .getColumnIndex("remark")) ? "" : questionCursor //
		 * .getString(questionCursor.getColumnIndex("remark")); //
		 * remark.setText(remarkTxt);
		 * 
		 * snaps[0] = null ==
		 * questionCursor.getString(questionCursor.getColumnIndex("image1")) ?
		 * "" :
		 * questionCursor.getString(questionCursor.getColumnIndex("image1"));
		 * snaps[1] = null ==
		 * questionCursor.getString(questionCursor.getColumnIndex("image2")) ?
		 * "" :
		 * questionCursor.getString(questionCursor.getColumnIndex("image2"));
		 * qustionid =
		 * questionCursor.getString(questionCursor.getColumnIndex("q_id"));
		 * System.out.println(" snaps 1" + snaps[0] + "  snaps2 " + snaps[1]);
		 */

		snaps[0] = "";
		snaps[1] = "";

		TextView surveyDetail = (TextView) findViewById(R.id.roomTypeText);
		surveyDetail.setText("Selected Group : " + db.selectedGroupName);
		int length = surveyDetail.getText().toString().length();
		if (length > 42) {
			Animation anim = new TranslateAnimation(5, -((length - 42) * 7), 0,
					0);
			anim.setDuration(6000);
			anim.setRepeatMode(Animation.RESTART);
			anim.setRepeatCount(Animation.INFINITE);
			surveyDetail.setAnimation(anim);
			int screenWidth = length * 7 + 5;
			surveyDetail.setLayoutParams(new LinearLayout.LayoutParams(
					screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT));
		}

		surveyDetail.requestFocus();
		imageCount = 0;
		int snapslength = snaps.length;
		for (int i = 0; i < snapslength; i++) {
			if (!snaps[i].equals(null) && !snaps[i].equals(""))
				imageCount++;
		}
		imageButton.setText("Capture Image(" + imageCount + "/2)");
		hideKeyboard();
	}

	/**
	 * @param n
	 * @param m
	 * @return Validate n and m value m should greater then n.
	 */
	private boolean validateNbyM(String n, String m) {
		int nval = 1;
		int mval = 2;
		try {
			nval = Integer.parseInt(n);
			mval = Integer.parseInt(m);
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		if (mval > nval) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		
		
		
		System.out.println("backpressed");
		finish();
		Intent i = new Intent(Questionnaire.this, SelectionScreen.class);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.questionnaire_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		try {
			switch (item.getItemId()) {
			case R.id.viewImage:
				showImage();
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

		if (imageCount < 1) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					Questionnaire.this, R.style.MyAlertDialogStyle);
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
			Intent i = new Intent(Questionnaire.this, ViewImagesActivity.class);
			i.putExtra("data", list.toArray(new String[] {}));
			System.out.println("String Activity+++++++++");
			startActivity(i);
		}
	}

	/**
	 * @param title
	 * @param message
	 *            utility method for display alert dialog
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

	/**
	 * @param qid
	 * @return Check whether qid is answered or not for selected floor
	 */
	/*
	 * private boolean isDataAvialable(String qid) { String where = "floor_id='"
	 * + db.selectedFloor + "' AND question_id=" + qid + " AND user_id='" +
	 * db.userId + "'"; Cursor cursor = db.select("answare",
	 * "count(*) as noitem", where, null, null, null, null); if
	 * (cursor.moveToFirst()) { if (Integer.parseInt(cursor.getString(0)) > 0) {
	 * cursor.close(); return true; } else { if (cursor != null &&
	 * !cursor.isClosed()) { cursor.close(); } return false; } } else { return
	 * false; } }
	 */

	/**
	 * @param
	 * @return Check answer is NC type or not apply only YES-NO and MCQ type
	 *         Question.
	 */
	private void ncSelcted() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle("Warning");
		alertDialog
				.setMessage("You are marking this question as NC Do you want to continue?");
		alertDialog.setCancelable(false);
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						dialog = null;
						
						saveAnswer();
					}
				});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	@Override
	protected void onDestroy() {
		if (questionCursor != null && !questionCursor.isClosed()) {
			questionCursor.close();
		}
		if (db != null) { 
			db.closeDb();
		}
		super.onDestroy();
	}
	
	 
	private File getNextFileName()

	{
		File root = new File(Environment.getExternalStorageDirectory()
		+ File.separator + "CQRA" + File.separator + "temp");
		root.mkdirs();
		String filename =System.currentTimeMillis()+".jpg";
		File file = new File(root, filename);
		System.out.println("FILENAME+++" + file.getAbsolutePath());
		return file;
	}
	
}