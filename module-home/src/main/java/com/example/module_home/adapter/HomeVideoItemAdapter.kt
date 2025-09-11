package com.example.module_home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.library_common.glide.setUrl
import com.example.module_home.databinding.HomeMVideoItemBinding
import com.example.module_home.model.VideoInfo

/**
 * @desc   首页视频列表适配器
 */
class HomeVideoItemAdapter(val context: Context) :
    BaseRecyclerViewAdapter<VideoInfo, HomeMVideoItemBinding>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): HomeMVideoItemBinding {
        return HomeMVideoItemBinding.inflate(layoutInflater, parent, false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<HomeMVideoItemBinding>,
        item: VideoInfo?,
        position: Int
    ) {
        item?.apply {
            holder.binding.tvTitle.text = title
            holder.binding.ivVideoCover.setUrl(imageUrl)
            holder.binding.tvCollectCount.text = collectionCount
        }
    }
}