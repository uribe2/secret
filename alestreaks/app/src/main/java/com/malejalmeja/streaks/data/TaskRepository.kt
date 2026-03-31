package com.malejalmeja.streaks.data

import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun saveTask(task: Task) {
        // Structure: users/{uid}/tasks/{taskId}
        // For MVP scaffold we store under a placeholder user.
        val uid = "local-dev-user"
        firestore.collection("users")
            .document(uid)
            .collection("tasks")
            .document(task.id)
            .set(task)
    }
}
