package com.kmr.fascan

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.kmr.fascan.databinding.ScanResultActivityBinding
import com.kmr.fascan.utils.ViewModelUtils
import com.kmr.fascan.viewmodels.ScanResultViewModel

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding: ScanResultActivityBinding
    private val viewModel: ScanResultViewModel by viewModels {
        ViewModelUtils.provideScanResultViewModelFactory()
    }
    private lateinit var sourcePictureUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.scan_result_activity)

        binding = DataBindingUtil.setContentView(this, R.layout.scan_result_activity)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.show()

        intent.extras?.run {
            if (containsKey("picture")) {
                sourcePictureUri = getParcelable("picture")!!
                loadSourcePicture(sourcePictureUri)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    private fun loadSourcePicture(uri: Uri) {
        with(binding) {
            ivwSourcePicture.setImageURI(uri)
        }
    }

    private fun validateSourcePicture() {
        val bmp = getBitmapFromUri(sourcePictureUri)
        val fvi = FirebaseVisionImage.fromBitmap(bmp)
        FirebaseVision.getInstance().visionFaceDetector.detectInImage(fvi)
            .addOnSuccessListener { faces ->
                // must check number of faces.
            }
    }

    private fun getBitmapFromUri(selectedPhotoUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )
        } else {
            val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        }
    }
}