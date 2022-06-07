package com.serhohuk.weatherapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.databinding.FragmentForecastBinding
import com.serhohuk.weatherapp.presentation.adapter.ForecastAdapter
import com.serhohuk.weatherapp.presentation.MainActivity
import com.serhohuk.weatherapp.presentation.utils.ForecastMapper
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.collectLatest


class ForecastWeatherFragment : Fragment() {

    private var _binding : FragmentForecastBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel
    private lateinit var job : Job
    private lateinit var adapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastBinding.inflate(inflater, container, false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        handleForecastResponse()
    }

    private fun handleForecastResponse(){
        job = lifecycleScope.launchWhenCreated {
            viewModel.stateFlowForecast.collectLatest {
                when(it){
                    is Resource.Success ->{
                        ensureActive()
                        binding.tvWeatherPlace.text = it.data!!.city!!.name + " - Forecast"
                        val simpleData = ForecastMapper.weatherForecastToSimpleForecast(it.data!!)
                        adapter.listItems.submitList(simpleData)
                    }
                    is Resource.Loading -> {
                        ensureActive()
                    }
                    is Resource.Error -> {
                        ensureActive()
                    }
                }
            }
        }
    }

    private fun initRecycler(){
        adapter = ForecastAdapter()
        binding.recView.adapter = adapter
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
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