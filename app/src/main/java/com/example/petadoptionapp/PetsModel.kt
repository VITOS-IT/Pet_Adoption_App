package com.example.petadoptionapp


data class PetListModel(
    var pets: List<PetsModel>? = emptyList()
)


data class PetsModel(
    var id: Int = 1,
    var name: String = "",
    var url: String = "",
    var type: String = "",
    var age: Int = 0,
    var vaccinated: Int = 0
)
