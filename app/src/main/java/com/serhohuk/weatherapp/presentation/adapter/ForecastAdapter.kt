package com.serhohuk.weatherapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.domain.models.SimpleForecast

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.DayWeatherViewHolder>() {

    inner class DayWeatherViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val image = itemView.findViewById<ImageView>(R.id.iv_weather)
        private val dayOfWeek = itemView.findViewById<TextView>(R.id.tv_day_of_week)
        private val temperature = itemView.findViewById<TextView>(R.id.tv_temp_range)

        fun bind(data : SimpleForecast){
            val imageLink = "http://openweathermap.org/img/wn/${data.imageId}@2x.png"
            Glide.with(image).load(imageLink).into(image)
            dayOfWeek.text = data.dt
            temperature.text =  "${data.minT} / ${data.maxT} ℃"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayWeatherViewHolder {
        return DayWeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_day_weather,parent, false))
    }

    override fun onBindViewHolder(holder: DayWeatherViewHolder, position: Int) {
        holder.bind(listItems.currentList[position])
    }

    override fun getItemCount(): Int {
        return listItems.currentList.size
    }


    private val diffs = object : DiffUtil.ItemCallback<SimpleForecast>(){
        override fun areItemsTheSame(oldItem: SimpleForecast, newItem: SimpleForecast): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(oldItem: SimpleForecast, newItem: SimpleForecast): Boolean {
            return oldItem == newItem
        }
    }

    val listItems = AsyncListDiffer(this, diffs)
}