package com.mobileSec.pomodoroapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobileSec.pomodoroapp.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val isPremium by viewModel.isPremium.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Text("←")
            }
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Premium Features",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isPremium) {
                    Text(
                        text = "✅ Premium Active",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Text("• Custom timer durations")
                    Text("• Extended session lengths")
                    Text("• Advanced statistics")
                } else {
                    Text("Get premium for:")
                    Text("• Custom timer durations (45, 60 minutes)")
                    Text("• Extended session lengths")
                    Text("• Advanced statistics")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // FRIDA can bypass this by calling viewModel.unlockPremium()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Upgrade to Premium - $9.99/month")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Debug section (would be hidden in production)
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Debug (Remove in production)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.unlockPremium() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Unlock Premium (Debug)")
                }
            }
        }
    }
}
