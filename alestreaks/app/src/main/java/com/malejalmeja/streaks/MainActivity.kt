package com.malejalmeja.streaks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.malejalmeja.streaks.data.Task
import com.malejalmeja.streaks.data.TaskRepository

class MainActivity : ComponentActivity() {
    private val taskRepository = TaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                AleStreaksHome(taskRepository)
            }
        }
    }
}

@Composable
private fun AleStreaksHome(taskRepository: TaskRepository) {
    val tasks = remember { mutableStateListOf<Task>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "AleStreaks", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Simple daily streak tracker (MVP scaffold).")

        Button(onClick = {
            val sample = Task(title = "Drink water", icon = "💧", color = "#3AA5FF")
            taskRepository.saveTask(sample)
            tasks.add(sample)
        }) {
            Text("Add sample task")
        }

        tasks.forEach { task ->
            Text("${task.icon} ${task.title}")
        }
    }
}
