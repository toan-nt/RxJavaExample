package com.toantmt.rxjavaexample.creating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentEmptyNeverThrowObservableBinding
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.IOException

class EmptyNeverThrowObservableFragment : Fragment() {
    private var _binding: FragmentEmptyNeverThrowObservableBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = EmptyNeverThrowObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEmptyNeverThrowObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            rangeDeferObservable()
        }
    }

    private fun rangeDeferObservable() {
        val observable = Observable.empty<String>()
        compositeDisposable.add(observable.subscribe ({
            Log.d(TAG, "Observable.empty,  This should never be printed!, $it")
        }, {
            Log.e(TAG, "Observable.empty, Or this!")
        }, {
            Log.d(TAG, "Observable.empty, Done will be printed.")
        }))

        val neverObservable = Observable.never<String>()
        compositeDisposable.add(neverObservable.subscribe ({
            Log.d(TAG, "Observable.never,  This should never be printed!, $it")
        }, {
            Log.e(TAG, "Observable.never, Or this!")
        }, {
            Log.d(TAG, "Observable.never, Done will be printed.")
        }))

        val errorObservable = Observable.error<Exception>(IOException())
        compositeDisposable.add(errorObservable.subscribe ({
            Log.d(TAG, "Observable.error,  This should never be printed!, $it")
        }, {
            Log.e(TAG, "Observable.error, Or this! $it")
        }, {
            Log.d(TAG, "Observable.error, Done will be printed.")
        }))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}