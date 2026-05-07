package me.rafa.arias.parksmart.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.rafa.arias.parksmart.repository.AuthRepository
import me.rafa.arias.parksmart.screens.*
import me.rafa.arias.parksmart.viewmodel.*


@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val startDestination = if (AuthRepository.isLoggedIn()) Routes.DASHBOARD else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Routes.LOGIN) {
            val vm: LoginViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.uiEvent.collect { event ->
                    when (event) {
                        LoginUiEvent.NavigateToDashboard -> navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                        LoginUiEvent.NavigateToRegister -> navController.navigate(Routes.REGISTER_STEP1)
                    }
                }
            }
            LoginScreen(viewModel = vm)
        }

        composable(Routes.REGISTER_STEP1) {
            val vm: RegisterStep1ViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.uiEvent.collect { event ->
                    when (event) {
                        RegisterStep1UiEvent.NavigateToStep2 -> navController.navigate(Routes.REGISTER_STEP2)
                    }
                }
            }
            RegisterStep1Screen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.REGISTER_STEP2) {
            val vm: RegisterStep2ViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.uiEvent.collect { event ->
                    when (event) {
                        is RegisterStep2UiEvent.NavigateToSuccess -> {
                            navController.navigate("${Routes.REGISTER_SUCCESS}/${event.nombre}/${event.cupos}") {
                                popUpTo(Routes.LOGIN) { inclusive = false }
                            }
                        }
                    }
                }
            }
            RegisterStep2Screen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("${Routes.REGISTER_SUCCESS}/{nombre}/{cupos}") { backStackEntry ->
            RegisterSuccessScreen(
                nombreParqueadero = backStackEntry.arguments?.getString("nombre") ?: "Mi Parqueadero",
                cupos = backStackEntry.arguments?.getString("cupos") ?: "0",
                onGoToDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.DASHBOARD) {
            val vm: DashboardViewModel = viewModel()
            DashboardScreen(
                viewModel = vm,
                onScanClick = { navController.navigate(Routes.SCANNER) },
                onCheckoutClick = { navController.navigate(Routes.CHECKOUT) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) },
                onReportsClick = { navController.navigate(Routes.REPORTS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }

        composable(Routes.SCANNER) {
            val vm: ScannerViewModel = viewModel()
            ScannerScreen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { navController.popBackStack() }
            )
        }

        composable(Routes.CHECKOUT) {
            val vm: CheckoutViewModel = viewModel()
            CheckoutScreen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            val vm: HistoryViewModel = viewModel()
            HistoryScreen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.DASHBOARD) { inclusive = false }
                }},
                onReportsClick = { navController.navigate(Routes.REPORTS) }
            )
        }

        composable(Routes.REPORTS) {
            val vm: ReportsViewModel = viewModel()
            ReportsScreen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() },
                onHomeClick = { navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.DASHBOARD) { inclusive = false }
                }},
                onHistoryClick = { navController.navigate(Routes.HISTORY) }
            )
        }

        composable(Routes.PROFILE) {
            val vm: ProfileViewModel = viewModel()
            LaunchedEffect(Unit) {
                vm.uiEvent.collect { event ->
                    when (event) {
                        ProfileUiEvent.NavigateToLogin -> navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
            ProfileScreen(
                viewModel = vm,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
