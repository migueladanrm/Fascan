package com.kmr.fascan.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kmr.fascan.ScanResultActivity
import com.kmr.fascan.databinding.HomeFragmentBinding
import com.kmr.fascan.utils.ViewModelUtils
import com.kmr.fascan.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelUtils.provideLoginViewModelFactory()
    }
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.btnFromGallery.setOnClickListener(btnFromGalleryOnClickListener)
        binding.btnFromCamera.setOnClickListener(btnFromCameraOnClickListener)
        return binding.root
    }

    private val btnFromGalleryOnClickListener = View.OnClickListener {
        checkPermissionsForStorage()
    }

    private val btnFromCameraOnClickListener = View.OnClickListener {
        checkPermissionForCamera()
    }

    private fun checkPermissionsForStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity?.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (activity?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(
                    permission,
                    PERMISSION_READ_EXTERNAL_STORAGE
                )
                requestPermissions(
                    permissionCoarse,
                    PERMISSION_WRITE_EXTERNAL_STORAGE
                )
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun checkPermissionForCamera(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(activity?.checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){

                val permissionCamera = arrayOf(Manifest.permission.CAMERA)

                requestPermissions(
                    permissionCamera,
                    PERMISSION_CAMERA
                )
            } else{
                takePicture()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            REQUEST_ACTION_PICK_PICTURE
        )
    }

    private fun takePicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ACTION_PICK_PICTURE) {
            val intent = Intent(requireContext(), ScanResultActivity::class.java).apply {
                putExtra("picture", data!!.data)
            }
            startActivity(intent)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TAKE_PICTURE){
            Log.d("HOMEFRAGMENT", "im here")
            val intent = Intent(requireContext(), ScanResultActivity::class.java).apply {
                putExtra("picture", data!!.data)
            }
            startActivity(intent)
        }

    }

    companion object {
        const val REQUEST_ACTION_PICK_PICTURE = 0xA1
        const val PERMISSION_READ_EXTERNAL_STORAGE = 0xA2
        const val PERMISSION_WRITE_EXTERNAL_STORAGE = 0xA3
        const val REQUEST_CODE_TAKE_PICTURE = 1888
        const val PERMISSION_CAMERA = 100
    }
}