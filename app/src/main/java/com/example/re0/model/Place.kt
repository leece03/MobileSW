package com.example.re0.model

data class Place(
    val name: String = "",
    val address: String = "",
    val description: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val type: PlaceType  =PlaceType.ZERO_WASTE,
    var length: String = "0.0",
)
