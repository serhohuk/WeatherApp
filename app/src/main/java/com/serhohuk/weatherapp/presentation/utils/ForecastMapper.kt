package com.serhohuk.weatherapp.presentation.utils

import com.serhohuk.weatherapp.domain.models.SimpleForecast
import com.serhohuk.weatherapp.domain.models.WeatherForecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object ForecastMapper {

    fun weatherForecastToSimpleForecast(wf : WeatherForecast): List<SimpleForecast> {
        var index = -1
        val firstObject = wf.list!!.first {
            convertToHours((it.dt!!.toLong() * 1000)) == "00"
        }
        wf.list!!.forEachIndexed { i, weatherInfoResponse ->
            if (weatherInfoResponse.dt == firstObject.dt){
                index = i
            }
        }
        if (index!=-1){
            val neededWeatherForecast = wf.list!!.slice(index until wf.list!!.size)
            val tempList = neededWeatherForecast.map{
                it.main!!.temp!!.roundToInt()
            }
            val result = mutableListOf<SimpleForecast>()
            var dayNumber = 0
            neededWeatherForecast.forEachIndexed { index, weatherInfoResponse ->
                if (convertToHours((weatherInfoResponse.dt!!.toLong() * 1000)) == "12"){
                    val currentTemps = tempList.slice(dayNumber*8 until (dayNumber*8)+8)
                    result.add(
                        SimpleForecast(
                            weatherInfoResponse.weather!!.get(0).icon.toString(),
                            convertToDayWeek(weatherInfoResponse.dt!!.toLong() * 1000),
                            currentTemps.minOrNull()?:0,
                            currentTemps.maxOrNull()?:0
                        )
                    )
                    dayNumber++
                }
            }
            return result
        }
        return listOf()
    }

    private fun convertToHours(millis: Long): String {
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        val date = Date(millis)
        return sdf.format(date)
    }

    private fun convertToDayWeek(millis: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        val date = Date(millis)
        return sdf.format(date)
    }

}