package com.example.sync_front.data.model


data class CategoryTypes(
    val foreignLanguage: ForeignLanguage,
    val cultureArt: CultureArt,
    val travelCompanion: TravelCompanion,
    val activity: Activity,
    val foodAndDrink: FoodAndDrink,
    val etc: Etc
)

data class ForeignLanguage(
    val languageExchange: Boolean,
    val tutoring: Boolean,
    val study: Boolean
)

data class CultureArt(
    val movie: Boolean,
    val drama: Boolean,
    val art: Boolean,
    val performance: Boolean,
    val music: Boolean
)

data class TravelCompanion(
    val sightseeing: Boolean,
    val nature: Boolean,
    val vacation: Boolean
)

data class Activity(
    val running: Boolean,
    val hiking: Boolean,
    val climbing: Boolean,
    val bike: Boolean,
    val soccer: Boolean,
    val surfing: Boolean,
    val tennis: Boolean,
    val bowling: Boolean,
    val tableTennis: Boolean
)

data class FoodAndDrink(
    val restaurant: Boolean,
    val cafe: Boolean,
    val drink: Boolean
)

data class Etc(
    val etc: Boolean
)