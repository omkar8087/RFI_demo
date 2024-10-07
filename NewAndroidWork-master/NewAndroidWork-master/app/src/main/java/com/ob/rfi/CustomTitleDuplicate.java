package com.ob.rfi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

/*import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;*/

public class CustomTitleDuplicate extends AppCompatActivity {

	
 
	public static boolean network_available = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		//request custom title bar
		/*requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		 
		
	//	getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
		
		setContentView(R.layout.background);
		
		//set custom title bar 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
		
		*/
		
		//networkIndicator = (ImageView) findViewById(R.id.networkicon);
		
		
		getSupportActionBar().setIcon(R.drawable.logo);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuItem networkIndicator = menu.findItem(R.id.network_image);
		
		if(networkIndicator==null){
			return super.onPrepareOptionsMenu(menu);
		}
		
		if(network_available){  
			System.out.println("network==========="+network_available);
		networkIndicator.setIcon(R.drawable.nw_avilable);
		}
		else{
			System.out.println("network in else==========="+network_available);
			networkIndicator.setIcon(R.drawable.nw_notavil);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {

		
			ConnectivityManager connectivityManager =
				(ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );     
			
			NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();     
			State mobile = connectivityManager.getNetworkInfo(0).getState();
			State wifi = connectivityManager.getNetworkInfo(1).getState();
			
			
			
			
			
			if(activeNetInfo == null)
			{
				network_available = false;
				//networkIndicator.setBackgroundResource(R.drawable.nw_notavil);
				//getSupportActionBar().setIcon(R.drawable.n);
				getSupportActionBar().setDisplayShowHomeEnabled(true);
				

			}else if (mobile == State.CONNECTED || mobile == State.CONNECTING)
			{
				network_available = true;
				//networkIndicator.setBackgroundResource(R.drawable.nw_avilable);
				//getSupportActionBar().setIcon(R.drawable.nw_avilable);
				invalidateOptionsMenu();
			
			} else if (wifi == State.CONNECTED || wifi == State.CONNECTING)
			{
				network_available = true;
			//	getSupportActionBar().setIcon(R.drawable.nw_avilable);
				invalidateOptionsMenu();
				//networkIndicator.setBackgroundResource(R.drawable.nw_avilable);
			}else{
				network_available = false;
				//getSupportActionBar().setIcon(R.drawable.nw_avilable);
				invalidateOptionsMenu();
					
				
				
				
				
				//networkIndicator.setBackgroundResource(R.drawable.nw_notavil);
			}
		
		}
		
	};
	 
	
	@Override
	protected void onResume() {
		super.onResume();
	
		registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));		
		invalidateOptionsMenu();
	}
	
	 
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		//EasyTracker.getInstance(this).activityStart(this);
	}
	 
	 
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		//EasyTracker.getInstance(this).activityStop(this);
	}
	
	/*@Override
	protected void onStop(){
	    super.onStop();
	    finish();
	}*/
	 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater =getMenuInflater();
		inflater.inflate(R.menu.custom_menu, menu);		
		
		return true;
	}
	
	/*@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(receiver);	
		} catch (IllegalArgumentException e) {
		}
		catch (Exception e) {
		}
		
	}*/
	
	@Override
	protected void onDestroy()
	 {
	    super.onDestroy();
	    try {
			unregisterReceiver(receiver);	
		} catch (IllegalArgumentException e) {
		}
		catch (Exception e) {
		}
	    
	    
	 }
	
	

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}
}