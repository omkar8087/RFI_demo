package com.ob.rfi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.multispinner.MultiSelectSpinner;
import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;
import com.ob.rfi.service.Webservice.downloadListener;

import java.util.List;

public class CheckFRI extends CustomTitle implements MultiSelectSpinner.OnMultipleItemsSelectedListener {
    public static CustomTitle Checkclass;
    private Spinner schemeSpin;
    private Spinner buildingSpin;
    private Spinner checkLististSpin;
    private Spinner subgrpSpin;
    private Spinner tradeSpin;
    private Spinner floorSpin;
    private Spinner supervisorspin;
    private Spinner foremanspin;
    private Spinner contractorspin;
    private EditText remarkText;

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
    private Spinner checklistspin;
    private Spinner grouptspin;
    private Button cancelButton;
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
    private String client_id;
    private String[] r_prj_id;
    private String[] r_clint_id;
    private String[] r_struct_id;
    private String[] r_stage_id;
    private String[] r_grp_id;
    private String[] r_checklist_id;
    private String[] r_subelmt_id;
    private String[] r_elemnt_id;
    private String[] r_subunit_id;
    private String[] r_unit_id;
    private String[] r_node_id;
    private String[] r_que_id;

    private String Q_id;
    private String[] r_level_id;
    private EditText coverageedit;
    private String[] r_coverage_id;
    private Spinner updateworktypeSpin;
    private String[] r_worktype_id;
    private String[] wTypeId;
    private EditText drawingeedit;
    private String[] scroll_status;
    private String[] r_drawing_id;
    private String remark;
    // private String rf_client_id;
    /*
     * private String rf_struct_id; private String rf_stage_id; private String
     * rf_unit_id; private String rf_subunit_id; private String rf_element_id;
     * private String rf_subelmt_id; private String rf_checklist_id; private
     * String rf_grp_id; private String rf_node_id; private String
     * rf_project_id;
     */
    public static CustomTitle destructcheckRfi;
    public static boolean insertFlagcheckRFi = false;

