package shira.chonbirth.sakaadmin.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.R
import shira.chonbirth.sakaadmin.components.errorDialog
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel

@Composable
fun Auth(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){
    val scrollState = rememberScrollState()

    Scaffold (
        content = {
            var onoff by remember {
                mutableStateOf(false)
            }
            var passcode = ""
            db.collection("passcode").document("code").get().addOnSuccessListener {
                passcode = it.get("key").toString()
            }
            val input = sharedViewModel.input
            errorDialog(sharedViewModel = sharedViewModel, openDialog = onoff, onYesClicked = { onoff = false})
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(R.drawable.icon_small), contentDescription = null, modifier = Modifier.size(100.dp))
                    Spacer(modifier = Modifier.size(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(value = input.value, onValueChange = {input.value = it}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Go))
                        IconButton(onClick = {
                            if (input.value == passcode){
                                navHostController.navigate("dashboard")
                            }else{
                                onoff = !onoff
                            }
                        }) {
                            Icon(imageVector = Icons.Default.ArrowForward , contentDescription = null)
                        }
                    }
                }
            }
        }
    )
}
