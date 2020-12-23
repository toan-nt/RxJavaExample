package com.toantmt.rxjavaexample.compare

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentZipMergeBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ZipMergeFragment : Fragment() {

    private var _binding: FragmentZipMergeBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = ZipMergeFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZipMergeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view.context)
    }

    private fun initUI(context: Context) {
        binding.btnSchedules.setOnClickListener {
            //mainThreadSchedules()
            backgroundThreadSchedules()
        }

        binding.btnZip.setOnClickListener {
            zipExample()
        }

        binding.btnMerge.setOnClickListener {
            mergeExample()
            //combineExample()
        }
    }

    private fun mainThreadSchedules() {
        compositeDisposable.add(
            Observable.defer {
                Log.i("Observable thread", Thread.currentThread().name)
                Observable.just(1)
            }
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.computation())
                .map {
                    Log.i("Operator thread1", Thread.currentThread().name)
                    it.toString()
                }
                .subscribeOn(Schedulers.computation())
                .map {
                    Log.i("Operator thread2", Thread.currentThread().name)
                    it.toString() + "2222"
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.i("Subscriber thread", Thread.currentThread().name)
                }
        )
    }

    private fun backgroundThreadSchedules() {
        val thread = Thread {
            Log.i("Observable thread 1111", Thread.currentThread().name)
            mainThreadSchedules()
        }
        thread.start()
    }

    private fun zipExample() {
        Observable.zip(Observable.just(1), Observable.just(2), { value1, value2 ->
            DataZip(value1, value2)
        }).subscribe {
            Log.d(TAG, "Zip Example: $it")
        }
    }

    private fun mergeExample() {
        Observable.merge(Observable.just(1), Observable.just(2, 3, 4)).subscribe {
            Log.d(TAG, "Merge Example: $it")
        }
    }

    private fun combineExample() {
        Observable.combineLatest(Observable.just(1), Observable.just(2, 3, 4), { value1, value2 ->
            Log.d(TAG, "Combine Example: $value1, value2: $value2")
            value1 + value2
        }).subscribe {
            Log.d(TAG, "Combine Example: $it")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }

    inner class DataZip(var numberOne: Int, var numberTwo: Int) {
        override fun toString(): String {
            return "numberOne: $numberOne, numberTwo: $numberTwo"
        }
    }
}