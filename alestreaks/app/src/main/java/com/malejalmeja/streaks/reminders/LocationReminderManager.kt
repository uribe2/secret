package com.malejalmeja.streaks.reminders

import android.content.Context

class LocationReminderManager(private val context: Context) {
    fun registerGeofence(taskId: String, latitude: Double, longitude: Double, radiusMeters: Float) {
        // TODO: wire Android GeofencingClient + PendingIntent receiver.
        // This scaffold exists so geofence integration can be added quickly.
    }
}
