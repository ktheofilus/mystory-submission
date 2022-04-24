package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.api.ListStoryItemLocation
import com.example.myapplication.databinding.ActivityMapsBinding
import com.example.myapplication.viewmodel.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val model: MapViewModel by viewModels()
    private var users :MutableList<ListStoryItemLocation> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        model.stories.observe(this){
            it.forEach{ itemLocation ->
                users.add(itemLocation)
                val pinpoint = LatLng(itemLocation.lat, itemLocation.lon)
                val marker = mMap.addMarker(
                    MarkerOptions()
                    .position(pinpoint)
                    .title(itemLocation.id)
                    )
                Log.d("TAG", "onCreate: $marker")

                mMap.setOnMarkerClickListener { marker ->
                    val markerName = marker.title
                    for (user in users) {
                        if (user.id==markerName){
                            val detailIntent = Intent(this@MapsActivity, DetailActivity::class.java)
                            detailIntent.putExtra("storyLoc",user)
                            startActivity(detailIntent)
                        }
                    }
                    false
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))

        model.getStoriesLocation()

    }

}