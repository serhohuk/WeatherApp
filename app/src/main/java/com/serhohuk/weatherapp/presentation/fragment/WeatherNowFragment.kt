package com.serhohuk.weatherapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.databinding.FragmentWeatherNowBinding
import com.serhohuk.weatherapp.presentation.MainActivity
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

class WeatherNowFragment : Fragment() {

    private var _binding : FragmentWeatherNowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var job : Job

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherNowBinding.inflate(inflater,container,false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleWeatherResponse()

        binding.ivSearch.setOnClickListener {
            Toast.makeText(activity, "it.error?.message", Toast.LENGTH_LONG).show()

        }
    }

    private fun handleWeatherResponse(){
        job = lifecycleScope.launchWhenCreated {
            viewModel.stateFlowCurrent.collectLatest {
                when(it){
                    is Resource.Success ->{
                        ensureActive()
                        binding.flProgress.visibility = View.GONE
                        it.data?.let { data->
                            binding.tvWeatherPlace.text = "${data.name},${data.sys?.country}"
                            binding.tvWeatherDescription.text = data.weather?.get(0)?.description.toString().replaceFirstChar { char ->
                                char.uppercase()
                            }
                            binding.tvTempNow.text = data.main?.temp!!.roundToInt().toString() +"°"
                            binding.tvFeelsLike.text = getString(R.string.feels_like) +" " + data.main?.feels_like!!.roundToInt() +"°"
                            binding.tvHumidityVal.text = data.main?.humidity.toString() + "%"
                            binding.tvPressureVal.text = data.main?.pressure.toString()
                            binding.tvWindVal.text = data.wind?.speed.toString()
                            val imageLink = "http://openweathermap.org/img/wn/${data.weather!![0].icon}@2x.png"
                            Glide.with(binding.ivWeather).load(imageLink).into(binding.ivWeather)
                        }
                    }
                    is Resource.Error -> {
                        binding.flProgress.visibility = View.GONE
                        Toast.makeText(activity, it.error?.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.flProgress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}