package com.toantmt.rxjavaexample.product

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.toantmt.adapter.ProductItemAdapter
import com.toantmt.service.Auth
import com.toantmt.db.AppDatabase
import com.toantmt.db.entities.ProductDetailEntity
import com.toantmt.db.entities.ProductResponse
import com.toantmt.rxjavaexample.databinding.FragmentProductItemsBinding
import com.toantmt.service.ProductApiService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductItemsFragment: Fragment() {

    private var _binding: FragmentProductItemsBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable = CompositeDisposable()
    private val productItemAdapter = ProductItemAdapter()

    companion object {
        val TAG = ProductItemsFragment::class.java.simpleName
        const val LINE_SEPARATE = "\n"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view.context)
        registerObservables()
    }

    private fun initUI(context: Context) {

        productItemAdapter.setItemSelectedListener {
            Log.e(TAG, "selected item: $it")
            val copyProduct = it.copy(name = "product ${it.id} change")
            AppDatabase.get().productDao().updateProduct(copyProduct)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = productItemAdapter
        }


//        Auth.getApiService(ProductApiService::class.java)
//            .getItems().enqueue(object: Callback<ProductResponse> {
//                override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
//                    Log.d(TAG, "response code: ${response.code()}");
//                    if (response.isSuccessful) {
//                        val response = response.body() ?: return
////                        AppDatabase.get().productDao().insertAllProduct(response.items)
//                        Log.d(TAG, "response: $response")
//                    }
//                }
//
//                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
//                    call.cancel()
//                }
//            })


        compositeDisposable.add(
            Auth.getApiService(ProductApiService::class.java)
                .getItemsRxjava()
                .subscribeOn(Schedulers.io())
                .flatMap {
                    Log.e(TAG, "item response")
                    AppDatabase.get().productDao().insertAllProduct(it.items)
                    Observable.fromIterable(it.items)
                }
                .flatMap {
                    Auth.getApiService(ProductApiService::class.java).getItemDetailBy(it.id)
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    Log.d(TAG, "product: $it")
                    AppDatabase.get().productDao().insertAllProductDetail(it)
                }, {
                    Log.e(TAG, "error: $it")
                })
        )

        // update UI from local data
        compositeDisposable.add(
            AppDatabase.get().productDao()
                .getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    Log.d(TAG, "local product size: ${it.size}")
                    productItemAdapter.mergeItems(it)
                }, {

                })
        )


    }

    private fun registerObservables() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()
    }
}