package com.toantmt.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ProductResponse(var items: List<ProductEntity> = arrayListOf())

@Entity(tableName = "product")
data class ProductEntity(@PrimaryKey var id: Long, var name: String)

@Entity(tableName = "product_detail")
data class ProductDetailEntity(@PrimaryKey var id: Long, var name: String, var url: String, var type: String)