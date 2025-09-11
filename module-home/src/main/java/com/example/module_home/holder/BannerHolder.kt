package com.example.module_home.holder

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.Holder
import com.example.library_common.glide.setUrl
import com.example.module_home.model.Banner

class BannerHolder : Holder<Banner> {

    private lateinit var imageView: ImageView
    override fun createView(context: Context?): View? {
        imageView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return imageView
    }

    override fun UpdateUI(
        context: Context?,
        position: Int,
        data: Banner?
    ) {
        imageView.setUrl(data?.imagePath)
    }
}