package com.ob.rfi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomeImageProcessing extends Activity implements OnClickListener, OnTouchListener {


    ImageView choosenImageView;
    Button choosePicture;
    Button savePicture;

    Bitmap bmp;
    Bitmap alteredBitmap;
    Canvas canvas;
    Paint paint;
    Matrix matrix;
    float downx = 0;
    float downy = 0;
    float upx = 0;
    float upy = 0;
    private String uriPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custome_image_process);

        choosenImageView = (ImageView) this.findViewById(R.id.ChoosenImageView);
        choosePicture = (Button) this.findViewById(R.id.ChoosePictureButton);
        savePicture = (Button) this.findViewById(R.id.SavePictureButton);

        Intent int1 = getIntent();
        uriPath = int1.getStringExtra("uri");
        processImage(uriPath);


        savePicture.setOnClickListener(this);
        choosePicture.setOnClickListener(this);
        choosenImageView.setOnTouchListener(this);

    }
/*
	  public void saveImage(){
		  if (alteredBitmap != null) {
	           ContentValues contentValues = new ContentValues(3);
	           contentValues.put(Media.DISPLAY_NAME, "Draw On Me");

	           Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
	           try {
	             OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
	             alteredBitmap.compress(CompressFormat.JPEG, 90, imageFileOS);
	             Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
	             t.show();

	           } catch (Exception e) {
	             Log.v("EXCEPTION", e.getMessage());
	           }
	  
	  }
	
	*/


    public void processImage(String uri) {
        Uri imageFileUri = Uri.parse("file://" + uri);
        try {

            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            // bmpFactoryOptions.inSampleSize=8;
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                    imageFileUri), null, bmpFactoryOptions);
            System.out.println("balaji-------dsfsfgsfgdf-----------" + imageFileUri);
            bmpFactoryOptions.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                    imageFileUri), null, bmpFactoryOptions);


            Display currentDisplay = getWindowManager().getDefaultDisplay();
            float dw = currentDisplay.getWidth();
            float dh = currentDisplay.getHeight();
	        
	         /*  bitmap = Bitmap.createBitmap((int) dw, (int) dh,
	               Bitmap.Config.ARGB_8888);
	           canvas = new Canvas(bitmap);
	           paint = new Paint();
	           paint.setColor(Color.GREEN);
	           imageView.setImageBitmap(bitmap);
	        
	           imageView.setOnTouchListener(this);
	        */

            /*

             *
             */


            alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                    .getHeight(), bmp.getConfig());

            canvas = new Canvas(alteredBitmap);
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            matrix = new Matrix();
            canvas.drawBitmap(bmp, matrix, paint);

            choosenImageView.setImageBitmap(alteredBitmap);
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%5");
            choosenImageView.setOnTouchListener(this);
        } catch (Exception e) {
            Log.v("ERROR", e.toString());
        }


    }

    public void onClick(View v) {

        if (v == choosePicture) {
            Intent choosePictureIntent = new Intent(
                    Intent.ACTION_PICK,
                    Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(choosePictureIntent, 0);
        } else if (v == savePicture) {

            if (alteredBitmap != null) {
                ContentValues contentValues = new ContentValues(3);
                contentValues.put(Media.DISPLAY_NAME, "Draw On Me");

                System.out.println("path--------------------" + Media.EXTERNAL_CONTENT_URI);
               // Uri imageFileUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
                //  Uri imageFileUri = getContentResolver().insert( Uri.parse("file:/"+uriPath), contentValues);

                try {
                    saveImage();
//                    OutputStream imageFileOS = getContentResolver().openOutputStream(imageFileUri);
//                    alteredBitmap.compress(CompressFormat.JPEG, 90, imageFileOS);
                    Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
                    t.show();
                    onBackPressed();

                } catch (Exception e) {
                    Log.v("EXCEPTION", e.getMessage());
                }
            }
        }
    }


    public void saveImage() {

        String[] str = uriPath.split("/");
        String filenmae = str[str.length - 1];
        System.out.println("file name============" + filenmae);


        File root = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            root = new File(/*Environment.getExternalStorageDirectory()*/getExternalFilesDirs(null)[0]
                    + File.separator + "RFI" + File.separator + "temp");
        }else {
            root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "RFI" + File.separator + "temp");
        }


        // File myDir = new File(root + "/saved_images");
        root.mkdirs();
        //   Random generator = new Random();
        int n = 10000;
        // n = generator.nextInt(n);
        //String fname = "Image-"+ n +".jpg";
       // File file = new File(root, filenmae);
        File file = StorageUtils.getTempStorageLocationFile(this,filenmae);


        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            alteredBitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            System.out.println("file successfulyy added-----------------");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Uri imageFileUri = intent.getData();
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&=" + imageFileUri);
            try {
                BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
                bmpFactoryOptions.inJustDecodeBounds = true;
                bmpFactoryOptions.inSampleSize = 8;
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                bmpFactoryOptions.inJustDecodeBounds = false;
                bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(
                        imageFileUri), null, bmpFactoryOptions);

                alteredBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp
                        .getHeight(), bmp.getConfig());
                canvas = new Canvas(alteredBitmap);
                paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(5);
                matrix = new Matrix();
                canvas.drawBitmap(bmp, matrix, paint);

                choosenImageView.setImageBitmap(alteredBitmap);
                choosenImageView.setOnTouchListener(this);
            } catch (Exception e) {
                Log.v("ERROR", e.toString());
            }
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                upx = event.getX();
                upy = event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                choosenImageView.invalidate();
                downx = upx;
                downy = upy;
                break;
            case MotionEvent.ACTION_UP:
                upx = event.getX();
                upy = event.getY();
                canvas.drawLine(downx, downy, upx, upy, paint);
                choosenImageView.invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:

                break;
        }
        return true;
    }

}