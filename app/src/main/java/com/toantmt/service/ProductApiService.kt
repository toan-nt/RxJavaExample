package com.toantmt.service

import com.toantmt.db.entities.ProductDetailEntity
import com.toantmt.db.entities.ProductResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ProductApiService {
    @GET("/items")
    fun getItemsRxjava(): Observable<ProductResponse>

    @GET("/items")
    fun getItems(): Call<ProductResponse>

    @GET("/items/{id}")
    fun getItemDetailBy(@Path("id") id: Long): Observable<ProductDetailEntity>
}