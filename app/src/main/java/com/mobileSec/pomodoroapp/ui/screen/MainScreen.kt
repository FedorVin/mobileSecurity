package com.mobileSec.pomodoroapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobileSec.pomodoroapp.data.model.Todo
import com.mobileSec.pomodoroapp.viewmodel.MainViewModel
import com.mobileSec.pomodoroapp.viewmodel.TimerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToSettings: () -> Unit
) {
    val timerState by viewModel.timerState.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val todos by viewModel.todos.collectAsState()
    val selectedTodo by viewModel.selectedTodo.collectAsState()
    val isBreakMode by viewModel.isBreakMode.collectAsState()
    val workSessionsToday by viewModel.workSessionsToday.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()

    var showAddTodoDialog by remember { mutableStateOf(false) }
    var todoTitle by remember { mutableStateOf("") }
    var todoDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pomodoro Timer",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Row {
                if (isPremium) {
                    Badge {
                        Text("PREMIUM")
                    }
                }
                IconButton(onClick = onNavigateToSettings) {
                    Text("⚙️")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Timer display
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isBreakMode) "Break Time" else "Work Time",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = formatTime(timeRemaining),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                selectedTodo?.let { todo ->
                    Text(
                        text = "Working on: ${todo.title}",
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (timerState) {
                        TimerState.IDLE -> {
                            Button(
                                onClick = { viewModel.startTimer() },
                                enabled = selectedTodo != null || isBreakMode
                            ) {
                                Text("Start")
                            }
                        }
                        TimerState.RUNNING -> {
                            Button(onClick = { viewModel.pauseTimer() }) {
                                Text("Pause")
                            }
                        }
                        TimerState.PAUSED -> {
                            Button(onClick = { viewModel.startTimer() }) {
                                Text("Resume")
                            }
                        }
                        TimerState.COMPLETED -> {
                            Button(onClick = { viewModel.resetTimer() }) {
                                Text("Reset")
                            }
                        }
                    }

                    OutlinedButton(onClick = { viewModel.resetTimer() }) {
                        Text("Reset")
                    }
                }

                if (isPremium) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.setCustomTimer(45) },
                            modifier = Modifier.size(width = 60.dp, height = 36.dp)
                        ) {
                            Text("45m", fontSize = 12.sp)
                        }
                        Button(
                            onClick = { viewModel.setCustomTimer(60) },
                            modifier = Modifier.size(width = 60.dp, height = 36.dp)
                        ) {
                            Text("60m", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Text(
            text = "Today's work sessions: $workSessionsToday",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Todo list
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Todo List",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { showAddTodoDialog = true }
            ) {
                Text("Add Todo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    isSelected = selectedTodo?.id == todo.id,
                    onSelect = { viewModel.selectTodo(todo) },
                    onToggleComplete = { viewModel.toggleTodoComplete(todo) }
                )
            }
        }
    }

    // Add todo dialog
    if (showAddTodoDialog) {
        AlertDialog(
            onDismissRequest = { showAddTodoDialog = false },
            title = { Text("Add New Todo") },
            text = {
                Column {
                    TextField(
                        value = todoTitle,
                        onValueChange = { todoTitle = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = todoDescription,
                        onValueChange = { todoDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (todoTitle.isNotBlank()) {
                            viewModel.addTodo(todoTitle, todoDescription)
                            todoTitle = ""
                            todoDescription = ""
                            showAddTodoDialog = false
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTodoDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onToggleComplete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = todo.title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
                if (todo.description.isNotBlank()) {
                    Text(
                        text = todo.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "🍅 ${todo.pomodorosCompleted}",
                    fontSize = 12.sp
                )
            }

            if (!todo.isCompleted) {
                Button(
                    onClick = onSelect,
                    modifier = Modifier.size(width = 80.dp, height = 36.dp)
                ) {
                    Text("Select", fontSize = 12.sp)
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

