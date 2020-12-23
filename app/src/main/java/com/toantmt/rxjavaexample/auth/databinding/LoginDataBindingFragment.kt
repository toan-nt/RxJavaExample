package com.toantmt.rxjavaexample.auth.databinding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jakewharton.rxbinding4.widget.textChanges
import com.toantmt.rxjavaexample.auth.rxbinding.LoginRxBindingFragment
import com.toantmt.rxjavaexample.databinding.FragmentDatabindingAuthBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable


class LoginDataBindingFragment : Fragment() {
    private var _binding: FragmentDatabindingAuthBinding? = null
    private val binding get() = _binding!!
    private var viewmodel: LoginViewModel? = null
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = LoginRxBindingFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatabindingAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel = ViewModelProvider(this)[LoginViewModel::class.java]
        _binding?.viewmodel = viewmodel

        initUI(view.context)
        registerObservables()
    }

    private fun initUI(context: Context) {
        compositeDisposable.add(binding.edtUserName.textChanges().subscribe {
            Log.e("LoginRxBindingFragment",  "username, $it")
          viewmodel?.loginRequest?.username = it.toString()
        })

        compositeDisposable.add(binding.edtPassword.textChanges().subscribe {
            Log.e("LoginRxBindingFragment",  "password, $it")
            viewmodel?.loginRequest?.password = it.toString()
        })
    }

    private fun registerObservables() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}