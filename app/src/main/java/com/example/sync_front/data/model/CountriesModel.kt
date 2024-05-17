package com.example.sync_front.data.model

data class CountriesRequestModel(
    val page: String,
    val perPage: String,
    val language: String
)
data class CountriesResponse(
    val status: Int,
    val message: String,
    val data: List<String>
)