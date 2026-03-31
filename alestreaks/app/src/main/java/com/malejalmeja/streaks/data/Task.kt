package com.malejalmeja.streaks.data

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val icon: String,
    val color: String,
    val cadenceType: String = "daily",
    val targetCount: Int = 1,
    val active: Boolean = true
)
