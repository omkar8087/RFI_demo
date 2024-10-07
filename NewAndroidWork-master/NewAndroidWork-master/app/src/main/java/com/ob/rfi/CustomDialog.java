package com.ob.rfi;
/*package com.ob.cqra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressWarnings("unchecked")
public class CustomDialog extends Dialog implements OnTouchListener{

	private int id = 0;
	private String title="";
	private Bitmap bitmap = null;
	private ImageView imageView;
	private ImageView leftImage;
	private ImageView rightImage;
	private int index=0;
	public int bmpWidth, bmpHeight;
	private Bitmap[] allImages;
	private ProgressBar progressBar;

	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;

	static final int DIRECTION_DIALOG = 1;
	static final int IMAGE_DIALOG = 2;
	static final int HISTORY_DIALOG = 3;

	private String[] h_snap;
	private Bitmap image;

//	public CustomDialog(Context context, String selectedDirections ,int dialogId, String title) {
//		super(context);
//		this.ctx = context;
//		this.directions = selectedDirections;
//		this.id = dialogId;
//		this.title = title;
//	}

	public CustomDialog(Context context,int dialogId, String title) {
		super(context);
//		this.ctx = context;
		this.id = dialogId;
		this.title = title;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if(id == HISTORY_DIALOG){

			setContentView(R.layout.dialogforhistory);
			((TextView)findViewById(R.id.h_imageDialogText)).setText(title);
			imageView=(ImageView) findViewById(R.id.h_image_display);
			progressBar = (ProgressBar)findViewById(R.id.progressBar);

			rightImage=(ImageView) findViewById(R.id.h_rightImage);
			leftImage=(ImageView) findViewById(R.id.h_leftImage);

			if(Questionnaire.h_snaptemp.length() > 0){
				
				imageView.setOnClickListener(new CancelListener());
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setOnTouchListener(this); 

				if(Questionnaire.h_snaptemp.length() > 0){
					Questionnaire.h_snaptemp = Questionnaire.h_snaptemp.substring(0, Questionnaire.h_snaptemp.length()-1);
					h_snap = customsplit(Questionnaire.h_snaptemp.trim(), ',');
					Questionnaire.h_snaptemp = "";
				}
				
				ChangeImageHistory(index);

				rightImage.setOnClickListener(new RightListener());
				leftImage.setOnClickListener(new LeftListener());

				imageView.setVisibility(View.VISIBLE);
				rightImage.setVisibility(View.VISIBLE);
				leftImage.setVisibility(View.VISIBLE);

				showButton();
			} else {
				progressBar.setVisibility(View.GONE);
				imageView.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
				leftImage.setVisibility(View.GONE);
			}
		}
	}
	
	private String[] customsplit(String string, char separator) {
		String[] result = {};
		if(string.length() > 0){
		char[] array = string.toCharArray();
		StringBuffer stringBuffer = new StringBuffer();
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < array.length; i++) {
			if(array[i]== separator){ 
				list.add(stringBuffer.toString());  
				stringBuffer = new StringBuffer();
			}else{
				stringBuffer.append(array[i]); 
			}
		}
		
		list.add(stringBuffer.toString());
		result = new String[list.size()];
		int i=0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String string3 = (String) iterator.next();
			result[i] = string3;
			i++;
		}
		}else {
			result = new String[]{};
		}
		return result;
	}

	private void displayImage(String imageName){
		rightImage.setClickable(false);
		leftImage.setClickable(false);
		String dir = Environment.getExternalStorageDirectory() + File.separator + "CQRA" + File.separator;
		
		File file = new File(dir+"/"+imageName.toString().trim());
		if(!file.exists()){

		}else{ 
			File images = Environment.getExternalStorageDirectory();  
			String path = images.toString();
			path = dir+"/"+imageName.toString().trim();
			FileInputStream in = null;
			image = null;

			byte[] b = new byte[(int) file.length()];
			try {
				in = new FileInputStream(new File(path));
				in.read(b);
				in.close();
				image = BitmapFactory.decodeByteArray(b, 0, b.length);
			}
			catch (FileNotFoundException e) {}
			catch (OutOfMemoryError e) {}
			catch (IOException e1) {}

			bitmap = null;
			if(image != null){
				Thread.currentThread().interrupt();
				progressBar.setVisibility(View.GONE);
				imageView.setVisibility(View.VISIBLE);
				bitmap = image;
				imageView.setImageBitmap(bitmap);
				bmpWidth = bitmap.getWidth();
				bmpHeight = bitmap.getHeight();
			}
			image = null;
		}
		rightImage.setClickable(true);
		leftImage.setClickable(true);
	}

//	private class OKListener implements android.view.View.OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//
//			ConQADatabase.selectedDirections = "";
//			if(((CheckBox)findViewById(R.id.NORTH)).isChecked()){
//				ConQADatabase.selectedDirections += "N"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.SOUTH)).isChecked()){
//				ConQADatabase.selectedDirections += "S"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.WEST)).isChecked()){
//				ConQADatabase.selectedDirections += "W"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.EAST)).isChecked()){
//				ConQADatabase.selectedDirections += "E"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.NORTHEAST)).isChecked()){
//				ConQADatabase.selectedDirections += "NE"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.NORTHWEST)).isChecked()){
//				ConQADatabase.selectedDirections += "NW"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.SOUTHEAST)).isChecked()){
//				ConQADatabase.selectedDirections += "SE"+seperator;					
//			}
//			if(((CheckBox)findViewById(R.id.SOUTHWEST)).isChecked()){
//				ConQADatabase.selectedDirections += "SW"+seperator;					
//			}
//
//			CustomDialog.this.dismiss();
//		}
//	}

	private class CancelListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			CustomDialog.this.dismiss();
		}
	}

	private class LeftListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
			if(id == HISTORY_DIALOG){
				if(index==0){}
				else{
					index--;
					ChangeImageHistory(index);
				}
				showButton();
			} else {
				if(index==0){}
				else{
					index--;
					ChangeImage(index);
				}
				showButton();
			}
		}
	}
	/// ===============================================

	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		view.setScaleType(ImageView.ScaleType.MATRIX);
		float scale = 0;

		// Dump touch event to log
		//		dumpEvent(event);

		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: //first finger down only
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
			
		case MotionEvent.ACTION_UP: //first finger lifted
			
		case MotionEvent.ACTION_POINTER_UP: //second finger lifted
			mode = NONE;
			break;
			
		case MotionEvent.ACTION_POINTER_DOWN: //second finger down
			oldDist = spacing(event);
			if (oldDist > 5f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;

		case MotionEvent.ACTION_MOVE: 
			if (mode == DRAG) { //movement of first finger
				matrix.set(savedMatrix);
				if (view.getLeft() >= -392){
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				}
			}
			else if (mode == ZOOM) { //pinch zooming
				float newDist = spacing(event);
				if (newDist > 5f) {
					matrix.set(savedMatrix);
					scale = newDist / oldDist; //thinking i need to play around with this value to limit it**
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		return true; 
	}


	//    public void setImage(Bitmap bm, int displayWidth, int displayHeight , PinchZoomExample pze) { 
	//        super.setImageBitmap(bm);
	//        sCurrentImage = bm;
	//        sPinchZoomExample = pze;
	//        //Fit to screen.
	//        float scale;
	//        if ((displayHeight / bm.getHeight()) >= (displayWidth / bm.getWidth())){
	//            scale =  (float)displayWidth / (float)bm.getWidth();
	//        } else {
	//            scale = (float)displayHeight / (float)bm.getHeight();
	//        }
	//
	//        savedMatrix.set(matrix);
	//        matrix.set(savedMatrix);
	//        matrix.postScale(scale, scale, mid.x, mid.y);
	//        setImageMatrix(matrix);
	//
	//        // Center the image
	//        float redundantYSpace = (float)displayHeight - (scale * (float)bm.getHeight()) ;
	//        float redundantXSpace = (float)displayWidth - (scale * (float)bm.getWidth());
	//
	//        redundantYSpace /= (float)2;
	//        redundantXSpace /= (float)2;
	//
	//        savedMatrix.set(matrix);
	//        matrix.set(savedMatrix);
	//        matrix.postTranslate(redundantXSpace, redundantYSpace);   //matrix.postTranslate(50, 50);
	//        setImageMatrix(matrix);
	//    }


	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	*//**	*//** Show an event in the LogCat view, for debugging *//**
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
				"POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_" ).append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid " ).append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")" );
		}
		sb.append("[" );
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#" ).append(i);
			sb.append("(pid " ).append(event.getPointerId(i));
			sb.append(")=" ).append((int) event.getX(i));
			sb.append("," ).append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";" );
		}
		sb.append("]" );
		Log.d(TAG, sb.toString());
	}*//*

	/// ===============================================
	private class RightListener implements android.view.View.OnClickListener {
		@Override
		public void onClick(View v) {
//			scaleindex = 0;
			if(id == HISTORY_DIALOG){
				if(index < h_snap.length-1){
					index++;
					ChangeImageHistory(index); 
				}
				else{}
			}else{
				if(index < allImages.length-1){
					index++;
					ChangeImage(index); 
				}
				else{}
			}
			showButton();
		}
	}

	protected void showButton() {
		if(id == HISTORY_DIALOG){
			if(index == 0){
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.VISIBLE);
			}else if (index == h_snap.length-1){
				rightImage.setVisibility(View.GONE);
				leftImage.setVisibility(View.VISIBLE);
			}else{
				rightImage.setVisibility(View.VISIBLE);
				leftImage.setVisibility(View.VISIBLE);
			}

			if(h_snap.length == 1){
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		} else {

			if(index == 0){
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.VISIBLE);
			}else if (index == allImages.length-1){
				rightImage.setVisibility(View.GONE);
				leftImage.setVisibility(View.VISIBLE);
			}else{
				rightImage.setVisibility(View.VISIBLE);
				leftImage.setVisibility(View.VISIBLE);
			}

			if(allImages.length == 1){
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		}
	}

	public void ChangeImageHistory(int index){  
		imageView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		for (int j = 0; j < h_snap.length; j++) {
			if(h_snap[j].length() > 0){
				if(j == index){
					displayImage(h_snap[j]);
				}
			}
		}
	}
	
	public void ChangeImage(int index){  
		bitmap = allImages[index];
		imageView.setImageBitmap(bitmap);
		bmpWidth = bitmap.getWidth();
		bmpHeight = bitmap.getHeight();
	}

	//	private class ZoomOutListener implements android.view.View.OnClickListener {
	//	@Override
	//	public void onClick(View v) {
	//		Zoomout();
	//	}
	//}
	//
	//private class ZoomInListener implements android.view.View.OnClickListener {
	//	@Override
	//	public void onClick(View v) {
	//		Zoomin();
	//	}
	//}

	//protected void Zoomin() {
	//	if(scaleindex < 1){
	//		scaleindex++;
	//		Scale(scaleindex);
	//	}else{}
	//}
	//
	//protected void Zoomout() {
	//	if(scaleindex==0){}else{
	//		scaleindex--;
	//		Scale(scaleindex);
	//	}
	//}
	//	public void Scale(int scale ){
	//		scaleindex1 = floatScale[scale];
	//		Matrix matrix = new Matrix();
	//		matrix.postScale(scaleindex1, scaleindex1);
	//		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);
	//		imageView.setImageBitmap(resizedBitmap);
	//	}
}
*/