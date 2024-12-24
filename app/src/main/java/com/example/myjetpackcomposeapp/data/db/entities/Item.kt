package com.example.myjetpackcomposeapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    val categoryId: Int,

    val shortName: String,

    val numberOrDose: String? = null,
    val volumeOrCapacity: String? = null,
    val price: Double? = null,
    val isPrescription: Boolean? = null,
    val additionalInfo: String? = null
)
