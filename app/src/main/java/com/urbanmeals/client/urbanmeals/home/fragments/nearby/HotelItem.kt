package com.urbanmeals.client.urbanmeals.home.fragments.nearby

data class HotelItem(val name: String,
                     val code: String,
                     val imageUrl: String,
                     val distance: Double,
                     val place: String,
                     val rating: String,
                     val phone: String,
                     val lat: Double,
                     val lon: Double,
                     val description: String)