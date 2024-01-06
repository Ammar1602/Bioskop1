package com.example.bioskop1

import android.app.Activity
import android.app.AlertDialog
import androidx.core.app.NotificationCompat.MessagingStyle.Message

object Helper {
    var id: Int = 0
    var name: String = ""
    var usernmae: String = ""
    var email: String = ""
    var token: String = ""
    var total: Double = 0.0

    val BASE_URL = "http://26.235.165.119/bioskop_api/public/api"

    fun message(message: String, activity: Activity, action: Boolean = false){
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("Message")
            .setMessage(message)
            .setPositiveButton("Ok"){dialog, which ->
                if (action){
                    activity.finish()
                }
            }
        alertDialog.show()
    }



}