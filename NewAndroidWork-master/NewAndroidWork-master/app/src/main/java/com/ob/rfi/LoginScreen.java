package com.ob.rfi;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ob.rfi.R;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

@SuppressWarnings("static-access")
public class LoginScreen extends CustomTitle {
	private RfiDatabase db;
	private String response;
	public EditText usernameEditText;
	public EditText passwordEditText;
	public String usernameText;
	public String passwordText;
	String userRole;
	private SharedPreferences pref;
	String errormessage = "";
	String[] responseSplit;
	int requestid = 0;
	private String android_ID = "";
	String method;
	String[] param;
	String[] value;
	private String dashboardroll;
	private String deviceDate = "";
	private String serverDate = "";

	private Context mContext;
	private Activity mActivity;
	private static final int MY_PERMISSIONS_REQUEST_CODE = 123;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loginscreen);


		mContext = getApplicationContext();
		mActivity = LoginScreen.this;
		checkPermission();
		//  set custom title
		//	title.setText("CQRA Login");
		usernameEditText = (EditText) findViewById(R.id.usernameTextInput);
		passwordEditText = (EditText) findViewById(R.id.passwordTextInput);

		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
									   Spanned dest, int dstart, int dend) {
				if (dstart > 14) {
					return "";
				} else {
					for (int i = start; i < end; i++) {
						if (Character.isSpaceChar(source.charAt(i))) {
							return "";
						}
					}
				}
				return null;
			}
		};
		passwordEditText.setFilters(new InputFilter[]{filter});

