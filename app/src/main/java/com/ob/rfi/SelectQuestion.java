package com.ob.rfi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.multispinner.MultiSelectSpinner;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

@SuppressWarnings("static-access")
public class SelectQuestion extends CustomTitle {

    private Spinner schemeSpin;
    private Spinner buildingSpin;
    private Spinner checklistspin;
    private Spinner subgrpSpin;
    private Spinner tradeSpin;
    private Spinner floorSpin;
    private Spinner supervisorspin;
    private Spinner foremanspin;
    private Spinner contractorspin;
    public static CustomTitle slectionclass;
    private String[] schemId;
    private String[] tradeId;
    private String[] chklstId;
    private String[] bldgId;
    private String[] subgrpId;
    private String[] supervisorId;
    private String[] supervisor;
    private String[] foremanId;
    private String[] foreman;
    private String[] contractorId;
    private String[] contractorNewId;
    private String[] contractorId1;
    private String[] Citems4;
    private String[] contractor;
    private String[] contid;
    private Button okBtn;
    private Button backBtn;
    private String errorMessage;
    private RfiDatabase db;
    private String selected = "";
    private Spinner clientspin;
    private String[] client;
    private String[] clientId;
    private Spinner rfiSpin;
    private Spinner projSpin;
    private Spinner clintSpin;
    private Spinner structureSpin;
    private Spinner stageSpin;
    private Spinner unitSpin;
    private Spinner subunitspin;
    private Spinner elementspin;
    private Spinner subelementspin;

    private Spinner grouptspin;
    //CHANGED BY SAYALI
    private Spinner coveragespin;
    private Spinner coverageSpinner;
    private String method;
    private String[] param;
    private String[] value;
    private String[] unitId;
    private String[] subunitId;
    private String[] elementId;
    private String[] subelementId;
    private String[] checklistId;
    private String[] groupId;
    private String[] NodeId;
    private String[] floorid;
    private String[] RfiId;

    private EditText coverageedit;
    private Spinner worktypeSpin;
    private String[] wTypeId;
    private String[] wTypeLevelId;
    private EditText drawingeedit;
    private String[] scroll_status;
    public static int nval;
    public int noRfi = 0;
    public static CustomTitle destruct;
    public static final String TAG = "SelectScreen";
    public static boolean insertFlag = false;
    private String responseData;
    /***** AKSHAY *****/
    ArrayList<Group> groupList;
    private boolean isCoverageSpinner = false;
    private boolean isCoverageTextViewNew = false;

    private MultiSelectSpinner unitSpinMulti;
    private MultiSelectSpinner subUnitSpinMulti;
    private MultiSelectSpinner elementSpinMulti;
    private MultiSelectSpinner subElementSpinMulti;
    private MultiSelectSpinner groupSpinMulti;

    boolean isUnit = false;
    boolean isSubUnit = false;
    boolean isElement = false;
    boolean isSubElement = false;
    boolean isGroup = false;
    private boolean isGroupDataAvailable = false;

