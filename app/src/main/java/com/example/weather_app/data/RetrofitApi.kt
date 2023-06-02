package com.example.weather_app.data

import com.example.weather_app.data.models.CurrentWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("weather?")
    fun getCurrentWeather(@Query("q") q : String, @Query("units") units : String, @Query("appid") api_key : String):Call<CurrentWeather>
}