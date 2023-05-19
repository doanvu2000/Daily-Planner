package com.dd.company.dailyplanner.ui.inbox

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityInboxBinding
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.utils.SharePreferenceUtil
import com.dd.company.dailyplanner.utils.enqueueShort
import com.dd.company.dailyplanner.utils.setGridManager
import com.dd.company.dailyplanner.utils.setOnSafeClick

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

    @SuppressLint("NotifyDataSetChanged")
    override fun initListener() {
        binding.btnClose.setOnSafeClick { finish() }
        binding.btnAddnewInbox.setOnSafeClick {

        }
        inboxDoneAdapter.onClickPlus = { item, position ->
            inboxNotDoneAdapter.dataList.add(item)
            inboxNotDoneAdapter.notifyDataSetChanged()
        }
        inboxNotDoneAdapter.onClickPlus = { item, position ->
            inboxDoneAdapter.dataList.add(item)
            inboxDoneAdapter.notifyDataSetChanged()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityInboxBinding {
        return ActivityInboxBinding.inflate(inflater)
    }
}