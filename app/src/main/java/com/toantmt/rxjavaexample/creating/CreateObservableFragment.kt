package com.toantmt.rxjavaexample.creating

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.R
import com.toantmt.rxjavaexample.databinding.FragmentCreateObservableBinding
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CreateObservableFragment : Fragment() {

    private var _binding: FragmentCreateObservableBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    companion object {
        val TAG = CreateObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCreateObservableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnCreate.setOnClickListener {
            create()
        }
    }

    private fun create() {
        val todos = resources.getStringArray(R.array.rx_todos)
        val observable = Observable.create<String> { emitter ->
            try {
                todos.forEach {
                    emitter.onNext(it)
                }
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

        compositeDisposable.add(observable.subscribe ({
            Log.d(TAG, "create, subscribe: $it")
            binding.txtResult.append(it)
            binding.txtResult.append(LINE_SEPARATE)
        }, { error ->
            Log.e(TAG, "create, error: $error")
        }, {
            Log.d(TAG, "create, done")
        }))

        val executor = Executors.newSingleThreadScheduledExecutor()
        val handler: ObservableOnSubscribe<String> =  ObservableOnSubscribe { emitter ->
            val future = executor.schedule({
                todos.forEach {
                    emitter.onNext(it)
                }
                emitter.onComplete()
                null
            }, 1, TimeUnit.SECONDS)

            emitter.setCancellable { future.cancel(false) }
        }

        val todoObservables = Observable.create(handler)
        compositeDisposable.add(todoObservables.doOnNext {
            Log.d(TAG, "todoObservables, doOnNext: $it")
        }.filter {
            it.contains("Todo 12")
        }.doAfterNext {
            Log.d(TAG, "todoObservables, doAfterNext: $it")
        }.doOnComplete {
            Log.d(TAG, "todoObservables, doOnComplete:")
        }.doOnSubscribe {
            Log.d(TAG, "todoObservables, doOnSubscribe:")
        }.subscribe({
            Log.d(TAG, "todoObservables, subscribe: $it")
        }, {error ->
            Log.e(TAG, "todoObservables, error: $error")
        }, {
            Log.d(TAG, "todoObservables, done")
        }))

        Thread.sleep(2000);
        executor.shutdown();

        val todoMaybe = Maybe.create<List<String>> { emitter ->
            try {
                todos.toList().apply {
                    if (isNotEmpty()) {
                        emitter.onSuccess(todos.toList())
                    } else {
                        emitter.onComplete()
                    }
                }

            } catch (e: Exception) {
                emitter.onError(e)
            }
        }

        compositeDisposable.add(todoMaybe.filter { it.contains("Todo 12") }.subscribe {
            Log.d(TAG, "todoMaybe, subscribe: $it")
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}