package com.toantmt.rxjavaexample.auth.rxbinding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding4.InitialValueObservable
import com.jakewharton.rxbinding4.widget.textChanges
import com.toantmt.rxjavaexample.databinding.FragmentRxBindingAuthBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.fragment_rx_binding_auth.*

class LoginRxBindingFragment: Fragment() {
    private var _binding: FragmentRxBindingAuthBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentRxBindingAuthBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view.context)
        registerObservables()
    }

    private fun initUI(context: Context) {
        val username = edtUserName.textChanges()
        val password = edtPassword.textChanges()
        val listCondition = listOf(username, password)

       Observable.combineLatest(listCondition) { objects ->
           val username = (objects[0] as CharSequence)
           val password = (objects[1] as CharSequence)
           val isValidUserName = username.isNotBlank()
           val isValidPassword = password.isNotBlank()
           Log.e("LoginRxBindingFragment",  "username: $username, $isValidUserName")
           Log.e("LoginRxBindingFragment",  "password: $password, $isValidPassword")

           isValidUserName && isValidPassword
       }.observeOn(AndroidSchedulers.mainThread()).subscribe { hasValid ->
           binding.btnLogin.isEnabled = hasValid
       }
    }

    private fun registerObservables() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}