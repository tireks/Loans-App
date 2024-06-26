package com.tirexmurina.feature.loan.home.menuTab.loansScreen.presentation.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tirexmurina.shared.loan.core.domain.entity.Loan

class LoanListAdapter(
    private val loanClickListener: (Loan) -> Unit
) : RecyclerView.Adapter<LoanViewHolder>(){

    var loans: List<Loan> = emptyList()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoanViewHolder {
        return LoanViewHolder(parent)
    }

    override fun getItemCount(): Int = loans.size

    override fun onBindViewHolder(holder: LoanViewHolder, position: Int) {
        holder.bind(loans[position], loanClickListener)
    }
}