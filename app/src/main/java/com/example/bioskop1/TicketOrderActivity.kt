package com.example.bioskop1

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.bioskop1.api.ApiRetrofit
import com.example.bioskop1.model.Seat
import com.example.bioskop1.ui.theme.Bioskop1Theme

class TicketOrderActivity : ComponentActivity(){
    private  val api by lazy { ApiRetrofit().apiEndPoint }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Bioskop1Theme {

            }
        }
    }
}

class TicketOrderViewModel {
    var selectedTime by mutableStateOf("Select time schedule")
    var selectedDate by mutableStateOf("Open date picker dialog")
    var selectedSeats by mutableStateOf<Set<Seat>>(seatOf())

}