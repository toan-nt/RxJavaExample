package com.toantmt.rxjavaexample.subject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.toantmt.rxjavaexample.databinding.FragmentSubjectBinding
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.*
import java.io.IOException

class SubjectFragment : Fragment() {

    private var _binding: FragmentSubjectBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()

    private val asyncTask = AsyncSubject.create<Any>()
    private val publishSubject = PublishSubject.create<Any>().toSerialized()
    private val unicastSubject = UnicastSubject.create<Any>()
    private val replaySubject = ReplaySubject.create<Any>()
    private val behaviorSubject = BehaviorSubject.create<Any>()
    private var replayEventCount = 0

    companion object {
        val TAG = SubjectFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSubjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnAsync.setOnClickListener {
            initAsyncSubject()
        }

        binding.btnMoreAsync.setOnClickListener {
            initMoreSubscribeAsync()
        }

        binding.btnBehaviorSubject.setOnClickListener {
            initBehaviorSubject()
        }

        binding.btnMoreBehavior.setOnClickListener {
            initMoreBehavior()
        }

        binding.btnPublishSubject.setOnClickListener {
            initPublishSubject()
        }

        binding.btnReplaySubject.setOnClickListener {
            initReplaySubject()
        }

        binding.btnMoreReplaySubject.setOnClickListener {
            initMoreReplaySubject()
        }

        binding.btnMoreReplayEvent.setOnClickListener {
            initMoreReplayEvent()
        }

        binding.btnUnicastSubject.setOnClickListener {
            initUnicastSubject()
        }

        binding.btnMoreUnicastSubject.setOnClickListener {
            initMoreSubscribeUnicast()
        }
    }

    private fun initAsyncSubject() {
        Log.i(TAG, "initAsyncSubject")
        initSubscribeWith(asyncTask, "initAsyncSubject", "First")
        asyncTask.onNext("Nguyen")
        asyncTask.onNext("Van")
        asyncTask.onNext("Toan")
        asyncTask.onComplete()
    }

    private fun initMoreSubscribeAsync() {
        Log.i(TAG, "initMoreSubscribeAsync")
        initSubscribeWith(asyncTask, "initMoreSubscribeAsync", "Second")
    }

    private fun initBehaviorSubject() {
        initSubscribeWith(behaviorSubject, "initBehaviorSubject", "First")
        behaviorSubject.onNext("Nguyen")
        behaviorSubject.onNext("Van")
    }

    private fun initMoreBehavior() {
        initSubscribeWith(behaviorSubject, "initMoreBehavior", "Second")
        behaviorSubject.onNext("Toan")
    }

    private fun initPublishSubject() {
        Log.i(TAG, "initPublishSubject")
        initSubscribeWith(publishSubject, "initPublishSubject", "First")
        publishSubject.onNext("Nguyen")
        publishSubject.onNext("Van")
        //publishSubject.onError(IOException("Mr.Toan throws a error exception"))
        initSubscribeWith(publishSubject, "initPublishSubject", "Second")
        publishSubject.onNext("Toan")
    }

    private fun initReplaySubject() {
        replaySubject.onNext("Nguyen")
        replaySubject.onNext("Van")
        initSubscribeWith(replaySubject, "initReplaySubject", "First")
        replaySubject.onNext("Toan")
    }

    private fun initMoreReplaySubject() {
        initSubscribeWith(replaySubject, "initMoreReplaySubject", "Second")
    }

    private fun initMoreReplayEvent() {
        replayEventCount += 1
        replaySubject.onNext("More event: $replayEventCount")
    }

    private fun initUnicastSubject() {
        unicastSubject.onNext("Nguyen")
        unicastSubject.onNext("Van")
        initSubscribeWith(unicastSubject, "initUnicastSubject", "First")
    }

    private fun initMoreSubscribeUnicast() {
        initSubscribeWith(unicastSubject, "initMoreSubscribeUnicast", "Second")
        unicastSubject.onNext("Mr.Toan")
        unicastSubject.onError(IOException("Mr.Toan throws a exception to terminal unicast"))
        unicastSubject.onNext("more Mr.Toan")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> initSubscribeWith(subject: Subject<T>, funcName: String, ordinal: String) {
        compositeDisposable.add(subject.subscribe({
            Log.d(TAG, "$funcName, onNext - $ordinal: $it")
        }, {
            Log.e(TAG, "$funcName, onError - $ordinal: $it")
        }, {
            Log.d(TAG, "$funcName, onComplete - $ordinal")
        }))
    }
}