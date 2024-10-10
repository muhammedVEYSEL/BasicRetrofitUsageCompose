package com.veys.retrofitcomposekotlin.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.veys.retrofitcomposekotlin.api.CryptoAPI
import com.veys.retrofitcomposekotlin.model.CryptoModel
import com.veys.retrofitcomposekotlin.ui.theme.RetrofitComposeKotlinTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitComposeKotlinTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val BASE_URL = "https://raw.githubusercontent.com/"
    var cryptoModel = remember {
        mutableStateListOf<CryptoModel>()
    }
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CryptoAPI::class.java).getData()

    retrofit.enqueue(object : Callback<List<CryptoModel>>{
        override fun onResponse(
            call: Call<List<CryptoModel>>,
            response: Response<List<CryptoModel>>
        ) {
            if(response.isSuccessful){
                response.body()?.let {
                    cryptoModel.addAll(it)

                }
            }
        }

        override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
          t.printStackTrace()
        }

    })


    Scaffold(topBar = {AppBar()}) {

     CryptoList(cryptoList = cryptoModel)
        println(it)
    }

}

@Composable
fun CryptoList(cryptoList:List<CryptoModel>){
    LazyColumn(contentPadding = PaddingValues(5.dp)) {
        items(cryptoList){ crypto ->
            CryptoRow(cryptoItem = crypto)
        }
    }
}


@Composable
fun CryptoRow(cryptoItem:CryptoModel){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.surfaceTint)) {
        Text(text = cryptoItem.currency
        , style = MaterialTheme.typography.headlineLarge
            , color = Color.White
            , modifier = Modifier.padding(2.dp)
            , fontWeight = FontWeight.Bold
        )
        Text(text = cryptoItem.price
            , color = Color.Cyan
        , style = MaterialTheme.typography.headlineSmall
                , modifier = Modifier.padding(2.dp)
            )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(){
    TopAppBar(title = { Text(text = "Crypto"
        , modifier = Modifier.padding(5.dp)
        , fontSize = 30.sp
        , color = Color.White
        , fontWeight = FontWeight.Bold
    )}
        , colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Blue)
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitComposeKotlinTheme {
          MainScreen()
    }
}