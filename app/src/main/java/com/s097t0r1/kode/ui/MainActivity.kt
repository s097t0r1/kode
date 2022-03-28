package com.s097t0r1.kode.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.s097t0r1.data.model.DataUser
import com.s097t0r1.kode.ui.details.DETAILS_SCREEN
import com.s097t0r1.kode.ui.details.DetailsScreen
import com.s097t0r1.kode.ui.main.MAIN_SCREEN
import com.s097t0r1.kode.ui.main.MainScreen
import com.s097t0r1.kode.ui.theme.KodeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            KodeTheme {
                Scaffold {
                    NavHost(navController = navController, startDestination = MAIN_SCREEN) {
                        composable(MAIN_SCREEN) { MainScreen() }
                        composable(
                            DETAILS_SCREEN,
                            arguments = listOf(navArgument("user") {
                                type = NavType.ParcelableType(DataUser::class.java)
                            })
                        ) { navBackStackEntry ->
                            DetailsScreen(navBackStackEntry.arguments?.getParcelable("user"))
                        }
                    }
                }
            }
        }
    }
}


