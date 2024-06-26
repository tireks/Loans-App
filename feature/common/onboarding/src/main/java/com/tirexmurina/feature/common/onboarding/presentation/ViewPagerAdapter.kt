package com.tirexmurina.feature.common.onboarding.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tirexmurina.feature.common.onboarding.R
import com.tirexmurina.feature.common.onboarding.databinding.ItemOnboardingBinding

class ViewPagerAdapter (
) : RecyclerView.Adapter<ViewPagerAdapter.OnboardingViewHolder>() {

    var slides: List<Int> = emptyList()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    inner class OnboardingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.item_onboarding,
                parent,
                false)
    ) {
        private val binding = ItemOnboardingBinding.bind(itemView)
        fun bind(slideLayoutRes: Int){
            binding.onboardingSlideContainer.removeAllViews()
            LayoutInflater.from(binding.root.context).inflate(slideLayoutRes, binding.onboardingSlideContainer, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        return OnboardingViewHolder(parent)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(slides[position])
    }

    override fun getItemCount(): Int = slides.size
}