    String bit = "";
    private ImageView viewCoverageImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.select_question);
        // title.setText("CQRA Question selection");
        destruct = this;
        slectionclass = this;
        MultiSelectSpinner multiSelectSpinner = new MultiSelectSpinner(getApplicationContext());
        //	rfiSpin = (Spinner) findViewById(R.id.rfi_id);
        clintSpin = (Spinner) findViewById(R.id.rfi_client_id);
        projSpin = (Spinner) findViewById(R.id.rfi_project_id);
        worktypeSpin = (Spinner) findViewById(R.id.rfi_worktype_id);
        structureSpin = (Spinner) findViewById(R.id.rfi_structure_id);
        stageSpin = (Spinner) findViewById(R.id.rfi_statge_id);
        unitSpin = (Spinner) findViewById(R.id.rfi_unit_id);
        subunitspin = (Spinner) findViewById(R.id.rfi_sub_unit_id);
        elementspin = (Spinner) findViewById(R.id.rfi_element_id);
        subelementspin = (Spinner) findViewById(R.id.rfi_sub_element_id);
        checklistspin = (Spinner) findViewById(R.id.rfi_checklist_id);
        grouptspin = (Spinner) findViewById(R.id.rfi_group_id);
        //changed by sayali
        coveragespin = (Spinner) findViewById(R.id.Coverage_spin_id);

        coverageedit = (EditText) findViewById(R.id.coverage_id);

        drawingeedit = (EditText) findViewById(R.id.drawing_id);

        unitSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_unit_id_multi);

        subUnitSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_sub_unit_id_multi);
        elementSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_element_id_multi);
        subElementSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_sub_element_id_multi);
        groupSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_group_id_multi);

        viewCoverageImageView = (ImageView) findViewById(R.id.viewCoverageImageview);




       /* int a=0;
        db.questionSubmitCount= String.valueOf(a);
        System.out.println("Before submit clicked--- "+db.questionSubmitCount);*/

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                char[] chars = {'\'', '"', '@', '$', '&', '?', '!', '*', '<', '>', '~', '^'};
                for (int i = start; i < end; i++) {
                    if (new String(chars).contains(String.valueOf(source.charAt(i)))) {
                        return "";
                    }
                }
                return null;
            }
        };
        coverageedit.setFilters(new InputFilter[]{filter});
        drawingeedit.setFilters(new InputFilter[]{filter});


        ResetId();

        okBtn = (Button) findViewById(R.id.question_select_submit);
        backBtn = (Button) findViewById(R.id.question_select_Back);

        db = new RfiDatabase(getApplicationContext());
        if (db.userId.equalsIgnoreCase("")) {
//            logout();
        } else {
            // setSchemeSpinnerData();
        }

        okBtn.setOnClickListener(new OnClickListener() {
            private SharedPreferences checkPreferences;
            private Editor editor;

            @Override
            public void onClick(View arg0) {

                String where = "user_id='" + db.userId + "'";
                Cursor cursor = db.select("Created_RFI", "FK_rfi_Id", where, null,
                        null, null, null);

                String id = "";
                if (cursor.moveToFirst()) {
                    do {
                        id = cursor.getString(0);
                    } while (cursor.moveToNext());
                } else {
                    id = "0";
                }

                nval = Integer.parseInt(id);
                nval = nval + 1;

                String value = String.valueOf(nval);
                String cov;
                if (isCoverageSpinner) {
                    if (coverageSpinner.getSelectedItem() != null)
                        cov = coverageSpinner.getSelectedItem().toString();
                    else
                        cov = "";
                } else {
                    cov = coverageedit.getText().toString();
                }

                cov = cov.replace("\'", "");

                db.insert("Created_RFI", "FK_rfi_Id,user_id,coverageText", "'" + value + "','" + db.userId + "','" + cov + "'");
                System.out.println("Data Inserted in Rfi_New_Create Table : RFI_ID : " + value);

                db.selectedrfiId = value;

                if (validateScreen()) {
                    displayErrorDialog("Error", errorMessage);

                    // setalertdata();

                    /*
                     * RfiDatabase.gropPosition = 0; RfiDatabase.childPosition =
                     * 0;
                     */
                } else if (!isGroupDataAvailable) {
                    displayErrorDialog("Error", "Please select group");

                } else if (isQuestionAvailable()) {

                    if (db.selectedScrollStatus.equalsIgnoreCase("true")) {
                        Intent int1 = new Intent(SelectQuestion.this,
                                RfiQuestionSelect.class);
                        if (isCoverageSpinner) {
                            int1.putExtra("coverage", coverageSpinner
                                    .getSelectedItem().toString());
                        } else {
                            int1.putExtra("coverage", coverageedit.getText()
                                    .toString());
                        }
                        int1.putExtra("drawingId", drawingeedit.getText()
                                .toString());

                        checkPreferences = getSharedPreferences("RFI_File",
                                MODE_PRIVATE);
                        editor = checkPreferences.edit();
                        if (isCoverageSpinner) {
                            editor.putString("coverage", coverageSpinner
                                    .getSelectedItem().toString());
                            int1.putExtra("coverage", coverageSpinner
                                    .getSelectedItem().toString());
                        } else {
                            editor.putString("coverage", coverageedit.getText()
                                    .toString());
                            int1.putExtra("coverage", coverageedit.getText()
                                    .toString());
                        }
                        editor.putString("drawing", drawingeedit.getText()
                                .toString());
                        editor.commit();

                        startActivity(int1);

                    } else {
                        Intent int1 = new Intent(SelectQuestion.this,
                                RfiQuestionSelect.class);
                        if (isCoverageSpinner) {
                            int1.putExtra("coverage", coverageSpinner
                                    .getSelectedItem().toString());
                        } else {
                            int1.putExtra("coverage", coverageedit.getText()
                                    .toString());
                        }
                        int1.putExtra("drawingId", drawingeedit.getText()
                                .toString());

                        checkPreferences = getSharedPreferences("RFI_File",
                                MODE_PRIVATE);
                        editor = checkPreferences.edit();
                        if (isCoverageSpinner) {
                            editor.putString("coverage", coverageSpinner
                                    .getSelectedItem().toString());
                            int1.putExtra("coverage", coverageSpinner
                                    .getSelectedItem().toString());
                        } else {
                            editor.putString("coverage", coverageedit.getText()
                                    .toString());
                            int1.putExtra("coverage", coverageedit.getText()
                                    .toString());
                        }
                        editor.putString("drawing", drawingeedit.getText()
                                .toString());
                        editor.commit();

                        startActivity(int1);

                        /*
                         * Intent int1=new
                         * Intent(SelectQuestion.this,RfiQuestionire.class);
                         * int1
                         * .putExtra("coverage",coverageedit.getText().toString
                         * ()); startActivity(int1);
                         */
                    }
                    /*
                     * Intent int1=new
                     * Intent(SelectQuestion.this,RfiQuestionire.class);
                     * int1.putExtra
                     * ("coverage",coverageedit.getText().toString());
                     */
                    /*
                     * Intent int1=new
                     * Intent(SelectQuestion.this,RfiQuestionSelect.class);
                     * int1.
                     * putExtra("coverage",coverageedit.getText().toString());
                     * int1
                     * .putExtra("drawingId",drawingeedit.getText().toString());
                     *
                     *
                     * checkPreferences=getSharedPreferences("RFI_File",MODE_PRIVATE
                     * ); editor=checkPreferences.edit();
                     * editor.putString("coverage"
                     * ,coverageedit.getText().toString());
                     * editor.putString("drawing"
                     * ,drawingeedit.getText().toString()); editor.commit();
                     *
                     * startActivity(int1);
                     */
                } else {
                    displayErrorDialog("", "Please Select Another Combination!");
                }
            }

        });

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /***** AKSHAY SHAH *****/
        // CHANGE MADE ON : 12-MAY-2015
        /*
         * if(!db.userId.equalsIgnoreCase("")) { if(isDataAvialable("Client")){
         * setRFIData(); //setClientData(); }else { updateData(); }
         * //disableList(false); } else { logout(); }
         *//***** AKSHAY SHAH *****/

        if (!db.userId.equalsIgnoreCase("")) {

            if (isDataAvialable("Client") && isDataAvialableAllocateTask("AllocateTask")) {

                setClientData();
                clintSpin.setClickable(true);
                clintSpin.setSelection(1);// changed pramod
            }
        }

    }

    private void setRFISpinnerData() {
        // TODO Auto-generated method stub
        String columns = "FK_rfi_Id,user_id";
        String where = "user_id = '" + db.userId + "'";
        Cursor cursor = db.select("Rfi_New_Create", columns, where, null, null, null, null);
        final String[] selectRFIDropDown = new String[cursor.getCount() + 1];
        final String[] selectRFID = new String[cursor.getCount() + 1];
        int i = 0;
        selectRFIDropDown[i] = "-----SELECT-----";
        selectRFID[i] = "0";
        if (cursor.moveToFirst()) {
            do {
                i = i + 1;
                selectRFIDropDown[i] = "RFI " + cursor.getString(cursor.getColumnIndex("FK_rfi_Id"));
                selectRFID[i] = "" + i;
            } while (cursor.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, selectRFIDropDown);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rfiSpin.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            rfiSpin.setSelection(0);
            rfiSpin.setClickable(true);
            rfiSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if (arg2 >= 1) {
                        db.selectedrfiId = selectRFID[arg2];
                        System.out.println("Selected RFI ID = " + db.selectedrfiId);
                        setClientData();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });
        } else {
            System.out.println("-----No Data in Cursor-----");
        }
    }

    //changed by sayali
    //27-02-2024
    public void coverageData() {
        method = "getCoverageForCRFI";
        param = new String[]{"projectId", "workTypeId", "nodeId", "checkListId", "group_Id"};
        value = new String[]{db.selectedSchemeId, db.selectedWorkTypeId, db.selectedNodeId, db.selectedChecklistId,
                db.selectedGroupId};
        callService1();
    }

    public void updateData() {
        // requestid = 1;
        /***** AKSHAY *****/
        /*
         * method = "getDetails"; param = new String[] { "userID","userRole" };
         * value = new String[] { db.userId,"maker" };
         */
        /***** AKSHAY *****/
        // Changes made 24-April-2015
        method = "getClientProjectWorkType";
        param = new String[]{"userID", "userRole"};
        value = new String[]{db.userId, "maker"};
        callService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setSpiner();
        if (db.userId.equalsIgnoreCase("")) {
            //logout();
        }
    }

    private void logout() {
        finish();
        Toast.makeText(SelectQuestion.this, "Session expired... Please login.",
                Toast.LENGTH_SHORT).show();
        Intent logout = new Intent(SelectQuestion.this, LoginScreen.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        db.copyDatabase(getApplicationContext(), "RFI.db");
        startActivity(logout);
    }

    public void setSpiner() {
        if (db.setSpinner) {

            rfiSpin.setSelection(0);
            clintSpin.setSelection(0);
            projSpin.setSelection(0);
            structureSpin.setSelection(0);
            stageSpin.setSelection(0);
            unitSpin.setSelection(0);
            subunitspin.setSelection(0);
            elementspin.setSelection(0);
            subelementspin.setSelection(0);
            checklistspin.setSelection(0);
            grouptspin.setSelection(0);
            db.setSpinner = false;
            System.out.println("setspinner");

        }

    }

    protected void clearData() {
        db.selectedGroupId = "";
        db.selectedGroupName = "";
        db.selectedSchemeId = "";
        db.selectedBuildingId = "";
        db.selectedFloorId = "";
        db.selectedChecklistId = "";
        db.selectedSubGroupId = "";
        db.selectedHeadingId = "";
        db.selectedQuestionId = "";
        db.selectedTradeId = "";
        db.selectedSupervisorId = "";
        db.selectedForemanId = "";
        db.selectedClientId = "";
        db.selectedScrollStatus = "";

    }

    protected boolean validateScreen() {
        boolean validate = true;
        System.out.println("In validate method");
        System.out.println("Value os isCoverageSpinner : " + isCoverageSpinner);
        /*
         * if (schemeSpin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Project"; } else if
         * (buildingSpin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Structure"; } else if
         * (floorSpin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Stage"; } else if (tradeSpin.getSelectedItemPosition()
         * == 0) { errorMessage = "Please Select Trade"; } else if
         * (checklistspin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Check List"; } else if
         * (subgrpSpin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Sub Group"; } else if (db.contr &&
         * contractorspin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Contractor"; }else if (db.contractor_flag &&
         * contractorspin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Allocate Contractor"; } else if
         * (supervisorspin.getSelectedItemPosition() == 0 &&
         * foremanspin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Atleast Supervisor or Foreman"; }else if
         * ((db.supervisor_flag) && foremanspin.getSelectedItemPosition() == 0)
         * { errorMessage = "Please Select Foreman"; }else if
         * (supervisorspin.getSelectedItemPosition() == 0 && db.foreman_flag
         * foremanspin.getSelectedItemPosition() == 0) { errorMessage =
         * "Please Select Supervisor"; }else if
         * (clientspin.getSelectedItemPosition() == 0 ) { errorMessage =
         * "Please Select client"; }
         */
        /***** AKSHAY *****/
        // CHANGE MADE ON : 7-MAY-2015
        if (isCoverageSpinner) {
            if (noRfi == 1) {
                errorMessage = "No Coverage available Please select another combination.";
                return true;
            } else {
                if (coverageSpinner.getSelectedItem() != null) {
                    String coverage_check = coverageSpinner.getSelectedItem().toString();
                    Cursor cursorCheck = db.select("Created_RFI", "coverageText", "coverageText = '" + coverage_check + "'", null, null, null, null);
                    System.out.println("Cursor Count : " + cursorCheck.getCount());
                    db.questionCount = String.valueOf(cursorCheck.getCount());
				/* if(cursorCheck.getCount() > 1) {
					errorMessage = "This Coverage Already Exists";
					return true;
				} else {
					validate = false;
				}  */
                } else {
                    errorMessage = "No Coverage available Please select another combination.";
                    return true;
                }

            }
        } else {
            System.out.println("Coverage Text : "
                    + coverageedit.getText().toString());
            if (coverageedit.getText().toString().equalsIgnoreCase("")) {
                errorMessage = "Please Enter Coverage.";
                return true;
            } else {
                int count = 0;
                String coverage_check = coverageedit.getText().toString();
                Cursor cursorCheck = db.select("Created_RFI", "coverageText", null, null, null, null, null);
                if (cursorCheck.moveToFirst()) {
                    do {
                        if (cursorCheck.getString(0).equalsIgnoreCase(coverage_check)) {
                            count++;
                        }
                    } while (cursorCheck.moveToNext());

                    if (count > 1) {
                        errorMessage = "This Coverage Already Exists";
                        // return true;
                        return false;
                        //This return statement changed by Balaji after discussion with pramod and sandeep, 4 Sept 2021
                    }
                } else {
                    validate = false;
                }
            }
        }
        if (drawingeedit.getText().toString().equalsIgnoreCase("")) {
            errorMessage = "Please Enter Drawing No.";
            return true;
        } else {
            validate = false;
        }

        /*
         * else if ((!db.supervisor_flag) && db.foreman_flag) { errorMessage =
         * "Please Select Supervisor or Allocate Foreman"; }
         */
        /*
         * db.contr= false; db.supervisor_flag= false; db.foreman_flag= false;
         * db.contractor_flag= false;
         */// System.out.println("schem="+db.selectedSchemeId);
        // System.out.println("building="+db.selectedBuildingId);
        // System.out.println("checklist="+db.selectedChecklistId);
        // System.out.println("subroup="+db.selectedGroupId);

        return validate;
    }

    @SuppressWarnings("deprecation")
    public void displayErrorDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    // ------------------------------------------------------------------------------

    private void setRFIData() {

        // Rfi_New_Create(FK_rfi_Id TEXT,user_id TEXT)"

        String where = "user_id='" + db.userId + "'";
        Cursor cursor = db.select("Rfi_New_Create", "FK_rfi_Id", where, null,
                null, null, null);

        String id = "";
        if (cursor.moveToFirst()) {

            do {

                id = cursor.getString(0);
            } while (cursor.moveToNext());
        } else {
            id = "0";
        }

        nval = Integer.parseInt(id);
        nval = nval + 1;

        String value = String.valueOf(nval);
        final String[] items = new String[2];
        items[0] = "--select--";
        items[1] = "create new" + value;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rfiSpin.setAdapter(adapter);
        rfiSpin.setSelection(1);
        rfiSpin.setClickable(false);
        rfiSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    setClientData();
                    db.selectedrfiId = items[1];

                } else {
                    System.out.println("hello");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    // /---client
    private void setClientData() {

        insertFlag = false;
        /***** AKSHAY SHAH *****/
        //CHANGES MADE ON 13-MAY-2015
	/*	String where = "user_id='" + db.userId + "'";

		// Client_ID TEXT,Clnt_Name TEXT,CL_Dispaly_Name TEXT

		System.out.println("cleint where -==========>" + where);
		Cursor cursor = db.select("Client as ct",
				"distinct(ct.Client_ID),ct.Clnt_Name", where, null, null, null,
				null);

		clientId = new String[cursor.getCount()];
		client = new String[cursor.getCount() + 1];

		client[0] = "--select--";

		if (cursor.moveToFirst()) {

			do {
				System.out.println("nnnnnnnnnnnnnnn");
				clientId[cursor.getPosition()] = cursor.getString(0);
				client[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			client[0] = "Client not allocated";

		}

		if (cursor != null && !cursor.isClosed()) {

			cursor.close();
		}		*/


        Cursor cursor = db.select("Client c, AllocateTask a", "distinct(c.Clnt_Name), c.Client_ID", "c.Client_ID = a.Client", null, null, null, null);
        final String[] clientID = new String[cursor.getCount()];
        String[] clientName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                clientName[i] = "--- SELECT ---";
                do {
                    i++;
                    clientName[i] = cursor.getString(0);
                    clientID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            displayDialog("Sorry!", "No Data in Client");
        }
        try {


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, clientName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            clintSpin.setAdapter(adapter);
            clintSpin.setSelection(2);// changed by pramod
            adapter.notifyDataSetChanged();
            clintSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

                public void onItemSelected(AdapterView<?> aview, View view,
                                           int position, long rowid) {
                    System.out.println("position" + position);
                    if (position >= 1) {

                        db.selectedclientname = clintSpin.getSelectedItem()
                                .toString();
                        System.out.println("client selected not present");
                        db.selectedClientId = clientID[position - 1];

                        setSchemeSpinnerData(clientID[position - 1]);
                        worktypeSpin.setSelection(0);
                        worktypeSpin.setClickable(false);
                        projSpin.setSelection(1);
                        projSpin.setClickable(true);

                        // db.selectedClient = client[position];

                    } else {
                        worktypeSpin.setSelection(0);
                        worktypeSpin.setClickable(false);
                        projSpin.setSelection(1);
                        projSpin.setClickable(false);

                        db.selectedClientId = "";

                        try {
                            db.selectedclientname = clintSpin.getSelectedItem().toString();
                        } catch (Exception e) {

                        }

                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        } catch (Exception e) {


        }
    }

    private void setSchemeSpinnerData(String Cl_ID) {

	/*	String where = "s.Scheme_Cl_Id = c.Client_ID AND s.Scheme_Cl_Id='"
				+ db.selectedClientId + "' AND s.user_id='" + db.userId + "'";
		Cursor cursor = db.select("Scheme as s, Client as c",
				"distinct(s.PK_Scheme_ID), s.Scheme_Name,s.scrolling_status",
				where, null, null, null, null);

		schemId = new String[cursor.getCount()];
		scroll_status = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				schemId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);
				scroll_status[cursor.getPosition()] = cursor.getString(2);

			} while (cursor.moveToNext());
		} else {
			items[0] = "Scheme(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		final String where1 = "FK_WorkTyp_ID ='" + "1" + "' AND user_id='"
				+ db.userId + "'"; */
        Cursor cursor = db.select("Scheme s, AllocateTask a", "distinct(s.Scheme_Name), s.PK_Scheme_ID, s.scrolling_status", "s.PK_Scheme_ID = a.Project AND s.Scheme_Cl_Id = '" + Cl_ID + "'", null, null, null, null);
        final String[] schemeID = new String[cursor.getCount()];
        final String[] scrolling_status = new String[cursor.getCount()];
        String[] schemeName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                schemeName[i] = "--- SELECT ---";
                do {
                    i++;
                    schemeName[i] = cursor.getString(0);
                    schemeID[i - 1] = cursor.getString(1);
                    scrolling_status[i - 1] = cursor.getString(2);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            displayDialog("Sorry!", "No Data in Scheme");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, schemeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projSpin.setAdapter(adapter);
        projSpin.setSelection(1);//changed pramod

        projSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSchemeId = schemeID[position - 1];
                    db.selectedScrollStatus = scrolling_status[position - 1];
                    // setBulidngSpinnerData(schemId[position - 1]);
                    setWorkTypeSpinnerData(schemeID[position - 1]);
                    worktypeSpin.setClickable(true);

                    db.selectedSchemeName = projSpin.getSelectedItem()
                            .toString();
                    System.out.println("heloooooooo===="
                            + db.selectedSchemeName);
                    /***** AKSHAY *****/
                    /*
                     * setCheckListSpinnerData();
                     * structureSpin.setClickable(true);
                     * structureSpin.setSelection(0);
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     * worktypeSpin.setSelection(0);
                     * worktypeSpin.setClickable(true);
                     *//***** AKSHAY *****/

                } else {
                    db.selectedSchemeId = "";
                    db.selectedScrollStatus = "";
                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    worktypeSpin.setSelection(0);
                    worktypeSpin.setClickable(false);
                    structureSpin.setClickable(false);
                    structureSpin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setWorkTypeSpinnerData(final String schemId2) {
        /* FK_PRJ_Id */

	/*	String where = "user_id='" + db.userId + "' AND FK_PRJ_Id='"
				+ db.selectedSchemeId + "'";

		Cursor cursor = db.select("WorkType",
				"distinct(WorkTyp_ID),WorkTyp_Name,WorkTyp_level", where, null,
				null, null, null);

		System.out.println("in workype...............");
		wTypeId = new String[cursor.getCount()];
		wTypeLevelId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				wTypeId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);
				wTypeLevelId[cursor.getPosition()] = cursor.getString(2);
			} while (cursor.moveToNext());
		} else {
			items[0] = "WorkType(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}   */
        Cursor cursor = db.select("WorkType w, AllocateTask a", "distinct(w.WorkTyp_Name),w.WorkTyp_ID", "w.WorkTyp_ID = a.WorkType AND w.FK_PRJ_Id = '" + schemId2 + "'", null, null, null, null);
        final String[] worktypeID = new String[cursor.getCount()];
        String[] workTypeName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                workTypeName[i] = "--- SELECT ---";
                do {
                    i++;
                    workTypeName[i] = cursor.getString(0);
                    worktypeID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            displayDialog("Sorry!", "No Data in WorkType");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, workTypeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        worktypeSpin.setAdapter(adapter);

        worktypeSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedWorkTypeId = worktypeID[position - 1];
                    System.out
                            .println("***** Inside workTypeSpin.onItemSelectedListener *****");
                    // db.selectedSchemeName=projSpin.getSelectedItem().toString();
                    System.out.println("db.selectedWorkTypeId : "
                            + db.selectedWorkTypeId);
                    // System.out.println("heloooooooo  worktype level id===="+wTypeLevelId[position
                    // - 1]);

                    /***** AKSHAY *****/
                    // CHANGE MADE ON 24-APRIL-2015
                    // setCheckListSpinnerData();
			/*		db.selectedlevelId = wTypeLevelId[position - 1];
					System.out.println("db.selectedlevelId : "
							+ db.selectedlevelId); */
	/*				if (isDataAvialableForID("Building", "FK_WorkTyp_ID",
							db.selectedWorkTypeId)) {
						System.out
								.println("Data already present in Building Table");
						if (Integer.parseInt(db.selectedlevelId) >= 1) {
							System.out
									.println("Setting Building Spinner Data.....SchemeID2 : "
											+ schemId2);
							System.out
									.println("Calling setBuildingSpinnerData by passing schemeid as : "
											+ schemId2);
							setBulidngSpinnerData(schemId2);
							structureSpin.setClickable(true);
							structureSpin.setSelection(0);
						}

					} else {
						System.out
								.println("Data not present in Building Table");
						method = "getStructure";
						param = new String[] { "userID", "userRole",
								"projectId", "workTypeId", "parentId" };
						value = new String[] { db.userId, "maker",
								db.selectedSchemeId, db.selectedWorkTypeId, "0" };
						callStructureService();
					} */
                    setBulidngSpinnerData(schemId2);
                    structureSpin.setClickable(true);
                    structureSpin.setSelection(0);
                    System.out.println("SelectedLevelID : "
                            + db.selectedlevelId);
                    System.out.println("SelectedWorkTypeID : "
                            + db.selectedWorkTypeId);
                    System.out.println("SelectedSchemeID : "
                            + db.selectedSchemeId);
                    /***** AKSHAY *****/
                    /*
                     * if(Integer.parseInt(db.selectedlevelId)>=1) {
                     * System.out.println
                     * ("Setting Building Spinner Data.....SchemeID2 : " +
                     * schemId2); System.out.println(
                     * "Calling setBuildingSpinnerData by passing schemeid as : "
                     * + schemId2); setBulidngSpinnerData(schemId2); }
                     *
                     *
                     *
                     *
                     * structureSpin.setClickable(true);
                     * structureSpin.setSelection(0);
                     */
                    /***** AKSHAY *****/
                    // CHANGE MADE ON 24-APRIL-2015
                    /*
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     */
                    /***** AKSHAY *****/

                } else {
                    db.selectedWorkTypeId = "";
                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedlevelId = "";
                    structureSpin.setClickable(false);
                    structureSpin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setBulidngSpinnerData(final String schemeid) {

        // Building(Bldg_ID TEXT,Bldg_Name TEXT,Build_scheme_id TEXT, user_id
        // TEXT)");

	/*	String where = "b.Build_scheme_id = s.PK_Scheme_ID AND b.Build_scheme_id='"
				+ schemeid
				+ "'"
				+ "AND b.FK_WorkTyp_ID='"
				+ db.selectedWorkTypeId
				+ "' AND s.user_id='"
				+ db.userId
				+ "' AND b.user_id='" + db.userId + "'";

		Cursor cursor = db.select("Building as b, Scheme as s",
				" distinct(b.Bldg_ID), b.Bldg_Name", where, null, null, null,
				null);
		System.out.println("Count of Cursor in Building Table is : "
				+ cursor.getCount());
		bldgId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];

		items[0] = "--Select--";
		if (cursor.moveToFirst()) {
			do {
				bldgId[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);

			} while (cursor.moveToNext());
		} else {
			items[0] = "Building(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		// ----------checking worktype id--------------

		final String where1 = "FK_WorkTyp_ID ='" + db.selectedWorkTypeId
				+ "' AND user_id='" + db.userId + "'";
		final String table = "floor";   */
        Cursor cursor = db.select("Building b, AllocateTask a", "distinct(b.Bldg_Name),b.Bldg_ID", "b.Bldg_ID = a.Structure AND b.FK_WorkTyp_ID = '" + db.selectedWorkTypeId + "'", null, null, null, null);
        final String[] buildingID = new String[cursor.getCount()];
        String[] buildingName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                buildingName[i] = "--- SELECT ---";
                do {
                    i++;
                    buildingName[i] = cursor.getString(0);
                    buildingID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            displayDialog("Sorry!", "No Data in Building");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, buildingName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpin.setAdapter(adapter);
        structureSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedBuildingId = buildingID[position - 1];
                    db.selectedNodeId = buildingID[position - 1];
                    // setFloorSpinnerData(bldgId[position - 1],schemeid);
		/*			if (isDataAvialableForID("floor", "FK_Bldg_ID",
							db.selectedBuildingId)) {
						setFloorSpinnerData(bldgId[position - 1], schemeid);
						stageSpin.setClickable(true);
						stageSpin.setSelection(0);
					} else {
						method = "getStage";
						param = new String[] { "userID", "userRole",
								"projectId", "workTypeId", "parentId" };
						value = new String[] { db.userId, "maker",
								db.selectedSchemeId, db.selectedWorkTypeId,
								db.selectedBuildingId };
						callStageService();
					}      */

                    setFloorSpinnerData(buildingID[position - 1], schemeid);
                    stageSpin.setClickable(true);
                    stageSpin.setSelection(0);

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 29 APRIL 2015
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     *
                     * System.out.println("selecteed bdg id" +
                     * db.selectedBuildingId);
                     *
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=2) {
                     * setFloorSpinnerData(bldgId[position - 1],schemeid);
                     * stageSpin.setClickable(true); stageSpin.setSelection(0);
                     *
                     *
                     * }
                     */

                } else {
                    stageSpin.setClickable(false);
                    stageSpin.setSelection(0);
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);
                    subunitspin.setClickable(false);
                    subunitspin.setSelection(0);
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                    /*
                     * // checklistspin.setClickable(false); //
                     * checklistspin.setSelection(0);
                     * grouptspin.setClickable(false);
                     * grouptspin.setSelection(0);
                     */

                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedFloorId = "";
                    db.selectedUnitId = "";
                    db.selectedSubUnitId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setFloorSpinnerData(String buld_id, final String schemeid) {

        // floor (floor_Id TEXT,floor_Name TEXT,Floor_Scheme_ID TEXT,FK_Bldg_ID
        // TEXT, user_id TEXT)");

	/*	String where = "f.Floor_Scheme_ID='" + schemeid
				+ "' AND f.FK_Bldg_ID='" + buld_id + "' AND f.FK_WorkTyp_ID='"
				+ db.selectedWorkTypeId + "' AND f.user_id='" + db.userId
				+ "' AND s.PK_Scheme_ID=f.Floor_Scheme_ID";
		Cursor cursor = db.select("floor as f, Scheme as s",
				"distinct(f.floor_Id),f.floor_Name", where, null, null, null,
				null);

		floorid = new String[cursor.getCount()];
		final String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				floorid[cursor.getPosition()] = cursor.getString(0);
				items[cursor.getPosition() + 1] = cursor.getString(1);
			} while (cursor.moveToNext());
		} else {
			items[0] = "Floor(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		final String where1 = "FK_WorkTyp_ID ='" + db.selectedWorkTypeId
				+ "' AND user_id='" + db.userId + "'";
		final String table = "Unit";   */
        Cursor cursor = db.select("floor f, AllocateTask a", "distinct(f.floor_Name),f.floor_Id", "f.floor_Id = a.Stage AND f.FK_Bldg_ID = '" + db.selectedBuildingId + "' AND f.Floor_Scheme_ID = '" + db.selectedSchemeId + "'", null, null, null, null);
        final String[] floorID = new String[cursor.getCount()];
        String[] floorName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                floorName[i] = "--- SELECT ---";
                do {
                    i++;
                    floorName[i] = cursor.getString(0);
                    floorID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            displayDialog("Sorry!", "No Data in Building");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, floorName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpin.setAdapter(adapter);

        stageSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedFloorId = floorID[position - 1];
                    db.selectedNodeId = floorID[position - 1];
                    // setUnitSpinnerData(floorid[position - 1],schemeid);
                    /***** AKSHAY *****/
                    // CHANGE MADE ON 30-APRIL-2015
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=3) {
                     * setUnitSpinnerData(floorid[position - 1],schemeid);
                     * unitSpin.setClickable(true); unitSpin.setSelection(0); }
                     *//***** AKSHAY *****/
		/*			if (isDataAvialableForID("Unit", "Fk_Floor_ID",
							db.selectedFloorId)) {
						setUnitSpinnerData(db.selectedFloorId,
								db.selectedSchemeId);
						unitSpin.setClickable(true);
						unitSpin.setSelection(0);
					} else {
						method = "getUnit";
						param = new String[] { "userID", "userRole",
								"projectId", "workTypeId", "parentId" };
						value = new String[] { db.userId, "maker",
								db.selectedSchemeId, db.selectedWorkTypeId,
								db.selectedFloorId };
						callUnitService();

					} */
                    Cursor cursor = db.select("Unit u, AllocateTask a", "distinct(u.Unit_Des),u.Unit_ID", "u.Unit_ID = a.Unit AND u.Fk_Floor_ID = '" + db.selectedFloorId + "' AND u.Unit_Scheme_ID = '" + db.selectedSchemeId + "'", null, null, null, null);
                    String[] unitName = new String[cursor.getCount() + 1];
                    if (cursor.getCount() > 0) {
                        setUnitSpinnerData(db.selectedFloorId, db.selectedSchemeId);
                        unitSpin.setClickable(true);
                        unitSpin.setSelection(0);
                    } else {
                        setCheckListSpinnerData();
                        checklistspin.setClickable(true);
                        checklistspin.setSelection(0);
                    }


                } else {
                    unitSpin.setClickable(false);
                    unitSpin.setSelection(0);

                    subunitspin.setClickable(false);
                    subunitspin.setSelection(0);
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedFloorId = "";
                    db.selectedUnitId = "";
                    db.selectedSubUnitId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setUnitSpinnerData(String floor_id, final String schemid) {

        // TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID
        // TEXT, user_id TEXT)");

	/*	String where = "u.Unit_Scheme_id=s.PK_Scheme_ID" + " AND s.user_id='"
				+ db.userId + "'  AND u.Fk_Floor_ID ='" + floor_id
				+ "'  AND u.FK_WorkTyp_ID ='" + db.selectedWorkTypeId
				+ "'  AND u.Unit_Scheme_id='" + schemid
				+ "' AND u.user_id=s.user_id ";
		System.out.println("where ->" + where);
		Cursor cursor = db.select("Unit as u,Scheme as s",
				"distinct(u.Unit_ID),u.Unit_Des", where, null, null, null,
				"u.Unit_Des");

		System.out.println("selected scheme id" + db.selectedBuildingId);

		unitId = new String[cursor.getCount()];
		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				unitId[cursor.getPosition()] = cursor.getString(0);
				System.out.println("trade id printed=" + cursor.getString(0));
				items[cursor.getPosition() + 1] = cursor.getString(1);
				System.out.println("trade name printed=" + cursor.getString(1));
			} while (cursor.moveToNext());
		} else {
			items[0] = "Unit(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		final String where1 = "FK_WorkTyp_ID ='" + db.selectedWorkTypeId
				+ "' AND user_id='" + db.userId + "'";
		final String table = "SubUnit"; */

        Cursor cursor = db.select("Unit u, AllocateTask a", "distinct(u.Unit_Des),u.Unit_ID", "u.Unit_ID = a.Unit AND u.Fk_Floor_ID = '" + db.selectedFloorId + "' AND u.Unit_Scheme_ID = '" + db.selectedSchemeId + "'", null, null, null, null);
        final String[] unitID = new String[cursor.getCount()];
        String[] unitName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                unitName[i] = "--- SELECT ---";
                do {
                    i++;
                    unitName[i] = cursor.getString(0);
                    unitID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            //Toast.makeText(SelectQuestion.this, "No Units....Setting CheckList", Toast.LENGTH_SHORT).show();
            setCheckListSpinnerData();
            checklistspin.setClickable(true);
            checklistspin.setSelection(0);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, unitName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        unitSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedUnitId = unitID[position - 1];
                    db.selectedNodeId = unitID[position - 1];

                    System.out.println("Selected Unit ID = " + db.selectedUnitId);

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 30-APRIL-2015
                    // setSubUnitSpinnerData(unitId[position - 1],schemid);
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=4) {
                     * setSubUnitSpinnerData(unitId[position - 1],schemid);
                     * subunitspin.setClickable(true);
                     * subunitspin.setSelection(0); }
                     *//***** AKSHAY *****/

		/*			if (isDataAvialableForID("SubUnit", "FK_Unit_ID",
							db.selectedUnitId)) {
						setSubUnitSpinnerData(db.selectedUnitId,
								db.selectedSchemeId);
						subunitspin.setClickable(true);
						subunitspin.setSelection(0);
					} else {
						method = "getSubUnit";
						param = new String[] { "userID", "userRole",
								"projectId", "workTypeId", "parentId" };
						value = new String[] { db.userId, "maker",
								db.selectedSchemeId, db.selectedWorkTypeId,
								db.selectedUnitId };
						callSubUnitService();
					} */

                    Cursor cursor = db.select("SubUnit s, AllocateTask a",
                            "distinct(s.Sub_Unit_Des),s.Sub_Unit_ID", "s.Sub_Unit_ID = a.SubUnit AND s.FK_Unit_ID in(" + db.selectedUnitId + ") " +
                                    "AND s.Sub_Unit_Scheme_id ='" + db.selectedSchemeId + "'", null, null, null, null);
                    if (cursor.getCount() > 0) {
                        setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);
                        subunitspin.setClickable(true);
                        subunitspin.setSelection(0);
                    } else {
                        setCheckListSpinnerData();
                        checklistspin.setSelected(true);
                        checklistspin.setSelection(0);
                    }


//                    setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);
//                    subunitspin.setClickable(true);
//                    subunitspin.setSelection(0);


                } else {
                    subunitspin.setClickable(false);
                    subunitspin.setSelection(0);
                    elementspin.setClickable(false);
                    elementspin.setSelection(0);
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedUnitId = "";
                    db.selectedSubUnitId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                    System.out.println("hello else unit");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isUnit && unitName.length > 0) {
            unitSpin.setVisibility(View.GONE);
            unitSpinMulti.setVisibility(View.VISIBLE);

            unitSpinMulti.setItems(unitName);
            unitSpinMulti.hasNoneOption(true);
            unitSpinMulti.setSelection(new int[]{0});
            unitSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedNodeId = "";
                    db.selectedUnitId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedNodeId += unitID[indices.get(count) - 1];

                            db.selectedUnitId += unitID[indices.get(count) - 1];
                        } else {
                            db.selectedNodeId += unitID[indices.get(count) - 1] + ",";
                            db.selectedUnitId += unitID[indices.get(count) - 1] + ",";
                        }

                    }
                    setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });

        } else {
            unitSpin.setVisibility(View.VISIBLE);
            unitSpinMulti.setVisibility(View.GONE);
        }


    }

    private void setSubUnitSpinnerData(String unit_id, final String schemid) {

        // TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID
        // TEXT, user_id TEXT)");
        // SubUnit(Sub_Unit_ID TEXT,Sub_Unit_Des TEXT,Sub_Unit_Scheme_id
        // TEXT,FK_Unit_ID TEXT, user_id TEXT)");

        Cursor cursor = db.select("SubUnit s, AllocateTask a",
                "distinct(s.Sub_Unit_Des),s.Sub_Unit_ID", "s.Sub_Unit_ID = a.SubUnit AND s.FK_Unit_ID in(" + db.selectedUnitId + ") " +
                        "AND s.Sub_Unit_Scheme_id ='" + db.selectedSchemeId + "'", null, null, null, null);
        final String[] subUnitID = new String[cursor.getCount()];
        String[] subUnitName = new String[cursor.getCount() + 1];
        subUnitName[0] = "--- SELECT ---";
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                subUnitName[i] = "--- SELECT ---";
                do {
                    i++;
                    subUnitName[i] = cursor.getString(0);
                    subUnitID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            //System.out.println("in sub units........");
            //Toast.makeText(SelectQuestion.this, "No SubUnits....Setting CheckList", Toast.LENGTH_SHORT).show();
            setCheckListSpinnerData();
            checklistspin.setClickable(true);
            checklistspin.setSelection(0);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subUnitName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subunitspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        subunitspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubUnitId = subUnitID[position - 1];
                    db.selectedNodeId = subUnitID[position - 1];

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 30-APRIL-2015
                    // setElementSpinnerData(subunitId[position - 1],schemid);
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=5) {
                     * setElementSpinnerData(subunitId[position - 1],schemid);
                     * elementspin.setClickable(true);
                     * elementspin.setSelection(0); }
                     */

		/*			if (isDataAvialableForID("Element", "FK_Sub_Unit_ID",
							db.selectedSubUnitId)) {
						setElementSpinnerData(db.selectedSubUnitId,
								db.selectedSchemeId);
						elementspin.setClickable(true);
						elementspin.setSelection(0);
					} else {

						method = "getElement";
						param = new String[] { "userID", "userRole",
								"projectId", "workTypeId", "parentId" };
						value = new String[] { db.userId, "maker",
								db.selectedSchemeId, db.selectedWorkTypeId,
								db.selectedSubUnitId };
						callElementService();

					}   */

				/*	setElementSpinnerData(db.selectedSubUnitId,db.selectedSchemeId);
					elementspin.setClickable(true);
					elementspin.setSelection(0); */
                    Cursor cursor = db.select("Element e, AllocateTask a", "distinct(e.Elmt_Des),e.Elmt_ID",
                            "e.Elmt_ID = a.Element AND e.FK_Sub_Unit_ID = '" + db.selectedSubUnitId + "' AND e.Elmt_Scheme_id = '" + db.selectedSchemeId + "'",
                            null, null, null, null);
//                    final String[] elementID = new String[cursor.getCount()];
//                    String[] elementName = new String[cursor.getCount() + 1];
                    if (cursor.getCount() > 0) {
                        setElementSpinnerData(db.selectedSubUnitId, db.selectedSchemeId);
                        elementspin.setClickable(true);
                        elementspin.setSelection(0);
                    } else {
                        setCheckListSpinnerData();
                        checklistspin.setSelected(true);
                        checklistspin.setSelection(0);
                    }

                } else {

                    try {
                        //	elementspin.setClickable(false);
                        //elementspin.setSelection(0);
                        //subelementspin.setClickable(false);
                        //subelementspin.setSelection(0);

                        db.selectedChecklistId = "";
                        db.selectedSubGroupId = "";
                        db.selectedSubUnitId = "";
                        db.selectedElementId = "";
                        db.selectedSubElementId = "";
                    } catch (Exception e) {
                        System.out.println("exception=====" + e.getMessage());
                    }
                    System.out.println("hello sub unit else");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        if (isSubUnit && subUnitName.length > 0) {
            subunitspin.setVisibility(View.GONE);
            subUnitSpinMulti.setVisibility(View.VISIBLE);
            subUnitSpinMulti.setItems(subUnitName);
            subUnitSpinMulti.hasNoneOption(true);
            subUnitSpinMulti.setSelection(new int[]{0});
            subUnitSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedNodeId = "";
                    db.selectedSubElementId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedNodeId += subUnitID[indices.get(count) - 1];

                            db.selectedSubUnitId += subUnitID[indices.get(count) - 1];
                        } else {
                            db.selectedNodeId += subUnitID[indices.get(count) - 1] + ",";
                            db.selectedSubUnitId += subUnitID[indices.get(count) - 1] + ",";
                        }

                    }
                    setElementSpinnerData(db.selectedSubUnitId, db.selectedSchemeId);

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });

        } else {
            subunitspin.setVisibility(View.VISIBLE);
            subUnitSpinMulti.setVisibility(View.GONE);
        }


    }

    private void setElementSpinnerData(String subunit_id, final String schemid) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        Cursor cursor = db.select("Element e, AllocateTask a",
                "distinct(e.Elmt_Des),e.Elmt_ID",
                "e.Elmt_ID = a.Element AND e.FK_Sub_Unit_ID in (" +
                        db.selectedSubUnitId + ") AND e.Elmt_Scheme_id = '"
                        + db.selectedSchemeId + "'", null, null, null, null);
        final String[] elementID = new String[cursor.getCount()];
        String[] elementName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                elementName[i] = "--- SELECT ---";
                do {
                    i++;
                    elementName[i] = cursor.getString(0);
                    elementID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            //Toast.makeText(SelectQuestion.this, "No Elements....Setting CheckList", Toast.LENGTH_SHORT).show();
            setCheckListSpinnerData();
            checklistspin.setClickable(true);
            checklistspin.setSelection(0);

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, elementName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elementspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        elementspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedElementId = elementID[position - 1];
                    db.selectedNodeId = elementID[position - 1];
                    // setSubElementSpinnerData(elementId[position -
                    // 1],schemid);

                    /***** AKSHAY *****/
                    // CHANGES MADE ON : 30-APRIL-2015
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *
                     *
                     * if(CheckWorkType(table, where1) &&
                     * Integer.parseInt(db.selectedlevelId)>=6) {
                     * setSubElementSpinnerData(elementId[position -
                     * 1],schemid); subelementspin.setClickable(true);
                     * subelementspin.setSelection(0); }
                     *//***** AKSHAY *****/

	/*				if (isDataAvialableForID("SubElement", "FK_Elmt_ID",
							db.selectedElementId)) {
						setSubElementSpinnerData(db.selectedElementId,
								db.selectedSchemeId);
						subelementspin.setClickable(true);
						subelementspin.setSelection(0);
					} else {
						method = "getSubElement";
						param = new String[] { "userID", "userRole",
								"projectId", "workTypeId", "parentId" };
						value = new String[] { db.userId, "maker",
								db.selectedSchemeId, db.selectedWorkTypeId,
								db.selectedElementId };
						callSubElementService();
					}    */
                    Cursor cursor = db.select("SubElement s, AllocateTask a",
                            "distinct(s.Sub_Elmt_Des),s.Sub_Elmt_ID", "s.Sub_Elmt_ID = a.SubElement AND s.FK_Elmt_ID in (" + db.selectedElementId + ") AND s.Sub_Elmt_Scheme_id = '"
                                    + db.selectedSchemeId + "'", null, null, null, null);
//                    final String[] elementID = new String[cursor.getCount()];
//                    String[] elementName = new String[cursor.getCount() + 1];
                    if (cursor.getCount() > 0) {
                        setSubElementSpinnerData(db.selectedElementId, db.selectedSchemeId);
                        subelementspin.setClickable(true);
                        subelementspin.setSelection(0);
                    } else {
                        setCheckListSpinnerData();
                        checklistspin.setSelected(true);
                        checklistspin.setSelection(0);
                    }
//                    setSubElementSpinnerData(db.selectedElementId, db.selectedSchemeId);
//                    subelementspin.setClickable(true);
//                    subelementspin.setSelection(0);

                } else {
                    subelementspin.setClickable(false);
                    subelementspin.setSelection(0);
                    checklistspin.setClickable(true);
                    checklistspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedElementId = "";
                    db.selectedSubElementId = "";

                    System.out.println("hello");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isElement && elementName.length > 0) {
            elementSpinMulti.setItems(elementName);
            elementSpinMulti.hasNoneOption(true);
            elementSpinMulti.setSelection(new int[]{0});
            elementSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedNodeId = "";
                    db.selectedElementId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedNodeId += elementID[indices.get(count) - 1];

                            db.selectedElementId += elementID[indices.get(count) - 1];
                        } else {
                            db.selectedNodeId += elementID[indices.get(count) - 1] + ",";
                            db.selectedElementId += elementID[indices.get(count) - 1] + ",";
                        }

                    }
                    setSubElementSpinnerData(db.selectedElementId, db.selectedSchemeId);

                }

                @Override
                public void selectedStrings(List<String> strings) {

                }
            });

            elementspin.setVisibility(View.GONE);
            elementSpinMulti.setVisibility(View.VISIBLE);
        } else {
            elementspin.setVisibility(View.VISIBLE);
            elementSpinMulti.setVisibility(View.GONE);
        }


    }

    private void setSubElementSpinnerData(String element_id,
                                          final String schemid) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // SubElement(Sub_Elmt_ID TEXT,Sub_Elmt_Des TEXT,Sub_Elmt_Scheme_id
        // TEXT,FK_Elmt_ID TEXT, user_id TEXT)");


        Cursor cursor = db.select("SubElement s, AllocateTask a",
                "distinct(s.Sub_Elmt_Des),s.Sub_Elmt_ID", "s.Sub_Elmt_ID = a.SubElement AND s.FK_Elmt_ID in (" + db.selectedElementId + ") AND s.Sub_Elmt_Scheme_id = '"
                        + db.selectedSchemeId + "'", null, null, null, null);
        final String[] subelementID = new String[cursor.getCount()];
        String[] subelementName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                subelementName[i] = "--- SELECT ---";
                do {
                    i++;
                    subelementName[i] = cursor.getString(0);
                    subelementID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            //Toast.makeText(SelectQuestion.this, "No SubElements....Setting CheckList", Toast.LENGTH_SHORT).show();
            setCheckListSpinnerData();
            checklistspin.setClickable(true);
            checklistspin.setSelection(0);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, subelementName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subelementspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        subelementspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubElementId = subelementID[position - 1];
                    db.selectedNodeId = subelementID[position - 1];

                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 4-MAY-2015
                    /*
                     * setCheckListSpinnerData();
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     *//***** AKSHAY *****/

			/*		if (isDataAvialableInCheckList(db.selectedNodeId,
							db.selectedWorkTypeId, db.userId)) {
						setCheckListSpinnerData();
						checklistspin.setClickable(true);
						checklistspin.setSelection(0);
					} else {
						method = "getCheckList";
						param = new String[] { "userID", "userRole",
								"workTypeId", "nodeId" };
						value = new String[] { db.userId, "maker",
								db.selectedWorkTypeId, db.selectedNodeId };
						callCheckListService();
					} */

                    setCheckListSpinnerData();
                    checklistspin.setClickable(true);
                    checklistspin.setSelection(0);

                } else {

                    checklistspin.setClickable(false);
                    checklistspin.setSelection(0);
                    grouptspin.setClickable(false);
                    grouptspin.setSelection(0);

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedSubElementId = "";

                    System.out.println("else subelement ");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isSubElement && subelementName.length > 0) {
            subElementSpinMulti.setVisibility(View.VISIBLE);
            subelementspin.setVisibility(View.GONE);
            subElementSpinMulti.hasNoneOption(true);
            subElementSpinMulti.setSelection(new int[]{0});
            groupSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedNodeId = "";
                    db.selectedSubElementId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedNodeId += subelementID[indices.get(count) - 1];

                            db.selectedSubElementId += subelementID[indices.get(count) - 1];
                        } else {
                            db.selectedNodeId += subelementID[indices.get(count) - 1] + ",";
                            db.selectedSubElementId += subelementID[indices.get(count) - 1] + ",";
                        }

                    }

                }

                @Override
                public void selectedStrings(List<String> strings) {


                }
            });
        } else {
//            subElementSpinMulti.setVisibility(View.GONE);
            subelementspin.setVisibility(View.VISIBLE);
            Log.d("CheckList", "cursor.getString(0)");

        }

