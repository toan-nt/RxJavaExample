package com.toantmt.rxjavaexample.conditonals

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.R
import com.toantmt.rxjavaexample.databinding.FragmentAmbConditionalsBinding
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class AmbConditionalFragment : Fragment() {

    private var _binding: FragmentAmbConditionalsBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = AmbConditionalFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAmbConditionalsBinding.inflate(inflater, container, false)
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}