package com.ob.rfi;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;*/
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
/*import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;*/
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ob.rfi.db.RfiDatabase;

public class UploadImages extends IntentService {

    // private static final String UPLOAD_URL =
    // "http://cqra-qa.com/audit_testing/Upload.php";
    //private static final String UPLOAD_URL = "http://cqra-qa.com/CQRAAUDIT1/Upload.php";
    //private static final String UPLOAD_URL = "http://192.168.1.158/CQRAAUDIT1/Upload.php";
//	private static final String UPLOAD_URL = "http://192.168.1.156/CQRAAUDIT1/Upload.php";

    //private static final String UPLOAD_URL = "http://cqra-qa.com/audit_testing/Upload.php";
    //private static final String UPLOAD_URL = "http://cqra-qa.com/CQRA/Upload.php";
    //private static final String UPLOAD_URL = "http://cqra-qa.com/RFI/Upload.php";

//    private static final String UPLOAD_URL = "http://cqra-qaqc.com/RFI/Upload.php";

    private static final String UPLOAD_URL = "https://bkw.orl.mybluehostin.me/RFI/Upload.php";
    // private static final String UPLOAD_URL = "http://durocrete-qa.com/DUROCRETE/Upload.php";
    // private static final String UPLOAD_URL =
    // "http://192.168.1.152/cqra/Upload.php";
    // private static final String UPLOAD_URL =
    // "http://192.168.1.161/CQRAAUDIT/Upload.php";
    public static final int NFID = 2928;
    private DefaultHttpClient mHttpClient;
    private NotificationManager nm;

    private Cursor cursor;
    private RfiDatabase db;
    private int count;
    private int timg;

    public UploadImages() {
        super("UploadImages");
    }

    @Override
    public void onHandleIntent(Intent intent) {

        createNotificationChannel();

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
                HttpVersion.HTTP_1_1);
        mHttpClient = new DefaultHttpClient(params);

		/*Intent notificationIntent = new Intent(getBaseContext(),
				HomeScreen.class);*/
        Intent notificationIntent = new Intent(getBaseContext(),
                HomeScreen.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                getBaseContext()).setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.app_icon).setAutoCancel(true)
                .setContentTitle("RFI Image Upload")
                .setContentText("Image Uploading Starting...");


        nm.notify(NFID, mBuilder.getNotification());


        db = new RfiDatabase(getApplicationContext());

        cursor = db.select("Images", "fileName", "status <>'"
                + RfiDatabase.UPLOADED + "'", null, null, null, null);
        count = 0;
        File f;
        String res;
        int t = cursor.getCount();

        if (cursor.moveToFirst()) {
            do {
                f = getFile(cursor.getString(0));
                if (f != null) {
                    System.out.println("inside loop");
                    Log.d(getClass().getName(), f.getAbsolutePath());
                    res = uploadUserPhoto(f);
                    Log.d("RSP", res);
                    if (res.length() > 10 && res.trim().endsWith(".jpg")) {
                        db.update("Images", "status='" + RfiDatabase.UPLOADED
                                + "'", "fileName='" + res + "'");
//                        Toast.makeText(getApplicationContext(), count + " of " + t + " Images if Uploaded.", Toast.LENGTH_LONG).show();

                        count++;
//						Toast.makeText(getApplicationContext(), count + "/" + cursor.getCount()+" Images Uploaded.", Toast.LENGTH_LONG).show();


                    }
                } else {
                    System.out.println("uploadedddddddddddddddddddddddd");
                    db.update("Images", "status='" + RfiDatabase.UPLOADED
                            + "'", "fileName='" + cursor.getString(0) + "'");
                }

                updateNotification(count + "/" + cursor.getCount());
            } while (cursor.moveToNext());
        }

