package com.example.newsappassignment.models

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsappassignment.R

@Entity(tableName = "facts")
class Fact(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val imageHref: String?,
    val catId: String
) {
    companion object {
        @JvmStatic
        @BindingAdapter("factImage")
        fun loadFactImage(imgView: ImageView, imgUrl: String?) {
            imgUrl?.let {
                val imgUri =
                    imgUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(imgView.context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .error(R.drawable.ic_image))
                    .into(imgView)
            }
        }


    }
}