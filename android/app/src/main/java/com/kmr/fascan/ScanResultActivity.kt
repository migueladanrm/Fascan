package com.kmr.fascan

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.kmr.fascan.databinding.ScanResultActivityBinding
import com.kmr.fascan.utils.ViewModelUtils
import com.kmr.fascan.viewmodels.ScanResultViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*

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
        binding.lblNoFaceFound.visibility = View.GONE
        binding.btnTryAgain.setOnClickListener { finish() }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.show()

        intent.extras?.run {
            if (containsKey("picture")) {
                sourcePictureUri = getParcelable("picture")!!
                validateSourcePicture()
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

    private fun validateSourcePicture() {
        val img = InputImage.fromFilePath(this, sourcePictureUri)
        val detector = FaceDetection.getClient()
        detector.process(img).addOnSuccessListener { faces ->
            val faceCount = faces.size

            if (faceCount < 1 || faceCount > 1) {
                val builder = AlertDialog.Builder(this)
                    .setMessage(
                        if (faceCount < 1)
                            R.string.NoFaceDetected
                        else R.string.MoreThanOneFaceDetected
                    )
                    .setPositiveButton(
                        R.string.Accept
                    ) { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }.show()

                builder.show()
            } else {
                val target = faces.first()
                val boundingBox = target.boundingBox

                extractFace(boundingBox)
            }
        }
    }

    private fun extractFace(bb: Rect) {
        val bmp = getBitmapFromUri(sourcePictureUri)
        val new = Bitmap.createBitmap(bmp, bb.left, bb.top, bb.width(), bb.height())

        with(binding) {
            ivwSource.setImageBitmap(new)
            pbrLoadingSource.visibility = View.GONE
        }

        send(new)
    }

    private fun send(facePicture: Bitmap) {
        val tmpFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg", cacheDir)

        FileOutputStream(tmpFile).use { fos ->
            facePicture.compress(Bitmap.CompressFormat.JPEG, 55, fos)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    AppService.create().detectFace(
                        MultipartBody.Part.createFormData(
                            "file",
                            tmpFile.name,
                            tmpFile.asRequestBody()
                        )
                    )
                }.let { face ->
                    with(binding) {
                        Glide.with(this@ScanResultActivity).load(face.resourceUrl)
                            .into(ivwOutput)

                        lblSearchingOutput.visibility = View.GONE
                    }
                }
            } catch (ex: Exception) {
                with(binding) {
                    lblSearchingOutput.visibility = View.GONE
                    lblNoFaceFound.visibility = View.VISIBLE
                }
            }
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