//        db.isUploaded=true;
//        db.imgUploaded = count+" of "+t+" Images Uploaded";
//		AlertDialog.Builder builder = new AlertDialog.Builder(UploadImages.this);
//		builder.setMessage(db.imgUploaded)
//				.setTitle("Image Upload")
//				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialogInterface, int i) {
//						//set what should happen when negative button is clicked
//						Toast.makeText(getApplicationContext(), "Nothing Happened", Toast.LENGTH_LONG).show();
//					}
//				});//).show();
//		AlertDialog alert11 = builder.create();
//		alert11.show();
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.closeDb();
        stopSelf();
    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.NEWS_CHANNEL_ID),getString(R.string.CHANNEL_NEWS), NotificationManager.IMPORTANCE_DEFAULT );
            notificationChannel.setDescription(getString(R.string.CHANNEL_DESCRIPTION));
            notificationChannel.setShowBadge(true);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void updateNotification(String perecent) {
        String CHANNEL_ID = "my_channel_01";// The id of the channel.

        Intent intent = new Intent(this, UploadImages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.NEWS_CHANNEL_ID))
                .setSmallIcon(R.drawable.app_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_icon_large))
                .setContentTitle("RFI Image Upload")
                .setContentText(perecent + "Uploaded (Failed " + ((cursor.getPosition() + 1) - count) + ")")
//                .setStyle(new NotificationCompat.BigTextStyle().bigText("perecent + \"Uploaded (Failed \"\n" + " + ((cursor.getPosition() + 1) - count) + \")\""))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setChannelId(getString(R.string.NEWS_CHANNEL_ID))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(getResources().getInteger(R.integer.notificationId), builder.build());

////        Toast.makeText(this, perecent + " Images Uploaded.", Toast.LENGTH_LONG).show();
//        Intent notificationIntent = new Intent(getBaseContext(),
//                HomeScreen.class);
//        notificationIntent.putExtra("key", perecent + "Uploaded (Failed "
//                + (cursor.getPosition() - (count - 1)) + ")");
//        PendingIntent contentIntent = PendingIntent.getActivity(
//                getBaseContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                getBaseContext(), NOTIFICATION_CHANNEL_ID)
////                .setChannelId(CHANNEL_ID)
//                .setContentIntent(contentIntent)
//                .setAutoCancel(true)
//                .setSmallIcon(R.drawable.app_icon)
//                .setContentTitle("RFI Image Upload")
//                .setContentText(
//                        perecent + "Uploaded (Failed "
//                                + ((cursor.getPosition() + 1) - count) + ")");
//
//        System.out.println("Count Image++++++++++++++++=" + count);
//        System.out.println("Cursor ++++++++++++++++=" + cursor.getPosition());
//        System.out.println("Count ++++++++++++++++=" + cursor.getCount());
//
//        nm.notify(NFID, mBuilder.build());
    }

    public String uploadUserPhoto(File image) {

        try {
            // Log.d(this.getClass().getName(), UPLOAD_URL);
            HttpPost httppost = new HttpPost(UPLOAD_URL);

            MultipartEntity multipartEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("upload", new StringBody("Upload"));
            // multipartEntity.addPart();

            multipartEntity.addPart(new FormBodyPart("image", new FileBody(
                    image)));
            httppost.setEntity(multipartEntity);
            Log.e("image data", ">>>>>" + httppost.toString());

            HttpResponse httpResponse = mHttpClient.execute(httppost);
            Log.d(this.getClass().getName(), httpResponse.getStatusLine()
                    .toString());

            String respons = readStream(httpResponse.getEntity().getContent());
            // Log.d(this.getClass().getName(),
            // respons+"="+httpResponse.getHeaders("Content-Length"));
            return respons;
        } catch (Exception e) {
            Log.d("#######" + this.getClass().getName(),
                    e.getLocalizedMessage());
        }
        return "noImg";
    }

    private String readStream(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in));
            String line = "";
            String s = "";
            while ((s = reader.readLine()) != null) {
                line += s;
            }
            return line;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(this.getClass().getName(), e.getMessage());
        }
        return null;
    }

    private File getFile(String filename) {
        String dir = /*Environment.getExternalStorageDirectory()*/null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            dir = getExternalFilesDirs(null)[0]
                    + File.separator + "RFI" + File.separator + "temp" + File.separator;
        }

//				Environment.getExternalStorageDirectory() + File.separator+ "RFI";
        Log.d(getClass().getName(), dir);
        File file = new File(dir + "/" + filename.toString().trim());
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }
}
