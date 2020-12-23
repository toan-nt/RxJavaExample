package com.toantmt.rxjavaexample.creating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentRangeObservableBinding
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable

class RangeObservableFragment: Fragment() {
    private var _binding: FragmentRangeObservableBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = RangeObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRangeObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            rangeDeferObservable()
        }
    }

    private fun rangeDeferObservable() {
        val observable = Observable.rangeLong(100, 2)

        compositeDisposable.add(observable.filter { it == 100L }.map {
            "value : $it"
        }.subscribe {
            Log.i(TAG, "subscribe observable: long1: $it")
        })

        compositeDisposable.add(observable.subscribe {
            Log.i(TAG, "subscribe observable: long2: $it")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}