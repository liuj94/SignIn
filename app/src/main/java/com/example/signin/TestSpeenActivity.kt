//package com.example.signin
//
//import android.util.Log
//import androidx.fragment.app.FragmentTransaction
//import com.example.signin.base.BaseBindingActivity
//import com.example.signin.base.BaseViewModel
//import com.example.signin.databinding.ActSeepBinding
//import com.example.signin.fragment.MettingDe1Fragment
//import com.example.signin.fragment.MettingDe2Fragment
//import com.example.signin.fragment.MettingDe3Fragment
//import com.example.signin.fragment.MettingDe4Fragment
//
//class TestSpeenActivity  : BaseBindingActivity<ActSeepBinding, BaseViewModel>() {
//    override fun getViewModel(): Class<BaseViewModel> = BaseViewModel::class.java
//    val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//    override fun initData() {
//        Log.d("hhhhhhhhhhhhhhhhhhh", "TestSpeenActivity")
//        initSceneCheckFragment()
//
//    }
//    //第一个fragment
//    private var mettingDe1Fragment: MettingDe1Fragment? = null
//    //第二个fragment
//    private var mettingDe3Fragment: MettingDe3Fragment? = null
//    private var mettingDe2Fragment: MettingDe2Fragment? = null
//    private var mettingDe4Fragment: MettingDe4Fragment? = null
//
//    private val sceneCheckFragmentTag = "MettingDe1Fragment"
//    private val recordSystemFragmentTag = "MettingDe2Fragment"
//
//
//    /**
//     * 显示 现场检查 fragment
//     */
//    fun initSceneCheckFragment() {
////        isShowSceneCheckFragment = true
//        //开启事务，fragment的控制是由事务来实现的
//
//        //add,初始化fragment并添加到事务中，如果为null就new一个
////        if (sceneCheckFragment == null) {
////            sceneCheckFragment = MettingDe1Fragment()
////            transaction.add(R.id.navi_home, sceneCheckFragment!!, sceneCheckFragmentTag)
////        }
//////        hideFragment(transaction)
////        transaction.show(sceneCheckFragment!!)
////        //提交事务
////        transaction.commit()
//    }
//
//    /**
//     * 显示 记录系统数fragment
//     */
//    fun initRecordSystemFragment() {
////        isShowSceneCheckFragment = false
//        //开启事务，fragment的控制是由事务来实现的
//        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//        //add,初始化fragment并添加到事务中，如果为null就new一个
//        if (recordSystemFragment == null) {
//            recordSystemFragment = MettingDe2Fragment()
//            transaction.add(R.id.navi_home, recordSystemFragment!!, recordSystemFragmentTag)
//        }
////        hideFragment(transaction)
//        transaction.show(recordSystemFragment!!)
//        //提交事务
//        transaction.commit()
//
//
//    }
//
//    /**
//     * 隐藏所有的fragment
//     * @param transaction FragmentTransaction
//     */
////    private fun hideFragment(transaction: FragmentTransaction) {
////        if (sceneCheckFragment != null) {
////            transaction.hide(sceneCheckFragment!!)
////        }
////
////        if (recordSystemFragment != null) {
////            transaction.hide(recordSystemFragment!!)
////        }
////
////    }
//
//}