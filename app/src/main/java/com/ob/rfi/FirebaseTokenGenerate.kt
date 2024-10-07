package com.ob.rfi

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ob.rfi.AppConstants.FIREBASE_TOKEN
import java.io.IOException

object FirebaseTokenGenerate {

     fun getToken() {

        Thread(Runnable {
            try {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val newToken = task.result
                    FIREBASE_TOKEN= task.result
                    println("Token --> $newToken")
                })


                /* val newToken = FirebaseInstanceId.getInstance()
                     .getToken(senderID, "FCM")
                 println("Token --> $newToken")*/

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }
}