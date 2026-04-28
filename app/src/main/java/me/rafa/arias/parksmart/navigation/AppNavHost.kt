package me.rafa.arias.parksmart.navigation



import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.rafa.arias.parksmart.screens.CheckoutScreen
import me.rafa.arias.parksmart.screens.DashboardScreen
import me.rafa.arias.parksmart.screens.HistoryScreen
import me.rafa.arias.parksmart.screens.LoginScreen
import me.rafa.arias.parksmart.screens.RegisterStep1Screen
import me.rafa.arias.parksmart.screens.RegisterStep2Screen
import me.rafa.arias.parksmart.screens.RegisterSuccessScreen
import me.rafa.arias.parksmart.screens.ScannerScreen


@Composable
fun AppNavHost() {
    val navController = rememberNavController()


    var registerNombre by remember { mutableStateOf("") }
    var registerCedula by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerTelefono by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // ahorita navega directo al dashboard
                    //  despues tenog q  conectar Firebase Auth
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER_STEP1)
                }
            )
        }

        composable(Routes.REGISTER_STEP1) {
            RegisterStep1Screen(
                onNextClick = { nombre, cedula, email, telefono, password ->

                    registerNombre = nombre
                    registerCedula = cedula
                    registerEmail = email
                    registerTelefono = telefono
                    registerPassword = password
                    navController.navigate(Routes.REGISTER_STEP2)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.REGISTER_STEP2) {
            RegisterStep2Screen(
                onRegisterClick = { nombreParqueadero, direccion, ciudad, nit, cupos, tarifaCarro, tarifaMoto ->
                    // Aquí dsp conecto la  Firestore
                    navController.navigate(Routes.REGISTER_SUCCESS) {
                        popUpTo(Routes.LOGIN) { inclusive = false }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.REGISTER_STEP2) {
            RegisterStep2Screen(
                onRegisterClick = { nombreParqueadero, direccion, ciudad, nit, cupos, tarifaCarro, tarifaMoto ->
                    navController.navigate(
                        "${Routes.REGISTER_SUCCESS}/$nombreParqueadero/$cupos"
                    ) {
                        popUpTo(Routes.LOGIN) { inclusive = false }
                    }
                },
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
            DashboardScreen(
                onScanClick = { navController.navigate(Routes.SCANNER) },
                onCheckoutClick = { navController.navigate(Routes.CHECKOUT) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) },
                onReportsClick = { navController.navigate(Routes.REPORTS) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.SCANNER) {
            ScannerScreen(
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { navController.popBackStack() }
            )
        }
        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                onBackClick = { navController.popBackStack() },
                onConfirmClick = { navController.popBackStack() }
            )
        }

        composable(Routes.HISTORY) {
            HistoryScreen(
                onBackClick = { navController.popBackStack() }
            )
        }


    }
}