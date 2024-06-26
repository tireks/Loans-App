package com.tirexmurina.feature.loan.home.navContainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tirexmurina.feature.loan.home.R
import com.tirexmurina.feature.loan.home.databinding.FragmentMainContainerBinding
import com.tirexmurina.feature.loan.home.homeTab.homeScreen.ui.HomeFragmentDirections
import com.tirexmurina.feature.loan.home.homeTab.requestScreen.ui.RequestFragmentDirections
import com.tirexmurina.feature.loan.home.menuTab.detailsScreen.ui.DetailsFragmentDirections
import com.tirexmurina.feature.loan.home.menuTab.loansScreen.ui.LoansFragmentDirections
import com.tirexmurina.feature.loan.home.menuTab.menuScreen.ui.MenuFragmentDirections
import com.tirexmurina.util.features.fragments.BaseFragment

class MainContainerFragment : BaseFragment<FragmentMainContainerBinding>() {

    private lateinit var navController: NavController
    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainContainerBinding {
        return FragmentMainContainerBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

    }

    fun fromHomeToRequest(amount : Int){
        val action = HomeFragmentDirections.actionHomeFragmentToRequestFragment(amount)
        navController.navigate(action)
    }

    fun fromHomeToDetails(id : Int){
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(id)
        navController.navigate(action)
    }

    fun fromHomeToLoans(){
        val action = HomeFragmentDirections.actionHomeFragmentToLoansFragment()
        navController.navigate(action)
    }

    fun fromLoansToDetails(id : Int){
        val action = LoansFragmentDirections.actionLoansFragmentToDetailsFragment(id)
        navController.navigate(action)
    }

    fun fromMenuToLoans(){
        val action = MenuFragmentDirections.actionMenuFragmentToLoansFragment()
        navController.navigate(action)
    }

    fun fromLoansToHome(){
        val action = LoansFragmentDirections.actionLoansFragmentToHomeFragment()
        navController.navigate(action)
    }

    fun fromRequestToHome(){
        val action = RequestFragmentDirections.actionRequestFragmentToHomeFragment()
        navController.navigate(action)
    }

    fun fromDetailsToLoans(){
        val action = DetailsFragmentDirections.actionDetailsFragmentToLoansFragment()
        navController.navigate(action)
    }

}