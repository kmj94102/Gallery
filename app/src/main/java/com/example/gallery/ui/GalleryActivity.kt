package com.example.gallery.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.gallery.BaseActivity
import com.example.gallery.data.GalleryItem
import com.example.gallery.databinding.ActivityMainBinding
import com.example.gallery.ui.detail.GalleryDetailActivity
import com.example.gallery.util.Constants
import com.example.gallery.util.listToArrayList
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : BaseActivity<GalleryViewModel>() {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override val viewModel: GalleryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    override fun observeData() {
        viewModel.stateLiveData.observe(this){
            when(it){
                is GalleryState.Uninitialized -> {
                    checkPermission()
                }
                is GalleryState.ShowGalleryList -> {
                    setGalleryItems(it.list)
                }
            }
        }
    }

    private fun checkPermission() {
        TedPermission
            .create()
            .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    initViews()
                    viewModel.searchGalleryList()
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(this@GalleryActivity, "권한 허용후 이용바랍니다.", Toast.LENGTH_SHORT).show()
                    Handler(mainLooper).postDelayed({finish()}, 1000)
                }
            })
            .check()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = GalleryAdapter(
            itemClickListener = {
                val intent = Intent(this@GalleryActivity, GalleryDetailActivity::class.java).apply {
                    putExtra(Constants.POSITION, it)
                    putExtra(Constants.SELECT_IMAGE_LIST, getSelectArrayList())
                }
                launch.launch(intent)
            },
            itemCountListener = {
                Log.e("+++++", "count $it")
                binding.textViewCount.text = "$it"
            }
        )

        textViewComplete.setOnClickListener {
            getSelectList()?.forEach {
                Log.e("++++", "${it.displayName} / ${it.contentUri}")
            }
        }

    }

    private fun setGalleryItems(list: List<GalleryItem>) = with(binding.recyclerView) {
        (adapter as? GalleryAdapter)?.submitList(list)
    }

    private fun getSelectList() = (binding.recyclerView.adapter as? GalleryAdapter)?.getSelectItemList()

    private fun getSelectArrayList() = listToArrayList(getSelectList())

    private val launch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.getParcelableArrayListExtra<GalleryItem>(Constants.SELECT_IMAGE_LIST)?.let { list ->
                (binding.recyclerView.adapter as? GalleryAdapter)?.apply {
                    setSelectItemList(list)
                    binding.textViewCount.text = "${getSelectList()?.size}"
                }
            }
        }
    }

}