package com.example.bioskop1

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bioskop1.api.ApiRetrofit
import com.example.bioskop1.model.Movie
import com.example.bioskop1.model.MovieModel
import com.example.bioskop1.model.OrderModel
import com.example.bioskop1.model.ResponseModel
import com.example.bioskop1.model.Schedulee
import com.example.bioskop1.model.Seat
import com.example.bioskop1.model.SeatModel
import com.example.bioskop1.ui.theme.Bioskop1Theme
import getSerializable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.Serializable
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


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TickerOrderScreen(){
        val movie = remember { getSerializable(this, "movie", Movie::class.java)}
        var movieModel by remember { mutableStateOf<MovieModel?>(null) }
        val viewModel = remember { TicketOrderViewModel()}
        var seatModel by remember { mutableStateOf<SeatModel>(null) }

        LaunchedEffect(movie){
            movieModel = showMovie(movie.id)
        }

        LaunchedEffect(viewModel.selectedTime, viewModel.selectedDate){
            getSeat(viewModel)?.let {
                seatModel = it
            }
        }

        movieModel?.let { nonNullMoviewModel ->
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
            ){
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 42.dp)
                ) {
                    TicketOrderContent(nonNullMoviewModel, seatModel, viewModel)
                }

                CustomTopBar()

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .fillMaxWidth()
                ){
                    Text(
                        text = "Grand Total: ${Helper.currencyFormat(viewModel.selectedDate.count() * movie.price)}")
                    Button(
                        onClick = {
                            onFinish(viewModel)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        )
                    {
                        Text(text = "Finish", color = Color.White)
                    }
                }
            }
        } ?: run{
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ){
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 42.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Error: Error", color = Color.Red)
                }
            }
        }
    }

    private fun  onFinish(viewModel: TicketOrderViewModel){
        if (viewModel.selectedSeats.isNotEmpty()){
            var successfulApiCalls = 0
            val totalApiCalls = viewModel.selectedSeats.size

            api.createOrder().enqueue(object : Callback<OrderModel> {
                override fun onResponse(call: Call<OrderModel>, response: Response<OrderModel>) {
                    if (response.isSuccessful){
                        val order = response.body()?.order

                        if (order != null){
                            viewModel.selectedSeats.forEach{seat ->
                                api.createdOrderDetail(
                                    order.id,
                                    viewModel.selectedTime.toInt(),
                                    seat.id,
                                    SimpleDateFormat("yyyy-MM-dd",
                                        Locale.getDefault()).format(
                                            SimpleDateFormat(Helper.DATE_PATTERN,
                                                Locale.getDefault()).parse(viewModel
                                                .selectedDate)
                                        )
                                ).enqueue(object : Callback<ResponseModel> {
                                    override fun onResponse(
                                        call: Call<ResponseModel>,
                                        response: Response<ResponseModel>
                                    ) {
                                        if (response.isSuccessful){
                                            successfulApiCalls++

                                            if (successfulApiCalls == totalApiCalls){
                                                navigateToHomeActivity( viewModel)
                                            }
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ResponseModel>,
                                        t: Throwable
                                    ) {
                                        Log.e("onFailure", t.message.toString())
                                    }
                                })
                            }
                        } else {
                            Log.e("onFinish", "Received null order response")
                        }
                    } else {
                        Log.e("onFinish", "Failed to create order ")
                    }
                }

                override fun onFailure(call: Call<OrderModel>, t: Throwable) {
                    Log.e("onFailure", t.message.toString())
                }
            })
        } else {
            Helper.message("Fill all the required option", this)
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
    var selectedSeats by mutableStateOf<Set<Seat>>(setOf())

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TicketOrderContent(movieModel: MovieModel, seatModel: SeatModel?, viewModel: TicketOrderViewModel){
    val movie : Movie = movieModel.movie
    var comboBoxValue by remember { mutableStateOf(viewModel.selectedTime) }
    var isDropdownExpanded by remember{ mutableStateOf(false) }
    val dropdownValues: List<Schedulee> = movieModel.schedules

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        var model : Any = R.drawable.baseline_movie_24
        if (!movie.image.isNullOrEmpty()){
            model = "${Helper.BASE_IMAGE}${movie.image}"
        }

        AsyncImage(
            model = model,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        TicketOrderRow("Price", Helper.currencyFormat(movie.price))

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ){
            Column{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(12.dp)
                ){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Option",
                        style = MaterialTheme.typography.titleLarge
                            .copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                        textAlign = TextAlign.Center
                    )
                }

                ComboBox(
                    label = "Time",
                    selectedValue = comboBoxValue,
                    values = dropdownValues,
                    onValueChange = {
                        viewModel.selectedTime = it.id.toString()
                        comboBoxValue = "${it.start_time} - ${it.end_time}"
                        viewModel.selectedSeats = setOf()
                    },
                    isDropdownExpanded = isDropdownExpanded,
                    onToogleDropdown = {isDropdownExpanded = !isDropdownExpanded}
                )

                Column(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Screening Date", style = MaterialTheme.typography.bodyMedium)

                    MyDatePickerDialog(viewModel)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(12.dp)
                ){
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Selected Seat",
                        style = MaterialTheme.typography.titleLarge
                            .copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                        textAlign = TextAlign.Center
                    )
                }

                seatModel?.let { nonNullSeatModel->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .padding(top = 8.dp)
                    ){
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 60.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ){
                            itemsIndexed(nonNullSeatModel.seats){s, seat ->
                                SeatButton(
                                    text = "${s + 1}",
                                    seat = seat,
                                    selectedSeats = viewModel.selectedSeats,
                                    onSeatSeleected = { selectedSeat ->
                                        viewModel.selectedSeats = if (viewModel.selectedSeats.contains(selectedSeat)) {
                                            viewModel.selectedSeats - selectedSeat
                                        } else {
                                            viewModel.selectedSeats + selectedSeat
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketOrderRow(label: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ){
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
private fun CustomTopBar(){
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val isWideScreen = screenWidthDp > 600.dp

    val topBarHeight = if (isWideScreen) 110.dp else 50.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarHeight)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            ),
        contentAlignment = Alignment.TopCenter
    ){
        Text(
            text = "Ticket Order",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ComboBox(
    label: String,
    selecteValue: String,
    values: List<Schedulee>,
    onValueChange: (Schedulee) -> Unit,
    isDropDownExpanded: Boolean,
    onToogleDropDown: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                    .clickable { onToogleDropDown }
                    .padding(16.dp)
            ){
                BasicTextField(
                    value = selecteValue,
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onToogleDropDown()
                        }
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToogleDropDown() }
                        .padding(16.dp)
                )
                if (isDropDownExpanded){
                    DropdownMenu(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(4.dp)
                            ),
                        expanded = true,
                        onDismissRequest = { onToogleDropDown() }
                    ) {
                        values.forEach {value ->
                            DropdownMenuItem(
                                text = { Text(text = "${value.start_time} - ${value.end_time}", color = Color.Black) },
                                onClick = {
                                    onValueChange(value)
                                    onToogleDropDown()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyDatePickerDialog(viewModel: TicketOrderViewModel){
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    Box(contentAlignment = Alignment.Center){
        Button(onClick = {showDatePicker = true}) {
            Text(text = viewModel.selectedDate)
        }
    }
}

private fun <T : Serializable> getSerializable(activity: Activity, name: String, clazz: Class<T>): T
{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name, clazz)!!
    else
        activity.intent.getSerializableExtra(name) as T
}