package com.example.petadoptionapp

import java.io.Serializable


data class InterestedListModel(
    var petInterests: List<InterestedInClass>? = emptyList()
)

data class InterestedInClass(
    var userId: Int = 0,
    var petId: Int = 0,
    var interestId: Int = 0,
    var dateTimestamp: String = ""
): Serializable
