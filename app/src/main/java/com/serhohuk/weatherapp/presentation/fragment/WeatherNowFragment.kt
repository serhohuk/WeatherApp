package com.serhohuk.weatherapp.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color.green
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.databinding.FragmentWeatherNowBinding
import com.serhohuk.weatherapp.presentation.MainActivity
import com.serhohuk.weatherapp.presentation.utils.ConnectionChecker
import com.serhohuk.weatherapp.presentation.utils.KeyboardUtils
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherNowFragment : Fragment() {

    private var _binding : FragmentWeatherNowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var job : Job
    private lateinit var jobForecast : Job
    private var jobSearch : Job? = null
    lateinit var shPrefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherNowBinding.inflate(inflater,container,false)
        shPrefs = requireActivity().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
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
            viewModel.getWeatherCurrent(shPrefs.getString("city", "Kyiv") ?: "Kyiv", Locale.getDefault().language)
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
                    if (ConnectionChecker.getConnectionType(requireContext())!=0){
                        delay(600)
                        if (text.isNotEmpty()) {
                            KeyboardUtils.hideKeyboard(binding.etSearch)
                            binding.flSearch.visibility = GONE
                            viewModel.getWeatherCurrent(text,Locale.getDefault().language)
                            viewModel.getWeatherForecast(text, Locale.getDefault().language)
                        }
                    } else {
                        delay(600)
                        Toast.makeText(requireContext(),
                            "You are offline , search unavailable",
                            Toast.LENGTH_SHORT).show()
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
                            shPrefs.edit().putString("city",data.name).apply()
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
                        ensureActive()
                        binding.flProgress.visibility = View.GONE
                        Toast.makeText(activity, it.error?.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        ensureActive()
                        binding.flProgress.visibility = View.VISIBLE
                    }
                }
            }
        }
        jobForecast = lifecycleScope.launchWhenCreated {
            viewModel.stateFlowForecast.collectLatest {
                when(it){
                    is Resource.Success -> {
                        ensureActive()
                        val tempList = it.data!!.list!!.map { weatherInfo ->
                            weatherInfo.main!!.temp!!.roundToInt()
                        }.take(8)
                        setChartView(tempList)
                    }
                    is Resource.Error -> {
                        ensureActive()
                        binding.flProgress.visibility = View.GONE
                        Toast.makeText(activity, it.error?.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        ensureActive()
                        binding.flProgress.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setChartView(data : List<Int>){
        val entries = mutableListOf<Entry>()
        data.forEachIndexed { index, i ->
            entries.add(Entry((index+1).toFloat(),i.toFloat()))
        }
        val dataSet = LineDataSet(entries,"Temperature")
        dataSet.setColor(resources.getColor(R.color.light_blue))
        dataSet.valueTextColor = resources.getColor(R.color.teal_700)
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        dataSet.setDrawFilled(true)
        dataSet.disableDashedLine()
        dataSet.setFillColor(ContextCompat.getColor(requireContext(),R.color.light_blue))
        val lineData = LineData(dataSet)
        binding.lineChart.data = lineData
        binding.lineChart.description.text = "3-hours Interval"
        binding.lineChart.setTouchEnabled(false)
        binding.lineChart.getAxisLeft().setDrawGridLines(false);
        binding.lineChart.getAxisRight().setDrawGridLines(false);
        binding.lineChart.getXAxis().setDrawGridLines(false);
        binding.lineChart.invalidate()
    }

    private fun setCurrentDate(){
        val sdf = SimpleDateFormat("MM-dd", Locale.getDefault())
        val currentDate: String = sdf.format(Date())
        binding.tvDate.text = currentDate
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
        jobForecast.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}