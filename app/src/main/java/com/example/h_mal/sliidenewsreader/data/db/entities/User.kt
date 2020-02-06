package com.example.h_mal.sliidenewsreader.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val username: String,
    val password: String,
    val role: String,
    val state: Boolean
)