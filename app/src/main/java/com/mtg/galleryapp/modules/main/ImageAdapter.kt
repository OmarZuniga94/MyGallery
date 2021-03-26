package com.mtg.galleryapp.modules.main

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mtg.galleryapp.Application
import com.mtg.galleryapp.R
import com.mtg.galleryapp.databinding.ItemImageBinding
import com.mtg.galleryapp.net.PicturesResponse
import com.mtg.galleryapp.utils.BindingViewHolder
import com.squareup.picasso.Picasso
import kotlin.math.abs

class ImageAdapter(private val list: MutableList<PicturesResponse>, private val listener: ImageClickListener) : RecyclerView.Adapter<BindingViewHolder<ItemImageBinding>>() {

    private var minimumHeight: Int = 0
    private var minimumWidth: Int = 0

    init {
        minimumWidth = if (Application.getContext().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            abs(Application.getContext().resources.displayMetrics.widthPixels / 2)
        } else {
            abs(Application.getContext().resources.displayMetrics.widthPixels / 3)
        }
        minimumHeight = minimumWidth
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemImageBinding> {
        return BindingViewHolder(LayoutInflater.from(Application.getContext()).inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemImageBinding>, position: Int) {
        Picasso.get().load(list[position].croppedPicture).resize(minimumWidth, minimumHeight)
                .error(R.drawable.ic_launcher_background).into(holder.getBinding().imgPhoto)
        holder.getBinding().cdvItem.layoutParams = ViewGroup.LayoutParams(minimumWidth, minimumHeight)
        holder.getBinding().cdvItem.setOnClickListener { listener.onImageClick(holder.getBinding().root, list[position]) }
    }

    override fun getItemCount(): Int = list.size

    override fun onViewRecycled(holder: BindingViewHolder<ItemImageBinding>) {
        super.onViewRecycled(holder)
        Picasso.get().cancelRequest(holder.getBinding().imgPhoto)
    }

    interface ImageClickListener {
        fun onImageClick(view: View, picture: PicturesResponse)
    }
}