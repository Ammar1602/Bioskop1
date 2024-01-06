package com.example.bioskop1

import android.content.Intent
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
import com.example.bioskop1.model.MovieModel
import com.example.bioskop1.model.Seat
import com.example.bioskop1.model.SeatModel
import com.example.bioskop1.ui.theme.Bioskop1Theme
import retrofit2.awaitResponse
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.Locale

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

    private fun navigateToHomeActivity(viewModel: TicketOrderViewModel){
        val intent = Intent(this@TicketOrderActivity,
            HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
    private suspend fun  showMovie(id: Int): MovieModel?{
        return try {
            val response = api.showMovie(id = id).awaitResponse()
            if (response.isSuccessful){
                response.body()
            }else {
                null
            }
        }catch (e: Exception){
            null
        }
    }

    private suspend fun getSeat(viewModel: TicketOrderViewModel):SeatModel?{
        return try {
            val selectedTime: Int = try {
                viewModel.selectedTime.toInt()
            } catch (e: NumberFormatException){
                return null
            }

            val selectedDate: String? = try {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    SimpleDateFormat(Helper.DATE_PATTERN, Locale.getDefault())
                        .parse(viewModel.selectedDate)
                )
            } catch (e: Exception){
                return null
            }

            if  (selectedTime != 0 && selectedDate != null){
                val response = api.getSeat(id= selectedTime, date = selectedDate)
                    .awaitResponse()
                if (response.isSuccessful){
                    response.body()
                } else {
                    null
                }
            }else{
                null
            }
        } catch (e:Exception){
            null
        }
    }
}

class TicketOrderViewModel {
    var selectedTime by mutableStateOf("Select time schedule")
    var selectedDate by mutableStateOf("Open date picker dialog")
    var selectedSeats by mutableStateOf<Set<Seat>>(seatOf())

}