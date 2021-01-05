package com.kmr.fascan

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import com.kmr.fascan.databinding.ScanResultActivityBinding
import com.kmr.fascan.utils.ViewModelUtils
import com.kmr.fascan.viewmodels.ScanResultViewModel

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding: ScanResultActivityBinding
    private val viewModel: ScanResultViewModel by viewModels {
        ViewModelUtils.provideScanResultViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.scan_result_activity)

        binding = DataBindingUtil.setContentView(this, R.layout.scan_result_activity)
        binding.vm = viewModel

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.show()

        intent.extras?.run {
            if (containsKey("picture")) {
                loadSourcePicture(getParcelable<Uri>("picture")!!)
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
}