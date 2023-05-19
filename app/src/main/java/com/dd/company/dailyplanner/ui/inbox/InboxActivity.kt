package com.dd.company.dailyplanner.ui.inbox

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityInboxBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.*

class InboxActivity : BaseActivity<ActivityInboxBinding>() {
    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }
    private val inboxNotDoneAdapter by lazy {
        InboxNotDoneAdapter()
    }
    private val inboxDoneAdapter by lazy {
        InboxDoneAdapter()
    }
    private val email by lazy {
        SharePreferenceUtil.get(SharePreferenceUtil.EMAIL_LOGIN).trim()
    }
    private var listPlan: MutableList<PlanEntity> = mutableListOf()

    override fun initView() {
        binding.rcvInboxDone.setGridManager(this, 1, inboxDoneAdapter)
        binding.rcvInboxNotDone.setGridManager(this, 1, inboxNotDoneAdapter)
    }

    override fun initData() {
        apiService.getPlan(email).enqueueShort(success = {
            val body = it.body()
            if (body?.status == true) {
                listPlan.clear()
                listPlan.addAll(body.data.filter { item -> item.type == 1 })
                setDataList()
            }
        }, failed = {
            Log.d("doanvv", "${it.message}")
        })
    }

    private fun setDataList() {
        inboxDoneAdapter.setDataList(listPlan.filter { it.isDone })
        inboxNotDoneAdapter.setDataList(listPlan.filter { !it.isDone })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    override fun initListener() {
        binding.btnClose.setOnSafeClick { onBack() }
        binding.btnAddnewInbox.setOnSafeClick {

        }
        inboxDoneAdapter.onClickPlus = { item, position ->
            item.isDone = false
            inboxNotDoneAdapter.dataList.add(item)
            inboxNotDoneAdapter.notifyDataSetChanged()
        }
        inboxNotDoneAdapter.onClickPlus = { item, position ->
            item.isDone = true
            inboxDoneAdapter.dataList.add(item)
            inboxDoneAdapter.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onBack() {
        listPlan.removeIf { it.type == 1 }
        listPlan.addAll(inboxDoneAdapter.dataList)
        listPlan.addAll(inboxNotDoneAdapter.dataList)
        apiService.syncPlan(email, listPlan).enqueueShort(success = {
            finish()
        }, failed = {
            showToast("Error when add plan: ${it.message}")
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        onBack()
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityInboxBinding {
        return ActivityInboxBinding.inflate(inflater)
    }
}