//		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//			// TODO: Consider calling
//			//    ActivityCompat#requestPermissions
//			// here to request the missing permissions, and then overriding
//			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//			//                                          int[] grantResults)
//			// to handle the case where the user grants the permission. See the documentation
//			// for ActivityCompat#requestPermissions for more details.
//			return;
//		}
//		android_ID = telephonyManager.getDeviceId();
//		// android_ID = "358472047320253";
//		android_ID = "123456789123456";
//		// android_ID = "358049044737968";

		db = new RfiDatabase(this); 
		
		Button loginButton = (Button) findViewById(R.id.LoginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				/*if(network_available){
				*/
				usernameText = usernameEditText.getText().toString().trim();
				passwordText = passwordEditText.getText().toString().trim();
				if (validateUser()) {
					authenticateUser();
					/*Intent i =new Intent( LoginScreen.this, HomeScreen.class);
					System.out.println("homescreen---------");
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);*/
				}
			/*}else{
				displayDialog("Error", "No network connection.");
			}*/
			}
		});

		Button exitButton = (Button) findViewById(R.id.exitButton);
		exitButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				onBackPressed();
				Intent i =new Intent(LoginScreen.this , HomeScreen.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
			}
		}); 
		setDeafaultUser();
	}

	protected boolean validateUser() {
		boolean validate = false;
		if (usernameText.length() == 0) {
			usernameEditText.setError("Please Enter Username");
		} else if (usernameText.length() < 1) {
			usernameEditText.setError("Username required minimum 5 Characters");
		} else if (passwordText.length() == 0) {
			passwordEditText.setError("Please Enter Password");
		} else if (passwordText.length() < 1) {
			passwordEditText.setError("Password required minimum 4 Characters");
		}  else
			validate = true;

		return validate;
	}

	private void authenticateUser() {
		savePref();
		boolean isRecordFound = false;
		if (network_available) {
			method = "verifyLogin";
			//method = "getDetails";
			param = new String[] { "userName", "password"};
			value = new String[] { usernameText, passwordText};
			System.out.println("before call service------------");
			callService();
			System.out.println("after call webservice-------------");
		} else {

			Cursor cursor = db.select("userMaster",	"Pk_User_ID,User_Name,Password", null, null, null, null, null);
			if (cursor.moveToFirst()) {
				do { 
					System.out.println("before check");
					if (usernameText.equals(cursor.getString(cursor.getColumnIndex("User_Name")))
							&& passwordText.equals(cursor.getString(cursor.getColumnIndex("Password")))) {

						db.userId = cursor.getString(cursor.getColumnIndex("Pk_User_ID"));
						cursor.moveToLast();
						isRecordFound = true;

						db.justLogged = true;

						db.closeDb();
						finish();
						System.out.println("after finish--------");
						Intent i =new Intent( LoginScreen.this, HomeScreen.class);
						System.out.println("homescreen---------");
						i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						startActivity(i);

					}
					else
					{
						System.out.println("out.......");
					}
					
				} while (cursor.moveToNext());
			}

			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

			if (!isRecordFound) {
				displayDialog("Error", "No network connection.");
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBackPressed() {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setCancelable(false);
		alertDialog.setMessage("Do you want to exit application?");
		alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
				 android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
		alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.show();
	}

	public void insertUser() {
		db = new RfiDatabase(this);
		System.out.println("response----------------fff"+response);
		if (!response.equalsIgnoreCase("fail")) {

			String[] userDetails = response.split("~");
			db.userId = userDetails[0];
			
			System.out.println("" + userDetails[0] + userDetails[1] + userDetails[2] + userDetails[3] + userDetails[8] );
			
			String[] d=response.split("\\|");
			String[] b=d[0].split("~");
			
			userRole=b[7];
			/***** AKSHAY *****/
			// Change made 24-April-2015
			db.userRole = userRole;
			dashboardroll=b[8];
			
			serverDate = b[9];
			
			System.out.println("Server Date : " + serverDate);
			
			if(!deviceDate.equalsIgnoreCase(serverDate)) {
				displayDialog(2,"Error", "Device Date does not match Server Date ");
			} else {
				System.out.println("user role =="+userRole+"new userrole="+b[7]+"dashboard roll=="+dashboardroll);
				Cursor cursor = db.select("userMaster", "User_Name", null, null,
						null, null, "pk_user_id");

				boolean recordFound = false;
				if (cursor.moveToFirst()) {
					do {
						if (usernameText.equals(cursor.getString(cursor.getColumnIndex("User_Name")))) {
							cursor.moveToLast();
							recordFound = true;

						} else {
							recordFound = false; 
						}
					} while (cursor.moveToNext());
				}

				if (!recordFound) { 
					//db.delete(" ",null);
					db.insert("userMaster", "Pk_User_ID,User_Name,Password,user_role,dashboardroll",
							"'" + db.userId + "','" + usernameText + "','" + passwordText +"','" + userRole+"','"+dashboardroll+ "'");
				} else {
					db.update("userMaster", "Password='" + passwordText + "',Pk_User_ID="+db.userId, "User_Name='" + usernameText + "'");
				}
				cursor.close();

				db.justLogged = true;

				db.closeDb();
				finish();
				Intent i =new Intent(LoginScreen.this , HomeScreen.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
			}

		} else {
			displayDialog(2,"Error", "Invalid Username or Password");
		}
	}  

	public void displayDialog(int id, String title, String message) {

		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		/*		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/

		if (id == 1) {

			/*			View view = inflater.inflate(R.layout.dialog_layout, null);

			Button button1  = (Button)view.findViewById(R.id.customButton1);
			Button button2  = (Button)view.findViewById(R.id.customButton2);

			if(!title.equals("")){
				TextView titleText = (TextView)view.findViewById(R.id.customTitleTextView);
				titleText.setVisibility(View.VISIBLE);
				titleText.setText(title);
			}

			if(!message.equals("")){
				TextView msgText = (TextView)view.findViewById(R.id.customMessageTextView);
				msgText.setVisibility(View.VISIBLE);
				msgText.setText(message);
			}
			if (id == 1) { 
				button1.setVisibility(View.VISIBLE);
				button1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});

				button2.setVisibility(View.VISIBLE);
				button2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {}
				});
			}
			alertDialog.setView(view);*/

			alertDialog.setButton("Try again", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					retry();
				}
			});
			alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					finish();
				}
			});
		} else {
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					usernameEditText.setText("");
					passwordEditText.setText("");
					dialog.dismiss();
				}
			});
		}
		alertDialog.show();
	}

	protected void retry() {
		callService();
	}

	protected void callService() {
		Webservice service = new Webservice(LoginScreen.this, network_available, "Loading.. Please wait..",
				method, param, value);
		service.setdownloadListener(new downloadListener(){
			@Override
			public void dataDownloadedSuccessfully(String data) {
				response = data;
				Date current = new Date();
				deviceDate = new SimpleDateFormat("dd/MM/yyyy").format(current);
				System.out.println("Current Date : " + deviceDate);
				insertUser();
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
		try {
			AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setCancelable(false);

			if(message.equals("Problem in connection.")){
				alertDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				alertDialog.setButton2("Try again", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						retry();
					}
				});
			}else{
				alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}  
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}

public void setDeafaultUser(){
		
		pref=getSharedPreferences("CQRA",Context.MODE_PRIVATE);
		String username1=pref.getString(db.USER_PREF_NAME, "");
		
		usernameEditText.setText(username1);
		
	}
	public void savePref(){
		SharedPreferences.Editor editor = pref.edit();   
        editor.putString(db.USER_PREF_NAME,usernameText);
        editor.commit();
        System.out.println("save pref--------------------");
		
	}
	
	@Override
	protected void onDestroy() {
		if(db!=null){
			db.closeDb();
		} 
		super.onDestroy();
	}




	protected void checkPermission() {
		if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
				+ ContextCompat.checkSelfPermission(
				mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                +ContextCompat.checkSelfPermission(
//                        mActivity,Manifest.permission.CALL_PHONE)
				+ ContextCompat.checkSelfPermission(
				mActivity,Manifest.permission.READ_PHONE_STATE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Do something, when permissions not granted
			if (ActivityCompat.shouldShowRequestPermissionRationale(
					mActivity, Manifest.permission.CAMERA)
					|| ActivityCompat.shouldShowRequestPermissionRationale(
					mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
					|| ActivityCompat.shouldShowRequestPermissionRationale(
					mActivity,Manifest.permission.READ_PHONE_STATE)
//            ||ActivityCompat.shouldShowRequestPermissionRationale(
//                    mActivity,Manifest.permission.CALL_PHONE)
			) {
				// If we should give explanation of requested permissions

				// Show an alert dialog here with request explanation
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity, R.style.MyAlertDialogStyle);
				builder.setMessage("Camera and Write External" +
						" Storage permissions are required to do the task.");
				builder.setTitle("Please grant those permissions");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						ActivityCompat.requestPermissions(
								mActivity,
								new String[]{
										Manifest.permission.CAMERA,
										Manifest.permission.READ_PHONE_STATE,
//                                        Manifest.permission.CALL_PHONE,
										Manifest.permission.WRITE_EXTERNAL_STORAGE
								},
								MY_PERMISSIONS_REQUEST_CODE
						);
					}
				});
				builder.setNeutralButton("Cancel", null);
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				// Directly request for required permissions, without explanation
				ActivityCompat.requestPermissions(
						mActivity,
						new String[]{
								Manifest.permission.CAMERA,
								Manifest.permission.READ_PHONE_STATE,
//                                Manifest.permission.CALL_PHONE,
								Manifest.permission.WRITE_EXTERNAL_STORAGE
						},
						MY_PERMISSIONS_REQUEST_CODE
				);
			}
		} else {
			// Do something, when permissions are already granted
			Toast.makeText(mContext, "Permissions already granted", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_CODE: {
				// When request is cancelled, the results array are empty
				if (
						(grantResults.length > 0) &&
								(grantResults[0]
										+ grantResults[1]
										+ grantResults[2]
//                                        + grantResults[3]
										== PackageManager.PERMISSION_GRANTED
								)
				) {
					// Permissions are granted
					Toast.makeText(mContext, "Permissions granted.", Toast.LENGTH_SHORT).show();
				} else {
					// Permissions are denied
					Toast.makeText(mContext, "Permissions denied.", Toast.LENGTH_SHORT).show();
					super.finish();
				}
				return;
			}
		}
	}

}