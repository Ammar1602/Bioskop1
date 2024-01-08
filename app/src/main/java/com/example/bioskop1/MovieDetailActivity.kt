import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Movie
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.bioskop1.Helper
import com.example.bioskop1.R
import com.example.bioskop1.ui.theme.Bioskop1Theme
import kotlinx.coroutines.channels.TickerMode
import java.io.Serializable

class MovieDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movie =  getSerializable(this, "movie", Movie::class.java)
        setContent {
            Bioskop1Theme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ){
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 42.dp)
                    ) {
                        MovieDetailContent(movie)
                    }

                    CustomTopBar()
                    Button(onClick = {
                        val intent = Intent(
                            this@MovieDetail

                        )
                    }) {

                    }
                }
            }
        }
    }
}
@Composable
private fun MovieDetailContent(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        var model: Any = R.drawable.baseline_movie_24
        if (!movie.image.isNullOrEmpty()){
            model = "${Helper.BASE_IMAGE}${movie.image}"
        }

        AsyncImage(model = model,
            contentDescription =null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()

        )

        Text(text = movie.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        MovieDetailRow(label = "Duration", value =movie.duration() )
        MovieDetailRow(label = "Release Date", value =movie.releasedate )
        MovieDetailRow(label = "Genre", value =movie.genre )
        MovieDetailRow(label = "price", value = Helper.currencyFormat(movie.price) )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
        ) {

            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(15.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Descripction",
                        style = MaterialTheme.typography.titleLarge
                            .copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.WHITE
                            ),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    text = movie.descripction,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 8.dp, end = 8.dp),

                    textAlign = TextAlign.Justify
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp)
        )
    }
}

@Composable
private fun MovieDetailRow(label:String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ){
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }

}
@Composable
private fun CustomTopBar(){
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val isWideScreen = screenWidthDp.dp > 600.dp

    val topBarHeight = if (isWideScreen)118.dp else 50.dp

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
            text = "Movie Detai",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.WHITE
            ),
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxSize(),
            textAlign = TextAlign.Center
        )
    }
}

fun <t : Serializable?> getSerializable(activity: Activity, name: String, clazz: Class<Movie>): t
{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        activity.intent.getSerializableExtra(name,clazz)!!
    else
        activity.intent.getSerializableExtra(name) as t
}