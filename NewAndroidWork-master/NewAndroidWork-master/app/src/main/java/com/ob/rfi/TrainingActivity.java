package com.ob.rfi;


import com.ob.rfi.db.RfiDatabase;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class TrainingActivity extends CustomTitle {
	public static final String TAG = "TrainingActivity";
	private Spinner client_spinner;
	private Spinner scheme_spinner;
	private Spinner trade_spinner;
	private ListView contractor_list;
	private TextView select_all_contractor;
	private CheckBox select_all_check_contractor;
	private ListView client_staff_list;
	private TextView select_all_client_staff;
	private CheckBox select_all_check_client_staff;
	private RfiDatabase db;
	private String []client;
	private String []scheme;
	private String []trade;
	String method;
	String[] param;
	String[] value;
	int requestid = 0;
	private Button submitButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.training);
		
		select_all_contractor = (TextView) findViewById(R.id.select_all_contractor);
		select_all_client_staff = (TextView) findViewById(R.id.select_all_client_satff);
		select_all_check_contractor = (CheckBox) findViewById(R.id.check_all_contractor);
		select_all_check_client_staff = (CheckBox) findViewById(R.id.check_all_client_staff);
		submitButton = (Button) findViewById(R.id.submit_button);
		Button cancelButton = (Button) findViewById(R.id.cancl_button);
		client_spinner = (Spinner) findViewById(R.id.scheme_spinner);
		scheme_spinner = (Spinner) findViewById(R.id.scheme_spinner);
		trade_spinner = (Spinner) findViewById(R.id.trade_spinner);
		contractor_list = (ListView) findViewById(R.id.selection_contractor);
		client_staff_list = (ListView) findViewById(R.id.selection_client_staff);
		db = new RfiDatabase(getApplicationContext());
	}
	
}
