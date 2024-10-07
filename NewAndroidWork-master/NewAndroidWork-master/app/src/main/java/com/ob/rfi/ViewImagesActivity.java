package com.ob.rfi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.squareup.picasso.Picasso;

public class ViewImagesActivity extends Activity {
    private static final String LOG_TAG = "ViewImageActivity";
    private static final int ACTION_REQUEST_FEATHER = 100;
    private static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;
    private static final String API_KEY = "yLCHPH3suku3eCw2czMaAA";
    private String[] imageNames;
    ImagePagerAdapter adapter;
    ViewPager viewPager;
    private String mSessionId;
    String mOutputFilePath;
    Uri mImageUri;

    int imageWidth, imageHeight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_images);
        setTitle("RFI Images");
        imageNames = getIntent().getStringArrayExtra("data");

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

    }


    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageNames.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = ViewImagesActivity.this;
            ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageBitmap(readImage(imageNames[position]));
            //((ViewPager) container).addView(imageView, 0);

            ((ViewPager) container).addView(imageView, 0);
//            myFunction();


            return imageView;


        }
        public void myFunction()
        {
            if (isExternalStorageAvilable()) {
                String dir = Environment.getExternalStorageDirectory()
                        + File.separator + "RFI" + File.separator + "temp" + File.separator
                        + imageNames;
                System.out.println("++++++++++++++++" + dir);
                //startFeather(Uri.parse("file://" + dir));
                if (getAPILevel() > 20) {
                    Intent int1 = new Intent(ViewImagesActivity.this, CustomeImageProcessing.class);
                    int1.putExtra("uri", dir);
                    startActivity(int1);
                    System.out.println("custome image processing--------------" + dir);
                } else {
                    System.out.println("android feather started--------------");
                    //startFeather(Uri.parse("file://" + dir));
                }
            }
        }
        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

    private Bitmap readImage(String imageName) {
        Bitmap image = null;
        try {
            File dir = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                dir = new File(/*Environment.getExternalStorageDirectory()*/getExternalFilesDirs(null)[0]
                        + File.separator + "RFI" + File.separator + "temp");
            }else {
                dir = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "RFI" + File.separator + "temp");
            }





            File file = new File(dir + "/" + imageName.toString().trim());
            if (!file.exists()) {
                // updateImages();
                return image;
            } else {
                String path ;
                path = dir +"/"+ imageName.toString().trim();
                FileInputStream in = null;

                byte[] b = new byte[(int) file.length()];
                in = new FileInputStream(new File(path));
                in.read(b);
                in.close();
                image = BitmapFactory.decodeByteArray(b, 0, b.length);
                return image;
            }
        } catch (FileNotFoundException e) {
            // updateImages();

        } catch (OutOfMemoryError e) {
        } catch (IOException e1) {
            // displayDialog("Error", "File not found");
        }
        return image;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add("Edit Image");
        // TODO Auto-generated method stub
        return true;
    }


    public static int getAPILevel() {
        return Build.VERSION.SDK_INT;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // TODO Auto-generated method stub
//        if (isExternalStorageAvilable()) {
//            String dir = Environment.getExternalStorageDirectory()
//                    + File.separator + "RFI" + File.separator
//                    + imageNames;
//            System.out.println("++++++++++++++++" + dir);
//            //startFeather(Uri.parse("file://" + dir));
//            if (getAPILevel() > 20) {
//                Intent int1 = new Intent(ViewImagesActivity.this, CustomeImageProcessing.class);
//                int1.putExtra("uri", dir);
//                startActivity(int1);
//                System.out.println("custome image processing--------------" + dir);
//            } else {
//                System.out.println("android feather started--------------");
//                //startFeather(Uri.parse("file://" + dir));
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case ACTION_REQUEST_FEATHER:

                    // send a notification to the media scanner
                    // updateMedia( mOutputFilePath );

                    // update the preview with the result
                    // loadAsync( data.getData() );
                    onSaveCompleted(mOutputFilePath);
                    mOutputFilePath = null;
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case ACTION_REQUEST_FEATHER:

                    // feather was cancelled without saving.
                    // we need to delete the entire session
                    if (null != mSessionId)
                        //deleteSession(mSessionId);

                        // delete the result file, if exists
                        if (mOutputFilePath != null) {
                            deleteFileNoThrow(mOutputFilePath);
                            mOutputFilePath = null;
                        }
                    break;
            }
        }
    }


    private boolean isExternalStorageAvilable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean deleteFileNoThrow(String path) {
        File file;
        try {
            file = new File(path);
        } catch (NullPointerException e) {
            return false;
        }

        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void onSaveCompleted(final String filepath) {

        if (mSessionId != null) {
            //processHD(mSessionId);
        } else {
            mSessionId = null;
        }
    }


}