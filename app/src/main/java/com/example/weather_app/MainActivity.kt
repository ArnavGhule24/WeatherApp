package com.example.weather_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.weather_app.data.RetrofitApi
import com.example.weather_app.data.models.CurrentWeather
import com.example.weather_app.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val base = "https://api.openweathermap.org/data/2.5/"
    private val api_key = "a51be8bb968bae8cdf520de8e0698b4b"
    lateinit var mainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        getWeatherData()

        mainBinding.tvForecast.setOnClickListener {

        }

    }

    fun getWeatherData(){

        val retrofit = Retrofit.Builder()
            .baseUrl(base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitApi : RetrofitApi = retrofit.create(RetrofitApi::class.java)
        val call : Call<CurrentWeather> = retrofitApi.getCurrentWeather("London", "metric", api_key)
        call.enqueue(object : Callback<CurrentWeather>{
            override fun onResponse(
                call: Call<CurrentWeather>,
                response: Response<CurrentWeather>
            ) {
                if(!response.isSuccessful ){
                    mainBinding.tvTemp.text = "Error"
                }


                val currentWeather = response.body() as CurrentWeather
                val iconId = currentWeather.weather[0].icon
                val imgurl = " https://openweathermap.org/img/wn/$iconId.png"
                Picasso.get().load(imgurl).into(mainBinding.imgWeather)

                mainBinding.tvSunrise.text=
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.ENGLISH
                    ).format(currentWeather.sys.sunrise * 1000)

                mainBinding.tvSunset.text=
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.ENGLISH
                    ).format(currentWeather.sys.sunset * 1000)

                mainBinding.apply {
                    tvStatus.text = currentWeather.weather[0].description
                    tvWind.text = "${currentWeather.wind.speed.toString()} KM/H"
                    tvLocation.text = "${currentWeather.name}\n${currentWeather.sys.country}"
                    tvTemp.text = "${currentWeather.main.temp.toInt()} °C"
                    tvHumidity.text = "${currentWeather.main.humidity}%"
                    tvPressure.text = "${currentWeather.main.pressure}hPa"
                    tvUpdateTime.text = "Last Update: ${
                        SimpleDateFormat(
                            "hh:mm a",
                            Locale.ENGLISH
                        ).format(currentWeather.dt * 1000)
                    }"
                }

                Log.d("ARNAV", imgurl)
            }

            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }

        })
    }
}