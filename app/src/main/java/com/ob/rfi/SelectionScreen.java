package com.ob.rfi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ob.rfi.db.RfiDatabase;

@SuppressWarnings("static-access")
public class SelectionScreen extends CustomTitle {

	ExpandableListAdapter mAdapter;
	private Vibrator vibrator;
	private RfiDatabase db;
	private String[] groups;
	private String[][] children;
	private String[][] q_ids;
	private TextView selectedGroup;
	public static boolean flag1 = false;
	public boolean flag2 = false;
	public List list;
	private int count=0;
	private String text="";
	private int backcount=0;
	
	ArrayList<String> stringArrayList = new ArrayList<String>();
	ArrayList<String> newArrayList = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.selection_screen);
		initComponent();
		initcount();
		
	} 

	private void initcount()
	{ 
		
		if (db.selectedsuprvsname.equals("Approved") || db.selectedsuprvsname.equals("Supervisor not allocated") ||db.selectedsuprvsname.equals("Not present")|| db.selectedsuprvsname.equals("--select--")) 
		{
			System.out.println("count not increamented.................supervisor");
			
		}else{
			
			db.flagselect1=true;
		}
		
		if (db.selectedclientname.equals("Approved") || db.selectedclientname.equals("Client not allocated") ||db.selectedclientname.equals("Not present")|| db.selectedclientname.equals("--select--")) 
		{
			System.out.println("count not increamented.................client");
		}else{
			
			db.flagselect3=true;
		}
		
		if (db.selectedforemanname.equals("Approved") || db.selectedforemanname.equals("Foreman not allocated") ||db.selectedforemanname.equals("Not present")|| db.selectedforemanname.equals("--select--")) 
		{
			System.out.println("count not increamented.................forman");
		}else
		{
			
			db.flagselect2=true;
		}
		
		
	}
	
	private void initComponent() {
		LinearLayout parent = (LinearLayout) findViewById(R.id.panelContent);
		vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
		selectedGroup = (TextView) findViewById(R.id.groupName);

		db = new RfiDatabase(getApplicationContext());
		if (db.userId.equalsIgnoreCase("")) {
			logout();
		} else {
			getQuestionGroups();
			ArrayList<String> idsList = getAnsweredGroupIds();
			int len = Q_groupName.length;
			// Log.e("MSG", len + "");
			LinearLayout innerLinearLayout[] = new LinearLayout[len % 2 == 0 ? (len / 2) + 1
					: (len / 2) + 2];
			Button[] button = new Button[len];

			for (int i = 0, j = 0; i < len; i++) {

				button[i] = new Button(this);
				button[i]
						.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT, 2f));
				button[i].setSingleLine(true);
				button[i].setEllipsize(TruncateAt.MARQUEE);
				button[i].setMarqueeRepeatLimit(10);
				button[i].setSelected(true);

				// Log.e("IDS***", "$$$$$$$$ === "+idsList.toString()+
				// " gruopId"+Q_groupId[i]);

				if (idsList.contains(String.valueOf(Q_groupId[i]))) {
					button[i]
							.setBackgroundResource(R.drawable.slider_button_bg);
				} else {
					button[i].setBackgroundResource(R.drawable.button_selector);
				}
				button[i].setHorizontallyScrolling(true);
				button[i].setText(Q_groupName[i]);
				button[i].setTag(Q_groupName[i]);
				button[i].setId(Q_groupId[i]);

				// Log.e("MSG", Q_groupName[i]);

				if (i % 2 == 0) {
					innerLinearLayout[++j] = new LinearLayout(this);
					innerLinearLayout[j]
							.setLayoutParams(new LayoutParams(
									LayoutParams.FILL_PARENT,
									LayoutParams.WRAP_CONTENT));
					innerLinearLayout[j].setGravity(Gravity.CENTER);
					if (len % 2 == 1 && i == (len - 1)) {
						TextView text = new TextView(this);
						text.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT, 2f));
						innerLinearLayout[j].addView(button[i]);
						innerLinearLayout[j].addView(text);
					} else {
						innerLinearLayout[j].addView(button[i]);
					}
					parent.addView(innerLinearLayout[j]);
				} else { 
					innerLinearLayout[j].addView(button[i]);
				}
   
				button[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						
						RfiDatabase.gropPosition=0;
						if (arg0.getTag().toString()
								.equalsIgnoreCase("No Question Group Found.")) {
							displayDialog("CQRA", "No Question Group Found.");
							System.out.println(" setlist empty........");
						} else {
							vibrator.vibrate(200);
							db.selectedGroupId = String.valueOf(arg0.getId());
							db.selectedGroupName = arg0.getTag().toString();
							System.out.println("call setlist........grp id=="+db.selectedGroupId);
							
							setListData();
						}
					}
				});
			} 

			selectedGroup.setText("Please select the group.");

			if (!db.selectedGroupId.equals(""))
				setListData();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (db.userId.equalsIgnoreCase("")) {
			logout();
		}
	}

	private void logout() {
		finish();
		Toast.makeText(SelectionScreen.this,
				"Session expired... Please login.", Toast.LENGTH_SHORT).show();
		Intent logout = new Intent(SelectionScreen.this, LoginScreen.class);
		logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(logout);
	}

	private ArrayList<String> getAnsweredGroupIds() {
		ArrayList<String> ids = new ArrayList<String>();
		String where = "user_id='" + db.userId + "' AND proj_id='"
				+ db.selectedSchemeId + "' AND bldg_id='"
				+ db.selectedBuildingId + "' AND floor_id='"
				+ db.selectedFloorId + "' AND checklist_id='"
				+ db.selectedChecklistId + "' AND sub_group_id='"
				+ db.selectedSubGroupId + "'";
		Log.d("Q", where);
		Cursor group_id_cursor = db.select("answare", "distinct(group_id)",
				where, null, null, null, null);
		if (group_id_cursor.moveToFirst()) {
			do {
				ids.add(group_id_cursor.getString(0));
			} while (group_id_cursor.moveToNext());
		}

		if (group_id_cursor != null && !group_id_cursor.isClosed()) {
			group_id_cursor.close();
		}

		return ids;
	}

	int[] Q_groupId;
	String[] Q_groupName;
	

	// private String[] q_observation;

	private void getQuestionGroups() {

		String where = "q.q_grp_id=qg.q_group_id AND qg.user_id=q.user_id AND qg.user_id='"
				+ db.userId
				+ "' AND q.proj_id='"
				+ db.selectedSchemeId
				+ "' AND q.bldg_id='"
				+ db.selectedBuildingId
				+ "' AND q.floor_id='"
				+ db.selectedFloorId
				+ "' AND q.checklist_id='"
				+ db.selectedChecklistId
				+ "' AND q.q_subgrp_id='" + db.selectedSubGroupId + "'";
		Log.d("Q", where);
		Cursor cursor = db.select("question_group as qg, question as q",
				"distinct(q.q_grp_id), qg.q_group_name", where, null, null,
				null, null);

		int cnt = 0;
		Q_groupId = new int[cursor.getCount()];
		Q_groupName = new String[cursor.getCount()];
		if (cursor.moveToFirst()) {
			do {
				Q_groupId[cnt] = Integer.parseInt(cursor.getString(0));
				Q_groupName[cnt] = cursor.getString(1);
				cnt++;
			} while (cursor.moveToNext());
		} else {
			Q_groupName = new String[] { "No Question Group Found." };
			Q_groupId = new int[] { 0 };
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}

	protected void setListData() {
		try {
			// Get Question Headings under selected Question Group
			String where = "q.q_grp_id='" + db.selectedGroupId
					+ "' AND q.user_id='" + db.userId + "' AND "
					+ "q.proj_id='" + db.selectedSchemeId + "' AND q.bldg_id='"
					+ db.selectedBuildingId + "' AND q.floor_id='"
					+ db.selectedFloorId + "' AND q.q_subgrp_id='"
					+ db.selectedSubGroupId + "' AND q.checklist_id='"
					+ db.selectedChecklistId
					+ "' AND qh.q_heading_id=q.q_heading_id AND qh.user_id='"
					+ db.userId + "' order by q.q_heading_id ";

			//+ db.userId + "' order by q.q_heading_id ";
			Cursor groupCursor = db  
					.select("question_heading as qh join question as q",
							"q.q_heading_id, qh.q_heading_text, q.PK_question_id, q.q_text, q.q_min_observation,"
									+ " q.completed_observations", where, null,
							null, null,null);
			
			//db.select(TABLE_NAME, COLUMNS, WHERE, SELECTION_ARGS, GROUP_BY, HAVING, OREDER_BY)
			

		 System.out.println(" **** "+ where);

			int cnt = -1;
			int queCnt = 0;
			String grps = ""; 
			String childs = "";
			String qids = "";
			String prevHeading = "";
			String min_obs = "";

			newArrayList.clear();
			
			/*if(groupCursor.moveToFirst())
			{
				do{
					
					newArrayList.add(groupCursor.getString(3).toString())
					
					dsf
					
				}while (groupCursor.moveToNext());

			}
			*/
			
			
			
			
			// q_observation = new String[groupCursor.getCount()];
			if (groupCursor.moveToFirst()) {
				selectedGroup.setText("Selected Group : "
						+ db.selectedGroupName);
				
								
				
			
				do {
					if(!newArrayList.contains(groupCursor.getString(3).toString())){
					
					System.out.println("dddddddata==="+groupCursor.getString(3).toString());
					
					min_obs = groupCursor.getString(4).equals("") ? "0"
							: groupCursor.getString(4);

					String obs = "$" + groupCursor.getString(5) + "/" + min_obs
							+ "$";
					obs = obs + getAnswareDate(groupCursor.getString(2)) + "$";
					System.out.println("1>>>>>" + groupCursor.getString(0));
					System.out.println("2>>>>>" + groupCursor.getString(1));
					System.out.println("3>>>>>" + groupCursor.getString(2));

					if (!prevHeading.equalsIgnoreCase(groupCursor.getString(0))) {
						System.out.println("PREV >>>>" + prevHeading
								+ "CURRENT <<<<<<<" + groupCursor.getString(0));
						queCnt = 0;
						prevHeading = groupCursor.getString(0);
						cnt++;
						// data under new header..
						grps += groupCursor.getString(1) + "^";
						qids += "~" + groupCursor.getString(2) + "^";
						childs += "~" + groupCursor.getString(3) + obs;

					} else {
						queCnt++;
						qids += groupCursor.getString(2) + "^";
						childs += groupCursor.getString(3) + obs;
					}

					// Getting observation count for question according to
					// sections
					// System.out.println("%%%% proj_id='"+db.selectedSchemeId+"' AND bldg_id='"
					// +db.selectedBuildingId+"' AND floor_id='"+db.selectedFloorId+"' AND question_id='"+
					// groupCursor.getString(2)+"' AND user_id='"+db.userId+"'");
					Cursor count_cursor = db.select(
							"answare",
							"completed_observations",
							"proj_id='" + db.selectedSchemeId
									+ "' AND bldg_id='" + db.selectedBuildingId
									+ "' AND floor_id='" + db.selectedFloorId
									+ "' AND question_id='"
									+ groupCursor.getString(2)
									+ "' AND user_id='" + db.userId + "'",
							null, null, null, null);
					// int pos = groupCursor.getPosition();
					if (count_cursor.moveToFirst()) {
						childs += count_cursor.getCount() + "/" + min_obs;
						System.out.println("inner query....");
					} else
						childs += "0" + "/" + min_obs;
					System.out.println("outer query.....");
					// System.out.println(" question count ="+pos+" ==="+q_observation[pos]);
					count_cursor.close();
					childs += "^";
					
					newArrayList.add(groupCursor.getString(3).toString());
				}else
				{
					System.out.println("continue");
					continue;
				}
					
					
				} while (groupCursor.moveToNext());
				System.out.println("Got the groups " + grps);
				qids = qids.substring(1, qids.length());
				// System.out.println(" childs == "+ childs);
				childs = childs.substring(1, childs.length());

				groups = grps.split("\\^");
				String[] tempqid = qids.split("~");
				String[] tempchild = childs.split("~");
				children = new String[groups.length][groupCursor.getCount()];
				q_ids = new String[groups.length][groupCursor.getCount()];

				// This will add question under respective question header in
				// list.
				for (int i = 0; i < groups.length; i++) {
					tempqid[i] = tempqid[i].substring(0,
							tempqid[i].length() - 1);
					tempchild[i] = tempchild[i].substring(0,
							tempchild[i].length() - 1);
					q_ids[i] = tempqid[i].split("\\^");
					children[i] = tempchild[i].split("\\^");
				}

				// If data available fill data into list
				ExpandableListView list = (ExpandableListView) findViewById(R.id.ExpandableListView);

				mAdapter = new MyExpandableListAdapter();
			
				list.setAdapter(mAdapter);
				
				list.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPos, int childPos, long id) {
						db.selectedQuestionId = q_ids[groupPos][childPos];
						finish();
						db.closeDb();
						RfiDatabase.gropPosition = groupPos;
						RfiDatabase.childPosition = childPos;
						startActivity(new Intent(SelectionScreen.this,
								Questionnaire.class));
						return false;
					}
				});

				if (RfiDatabase.gropPosition + RfiDatabase.childPosition > 0) {

					list.expandGroup(RfiDatabase.gropPosition);
					list.setSelectedChild(RfiDatabase.gropPosition,
							RfiDatabase.childPosition, true);
					// System.out.println("chlidselection ++++++++++++++++++++"+result+">>>>>>"+CqraDatabase.gropPosition+">>>>>>>>>"+CqraDatabase.childPosition);
				}

			} else {
				selectedGroup.setText("Data not available.");
			}
			if (groupCursor != null && !groupCursor.isClosed()) {
				groupCursor.close();
			}
		} catch (Exception e) {
			System.out.println(" exception :=" + e.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		db.closeDb();
		super.onDestroy();
	}

	public class MyExpandableListAdapter extends BaseExpandableListAdapter {

		private int obsPos = -1;

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public TextView getGenericView(int id, boolean flag) {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			TextView textView = new TextView(SelectionScreen.this);
			textView.setMinHeight(40);
			textView.setLayoutParams(param);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setPadding(50, 0, 5, 0);
			textView.setTypeface(null, Typeface.BOLD);
			textView.setTextSize(18);
			textView.setBackgroundResource(R.drawable.list_header);
			// textView.setBackgroundColor(0x290000FF);
 
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			boolean flag = false;
			String observation = getChild(groupPosition, childPosition)
					.toString();
			String data[] = observation.split("\\$");
			observation = data[0];
			
			System.out.println("row data ========"+observation);
			String[] array = data[1].split("/");
			if (Integer.parseInt(array[0]) >= Integer.parseInt(array[1])) {
				flag = true;
			}
			/*
			 * TextView textView = getGenericView(2,flag);
			 * textView.setText(getChild
			 * (groupPosition,childPosition).toString()); return textView;
			 */
			
			//hiiiiiiiiiii
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) getBaseContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.custom_list_view,
						null);
			}
			
			  list = new ArrayList();
			  System.out.println("list size=="+list.size());
			  if(!list.contains(observation))
			  {
				  list.add(observation);
			
			
			TextView qtv = (TextView) convertView.findViewById(R.id.list_question_text);           ///chage
			qtv.setText(observation);

			
			TextView octv = (TextView) convertView
					.findViewById(R.id.list_observation_count);
			octv.setText("Total:" + data[1] + " Stage:" + data[3]);
			obsPos++;

			if (!flag) {
				octv.setTextColor(getResources().getColor(R.color.Red));
			} else {
				octv.setTextColor(getResources().getColor(R.color.White));
			}

			DateFormat df = new DateFormat();
			if (data[2] != null
					&& df.format("yyyy-MM-dd", new Date()).equals(data[2])) {
				convertView.setBackgroundResource(R.drawable.list_child);
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						android.R.color.transparent));
			}
			
			  }
			  return convertView;
			 ///chage
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {

			TextView textView = getGenericView(1, false);
			System.out.println("Group position >>>>>>>>>> " + groupPosition);
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}
		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}
	}

	/*
	 * @Override public void onBackPressed() { super.onBackPressed();
	 * db.selectedHeadingId = ""; db.selectedGroupId = ""; //finish();
	 * db.closeDb();
	 * 
	 * 
	 * int k=0;
	 * 
	 * if(k==0) {
	 * 
	 * 
	 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 * builder.setTitle("  "); builder.setMessage("select Cards");
	 * 
	 * builder.setPositiveButton("Green Card", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { Intent i = new Intent(SelectionScreen.this,
	 * SelectQuestion.class); i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	 * startActivity(i);
	 * 
	 * dialog.dismiss(); finish(); } });
	 * 
	 * builder.setNegativeButton("Red Card", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { Intent
	 * i = new Intent(SelectionScreen.this, SelectQuestion.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i);
	 * dialog.dismiss(); } }); AlertDialog alert = builder.create();
	 * alert.show(); }
	 * 
	 * 
	 * 
	 * Intent i = new Intent(SelectionScreen.this, SelectQuestion.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i); }
	 */

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub
	 * 
	 * 
	 * System.out.println("keycode++++++++++++++++++++++======"+keyCode+"Event"+
	 * event); if (keyCode == KeyEvent.KEYCODE_BACK) {
	 * System.out.println("id++++++++++==="+db.selectedSupervisorId); if
	 * (db.selectedSupervisorId.equals("1")) { finish(); Intent i = new
	 * Intent(SelectionScreen.this, SelectQuestion.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i); }
	 * 
	 * else {
	 * 
	 * if (CqraDatabase.obs_taken) {
	 * 
	 * String S_name=db.selectedSupervisor;
	 * 
	 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
	 * builder.setTitle("Assign card for"); builder.setMessage(S_name);
	 * 
	 * builder.setPositiveButton("Green Card", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int which) { flag2=true; saveSupervisorEntries(); Intent i = new
	 * Intent(SelectionScreen.this, SelectQuestion.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i); db.
	 * obs_taken=false; dialog.dismiss(); finish(); } });
	 * 
	 * builder.setNegativeButton("Red Card", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * flag2=false; saveSupervisorEntries(); Intent i = new
	 * Intent(SelectionScreen.this, SelectQuestion.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i);
	 * dialog.dismiss(); db. obs_taken=false; finish(); } }); AlertDialog alert
	 * = builder.create(); alert.show();
	 * 
	 * } else {
	 * 
	 * Intent i = new Intent(SelectionScreen.this, SelectQuestion.class);
	 * i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); startActivity(i);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * } return true;
	 * 
	 * // return super.onKeyDown(keyCode, event);
	 * 
	 * }
	 */

	public void inspection_log() {
		System.out.println("inspection");
		DateFormat df = new DateFormat();
		String InspectionDate = (String) df.format("yyyy-MM-dd", new Date());
		String i_data = "'" + db.selectedSchemeId + "','"
				+ db.selectedBuildingId + "','" + db.selectedTradeId + "','"
				+ db.selectedContractorId + "','" + InspectionDate + "','"
				+ db.userId + "'";
		db.insert(
				"inspection_log",
				"scheme_Id,structure_id,trade_id,contractor_id,Inspection_date,user_id",
				i_data);

	}

	public void setalertdata()
	{
		db.setSpinner=true;
		DateFormat df = new DateFormat();
		String alertDate = (String) df.format("yyyy-MM-dd", new Date());
		
		 
		 
		String alert_s="S";
		String alert_f="F";  
		String alert_c="C";
		String alert_AF="f_yes";
		String alert_AS="s_yes";
		System.out.println("alertsssss"+db.checkalert_s+db.checkalert_f);
		System.out.println("supervisor="+db.selectedsuprvsname+" "+"foreman"+" "+db.selectedforemanname);
		
		/*if (db.selectedSupervisorId.equals("0") && !db.checkalert_s) {
			String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_s+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
			System.out.println("s inserted**************************8********");
			
			
		}	
		
		if (db.selectedForemanId.equals("0") && !db.checkalert_f) {
			String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_f+"','"+db.userId+ "'";
				db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
				System.out.println("f inserted****************************************************8");
				
		} 
		 
		if (db.selectedSupervisorId.equals("1") && !db.checkalert_s) {
			String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_AS+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
			System.out.println("s inserted**************************8********");
		} 
		
		if (db.selectedForemanId.equals("1") && !db.checkalert_f) {
			String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_AF+"','"+db.userId+ "'";
				db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
				System.out.println("f inserted****************************************************8");
				
		}*/
		
		if (db.selectedsuprvsname.contains("Not present") && db.selectedforemanname.contains("Not present")) {
			String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_s+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
			String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_f+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
			
			
			System.out.println("both inserted");
			
			 
		}	/*if (db.selectedsuprvsname.contains("Not present") && db.selectedforemanname.contains("Foreman not allocated")) {
			String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_s+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
			
			
			System.out.println("A supervisor inserted");
			
			
		}
		
		if (db.selectedsuprvsname.contains("Not present") && db.selectedforemanname.contains("--select--")) {
			String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_s+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
			
			
			System.out.println("  b supervisor inserted");
			
			
		}
		
		
		if (!db.selectedsuprvsname.contains("Not present") && db.selectedforemanname.contains("Not present")) {
			String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_f+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
			
			
			System.out.println(" A foreman inserted");
			
			
		}
		
		if (db.selectedsuprvsname.contains("--select--") && db.selectedforemanname.contains("Not present")) {
			String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_f+"','"+db.userId+ "'";
			db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
			
			
			System.out.println(" B foreman inserted");
			
			
		}*/
		
		
		
		
		
		
		//--------
			if (db.selectedsuprvsname.contains("Not present")) {
				String insertalert_s="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_s+"','"+db.userId+ "'";
				db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_s);
				System.out.println("  b supervisor inserted");
				
				
			}
		
		
			if (db.selectedforemanname.contains("Not present")) {
				String insertalert_f="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_f+"','"+db.userId+ "'";
				db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_f);
				System.out.println(" B foreman inserted");
				
				
			}
		
			if (db.selectedclientname.contains("Not present")) {
				
				String insertalert_c="'"+db.selectedSchemeId+"','"+db.selectedBuildingId + "','"+db.selectedTradeId+ "','"+alertDate+"','"+alert_c+"','"+db.userId+ "'";
				db.insert("alert_details","scheme_Id,structure_id,trade_id,a_date,alert_for,user_id ",insertalert_c);
				System.out.println("client inserted");
				
			}
		
		
		
		
		
		  
	}
	
	public void setalert()
	{
		System.out.println("setalert=======================================");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Assign card for");
		

		builder.setPositiveButton("Green Card",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {

						flag2 = true;
						saveSupervisorEntries();
						
						
						Intent i = new Intent(SelectionScreen.this,
								SelectQuestion.class);
						i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						
						db.setSpinner = true;
						finish();
						startActivity(i);
						db.obs_taken = false;
						
						
						// finish();
					}
				});

		builder.setNegativeButton("Red Card",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						flag2 = false;
						saveSupervisorEntries();
						
						if(!(db.isclient || db.isforman))
						{
						Intent i = new Intent(SelectionScreen.this,
								SelectQuestion.class);
						i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
						finish();
						
						db.obs_taken = false;
						db.setSpinner = true;
						startActivity(i);
						
						}
						
						dialog.dismiss();
						// finish();
					}
				});

		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		db.setSpinner = true;

		if (db.inspectionflag) {
			inspection_log();
			db.inspectionflag = false;
		}

		
		if(db.isanswered)
		{
			setalertdata();
			System.out.println("method called===");
			db.isanswered=false;
		}
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			/*if (db.selectedSupervisorId.equals("0") || db.selectedSupervisorId.equals("1")) {
			*/
			if (db.selectedsuprvsname.equals("Approved") || db.selectedsuprvsname.equals("Supervisor not allocated") ||db.selectedsuprvsname.equals("Not present")|| db.selectedsuprvsname.equals("--select--")) 
			{
			finish();
				db.setSpinner = true;
				System.out.println("not in alertbox....................");
				Intent i = new Intent(SelectionScreen.this,
						SelectQuestion.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
			}else {
				if (RfiDatabase.obs_taken) { 

					String S_name = db.selectedSupervisor;
 
					
					AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
					/*builder.setTitle("Assign card for");
					builder.setMessage("Superviser:"+S_name);*/
					System.out.println("outside....................");
					builder.setPositiveButton("Green Card",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									flag2 = true;
									
									saveSupervisorEntries();
									if(db.flagselect2)
									{
									saveFormanEntries();
									}
									if(db.flagselect3)
									{
										saveclientEntries();
									}
									
									db.flagselect2=false;
									db.flagselect3=false;
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									db.setSpinner = true;
									finish();
									startActivity(i);
									db.obs_taken = false;
									dialog.dismiss();
									
									// finish();
								}
							});

					builder.setNegativeButton("Red Card",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									flag2 = false;
									saveSupervisorEntries();
									if(db.flagselect2)
									{
									saveFormanEntries();
									} 
									if(db.flagselect3)
									{
										saveclientEntries();
									}
									db.flagselect2=false;
									db.flagselect3=false;
									
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									finish();
									
									db.obs_taken = false;
									db.setSpinner = true;
									startActivity(i);
									dialog.dismiss();
									
									
								}
							});

					builder.setNeutralButton("NA",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									finish();
									db.flagselect2=false;
									db.flagselect3=false;
									db.setSpinner = true;
									startActivity(i);
									dialog.dismiss();
									
								
								}
							}); 

					AlertDialog alert = builder.create();
					alert.show();

				} else {
					
					finish(); 
					System.out.println();
					Intent i = new Intent(SelectionScreen.this,
							SelectQuestion.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
					startActivity(i);
				
				}
 
			}
			
			//-------------client
			
	/*		
			if (db.selectedclientname.equals("Approved") || db.selectedclientname.equals("Client not allocated") ||db.selectedclientname.equals("Not present")|| db.selectedclientname.equals("--select--")) 
			{
			finish();
				db.setSpinner = true;
				Intent i = new Intent(SelectionScreen.this,
						SelectQuestion.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
			}else {
				if (CqraDatabase.obs_taken) {

					String S_name = db.selectedClient;

					AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
					builder1.setTitle("Assign card for");
					builder1.setMessage("Client:"+ S_name);

					builder1.setPositiveButton("Green Card",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
  
									flag2 = true;
									//saveSupervisorEntries();
									
									if(!(db.isforman ))
									{
									saveclientEntries();
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

									db.setSpinner = true;
									finish();
									startActivity(i);
									db.obs_taken = false;
									dialog.dismiss();
									}
									dialog.dismiss();
								}
							});
 
					builder1.setNegativeButton("Red Card",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									flag2 = false;
									
									
									if(!(db.isforman ))
									{
										
									saveclientEntries();
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									finish();

									db.setSpinner = true;
									startActivity(i);
									db.obs_taken = false;
									dialog.dismiss();
									}
									dialog.dismiss();
								}
							});

					builder1.setNeutralButton("NA",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if(!(db.isforman && db.supervisorcompte))
									{
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									finish();
									db.setSpinner = true;
									startActivity(i);
									dialog.dismiss();
									}
									dialog.dismiss();
								}
							});

					AlertDialog alert1 = builder1.create();
					alert1.show();

				} else {
					if(!(db.isforman ))
					{ 
					finish();
					Intent i = new Intent(SelectionScreen.this,
							SelectQuestion.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
					}
				}

			} 
			
			//--------------forman
			/*		if (db.selectedforemanname.equals("Approved") || db.selectedforemanname.equals("Foreman not allocated") ||db.selectedforemanname.equals("Not present")|| db.selectedforemanname.equals("--select--")) 
			{
			finish();
				db.setSpinner = true;
				Intent i = new Intent(SelectionScreen.this,
						SelectQuestion.class);
				i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(i);
			}else {
				if (CqraDatabase.obs_taken) {

					String S_name = db.selectedForeman;

					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Assign card for");
					builder.setMessage(S_name);

					builder.setPositiveButton("Green Card",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
     
									flag2 = true;
									//saveSupervisorEntries();    
									saveFormanEntries();
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

									db.setSpinner = true; 
			 						finish();     
									startActivity(i);
									db.obs_taken = false;
									dialog.dismiss();
									// finish();  
								}
							}); 

					builder.setNegativeButton("Red Card",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									flag2 = false;
									//saveSupervisorEntries();
									saveFormanEntries();
									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									finish();

									db.setSpinner = true;
									startActivity(i);
									dialog.dismiss();
									db.obs_taken = false;
									// finish();
								}
							});

					builder.setNeutralButton("NA",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									Intent i = new Intent(SelectionScreen.this,
											SelectQuestion.class);
									i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									finish();
									db.setSpinner = true;
									startActivity(i);
									dialog.dismiss();
									// finish();
								}
							});

					AlertDialog alert = builder.create();
					alert.show();

				} else {
					finish();
					Intent i = new Intent(SelectionScreen.this,
							SelectQuestion.class);
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(i);
				}

			}
			*/
			
			
			}
		return true;

		// return super.onKeyDown(keyCode, event);

	}

	public void saveSupervisorEntries() {

		String card_type = "";
		String cardDate = "";

		if (flag2) {
			card_type = "g"; 
		} else {
			card_type = "r";
		}

		DateFormat df = new DateFormat();
		cardDate = (String) df.format("yyyy-MM-dd", new Date());

		String insertcard = "'" + db.selectedSchemeId + "','"
				+ db.selectedBuildingId + "','" + db.selectedTradeId + "','"
				+ card_type + "','" + cardDate + "','"
				+ db.selectedSupervisorId + "','" + db.userId + "'";
		db.insert(
				"card_details",
				"scheme_Id,structure_id,trade_id,card_type,date,supervisor_id,user_id ",
				insertcard);

		System.out.println("supervisor  id++++++++++++++++++++++++++++++=="
				+ db.selectedSupervisorId);
		System.out.println("hiiiiiiiiiiiiiii++++===" + insertcard);

	}
	
	
	public void saveFormanEntries() {

		String card_type = "";
		String cardDate = "";

		if (flag2) {
			card_type = "g"; 
		} else {
			card_type = "r";
		}

		DateFormat df = new DateFormat();
		cardDate = (String) df.format("yyyy-MM-dd", new Date());

		String insertcard = "'" + db.selectedSchemeId + "','"
				+ db.selectedBuildingId + "','" + db.selectedTradeId + "','"
				+ card_type + "','" + cardDate + "','"
				+ db.selectedForemanId + "','" + db.userId + "'";
		db.insert(
				"card_details_forman",
				"scheme_Id,structure_id,trade_id,card_type,date,foreman_id,user_id ",
				insertcard);

		System.out.println("forman id++++++++++++++++++++++++++++++=="
				+ db.selectedForemanId);
		System.out.println("hiiiiiiiiiiiiiii++++===" + insertcard);

	}
	
	
	public void saveclientEntries() {

		String card_type = "";
		String cardDate = "";

		if (flag2) {
			card_type = "g"; 
		} else {
			card_type = "r";
		}

		DateFormat df = new DateFormat();
		cardDate = (String) df.format("yyyy-MM-dd", new Date());

		String insertcard = "'" + db.selectedSchemeId + "','"
				+ db.selectedBuildingId + "','" + db.selectedTradeId + "','"
				+ card_type + "','" + cardDate + "','"
				+ db.selectedClientId + "','" + db.userId + "'";
		db.insert(
				"card_details_client",
				"scheme_Id,structure_id,trade_id,card_type,date,client_staf_id,user_id ",
				insertcard);

		System.out.println("client id++++++++++++++++++++++++++++++=="
				+ db.selectedClientId);
		System.out.println("hiiiiiiiiiiiiiii++++===" + insertcard);

	}
	

	private String getAnswareDate(String qid) {
		String where = "proj_id='" + db.selectedSchemeId + "' AND bldg_id='"
				+ db.selectedBuildingId + "' AND floor_id='"
				+ db.selectedFloorId + "' AND question_id=" + qid
				+ " AND user_id='" + db.userId + "'";
		Cursor cursor = db.select("answare", "ans_date", where, null, null,
				null, null);

		if (cursor.moveToFirst()) {
			String result = cursor.getString(0);
			cursor.close();
			return result;
		} else {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			} 
			return "NONE";
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	}
	
	


	@SuppressWarnings("deprecation")
	protected void displayDialog(final String title, final String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(false);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onBackPressed();

			}
		});
		alertDialog.show();
	}

}