package com.example.bioskop1

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bioskop1.ui.theme.Bioskop1Theme
import java.io.Serializable

class TicketDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bioskop1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting4("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting4(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    Bioskop1Theme {
        Greeting4("Android")
    }
}

private fun <T : Serializable?> getSerializable(
    activity: Activity,
    name: String,
    clazz: Class<T>
): T? {
 return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
     activity.intent.getSerializableExtra(name, clazz)
    else
        activity.intent.getSerializableExtra(name)as  T
}