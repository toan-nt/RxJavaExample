package com.toantmt.rxjavaexample.creating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentDeferObservableBinding
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable

class DeferObservableFragment: Fragment() {
    private var _binding: FragmentDeferObservableBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = DeferObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDeferObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            createDeferObservable()
        }
    }

    private fun createDeferObservable() {
        val observable = Observable.defer {
            val time = System.currentTimeMillis()
            Log.i(TAG, "create observable: time: $time")
            Observable.just(time)
        }

        compositeDisposable.add(observable.subscribe {
            Log.i(TAG, "subscribe observable: time1: $it")
        })

        compositeDisposable.add(observable.subscribe {
            Log.i(TAG, "subscribe observable: time2: $it")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}