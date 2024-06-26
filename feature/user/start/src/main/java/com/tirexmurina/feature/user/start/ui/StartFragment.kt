package com.tirexmurina.feature.user.start.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.tirexmurina.feature.user.start.R
import com.tirexmurina.feature.user.start.databinding.FragmentStartBinding
import com.tirexmurina.feature.user.start.presentation.StartState
import com.tirexmurina.feature.user.start.presentation.StartViewModel
import com.tirexmurina.feature.user.start.ui.bottomContainer.BottomContainerFragment
import com.tirexmurina.util.features.fragments.BaseFragment
import com.tirexmurina.util.features.fragments.hide
import com.tirexmurina.util.features.fragments.showDialog
import org.koin.androidx.viewmodel.ext.android.viewModel


class StartFragment : BaseFragment<FragmentStartBinding>(), BottomContainerFragment.BottomContainerFragmentListener {

    private val viewModel: StartViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStartBinding {
        return FragmentStartBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        setupSlidingFragment()
        viewModel.startScreen()
    }

    private fun handleState(startState: StartState) {
        when(startState){
            is StartState.Content -> handleContent()
            is StartState.Error -> handleError(startState)
            is StartState.Initial -> {Unit}
            is StartState.Loading -> {Unit}
        }
    }

    private fun handleError(state: StartState.Error) {
        binding.bottomContainer.hide()
        when(state){
            StartState.Error.Forbidden ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")
            StartState.Error.SharedPrefsCorrupted ->
                createDialog("Какие-то проблемы c внутренним хранилищем. Нет доступа к токену")
            StartState.Error.NetworkFault ->
                createDialog("Проблема с сетевым подключением. Проверьте, включен ли у вас интернет")
            StartState.Error.NotFound ->
                createDialog("Важный элемент не был найден в ответе сервера")
            StartState.Error.RequestFault ->
                createDialog("Проблема с запросом на сервер")
            StartState.Error.ResponseFault ->
                createDialog("Непредвиденная ошибка в ответе сервера")
            StartState.Error.Unauthorized ->
                createDialog("Кажется произошла ошибка авторизации. Попробуйте перзапустить приложение")
            StartState.Error.UnknownError ->
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

    private fun handleContent() {
        binding.progressBar.hide()
        showSlidingFragment()
    }

    private fun setupSlidingFragment() {
        val slidingFragment = BottomContainerFragment()
        childFragmentManager.beginTransaction()
            .replace(R.id.bottomContainer, slidingFragment)
            .commit()
    }

    private fun showSlidingFragment() {
        binding.bottomContainer.visibility = View.VISIBLE
        binding.bottomContainer.translationY = binding.bottomContainer.height.toFloat()
        binding.bottomContainer.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.bottomContainer.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val slideUp = ObjectAnimator.ofFloat(
                    binding.bottomContainer,
                    "translationY",
                    binding.bottomContainer.height.toFloat(),
                    0f
                )
                slideUp.duration = 300
                slideUp.start()
            }
        })
    }

    override fun onLogin(login: String, password: String) {
        viewModel.loginUser(login, password)
    }

    override fun onRegister(login: String, password: String) {
        viewModel.registerUser(login, password)
    }

}