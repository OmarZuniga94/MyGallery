package com.mtg.galleryapp.modules.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mtg.galleryapp.R
import com.mtg.galleryapp.databinding.ActivityMainBinding
import com.mtg.galleryapp.modules.detail.DetailActivity
import com.mtg.galleryapp.net.PicturesResponse


class MainActivity : AppCompatActivity(), MainViewModel.MainModelCallback, ImageAdapter.ImageClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.listener = this
        initViews()
    }

    private fun initViews() {
        /* Manage orientation counters for adapter */
        binding.rcvImages.layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        } else {
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        }
        // Observe images obtained by network request
        binding.rcvImages.adapter = ImageAdapter(viewModel.images, this)
        binding.lytRefresh.setOnRefreshListener { _ ->
            viewModel.loadImages()
        }
        // Obtain authorization to get images from server
        binding.lytLoader.visibility = VISIBLE
        viewModel.validateAuthorization()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.rcvImages.layoutManager = if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        } else {
            GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        }
    }

    override fun onImageClick(view: View, picture: PicturesResponse) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.ARG_PICTURE, picture)
        // Get the transition name from the string
        val transitionName = getString(R.string.t_detail)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                view, transitionName)
        ActivityCompat.startActivity(this, intent, options.toBundle())
    }

    override fun onError(msg: String) {
        binding.lytLoader.visibility = GONE
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onAuthorizationObtained() {
        binding.lytLoader.visibility = GONE
        viewModel.loadImages()
    }

    override fun onImagesObtained() {
        binding.lytLoader.visibility = GONE
        binding.rcvImages.adapter?.notifyDataSetChanged()
        binding.lytRefresh.isRefreshing = false
        if (viewModel.pageObtain != 1) {
            binding.rcvImages.scrollToPosition(-100)
        }
    }
}