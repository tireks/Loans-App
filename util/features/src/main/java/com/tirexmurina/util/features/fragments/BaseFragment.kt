package com.tirexmurina.util.features.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

// Абстрактный базовый класс биндинга для фрагментов.
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    // Метод для создания представления фрагмента.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Создание биндинга к представлению.
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    // Абстрактный метод для инфлэйта представления биндинга.
    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}