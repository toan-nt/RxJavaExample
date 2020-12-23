package com.toantmt.rxjavaexample.combine

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.toantmt.adapter.MenuItemViewAdapter
import com.toantmt.rxjavaexample.databinding.FragmentCombineObservablesBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Emitter
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers

import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


class CombineObservableFragment : Fragment() {
    private var _binding: FragmentCombineObservablesBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()
    private val menuItemAdapter = MenuItemViewAdapter()

    companion object {
        val TAG = CombineObservableFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCombineObservablesBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilteringObservables(view.context)
    }

    private fun initFilteringObservables(context: Context) {

        menuItemAdapter.setItemSelectedListener { content ->
            val filterEnum = CombineEnum.getFilterEnum(content) ?: return@setItemSelectedListener
            initFilteringBy(filterEnum)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            menuItemAdapter.actualItems.clear()
            menuItemAdapter.actualItems.addAll(CombineEnum.values().map { it.name })
            adapter = menuItemAdapter
        }
    }

    private fun initFilteringBy(combineEnum: CombineEnum) {

    }

    private fun initDebounce() {
        Log.d(TAG, "initDebounce: init")
        val source = Observable.create<String> {
            it.onNext("A")
            Thread.sleep(1500)
            it.onNext("B")
            Thread.sleep(700)
            it.onNext("C")
            it.onComplete()
        }
        compositeDisposable.add(
            source
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(1, TimeUnit.SECONDS)
                .subscribe({
                    Log.d(TAG, "initDebounce: onNext: $it")
                }, { error ->
                    Log.e(TAG, "initDebounce: error: $error")
                }, {
                    Log.i(TAG, "initDebounce: onComplete")
                })
        )
    }

    private fun initDistinct() {
        Log.d(TAG, "initDistinct: init")
        val list = listOf(1, 1, 2, 3, 4, 6, 7, 8, 8, 4, 9)
        val listDistinct = list.distinct()
        Log.d(TAG, "initDistinct: listDistinct: $listDistinct")
        Observable.fromIterable(list).distinct().blockingSubscribe {
            Log.d(TAG, "initDistinct: blockingSubscribe: $it")
        }

        Log.d(TAG, "initDistinctUntilChanged")
        val list2 = listOf(1, 1, 1, 2, 1, 2, 3, 3, 4)
        Observable.fromIterable(list2).distinctUntilChanged().blockingSubscribe {
            Log.d(TAG, "distinctUntilChanged: blockingSubscribe: $it")
        }
    }

//    private fun initElementAt() {
//        Log.d(TAG, "initElementAt")
//        val source = Observable.generate(
//            Callable { 1L },
//            BiFunction { state: Long, emitter: Emitter<Long> ->
//                Log.d(TAG, "generate: $state")
//                emitter.onNext(state)
//                state + 1L
//            }).scan { product: Long, x: Long -> product + x }
//
//        val element = source.elementAt(10)
//        compositeDisposable.add(element.subscribe { o: Long? -> Log.d(TAG, "elementAt: $o") })
//
//        Log.d(TAG, "elementAtOrError")
//        val source2 = Observable.just("Kirk", "Spock", "Chekov", "Sulu")
//        val element2: Single<String> = source2.elementAtOrError(4)
//
//        compositeDisposable.add(element2.subscribe(
//            { name -> println("onSuccess will not be printed!") }
//        ) { error -> println("onError: $error") })
//    }

    private fun initFilter() {
        Log.d(TAG, "initFilter")

    }

    private fun initFirst() {
        Log.d(TAG, "initFirst")
        val source = Observable.just("A", "B", "C")
        val firstOrDefault = source.first("D")
        compositeDisposable.add(firstOrDefault.subscribe({
            Log.d(TAG, "initFirst, subscribe: $it")
        }, {
            Log.e(TAG, "initFirst, error: $it")
        }))

        Log.d(TAG, "firstElement")
        val source1 = Observable.just(1)
        val firstOrDefault1 = source1.firstElement()
        compositeDisposable.add(firstOrDefault1.subscribe({
            Log.d(TAG, "firstElement, subscribe: $it")
        }, {
            Log.e(TAG, "firstElement, error: $it")
        }))
    }

    private fun initIgnoreElements() {
        Log.d(TAG, "initIgnoreElements")
        val source = Observable.just("A", "B", "C")
        val firstOrDefault = source.ignoreElements()
        firstOrDefault.doOnComplete {  }.blockingAwait()

    }

    private fun initLast() {

    }

    private fun initSample() {
        Log.d(TAG, "ofType")
        val numbers = Observable.just<Number>(1, 4.0, 3, 2.71, 2f, 7)
        val integers = numbers.ofType(Int::class.java)

        compositeDisposable.add(integers.subscribe { x: Int -> Log.d(TAG, "ofType: $x") })

        val source = Observable.create<Any> {
            it.onNext("Toan")
            it.onNext(CombineEnum.combineLatest)
            it.onNext(12121)
            it.onComplete()
        }.publish()

        compositeDisposable.add(source.subscribe {
            Log.e(TAG, "initSample : $it")
        })
    }

    private fun initSkip() {

    }

    private fun initSkipLast() {

    }

    private fun initTake() {

    }

    private fun initTakeLast() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
        compositeDisposable.clear()
    }

    enum class CombineEnum {
        combineLatest,
        join_and_groupJoin,
        merge,
        mergeDelayError,
        rxjava_joins,
        startWith,
        switchOnNext,
        zip;

        companion object {
            fun getFilterEnum(content: String): CombineEnum? {
                return values().find { it.name == content }
            }
        }
    }

}