package com.example.signin.view




import android.content.Context
import android.util.AttributeSet

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView


class MyScrollView : NestedScrollView {

    lateinit var mContext: Context

    var recyclerView: RecyclerView? = null

    fun setMyRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    var contentHeight = 0

    constructor(cn: Context) : super(cn) {
        mContext = cn
    }

    constructor(cn: Context, attrs: AttributeSet) : super(cn, attrs) {
        mContext = cn
    }

    constructor(cn: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        cn,
        attrs,
        defStyleAttr
    ) {
        mContext = cn
    }


    /*
    * 注意这里嵌套滑动的主导者是RecyclerView（所有嵌套滑动的主导者都是子view询问父view是否需要优先滚动）
    * 当NestedScrollView消耗不完的事件才交给RecyclerView来处理
    * */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (scrollY < contentHeight) {
            if (scrollY + dy <= contentHeight) {//NestedScrollView能处理所有的滚动事件（内容区域已经还在显示），所以没有剩余的滑动给RecyclerView
                scrollBy(0, dy)
                consumed[1] = dy
            } else if (scrollY + dy > contentHeight) {//内容区域已经全部隐藏，所以剩余滑动事件交给RecyclerView
                val scrollViewNeedScrollY = contentHeight - scrollY
                scrollBy(0, scrollViewNeedScrollY)
                consumed[1] = scrollViewNeedScrollY
            }
        }
    }


    /*
    * 注意这里的Fling不是属于传统意义的嵌套滑动的范畴，所以没有重写onNestedFling，这里的主导view是NestedScrollView
    * 当其没有消耗的事件才交给recyclerView来处理
    * */
    override fun fling(velocityY: Int) {
        val dy = FlingUtil.getDistanceByVelocity(mContext, velocityY)
        if (scrollY < contentHeight) {
            if (scrollY + dy <= contentHeight) {
                super.fling(velocityY)
            } else if (scrollY + dy > contentHeight) {
                val scrollViewNeedScrollY = contentHeight - scrollY
                //让NestedScrollView先处理所有的滚动事件
                val scrollViewNeedVelocity =
                    FlingUtil.getVelocityByDistance(mContext, scrollViewNeedScrollY.toDouble())
                if (velocityY > 0) {
                    super.fling(scrollViewNeedVelocity)
                } else {
                    super.fling(-scrollViewNeedVelocity)
                }
                //把剩余的滚动事件交给RecyclerView处理
                val recyclerViewScrollY = dy - scrollViewNeedScrollY
                val recyclerViewNeedVelocity =
                    FlingUtil.getVelocityByDistance(mContext, recyclerViewScrollY)
                if (velocityY > 0) {
                    recyclerView?.fling(0, recyclerViewNeedVelocity)
                } else {
                    recyclerView?.fling(0, -recyclerViewNeedVelocity)
                }
            }
        }
    }

}