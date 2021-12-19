package com.example.petadoptionapp

import java.io.Serializable

data class UsersData ( var users: List<UserData>? = emptyList())

data class UserData (
    var email:String = "",
    var reservationsAt:String = ""
) : Serializable
