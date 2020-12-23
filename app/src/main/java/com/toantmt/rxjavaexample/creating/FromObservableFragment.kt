package com.toantmt.rxjavaexample.creating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentFromObservableBinding
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Action
import java.io.IOException
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class FromObservableFragment: Fragment() {
    private var _binding: FragmentFromObservableBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()
    private var count = 0;

    companion object {
        val TAG = FromObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFromObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            rangeDeferObservable()
        }
    }

    private fun rangeDeferObservable() {
        val list: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        val observable = Observable.fromIterable(list)
        val connectObservable = observable.publish()

        compositeDisposable.add(connectObservable.map {
            "value : $it"
        }.subscribe {
            Log.i(TAG, "subscribe observable: long1: $it")
        })

        compositeDisposable.add(connectObservable.subscribe {
            Log.i(TAG, "subscribe observable: long2: $it")
        })

        connectObservable.connect()

        // from Array: signals the elements of the given array and then complete the sequence
        val arrayObservable = Observable.fromArray(list)
        compositeDisposable.add(arrayObservable.subscribe {
            Log.i(TAG, "subscribe arrayObservable: $it")
        })

        // from callable
        val callabe = Callable {
             if (count == 0) {
                count += 1
                val content = "Hello world: $count"
                 Log.d(TAG, content)
                 content
            } else {
                count = 0
                val content = "exception: number of count: $count"
                 Log.d(TAG, content)
                 throw IOException(content)
            }
        }

        val callableObservable = Completable.fromCallable(callabe)
        compositeDisposable.add(callableObservable.subscribe({
            Log.d(TAG, "callable subscribe:")
        }, { error ->
            Log.e(TAG, "callable error: $error")
        }))

        // from Action
        val action = Action {
            throw IOException("action exception")
        }

        val maybeAction = Maybe.fromAction<String>(action)
        compositeDisposable.add(maybeAction.subscribe({
            Log.d(TAG, "maybeAction subscribe: $it")
        }, { error ->
            Log.e(TAG, "maybeAction error: $error")
        }))

        val executor = Executors.newSingleThreadScheduledExecutor()
        val future = executor.schedule<Any>({ "Hello world: $count!" }, 0, TimeUnit.SECONDS)
        Log.e(TAG, "maybeAction error: $1")
        val futureObservable = Observable.fromFuture<Any>(future).repeatUntil { count == 5 }
        compositeDisposable.add(futureObservable
            .doOnNext { count += 1 }
            .subscribe {
            Log.d(TAG, "futureObservable subscribe: $count")
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }
}