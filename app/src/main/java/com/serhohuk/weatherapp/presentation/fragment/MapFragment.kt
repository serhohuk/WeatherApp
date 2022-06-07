package com.serhohuk.weatherapp.presentation.fragment

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.serhohuk.weatherapp.databinding.FragmentGoogleMapBinding
import com.serhohuk.weatherapp.presentation.MainActivity
import com.serhohuk.weatherapp.presentation.utils.ConnectionChecker
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : Fragment() {

    private var _binding : FragmentGoogleMapBinding? =  null
    private val binding get() = _binding!!
    private lateinit var viewModel : MainViewModel

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if ((permissions.get(Manifest.permission.ACCESS_FINE_LOCATION)!!) &&
            (permissions.get(Manifest.permission.ACCESS_COARSE_LOCATION)!!)){
            handleLocation()
        } else {
            Toast.makeText(context, "Permissions denied", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoogleMapBinding.inflate(inflater,container,false)
        viewModel = (activity as MainActivity).viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureMaps()


        binding.fbLocation.setOnClickListener {
            handleLocation()
        }

        val mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                clearMapMarkers()
                val marker = Marker(binding.map)
                marker.position = p
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                marker.setOnMarkerClickListener { marker, mapView ->
                    if (ConnectionChecker.getConnectionType(requireContext())!=0){
                        viewModel.coordinatesData.value =
                            com.serhohuk.weatherapp.domain.models.GeoPoint(
                                marker.position.latitude, marker.position.longitude)
                    } else {
                        Toast.makeText(requireContext(),
                            "You are offline , map unavailable",
                            Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                binding.map.overlays.add(marker)
                binding.map.invalidate()
                return true
            }
        }

        binding.map.overlays.add(MapEventsOverlay(mReceive))




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

    private fun isLocationEnabled(): Boolean {
        var locationManager : LocationManager? = null
        if (locationManager == null){
            locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun initMyLocation() {
        val provider = GpsMyLocationProvider(context)
        val mapController = MapController(binding.map)
        provider.addLocationSource(LocationManager.GPS_PROVIDER)
        provider.addLocationSource(LocationManager.NETWORK_PROVIDER)
        val locationNewOverlay = object : MyLocationNewOverlay(provider, binding.map){
            override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
                if(ConnectionChecker.getConnectionType(requireContext())!=0){
                    viewModel.coordinatesData.value = com.serhohuk.weatherapp.domain.models.GeoPoint(
                        myLocation.latitude, myLocation.longitude
                    )
                } else {
                    Toast.makeText(requireContext(),
                        "You are offline , map unavailable",
                        Toast.LENGTH_SHORT).show()
                }
                return true
            }

        }
        locationNewOverlay.enableMyLocation()
        locationNewOverlay.setPersonAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        locationNewOverlay.disableFollowLocation()
        locationNewOverlay.runOnFirstFix {
            activity?.runOnUiThread {
                mapController.setCenter(locationNewOverlay.myLocation);
            }
        }
        binding.map.overlays.add(locationNewOverlay)
    }

    private fun handleLocation(){
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED
        ){
            locationPermissionRequest.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION))
        } else if (!isLocationEnabled()){
            val lm = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            ) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Activate your GPS")
                builder.setMessage("activate GPS.")
                builder.setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    })
                val alertDialog: Dialog = builder.create()
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()
            }
        } else {
            initMyLocation()
        }
    }

    private fun clearMapMarkers(){
        val overlayLocation = binding.map.overlays.firstOrNull {
            it is MyLocationNewOverlay
        }
        val overlayListener = binding.map.overlays.firstOrNull {
            it is MapEventsOverlay
        }
        binding.map.overlays.clear()
        overlayLocation?.let {
            binding.map.overlays.add(it)
        }
        overlayListener?.let {
            binding.map.overlays.add(it)
        }
    }


    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
        binding.map.overlays.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}