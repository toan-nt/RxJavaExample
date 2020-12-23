package com.toantmt.db.dao

import androidx.room.*
import com.toantmt.db.entities.ProductDetailEntity
import com.toantmt.db.entities.ProductEntity
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAll(): Observable<List<ProductEntity>>

    @Query("SELECT * FROM product_detail WHERE id LIKE :id")
    fun getProductDetailBy(id: Long): ProductDetailEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(productEntity: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProduct(productEntity: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProductDetail(productDetailEntity: List<ProductDetailEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductDetail(productDetailEntity: ProductDetailEntity)
}