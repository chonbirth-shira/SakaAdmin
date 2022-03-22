package shira.chonbirth.sakaadmin.ui.screen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.ui.screen.mainscreen.JobComplete
import shira.chonbirth.sakaadmin.ui.screen.mainscreen.JobDTP
import shira.chonbirth.sakaadmin.ui.screen.mainscreen.JobList
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore, context: Context){
    val navControllerBotNav = rememberNavController()
    val navBackStackEntryBotNav by navControllerBotNav.currentBackStackEntryAsState()
    val currentRouteBotNav = navBackStackEntryBotNav?.destination?.route
    Scaffold(
        content = {
            NavHostContainer(navController = navControllerBotNav, sharedViewModel = sharedViewModel, db = db, navHostController = navHostController, context = context)
                  },
        bottomBar = {
            BottomNavigationBar(navController = navControllerBotNav, sharedViewModel = sharedViewModel)
        }
        ,
        floatingActionButton = {
            if (currentRouteBotNav == "joblist"){
                FloatingActionButton(onClick = {
                    navHostController.navigate("new_task_pro")
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationBar(navController: NavHostController, sharedViewModel: SharedViewModel) {
    BottomNavigation(
        elevation = BottomNavigationDefaults.Elevation,
        backgroundColor = MaterialTheme.colors.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomNavigationItem(
            selected = currentRoute == "joblist",
            onClick = { if (currentRoute != "joblist"){
                navController.navigate("joblist") {
                    navController.popBackStack()
                }
            }
            },
            icon = {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = "joblist")
            },
            label = {
                Text(text = "DTP ...")
            },
            alwaysShowLabel = true
        )

        BottomNavigationItem(
            selected = currentRoute == "jobdtp",
            onClick = { if (currentRoute != "jobdtp") {
                navController.navigate("jobdtp") {
                    navController.popBackStack()
                }
            }
            },
            icon = {
                if ( false){
                    BadgeBox( badgeContent = { Text(text = "12") }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "jobdtp")
                    }
                }else{
                    Icon(imageVector = Icons.Filled.Done, contentDescription = "jobdtp")
                }
            },
            label = {
                Text(text = "Ready to Print")
            },
            alwaysShowLabel = true
        )
        BottomNavigationItem(
            selected = currentRoute == "jobcomplete",
            onClick = { if (currentRoute != "jobcomplete") {
                navController.navigate("jobcomplete"){
                    navController.popBackStack()
                }
            }
            },
            icon = {
                if ( false){
                    BadgeBox( badgeContent = { Text(text = "12") }) {
                        Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "jobcomplete")
                    }
                }else{
                    Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "jobcomplete")
                }
            },
            label = {
                Text(text = "Completed")
            },
            alwaysShowLabel = true
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun NavHostContainer(
    navController: NavHostController,
    db: FirebaseFirestore,
    sharedViewModel: SharedViewModel,
    navHostController: NavHostController,
    context: Context
) {

    NavHost(
        navController = navController,
        startDestination = "joblist",

        builder = {
            composable(route = "joblist") {
                JobList(db = db, navHostController = navController, navHostController1 =navHostController, sharedViewModel = sharedViewModel, context = context)
//                navController.popBackStack("joblist",true)
//                navController.popBackStack("jobdtp",true)
//                navController.popBackStack("jobcomplete",true)
//                navController.popBackStack()
            }
            composable(route = "jobdtp") {
                JobDTP(db = db)
//                navController.popBackStack("joblist",true)
//                navController.popBackStack("jobdtp",true)
//                navController.popBackStack("jobcomplete",true)
            }
            composable(route = "jobcomplete") {
                JobComplete(db=db, navHostController = navHostController)
//                navController.popBackStack("joblist",true)
//                navController.popBackStack("jobdtp",true)
//                navController.popBackStack("jobcomplete",true)
            }
        })
}
