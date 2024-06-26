package com.tirexmurina.feature.user.start.presentation.bottomConrainer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tirexmurina.feature.user.start.ui.bottomContainer.LoginFragment
import com.tirexmurina.feature.user.start.ui.bottomContainer.RegistrationFragment

class BottomViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {


    private val fragments = arrayOf(LoginFragment(), RegistrationFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}