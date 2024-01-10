package com.example.bioskop1.model

import java.io.Serializable

data class TicketModel(
    val tickets: List<Ticket>
)

data class Ticket(
    val movie_title: String,
    val movie_price: String,
    val movie_image: String,
    val cinema: String,
    val seat: Int,
    val date: String,
    val start_time: String,
    val end_time: String,
    val status: String
): Serializable
