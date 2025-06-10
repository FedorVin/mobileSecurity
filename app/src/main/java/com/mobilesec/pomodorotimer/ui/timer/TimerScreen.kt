package com.mobilesec.pomodorotimer.ui.timer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobilesec.pomodorotimer.viewmodel.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    onNavigateToTodos: () -> Unit
) {
    val timeRemaining by viewModel.timeRemaining.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val isWorkSession by viewModel.isWorkSession.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Header
        Text(
            text = if (isWorkSession) "Work Session" else "Break Time",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isWorkSession) Color(0xFF4CAF50) else Color(0xFF2196F3)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Timer Circle
        Card(
            modifier = Modifier.size(200.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = if (isWorkSession) Color(0xFF4CAF50) else Color(0xFF2196F3)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatTime(timeRemaining, isPremium)),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Control Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { if (isRunning) viewModel.pauseTimer() else viewModel.startTimer() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color(0xFFFF9800) else Color(0xFF4CAF50)
                )
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }

            Button(
                onClick = { viewModel.resetTimer() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf44336))
            ) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Premium Features
        if (!isPremium) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Premium Features Locked", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.upgradeToPremium() }
                    ) {
                        Text("Upgrade to Premium")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Navigation Button
        Button(
            onClick = onNavigateToTodos,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Todo List")
        }
    }
}

//
// private fun formatTime(seconds: Int): String {
//     val minutes = seconds / 60
//     val remainingSeconds = seconds % 60
//     return String.format("%02d:%02d", minutes, remainingSeconds)
// }

private fun formatTime(seconds: Int, showSeconds: Boolean = true): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (showSeconds) {
        String.format("%02d:%02d", minutes, remainingSeconds)
    } else {
        String.format("%02d:--", minutes)
    }
}