//        subElementSpinMulti.setItems(subelementName);

    }

    public boolean isDataAvialableInCheckList(String nodeID, String worktypeID,
                                              String userID) {
        Cursor cursor = db.select("CheckList", "count(*) as noitem",
                "user_id='" + db.userId + "' AND Node_Id ='" + nodeID
                        + "' AND FK_WorkTyp_ID = '" + worktypeID + "'", null,
                null, null, null);
        System.out.println("user_id='" + db.userId + "' AND Node_Id ='"
                + nodeID + "' AND FK_WorkTyp_ID = '" + worktypeID + "'");
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d("CheckList", cursor.getString(0));
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

    // CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT, user_id
    // TEXT)");

    private void setCheckListSpinnerData() {
        checklistspin.setClickable(true);
        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT, user_id
        // TEXT)");
        //Toast.makeText(getApplicationContext(), "In CheckList Method", Toast.LENGTH_SHORT).show();
        Cursor cursor = db.select("CheckList c, AllocateTask a",
                "distinct(c.CheckList_Name), c.CheckList_ID",
                "c.CheckList_ID = a.CheckList AND c.FK_WorkTyp_ID = '"
                        + db.selectedWorkTypeId + "' AND a.NodeID = '" + db.selectedNodeId + "'", null, null, null, null);
        final String[] checklistID = new String[cursor.getCount()];
        String[] checklistName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                checklistName[i] = "--- SELECT ---";
                do {
                    System.out.println("======================-----" + cursor.getString(0));
                    i++;
                    checklistName[i] = cursor.getString(0);
                    checklistID[i - 1] = cursor.getString(1);

                } while (cursor.moveToNext());
            } else {
                displayDialog("Sorry!", "No Data");
            }
        } else {
            //Toast.makeText(SelectQuestion.this, "No CheckLists....Setting CheckList", Toast.LENGTH_SHORT).show();
        }

		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, checklistName);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		checklistspin.setAdapter(adapter);
		adapter.notifyDataSetChanged();*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, checklistName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        checklistspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedChecklistId = checklistID[position - 1];
                    db.selectedChecklistName = checklistspin.getSelectedItem()
                            .toString();
                    /***** AKSHAY *****/
                    // CHANGE MADE ON : 4-MAY-2015
                    /*
                     * setGoupSpinnerData(checklistId[position - 1]);
                     * grouptspin.setClickable(true);
                     * grouptspin.setSelection(0);
                     */
					/*if (isDataAvialableForID("Group1", "FK_Checklist_ID",
							db.selectedChecklistId)) {
						setGoupSpinnerData(checklistId[position - 1]);
						grouptspin.setClickable(true);
						grouptspin.setSelection(0);
					} else {*/
                    method = "getGroup";
                    param = new String[]{"userID", "userRole", "nodeId",
                            "checkListId"};
                    value = new String[]{db.userId, "maker",
                            db.selectedNodeId, db.selectedChecklistId};
                    callGroupService();
                    //	}

                    setGoupSpinnerData(checklistID[position - 1]);
                    grouptspin.setClickable(true);
                    grouptspin.setSelection(0);

                } else {
                    db.selectedChecklistId = "";
                    grouptspin.setClickable(false);
                    grouptspin.setSelection(0);
                    db.selectedGroupId = "";
                    System.out.println("else checklist...");

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setGoupSpinnerData(final String checklistId) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // Group1(Grp_ID TEXT,Grp_Name TEXT,Node_id TEXT,FK_Checklist_ID
        // TEXT,user_id TEXT)");

	/*	String where = "c.Checklist_ID='" + checklistId + "' AND g.Node_id='"
				+ db.selectedNodeId
				+ "'  AND  g.FK_Checklist_ID=c.Checklist_ID"
				+ " AND c.user_id='" + db.userId

				+ "' AND g.user_id=c.user_id ";
		System.out.println("where ->" + where);
		Cursor cursor = db.select("Group1 as g,CheckList as c",
				"distinct(g.Grp_ID),g.Grp_Name,g.Node_id", where, null, null,
				null, "g.Grp_Name");

		// System.out.println("selected scheme id"+db.selectedBuildingId);

		groupId = new String[cursor.getCount()];

		String[] items = new String[cursor.getCount() + 1];
		items[0] = "--Select--";
		if (cursor.moveToFirst()) {

			do {
				groupId[cursor.getPosition()] = cursor.getString(0);

				items[cursor.getPosition() + 1] = cursor.getString(1);

				System.out
						.println("goup id==" + cursor.getString(2).toString());
			} while (cursor.moveToNext());
		} else {
			items[0] = "Group(s) not available";
		}

		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}     */
        //	Toast.makeText(SelectQuestion.this, "No Groups....Setting CheckList", Toast.LENGTH_SHORT).show();
        System.out.println("setting data to gropu spinner");
        Cursor cursor = db.select("Group1 g, AllocateTask a",
                "distinct(g.Grp_Name),g.Grp_ID",
                "g.Grp_ID = a.GroupColumn AND g.FK_Checklist_ID = '" +
                        db.selectedChecklistId + "'", null, null, null, "g.GRP_Sequence_tint");
        final String[] groupID = new String[cursor.getCount()];
        final String[] groupName = new String[cursor.getCount() + 1];
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                int i = 0;
                groupName[i] = "--- SELECT ---";
                do {
                    i++;
                    groupName[i] = cursor.getString(0);
                    groupID[i - 1] = cursor.getString(1);
                    System.out.println("Printed group ID to print=========" + groupID[i - 1]);
                    isGroupDataAvailable = true;
                } while (cursor.moveToNext());
            } else {
                isGroupDataAvailable = false;
                displayDialog("Sorry!", "No Data");
            }
        } else {
            //Toast.makeText(SelectQuestion.this, "No Groups....Setting CheckList", Toast.LENGTH_SHORT).show();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, groupName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grouptspin.setAdapter(adapter);

        grouptspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedGroupId = groupID[position - 1];
                    System.out.println("Selected Group ID is : "
                            + db.selectedGroupId);

                    db.selectedGroupName = groupName[position - 1];
                    coverageData();
                    /*
                     * // System.out.println("Size Of Group List is : " +
                     * groupList.size());
                     * db.selectedGroupName=grouptspin.getSelectedItem
                     * ().toString(); String listIndexGroup = ""; for(int i = 0;
                     * i < groupList.size(); i++) {
                     * System.out.println("Iteration : " + i); Group temp =
                     * groupList.get(i); if(temp.getGroupID() ==
                     * db.selectedGroupId) { listIndexGroup =
                     * temp.getGroupSequenceInt();
                     * System.out.println("Selected Group Sequence Number is : "
                     * + listIndexGroup); break; } else { continue; } }
                     */
                    System.out.println("selected group===" + db.selectedGroupId);
                    if (isSelectedGroupSequenceOne(db.selectedGroupId)) {
                        if (isCoverageSpinner) {
                            isCoverageSpinner = false;
                            isCoverageTextViewNew = true;
                            System.out.println("In group sequence = 1 block");
                            View change = (View) findViewById(R.id.coverage_id_spinner);
                            ViewGroup parent = (ViewGroup) change.getParent();
                            int index = parent.indexOfChild(change);
                            parent.removeView(change);
                            LayoutInflater inflater = getLayoutInflater();
                            change = inflater.inflate(
                                    R.layout.select_question_coverage_textview,
                                    parent, false);
                            parent.addView(change, index);
                            coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
                            //coverageedit=coverageedit.replace("\n", "").replace("\r", "");
                        } else {
                            if (isCoverageTextViewNew) {
                                coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
                                isCoverageSpinner = false;
                            } else {
                                coverageedit = (EditText) findViewById(R.id.coverage_id);
                                isCoverageSpinner = false;
                            }
                        }
                        System.out.println("Enter Coverage Manually!!!");
                    } else {
                        if (isCoverageSpinner) {
                            method = "getCoverage";
                            param = new String[]{"nodeId", "checkListId"};
                            value = new String[]{db.selectedNodeId,
                                    db.selectedChecklistId};
                            // value = new String[] { "29","1"};
                            callCoverageService();
                        } else {
                            View change;
                            if (isCoverageTextViewNew) {
                                change = (View) findViewById(R.id.coverage_id_edittext);
                            } else {
                                change = (View) findViewById(R.id.coverage_id);
                            }
                            ViewGroup parent = (ViewGroup) change.getParent();
                            int index = parent.indexOfChild(change);
                            parent.removeView(change);
                            LayoutInflater inflater = getLayoutInflater();
                            change = inflater.inflate(
                                    R.layout.select_question_coverage_spinner,
                                    parent, false);
                            parent.addView(change, index);
                            coverageSpinner = (Spinner) findViewById(R.id.coverage_id_spinner);
                            isCoverageSpinner = true;
                            method = "getCoverage";
                            param = new String[]{"nodeId", "checkListId"};
                            value = new String[]{db.selectedNodeId,
                                    db.selectedChecklistId};
                            // value = new String[] { "29","1"};
                            callCoverageService();
                        }
                    }
                    /*
                     * groupList.clear();
                     * if(listIndexGroup.equalsIgnoreCase("1")) {
                     * System.out.println("Enter Coverage Manually!!!"); } else
                     * { View change = (View) findViewById(R.id.coverage_id);
                     * ViewGroup parent = (ViewGroup) change.getParent(); int
                     * index = parent.indexOfChild(change);
                     * parent.removeView(change); LayoutInflater inflater =
                     * getLayoutInflater(); change =
                     * inflater.inflate(R.layout.select_question_coverage_spinner
                     * , parent, false); parent.addView(change, index);
                     * coverageSpinner = (Spinner)
                     * findViewById(R.id.coverage_id_spinner); method =
                     * "getCoverage"; param = new String[] {
                     * "nodeId","checkListId"}; value = new String[] {
                     * db.selectedNodeId,db.selectedChecklistId};
                     * callCoverageService(); }
                     */
                    if (isQuestionAvailable()) {
                        System.out
                                .println("Data Already Available in Question table");
                    } else {
                        method = "getQuestion";
                        param = new String[]{"userID", "userRole", "nodeId",
                                "groupId"};
                        value = new String[]{db.userId, "maker",
                                db.selectedNodeId, db.selectedGroupId};
                        callQuestionService();
                    }

                } else {
                    db.selectedGroupId = "";
                    System.out.println("else..................");
                }
            }

            public boolean isSelectedGroupSequenceOne(String userSelectedGroupID) {
				/*
				String columns1 = "min(GRP_Sequence_tint)";
				//String where1 = "Grp_ID = '" + userSelectedGroupID + "'";
				Cursor cursor1 = db.select("Group1", columns1, null, null, null,
						null, null);
				String group_id_req=cursor1.getString(cursor1
						.getColumnIndex("GRP_Sequence_tint"));
				if(group_id_req<userSelectedGroupID)*/
                String columns = "GRP_Sequence_tint";
                String where = "Grp_ID = '" + userSelectedGroupID + "'";
                Cursor cursor = db.select("Group1", columns, where, null, null,
                        null, null);
                if (cursor.moveToFirst()) {
                    String value = cursor.getString(cursor
                            .getColumnIndex("GRP_Sequence_tint"));

                    System.out.println("selected sequesnce byPramod " + value);
                    if (value.equalsIgnoreCase("1")) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    System.out.println("Cusror Count Pramod : " + cursor.getCount());
                    return false;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isGroup && groupName.length > 0) {
            grouptspin.setVisibility(View.VISIBLE);
            subelementspin.setVisibility(View.GONE);

            groupSpinMulti.setItems(groupName);
            groupSpinMulti.hasNoneOption(true);
            groupSpinMulti.setSelection(new int[]{0});
            groupSpinMulti.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                @Override
                public void selectedIndices(List<Integer> indices) {
                    db.selectedGroupId = "";
                    for (int count = 0; count < indices.size(); count++) {
                        if (count == indices.size() - 1) {
                            db.selectedGroupId += groupID[indices.get(count) - 1];
                        } else {
                            db.selectedGroupId += groupID[indices.get(count) - 1] + ",";
                        }

                    }

                }

                @Override
                public void selectedStrings(List<String> strings) {

                    db.selectedGroupName = "";
                    for (int count = 0; count < strings.size(); count++) {
                        if (count == strings.size() - 1) {
                            db.selectedGroupName += strings.get(count);
                        } else {
                            db.selectedGroupName += strings.get(count) + ",";
                        }

                    }


                }
            });


        } else {
            grouptspin.setVisibility(View.VISIBLE);
            subelementspin.setVisibility(View.VISIBLE);
        }


    }


    public boolean CheckWorkType(String table, String where) {
        /*
         * String where = "user_id='" + db.userId;
         *
         * Cursor cursor = db.select("Group1 as g,CheckList as c",
         * "distinct(g.Grp_ID),g.Grp_Name,g.Node_id", where, null, null, null,
         * "g.Grp_Name");
         */

        Cursor cursor = db.select(table, "count(*) as noitem", where, null,
                null, null, null);

        System.out.println("in checkWorkType..................");
        // db.select(TABLE_NAME, COLUMNS, WHERE, SELECTION_ARGS, GROUP_BY,
        // HAVING, OREDER_BY)
        if (cursor.moveToFirst()) {
            System.out.println(Integer.parseInt(cursor.getString(0)));
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                System.out
                        .println("entry present..............for worktype id");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.selectedHeadingId = "";
        finish();
        db.closeDb();
        startActivity(new Intent(this, HomeScreen.class));
    }


    //changed by sayali
    //27-2-2024
    protected void callService1() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                responseData = data;
                System.out.println("success data");

                saveCoverageData(data);

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

    // -------------------------------------------------------------------------balaji
    // code-----------
    protected void callService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                responseData = data;
                System.out.println("success data");
                saveData(data);

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

    protected void callStructureService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                responseData = data;
                System.out.println("success data");
                saveStructureData(data);

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

    protected void saveStructureData(String data) {
        // TODO Auto-generated method stub
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No Data!!!");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray structureArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < structureArray.length(); i++) {
                    JSONObject structureObject = structureArray
                            .getJSONObject(i);
                    String building_id = structureObject.getString("NODE_Id");
                    String building_name = structureObject
                            .getString("NODE_Description_var");
                    String building_project_id = structureObject
                            .getString("NODE_PRJ_Id");
                    String parent_id = structureObject
                            .getString("NODE_Parent_Id");
                    String fk_worktype_id = structureObject
                            .getString("NDCHKL_WT_Id");
                    String column = "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id";
                    String values = "'" + building_id + "','" + building_name
                            + "','" + building_project_id + "','"
                            + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("Building", column, values);
                }
                setBulidngSpinnerData(db.selectedSchemeId);
                structureSpin.setClickable(true);
                structureSpin.setSelection(0);

            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callStageService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveStageData(data);
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

    public void saveStageData(String data) {
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No DATA!!!");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray stageArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < stageArray.length(); i++) {
                    JSONObject stageObject = stageArray.getJSONObject(i);
                    String floor_id = stageObject.getString("NODE_Id");
                    String floor_name = stageObject
                            .getString("NODE_Description_var");
                    String floor_scheme_id = stageObject
                            .getString("NODE_PRJ_Id");
                    String fk_building_id = stageObject
                            .getString("NODE_Parent_Id");
                    String fk_worktype_id = stageObject
                            .getString("NDCHKL_WT_Id");
                    String column = "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + floor_id + "','" + floor_name + "','"
                            + floor_scheme_id + "','" + fk_building_id + "','"
                            + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("floor", column, values);
                }
                setFloorSpinnerData(db.selectedBuildingId, db.selectedSchemeId);
                stageSpin.setClickable(true);
                stageSpin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callUnitService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveUnitData(data);
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

    public void saveUnitData(String data) {
        if (data.equalsIgnoreCase("$")) {
            unitSpin.setClickable(false);
            subunitspin.setClickable(false);
            elementspin.setClickable(false);
            subelementspin.setClickable(false);
            if (isDataAvialableInCheckList(db.selectedNodeId,
                    db.selectedWorkTypeId, db.userId)) {
                setCheckListSpinnerData();
                checklistspin.setClickable(true);
                checklistspin.setSelection(0);
            } else {
                method = "getCheckList";
                param = new String[]{"userID", "userRole", "workTypeId",
                        "nodeId"};
                value = new String[]{db.userId, "maker",
                        db.selectedWorkTypeId, db.selectedNodeId};
                callCheckListService();
            }
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray unitArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < unitArray.length(); i++) {
                    JSONObject stageObject = unitArray.getJSONObject(i);
                    String unit_id = stageObject.getString("NODE_Id");
                    String unit_name = stageObject
                            .getString("NODE_Description_var");
                    String unit_scheme_id = stageObject
                            .getString("NODE_PRJ_Id");
                    String fk_floor_id = stageObject
                            .getString("NODE_Parent_Id");
                    String fk_worktype_id = stageObject
                            .getString("NDCHKL_WT_Id");
                    String column = "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + unit_id + "','" + unit_name + "','"
                            + unit_scheme_id + "','" + fk_floor_id + "','"
                            + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("Unit", column, values);
                }
                setUnitSpinnerData(db.selectedFloorId, db.selectedSchemeId);
                unitSpin.setClickable(true);
                unitSpin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callSubUnitService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveSubUnitData(data);
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

    public void saveSubUnitData(String data) {
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No Data");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray subunitArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < subunitArray.length(); i++) {
                    JSONObject stageObject = subunitArray.getJSONObject(i);
                    String sub_unit_id = stageObject.getString("NODE_Id");
                    String sub_unit_name = stageObject
                            .getString("NODE_Description_var");
                    String sub_unit_scheme_id = stageObject
                            .getString("NODE_PRJ_Id");
                    String fk_unit_id = stageObject.getString("NODE_Parent_Id");
                    String fk_worktype_id = stageObject
                            .getString("NDCHKL_WT_Id");
                    String column = "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + sub_unit_id + "','" + sub_unit_name
                            + "','" + sub_unit_scheme_id + "','" + fk_unit_id
                            + "','" + fk_worktype_id + "','" + db.userId + "'";
                    db.insert("SubUnit", column, values);
                }
                setSubUnitSpinnerData(db.selectedUnitId, db.selectedSchemeId);
                subunitspin.setClickable(true);
                subunitspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callElementService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveElementData(data);
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

    public void saveElementData(String data) {
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No Data!!!");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray elementArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < elementArray.length(); i++) {
                    JSONObject stageObject = elementArray.getJSONObject(i);
                    String element_id = stageObject.getString("NODE_Id");
                    String element_name = stageObject
                            .getString("NODE_Description_var");
                    String element_scheme_id = stageObject
                            .getString("NODE_PRJ_Id");
                    String fk_sub_unit_id = stageObject
                            .getString("NODE_Parent_Id");
                    String fk_worktype_id = stageObject
                            .getString("NDCHKL_WT_Id");
                    String column = "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + element_id + "','" + element_name
                            + "','" + element_scheme_id + "','"
                            + fk_sub_unit_id + "','" + fk_worktype_id + "','"
                            + db.userId + "'";
                    db.insert("Element", column, values);
                }
                setElementSpinnerData(db.selectedSubUnitId, db.selectedSchemeId);
                elementspin.setClickable(true);
                elementspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callSubElementService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveSubElementData(data);
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

    public void saveSubElementData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray subElementArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < subElementArray.length(); i++) {
                    JSONObject stageObject = subElementArray.getJSONObject(i);
                    String sub_element_id = stageObject.getString("NODE_Id");
                    String sub_element_name = stageObject
                            .getString("NODE_Description_var");
                    String sub_element_scheme_id = stageObject
                            .getString("NODE_PRJ_Id");
                    String fk_element_id = stageObject
                            .getString("NODE_Parent_Id");
                    String fk_worktype_id = stageObject
                            .getString("NDCHKL_WT_Id");
                    String column = "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id";
                    String values = "'" + sub_element_id + "','"
                            + sub_element_name + "','" + sub_element_scheme_id
                            + "','" + fk_element_id + "','" + fk_worktype_id
                            + "','" + db.userId + "'";
                    db.insert("SubElement", column, values);
                }
                setSubElementSpinnerData(db.selectedElementId,
                        db.selectedSchemeId);
                subelementspin.setClickable(true);
                subelementspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callCheckListService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveCheckListData(data);
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

    public void saveCheckListData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray checkListArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < checkListArray.length(); i++) {
                    JSONObject stageObject = checkListArray.getJSONObject(i);
                    String checklist_id = stageObject.getString("CHKL_Id");
                    String checklist_name = stageObject
                            .getString("CHKL_Name_var");
                    String node_id = stageObject.getString("NDUSER_NODE_Id");
                    String worktype_id = stageObject.getString("NDUSER_WT_Id");
                    String column = "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id";
                    String values = "'" + checklist_id + "','" + checklist_name
                            + "','" + node_id + "','" + worktype_id + "','"
                            + db.userId + "'";
                    db.insert("CheckList", column, values);
                }
                setCheckListSpinnerData();
                checklistspin.setClickable(true);
                checklistspin.setSelection(0);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callCoverageService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                setCoverageSpinnerData(data);
                coverageSpinner.setClickable(true);
                coverageSpinner.setSelection(0);
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

    public void setCoverageSpinnerData(String data) {
        if (data.equalsIgnoreCase("$")) {
            System.out.println("No Data");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                ArrayList<Coverage> coverageList = new ArrayList<Coverage>();

                ArrayList<String> coverageNamesList = new ArrayList<String>();

                JSONArray coverageArray = new JSONArray(tabledata[0]);
                System.out.println("table data----------" + coverageArray);
                for (int i = 0; i < coverageArray.length(); i++) {

                    Coverage coverage = new Coverage();
                    JSONObject stageObject = coverageArray.getJSONObject(i);

                    String rfi_group_id = stageObject.getString("RFI_GRP_Id");
                    String rfi_node_id = stageObject.getString("RFI_NODE_Id");
                    String rfi_gorup_id = stageObject.getString("RFI_GRP_Id");
                    String rfi_checklist_id = stageObject
                            .getString("RFI_CHKL_Id");
                    String rfi_coverage_var = stageObject
                            .getString("RFI_Coverage_var");

                    System.out.println("table data------111111----" + rfi_coverage_var);
                    // if(db.selectedGroupName.equalsIgnoreCase(rfi_group_id)){
                    if (db.selectedGroupId.equalsIgnoreCase(rfi_group_id)) {
                        coverage.setRFI_GRP_Id(rfi_group_id);
                        coverage.setRFI_NODE_Id(rfi_node_id);
                        coverage.setRFI_CHKL_Id(rfi_checklist_id);


                        coverage.setRFI_Coverage_var(rfi_coverage_var);
                        if (db.selectedGroupId.equalsIgnoreCase(rfi_gorup_id)) {
                            coverageList.add(coverage);
                            coverageNamesList.add(rfi_coverage_var);
                            System.out.println("table data------222222222----" + rfi_coverage_var);
                        }
                    }

                }
                if (coverageNamesList.size() == 0) {


                    if (isCoverageSpinner) {
                        isCoverageSpinner = false;
                        isCoverageTextViewNew = true;
                        System.out.println("In group sequence = 1 block");
                        View change = (View) findViewById(R.id.coverage_id_spinner);
                        ViewGroup parent = (ViewGroup) change.getParent();
                        int index = parent.indexOfChild(change);
                        parent.removeView(change);
                        LayoutInflater inflater = getLayoutInflater();
                        change = inflater.inflate(
                                R.layout.select_question_coverage_textview,
                                parent, false);
                        parent.addView(change, index);
                        coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
                        coverageedit.setEnabled(false);
                        coverageedit.setFocusable(false);
                        noRfi = 1;
                        coverageedit.setFilters(new InputFilter[]{filter});
                    } else {
                        if (isCoverageTextViewNew) {

                            coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
                            isCoverageSpinner = false;
                        } else {

                            coverageedit = (EditText) findViewById(R.id.coverage_id);
                            isCoverageSpinner = false;
                        }
                    }


                } else {

                    if (coverageNamesList.size() == 0) {
                        coverageedit = (EditText) findViewById(R.id.coverage_id);
                        isCoverageSpinner = false;
                    } else {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, coverageNamesList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        coverageSpinner.setAdapter(adapter);

                        coverageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                /*if (i > 0) {
                                    db.selectedCoverage = coverageNamesList.get(i);
                                    System.out.println("Selected coverage  is : "
                                            + db.selectedCoverage);
                            }else {
                                    db.selectedCoverage= coverageNamesList.get(1);
                                }*/
                                db.selectedCoverage = coverageNamesList.get(i);
                                System.out.println("Selected coverage  is : "
                                        + db.selectedCoverage);

                                method = "getBitForPreviousGroup";
                                param = new String[]{"groupId", "nodeID", "chkId",
                                        "coverage"};
                                value = new String[]{db.selectedGroupId, db.selectedNodeId,
                                        db.selectedChecklistId, db.selectedCoverage};

                                callPreviousPendingBit();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callPreviousPendingBit() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);

        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                String[] res = data.split("\\$");

                try {
                    JSONArray groupbit = new JSONArray(res[0]);
                    System.out.println("table data----------" + groupbit);
                    for (int i = 0; i < groupbit.length(); i++) {
                        JSONObject stageObject = groupbit.getJSONObject(i);

                        bit = stageObject.getString("count");
                        System.out.println("Pending RFI bit" + bit);

                    }
                    if (Integer.parseInt(bit) >= 1) {
                        displayDialog("Error", "Previous Group is Pending..");
                        okBtn.setVisibility(View.GONE);
                    } else {
                        //callCoverageService();
                        okBtn.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                    e.printStackTrace();
                    flushData();
                    displayDialog("No Record Found",
                            "Sufficient Data is not available.");
                }
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


/*	public void setCoverageSpinnerData(String data) {
		if (data.equalsIgnoreCase("$")) {
			System.out.println("No Data");
		} else {

			try {

				String[] tabledata = data.split("\\$");

				ArrayList<Coverage> coverageList = new ArrayList<Coverage>();

				ArrayList<String> coverageNamesList = new ArrayList<String>();

				JSONArray coverageArray = new JSONArray(tabledata[0]);
				System.out.println("table data----------"+coverageArray);
				for (int i = 0; i < coverageArray.length(); i++) {

					Coverage coverage = new Coverage();
					JSONObject stageObject = coverageArray.getJSONObject(i);

					String rfi_group_id = stageObject.getString("RFI_GRP_Id");
					String rfi_node_id = stageObject.getString("RFI_NODE_Id");
					String rfi_checklist_id = stageObject
							.getString("RFI_CHKL_Id");
					String rfi_coverage_var = stageObject
							.getString("RFI_Coverage_var");

					System.out.println("table data------111111----"+rfi_coverage_var);
				//	if(db.selectedGroupName.equalsIgnoreCase(rfi_group_id)){
				//	if(db.selectedGroupId.equalsIgnoreCase(rfi_group_id)){
						coverage.setRFI_GRP_Id(rfi_group_id);
						coverage.setRFI_NODE_Id(rfi_node_id);
						coverage.setRFI_CHKL_Id(rfi_checklist_id);


							coverage.setRFI_Coverage_var(rfi_coverage_var);

								coverageList.add(coverage);
									coverageNamesList.add(rfi_coverage_var);
									System.out.println("table data------222222222----"+rfi_coverage_var);

				//	}






				}





				if(coverageNamesList.size()==0){


				if (isCoverageSpinner) {
					isCoverageSpinner = false;
					isCoverageTextViewNew = true;
					System.out.println("In group sequence = 1 block");
					View change = (View) findViewById(R.id.coverage_id_spinner);
					ViewGroup parent = (ViewGroup) change.getParent();
					int index = parent.indexOfChild(change);
					parent.removeView(change);
					LayoutInflater inflater = getLayoutInflater();
					change = inflater.inflate(
							R.layout.select_question_coverage_textview,
							parent, false);
					parent.addView(change, index);
					coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
					coverageedit.setFilters(new InputFilter[] { filter });
				} else {
					if (isCoverageTextViewNew) {
						coverageedit = (EditText) findViewById(R.id.coverage_id_edittext);
						isCoverageSpinner = false;
					} else {
						coverageedit = (EditText) findViewById(R.id.coverage_id);
						isCoverageSpinner = false;
					}
				}






				}else{
















				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, coverageNamesList);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				coverageSpinner.setAdapter(adapter);



				}


			} catch (Exception e) {
				Log.d(TAG, e.toString());
				e.printStackTrace();
				flushData();
				displayDialog("No Record Found",
						"Sufficient Data is not available.");
			}
		}
	}
*/

    private String blockCharacterSet = "\'";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    public void callGroupService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveGroupData(data);
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

    public void saveGroupData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {
            try {
                String[] tabledata = data.split("\\$");
                groupList = new ArrayList<Group>();
                JSONArray GroupArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < GroupArray.length(); i++) {
                    Group group = new Group();
                    JSONObject stageObject = GroupArray.getJSONObject(i);
                    String group_id = stageObject.getString("GRP_Id");
                    group.setGroupID(group_id);
                    String group_name = stageObject.getString("GRP_Name_var");
                    group.setGroupName(group_name);
                    String node_id = stageObject.getString("NDUSER_NODE_Id");
                    group.setNodeID(node_id);
                    String checklist_id = stageObject
                            .getString("NDUSER_CHKL_Id");
                    group.setCheckListID(checklist_id);
                    String group_sequence_tint = stageObject
                            .getString("GRP_Sequence_tint");
                    group.setGroupSequenceInt(group_sequence_tint);
                    String column = "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID,user_id,GRP_Sequence_tint";
                    String values = "'" + group_id + "','" + group_name + "','"
                            + node_id + "','" + checklist_id + "','"
                            + db.userId + "','" + group_sequence_tint + "'";
                    // String values = "'" + group_id + "','" + group_name +
                    // "','" + node_id + "','" + checklist_id + "','" +
                    // db.userId + "','" + 3 + "'";
                    db.insert("Group1", column, values);
                    groupList.add(group);
                }
                grouptspin.setClickable(true);
                grouptspin.setSelection(0);
                grouptspin.setVisibility(View.VISIBLE);
                setGoupSpinnerData(db.selectedChecklistId);

            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void callQuestionService() {
        Webservice service = new Webservice(SelectQuestion.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)
                responseData = data;
                System.out.println("success data");
                saveQuestionData(data);
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

    public void saveQuestionData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {

                String[] tabledata = data.split("\\$");

                JSONArray questionArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < questionArray.length(); i++) {
                    JSONObject stageObject = questionArray.getJSONObject(i);
                    String question_id = stageObject.getString("NDCHKL_QUE_Id");
                    String question_description = stageObject
                            .getString("QUE_Description_var");
                    String question_sequence = stageObject
                            .getString("QUE_SequenceNo_int");
                    String question_type = stageObject
                            .getString("QUE_Type_var");
                    String node_id = stageObject.getString("NDCHKL_NODE_Id");
                    String checklist_id = stageObject
                            .getString("NDCHKL_CHKL_Id");
                    String group_id = stageObject.getString("NDCHKL_GRP_Id");
                    String column = "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type,NODE_Id,Fk_CHKL_Id,Fk_Grp_ID,user_id";
                    String values = "'" + question_id + "','"
                            + question_description + "','" + question_sequence
                            + "','" + question_type + "','" + node_id + "','"
                            + checklist_id + "','" + group_id + "','"
                            + db.userId + "'";
                    db.insert("question", column, values);
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }


    public void saveData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                JSONArray clientArray = new JSONArray(tabledata[0]);
                for (int i = 0; i < clientArray.length(); i++) {
                    JSONObject clientObject = clientArray.getJSONObject(i);
                    String client_id = clientObject.getString("CL_Id");
                    String client_name = clientObject.getString("CL_Name_var");
                    String client_display_name = clientObject
                            .getString("CL_DisplayName_var");
                    String client_address = clientObject
                            .getString("CL_Address_var");
                    String column = "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id";
                    String values = "'" + client_id + "','" + client_name
                            + "','" + client_display_name + "','"
                            + client_address + "','" + db.userId + "'";
                    db.insert("Client", column, values);
                    System.out.println("Row inserted in Client Table");
                }

                JSONArray projectArray = new JSONArray(tabledata[1]);
                for (int i = 0; i < projectArray.length(); i++) {
                    JSONObject projectObject = projectArray.getJSONObject(i);
                    String project_id = projectObject.getString("PRJ_Id");
                    String project_name = projectObject
                            .getString("PRJ_Name_var");
                    String project_client_id = projectObject
                            .getString("PRJ_CL_Id");
                    String project_display_name = projectObject
                            .getString("PRJ_DisplayName_var");
                    String project_address = projectObject
                            .getString("PRJ_Address_var");
                    String project_region = projectObject
                            .getString("PRJ_Region_var");
                    String project_scrolling_status = projectObject
                            .getString("PRJ_ScrollingUIStatus_bit");
                    String column = "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id";
                    String values = "'" + project_id + "','" + project_name
                            + "','" + project_client_id + "','"
                            + project_display_name + "','" + project_address
                            + "','" + project_region + "','"
                            + project_scrolling_status + "','" + db.userId
                            + "'";
                    db.insert("Scheme", column, values);
                    System.out.println("Row inserted in Project Table");
                }
                JSONArray workTypeArray = new JSONArray(tabledata[2]);
                for (int i = 0; i < workTypeArray.length(); i++) {
                    JSONObject workTypeObject = workTypeArray.getJSONObject(i);
                    String worktype_id = workTypeObject.getString("WT_Id");
                    String worktype_name = workTypeObject
                            .getString("WT_Name_var");
                    String worktype_level_tint = workTypeObject
                            .getString("WT_Level_tint");
                    String worktype_project_id = workTypeObject
                            .getString("PRJ_Id");
                    String column = "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id";
                    String values = "'" + worktype_id + "','" + worktype_name
                            + "','" + worktype_level_tint + "','"
                            + worktype_project_id + "','" + db.userId + "'";
                    db.insert("WorkType", column, values);
                    System.out.println("Row inserted in WorkType Table");
                }

                /***** AKSHAY *****/
                /*
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4);
                 */

                /***** AKSHAY *****/
                /*
                 * column =
                 * "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id"
                 * ; saveToDatabase("Scheme", column, tabledata[1],true,7);
                 * System
                 * .out.println("table 1========"+tabledata[1].toString());
                 *
                 * column =
                 * "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id"
                 * ;//worktype saveToDatabase("WorkType", column,
                 * tabledata[2],true,4);
                 *
                 *
                 *
                 * column =
                 * "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Building", column,
                 * tabledata[3],true,4);
                 *
                 *
                 * column =
                 * "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("floor", column,
                 * tabledata[4],true,5);
                 *
                 *
                 *
                 *
                 *
                 * //Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                 * TEXT,Fk_Floor_ID TEXT, user_id TEXT)") column =
                 * "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Unit", column,
                 * tabledata[5],true,5);
                 *
                 *
                 *
                 * column =
                 * "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubUnit", column,
                 * tabledata[6],true,5);
                 *
                 *
                 * column =
                 * "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("Element", column,
                 * tabledata[7],true,5);
                 *
                 *
                 * column =
                 * "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubElement", column,
                 * tabledata[8],true,5);
                 *
                 *
                 *
                 *
                 *
                 * column =
                 * "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("CheckList", column,
                 * tabledata[9],true,4);
                 *
                 * column =
                 * "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";//buildng
                 * saveToDatabase("Group1", column, tabledata[10],true,4);
                 *
                 *
                 *
                 * column =
                 * "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id"
                 * ;//buildng saveToDatabase("question", column,
                 * tabledata[11],true,7);
                 */

                /***** AKSHAY *****/

                /***** AKSHAY *****/
                // Change Made on 24-April-2015
                /* System.out.println("client data=========="+column.length()); */

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    //changed by sayali
    ////27-02-2024
    public void saveCoverageData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            System.out.println("No Data");
        } else {

            try {
                SharedPreferences sharedPreferences = getSharedPreferences("coverageData", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("data_key", data);
                editor.apply();

                // Update the spinner with the new data
                setMultipleCoverageDataToSpinner(data);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }
    String strCoverage="";
    private void setMultipleCoverageDataToSpinner(String data) {
        String[] coverageData = data.split("~");

        // ArrayList<String> myCoverageData = (ArrayList<String>) Arrays.asList(coverageData);
        strCoverage="";
        ArrayList<String> myCoverageData = new ArrayList<String>();
        myCoverageData.addAll(Arrays.asList(coverageData));
        for (int count=0;count<coverageData.length;count++){
            strCoverage+=(count+1)+") "+myCoverageData.get(count)+"\n";
        }


      //  myCoverageData.add(0, "Coverage");


        viewCoverageImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coverageData.length>0){
                    displayCoverageDialog("Previous Coverage",strCoverage);
                }
            }
        });



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, myCoverageData);
        /*adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coveragespin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        coveragespin.setSelection(0);
        coveragespin.setClickable(true);

        coveragespin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //  db.selectedCoverage = myCoverageData.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


    }

    public void saveDataScheme(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                /*
                 * AKSHAY
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4); AKSHAY
                 */

                String column = "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id";
                saveToDatabase("Scheme", column, tabledata[1], true, 7);
                System.out.println("table 1========" + tabledata[1].toString());

                /***** AKSHAY *****/

                /*
                 * column =
                 * "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id"
                 * ;//worktype saveToDatabase("WorkType", column,
                 * tabledata[2],true,4);
                 *
                 *
                 *
                 * column =
                 * "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Building", column,
                 * tabledata[3],true,4);
                 *
                 *
                 * column =
                 * "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("floor", column,
                 * tabledata[4],true,5);
                 *
                 *
                 *
                 *
                 *
                 * //Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                 * TEXT,Fk_Floor_ID TEXT, user_id TEXT)") column =
                 * "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Unit", column,
                 * tabledata[5],true,5);
                 *
                 *
                 *
                 * column =
                 * "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubUnit", column,
                 * tabledata[6],true,5);
                 *
                 *
                 * column =
                 * "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("Element", column,
                 * tabledata[7],true,5);
                 *
                 *
                 * column =
                 * "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubElement", column,
                 * tabledata[8],true,5);
                 *
                 *
                 *
                 *
                 *
                 * column =
                 * "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("CheckList", column,
                 * tabledata[9],true,4);
                 *
                 * column =
                 * "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";//buildng
                 * saveToDatabase("Group1", column, tabledata[10],true,4);
                 *
                 *
                 *
                 * column =
                 * "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id"
                 * ;//buildng saveToDatabase("question", column,
                 * tabledata[11],true,7);
                 *
                 *
                 * System.out.println("client data=========="+column.length());
                 *//***** AKSHAY *****/

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveDataWorkType(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                /*
                 * AKSHAY
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4);
                 *
                 *
                 * String column =
                 * "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id"
                 * ; saveToDatabase("Scheme", column, tabledata[1],true,7);
                 * System
                 * .out.println("table 1========"+tabledata[1].toString());
                 * AKSHAY
                 */

                String column = "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id";// worktype
                saveToDatabase("WorkType", column, tabledata[2], true, 4);

                /***** AKSHAY *****/

                /*
                 * column =
                 * "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Building", column,
                 * tabledata[3],true,4);
                 *
                 *
                 * column =
                 * "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("floor", column,
                 * tabledata[4],true,5);
                 *
                 *
                 *
                 *
                 *
                 * //Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                 * TEXT,Fk_Floor_ID TEXT, user_id TEXT)") column =
                 * "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("Unit", column,
                 * tabledata[5],true,5);
                 *
                 *
                 *
                 * column =
                 * "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubUnit", column,
                 * tabledata[6],true,5);
                 *
                 *
                 * column =
                 * "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id"
                 * ;//buildng saveToDatabase("Element", column,
                 * tabledata[7],true,5);
                 *
                 *
                 * column =
                 * "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("SubElement", column,
                 * tabledata[8],true,5);
                 *
                 *
                 *
                 *
                 *
                 * column =
                 * "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id"
                 * ;//buildng saveToDatabase("CheckList", column,
                 * tabledata[9],true,4);
                 *
                 * column =
                 * "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";//buildng
                 * saveToDatabase("Group1", column, tabledata[10],true,4);
                 *
                 *
                 *
                 * column =
                 * "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id"
                 * ;//buildng saveToDatabase("question", column,
                 * tabledata[11],true,7);
                 *
                 *
                 * System.out.println("client data=========="+column.length());
                 *//***** AKSHAY *****/

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveDataCheckList(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                /*
                 * AKSHAY
                 * System.out.println("data========="+tabledata.toString());
                 *
                 * String column =
                 * "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id"
                 * ;//project saveToDatabase("Client", column,
                 * tabledata[0],true,4);
                 *
                 *
                 * String column =
                 * "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status,user_id"
                 * ; saveToDatabase("Scheme", column, tabledata[1],true,7);
                 * System
                 * .out.println("table 1========"+tabledata[1].toString());
                 *
                 *
                 *
                 * String column =
                 * "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id"
                 * ;//worktype saveToDatabase("WorkType", column,
                 * tabledata[2],true,4); AKSHAY
                 */

                String column = "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("Building", column, tabledata[3], true, 4);

                column = "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID,FK_WorkTyp_ID, user_id";// buildng
                saveToDatabase("floor", column, tabledata[4], true, 5);

                // Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                // TEXT,Fk_Floor_ID TEXT, user_id TEXT)")
                column = "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("Unit", column, tabledata[5], true, 5);

                column = "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("SubUnit", column, tabledata[6], true, 5);

                column = "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID,FK_WorkTyp_ID, user_id";// buildng
                saveToDatabase("Element", column, tabledata[7], true, 5);

                column = "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("SubElement", column, tabledata[8], true, 5);

                column = "Checklist_ID,Checklist_Name,Node_Id,FK_WorkTyp_ID,user_id";// buildng
                saveToDatabase("CheckList", column, tabledata[9], true, 4);

                column = "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";// buildng
                saveToDatabase("Group1", column, tabledata[10], true, 4);

                column = "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id";// buildng
                saveToDatabase("question", column, tabledata[11], true, 7);

                System.out.println("client data==========" + column.length());

                /*
                 * column = "q_type_id,q_type_text,q_type_desc, user_id";
                 * saveToDatabase("question_type", column, tabledata[7],true,4);
                 *
                 * column =
                 * "severity_id,mild,moderate,severe,very_severe,exstream, user_id"
                 * ; saveToDatabase("severity", column, tabledata[8],true,7);
                 */

                /*
                 * String tempdata="0~Other"; column =
                 * "q_heading_id,q_heading_text, user_id";
                 * saveToDatabase("question_heading", column, tempdata,true,3);
                 */

                // setSchemeSpinnerData();

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficient Data is not available.");
            }
        }
    }

    public void saveToDatabase(String table, String columns, String respose,
                               boolean adduserId, int colCnt) {
        Cursor cursor = db.select(table, columns,
                "user_id='" + db.userId + "'", null, null, null, null);
        String existingData = "";
        if (cursor.moveToFirst()) {
            for (int i = 0; i < colCnt; i++) {
                existingData += "'" + cursor.getString(i) + "',";
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        int a = 0;
        String[] rowdata = respose.split("\\|");

        System.out.println("size of the table=" + rowdata.length);
        for (int len = 0; len < rowdata.length; len++) {
            String[] singlerow = rowdata[len].split("~");
            StringBuffer values = new StringBuffer("'");
            for (int i = 0; i < (singlerow.length - 1); i++) {
                /*
                 * if(singlerow[i].contains("'"))
                 * singlerow[i].replaceAll("'"," ");
                 */
                System.out.println("apostropeeeee----------------"
                        + singlerow[i]);
                values.append(singlerow[i] + "','");
                a++;
            }
            values.append(singlerow[singlerow.length - 1] + "'");
            if (adduserId)
                values.append(",'" + db.userId + "'");

            if (a > 1) {
                if (!existingData.contains(values.toString()))
                    db.insert(table, columns, values.toString());
                System.out.println("colum=" + table + "table value==="
                        + values.toString());
            }
        }

    }

    private boolean isDataAvialable(String table) {

        Cursor cursor = db.select(table, "count(*) as noitem", "user_id='"
                + db.userId + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d(table, cursor.getString(0));
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


    private boolean isDataAvialableAllocateTask(String table) {

        Cursor cursor = db.select(table, "count(*) as noitem", null, null, null, null, null);
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d(table, cursor.getString(0));
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


    private boolean isDataAvialableForID(String table, String targetColumn,
                                         String targetValue) {

        Cursor cursor = db.select(table, "count(*) as noitem", "user_id='"
                + db.userId + "' AND " + targetColumn + " ='" + targetValue
                + "'", null, null, null, null);
        System.out.println("user_id='" + db.userId + "' AND " + targetColumn
                + " = '" + targetValue + "'");
        if (cursor.moveToFirst()) {
            if (Integer.parseInt(cursor.getString(0)) > 0) {
                Log.d(table, cursor.getString(0));
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
                            // retry();
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

    private void flushData() {
        db.delete("CheckList", "user_id='" + db.userId + "'");
        db.delete("Scheme", "user_id='" + db.userId + "'");
        db.delete("Building", "user_id='" + db.userId + "'");
        /*
         * db.delete("subgroup", "user_id='" + db.userId + "'");
         * db.delete("answare", "user_id='" + db.userId + "'");
         * db.delete("floor", "user_id='" + db.userId + "'");
         * db.delete("question_heading", "user_id='" + db.userId + "'");
         * db.delete("question_group", "user_id='" + db.userId + "'");
         * db.delete("question", "user_id='" + db.userId + "'");
         * db.delete("userMaster", "Pk_User_ID='" + db.userId + "'");
         * db.delete("card_details","user_id='" + db.userId + "'");
         * db.delete("alert_details", "user_id='" + db.userId + "'");
         * db.delete("scheme_foreman", "user_id='" + db.userId + "'");
         * db.delete("scheme_superviser", "user_id='" + db.userId + "'");
         * db.delete("mockuptable", "user_id='" + db.userId + "'");
         * db.delete("inspection_log", "user_id='" + db.userId + "'");
         * db.delete("trade_match", "user_id='" + db.userId + "'");
         * db.delete("contractor_sf", "user_id='" + db.userId + "'");
         */
        // db.update("sqlite_sequence", "seq=0", "name = 'question'");

        System.out
                .println("data flush+++++++++++++++++++++++++++++++++++++++++++++++++");
        // deleteImages();
    }

    public boolean isQuestionAvailable() {
        String whereClause = "Fk_CHKL_Id='" + db.selectedChecklistId
                + "' AND Fk_Grp_ID='" + db.selectedGroupId + "' AND NODE_Id='"
                + db.selectedBuildingId + "' AND user_id='" + db.userId + "'";

        System.out.println("check avaible q===" + whereClause);
        Cursor cursor = db.select("question",
                "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type", whereClause,
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

    public void ResetId() {
        db.selectedClientId = "";
        db.selectedSchemeId = "";
        db.selectedWorkTypeId = "";
        db.selectedBuildingId = "";
        db.selectedFloorId = "";
        db.selectedUnitId = "";
        db.selectedSubUnitId = "";
        db.selectedElementId = "";
        db.selectedSubElementId = "";
        db.selectedChecklistId = "";
        db.selectedGroupId = "";
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


     void displayCoverageDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);


        alertDialog.setButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        /*alertDialog.setButton2("Try again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // retry();
                    }
                });*/

        alertDialog.show();
    }

}
