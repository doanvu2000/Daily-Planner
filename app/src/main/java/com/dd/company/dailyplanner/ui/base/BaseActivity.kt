package com.dd.company.dailyplanner.ui.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = inflateViewBinding(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
        initListener()

    }

    abstract fun initView()
    abstract fun initData()
    abstract fun initListener()

    abstract fun inflateViewBinding(inflater: LayoutInflater): VB

//    fun isNetworkAvailable(): Boolean {
//        val connectivityManager =
//            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetworkInfo = connectivityManager.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }
}