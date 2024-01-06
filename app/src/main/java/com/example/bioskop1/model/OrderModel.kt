package com.example.bioskop1.model

data class OrderModel(
    val order: Order,
    val message: String
)

data class Order(
    val id: Int,
    val user_id: Int,
    val date: String,
    val created_at: String,
    val update_at: String
)
