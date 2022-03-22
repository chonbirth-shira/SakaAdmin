package shira.chonbirth.sakaadmin

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.ui.screen.*
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun SetupNavGraph(navHostController: NavHostController, sharedViewModel: SharedViewModel, firestore: FirebaseFirestore, context: Context){
    NavHost(navController = navHostController, startDestination = Screen.Dashboard.route){
        composable(route = Screen.Dashboard.route){
            Dashboard(navHostController = navHostController, sharedViewModel = sharedViewModel, db = firestore)
        }
        composable(route = Screen.NewTask.route){
            NewTask(navHostController = navHostController, sharedViewModel = sharedViewModel, db = firestore )
        }
        composable(route = Screen.Orders.route){
            Orders(navHostController = navHostController, sharedViewModel = sharedViewModel, db = firestore)
        }
    }
}