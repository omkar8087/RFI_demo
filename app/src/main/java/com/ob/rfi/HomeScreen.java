package com.ob.rfi;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@SuppressWarnings("static-access")
public class HomeScreen extends CustomTitle {

    public static final String TAG = "HomeScreen";
    int requestid = 0;
    String[] responseSplit;
    String[] imagesName;
    String[] imageString;
    byte[] configLabel;
    private RfiDatabase db;
    private String method;
    private String[] param;
    private String[] value;
    private String rollval;
    private String rollvaldashboard;
    private SharedPreferences checkPreferences;
    private Editor editor;
    private String roll;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home);
        // title.setText("CQRA Home");

        String contentMsg = getIntent().getStringExtra("key");
        if (!TextUtils.isEmpty(contentMsg)) {
            displayDialog("Upload Image", contentMsg);
        }

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(UploadImages.NFID);


        db = new RfiDatabase(getApplicationContext());

        checkuserRole();


        Button allocateTask = (Button) findViewById(R.id.allocate_task);//allocate
        Button startInspectButton = (Button) findViewById(R.id.start_ins);//create
        Button updaterfi = (Button) findViewById(R.id.updaterfi);//update
        Button allocateUnitButton = (Button) findViewById(R.id.check_rfi);//check rfi
        Button testRfi = (Button) findViewById(R.id.test_rfi);//dashboard
        Button approve_rfi = (Button) findViewById(R.id.approve_rfi);//approve
        ImageView notificationImageview = (ImageView) findViewById(R.id.notification_imageview);//notification icon on home page

        LinearLayout approverfiLinLayout = (LinearLayout) findViewById(R.id.approve_rfi_lin_id);

        LinearLayout allocateTaskLinLayout = (LinearLayout) findViewById(R.id.allocate_task_lin_id);
        LinearLayout createRfiLinLayout = (LinearLayout) findViewById(R.id.create_rfi_lin_id);
        LinearLayout updaterfiLinLayout = (LinearLayout) findViewById(R.id.update_rfi_lin_id);
        LinearLayout checkRfiLinLayout = (LinearLayout) findViewById(R.id.check_rfi_lin_id);
        LinearLayout dashboardLinLayout = (LinearLayout) findViewById(R.id.dashboard_lin_id);
        LinearLayout logOutLinLayout = (LinearLayout) findViewById(R.id.log_out_lin_id);

        notificationImageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notificationIntent = new Intent(HomeScreen.this, DisplayNotificationActivity.class);
                startActivity(notificationIntent);
            }
        });

        System.out.println("checking value of User Roll and user ID=" + rollval + " " + db.userId);
        try {
            if (rollval.equalsIgnoreCase("maker")) {
                allocateUnitButton.setEnabled(false);
                if (isRFIavailableForUser(db.userId)) {
                    updaterfi.setEnabled(true);
                } else {
                    updaterfi.setEnabled(true);
                }
                checkRfiLinLayout.setVisibility(View.GONE);
                approverfiLinLayout.setVisibility(View.GONE);
                approve_rfi.setEnabled(false);
            } else if (rollval.equalsIgnoreCase("Approver")) {
                allocateUnitButton.setEnabled(false);
                updaterfi.setEnabled(false);
                allocateTaskLinLayout.setVisibility(View.GONE);
                updaterfiLinLayout.setVisibility(View.GONE);
                checkRfiLinLayout.setVisibility(View.GONE);
                createRfiLinLayout.setVisibility(View.GONE);
                approverfiLinLayout.setVisibility(View.VISIBLE);
                approve_rfi.setEnabled(true);
            } else {
                System.out.println("checking value2=" + rollval);
                startInspectButton.setEnabled(false);
                allocateTask.setEnabled(false);
                updaterfi.setEnabled(false);
                allocateTaskLinLayout.setVisibility(View.GONE);
                createRfiLinLayout.setVisibility(View.GONE);
                updaterfiLinLayout.setVisibility(View.GONE);

                approverfiLinLayout.setVisibility(View.GONE);
                approve_rfi.setEnabled(false);

            }
            //clearAllTableDataExceptUser();

            if (rollvaldashboard.equalsIgnoreCase("true")) {    //testRfi.setVisibility(View.VISIBLE);
                testRfi.setEnabled(true);
                testRfi.setShadowLayer(1.5f, -1, 1, Color.GRAY);

            } else {

                testRfi.setEnabled(false);

                //testRfi.setVisibility(View.GONE);
            }
            //flushData();

        } catch (Exception e) {
            System.out.println("erroe=" + e.getMessage());
        }

        allocateTask.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (network_available) {

                    Intent i = new Intent(HomeScreen.this, AllocateTask.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);


                    // i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else {
                    displayDialog("Error", "No network connection.");
                }
            }
        });


        //onclicklistener for create button
        startInspectButton.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {

                if (isDataAvialable("AllocateTask"))
                {
                    finish();
                    Intent i = new Intent(HomeScreen.this, SelectQuestion.class);
                    // i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(i);

                }
                else
                {
                    finish();


                    Intent i = new Intent(HomeScreen.this, SelectQuestion.class);
                    // i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(i);
                    displayDialog("Error", "Please allocate the task first.");
                }
				
				
				
				/* if((network_available==false) && isDataAvialable("Client")){
					Intent i = new Intent(HomeScreen.this, SelectQuestion.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					
					
				}else if(network_available==true && (!isDataAvialable("Client"))){
					Intent i = new Intent(HomeScreen.this, SelectQuestion.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					
				}else if(network_available==false && (!isDataAvialable("Client"))){
					displayDialog("Error", "No network connection.");
				}
				else{
					Intent i = new Intent(HomeScreen.this, SelectQuestion.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					
				}
				*/


            }
        });


        /************** start updaterfi button**********************/

        updaterfi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*if (isDataAvialable("question")) {*/
				/*if(network_available){
					finish();
					Intent i = new Intent(HomeScreen.this, UpdateRfi.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
				}else{
					displayDialog("Error", "No network connection.");
				}*/
				/*} else {
					displayDialog("Error", "Please allocate the task first.");
				}*/


                if ((network_available == false) && isDataAvialable1("Rfi_update_details")) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, UpdateRfi.class);
                    // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    startActivity(i);


                } else if (network_available == true && (!isDataAvialable1("Rfi_update_details"))) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, UpdateRfi.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);


                } else if (network_available == false && (!isDataAvialable1("Rfi_update_details"))) {
                    displayDialog("Error", "No network connection.");
                } else {
                    finish();
                    Intent i = new Intent(HomeScreen.this, UpdateRfi.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }


            }
        });

        approve_rfi.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				/*if(network_available){

				finish();
				Intent i = new Intent(HomeScreen.this, CheckFRI.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
				}else{
					displayDialog("Error", "No network connection.");
				}*/


                if ((network_available == false) && isDataAvialable1("Check_update_details")) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, ApproveRFI.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);


                } else if (network_available == true && (!isDataAvialable1("Check_update_details"))) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, ApproveRFI.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);


                } else if (network_available == false && (!isDataAvialable1("Check_update_details"))) {
                    displayDialog("Error", "No network connection.");
                } else {
                    finish();
                    Intent i = new Intent(HomeScreen.this, ApproveRFI.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);

                }


            }
        });


        allocateUnitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				/*if(network_available){
				
				finish();
				Intent i = new Intent(HomeScreen.this, CheckFRI.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
				}else{
					displayDialog("Error", "No network connection.");
				}*/


                if ((network_available == false) && isDataAvialable1("Check_update_details")) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, CheckFRI.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);


                } else if (network_available == true && (!isDataAvialable1("Check_update_details"))) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, CheckFRI.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);


                } else if (network_available == false && (!isDataAvialable1("Check_update_details"))) {
                    displayDialog("Error", "No network connection.");
                } else {
                    finish();
                    Intent i = new Intent(HomeScreen.this, CheckFRI.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);

                }


            }
        });

        testRfi.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (network_available) {
                    finish();
                    Intent i = new Intent(HomeScreen.this, DashBoardSelection.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                } else {
                    displayDialog("Error", "No network connection.");
                }

            }
        });


        Button exitButton = (Button) findViewById(R.id.log_out);
        exitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                db.copyDatabase(HomeScreen.this, "RFI.db");
                //System.out.println("saved------------------");
                onBackPressed();
            }
        });
        clearData();
        getOverflowMenu();


        if (db.justLogged) {
            checkIfDataAvailable();
            db.justLogged = false;
        }

        if (db.isUploaded) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(db.imgUploaded)
                    .setTitle("Image Upload")
                    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what should happen when negative button is clicked
                            Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
                        }
                    }).show();
        }

    }


    private void checkIfDataAvailable() {
        if ((isRFIDetailsAvilable()) || (ischeckRFIDetailsAvilable()) || iscancelRFIDataAvailable()) {
//            displayDialog("Data Available", "Do you want to clear Data");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
            alertDialog.setTitle("Data Available");
            alertDialog.setMessage("Do you want to Upload Previous Data or Refresh All Data?");
            alertDialog.setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builduploadData();
                }
            }).setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    flushData();
                    finish();

                    Intent ai = new Intent(HomeScreen.this,
                            LoginScreen.class);
                    ai.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(ai);
                }
            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.create();
            alertDialog.show();
        }

    }


    public void checkuserRole() {
        //System.out.println("user role =="+userRole+"new userrole="+b[7]);


        String where = "Pk_User_ID='" + db.userId + "'";

        Cursor cursor = db.select("userMaster",
                " user_role,dashboardroll", where, null,
                null, null, null);

        System.out.println("where ==" + where);
        if (cursor.moveToFirst()) {
            do {

                rollval = cursor.getString(0);
                rollvaldashboard = cursor.getString(1);
                System.out.println("rollllllll=" + rollval);
                //items[cursor.getPosition() + 1] = cursor.getString(1);
                checkPreferences = getSharedPreferences("RFI_File", MODE_PRIVATE);
                editor = checkPreferences.edit();
                editor.putString("roll", rollval);
                editor.commit();

            } while (cursor.moveToNext());
        } else {
            //items[0] = "Building(s) not available";
            System.out.println("not available role..");
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }


    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            @SuppressLint("SoonBlockedPrivateApi") Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void clearData() {
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

    protected void callService() {
        Webservice service;
        if (method.equalsIgnoreCase("updateRFI")) {
            service = new Webservice(HomeScreen.this, network_available,
                    "Uploading.. Please wait..", method, param, value);
            service.setParcentage(true);
        } else {
            service = new Webservice(HomeScreen.this, network_available,
                    "Loading.. Please wait..", method, param, value);
        }

        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                if (data.equalsIgnoreCase("success")) {
                    doNext();
                    //saveData(data);

                }
            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");

                System.out.println("faileddddddddddddddd");
            }

            @Override
            public void netNotAvailable() {

                System.out.println("connection not available");
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }


    public void saveData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("error in save data");
            displayDialog("No Record Found", "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                String column = "PK_scheme_ID, Scheme_Name, user_id";//project
                saveToDatabase("Scheme", column, tabledata[0], true, 3);

                column = "FK_Scheme_ID, Bldg_ID, Bldg_Name,No_Floor,user_id";//Structure or building
                saveToDatabase("Building", column, tabledata[1], true, 5);

                column = "FK_Scheme_Id,Fk_Building_Id,floor_Id, floor_Name, user_id";//floor or stage
                saveToDatabase("floor", column, tabledata[2], true, 5);

				/*column = "FK_Scheme_Id,Fk_Building_Id,Fk_Trade_Id, PK_checklist_Id, Checklist_Name,Fk_Floor_Id, user_id"; //checklist
				saveToDatabase("Checklist", column, tabledata[3],true,5);*/

                column = "PK_Trade_ID, Trade_Name,PK_Scheme_ID,FK_Building_Id,FK_floor_ID,user_id";//trade
                saveToDatabase("Trade", column, tabledata[3], true, 6);

                column = "checklist_id, subgroup_id, subgroup_name, user_id"; //subgroup
                saveToDatabase("subgroup", column, tabledata[4], true, 4);

                column = "q_group_id, q_group_name, user_id";
                saveToDatabase("question_group", column, tabledata[5], true, 3);//question group

                System.out.println("Qoestion heading data ==== >>>>> " + tabledata[6].toString());
                if (!tabledata[7].equalsIgnoreCase("NONE")) {
                    column = "q_heading_id,q_heading_text, user_id";
                    saveToDatabase("question_heading", column, tabledata[6], true, 3);
                }

                if (!tabledata[8].equalsIgnoreCase("NS")) {
                    column = "scheme_Id, structure_id, contractor_id,supervisor_id,trade_id,supervisor_name,contractor_name,user_id";
                    saveToDatabase("scheme_superviser", column, tabledata[8], true, 7);

                    column = "scheme_Id, structure_id,contractor_id,s_f_id,trade_id,s_f_name,contractor_name,user_id";
                    saveToDatabase("contractor_sf", column, tabledata[8], true, 7);
                    System.out.println("tablellllllllllllllllllllllll");
                }

                if (!tabledata[9].equalsIgnoreCase("NF")) {
                    column = "scheme_Id, structure_id, contractor_id,foreman_id,trade_id,foreman_name,contractor_name,user_id";
                    saveToDatabase("scheme_foreman", column, tabledata[9], true, 7);

                    column = "scheme_Id, structure_id,contractor_id,s_f_id,trade_id,s_f_name,contractor_name,user_id";
                    saveToDatabase("contractor_sf", column, tabledata[9], true, 7);
                    System.out.println("table lenth==========" + column.length()
                    );
                }

                //client data
                column = "scheme_Id,structure_id,trade_id,client_staf_id,client_name,user_id";
                saveToDatabase("client_staff", column, tabledata[10], true, 5);

                System.out.println("client data==========" + column.length());

				/*column = "q_type_id,q_type_text,q_type_desc, user_id";
				saveToDatabase("question_type", column, tabledata[7],true,4);

			 	column = "severity_id,mild,moderate,severe,very_severe,exstream, user_id";
				saveToDatabase("severity", column, tabledata[8],true,7);*/

                String tempdata = "0~Other";
                column = "q_heading_id,q_heading_text, user_id";
                saveToDatabase("question_heading", column, tempdata, true, 3);

                //setSchemeSpinnerData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found", "Sufficient Data is not available.");
            }
        }
    }


    public void saveToDatabase(String table, String columns, String respose, boolean adduserId, int colCnt) {
        Cursor cursor = db.select(table, columns, "user_id='" + db.userId + "'", null, null, null, null);
        String existingData = "";
        if (cursor.moveToFirst()) {
            for (int i = 0; i < colCnt; i++) {
                existingData += "'" + cursor.getString(i) + "',";
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        String[] rowdata = respose.split("\\|");
        for (int len = 0; len < rowdata.length; len++) {
            String[] singlerow = rowdata[len].split("~");
            StringBuffer values = new StringBuffer("'");
            for (int i = 0; i < (singlerow.length - 1); i++) {
                values.append(singlerow[i] + "','");
            }
            values.append(singlerow[singlerow.length - 1] + "'");
            if (adduserId)
                values.append(",'" + db.userId + "'");

            if (!existingData.contains(values.toString()))
                db.insert(table, columns, values.toString());
        }

    }


    protected void doNext() {
        clearData();

        UpdateRfiEntries();
        UpdateRfiCheckEntries();
        moveimages();
        movecheckimages();
        //	db.delete("userMaster", "Pk_User_ID='" + db.userId + "'");
        db.delete("question", "user_id='" + db.userId + "'");
        db.delete("Client", "user_id='" + db.userId + "'");
        db.delete("Scheme", "user_id='" + db.userId + "'");
        db.delete("Building", "user_id='" + db.userId + "'");
        db.delete("floor", "user_id='" + db.userId + "'");
        db.delete("Unit", "user_id='" + db.userId + "'");

        db.delete("SubUnit", "user_id='" + db.userId + "'");
        db.delete("CheckList", "user_id='" + db.userId + "'");
        db.delete("Group1", "user_id='" + db.userId + "'");
        db.delete("Answer", "user_id='" + db.userId + "'");
        db.delete("Element", "user_id='" + db.userId + "'");
        db.delete("SubElement", "user_id='" + db.userId + "'");
        db.delete("Rfi_New_Create", "user_id='" + db.userId + "'");
        db.delete("Check_update_details", "user_id1='" + db.userId + "'");
        db.delete("CheckAnswer", "user_id='" + db.userId + "'");
        db.delete("ApproverDetails", "user_id1='" + db.userId + "'");
        db.update("sqlite_sequence", "seq=0", "name = 'question'");
        displayDialog("Success", "Data uploaded.");
    }

    private void builduploadData() {


        StringBuilder uploadRFIString = new StringBuilder();
        String uploadanswerRFIData = "";

        StringBuilder uploadcheckRFIString = new StringBuilder();
        String uploadanswercheckRFIData = "";

        StringBuilder uploadCancelRFIString = new StringBuilder();
        String uploadCancelRFIData = "";

        StringBuilder uploadApproverRFIString = new StringBuilder();
        String uploadanswerApproverRFIData = "";

        if ((isRFIDetailsAvilable()) || (ischeckRFIDetailsAvilable() && RfiDatabase.isToBeUploadedCheck)
                || iscancelRFIDataAvailable() || isApproverDetailsAvailable()) {

            if (isRFIDetailsAvilable()) {

                String where = "user_id='" + db.userId + "'";


                String culumn1 = "Rfi_id,CL_Id,PRJ_Id,FK_NODE_Id" +
                        ",Level_int,Fk_CHKL_Id,Fk_Grp_ID, " +
                        "user_id,Structure_Id,Stage_Id,Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,dated,Coverage_str,FK_question_id,ans_text,remark,snap1,snap2,snap3,snap4," +
                        "checker_remark,drawing_no,check_date";


                Cursor cursor = db.select("Answer", culumn1, where, null, null, null, null);

                if (cursor.moveToFirst()) {
                    do {
                        if (db.fromCreate) {
                            uploadRFIString.append("create new" + cursor.getString(0) + "~");
                        } else if (RfiDatabase.isToBeUploaded) {
                            uploadRFIString.append(cursor.getString(0) + "~");
                        } else {
                            continue;
                        }
                        String temp = cursor.getString(15).replaceAll("\\n", "");
                        uploadRFIString.append(cursor.getString(1) + "~");
                        uploadRFIString.append(cursor.getString(2) + "~");
                        uploadRFIString.append(cursor.getString(3) + "~");
                        uploadRFIString.append(cursor.getString(4) + "~");
                        uploadRFIString.append(cursor.getString(5) + "~");
                        uploadRFIString.append(cursor.getString(6) + "~");
                        uploadRFIString.append(cursor.getString(7) + "~");
                        uploadRFIString.append(cursor.getString(8) + "~");
                        uploadRFIString.append(cursor.getString(9) + "~");
                        uploadRFIString.append(cursor.getString(10) + "~");
                        uploadRFIString.append(cursor.getString(11) + "~");
                        uploadRFIString.append(cursor.getString(12) + "~");
                        uploadRFIString.append(cursor.getString(13) + "~");
                        uploadRFIString.append(cursor.getString(14) + "~");
                        uploadRFIString.append(temp + "~");
                        uploadRFIString.append(cursor.getString(16) + "~");
                        uploadRFIString.append(cursor.getString(17) + "~");
                        uploadRFIString.append(cursor.getString(18) + "~");
                        uploadRFIString.append(cursor.getString(19) + "~");
                        uploadRFIString.append(cursor.getString(20) + "~");
                        uploadRFIString.append(cursor.getString(21) + "~");
                        uploadRFIString.append(cursor.getString(22) + "~");
                        uploadRFIString.append(cursor.getString(23) + "~");
                        uploadRFIString.append(cursor.getString(24) + "~");
                        /*uploadRFIString.append(cursor.getString(23) + "~");*/
                        uploadRFIString.append(cursor.getString(25));
                        uploadRFIString.append("$");


                    } while (cursor.moveToNext());

                    if (db.fromCreate) {
                        db.fromCreate = false;
                    }

                } else {
                    System.out.println("id data not available error");
                    displayDialog("Error", "Problem in connection.");
                }
                cursor.close();

            }


            if (ischeckRFIDetailsAvilable()) {

                String where = "user_id='" + db.userId + "'";
                String culumn = "ans_text,remark,snap1,snap2" +
                        ",Rfi_id,dated,FK_question_id, " +
                        "FK_QUE_SequenceNo,FK_QUE_Type,FK_NODE_Id,Fk_CHKL_Id,Fk_Grp_ID,user_id";

                String culumn2 = "Rfi_id,user_id,dated,FK_question_id,ans_text,snap1,snap2,snap3,snap4,remark,check_date,isAnswered";

                String culumn1 = "Rfi_id,CL_Id,PRJ_Id,FK_NODE_Id" +
                        ",Level_int,Fk_CHKL_Id,Fk_Grp_ID, " +
                        "user_id,Structure_Id,Stage_Id,Unit_id,Sub_Unit_Id,Element_Id,SubElement_Id,dated,Coverage_str,FK_question_id,ans_text,remark,snap1,snap2,snap3,snap4,workType_id";

                Cursor cursor1 = db.select("CheckAnswer", culumn2, where, null, null, null, null);

                if (cursor1.moveToFirst()) {
                    do {

                        if (cursor1.getString(cursor1.getColumnIndex("isAnswered")).equalsIgnoreCase("1")) {
                            uploadcheckRFIString.append(cursor1.getString(0) + "~");
                            uploadcheckRFIString.append(cursor1.getString(1) + "~");
                            uploadcheckRFIString.append(cursor1.getString(2) + "~");
                            uploadcheckRFIString.append(cursor1.getString(3) + "~");
                            uploadcheckRFIString.append(cursor1.getString(4) + "~");
                            uploadcheckRFIString.append(cursor1.getString(5) + "~");
                            uploadcheckRFIString.append(cursor1.getString(6) + "~");
                            uploadcheckRFIString.append(cursor1.getString(7) + "~");
                            uploadcheckRFIString.append(cursor1.getString(8) + "~");
                            uploadcheckRFIString.append(cursor1.getString(9) + "~");
                            uploadcheckRFIString.append(cursor1.getString(10));
                            uploadcheckRFIString.append("$");
                        }

                    } while (cursor1.moveToNext());
                } else {
                    System.out.println("error upload check");
                    //displayDialog("Error", "Problem in connection.");
                }
                cursor1.close();
            }
//-----------------Approver
            if (isApproverDetailsAvailable()) {

                String where = "user_id1='" + db.userId + "' AND Answer IS NOT NULL AND Answer !=''";
                Cursor cursor1 = db
                        .select("ApproverDetails",
                                "RFI_Id,CL_Id,PRJ_Id,NODE_Id,Level_int,CHKL_Id,GRP_Id," +
                                        "USER_Id,StructureId,StageId,UnitId,SubUnitId,ElementId,SubElementId," +
                                        "Coverage,Rfi_name,Fk_WorkTyp_ID,drawing_no,Answer,Remark",
                                where, null, "RFI_Id", null, null);

                if (cursor1.moveToFirst()) {
                    do {

                     //   if (cursor1.getString(cursor1.getColumnIndex("isAnswered")).equalsIgnoreCase("1")) {
                        uploadApproverRFIString.append(cursor1.getString(0) + "~");
                        uploadApproverRFIString.append(cursor1.getString(1) + "~");
                        uploadApproverRFIString.append(cursor1.getString(2) + "~");
                        uploadApproverRFIString.append(cursor1.getString(3) + "~");
                        uploadApproverRFIString.append(cursor1.getString(4) + "~");
                        uploadApproverRFIString.append(cursor1.getString(5) + "~");
                        uploadApproverRFIString.append(cursor1.getString(6) + "~");
                        uploadApproverRFIString.append(cursor1.getString(7) + "~");
                        uploadApproverRFIString.append(cursor1.getString(8) + "~");
                        uploadApproverRFIString.append(cursor1.getString(9) + "~");
                        uploadApproverRFIString.append(cursor1.getString(10) + "~");
                        uploadApproverRFIString.append(cursor1.getString(11) + "~");
                        uploadApproverRFIString.append(cursor1.getString(12) + "~");
                        uploadApproverRFIString.append(cursor1.getString(13) + "~");
                        uploadApproverRFIString.append(cursor1.getString(14) + "~");
                        uploadApproverRFIString.append(cursor1.getString(15) + "~");
                        uploadApproverRFIString.append(cursor1.getString(16) + "~");
                        uploadApproverRFIString.append(cursor1.getString(17) + "~");
                        uploadApproverRFIString.append(cursor1.getString(18) + "~");
                        uploadApproverRFIString.append(cursor1.getString(19) + "~");
                        uploadApproverRFIString.append(db.userId);
                        uploadApproverRFIString.append("$");
                       // }

                    } while (cursor1.moveToNext());
                } else {
                    System.out.println("error upload approver");
                    //displayDialog("Error", "Problem in connection.");
                }
                cursor1.close();
            }



            //----------------------------------

            if (iscancelRFIDataAvailable()) {
                String where = "User_ID = '" + db.userId + "'";
                String columns = "RFI_ID,Remark";

                Cursor cursor = db.select("CancelRFI", columns, where, null, null, null, null);

                if (cursor.moveToFirst()) {
                    do {

                        uploadCancelRFIString.append(cursor.getString(0) + "~");
                        uploadCancelRFIString.append(cursor.getString(1) + "~");
                        uploadCancelRFIString.append(db.userId);
                        uploadCancelRFIString.append("$");

                    } while (cursor.moveToNext());
                } else {
                    System.out.println("No Canceled RFIs");
                }
                cursor.close();
            }


            if (uploadRFIString.length() > 1)
                uploadanswerRFIData = uploadRFIString.deleteCharAt(uploadRFIString.length() - 1).toString();

            if (uploadcheckRFIString.length() > 1)
                uploadanswercheckRFIData = uploadcheckRFIString.deleteCharAt(uploadcheckRFIString.length() - 1).toString();

            if (uploadCancelRFIString.length() > 1)
                uploadCancelRFIData = uploadCancelRFIString.deleteCharAt(uploadCancelRFIString.length() - 1).toString();


            if (uploadApproverRFIString.length() > 1)
                uploadanswerApproverRFIData = uploadApproverRFIString.deleteCharAt(uploadApproverRFIString.length() - 1).toString();


            uploadData(uploadanswerRFIData + "|" + uploadanswercheckRFIData + "|" + uploadCancelRFIData +"|"+uploadanswerApproverRFIData);
            System.out.println("RFI Data Upload=" + uploadanswerRFIData + "/n" + uploadanswercheckRFIData + "/n" + uploadCancelRFIData
                    +"\n Approver Data \n"+uploadanswerApproverRFIData);


        } else {
            displayDialog("Error", "No data to upload.");
        }
    }

    private void uploadData(String data) {
        Log.d("DATA", data);
        //method = "uploadAnswers";
        System.out.println("Upload Data===" + data);
        method = "updateRFI";
        param = new String[]{"rfiDetails"};
        value = new String[]{data};
        callService();
    }


    //updating rfi as complete when uploading answer
    public void UpdateRfiEntries() {
        String where = "a.Rfi_id=u.RFI_Id AND a.user_id='" + db.userId + "'";
        ArrayList<String> rfi_id = new ArrayList<String>();
        Cursor cursor = db.select("Answer as a,Rfi_update_details as u", "u.RFI_Id", where, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                rfi_id.add(cursor.getString(0));


            } while (cursor.moveToNext());

            for (String v : rfi_id) {
                if (v.length() > 2) {
                    Log.d(getClass().getName(), v);

                    db.update("Rfi_update_details", "status_device='" + "complete'", "user_id1='" + db.userId + "' AND RFI_Id='" + v + "'");

                    System.out.println("updating RFI_update_details....." + v);

                }
            }
        } else {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


    }

    //updating check  rfi as complete when uploading answer
    public void UpdateRfiCheckEntries() {
        String where = "a.Rfi_id=u.RFI_Id AND a.user_id='" + db.userId + "'";
        ArrayList<String> rfi_id = new ArrayList<String>();
        Cursor cursor = db.select("CheckAnswer as a,Check_update_details as u", "u.RFI_Id", where, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                rfi_id.add(cursor.getString(0));


            } while (cursor.moveToNext());

            for (String v : rfi_id) {
                if (v.length() > 2) {
                    Log.d(getClass().getName(), v);

                    db.update("Check_update_details", "status_device='" + "complete'", "user_id1='" + db.userId + "' AND RFI_Id='" + v + "'");

                    System.out.println("updating RFI_check_details....." + v);

                }
            }
        } else {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


    }


    private boolean isImageAvilableForUpload() {
        String where = "status <>'" + RfiDatabase.UPLOADED + "'";
        Cursor cursor = db.select("Images", "count(*) as noitem", where,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
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


    private boolean isDataAvilable() {
        String where = " a.question_id=q.PK_question_id AND a.user_id='" + db.userId + "'";
        Cursor cursor = db.select("question q ,answare a", "count(*) as noitem", where,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
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


    private boolean isCardDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("card_details", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean iscancelRFIDataAvailable() {
        String where = "RFI_ID='" + db.selectedrfiId + "'";
        Cursor cursor = db.select("CancelRFI", "count(*) as noitem", where,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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

    private boolean isRFIDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("Answer", "count(*) as noitem", where,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean ischeckRFIDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("CheckAnswer", "count(*) as noitem", where,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean isApproverDetailsAvailable() {
        String where = "user_id1='" + db.userId + "'";
        Cursor cursor = db.select("ApproverDetails", "count(*) as noitem", where,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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

    private boolean isCardFormanDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("card_details_forman", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean isCardClientDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("card_details_client", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean isAlertDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("alert_details", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean isMockupDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("mockuptable", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean isInspectionDetailsAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("inspection_log", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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


    private boolean isDprAvilable() {
        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("dpr_table", "count(*) as noitem", null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {

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
                            retry();
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

    protected void retry() {
        callService();
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog alertDialog;
        switch (item.getItemId()) {
            case R.id.upload:
                alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
                alertDialog.setCancelable(false);
                alertDialog.setMessage("Do you want to upload the data?");
                alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("QuestionCount: " + db.questionCount);
                        System.out.println("Submit count: " + db.questionSubmitCount);
                        /*if (!db.questionSubmitCount.equalsIgnoreCase(db.questionCount)) {
                            displayDialog("Error","All Questions answer mandatory for upload data");
                        }
                        else {
                            builduploadData();
                        }*/
                        builduploadData();
                    }
                });
                alertDialog.setButton2("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            case R.id.update:
                updateData();
                return true;

            case R.id.upload_images:
                if (isImageAvilableForUpload()) {
                    startImageUploadServic();
                } else {
                    Toast.makeText(this, "There are no images to upload.", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startImageUploadServic() {
        if (network_available) {
            Toast.makeText(this, "Images upload service started", Toast.LENGTH_LONG).show();
            System.out.println("before activity start");
            startService(new Intent(this, UploadImages.class));
            System.out.println("after activity start");

        } else {
            displayDialog("Error", "Network not Available");
        }
    }

    @SuppressWarnings("deprecation")
    private void updateData() {
        AlertDialog alertDialog;
        checkPreferences = getSharedPreferences("RFI_File", MODE_PRIVATE);
        roll = checkPreferences.getString("roll", "");
        try {
            System.out.println("user rolllllllllllll=========" + roll);
            if (roll.equalsIgnoreCase("maker")) {
                Cursor cursor = db.select("Answer", "count(*) as noitem",
                        "ans_text <> ''", null, null, null, null);
                if (cursor.moveToFirst()) {
                    if (Integer.parseInt(cursor.getString(0)) >= 1) {
                        cursor.close();

                        alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Please upload data before refreshing.");
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Clear Data",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        clearAllTableDataExceptUser();
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.show();

                    } else {
                        alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Do you want to Clear All the Data?");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        flushData();
                                        finish();

                                        Intent i = new Intent(HomeScreen.this,
                                                LoginScreen.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);

                                    }
                                });
                        alertDialog.setButton2("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

            } else {
                Cursor cursor = db.select("CheckAnswer", "count(*) as noitem",
                        "ans_text <> ''", null, null, null, null);
                if (cursor.moveToFirst()) {
                    if (Integer.parseInt(cursor.getString(0)) >= 1) {
                        cursor.close();

                        alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Please upload data before refreshing.");
                        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });

                        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Clear Data",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        clearAllTableDataExceptUser();
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();

                    } else {
                        alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
                        alertDialog.setCancelable(false);
                        alertDialog.setMessage("Do you want to refresh the data?");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        flushData();
                                        finish();

                                        Intent i = new Intent(HomeScreen.this,
                                                LoginScreen.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);

                                    }
                                });
                        alertDialog.setButton2("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }


            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
			
			
			
		/*	
			
			Cursor cursor = db.select("Answer", "count(*) as noitem",
					"ans_text <> ''", null, null, null, null);
			if (cursor.moveToFirst()) {
				if (Integer.parseInt(cursor.getString(0)) >= 1) {
					cursor.close();
  
					alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setCancelable(false);
					alertDialog.setMessage("Please upload data before updating.");
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					}); 

					alertDialog.show();

				} else {
					alertDialog = new AlertDialog.Builder(this).create();
					alertDialog.setCancelable(false);
					alertDialog.setMessage("Do you want to update the data?");
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							flushData(); 
							finish(); 

							Intent i = new Intent(HomeScreen.this,
									LoginScreen.class);
							i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(i);

						}
					});
					alertDialog.setButton2("Cancel",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss(); 
						} 
					});
					alertDialog.show();
				}
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);


        int positionOfMenuItem = 2; // or whatever...
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString("Refresh/Clear");
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);

        return true;
    }

    private void flushData() {
        db.delete("userMaster", "Pk_User_ID='" + db.userId + "'");
        db.delete("question", "user_id='" + db.userId + "'");
        db.delete("Client", "user_id='" + db.userId + "'");
        db.delete("Scheme", "user_id='" + db.userId + "'");
        db.delete("Building", "user_id='" + db.userId + "'");
        db.delete("floor", "user_id='" + db.userId + "'");
        db.delete("Unit", "user_id='" + db.userId + "'");
        db.delete("SubUnit", "user_id='" + db.userId + "'");
        db.delete("CheckList", "user_id='" + db.userId + "'");
        db.delete("Group1", "user_id='" + db.userId + "'");
        db.delete("Element", "user_id='" + db.userId + "'");
        db.delete("SubElement", "user_id='" + db.userId + "'");
        db.delete("Images", "user_id='" + db.userId + "'");
        db.delete("Rfi_update_details", "user_id1='" + db.userId + "'");
        db.delete("Check_update_details", "user_id1='" + db.userId + "'");
        db.delete("Rfi_New_Create", "user_id='" + db.userId + "'");


        db.delete("CheckListWise", "user_id='" + db.userId + "'");
        db.delete("MakerWise", "user_id='" + db.userId + "'");
        db.delete("CheckerWise", "user_id='" + db.userId + "'");
        db.delete("ApproverWise", "user_id='" + db.userId + "'");
        db.delete("ContractorWise", "user_id='" + db.userId + "'");

        //db.delete("", "user_id='" + db.userId + "'");
        db.delete("Answer", "user_id='" + db.userId + "'");
        db.delete("AllocateTask", "UserID='" + db.userId + "'");

        System.out.println("data flush+++++++++++++++++++++++++++++++++++++++++++++++++");
        deleteImages();
    }

    public void moveimages() {
        ArrayList<String> imageName = new ArrayList<String>();
        Cursor cursor = db.select("Answer", "snap1,snap2", "user_id='"
                + db.userId + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                imageName.add(cursor.getString(0));
                imageName.add(cursor.getString(1));

            } while (cursor.moveToNext());

            for (String v : imageName) {
                if (v.length() > 2) {
                    Log.d(getClass().getName(), v);
                    System.out.println("moving ansswer images..");
                    db.insert("Images", "fileName,user_id", "'" + v + "','"
                            + db.userId + "'");
                }
            }
        } else {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    public void movecheckimages() {
        ArrayList<String> imageName = new ArrayList<String>();
        Cursor cursor = db.select("CheckAnswer", "snap3,snap4", "user_id='"
                + db.userId + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                imageName.add(cursor.getString(0));
                imageName.add(cursor.getString(1));

            } while (cursor.moveToNext());

            for (String v : imageName) {
                if (v.length() > 2) {
                    Log.d(getClass().getName(), v);
                    System.out.println("moving check images..");
                    db.insert("Images", "fileName,user_id", "'" + v + "','"
                            + db.userId + "'");

                    System.out.println("moving===================================>" + v);
                }
            }
        } else {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    private void deleteImages() {
        Cursor cursor = db.select("Images", "fileName", "status ='"
                + RfiDatabase.UPLOADED + "'", null, null, null, null);
        File f;
        if (cursor.moveToFirst()) {
            do {
                Log.d("DELET", cursor.getString(0));
                f = getFile(cursor.getString(0));
                if (f != null) {
                    Log.d("DELET", f.getAbsolutePath());
                    f.delete();
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        //db.delete("Images", "status ='" + CqraDatabase.UPLOADED + "'");
    }

    private File getFile(String filename)
    {
        String dir = Environment.getExternalStorageDirectory() + File.separator
                + "CQRA";
        //		Log.d(getClass().getName(), dir);
        File file = new File(dir + "/" + filename.toString().trim());
        if (file.exists())
        {
            return file;
        }
        else
        {
            return null;
        }
    }

    private boolean isDataAvialable(String table) {
        Cursor cursor = db.select(table, "count(*) as noitem", "UserID='"
                + db.userId + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) >= 1) {
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

    private boolean isDataAvialable1(String table) {
        Cursor cursor = db.select(table, "count(*) as noitem", "user_id='"
                + db.userId + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) >= 1) {
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


    public boolean isRFIavailableForUser(String user_id) {
        String columns = "FK_rfi_Id,user_id";
        String where = "user_id = '" + db.userId + "'";
        Cursor cursor = db.select("Rfi_New_Create", columns, where, null, null, null, null);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }


    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                checkPreferences = getSharedPreferences("RFI_File", MODE_PRIVATE);
                editor = checkPreferences.edit();
                editor.putString("", "");
                editor.putString("drawingcoverage", "");
                editor.commit();


                finish();
                Intent i = new Intent(HomeScreen.this, LoginScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        db.closeDb();
        super.onDestroy();


    }

    protected void clearAllTableDataExceptUser() {
        clearData();

        UpdateRfiEntries();
        UpdateRfiCheckEntries();
        moveimages();
        movecheckimages();
        //	db.delete("userMaster", "Pk_User_ID='" + db.userId + "'");
        db.delete("question", "user_id='" + db.userId + "'");
        db.delete("Client", "user_id='" + db.userId + "'");
        db.delete("Scheme", "user_id='" + db.userId + "'");
        db.delete("Building", "user_id='" + db.userId + "'");
        db.delete("floor", "user_id='" + db.userId + "'");
        db.delete("Unit", "user_id='" + db.userId + "'");

        db.delete("SubUnit", "user_id='" + db.userId + "'");
        db.delete("CheckList", "user_id='" + db.userId + "'");
        db.delete("Group1", "user_id='" + db.userId + "'");
        db.delete("Answer", "user_id='" + db.userId + "'");
        db.delete("Element", "user_id='" + db.userId + "'");
        db.delete("SubElement", "user_id='" + db.userId + "'");
        db.delete("Rfi_New_Create", "user_id='" + db.userId + "'");
        db.delete("Check_update_details", "user_id1='" + db.userId + "'");
        db.delete("CheckAnswer", "user_id='" + db.userId + "'");
        db.update("sqlite_sequence", "seq=0", "name = 'question'");
        //displayDialog("Success", "Data Cleared.");
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
}
