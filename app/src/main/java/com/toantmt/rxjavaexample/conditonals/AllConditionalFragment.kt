package com.toantmt.rxjavaexample.conditonals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.R
import com.toantmt.rxjavaexample.databinding.FragmentAllConditionalsBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

import java.util.concurrent.TimeUnit

class AllConditionalFragment : Fragment() {

    private var _binding: FragmentAllConditionalsBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = AllConditionalFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllConditionalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            create()
        }
    }

    private fun create() {
        val todos = resources.getIntArray(R.array.rx_number).toList()


        val observable1 = Observable.timer(3, TimeUnit.SECONDS)
            .flatMap {
                Observable.just(10, 20, 30, 40)
            }

        val observable2 = Observable.timer(4, TimeUnit.SECONDS)
            .flatMap {
                Observable.just(100, 200, 300, 500)
            }

        val observable3 = Observable.ambArray(observable1, observable2).subscribe {
            Log.d(TAG, "ambArray: $it")
        }

        compositeDisposable.add(observable3)

        Observable.range(0, 10).defaultIfEmpty(1).skipWhile { next -> next < 5 }.blockingForEach {
            Log.d(TAG, "ambWith blockingSubscribe: $it")
        }

        val takeUntilObservable = Observable.range(0, 10).defaultIfEmpty(1).takeUntil { next -> next == 7 }.subscribe  ({
            Log.d(TAG, "takeUntil onnext: $it")
        }, {
            Log.e(TAG, "takeUntil error: $it")
        }, {
            Log.d(TAG, "takeUntil done")
        })

        compositeDisposable.add(takeUntilObservable)

        val takeWhileObservable = Observable.range(0, 10).defaultIfEmpty(1).takeWhile { next -> next <= 5 }.subscribe  ({
            Log.d(TAG, "takeWhile onnext: $it")
        }, {
            Log.e(TAG, "takeWhile error: $it")
        }, {
            Log.d(TAG, "takeWhile done")
        })

        compositeDisposable.add(takeWhileObservable)

//        compositeDisposable.add(observable.subscribe {
//            Log.d(TAG, "ambWith subscribe: $it")
//        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}