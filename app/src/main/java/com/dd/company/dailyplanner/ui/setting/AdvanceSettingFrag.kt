package com.dd.company.dailyplanner.ui.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dd.company.dailyplanner.R
import com.dd.company.dailyplanner.data.api.PlanService
import com.dd.company.dailyplanner.data.api.RetrofitClient
import com.dd.company.dailyplanner.databinding.LayoutAdvanceSettingBinding
import com.dd.company.dailyplanner.ui.base.BaseFragment
import com.dd.company.dailyplanner.ui.home.DialogConfirmRemovePlan
import com.dd.company.dailyplanner.utils.*

class AdvanceSettingFrag : BaseFragment<LayoutAdvanceSettingBinding>() {
    override fun initView() {
        val data: List<String> = resources.getStringArray(R.array.day_of_week_value).toList()
        val index = SharePreferenceUtil.getStartDayOfWeek()
        binding.tvDate.text = data[index]
    }

    private val dialogConfirmRemovePlan by lazy {
        DialogConfirmRemovePlan(requireContext())
    }
    private val email by lazy {
        SharePreferenceUtil.get(SharePreferenceUtil.EMAIL_LOGIN).trim()
    }
    private val apiService by lazy {
        RetrofitClient.getInstance().create(PlanService::class.java)
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.layoutRemoveAll.setOnSafeClick {
            showDialogConfirm()
        }
        binding.toolbar.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.layoutStartDayOfWeek.setOnSafeClick {
            requireContext().showDialogPickOptionValue { s, i ->
                binding.tvDate.text = s
                SharePreferenceUtil.setStartDayOfWeek(i)
            }
        }
    }

    private fun showDialogConfirm() {
        if (!dialogConfirmRemovePlan.isShowing()) {
            dialogConfirmRemovePlan.show(title = "Bạn có chắc chắn muốn xóa hết dữ liệu?",
            stringSubmit = "Xóa hết tất cả") {
                showLoading()
                apiService.syncPlan(email, mutableListOf()).enqueueShort(success = {
                    hideLoading()
                    if (it.code() == 200) {
                        requireContext().showToast("Đã xóa tất cả dữ liệu!")
                    } else {
                        requireContext().showToast("${it.raw()}")
                    }
                }, failed = {
                    hideLoading()
                    requireContext().showToast(it.message ?: "Lỗi khi xóa dữ liệu!")
                })
            }
        }
    }

    private fun showLoading() {
        binding.loading.show()
    }

    private fun hideLoading() {
        binding.loading.gone()
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): LayoutAdvanceSettingBinding {
        return LayoutAdvanceSettingBinding.inflate(inflater)
    }
}