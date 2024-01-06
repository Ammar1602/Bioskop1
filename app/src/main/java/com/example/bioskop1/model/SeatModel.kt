package com.example.bioskop1.model

data class SeatModel(
    val seats: List<Seat>
)

data class Seat(
    val id: Int,
    val status: String
)
