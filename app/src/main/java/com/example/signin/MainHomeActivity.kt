package com.example.signin


import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSON
import com.dylanc.longan.toast
import com.example.signin.adapter.MainViewPagerAdapter

import com.example.signin.base.BaseBindingActivity
import com.example.signin.bean.TypeData
import com.example.signin.bean.TypeModel
import com.example.signin.databinding.ActivityMainBinding
import com.example.signin.fragment.HomeMainFragment
import com.example.signin.fragment.MyFragment


import com.example.signin.mvvm.vm.MainHomeAVM
import com.example.signin.net.RequestCallback
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response


class MainHomeActivity : BaseBindingActivity<ActivityMainBinding, MainHomeAVM>() {


    override fun getViewModel(): Class<MainHomeAVM> = MainHomeAVM::class.java



    override fun initData() {
        getFragmentLists()
        getDataType("sys_zhuce")
        getDataType("sys_ruzhu")
        getDataType("sys_huichang")
        getDataType("sys_laicheng")
        getDataType("sys_liping")
        getDataType("sys_fancheng")
        getDataType("sys_canyin")
        getDataType("user_meeting_sign_up_status")
        getDataType("user_meeting_type")

        getDataType("sys_invoice_status")
        getDataType("sys_invoice_type")
        getDataType("transport_type")
        getDataType("sys_examine_reason")

    }

    var homeFragment: HomeMainFragment? = null
    var myFragment: MyFragment? = null
    fun getFragmentLists() {
        val fragmentLists: MutableList<Fragment> = ArrayList<Fragment>()
        homeFragment = HomeMainFragment()
        fragmentLists.add(homeFragment!!)
        myFragment = MyFragment()
        fragmentLists.add(myFragment!!)

        initAdapter(fragmentLists)

    }


    private fun initAdapter(fragments: MutableList<Fragment>) {
        val mAdapter = MainViewPagerAdapter(supportFragmentManager, fragments)
        binding.mViewPager.adapter = mAdapter
        binding.mViewPager.offscreenPageLimit = 4
        initListener()



    }

    override fun initListener() {
        binding.mMainBottomRg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.mMainHomeRb -> {

                    binding.mViewPager?.currentItem = 0

                }
                R.id.mMainMineRb -> {
                    binding.mViewPager?.currentItem = 1


                }


            }
        }



    }



    private var exitTime: Long = 0
    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次返回退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            System.exit(0);
        }
    }

    fun getDataType(type : String){

        OkGo.get<List<TypeData>>(PageRoutes.Api_datatype+type)
            .tag(PageRoutes.Api_datatype)
            .execute(object : RequestCallback<List<TypeData>>() {
                override fun onMySuccess(data: List<TypeData>?) {
                    super.onMySuccess(data)
                    var model:TypeModel
                   var d = kv.getString("TypeModel","")
                    if(kv.getString("TypeModel","").isNullOrEmpty()){
                        model = TypeModel()
                    }else{
                        model = JSON.parseObject(kv.getString("TypeModel",""), TypeModel::class.java)
                    }
                    when (type){
                        "sys_zhuce"->{ model.sys_zhuce = data }
                        "sys_ruzhu"->{model.sys_ruzhu = data}
                        "sys_huichang"->{model.sys_huichang = data}
                        "sys_laicheng"->{model.sys_laicheng = data}
                        "sys_liping"->{model.sys_liping = data}
                        "sys_fancheng"->{model.sys_fancheng = data}
                        "sys_canyin"->{model.sys_canyin = data}
                        "user_meeting_sign_up_status"->{model.user_meeting_sign_up_status = data}
                        "user_meeting_type"->{model.user_meeting_type = data}

                        "sys_invoice_status"->{model.sys_invoice_status = data}
                        "sys_invoice_type"->{model.sys_invoice_type = data}
                        "transport_type"->{model.transport_type = data}
                        "sys_examine_reason"->{model.sys_examine_reason = data}

                    }
                    kv.putString("TypeModel",JSON.toJSONString(model))
                }


                override fun onError(response: Response<List<TypeData>>) {
                    super.onError(response)

                    mViewModel.isShowLoading.value = false
                }

                override fun onFinish() {
                    super.onFinish()

                }


            })
    }



}