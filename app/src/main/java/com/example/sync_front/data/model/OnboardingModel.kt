package com.example.sync_front.data.model

data class OnboardingModel(
    val language: String,
    val userName: String,
    val countryName: String,
    val gender: String,
    val university: String,
    val email: String,
    val syncType: String,
    val categoryTypes: CategoryTypes
)