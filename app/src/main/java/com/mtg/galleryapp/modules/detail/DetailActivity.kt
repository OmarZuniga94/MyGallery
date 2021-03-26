package com.mtg.galleryapp.modules.detail

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.mtg.galleryapp.R
import com.mtg.galleryapp.databinding.ActivityDetailBinding
import com.mtg.galleryapp.net.PicturesResponse
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity(), DetailViewModel.DetailModelCallback {

    companion object {
        const val ARG_PICTURE = "picture"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var picture: PicturesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        picture = intent.extras?.getParcelable(ARG_PICTURE)!!
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.listener = this
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.lytLoader.visibility = VISIBLE
        viewModel.loadDetail(picture.id)
        binding.imgBack.setOnClickListener { onBackPressed() }
        binding.fab.setOnClickListener {
            val textToShare = "See this image!! It's awesome!!! ${picture.croppedPicture}"
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, textToShare.substring(0, textToShare.length - 3))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(intent, null)
            startActivity(shareIntent)
        }
    }

    override fun onError(msg: String) {
        binding.lytLoader.visibility = GONE
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDetailObtained() {
        binding.lytLoader.visibility = GONE
        binding.txtAuthor.isSelected = true
        Picasso.get().load(viewModel.detailData.value?.fullPicture)
                .error(R.drawable.ic_launcher_background).into(binding.imgFullPhoto)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ActivityCompat.finishAfterTransition(this)
    }
}