package com.serhohuk.weatherapp.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.databinding.FragmentWeatherNowBinding
import com.serhohuk.weatherapp.presentation.MainActivity
import com.serhohuk.weatherapp.presentation.utils.KeyboardUtils
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class WeatherNowFragment : Fragment() {

    private var _binding : FragmentWeatherNowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var job : Job
    private var jobSearch : Job? = null

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
        setCurrentDate()

        binding.ivSearch.setOnClickListener {
            binding.flSearch.visibility = VISIBLE
            binding.etSearch.requestFocus()
            KeyboardUtils.showKeyboard(binding.etSearch)
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.flSearch.visibility = GONE
                true
            }
            false
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.flSearch.visibility = GONE
            binding.etSearch.text.clear()
            viewModel.getWeatherCurrent("Kyiv", "uk")
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                var text = s.toString()
                if (jobSearch!=null){
                    jobSearch?.cancel()
                }
                jobSearch = lifecycleScope.launch(Dispatchers.Main) {
                    delay(600)
                    if (text.isNotEmpty()) {
                        KeyboardUtils.hideKeyboard(binding.etSearch)
                        binding.flSearch.visibility = GONE
                        viewModel.getWeatherCurrent(text,"uk")
                    }
                }
            }

        })
    }

    private fun handleWeatherResponse(){
        job = lifecycleScope.launchWhenCreated {
            viewModel.stateFlowCurrent.collectLatest {
                when(it){
                    is Resource.Success ->{
                        ensureActive()
                        binding.swipeRefresh.isRefreshing = false
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

    private fun setCurrentDate(){
        val sdf = SimpleDateFormat("MM-dd", Locale.getDefault())
        val currentDate: String = sdf.format(Date())
        binding.tvDate.text = currentDate
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