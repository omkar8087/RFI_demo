package com.ob.rfi.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

@SuppressWarnings("static-access")
public class RfiDatabase {

	public static String imgUploaded="";
	public static boolean isUploaded= false;



	public static final String UPLOADED		=	"uploaded"; 
	public static final String ANSWERED		=	"answered";
	public static final String NOTUPLOADED	=	"notuploaded";
	public static final String NOTANSWERED	=	"notanswered";
	public static final String USER_PREF_NAME = "username";

	private SQLiteDatabase db;
	private Context context;
	private SQLiteStatement insertStmt;
	private SQLiteStatement updateStmt;
	private SQLiteStatement deleteStmt;
	
	private SQLiteQueryBuilder bb;
	private SQLiteOpenHelper mOpenHelper;
	public static String LastSelectedListItem="";
	public static String selectedSubUnitId="";
	public static String selectedElementId="";
	public static String selectedSubElementId=""; 
	public static String selectedUnitId="";
	public static String selectedlevelId="";
	public static String selectedCoverage="";
	
	public static String selectedSchemeName="";
	public static String selectedChecklistName="";
	public static String questionSubmitCount="0";
	public static String questionCount="";
	
	public static String selectedgroupName="";
	public static String selectedrfiId="";
	public static String selectedNodeId="";
	

	public static String userId 	= "";
	public static String userRole = "";  // AKSHAY
	public static String selectedGroupId 	= "";
	public static String selectedGroupName 	= "";
	public static String selectedRfiName="";

	public static String selectedSchemeId 	= "";
	public static String selectedBuildingId = "";
	public static String selectedWorkTypeId = "";
	public static String selectedFloorId 	= "";
	public static String selectedChecklistId = "";
	public static String selectedSubGroupId = "";
	public static String selectedHeadingId 	= "";
	public static String selectedQuestionId = "";
	public static String selectedTradeId="";
	public static String selectedSupervisorId="";
	public static String selectedSupervisor="";
	public static String selectedSupervisortext="";
	public static String selectedForemanId="";
	public static String selectedForeman="";
	public static String selectedContractorId="";
	public static String selectedContractor="";
	
	public static String selectedClientId="";
	public static String selectedClient="";
	public static String selectedclientname="";
	public static String selectedStructureName="";
	
	public static String selectedScrollStatus="";
	
	public static String selectedsuprvsname="";
	public static String selectedforemanname="";
	
	public static boolean obs_taken=false;
	public static boolean isToBeUploaded = false;
	public static boolean isToBeUploadedCheck = false;
	public static boolean checkalert_s=false;
	public static boolean checkalert_f=false;
	public static boolean setSpinner=false;
	public static boolean supervisor_flag=false;
	public static boolean foreman_flag=false;
	public static boolean contractor_flag=false;
	public static boolean contr=false;
	public static boolean remark_flag=false;
	public static boolean apporve_flag=false;
	public static boolean apporve_count_flag=false;
	public static boolean inspectionflag=false;
	public static boolean isanswered=false;
	public static boolean isforman=false;
	public static boolean isclient=false;
	public static boolean supervisorcompte=false;
	//---------------
	public static boolean compte1=false;
	public static boolean compte2=false;
	public static boolean compte3=false;
	
	public static boolean flagselect1=false;
	public static boolean flagselect2=false;
	public static boolean flagselect3=false;
	
	public static boolean rollFlag=false;
	
	public static int gropPosition=0;
	public static int childPosition=0;
	
	public static boolean fromCreate = false;

	public static boolean justLogged = false;



	public static String selectedDirections = "";

	public static File path = new File(Environment.getExternalStorageDirectory()
			+ File.separator + "ConQA" + File.separator);

	public RfiDatabase(Context context) {
		this.context = context;
		openHelper openHelper = new openHelper(this.context);
		this.db = openHelper.getWritableDatabase();
	}

	public long insert(String TABLE_NAME, String TABLE_FIELDS,
			String TABLE_VALUES) {

		String INSERT = "insert into " + TABLE_NAME + "(" + TABLE_FIELDS
		+ ") values (" + TABLE_VALUES + ")";
		
		this.insertStmt = this.db.compileStatement(INSERT);
		return this.insertStmt.executeInsert();
	}

