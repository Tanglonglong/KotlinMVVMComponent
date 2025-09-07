package com.example.module_view.view


import android.R.color.black
import android.R.color.white
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.module_view.R
import com.example.module_view.databinding.LayoutTitleBarBinding


/**
 * @JvmOverloads 是 Kotlin 中的一个注解，主要用于解决 Kotlin 和 Java 互操作时的默认参数问题。
 * 它的核心功能是‌为带有默认参数的 Kotlin 函数生成多个重载方法‌，
 * 使得 Java 代码可以像调用普通重载方法一样使用这些函数
 * @desc: 标题控件
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var mBinding: LayoutTitleBarBinding


    init {
        init(context, attrs)
    }

    /**
     * 初始化
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val valueArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)

        mBinding = LayoutTitleBarBinding.inflate(inflater, this, true)
        mBinding.leftLayer.setOnClickListener {
            val activity = context as? Activity ?: return@setOnClickListener
            if (activity.isDestroyed || activity.isFinishing) {
                return@setOnClickListener
            }
            activity.finish()
        }
        setAttrs(attrs, valueArray)
    }

    /**
     * 设置属性
     *
     * @param attrs attrs
     * @param array array
     */
    private fun setAttrs(attrs: AttributeSet?, valueArray: TypedArray) {
        if (attrs == null) {
            return
        }
        //设置 leftView
        mBinding.tvLeft.text = valueArray.getString(R.styleable.TitleBar_leftText)
        mBinding.tvLeft.setTextColor(
            valueArray.getColor(
                R.styleable.TitleBar_leftTextColor,
                context.getColor(black)
            )
        )
        val size = valueArray.getDimension(
            R.styleable.TitleBar_leftTextSize,
            context.resources.getDimension(R.dimen.text_size_17)
        )
        mBinding.tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        mBinding.ivLeftIcon.setImageResource(
            valueArray.getResourceId(
                R.styleable.TitleBar_leftIcon,
                R.drawable.ic_back
            )
        )

        val leftTextVisible = valueArray.getBoolean(R.styleable.TitleBar_leftTextVisible, true);
        mBinding.tvLeft.visibility = if (leftTextVisible) VISIBLE else GONE
        val leftIconVisible = valueArray.getBoolean(R.styleable.TitleBar_leftIconVisible, true);
        mBinding.ivLeftIcon.visibility = if (leftIconVisible) VISIBLE else GONE
        val leftLayoutVisible = valueArray.getBoolean(R.styleable.TitleBar_leftVisible, true);
        mBinding.leftLayer.visibility = if (leftLayoutVisible) VISIBLE else GONE
        // 设置 middleView
        mBinding.tvMiddle.text = valueArray.getString(R.styleable.TitleBar_middleText);

        val midSize = valueArray.getDimension(
            R.styleable.TitleBar_middleTextSize,
            context.resources.getDimension(R.dimen.text_size_18)
        )
        mBinding.tvMiddle.setTextSize(TypedValue.COMPLEX_UNIT_PX, midSize)

        mBinding.tvMiddle.setTextColor(
            valueArray.getColor(
                R.styleable.TitleBar_middleTextColor,
                context.getColor(black)
            )
        )
        val middleVisible = valueArray.getBoolean(R.styleable.TitleBar_middleVisible, true);
        mBinding.tvMiddle.visibility = if (middleVisible) VISIBLE else GONE
        // 设置 rightView
        mBinding.tvRight.text = valueArray.getString(R.styleable.TitleBar_rightText)
        mBinding.tvRight.setTextColor(
            valueArray.getColor(
                R.styleable.TitleBar_rightTextColor,
                context.getColor(black)
            )
        );
        mBinding.tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        mBinding.rightIvIcon.setImageResource(
            valueArray.getResourceId(
                R.styleable.TitleBar_rightIcon,
                0
            )
        )
        val rightVisible = valueArray.getBoolean(R.styleable.TitleBar_rightVisible, true);
        mBinding.rightLayer.visibility = if (rightVisible) VISIBLE else GONE
        val rightIconVisible = valueArray.getBoolean(R.styleable.TitleBar_rightIconVisible, true);
        mBinding.rightIvIcon.visibility = if (rightIconVisible) VISIBLE else GONE
        val rightTextVisible = valueArray.getBoolean(R.styleable.TitleBar_rightTextVisible, true);
        mBinding.tvRight.visibility = if (rightTextVisible) VISIBLE else GONE

        // 设置背景
        val backgroundRes = valueArray.getResourceId(R.styleable.TitleBar_backgroundDrawable, -1)
        if (backgroundRes != -1) {
            // 处理资源引用（可能是@color或@drawable）
            val type = resources.getResourceTypeName(backgroundRes)
            when (type) {
                "color" -> mBinding.root.setBackgroundColor(
                    ContextCompat.getColor(context, backgroundRes)
                )

                "drawable" -> mBinding.root.setBackgroundResource(backgroundRes)
            }
        } else {
            mBinding.root.setBackgroundColor(
                valueArray.getColor(
                    R.styleable.TitleBar_backgroundColor,
                    context.getColor(white)
                )
            )
        }


        val showDividerLine = valueArray.getBoolean(R.styleable.TitleBar_showDividerLine, true);
        mBinding.dividerLine.visibility = if (showDividerLine) VISIBLE else GONE
        valueArray.recycle()
    }
}
