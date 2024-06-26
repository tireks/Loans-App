package com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.ItemLoanBinding
import com.tirexmurina.feature.loan.home.utils.loanStateHelper
import com.tirexmurina.shared.loan.core.domain.entity.Loan
import com.tirexmurina.util.features.fragments.formatters.formatDateForDetails
import com.tirexmurina.util.features.fragments.formatters.formatDateForList

class LoanViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(
            R.layout.item_loan,
            parent,
            false)
) {
    private val binding = ItemLoanBinding.bind(itemView)
    fun bind(
        loan: Loan,
        loanClickListener: (Loan) -> Unit
    ) {
        var res = itemView.context.resources
        binding.itemId.text = res.getString(R.string.loan_item_id_template, loan.id.toString())
        binding.itemAmount.text = res.getString(R.string.money_amount_template, loan.amount.toString())
        binding.itemDate.text = formatDateForList(loan.date)
        val (statusText, statusColor) = loanStateHelper(itemView.context, loan.state)
        binding.itemStatus.text = statusText
        binding.itemStatus.setTextColor(statusColor)


        itemView.setOnClickListener {
            loanClickListener(loan)
        }
    }
}