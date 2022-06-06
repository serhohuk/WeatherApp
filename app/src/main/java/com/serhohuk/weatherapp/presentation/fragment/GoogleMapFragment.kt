package com.serhohuk.weatherapp.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.databinding.FragmentGoogleMapBinding
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController

class GoogleMapFragment : Fragment() {

    private var _binding : FragmentGoogleMapBinding? =  null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoogleMapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureMaps()

    }

    private fun configureMaps(){
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setUseDataConnection(true)
        binding.map.isTilesScaledToDpi = true
        binding.map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        binding.map.setMultiTouchControls(true);
        val mapController = MapController(binding.map)
        mapController.setZoom(10.0)
        val startPoint = GeoPoint(50.450001, 30.523333);
        mapController.setCenter(startPoint);
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}