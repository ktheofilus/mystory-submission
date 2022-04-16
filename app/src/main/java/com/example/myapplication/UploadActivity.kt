package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityUploadBinding
import com.example.myapplication.utils.Utilities.rotateBitmap
import com.example.myapplication.utils.Utilities.uriToFile
import com.example.myapplication.viewmodel.UploadViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUploadBinding
    private val model: UploadViewModel by viewModels()

    private var getFile: File? = null

    private var descExist = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var loc:Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar2.visibility = View.INVISIBLE

        binding.uploadButton.isEnabled=false

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        model.isLoading.observe(this){
            model.showLoading(binding.progressBar2)
        }

        model.message.observe(this){message->
            Snackbar.make(
                binding.root,
                message,
                Snackbar.LENGTH_SHORT
            ).show()
        }

        model.isUploaded.observe(this){isUploaded->
            if (isUploaded){
                val uploadtIntent = Intent()
                setResult(RESULT_CODE, uploadtIntent)
                finish()
            }
        }


        if (!this.allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                UploadActivity.REQUIRED_PERMISSIONS,
                UploadActivity.REQUEST_CODE_PERMISSIONS
            )
        }



        binding.descEditTextTextMultiLine.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                descExist = s.toString().isNotEmpty()
                checkButton(descExist)

            }
            override fun afterTextChanged(s: Editable) {
            }

        })

        getMyLastLocation()

        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.no_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkLocationPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            getFile=myFile
            checkButton(descExist)
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {

        if (getFile != null) {
            val desc = binding.descEditTextTextMultiLine.text.toString()
            getMyLastLocation()
            model.upload(getFile!!,desc,loc.latitude,loc.longitude)
        }

    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val photo = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = photo
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
            checkButton(descExist)
            binding.previewImageView.setImageBitmap(result)

        }
    }

    fun checkButton(boolean: Boolean){
        if (boolean){
            binding.uploadButton.isEnabled = getFile != null
        }else binding.uploadButton.isEnabled =false


    }

    private fun getMyLastLocation() {

        if (checkLocationPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkLocationPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            Log.d("TAG", "getMyLastLocation: fusedlocation")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                Log.d("TAG", "getMyLastLocation: $location")
                if (location != null) {
                    loc=location
                } else {
                    Toast.makeText(
                        this@UploadActivity,
                        R.string.no_location,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    companion object {
        const val CAMERA_X_RESULT = 200
        const val RESULT_CODE = 110
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }







}