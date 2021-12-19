package com.example.petadoptionapp

import java.io.Serializable

data class LoginHistoryDTO( var loginEntries: List<LoginHistoryItemDTO>? = emptyList()
)

data class LoginHistoryItemDTO(
    var loginTimestamp: Long = 0,
   ): Serializable