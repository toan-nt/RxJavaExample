package com.toantmt.rxjavaexample.creating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentIntervalObservableBinding
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class IntervalObservableFragment: Fragment() {
    private var _binding: FragmentIntervalObservableBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = IntervalObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentIntervalObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            rangeDeferObservable()
        }
    }

    private fun rangeDeferObservable() {
        val observable = Observable.interval(1, TimeUnit.SECONDS)

        compositeDisposable.add(
            observable
                .doOnNext {
                    Log.i(TAG, "doOnNext: long1: $it")
                    if (it  == 30L) {
                        compositeDisposable.dispose()
                    }
                }
            .filter { it >= 25L }
            .map {
            "value : $it"
        }.subscribe {
            Log.i(TAG, "subscribe observable: long1: $it")
        })

        compositeDisposable.add(observable.filter { it < 20L }.subscribe {
            Log.i(TAG, "subscribe observable: long2: $it")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}