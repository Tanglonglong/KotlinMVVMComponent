package com.example.module_home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.library_base.utils.ViewUtils
import com.example.library_base.utils.dpToPx
import com.example.library_common.glide.setUrl
import com.example.module_home.databinding.HomeMLayoutHomeTabItemBinding
import com.example.module_home.model.ProjectSubInfo

/**
 * @desc   首页列表信息
 */
class HomeTabItemAdapter : BaseRecyclerViewAdapter<ProjectSubInfo, HomeMLayoutHomeTabItemBinding>() {

    override fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): HomeMLayoutHomeTabItemBinding {
        return HomeMLayoutHomeTabItemBinding.inflate(layoutInflater, parent, false)
    }

    override fun onBindDefViewHolder(
        holder: BaseBindViewHolder<HomeMLayoutHomeTabItemBinding>,
        item: ProjectSubInfo?,
        position: Int
    ) {
        if (item == null) return
        holder.binding.apply {
            ivMainIcon.setUrl(item.envelopePic)
            tvTitle.text = item.title
            tvSubTitle.text = item.desc
            tvAuthorName.text = item.author
            tvTime.text = item.niceDate
            ViewUtils.setClipViewCornerRadius(holder.itemView, dpToPx(8))
        }
    }
}