	public Cursor UNION(String TABLE_NAME1,String TABLE_NAME2,String COLUMNS1,String COLUMNS2,String WHERE1,String WHERE2)
	{
		String uni="select distinct "+new String[] { COLUMNS1 }+"from"+TABLE_NAME1+"where"+WHERE1+"union"+
				"select distinct "+new String[] { COLUMNS2 }+"from"+TABLE_NAME2+"where"+WHERE2;
		
	//	Cursor cur = this.db.query(true, , columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal)

		String q1="select distinct "+new String[] { COLUMNS2 }+"from"+TABLE_NAME2+"where"+WHERE2;
		return null;
		
		//qb.setTables(TABLE_MESSAGES);
		  
        
		
		
		
        
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*public Cursor select(String TABLE_NAME, String COLUMNS, String WHERE,
			String[] SELECTION_ARGS, String GROUP_BY, String HAVING, String OREDER_BY) {

		Cursor cursor = this.db.query(TABLE_NAME, new String[] { COLUMNS },
				WHERE, SELECTION_ARGS, GROUP_BY, HAVING, OREDER_BY);


		return cursor;
	}
	*/
	
	
	public void update(String TABLE_NAME, String TABLE_FIELDS, String CONDITION) {
		String UPDATE = "update " + TABLE_NAME + " set " + TABLE_FIELDS
		+ " where  " + CONDITION;
		this.updateStmt = this.db.compileStatement(UPDATE);
		this.updateStmt.execute();
		return;
	}
	
	
	

	public void delete(String TABLE_NAME, String CONDITION) {
		String DELETE = "delete from " + TABLE_NAME + " where  " + CONDITION;
		System.out.println(" delete ouery"+DELETE);
		this.deleteStmt = this.db.compileStatement(DELETE);
		this.deleteStmt.execute();
		return;
	}  
  
	public void deleteAll(String TABLE_NAME) {
		this.db.delete(TABLE_NAME, null, null);
	}

	public List<String> selectAll(String TABLE_NAME, String COLUMNS,
			String WHERE, String[] SELECTION_ARGS, String GROUP_BY,
			String HAVING, String OREDER_BY) {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query(TABLE_NAME, new String[] { COLUMNS },
				WHERE, SELECTION_ARGS, GROUP_BY, HAVING, OREDER_BY);
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public Cursor select(String TABLE_NAME, String COLUMNS, String WHERE,
			String[] SELECTION_ARGS, String GROUP_BY, String HAVING, String OREDER_BY) {

		Cursor cursor = this.db.query(TABLE_NAME, new String[] { COLUMNS },
				WHERE, SELECTION_ARGS, GROUP_BY, HAVING, OREDER_BY);

		return cursor;
	}
	
	
	
	public Cursor UNIONNew(String TABLE_NAME, String COLUMNS,String WHERE,String TABLE_NAME1,String COLUMNS1,String WHERE1,
			String TABLE_NAME2, String COLUMNS2,String WHERE2,
			String TABLE_NAME3, String COLUMNS3,String WHERE3,
			String TABLE_NAME4, String COLUMNS4,String WHERE4
			) {

		
/*		String sqlString = "SELECT " + COLUMNS + " FROM " + TABLE_NAME  +
				" UNION SELECT " + COLUMNS1 + " FROM " + TABLE_NAME1   +
				" UNION SELECT " + COLUMNS2 + " FROM " + TABLE_NAME2  +
				" UNION SELECT " + COLUMNS3 + " FROM " + TABLE_NAME3   +
				" UNION SELECT " + COLUMNS4 + " FROM " + TABLE_NAME4  
*/				;
		 
		String sqlString = "SELECT " + COLUMNS + " FROM " + TABLE_NAME + " WHERE "+WHERE +
				" UNION SELECT " + COLUMNS1 + " FROM " + TABLE_NAME1 + " WHERE "+WHERE1  +
				" UNION SELECT " + COLUMNS2 + " FROM " + TABLE_NAME2 + " WHERE "+WHERE2  +
				" UNION SELECT " + COLUMNS3 + " FROM " + TABLE_NAME3 + " WHERE "+WHERE3  +
				" UNION SELECT " + COLUMNS4 + " FROM " + TABLE_NAME4 + " WHERE "+WHERE4  ;

		
		
			Cursor cursor = db.rawQuery(sqlString, null); 
		
		//Cursor cursor = this.db.r

		return cursor;
	}
	
	public Cursor UNIONNew1(String TABLE_NAME, String COLUMNS,String WHERE,String TABLE_NAME1,String COLUMNS1,String WHERE1,
			String TABLE_NAME2, String COLUMNS2,String WHERE2,
			String TABLE_NAME3, String COLUMNS3,String WHERE3,
			String TABLE_NAME4, String COLUMNS4,String WHERE4
			) {
		String sqlString = "SELECT " + COLUMNS + " FROM " + TABLE_NAME  +
				" UNION SELECT " + COLUMNS1 + " FROM " + TABLE_NAME1   +
				" UNION SELECT " + COLUMNS2 + " FROM " + TABLE_NAME2  +
				" UNION SELECT " + COLUMNS3 + " FROM " + TABLE_NAME3   +
				" UNION SELECT " + COLUMNS4 + " FROM " + TABLE_NAME4  
				;
		
		Cursor cursor = db.rawQuery(sqlString, null); 
		
		//Cursor cursor = this.db.r

		return cursor;
	}
	
	
	public void closeDb() {
		if(this.db.isOpen()){
			this.db.close();
		}
	}

	public	String getNamebyId(String table,String id){
		String name="";
		String culumn="";
		String where="";
		if(table.equalsIgnoreCase("userMaster")){
			culumn="User_Name";
			where="Pk_User_ID="+id;
		}else if(table.equalsIgnoreCase("Scheme")){
			culumn="Scheme_Name";
			where="PK_scheme_ID='"+id+"' And user_id='"+this.userId+"'";
		}else if(table.equalsIgnoreCase("Building")){
			culumn="Bldg_Name";
			where="Bldg_ID='"+id+"' And user_id='"+this.userId+"'";

		}else if(table.equalsIgnoreCase("Checklist")){
			culumn="Checklist_Name";
			where="PK_checklist_Id='"+id+"' And user_id='"+this.userId+"'";
		}

		Cursor cursor = this.select(table, culumn, where, null, null, null, null);
		if (cursor.moveToFirst()) {
			name=cursor.getString(0);		 
			cursor.close();

		} else {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}				
		} 
		if(name.equals("")){
			return id;
		}
		return validateFilename(name);	
	}

	public static String validateFilename(String filename){
		return filename.replace("?", "-")
		.replace("/", "-")
		.replace("\\","-" )
		.replace("^", "-")
		.replace("$", "-")
		.replace("#", "-")
		.replace("*", "-")
		.replace("\"", "-")
		.replace("\'", "-")
		.replace("`", "-")
		.replace("|", "-");
	}


	private static class openHelper extends SQLiteOpenHelper {

		public openHelper(Context context) {
			super(context, "RFI.db", null, 13);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE userMaster(Pk_User_ID INTEGER PRIMARY KEY, User_Name TEXT, Password TEXT,user_role TEXT,dashboardroll TEXT)");
			
			
			db.execSQL("CREATE TABLE Client(Client_ID TEXT,Clnt_Name TEXT,CL_Dispaly_Name TEXT,Clnt_Adrs TEXT, user_id TEXT)");
			
			db.execSQL("CREATE TABLE Scheme(PK_Scheme_ID TEXT,Scheme_Name TEXT,Scheme_Cl_Id TEXT,Scheme_Diplay_Name TEXT,Scheme_Adrs TEXT," +
					"Scheme_Region TEXT,scrolling_status TEXT, user_id TEXT)");
			
			
			db.execSQL("CREATE TABLE Building(Bldg_ID TEXT,Bldg_Name TEXT,Build_scheme_id TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
			
			db.execSQL("CREATE TABLE floor (floor_Id TEXT,floor_Name TEXT,Floor_Scheme_ID TEXT,FK_Bldg_ID TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
			
			db.execSQL("CREATE TABLE Unit(Unit_ID TEXT,Unit_Des TEXT,Unit_Scheme_id TEXT,Fk_Floor_ID TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
			
			db.execSQL("CREATE TABLE SubUnit(Sub_Unit_ID TEXT,Sub_Unit_Des TEXT,Sub_Unit_Scheme_id TEXT,FK_Unit_ID TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
			
			db.execSQL("CREATE TABLE Element(Elmt_ID TEXT,Elmt_Des TEXT,Elmt_Scheme_id TEXT,FK_Sub_Unit_ID TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
		
			db.execSQL("CREATE TABLE SubElement(Sub_Elmt_ID TEXT,Sub_Elmt_Des TEXT,Sub_Elmt_Scheme_id TEXT,FK_Elmt_ID TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
			
			db.execSQL("CREATE TABLE CheckList(Checklist_ID TEXT,Checklist_Name TEXT,Node_Id TEXT,FK_WorkTyp_ID TEXT, user_id TEXT)");
			
			
			db.execSQL("CREATE TABLE Group1(Grp_ID TEXT,Grp_Name TEXT,Node_id TEXT,FK_Checklist_ID TEXT,user_id TEXT,GRP_Sequence_tint TEXT)");
			
		 
			
			db.execSQL("CREATE TABLE question(PK_question_id TEXT, QUE_Des TEXT, " +
					"QUE_SequenceNo Text,QUE_Type TEXT, NODE_Id TEXT, Fk_CHKL_Id TEXT, Fk_Grp_ID TEXT,user_id TEXT)");
			
			 
			
			 
			
			db.execSQL("CREATE TABLE Answer(PK_Answer_id INTEGER PRIMARY KEY AUTOINCREMENT,CL_Id TEXT,PRJ_Id TEXT,Level_int TEXT,Structure_Id TEXT,Stage_Id TEXT,"+
					"Unit_id TEXT,Sub_Unit_Id TEXT,Element_Id TEXT,SubElement_Id TEXT,Coverage_str TEXT,"+
					"ans_text TEXT,remark TEXT,snap1 TEXT,snap2 TEXT,snap3 TEXT,snap4 TEXT" +
					",Rfi_id TEXT,dated TEXT,FK_question_id TEXT, " +
					"FK_QUE_SequenceNo Text,FK_QUE_Type TEXT, FK_NODE_Id TEXT,Fk_CHKL_Id TEXT, Fk_Grp_ID TEXT," +
					"checker_remark TEXT,check_date TEXT,drawing_no TEXT,answerFlag TEXT,user_id TEXT)");
			
			
			
			db.execSQL("CREATE TABLE Rfi_New_Create(FK_rfi_Id TEXT,user_id TEXT)");
			db.execSQL("CREATE TABLE Created_RFI(FK_rfi_Id TEXT,user_id TEXT,coverageText TEXT)");
			db.execSQL("CREATE TABLE Rfi_update_status(status TEXT DEFAULT notcompleted,FK_rfi_Id TEXT,user_id TEXT)");
			
			
			db.execSQL("CREATE TABLE Images (status TEXT DEFAULT notuploaded, fileName TEXT,user_id TEXT)");
			
			db.execSQL("CREATE TABLE Rfi_check(rfi_no TEXT,user_id TEXT)"); 
			
			
			 //--------updaterfi
			db.execSQL("CREATE TABLE Rfi_update_details(RFI_Id TEXT,CL_Id TEXT,PRJ_Id TEXT,NODE_Id TEXT,Level_int TEXT,Fk_WorkTyp_ID TEXT,CHKL_Id TEXT,GRP_Id TEXT,USER_Id TEXT," +
					"StructureId TEXT,StageId TEXT,UnitId TEXT,SubUnitId TEXT," +
					"ElementId TEXT,SubElementId TXT,RFICreatedOn_date TEXT,Coverage TEXT,QUE_Id TEXT,Answer TEXT,Remark TEXT," +
					"Image1 TEXT,image2 TEXT,Status TEXT,enterByUserId TEXT,detailCreatedOn_date TEXT,CheckedByUserId TEXT,DetailCheckedOn_date TEXT,Rfi_name TEXT,Image3 TEXT,image4 TEXT,checker_remark TEXT,drawing_no TEXT ,EmpytPlace TEXT,status_device TEXT,user_id1 TEXT)" );
			
			
			db.execSQL("CREATE TABLE Check_update_details(RFI_Id TEXT,CL_Id TEXT,PRJ_Id TEXT,NODE_Id TEXT,Level_int TEXT,Fk_WorkTyp_ID TEXT,CHKL_Id TEXT,GRP_Id TEXT,USER_Id TEXT," +
					"StructureId TEXT,StageId TEXT,UnitId TEXT,SubUnitId TEXT," +
					"ElementId TEXT,SubElementId TXT,RFICreatedOn_date TEXT,Coverage TEXT,QUE_Id TEXT,Answer TEXT,Remark TEXT," +
					"Image1 TEXT,image2 TEXT,Status TEXT,enterByUserId TEXT,detailCreatedOn_date TEXT,CheckedByUserId TEXT,DetailCheckedOn_date TEXT,Rfi_name TEXT,Image3 TEXT,image4 TEXT,checker_remark TEXT,drawing_no TEXT ,EmpytPlace TEXT,status_device TEXT,user_id1 TEXT)" );
			
			
			db.execSQL("CREATE TABLE CheckAnswer(PK_Answer_id INTEGER PRIMARY KEY AUTOINCREMENT,CL_Id TEXT,PRJ_Id TEXT,Level_int TEXT,Structure_Id TEXT,Stage_Id TEXT,"+
					"Unit_id TEXT,Sub_Unit_Id TEXT,Element_Id TEXT,SubElement_Id TEXT,Coverage_str TEXT,"+
					"ans_text TEXT,remark TEXT,snap1 TEXT,snap2 TEXT,snap3 TEXT,snap4 TEXT" +
					",Rfi_id TEXT,dated TEXT,FK_question_id TEXT, " +
					"FK_QUE_SequenceNo Text,FK_QUE_Type TEXT, FK_NODE_Id TEXT,Fk_CHKL_Id TEXT, Fk_Grp_ID TEXT,workType_id,check_date TEXT,answerFlag TEXT,user_id TEXT,isAnswered TEXT)");
			
			//added on 10-06-2014
			db.execSQL("CREATE TABLE WorkType(WorkTyp_ID TEXT,WorkTyp_Name TEXT,WorkTyp_level TEXT,FK_PRJ_Id TEXT,user_id TEXT)");
			
			db.execSQL("CREATE TABLE CheckListWise(ClientId TEXT,ClientName TEXT,ProjectId TEXT,ProjectName TEXT,StructureId TEXT,StructureName TEXT," +
					"CheckListId TEXT,CheckListName TEXT,PendingRFICount TEXT,CompleteRFICount TEXT,TotalRFICount TEXT,user_id TEXT)");
			
			
			
			db.execSQL("CREATE TABLE MakerWise(ClientId TEXT,ClientName TEXT,ProjectId TEXT,ProjectName TEXT,StructureId TEXT,StructureName TEXT," +
					"MakerUserId TEXT,MakerUserName TEXT,TotalRFICount TEXT,user_id TEXT)");
			
			
			db.execSQL("CREATE TABLE CheckerWise(ClientId TEXT,ClientName TEXT,ProjectId TEXT,ProjectName TEXT,StructureId TEXT,StructureName TEXT," +
					"CheckerUserId TEXT,CheckerUserName TEXT,TotalRFICount TEXT,user_id TEXT)");
			
			 
			 
			db.execSQL("CREATE TABLE ApproverWise(ClientId TEXT,ClientName TEXT,ProjectId TEXT,ProjectName TEXT,StructureId TEXT,StructureName" +
					" TEXT,ApproverUserId TEXT,ApproverUserName TEXT,TotalRFICount TEXT,user_id TEXT)");
			
			db.execSQL("CREATE TABLE ContractorWise(ClientId TEXT,ClientName TEXT,ProjectId TEXT,ProjectName TEXT,StructureId TEXT,StructureName TEXT," +
					"RepresentingId TEXT,Representingname TEXT,MakeRFICount TEXT,CheckedRFICount TEXT,ApprovedRFICount TEXT,user_id TEXT)");
			
			
			db.execSQL("CREATE TABLE CancelRFI(RFI_ID TEXT,Remark TEXT,User_ID TEXT)");
			
			db.execSQL("CREATE TABLE AllocateTask(Client TEXT,Project TEXT,WorkType TEXT,Structure TEXT,Stage TEXT,Unit TEXT,SubUnit TEXT,Element TEXT," +
					"SubElement TEXT,CheckList TEXT,GroupColumn TEXT,UserID TEXT,NodeID TEXT)");
			
			
		/*	
			RFI_Id~CL_Id~PRJ_Id~NODE_Id~Level_int~CHKL_Id~GRP_Id~USER_Id~StructureId~StageId~UnitId~SubUnitId~ElementId~SubElementId~
			RFICreatedOn_date~Coverage~QUE_Id~Answer~Remark~ImagePath1~imagePath2~Status~enterByUserId~detailCreatedOn_date~CheckedByUserId~DetailCheckedOn_date~|$
		*/	
			
			 
		}

		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + "userMaster");
			db.execSQL("DROP TABLE IF EXISTS " + "question");
			db.execSQL("DROP TABLE IF EXISTS " + "Client");
			db.execSQL("DROP TABLE IF EXISTS " + "Scheme");
			db.execSQL("DROP TABLE IF EXISTS " + "Building");
			db.execSQL("DROP TABLE IF EXISTS " + "floor");
			db.execSQL("DROP TABLE IF EXISTS " + "Unit");	
			db.execSQL("DROP TABLE IF EXISTS " + "SubUnit");
			db.execSQL("DROP TABLE IF EXISTS " + "CheckList");
			db.execSQL("DROP TABLE IF EXISTS " + "Group1");
			db.execSQL("DROP TABLE IF EXISTS " + "Element");
			db.execSQL("DROP TABLE IF EXISTS " + "SubElement");
			db.execSQL("DROP TABLE IF EXISTS " + "Images");
			db.execSQL("DROP TABLE IF EXISTS " + "CancelRFI");
			db.execSQL("DROP TABLE IF EXISTS " + "AllocateTask");
			db.execSQL("DROP TABLE IF EXISTS " + "Created_RFI");
			db.execSQL("DROP TABLE IF EXISTS " + "Rfi_New_Create");
			db.execSQL("DROP TABLE IF EXISTS " + "Rfi_update_status");
			db.execSQL("DROP TABLE IF EXISTS " + "Rfi_update_details");
			db.execSQL("DROP TABLE IF EXISTS " + "Check_update_details");
			db.execSQL("DROP TABLE IF EXISTS " + "CheckAnswer");
			db.execSQL("DROP TABLE IF EXISTS " + "WorkType");
			db.execSQL("DROP TABLE IF EXISTS " + "MakerWise");
			db.execSQL("DROP TABLE IF EXISTS " + "CheckListWise");
			db.execSQL("DROP TABLE IF EXISTS " + "CheckerWise");
			db.execSQL("DROP TABLE IF EXISTS " + "ApproverWise");
			db.execSQL("DROP TABLE IF EXISTS " + "ContractorWise");
			db.execSQL("DROP TABLE IF EXISTS " + "Rfi_check");
			db.execSQL("DROP TABLE IF EXISTS " + "Answer"); 
			//db.delete("Answer", null, null);
			onCreate(db);
		}
	}


	public static void copyDatabase(Context applicationContext, String string) {
		// TODO Auto-generated method stub
		System.out.println("first......");
		boolean DEBUG = true;
		if(!DEBUG){
			System.out.println("not saved......"+DEBUG);
			return;
		}
		System.out.println("not saved....dsafdsf..");
		String databasePath = applicationContext.getDatabasePath(string).getPath();
		File f = new File(databasePath);
		OutputStream myOutput = null;
		InputStream myInput = null;
		System.out.println("database========"+databasePath);
		Log.d("testing", " testing db path " + databasePath); 
		Log.d("testing", " testing db exist " + f.exists());

		if (f.exists()) {
			try {
 
				File directory = new File("/mnt/sdcard/DB");
				if (!directory.exists())
					directory.mkdir();

				myOutput = new FileOutputStream(directory.getAbsolutePath()
						+ "/" + string);
				myInput = new FileInputStream(databasePath);
 
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);
					
					System.out.println("saved------------------");
				}

				myOutput.flush();
			} catch (Exception e) {
			} finally {
				try {
					if (myOutput != null) {
						myOutput.close();
						myOutput = null;
					}
					if (myInput != null) {
						myInput.close();
						myInput = null;
					}
				} catch (Exception e) {
				}
			}
		}
	}


	
}
