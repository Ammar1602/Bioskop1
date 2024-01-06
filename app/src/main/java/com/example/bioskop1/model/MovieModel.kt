package com.example.bioskop1.model

import android.media.Image
import java.io.Serializable


class MovieModel (
    val movies : List<Movie>,
    val movie: Movie,
    val schedules: List<Schedulee>,
): Serializable

data class Movie(
    val created_at: String,
    val description: String,
    val duration: String,
    val genre: String,
    val id: Int,
    val image: String,
    val price: Double,
    val release_date: String,
    val title: String,
    val update_at: String,
): Serializable

data class Schedulee(
    val cinema_id: Int,
    val created_at: Any,
    val end_time: String,
    val id: Int,
    val movie_id: Int,
    val start_time: String,
    val update_at: Any,
):Serializable