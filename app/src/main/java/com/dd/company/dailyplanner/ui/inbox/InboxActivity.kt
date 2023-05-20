package com.dd.company.dailyplanner.ui.inbox

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.PlanEntity
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.ActivityInboxBinding
import com.dd.company.dailyplanner.ui.addplan.AddPlanActivity
import com.dd.company.dailyplanner.ui.base.BaseActivity
import com.dd.company.dailyplanner.ui.copyplan.CopyPlanActivity
import com.dd.company.dailyplanner.ui.editplan.EditPlanActivity
import com.dd.company.dailyplanner.ui.home.DialogConfirmRemovePlan
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
    private val dialogConfirmRemovePlan by lazy {
        DialogConfirmRemovePlan(this)
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
                listPlan.addAll(body.data)
                setDataList()
            }
        }, failed = {
            Log.d("doanvv", "${it.message}")
        })
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun setDataList() {
        inboxDoneAdapter.setDataList(listPlan.filter { it.isDone && it.type == 1 })
        inboxNotDoneAdapter.setDataList(listPlan.filter { !it.isDone && it.type == 1 })
    }

    private var planSelected: PlanEntity? = null

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    override fun initListener() {
        binding.btnClose.setOnSafeClick { onBack() }
        binding.btnAddnewInbox.setOnSafeClick {
            openActivity(AddPlanActivity::class.java, bundleOf("is_inbox" to true))
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
        inboxDoneAdapter.setOnClickItem { item, position ->
            planSelected = item
            showEditBottom(item)
        }
        inboxNotDoneAdapter.setOnClickItem { item, position ->
            planSelected = item
            showEditBottom(item)
        }
        binding.btnCloseEdit.setOnSafeClick {
            hideLayoutEdit()
        }
        binding.btnRemovePlan.setOnSafeClick {
            //remove plan
            showDialogConfirmRemove()
        }
        binding.btnCopyPlan.setOnSafeClick {
            //copy plan
            hideLayoutEdit()
            openActivity(CopyPlanActivity::class.java, bundleOf("plan_entity" to planSelected, "is_inbox" to true))
        }
        binding.btnDonePlan.setOnSafeClick {
            //done plan
            planSelected?.isDone = !(planSelected?.isDone ?: false)
            val findPlan = listPlan.find { it.id == planSelected?.id }
            findPlan?.isDone = planSelected?.isDone ?: false
            planSelected?.let { syncPlan() }
            hideLayoutEdit()
        }
        binding.btnEditPlan.setOnSafeClick {
            //edit plan
            hideLayoutEdit()
            openActivity(
                EditPlanActivity::class.java, bundleOf(
                    "plan_entity" to planSelected,
                    "is_inbox" to true
                )
            )
        }
    }

    private fun showDialogConfirmRemove() {
        if (!dialogConfirmRemovePlan.isShowing()) {
            dialogConfirmRemovePlan.show {
                listPlan.remove(planSelected)
                syncPlan("Hộp thư đã xóa!")
                hideLayoutEdit()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun syncPlan(msg: String? = null) {
        apiService.syncPlan(email, listPlan).enqueueShort(success = {
            initData()
            msg?.let {
                showToast(it)
            }
        }, failed = {
            showToast("Failed to update status plan")
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onBack() {
        val data = listPlan.filter { it.type == 0 }.toMutableList()
        data.addAll(inboxDoneAdapter.dataList)
        data.addAll(inboxNotDoneAdapter.dataList)
        apiService.syncPlan(email, data).enqueueShort(success = {
            finish()
        }, failed = {
            showToast("Error when add plan: ${it.message}")
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBackPressed() {
        onBack()
    }

    @SuppressLint("SetTextI18n")
    private fun showEditBottom(item: PlanEntity?) {
        item?.let {
            binding.layoutBottomEdit.show()
            binding.imgIconPlan.setImageResource(getDrawableIdByName(it.icon))
            binding.tvDescriptionPlan.apply {
                binding.tvDescriptionPlan.text = it.content
                if (it.isDone) {
                    paintFlags = binding.tvWorkDone.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.btnDonePlan.setImageResource(R.drawable.layout_not_done_plan)
                } else {
                    paintFlags = binding.tvWorkDone.paintFlags
                    binding.btnDonePlan.setImageResource(R.drawable.layout_done_plan)
                }
            }
        }
    }

    private fun hideLayoutEdit() {
        binding.layoutBottomEdit.gone()
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityInboxBinding {
        return ActivityInboxBinding.inflate(inflater)
    }
}