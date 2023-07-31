package com.example.storyapps.ui.story.addstory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapps.ViewModelFactory
import com.example.storyapps.data.Result
import com.example.storyapps.databinding.FragmentAddStoryBinding
import com.example.storyapps.service.AddStory
import com.example.storyapps.ui.camera.CameraActivity
import com.example.storyapps.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private val addStoryViewModel: AddStoryViewModel by viewModels {
        ViewModelFactory(context = requireActivity())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var getFile: File? = null
    private var getLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getMyLastLocation()
        setupView()
        setupViewModelObserver()
    }

    private fun setupView() {
        binding.apply {
            btnCamera.setOnClickListener { startCameraX() }
            btnGallery.setOnClickListener { startGallery() }
            btnUpload.setOnClickListener { upload() }

            edtDescription.doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    edtDescription.error = "Deskripsi tidak boleh kosong"
                } else {
                    edtDescription.error = null
                }
                buttonStatus()
            }
        }
    }

    private fun setupViewModelObserver() {
        addStoryViewModel.file.observe(viewLifecycleOwner) { file ->
            getFile = file
            binding?.ivPreview?.setImageBitmap(BitmapFactory.decodeFile(file.path))
        }

        addStoryViewModel.location.observe(viewLifecycleOwner) { location ->
            getLocation = location
        }
    }

    private fun upload() {
        val text = binding.edtDescription.text

        if (getFile != null) {
            val file = Utils.reduceFileImage(getFile as File)

            val description = text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )
            if (binding?.cbLocation?.isChecked == true) {
                val location = getLocation as Location
                val latitude =
                    location.latitude.toString().toRequestBody("text/plain".toMediaType())
                val longitude =
                    location.longitude.toString().toRequestBody("text/plain".toMediaType())
                val item = AddStory(imageMultipart, description, latitude, longitude)
                addStoryViewModel.addStoryLocation(item).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.data, Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } else {
                val item = AddStory(imageMultipart, description)
                addStoryViewModel.addStory(item).observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding?.progressBar?.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.data, Toast.LENGTH_SHORT).show()
                                findNavController().navigateUp()
                            }
                            is Result.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(activity, result.error, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(
                activity, "Masukkan gambar", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(activity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION") it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
                Utils.rotateFile(file, isBackCamera)
                addStoryViewModel.setFile(file)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = Utils.uriToFile(uri, requireActivity())
                addStoryViewModel.setFile(myFile)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    addStoryViewModel.setLocation(location)
                    binding?.cbLocation?.isEnabled = true
                } else {
                    Toast.makeText(
                        activity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding?.cbLocation?.isEnabled = false
                }
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun buttonStatus() {
        binding.apply {
            btnUpload.isEnabled = !(edtDescription.text.isNullOrEmpty())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}