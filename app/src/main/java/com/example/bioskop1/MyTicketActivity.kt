package com.example.bioskop1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bioskop1.model.Ticket
import com.example.bioskop1.ui.theme.Bioskop1Theme

class MyTicketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bioskop1Theme {
                Column {
                    CustomTopBarr()
                    TicketGrid()
                }
            }
        }
    }

    @Composable
    private fun  TicketGrid(){
        var ticket by remember { mutableStateOf<List<Ticket>> (emptyList()) }

        LaunchedEffect(key1 = Unit){
            getTicket()?.let{
                ticket = it
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier.padding(16.dp)
        ){
            items(ticket){
                TicketCard(ticket = ticket)
            }
        }
    }

    private suspend fun getTicket():
}



@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    Bioskop1Theme {
        Greeting3("Android")
    }
}