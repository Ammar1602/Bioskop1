package com.example.bioskop1

import android.app.Activity
import android.app.AlertDialog
import androidx.core.app.NotificationCompat.MessagingStyle.Message
import java.text.NumberFormat
import java.util.Locale

object Helper {
    var id: Int = 0
    var name: String = ""
    var usernmae: String = ""
    var email: String = ""
    var token: String = ""
    var total: Double = 0.0

    val BASE_URL = "http://26.235.165.119/bioskop_api/public/api"
    val BASE_IMAGE = "http://26.235.165.119/bioskop_api/public/images"
    val DATE_PATTERN = "EEEE, dd MMM yyyy"

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

fun currencyFormat(price: Double): String{
    return NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(price)
}

}