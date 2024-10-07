package com.ob.rfi;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabRfiActivity extends CustomTitle
{
	LocalActivityManager mLocalActivityManager;
	private TabHost tabHost;


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_tab);
		 tabHost = (TabHost)findViewById(android.R.id.tabhost);
		 
		
		 
		 mLocalActivityManager = new LocalActivityManager(this, false);
		 
         tabHost.setup(mLocalActivityManager);
         mLocalActivityManager.dispatchCreate(savedInstanceState);
		SetTabContent();
		
		
		
		
		
	} 
	
	
	public void SetTabContent(){
 
        // create the TabHost that will contain the Tabs
       
         
        TabSpec tab1 = tabHost.newTabSpec("CheckList");
        TabSpec tab2 = tabHost.newTabSpec("Maker");
        TabSpec tab3 = tabHost.newTabSpec("Checker");
        TabSpec tab4 = tabHost.newTabSpec("Checker");
        TabSpec tab5 = tabHost.newTabSpec("Checker");
         
     // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
         tab1.setIndicator("CheckListwise");
         tab1.setContent(new Intent(TabRfiActivity.this,CheckListWiseList.class));
        
         tab2.setIndicator("Makerwise"); 
         tab2.setContent(new Intent(TabRfiActivity.this,MakerWiseList.class));
 
         tab3.setIndicator("Checkerwise");
         tab3.setContent(new Intent(TabRfiActivity.this,CheckerWiseList.class));
        
         tab4.setIndicator("Approverwise");
         tab4.setContent(new Intent(TabRfiActivity.this,ApproverWise.class));
        
         tab5.setIndicator("Representing Wise");
         tab5.setContent(new Intent(TabRfiActivity.this,ContractorWiseList.class));
        
         
         /** Add the tabs  to the TabHost to display. */
         
         tabHost.addTab(tab1); 
         tabHost.addTab(tab2);  
         tabHost.addTab(tab3);
         tabHost.addTab(tab4);
         tabHost.addTab(tab5);
        
	}
	
	
	@Override
    protected void onPause() {
            super.onPause();
            mLocalActivityManager.dispatchPause(isFinishing()); //you have to manually dispatch the pause msg
    }

    @Override 
    protected void onResume() {
            super.onResume();
            mLocalActivityManager.dispatchResume(); //you have to manually dispatch the resume msg
    } 
	

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	//super.onBackPressed();
    	finish();
    	Intent int1=new Intent(TabRfiActivity.this,DashBoardSelection.class);
    	startActivity(int1);		
    	
    }
    
}
