package com.example.bioskop1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bioskop1.api.ApiRetrofit
import com.example.bioskop1.model.UserModel
import com.example.bioskop1.ui.theme.Bioskop1Theme
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {
    private  val api by lazy { ApiRetrofit().apiEndPoint }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Bioskop1Theme {

                val viewModel = remember { LoginViewModel() }

                viewModel.email = "ammar.fs2002@gmail.com"
                viewModel.password = "12345678"


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(viewModel = viewModel) {
                        login(viewModel)
                    }
                }
            }
        }
    }

    private fun login(viewModel: LoginViewModel){
        if (viewModel.email.isNullOrEmpty() || viewModel.password.isNullOrEmpty()){
            Helper.message("All field must be filled", this, false)
        }else if (!Patterns.EMAIL_ADDRESS.matcher(viewModel.email).matches()){
            Helper.message("Email format must be email", this, false)
        }else{
            api.login(
                viewModel.email,
                viewModel.password
            ).enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>){
                    if (!response.isSuccessful){
                        val errors = JSONObject(response.errorBody()!!.string())
                        Helper.message(errors.getString("errros"),
                            this@LoginActivity, false)
                    }else{
                        Helper.id = response.body()!!.user.id
                        Helper.name = response.body()!!.user.name
                        Helper.email = response.body()!!.user.email
                        Helper.token = response.body()!!.token
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    }
                }
                override fun onFailure(call: Call<UserModel>, t: Throwable){
                    Log.e("onFailure", t.message.toString())
                }
            })
        }
    }
}

class LoginViewModel {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel, onLoginClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(bottom = 42.dp, top = 70.dp)
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    // Handle the next action (e.g., move focus to the next field)
                }
            )
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Handle the next action (e.g., move focus to the next field)
                }
            ),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier =  Modifier.weight(1f))
        Button(onClick = {
            onLoginClick()
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    Bioskop1Theme {
        Greeting2("Android")
    }
}

