package com.tirexmurina.shared.user.source.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserLocalModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val password: String
)
