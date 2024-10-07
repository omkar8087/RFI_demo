package com.ob.rfi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.ob.rfi.db.RfiDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayNotificationActivity extends CustomTitle {
    private SharedPreferences checkPreferences;
    private RfiDatabase db;
    private String userID="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_display);
        db = new RfiDatabase(this);
        init("");

    }

    private void init(String notificationText) {

        Intent intent = getIntent();
        if (intent != null && intent.getExtras()!=null) {
            notificationText = intent.getExtras().getString("data");
           userID=notificationText.split("\\$")[0];
           if(db.userId.equalsIgnoreCase("")){
               db.userId=userID;
            }
            notificationText=notificationText.split("\\$")[1];
        }else {
           // userID=db.userId;
        }
        TextView notificationTextView = (TextView) findViewById(R.id.notif_text_id);
        TextView clearNotificationTextView = (TextView) findViewById(R.id.clear_notif_text_id);
        clearNotificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllNotification();
            }
        });

        StringBuilder stringBuilder = new StringBuilder();
        List<String> notificationList = getNotificationFromDB(notificationText);
        if (notificationList.size()==1 && notificationList.get(0).isEmpty()){
            clearNotificationTextView.setVisibility(View.GONE);
            notificationTextView.setText("");
        }else {
            clearNotificationTextView.setVisibility(View.VISIBLE);
            notificationTextView.setText("Notification not available");
        }
        int count=0;
        for (int i = 0; i < notificationList.size(); i++) {
            if (!notificationList.get(i).isEmpty()){
                stringBuilder.append((count + 1) + ") " + notificationList.get(i) + "\n\n");
                count++;
            }
            if (i == 50) {
                break;
            }
        }
        notificationTextView.setText(stringBuilder.toString());
        notificationTextView.setMovementMethod(new ScrollingMovementMethod());
        saveNotification(notificationText.replace("'",""));
    }


    public ArrayList<String> getAllNotifications(String latestNotification) {
        Gson gson = new Gson();
        checkPreferences = getSharedPreferences("RFI_File",
                MODE_PRIVATE);
        String jsonText = checkPreferences.getString("rfiNotif", null);
        SharedPreferences.Editor editor = checkPreferences.edit();

        ArrayList<String> wordList = new ArrayList<>();
        if (jsonText != null) {
            String[] text = gson.fromJson(jsonText, String[].class);
            wordList = new ArrayList<String>(Arrays.asList(text)); /*Arrays.asList(text);*/
        }

        wordList.add(0, latestNotification);
//Set the values
        String jsonDataToSave = gson.toJson(wordList);
        editor.putString("rfiNotif", jsonDataToSave);
        editor.commit();

        return wordList;
    }


    public ArrayList<String> getNotificationFromDB(String latestNotification) {
//        if (userID.isEmpty()){
//            userID=db.userId;
//        }
        String columns = "notifId,notifText,user_id";
        String where = "user_id = '" + userID + "'";
        Cursor cursor = db.select("RfiNotification", columns, where, null, null, null, null);
        ArrayList<String> notificationTextList = new ArrayList<>();
        notificationTextList.add(latestNotification);
        if (!latestNotification.isEmpty()) {

        }
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
               notificationTextList.add((cursor.getString(cursor.getColumnIndex("notifText"))).toString().replaceAll("~","\\n"));
            } while (cursor.moveToNext());

        } else {
            Log.d("Notification ", "Notification not found in DB");
        }
        try {
            db.closeDb();
        } catch (Exception e) {
            Log.e("Notification ", e.getLocalizedMessage());
        }
        return notificationTextList;
    }


    public void saveNotification(String notificationText) {
        if (notificationText.isEmpty()) {
            return;
        }
//        db = new RfiDatabase(this);
//        if (userID.isEmpty()){
//            userID=db.userId;
//        }
        db.insert("RfiNotification", "notifText,user_id",
                "'" + notificationText + "','" + userID + "'");
        try {
            db.closeDb();
        } catch (Exception e) {
            Log.e("Notification ", e.getLocalizedMessage());
        }
    }

    public void deleteAllNotification(){
        db = new RfiDatabase(this);
        try {
            db.delete("RfiNotification", "user_id='" + userID + "'");
            db.closeDb();
        } catch (Exception e) {
            Log.e("Notification ", e.getLocalizedMessage());
        }
    }
}
