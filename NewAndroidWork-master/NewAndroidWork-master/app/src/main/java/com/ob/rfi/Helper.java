package com.ob.rfi;

import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Helper {
	
	
	/* public static void getListViewSize(ListView myListView) {
	        ListAdapter myListAdapter = myListView.getAdapter();
*/

	        public static void setListViewHeightBasedOnChildren(ListView myListView) {
	            ListAdapter myListAdapter = myListView.getAdapter();
	            if (myListAdapter == null) {
	                // pre-condition
	                return;
	            }
	        
	        
	        
	        /*
	        if (myListAdapter == null) {
	            //do nothing return null
	            return;
	        }*/
	        //set listAdapter in loop for getting final size
	        int totalHeight = 0;
	        int desiredWidth = MeasureSpec.makeMeasureSpec(myListView.getWidth(), MeasureSpec.AT_MOST);
	        for (int size = 0; size < myListAdapter.getCount(); size++) {
	            View listItem = myListAdapter.getView(size, null, myListView);
	            //listItem.measure(0, 0);
	            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	            totalHeight += listItem.getMeasuredHeight();
	        }
	      //setting listview item in adapter
	        ViewGroup.LayoutParams params = myListView.getLayoutParams();
	        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
	        myListView.setLayoutParams(params);
	        myListView.requestLayout();
	        // print height of adapter on log
	        Log.i("height of listItem:", String.valueOf(totalHeight));
	    }

}
