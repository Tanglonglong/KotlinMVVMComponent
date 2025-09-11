package com.example.module_home.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


// 定义数据表，指定表名，如果不指定表名则默认为类名
@Parcelize
data class VideoInfo(
    var id: Long = 0,
    var title: String?,
    var desc: String?,

    var authorName: String?,

    var playUrl: String?,

    var imageUrl: String?,
    var collectionCount: String?
) :Parcelable{
    constructor() : this(0, "", "", "", "", "", "");
}