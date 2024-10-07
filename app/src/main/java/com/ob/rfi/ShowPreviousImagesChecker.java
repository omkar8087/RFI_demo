package com.ob.rfi;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ob.rfi.db.RfiDatabase;
import com.squareup.picasso.Picasso;

public class ShowPreviousImagesChecker extends Activity{

	
	private Button Btn1;
	private Button Btn2;
	private Button Btn4;
	private Button Btn3;
	private ImageView image1;
	private RfiDatabase db;
	private Cursor questionupdateCursor1;
	private String imageUrl1;
	private String imageUrl2;
	private String imageUrl3;
	private String imageUrl4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_previousi_mages);
		
		db=new RfiDatabase(getApplicationContext());
		Intent int1=getIntent();
		
		String q_id=int1.getStringExtra("q_id");
		//
		
		Btn1=(Button)findViewById(R.id.button1);
		Btn2=(Button)findViewById(R.id.button2);
		Btn3=(Button)findViewById(R.id.button3);
		Btn4=(Button)findViewById(R.id.button4);
		 image1=(ImageView)findViewById(R.id.imageButton1);
		
		 
		 
		 
		 
		 
		 try {
			String whereupdateClause="QUE_Id='"+q_id+
						"' AND CHKL_Id='"+db.selectedChecklistId+"' AND RFI_Id='"+db.selectedrfiId+"' AND GRP_Id='"+db.selectedGroupId+"' AND user_id1='"+db.userId+"'";
				
				questionupdateCursor1 = db.select("Check_update_details", "Image1,Image2,Image3,Image4",whereupdateClause, null, null, null, null);
			 
			 
				if(questionupdateCursor1.moveToFirst()){
					do{
				 
			imageUrl1=questionupdateCursor1.getString(0).toString();
			imageUrl2=questionupdateCursor1.getString(1).toString();
			imageUrl3=questionupdateCursor1.getString(2).toString();
			imageUrl4=questionupdateCursor1.getString(3).toString();
			
			System.out.println("image name="+imageUrl1+"="+imageUrl2+" , "+imageUrl3+" , "+imageUrl4);
			
					}while(questionupdateCursor1.moveToNext());
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		 Btn1.setOnClickListener(new OnClickListener() {
			
			 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					
				
				if(imageUrl1.equals("")){
					image1.setImageResource(R.drawable.black_image);	
					Toast.makeText(getApplicationContext(), "Image Not Available.", Toast.LENGTH_LONG).show();
				}else
				{
				image1.setImageResource(R.drawable.loading_image);	
				 SetImage(imageUrl1);
				}
			}
		});
		 
		 Btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(imageUrl2.equals("")){
						image1.setImageResource(R.drawable.black_image);	
						Toast.makeText(getApplicationContext(), "Image Not Available.", Toast.LENGTH_LONG).show();
					}else
					{
						image1.setImageResource(R.drawable.loading_image);	
					 SetImage(imageUrl2);
					}
				}
			});
			
		 Btn3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(imageUrl3.equals("")){
						image1.setImageResource(R.drawable.black_image);	
						Toast.makeText(getApplicationContext(), "Image Not Available.", Toast.LENGTH_LONG).show();
					}else
					{
						image1.setImageResource(R.drawable.loading_image);	
					 SetImage(imageUrl3);
					}
				}
			});
			
		 Btn4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(imageUrl4.equals("")){
						image1.setImageResource(R.drawable.black_image);	
						Toast.makeText(getApplicationContext(), "Image Not Available.", Toast.LENGTH_LONG).show();
					}else
					{
						image1.setImageResource(R.drawable.loading_image);	
					 SetImage(imageUrl4);
					}
				}
			});
			
		 
		 
	}
	
	
	
	public void SetImage(String URL){
		
		
		/*Picasso.with(this).load("http://cqra-qa.com/RFI/images/create_new1_40_12_34_7_319_1_Major_1406288908584_0.jpg").
		resize(50, 50).centerCrop().into(image1);*/
		
System.out.println("url in setimage"+URL);
		Picasso.with(this)
	    .load("https://bkw.orl.mybluehostin.me/RFI/images/"+URL)
	    .placeholder(R.drawable.loading_image)
	    .error(R.drawable.download_failed)
	    .into(image1);


	} 
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
}