    public static final String TAG = "UpdateScreen";
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
    private List<String> multipleSelectedList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.check_rfi);
        // title.setText("CQRA Question selection");

        destructcheckRfi = this;
        Checkclass = this;
        insertFlagcheckRFi = false;
        rfiSpin = (Spinner) findViewById(R.id.rfi_id);
        clintSpin = (Spinner) findViewById(R.id.rfi_client_id);
        projSpin = (Spinner) findViewById(R.id.rfi_project_id);
        updateworktypeSpin = (Spinner) findViewById(R.id.check_worktype_id);

        structureSpin = (Spinner) findViewById(R.id.rfi_structure_id);
        stageSpin = (Spinner) findViewById(R.id.rfi_statge_id);
        unitSpin = (Spinner) findViewById(R.id.rfi_unit_id);
        subunitspin = (Spinner) findViewById(R.id.rfi_sub_unit_id);
        elementspin = (Spinner) findViewById(R.id.rfi_element_id);
        subelementspin = (Spinner) findViewById(R.id.rfi_sub_element_id);
        checklistspin = (Spinner) findViewById(R.id.rfi_checklist_id);
        grouptspin = (Spinner) findViewById(R.id.rfi_group_id);
        coverageedit = (EditText) findViewById(R.id.coverage_id);
        drawingeedit = (EditText) findViewById(R.id.drawing_edit_id);


        unitSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_unit_id_multi);

        subUnitSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_sub_unit_id_multi);
        elementSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_element_id_multi);
        subElementSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_sub_element_id_multi);
        groupSpinMulti = (MultiSelectSpinner) findViewById(R.id.rfi_group_id_multi);


        // setSpiner();

        ResetId();


       /* int a=0;
        db.questionSubmitCount= String.valueOf(a);
        System.out.println("Before submit clicked--- "+db.questionSubmitCount);*/

        okBtn = (Button) findViewById(R.id.question_select_submit);
        backBtn = (Button) findViewById(R.id.question_select_Back);
        cancelButton = (Button) findViewById(R.id.cancel_rfi_button);

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

                if (validateScreen()) {
                    displayErrorDialog("Error", errorMessage);
                } else if (isQuestionAvailable()) {

                    if (db.selectedScrollStatus.equalsIgnoreCase("true")) {
                        Intent int1 = new Intent(CheckFRI.this,
                                CheckRfiScroll.class);
                        int1.putExtra("coverage", coverageedit.getText()
                                .toString());
                        int1.putExtra("Q_id", Q_id);
                        int1.putExtra("drawingId", drawingeedit.getText()
                                .toString());

                        checkPreferences = getSharedPreferences("RFI_File",
                                MODE_PRIVATE);
                        editor = checkPreferences.edit();
                        editor.putString("coverage", coverageedit.getText()
                                .toString());
                        editor.putString("drawing", drawingeedit.getText()
                                .toString());
                        editor.commit();

                        startActivity(int1);

                    } else {
                        Intent int1 = new Intent(CheckFRI.this,
                                CheckRfiScroll.class);
                        int1.putExtra("coverage", coverageedit.getText()
                                .toString());
                        int1.putExtra("Q_id", Q_id);
                        int1.putExtra("drawingId", drawingeedit.getText()
                                .toString());

                        checkPreferences = getSharedPreferences("RFI_File",
                                MODE_PRIVATE);
                        editor = checkPreferences.edit();
                        editor.putString("coverage", coverageedit.getText()
                                .toString());
                        editor.putString("drawing", drawingeedit.getText()
                                .toString());
                        editor.commit();

                        startActivity(int1);

                        /*
                         * Intent int1=new Intent(CheckFRI.this,
                         * CheckQuestionire.class);
                         * int1.putExtra("coverage",coverageedit
                         * .getText().toString()); startActivity(int1);
                         */
                    }

                } else {
                    displayErrorDialog("", "Please Select Another Combination!");
                }
                /* } */
            }
        });

        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        CheckFRI.this, R.style.MyAlertDialogStyle);
                alert.setTitle("Enter A Remark");
                LayoutInflater inflater = getLayoutInflater();
                View toBeAdded = inflater.inflate(R.layout.remark, null);
                alert.setView(toBeAdded);
                remarkText = (EditText) toBeAdded.findViewById(R.id.txtRemark);
                alert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                /*remark = remarkText.getText().toString();
                                if (!remark.trim().isEmpty()) {
                                    if (!db.selectedrfiId.equalsIgnoreCase("")) {
                                        System.out.println("Remark : " + remark);
                                        if (remark.equalsIgnoreCase("")) {
                                            remark = "No Remark";
                                        }
                                        String columns = "RFI_ID, Remark, User_ID";
                                        String values = "'" + db.selectedrfiId
                                                + "','" + remark + "','" + db.userId + "'";
                                        db.insert("CancelRFI", columns, values);
                                    } else {
                                        Toast.makeText(CheckFRI.this,
                                                "Select a RFI!",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    finish();
                                    db.closeDb();
                                    startActivity(new Intent(CheckFRI.this, HomeScreen.class));
                                }else {
                                    Toast.makeText(CheckFRI.this,
                                            "Please Enter Remark",
                                            Toast.LENGTH_SHORT).show();
                                }*/

                            }
                        });
                alert.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                System.out.println("Cancel clicked");
                            }
                        });
               /* alert.create();
                alert.show();*/

                final AlertDialog dialog = alert.create();
                dialog.show();


                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        remark = remarkText.getText().toString();
                        if (!remark.trim().isEmpty()) {
                            if (!db.selectedrfiId.equalsIgnoreCase("")) {
                                System.out.println("Remark : " + remark);
                                if (remark.equalsIgnoreCase("")) {
                                    remark = "No Remark";
                                }
                                String columns = "RFI_ID, Remark, User_ID";
                                String values = "'" + db.selectedrfiId
                                        + "','" + remark + "','" + db.userId + "'";
                                db.insert("CancelRFI", columns, values);
                            } else {
                                Toast.makeText(CheckFRI.this,
                                        "Select a RFI!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            finish();
                            db.closeDb();
                            startActivity(new Intent(CheckFRI.this, HomeScreen.class));
                            dialog.dismiss();
                        }else {
                            Toast.makeText(CheckFRI.this,
                                    "Please Enter Remark",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        /*
         * if(!db.userId.equalsIgnoreCase("")) {
         * if(isDataAvialable("checklist")){ setRFIData(); //setClientData();
         * }else { updateData(); } //disableList(false); } else { logout(); }
         */

        if (!db.userId.equalsIgnoreCase("")) {
            if (isDataRfiAvialable("Check_update_details")) {

                if (isDataAvialable("checklist")) {
                    setRFIData();
                }

            } else {
                getRFIDetails();
            }

        } else {
//            logout();
        }

    }

    public void getRFIDetails() {
        // requestid = 1;
        // method = "getListForModify";
        method = "getListForCheck";
        param = new String[]{"userID"};
        value = new String[]{db.userId};
        callService1();
    }

    public void updateData() {
        // requestid = 1;
        method = "getDetailsForCheckRFI";
        param = new String[]{"userID", "userRole"};
        value = new String[]{db.userId, "checker"};
        callService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // setSpiner();
        if (db.userId.equalsIgnoreCase("")) {
            //logout();
        }
    }

    private void logout() {
        finish();
        Toast.makeText(CheckFRI.this, "Session expired... Please login.",
                Toast.LENGTH_SHORT).show();
        Intent logout = new Intent(CheckFRI.this, LoginScreen.class);
        logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        db.copyDatabase(getApplicationContext(), "RFI.db");
        startActivity(logout);
    }

    public void setSpiner() {
        /* if (db.setSpinner) { */

        // rfiSpin.setSelection(0);
        clintSpin.setClickable(false);
        projSpin.setClickable(false);
        structureSpin.setClickable(false);
        stageSpin.setClickable(false);
        unitSpin.setClickable(false);
        subunitspin.setClickable(false);
        elementspin.setClickable(false);
        subelementspin.setClickable(false);
        checklistspin.setClickable(false);
        grouptspin.setClickable(false);
        db.setSpinner = false;
        System.out.println("setspinner");

        /* } */

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

    /*
     * protected boolean validateScreen() { boolean validate = true; if
     * (schemeSpin.getSelectedItemPosition() == 0) { errorMessage =
     * "Please Select Project"; } else if
     * (buildingSpin.getSelectedItemPosition() == 0) { errorMessage =
     * "Please Select Structure"; } else if (floorSpin.getSelectedItemPosition()
     * == 0) { errorMessage = "Please Select Stage"; } else if
     * (tradeSpin.getSelectedItemPosition() == 0) { errorMessage =
     * "Please Select Trade"; } else if
     * (checkLististSpin.getSelectedItemPosition() == 0) { errorMessage =
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
     * ((db.supervisor_flag) && foremanspin.getSelectedItemPosition() == 0) {
     * errorMessage = "Please Select Foreman"; }else if
     * (supervisorspin.getSelectedItemPosition() == 0 && db.foreman_flag
     * foremanspin.getSelectedItemPosition() == 0) { errorMessage =
     * "Please Select Supervisor"; }else if
     * (clientspin.getSelectedItemPosition() == 0 ) { errorMessage =
     * "Please Select client"; }
     *
     *
     *
     *
     *
     * else if ((!db.supervisor_flag) && db.foreman_flag) { errorMessage =
     * "Please Select Supervisor or Allocate Foreman"; }else validate = false;
     * db.contr= false; db.supervisor_flag= false; db.foreman_flag= false;
     * db.contractor_flag= false; //
     * System.out.println("schem="+db.selectedSchemeId); //
     * System.out.println("building="+db.selectedBuildingId); //
     * System.out.println("checklist="+db.selectedChecklistId); //
     * System.out.println("subroup="+db.selectedGroupId);
     *
     * return validate; }
     */

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

        // Rfi_update_details(RFI_Id TEXT

        String where = " status_device='" + "incomplete" + "' AND user_id1='"
                + db.userId + "'";

        // String
        // where1="RFI_Id,CL_Id,PRJ_Id,NODE_Id,Level_int,CHKL_Id,GRP_Id,USER_Id,StructureId,StageId,UnitId,SubUnitId,ElementId,SubElementId";

        Cursor cursor = db
                .select("Check_update_details",
                        "distinct(RFI_Id),CL_Id,PRJ_Id,NODE_Id,Level_int,CHKL_Id,GRP_Id,USER_Id,StructureId,StageId,UnitId,SubUnitId,ElementId,SubElementId,QUE_Id,Coverage,Rfi_name,Fk_WorkTyp_ID,drawing_no",
                        where, null, "RFI_Id", null, null);
        System.out.println("user iddddddddd==" + db.userId);
        // schemId = new String[cursor.getCount()];

        RfiId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";

        /*
         * RfiId= new String[cursor.getCount()+1];
         *
         * String[] items = new String[cursor.getCount() + 1]; RfiId[0] =
         * "--select--";
         */
        r_prj_id = new String[cursor.getCount()];
        r_clint_id = new String[cursor.getCount()];
        r_struct_id = new String[cursor.getCount()];
        r_stage_id = new String[cursor.getCount()];
        r_unit_id = new String[cursor.getCount()];
        r_subunit_id = new String[cursor.getCount()];
        r_elemnt_id = new String[cursor.getCount()];
        r_subelmt_id = new String[cursor.getCount()];
        r_checklist_id = new String[cursor.getCount()];
        r_grp_id = new String[cursor.getCount()];
        r_node_id = new String[cursor.getCount()];
        r_que_id = new String[cursor.getCount()];
        r_level_id = new String[cursor.getCount()];
        r_coverage_id = new String[cursor.getCount()];
        r_worktype_id = new String[cursor.getCount()];
        r_drawing_id = new String[cursor.getCount()];
        int count = 0;
        if (cursor.moveToFirst()) {

            do {
                count++;
                // items[1]= cursor.getString(0);
                RfiId[cursor.getPosition()] = cursor.getString(0);
                items[cursor.getPosition() + 1] = cursor.getString(16);
                r_prj_id[cursor.getPosition()] = cursor.getString(2);
                r_clint_id[cursor.getPosition()] = cursor.getString(1);
                r_struct_id[cursor.getPosition()] = cursor.getString(8);
                r_stage_id[cursor.getPosition()] = cursor.getString(9);
                r_unit_id[cursor.getPosition()] = cursor.getString(10);
                r_subunit_id[cursor.getPosition()] = cursor.getString(11);
                r_elemnt_id[cursor.getPosition()] = cursor.getString(12);
                r_subelmt_id[cursor.getPosition()] = cursor.getString(13);
                r_checklist_id[cursor.getPosition()] = cursor.getString(5);
                r_grp_id[cursor.getPosition()] = cursor.getString(6);
                System.out.println("Group ID : "
                        + r_grp_id[cursor.getPosition()]);
                r_node_id[cursor.getPosition()] = cursor.getString(3);
                r_que_id[cursor.getPosition()] = cursor.getString(14);
                r_level_id[cursor.getPosition()] = cursor.getString(4);
                r_coverage_id[cursor.getPosition()] = cursor.getString(15);
                r_worktype_id[cursor.getPosition()] = cursor.getString(17);
                r_drawing_id[cursor.getPosition()] = cursor.getString(18);
                // items[cursor.getPosition() + 1] = cursor.getString(1);
            } while (cursor.moveToNext());
        } else {
            items[0] = "RFI(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rfiSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        rfiSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    setSpinerselection();
                    setClientData(r_clint_id[position - 1]);
                    db.selectedrfiId = RfiId[position - 1];
                    db.selectedlevelId = r_level_id[position - 1];
                    db.selectedRfiName = rfiSpin.getSelectedItem().toString();
                    coverageedit.setText("");
                    drawingeedit.setText("");
                    System.out.println("selected RFI id" + RfiId[position - 1]);
                    // rf_client_id=r_clint_id[position-1];
                    /*
                     * rf_project_id=r_prj_id[position-1];
                     * System.out.println("iddddddddd====="
                     * +r_clint_id[position-1]+"=="+r_prj_id[position-1]);
                     * rf_struct_id=r_struct_id[position-1];
                     * rf_stage_id=r_stage_id[position-1];
                     * rf_unit_id=r_unit_id[position-1];
                     * rf_subunit_id=r_subunit_id[position-1];
                     * rf_element_id=r_elemnt_id[position-1];
                     * rf_subelmt_id=r_subelmt_id[position-1];
                     * rf_checklist_id=r_checklist_id[position-1];
                     * rf_grp_id=r_grp_id[position-1];
                     * rf_node_id=r_node_id[position-1];
                     */

                    // -----call all method-------------

                    // ----------checking worktype id floor--------------

                    setWorkTypeSpinnerData(r_worktype_id[position - 1],
                            r_prj_id[position - 1]);

                    final String where111 = "FK_WorkTyp_ID ='"
                            + r_worktype_id[position - 1] + "' AND user_id='"
                            + db.userId + "'";
                    final String table111 = "Building";
                    if (CheckWorkType(table111, where111)
                            && Integer.parseInt(db.selectedlevelId) >= 1) {
                        setBulidngSpinnerData(r_prj_id[position - 1],
                                r_struct_id[position - 1],
                                r_worktype_id[position - 1]);
                    }
                    // ---------end floor----------

                    // ----------checking worktype id floor--------------

                    final String where11 = "FK_WorkTyp_ID ='"
                            + r_worktype_id[position - 1] + "' AND user_id='"
                            + db.userId + "'";
                    final String table11 = "floor";
                    if (CheckWorkType(table11, where11)
                            && Integer.parseInt(db.selectedlevelId) >= 2) {
                        setFloorSpinnerData(r_struct_id[position - 1],
                                r_prj_id[position - 1],
                                r_stage_id[position - 1],
                                r_worktype_id[position - 1]);
                    }
                    // ---------end floor----------

                    // ----------checking worktype id unit--------------

                    final String where22 = "FK_WorkTyp_ID ='"
                            + r_worktype_id[position - 1] + "' AND user_id='"
                            + db.userId + "'";
                    final String table22 = "Unit";
                    if (CheckWorkType(table22, where22)
                            && Integer.parseInt(db.selectedlevelId) >= 3) {
                        // setFloorSpinnerData(r_struct_id[position-1],
                        // r_prj_id[position-1],r_stage_id[position-1],r_worktype_id[position-1]);
                        setUnitSpinnerData(r_stage_id[position - 1],
                                r_prj_id[position - 1],
                                r_unit_id[position - 1],
                                r_worktype_id[position - 1]);
                    }
                    // ---------end unit----------

                    // ----------checking worktype id Subunit--------------

                    final String where33 = "FK_WorkTyp_ID ='"
                            + r_worktype_id[position - 1] + "' AND user_id='"
                            + db.userId + "'";
                    final String table33 = "SubUnit";
                    if (CheckWorkType(table33, where33)
                            && Integer.parseInt(db.selectedlevelId) >= 4) {
                        // setFloorSpinnerData(r_struct_id[position-1],
                        // r_prj_id[position-1],r_stage_id[position-1],r_worktype_id[position-1]);
                        // setUnitSpinnerData(r_stage_id[position-1],r_prj_id[position-1],r_unit_id[position-1],r_worktype_id[position-1]);
                        setSubUnitSpinnerData(r_unit_id[position - 1],
                                r_prj_id[position - 1],
                                r_subunit_id[position - 1],
                                r_worktype_id[position - 1]);
                    }
                    // ---------end subunit----------

                    // ----------checking worktype id Element--------------

                    final String where44 = "FK_WorkTyp_ID ='"
                            + r_worktype_id[position - 1] + "' AND user_id='"
                            + db.userId + "'";
                    final String table44 = "Element";
                    if (CheckWorkType(table44, where44)
                            && Integer.parseInt(db.selectedlevelId) >= 5) {
                        // setFloorSpinnerData(r_struct_id[position-1],
                        // r_prj_id[position-1],r_stage_id[position-1],r_worktype_id[position-1]);
                        // setUnitSpinnerData(r_stage_id[position-1],r_prj_id[position-1],r_unit_id[position-1],r_worktype_id[position-1]);
                        setElementSpinnerData(r_subunit_id[position - 1],
                                r_prj_id[position - 1],
                                r_elemnt_id[position - 1],
                                r_worktype_id[position - 1]);
                    }
                    // ---------end Element----------

                    // ----------checking worktype id Subelement--------------

                    final String where55 = "FK_WorkTyp_ID ='"
                            + r_worktype_id[position - 1] + "' AND user_id='"
                            + db.userId + "'";
                    final String table55 = "SubElement";
                    if (CheckWorkType(table55, where55)
                            && Integer.parseInt(db.selectedlevelId) >= 6) {
                        // setFloorSpinnerData(r_struct_id[position-1],
                        // r_prj_id[position-1],r_stage_id[position-1],r_worktype_id[position-1]);
                        // setUnitSpinnerData(r_stage_id[position-1],r_prj_id[position-1],r_unit_id[position-1],r_worktype_id[position-1]);
                        setSubElementSpinnerData(r_elemnt_id[position - 1],
                                r_prj_id[position - 1],
                                r_subelmt_id[position - 1],
                                r_worktype_id[position - 1]);
                    }
                    // ---------end SubElement----------

                    System.out.println("selected level id==="
                            + r_level_id[position - 1]);

                    setSchemeSpinnerData(r_clint_id[position - 1],
                            r_prj_id[position - 1]);
                    /*
                     * setBulidngSpinnerData(r_prj_id[position-1]
                     * ,r_struct_id[position-1]);
                     * setFloorSpinnerData(r_struct_id[position-1],
                     * r_prj_id[position-1],r_stage_id[position-1]);
                     * setUnitSpinnerData
                     * (r_stage_id[position-1],r_prj_id[position
                     * -1],r_unit_id[position-1]);
                     * setSubUnitSpinnerData(r_unit_id
                     * [position-1],r_prj_id[position
                     * -1],r_subunit_id[position-1]);
                     *
                     * setElementSpinnerData(r_subunit_id[position-1],
                     * r_prj_id[position-1],r_elemnt_id[position-1]);
                     *
                     * setSubElementSpinnerData(r_elemnt_id[position-1],
                     * r_prj_id[position-1],r_subelmt_id[position-1]);
                     * setCheckListSpinnerData
                     * (r_prj_id[position-1],r_checklist_id[position-1]);
                     */
                    setCheckListSpinnerData(r_prj_id[position - 1],
                            r_checklist_id[position - 1],
                            r_worktype_id[position - 1]);
                    setGoupSpinnerData(r_checklist_id[position - 1],
                            r_grp_id[position - 1], r_node_id[position - 1]);

                    System.out.println("selected all id project="
                            + r_prj_id[position - 1] + " struct="
                            + r_struct_id[position - 1] + " floor id="
                            + r_stage_id[position - 1] + " unit id="
                            + r_unit_id[position - 1] + " sub unit ="
                            + r_subunit_id[position - 1] + " el="
                            + r_elemnt_id[position - 1] + "sub ele="
                            + r_subunit_id[position - 1] + " check ="
                            + r_checklist_id[position - 1] + " grop="
                            + r_grp_id[position - 1] + "node id="
                            + r_node_id[position - 1] + " question id "
                            + r_que_id[position - 1]);

                    Q_id = r_que_id[position - 1];

                    setSpiner();
                    coverageedit.setText(r_coverage_id[position - 1]);
                    drawingeedit.setText(r_drawing_id[position - 1]);
                } else {
                    db.selectedrfiId = "";
                    System.out.println("hello");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        elementSpinMulti.setItems(items);
        elementSpinMulti.hasNoneOption(true);
        elementSpinMulti.setSelection(new int[]{0});
        elementSpinMulti.setListener(this);
    }

    // /---client
    private void setClientData(String rf_client_id) {

        String where = "Client_ID='" + rf_client_id + "' AND user_id='"
                + db.userId + "'";

        System.out.println("client in the table=====" + rf_client_id);
        // Client_ID TEXT,Clnt_Name TEXT,CL_Dispaly_Name TEXT

        System.out.println("cleint where -==========>" + where);
        Cursor cursor = db.select("Client as ct",
                "distinct(ct.Client_ID),ct.Clnt_Name", where, null, null, null,
                null);

        clientId = new String[cursor.getCount()];
        client = new String[cursor.getCount() + 1];
        client[0] = "--select--";
        boolean setspinflag = false;

        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                System.out.println("nnnnnnnnnnnnnnn");
                clientId[cursor.getPosition()] = cursor.getString(0);
                client[cursor.getPosition() + 1] = cursor.getString(1);
            } while (cursor.moveToNext());
        } else {
            client[0] = "Client not allocated";

        }

        if (cursor != null && !cursor.isClosed()) {

            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, client);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        clintSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (setspinflag) {
            clintSpin.setSelection(1);
            clintSpin.setClickable(false);
        }
        clintSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                System.out.println("position" + position);
                if (position > 0) {

                    System.out.println("selected ckient id"
                            + clientId[position - 1]);
                    db.selectedclientname = clintSpin.getSelectedItem()
                            .toString();
                    System.out.println("client selected not present");
                    db.selectedClientId = clientId[position - 1];
                    // setSchemeSpinnerData(clientId[position- 1]);
                    // db.selectedClient = client[position];

                } else {
                    db.selectedClientId = "";
                    db.selectedclientname = clintSpin.getSelectedItem()
                            .toString();

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSchemeSpinnerData(String Cl_ID, String rf_project_id) {

        /*
         * //PK_Scheme_ID TEXT,Scheme_Name TEXT,Scheme_Cl_Id
         * TEXT,Scheme_Diplay_Name TEXT,Scheme_Adrs TEXT," + "Scheme_Region
         * TEXT, user_id TEXT
         */
        String where = "s.Scheme_Cl_Id = c.Client_ID AND Scheme_Cl_Id='"
                + Cl_ID + "' AND PK_Scheme_ID='" + rf_project_id
                + "' AND s.user_id='" + db.userId + "'";
        Cursor cursor = db.select("Scheme as s, Client as c",
                "distinct(s.PK_Scheme_ID), s.Scheme_Name,scrolling_status",
                where, null, null, null, null);

        System.out.println("where in project==========" + where
                + "=============id=" + rf_project_id);
        boolean setspinflag = false;
        schemId = new String[cursor.getCount()];
        scroll_status = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (setspinflag) {
            projSpin.setSelection(1);
            projSpin.setClickable(false);
        }

        projSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSchemeId = schemId[position - 1];
                    db.selectedScrollStatus = scroll_status[position - 1];
                    // setBulidngSpinnerData(schemId[position - 1]);

                    db.selectedSchemeName = projSpin.getSelectedItem()
                            .toString();
                    System.out.println("heloooooooo===="
                            + db.selectedSchemeName);
                    // setCheckListSpinnerData(schemId[position - 1]);

                    System.out.println("selected scheme id"
                            + schemId[position - 1]);

                    /*
                     * structureSpin.setClickable(true);
                     * structureSpin.setSelection(0);
                     * checklistspin.setClickable(true);
                     * checklistspin.setSelection(0);
                     */

                } else {
                    db.selectedSchemeId = "";
                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedScrollStatus = "";
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

    private void setWorkTypeSpinnerData(String workype_id, final String schemId2) {
        /* FK_PRJ_Id */

        String where = "user_id='" + db.userId + "' AND FK_PRJ_Id='" + schemId2
                + "' AND WorkTyp_ID='" + workype_id + "'";

        Cursor cursor = db.select("WorkType",
                "distinct(WorkTyp_ID),WorkTyp_Name", where, null, null, null,
                null);

        System.out.println("in workype...............");
        wTypeId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        boolean setspinflag = false;
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                wTypeId[cursor.getPosition()] = cursor.getString(0);
                items[cursor.getPosition() + 1] = cursor.getString(1);
            } while (cursor.moveToNext());
        } else {
            items[0] = "WorkType(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateworktypeSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (setspinflag) {
            updateworktypeSpin.setSelection(1);
            updateworktypeSpin.setClickable(false);
        }

        updateworktypeSpin
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> aview, View view,
                                               int position, long rowid) {
                        if (position > 0) {
                            db.selectedWorkTypeId = wTypeId[position - 1];

                            // db.selectedSchemeName=projSpin.getSelectedItem().toString();
                            System.out.println("heloooooooo  worktype id===="
                                    + db.selectedWorkTypeId);

                        } else {
                            db.selectedWorkTypeId = "";

                        }
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
    }

    private void setBulidngSpinnerData(final String schemeid,
                                       String rf_struct_id, String wrktype_id) {

        String structr = "";

        // Building(Bldg_ID TEXT,Bldg_Name TEXT,Build_scheme_id TEXT, user_id
        // TEXT)");

        String where = "b.Build_scheme_id = s.PK_Scheme_ID AND s.PK_Scheme_ID='"
                + schemeid
                + "' "
                + "AND s.user_id='"
                + db.userId
                + "' AND b.FK_WorkTyp_ID='"
                + wrktype_id
                + "' AND b.Bldg_ID='"
                + rf_struct_id + "' AND b.user_id='" + db.userId + "'";

        Cursor cursor = db.select("Building as b, Scheme as s",
                " distinct(b.Bldg_ID), b.Bldg_Name", where, null, null, null,
                null);

        bldgId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        boolean setspinflag = false;

        items[0] = "--Select--";
        if (cursor.moveToFirst()) {
            do {
                setspinflag = true;
                bldgId[cursor.getPosition()] = cursor.getString(0);
                items[cursor.getPosition() + 1] = cursor.getString(1);

            } while (cursor.moveToNext());
        } else {
            items[0] = "Building(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        structureSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (setspinflag) {
            structureSpin.setSelection(1);
            structureSpin.setClickable(false);
        }

        structureSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedBuildingId = bldgId[position - 1];
                    db.selectedNodeId = bldgId[position - 1];

                    System.out.println("selected scheme id"
                            + bldgId[position - 1]);
                    // setFloorSpinnerData(bldgId[position - 1],schemeid);
                    /*
                     * stageSpin.setClickable(true); stageSpin.setSelection(0);
                     */

                    System.out.println("selecteed bdg id"
                            + db.selectedBuildingId);

                } else {

                    db.selectedBuildingId = "";
                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setFloorSpinnerData(String buld_id, final String schemeid,
                                     String rf_stage_id, String wk_type_id) {

        // floor (floor_Id TEXT,floor_Name TEXT,Floor_Scheme_ID TEXT,FK_Bldg_ID
        // TEXT, user_id TEXT)");
        String flr_id = "";
        String where = "f.Floor_Scheme_ID='" + schemeid
                + "' AND f.FK_Bldg_ID='" + buld_id

                + "' AND f.FK_WorkTyp_ID='" + wk_type_id

                + "' AND f.floor_Id='" + rf_stage_id

                + "' AND f.user_id='" + db.userId
                + "' AND s.PK_Scheme_ID=f.Floor_Scheme_ID";
        Cursor cursor = db.select("floor as f, Scheme as s",
                "distinct(f.floor_Id),f.floor_Name", where, null, null, null,
                null);

        boolean setspinflag = false;
        floorid = new String[cursor.getCount()];
        final String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                floorid[cursor.getPosition()] = cursor.getString(0);
                items[cursor.getPosition() + 1] = cursor.getString(1);
            } while (cursor.moveToNext());
        } else {
            items[0] = "Floor(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stageSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (setspinflag) {
            stageSpin.setSelection(1);
            stageSpin.setClickable(false);
        }

        stageSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (position > 0) {
                    db.selectedFloorId = floorid[position - 1];
                    db.selectedNodeId = floorid[position - 1];

                    System.out.println("selected stage id"
                            + floorid[position - 1]);
                    // setUnitSpinnerData(floorid[position - 1],schemeid);
                    /*
                     * unitSpin.setClickable(true); unitSpin.setSelection(0);
                     */

                } else {

                    db.selectedChecklistId = "";
                    db.selectedSubGroupId = "";
                    db.selectedFloorId = "";

                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    // 4 ~1 ~1 ~41 ~3 ~2 ~2 ~3 ~1 ~37 ~41 ~0 ~0 ~0 ~12/31/2013 5:59:29 PM ~ ~25
    // ~Confirmity ~ ~ ~ ~False ~3 ~1/3/2014 5:59:40 PM ~3 ~1/28/2014 11:40:02
    // AM

    private void setUnitSpinnerData(String floor_id, final String schemid,
                                    String rf_unit_id, String wrkType_id) {

        // TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID
        // TEXT, user_id TEXT)");

        String Unt_id = "";
        String where = "u.Unit_Scheme_id=s.PK_Scheme_ID" + " AND s.user_id='"
                + db.userId + "'  AND u.Fk_Floor_ID ='" + floor_id
                + "'  AND u.Unit_ID ='" + rf_unit_id
                + "'  AND u.FK_WorkTyp_ID ='" + wrkType_id
                + "'  AND u.Unit_Scheme_id='" + schemid
                + "' AND u.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("Unit as u,Scheme as s",
                "distinct(u.Unit_ID),u.Unit_Des", where, null, null, null,
                "u.Unit_Des");

        System.out.println("selected scheme id" + db.selectedBuildingId);

        boolean setspinflag = false;
        unitId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        unitSpin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        unitSpin.setVisibility(View.GONE);
        if (setspinflag) {
            unitSpin.setSelection(1);
            unitSpin.setClickable(false);
        }

        unitSpin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedUnitId = unitId[position - 1];
                    db.selectedNodeId = unitId[position - 1];

                    System.out.println("selected unit id"
                            + unitId[position - 1]);
                    // setSubUnitSpinnerData(unitId[position - 1],schemid);
                    /*
                     * subunitspin.setClickable(true);
                     * subunitspin.setSelection(0);
                     */

                } else {

                    System.out.println("hello else unit");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        if (isUnit) {
//            unitSpin.setVisibility(View.GONE);
//            unitSpinMulti.setVisibility(View.VISIBLE);
//        } else {
//            unitSpin.setVisibility(View.VISIBLE);
//            unitSpinMulti.setVisibility(View.GONE);
//        }

        unitSpinMulti.setItems(items);
        unitSpinMulti.hasNoneOption(true);
        unitSpinMulti.setSelection(new int[]{0});
        unitSpinMulti.setListener(this);


    }

    private void setSubUnitSpinnerData(String unit_id, final String schemid,
                                       String rf_subunit_id, String wrkType_id) {

        // TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID
        // TEXT, user_id TEXT)");
        // SubUnit(Sub_Unit_ID TEXT,Sub_Unit_Des TEXT,Sub_Unit_Scheme_id
        // TEXT,FK_Unit_ID TEXT, user_id TEXT)");
        String subunt_id = "";

        String where = "u.Sub_Unit_Scheme_id=s.PK_Scheme_ID"
                + " AND s.user_id='" + db.userId + "' AND s.PK_Scheme_ID='"
                + schemid + "' AND u.FK_Unit_ID='" + unit_id
                + "' AND u.FK_WorkTyp_ID='" + wrkType_id
                + "' AND u.Sub_Unit_ID='" + rf_subunit_id

                + "' AND u.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("SubUnit as u,Scheme as s",
                "distinct(u.Sub_Unit_ID),u.Sub_Unit_Des", where, null, null,
                null, "u.Sub_Unit_Des");

        System.out.println("selected scheme id" + db.selectedBuildingId);

        boolean setspinflag = false;
        subunitId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                subunitId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "SubUnit(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subunitspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (setspinflag) {
            subunitspin.setSelection(1);
            subunitspin.setClickable(false);
        }

        subunitspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubUnitId = subunitId[position - 1];
                    db.selectedNodeId = subunitId[position - 1];
                    // setElementSpinnerData(subunitId[position - 1],schemid);
                    /*
                     * elementspin.setClickable(true);
                     * elementspin.setSelection(0);
                     */

                    System.out.println("selected unit sub id"
                            + subunitId[position - 1]);

                } else {

                    System.out.println("hello sub unit else");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//		if (isSubUnit){
//			subunitspin.setVisibility(View.GONE);
//			subUnitSpinMulti.setVisibility(View.VISIBLE);
//		}else {
//			subunitspin.setVisibility(View.VISIBLE);
//			subUnitSpinMulti.setVisibility(View.GONE);
//		}

        subUnitSpinMulti.setItems(items);
        subUnitSpinMulti.hasNoneOption(true);
        subUnitSpinMulti.setSelection(new int[]{0});
        subUnitSpinMulti.setListener(this);
    }

    private void setElementSpinnerData(String subunit_id, final String schemid,
                                       String rf_element_id, String wrkType_id) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        String elemnt = "";

        String where = "e.Elmt_Scheme_id=s.PK_Scheme_ID" + "  AND s.user_id='"
                + db.userId + "'  AND e.FK_Sub_Unit_ID='" + subunit_id
                + "'  AND e.Elmt_Scheme_id='" + schemid
                + "'  AND e.FK_WorkTyp_ID='" + wrkType_id
                + "'  AND e.Elmt_ID='" + rf_element_id

                + "' AND e.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("Element as e,Scheme as s",
                "distinct(e.Elmt_ID),e.Elmt_Des", where, null, null, null,
                "e.Elmt_Des");

        // System.out.println("selected scheme id"+db.selectedBuildingId);
        boolean setspinflag = false;

        elementId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                elementId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "Element(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        elementspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (setspinflag) {
            elementspin.setSelection(1);
            elementspin.setClickable(false);
        }

        elementspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedElementId = elementId[position - 1];
                    db.selectedNodeId = elementId[position - 1];
                    // setSubElementSpinnerData(elementId[position -
                    // 1],schemid);
                    /*
                     * subelementspin.setClickable(true);
                     * subelementspin.setSelection(0);
                     */
                    System.out.println("selected el sub id"
                            + elementId[position - 1]);

                } else {

                    System.out.println("hello");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isElement) {
            elementspin.setVisibility(View.GONE);
            elementSpinMulti.setVisibility(View.VISIBLE);
        } else {
            elementspin.setVisibility(View.VISIBLE);
            elementSpinMulti.setVisibility(View.GONE);
        }

        elementSpinMulti.setItems(items);
        elementSpinMulti.hasNoneOption(true);
        elementSpinMulti.setSelection(new int[]{0});
        elementSpinMulti.setListener(this);
    }

    private void setSubElementSpinnerData(String element_id,
                                          final String schemid, String rf_subelmt_id, String wrkType_id) {

        String subelmnt = "";
        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // SubElement(Sub_Elmt_ID TEXT,Sub_Elmt_Des TEXT,Sub_Elmt_Scheme_id
        // TEXT,FK_Elmt_ID TEXT, user_id TEXT)");

        String where = "e.Sub_Elmt_Scheme_id=s.PK_Scheme_ID"
                + " AND s.user_id='" + db.userId + "' AND s.PK_Scheme_ID='"
                + schemid + "' AND e.FK_Elmt_ID='" + element_id
                + "' AND e.FK_WorkTyp_ID='" + wrkType_id
                + "' AND e.Sub_Elmt_ID='" + rf_subelmt_id

                + "' AND e.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("SubElement as e,Scheme as s",
                "distinct(e.Sub_Elmt_ID),e.Sub_Elmt_Des", where, null, null,
                null, "e.Sub_Elmt_Des");

        // System.out.println("selected scheme id"+db.selectedBuildingId);
        boolean setspinflag = false;

        subelementId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                subelementId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "Element(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subelementspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (setspinflag) {
            subelementspin.setSelection(1);
            subelementspin.setClickable(false);
        }

        subelementspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedSubElementId = subelementId[position - 1];
                    db.selectedNodeId = subelementId[position - 1];
                    System.out.println("selected subel sub id"
                            + subelementId[position - 1]);

                } else {
                    System.out.println("else subelement ");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        if (isSubElement) {
            subElementSpinMulti.setVisibility(View.VISIBLE);
            subelementspin.setVisibility(View.GONE);

        } else {
            subElementSpinMulti.setVisibility(View.GONE);
            subelementspin.setVisibility(View.VISIBLE);
        }

        subElementSpinMulti.setItems(items);
        subElementSpinMulti.hasNoneOption(true);
        subElementSpinMulti.setSelection(new int[]{0});
        subElementSpinMulti.setListener(this);
    }

    // CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT, user_id
    // TEXT)");

    private void setCheckListSpinnerData(String schemid,
                                         String rf_checklist_id, String wrkType_id) {

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT, user_id
        // TEXT)");

        String chec_id = "";

        String where = "s.user_id='" + db.userId + "' AND FK_WorkTyp_ID ='"
                + wrkType_id + "' AND c.Checklist_ID='" + rf_checklist_id
                + "' AND c.user_id=s.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("CheckList as c,Scheme as s",
                "distinct(c.Checklist_ID),c.Checklist_Name", where, null, null,
                null, "c.Checklist_Name");

        // System.out.println("selected scheme id"+db.selectedBuildingId);
        boolean setspinflag = false;

        checklistId = new String[cursor.getCount()];
        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
                checklistId[cursor.getPosition()] = cursor.getString(0);
                System.out.println("trade id printed=" + cursor.getString(0));
                items[cursor.getPosition() + 1] = cursor.getString(1);
                System.out.println("trade name printed=" + cursor.getString(1));
            } while (cursor.moveToNext());
        } else {
            items[0] = "CheckList(s) not available";
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        checklistspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (setspinflag) {
            checklistspin.setSelection(1);
            checklistspin.setClickable(false);
        }

        checklistspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedChecklistId = checklistId[position - 1];
                    // setGoupSpinnerData(checklistId[position - 1]);
                    db.selectedChecklistName = checklistspin.getSelectedItem()
                            .toString();
                    /*
                     * grouptspin.setClickable(true);
                     * grouptspin.setSelection(0);
                     */

                } else {

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

    private void setGoupSpinnerData(final String checklistId, String rf_grp_id,
                                    String node_id) {
        System.out.println("In setGroupSpinnerData.....");
        System.out.println("CheckList ID : " + checklistId);
        System.out.println("Group ID : " + rf_grp_id);
        System.out.println("Node ID : " + node_id);

        // Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID
        // TEXT, user_id TEXT)");

        // Group1(Grp_ID TEXT,Grp_Name TEXT,Node_id TEXT,FK_Checklist_ID
        // TEXT,user_id TEXT)");

        String grp_id = "";

        String where = "c.Checklist_ID='" + checklistId
                + "'  AND  g.FK_Checklist_ID=c.Checklist_ID"
                + " AND c.user_id='" + db.userId + "' AND g.Grp_ID='"
                + rf_grp_id + "' AND g.Node_id='" + node_id

                + "' AND g.user_id=c.user_id ";
        System.out.println("where ->" + where);
        Cursor cursor = db.select("Group1 as g,CheckList as c",
                "distinct(g.Grp_ID),g.Grp_Name,g.Node_id", where, null, null,
                null, "g.Grp_Name");

        // System.out.println("selected scheme id"+db.selectedBuildingId);

        System.out.println("Cursor Count : " + cursor.getCount());
        boolean setspinflag = false;

        groupId = new String[cursor.getCount()];

        String[] items = new String[cursor.getCount() + 1];
        items[0] = "--Select--";
        if (cursor.moveToFirst()) {

            do {
                setspinflag = true;
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
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grouptspin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (setspinflag) {
            grouptspin.setSelection(1);
            grouptspin.setClickable(false);
        }

        grouptspin.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> aview, View view,
                                       int position, long rowid) {
                if (position > 0) {
                    db.selectedGroupId = groupId[position - 1];

                    db.selectedGroupName = grouptspin.getSelectedItem()
                            .toString();

                } else {
                    System.out.println("else..................");
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//		if(isGroup){
//			grouptspin.setVisibility(View.VISIBLE);
//			subelementspin.setVisibility(View.GONE);
//
//		}else {
//			grouptspin.setVisibility(View.GONE);
//			subelementspin.setVisibility(View.VISIBLE);
//		}


        groupSpinMulti.setItems(items);
        groupSpinMulti.hasNoneOption(true);
        groupSpinMulti.setSelection(new int[]{0});
        groupSpinMulti.setListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db.selectedHeadingId = "";
        finish();
        db.closeDb();
        startActivity(new Intent(this, HomeScreen.class));
    }

    // -------------------------------------------------------------------------balaji
    // code-----------

    protected void callService() {
        Webservice service = new Webservice(CheckFRI.this, network_available,
                "Loading.. Please wait..", method, param, value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

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

    protected void callService1() {
        Webservice service = new Webservice(CheckFRI.this, network_available,
                "Loading.. Please wait..", method, param, value);
        service.setdownloadListener(new downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                System.out.println("success data");

                saveDataRFI(data);
                // saveDataRFI(da);
                if (isDataAvialable("checklist")) {
                    setRFIData();
                } else {
                    updateData();
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

    public void saveData(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                System.out.println("data=========" + tabledata.toString());

                String column = "Client_ID,Clnt_Name,CL_Dispaly_Name,Clnt_Adrs,user_id";// project
                saveToDatabase("Client", column, tabledata[0], true, 4);

                column = "PK_Scheme_ID,Scheme_Name,Scheme_Cl_Id,Scheme_Diplay_Name,Scheme_Adrs,Scheme_Region,scrolling_status, user_id";
                saveToDatabase("Scheme", column, tabledata[1], true, 7);

                /*
                 * column =
                 * "Bldg_ID,Bldg_Name,Build_scheme_id,user_id";//buildng
                 * saveToDatabase("Building", column, tabledata[2],true,3);
                 *
                 *
                 * column =
                 * "floor_Id,floor_Name,Floor_Scheme_ID,FK_Bldg_ID, user_id"
                 * ;//buildng saveToDatabase("floor", column,
                 * tabledata[3],true,4);
                 *
                 *
                 *
                 *
                 *
                 * //Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id
                 * TEXT,Fk_Floor_ID TEXT, user_id TEXT)") column =
                 * "Unit_ID,Unit_Des,Unit_Scheme_id,Fk_Floor_ID,user_id"
                 * ;//buildng saveToDatabase("Unit", column,
                 * tabledata[4],true,4);
                 *
                 *
                 *
                 * column =
                 * "Sub_Unit_ID,Sub_Unit_Des,Sub_Unit_Scheme_id,FK_Unit_ID,user_id"
                 * ;//buildng saveToDatabase("SubUnit", column,
                 * tabledata[5],true,4);
                 *
                 *
                 * column =
                 * "Elmt_ID,Elmt_Des,Elmt_Scheme_id,FK_Sub_Unit_ID, user_id"
                 * ;//buildng saveToDatabase("Element", column,
                 * tabledata[6],true,4);
                 *
                 *
                 * column =
                 * "Sub_Elmt_ID,Sub_Elmt_Des,Sub_Elmt_Scheme_id,FK_Elmt_ID,user_id"
                 * ;//buildng saveToDatabase("SubElement", column,
                 * tabledata[7],true,4);
                 *
                 *
                 *
                 *
                 *
                 * column =
                 * "Checklist_ID,Checklist_Name,Node_Id,user_id";//buildng
                 * saveToDatabase("CheckList", column, tabledata[8],true,3);
                 *
                 * column =
                 * "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID, user_id";//buildng
                 * saveToDatabase("Group1", column, tabledata[9],true,4);
                 *
                 *
                 *
                 * column =
                 * "PK_question_id,QUE_Des,QUE_SequenceNo,QUE_Type, NODE_Id, Fk_CHKL_Id, Fk_Grp_ID,user_id"
                 * ;//buildng saveToDatabase("question", column,
                 * tabledata[10],true,7);
                 */

                column = "WorkTyp_ID,WorkTyp_Name,WorkTyp_level,FK_PRJ_Id,user_id";// worktype
                saveToDatabase("WorkType", column, tabledata[2], true, 4);

                column = "Bldg_ID,Bldg_Name,Build_scheme_id,FK_WorkTyp_ID,user_id";// buildng
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

                column = "Grp_ID,Grp_Name,Node_id,FK_Checklist_ID,GRP_Sequence_tint,user_id";// buildng
                saveToDatabase("Group1", column, tabledata[10], true, 4);
                System.out
                        .println("Response For Group Data : " + tabledata[10]);
                System.out.println("Data inserted in Group1 table");

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

    public void saveDataRFI(String data) {
        if (data.equalsIgnoreCase("No Record Found")) {
            displayDialog("No Record Found",
                    "(Note: You may not have the updated data.)");
        } else {

            try {
                String[] tabledata = data.split("\\$");

                System.out.println("data=========need to check====" + tabledata.toString());

                String column = "RFI_Id,CL_Id,PRJ_Id,NODE_Id,Level_int,Fk_WorkTyp_ID,CHKL_Id,GRP_Id,USER_Id,"
                        + "StructureId,StageId,UnitId,SubUnitId,"
                        + "ElementId,SubElementId,RFICreatedOn_date,Coverage,QUE_Id,Answer,Remark,"
                        + "Image1,image2,Status,enterByUserId,detailCreatedOn_date,CheckedByUserId,DetailCheckedOn_date,Rfi_name,Image3,image4,checker_remark,drawing_no,EmpytPlace,status_device,user_id1";// project
                saveToDatabase1("Check_update_details", column, tabledata[0],
                        true, 32);

                // setRFIData();
                // setClientData();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
                e.printStackTrace();
                flushData();
                displayDialog("No Record Found",
                        "Sufficientkl Data is not available.");
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
        for (int len = 0; len < rowdata.length; len++) {
            String[] singlerow = rowdata[len].split("~");
            StringBuffer values = new StringBuffer("'");
            for (int i = 0; i < (singlerow.length - 1); i++) {
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

    public void saveToDatabase1(String table, String columns, String respose,
                                boolean adduserId, int colCnt) {
        Cursor cursor = db.select(table, columns,
                "USER_Id='" + db.userId + "'", null, null, null, null);
        String existingData = "";
        if (cursor.moveToFirst()) {
            for (int i = 0; i < colCnt; i++) {
                existingData += "'" + cursor.getString(i) + "',";
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        int km = 0;

        String[] rowdata = respose.split("\\|");
        for (int len = 0; len < rowdata.length; len++) {
            System.out.println("row===" + rowdata[len].toString());

            String[] singlerow = rowdata[len].split("~", -1);
            System.out.println("row data lenght=" + singlerow.length);
            StringBuffer values = new StringBuffer("'");
            for (int i = 0; i < (singlerow.length - 1); i++) {
                values.append(singlerow[i] + "','");
                km++;
            }
            values.append(singlerow[singlerow.length - 1] + "'");

            if (adduserId) {
                values.append(",'" + "incomplete" + "','" + db.userId + "'");
            }
            if (!existingData.contains(values.toString()))
                db.insert(table, columns, values.toString());
            km = 0;
            System.out.println("colum=" + table + "table value==="
                    + values.toString());
        }

    }

    public String method(String str) {

        if (str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
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

    private boolean isDataRfiAvialable(String table) {

        Cursor cursor = db.select(table, "count(*) as noitem", "user_id1='"
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

    public void setSpinerselection() {
        /* if (db.setSpinner) { */

        // rfiSpin.setSelection(0);
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

        /* } */

    }

    protected boolean validateScreen() {
        boolean validate = true;

        if (coverageedit.getText().toString().equalsIgnoreCase("")) {
            errorMessage = "Please Enter Coverage.";
        } else
            validate = false;

        return validate;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {

        multipleSelectedList = strings;

    }
}
