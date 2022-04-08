package shira.chonbirth.sakaadmin

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.content.Context
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.delay
import shira.chonbirth.sakaadmin.components.generatePDF
import shira.chonbirth.sakaadmin.ui.theme.SakaAdminTheme
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.io.File

//private const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
//
//private fun foregroundPermissionApproved(context: Context): Boolean {
//    val writePermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//        context, Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//    val readPermissionFlag = PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
//        context, Manifest.permission.READ_EXTERNAL_STORAGE
//    )
//
//    return writePermissionFlag && readPermissionFlag
//}

//private fun requestForegroundPermission(context: Context) {
//    val provideRationale = foregroundPermissionApproved(context = context)
//    if (provideRationale) {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
//            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//        )
//    } else {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
//            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
//        )
//    }
//}

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    private val sharedViewModel: SharedViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            SakaAdminTheme {
                val context = LocalContext.current
//                requestForegroundPermission(context)

                val db = Firebase.firestore
                navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
//                    LaunchedEffect(key1 = true){
//                        delay(3000)
//                        generatePDF(context,getDirectory())
//                    }
//                    Toast.makeText(context, getDirectory().toString(), Toast.LENGTH_LONG).show()
                    SetupNavGraph(navHostController = navController, sharedViewModel = sharedViewModel, firestore = db, context = context)
                    sharedViewModel.splashScreen.value = false
                }
            }
        }
    }
    private fun getDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
}