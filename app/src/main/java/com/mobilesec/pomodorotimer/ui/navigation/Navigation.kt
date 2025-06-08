package com.mobilesec.pomodorotimer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mobilesec.pomodorotimer.ui.timer.TimerScreen
import com.mobilesec.pomodorotimer.ui.todolist.TodoListScreen
import com.mobilesec.pomodorotimer.viewmodel.TimerViewModel
import com.mobilesec.pomodorotimer.viewmodel.TodoViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    timerViewModel: TimerViewModel,
    todoViewModel: TodoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "timer"
    ) {
        composable("timer") {
            TimerScreen(
                viewModel = timerViewModel,
                onNavigateToTodos = { navController.navigate("todos") }
            )
        }
        composable("todos") {
            TodoListScreen(
                viewModel = todoViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
