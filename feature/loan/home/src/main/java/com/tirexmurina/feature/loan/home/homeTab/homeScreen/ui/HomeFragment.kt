package com.tirexmurina.feature.loan.home.homeTab.homeScreen.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.google.android.material.slider.Slider
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.FragmentHomeBinding
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeState
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.presentation.HomeViewModel
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.utils.HomeTextWatcher
import com.tirexmurina.feature.loan.home.navContainer.MainContainerFragment
import com.tirexmurina.feature.loan.home.utils.loanStateHelper
import com.tirexmurina.shared.loan.core.data.models.LoanConditionsModel
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.formatters.formatDateForList
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.show
import com.tirexmurina.util.features.fragments.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModel()
    private var navigationHost: MainContainerFragment? = null
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        setupButtons()
        startScreen()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            val mainContainer = parent.parentFragment
            if (mainContainer is MainContainerFragment) {
                navigationHost = mainContainer
            }
        }
    }

    private fun setupButtons() {
        binding.toolbarQuestionButton.setOnClickListener {
            viewModel.openOnboarding()
        }
        binding.loanRequestButton.setOnClickListener {
            navigationHost?.fromHomeToRequest(binding.amountEditText.text.toString().toInt())
        }
        binding.loansButton.setOnClickListener {
            navigationHost?.fromHomeToLoans()
        }
    }

    private fun startScreen() {
        viewModel.initScreen()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun handleState(state: HomeState) {
        when (state) {
            is HomeState.Content -> handleContent(state)
            is HomeState.Error -> handleError(state)
            is HomeState.Initial -> Unit
            is HomeState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.contentContainer.hide()
        binding.progressBar.show()
    }

    private fun handleError(state: HomeState.Error) {
        binding.contentContainer.hide()
        when (state) {
            HomeState.Error.Forbidden ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")

            HomeState.Error.LoanConditionsCannotFind ->
                createDialog("Какие-то проблемы на сервере. Не удается найти условия займа")

            HomeState.Error.NetworkFault ->
                createDialog("Проблема с сетевым подключением. Проверьте, включен ли у вас интернет")

            HomeState.Error.NotFound ->
                createDialog("Важный элемент не был найден в ответе сервера")

            HomeState.Error.RequestFault ->
                createDialog("Проблема с запросом на сервер")

            HomeState.Error.ResponseFault ->
                createDialog("Непредвиденная ошибка в ответе сервера")

            HomeState.Error.Unauthorized ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")

            HomeState.Error.UnknownError ->
                createDialog("Непредвиденная ошибка")
        }
    }

    private fun createDialog(msg: String) {
        showDialog(
            { requireActivity().finish() },
            R.layout.dialog,
            R.id.dialogTitle,
            requireContext(),
            "OK",
            msg
        )
    }

    private fun handleContent(state: HomeState.Content) {

        binding.contentContainer.show()
        binding.progressBar.hide()
        Glide.with(this)
            .load(R.drawable.ic_banner)
            .fitCenter()
            .into(binding.bannerImage)
        when (state) {
            is HomeState.Content.GotLoans -> {
                setupNewLoanCard(state.conditions)
                setupLoansCard(state.list)
            }

            is HomeState.Content.NoLoans -> {
                setupNewLoanCard(state.conditions)
                binding.loansCard.hide()
                binding.loansEmptyLabel.show()
            }
        }
    }

    private fun setupNewLoanCard(conditions: LoanConditionsModel) {
        binding.amountSlider.valueTo = conditions.maxAmount.toFloat()
        binding.amountSlider.valueFrom = 100.toFloat()
        binding.amountSliderMax.text =
            getString(R.string.money_amount_template, conditions.maxAmount.toInt().toString())
        binding.amountSliderMin.text = getString(R.string.money_amount_template, "100")
        binding.amountEditText.setText("100")
        binding.conditionsLabel.text = getString(
            R.string.home_screen_conditions_label_template,
            conditions.percent.toString(),
            conditions.period.toString()
        )

        val textWatcher = HomeTextWatcher { resultString ->
            if (!resultString.isNullOrEmpty()) {

                val newValue = resultString.toString().toFloat()
                if (newValue <= conditions.maxAmount && newValue >= 100 && binding.amountSlider.value != newValue) {
                    binding.amountSlider.value = newValue
                } else {
                    if (newValue > conditions.maxAmount) {
                        binding.amountEditText.setText(conditions.maxAmount.toInt().toString())
                        binding.amountEditText.setSelection(
                            conditions.maxAmount.toInt().toString().length
                        )
                    }
                    if (newValue < 100) {
                        binding.amountEditText.setText(100.toString())
                        binding.amountEditText.setSelection(100.toString().length)
                    }

                }

            }
            if (resultString.isNullOrEmpty()) {
                binding.amountEditText.setText(binding.amountSliderMin.text)
            }
        }
        binding.amountSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                binding.amountEditText.clearFocus()
            }

            override fun onStopTrackingTouch(slider: Slider) {
            }
        })
        binding.amountSlider.addOnChangeListener { slider, value, fromUser ->
            if (fromUser) {
                binding.amountEditText.text.clear()
                binding.amountEditText.append(value.toInt().toString())
            }
        }

        binding.amountEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.amountEditText.addTextChangedListener(textWatcher)
            } else {
                binding.amountEditText.removeTextChangedListener(textWatcher)
            }
        }

        binding.amountEditText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v)
                binding.amountEditText.clearFocus()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun setupLoansCard(loans: List<Loan>) {
        binding.loansCard.show()
        binding.loansEmptyLabel.hide()
        binding.loansContainer.removeAllViews()
        for (loan in loans) {
            val itemLayout = layoutInflater.inflate(
                R.layout.item_loan,
                binding.loansContainer,
                false
            ) as ConstraintLayout

            val itemId = itemLayout.findViewById<TextView>(R.id.itemId)
            val itemStatus = itemLayout.findViewById<TextView>(R.id.itemStatus)
            val itemAmount = itemLayout.findViewById<TextView>(R.id.itemAmount)
            val itemDate = itemLayout.findViewById<TextView>(R.id.itemDate)
            val (statusText, statusColor) = loanStateHelper(requireContext(), loan.state)

            itemId.text = getString(R.string.loan_item_id_template, loan.id.toString())
            itemStatus.text = statusText
            itemStatus.setTextColor(statusColor)
            itemAmount.text = getString(R.string.money_amount_template, loan.amount.toString())
            itemDate.text = formatDateForList(loan.date)

            itemLayout.setOnClickListener {
                navigationHost?.fromHomeToDetails(loan.id)
            }

            binding.loansContainer.addView(itemLayout)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }
}