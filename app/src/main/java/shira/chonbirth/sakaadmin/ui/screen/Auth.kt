package shira.chonbirth.sakaadmin.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.components.showDatePicker
import shira.chonbirth.sakaadmin.components.showDatePicker1
import shira.chonbirth.sakaadmin.data.Data
import shira.chonbirth.sakaadmin.ui.theme.*
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Auth(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){
    val scrollState = rememberScrollState()

    Scaffold (
        content = {
            Column(modifier = Modifier
                .background(color = MaterialTheme.colors.Background)
                .verticalScroll(state = scrollState)
                .fillMaxSize()
                .padding(10.dp)) {
                Column(modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .background(color = MaterialTheme.colors.Items)
                    .fillMaxWidth()
                    .padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(text = "Today", style = MaterialTheme.typography.h6)
                    }
                    Divider(Modifier.padding(top = 6.dp, bottom = 4.dp))
                    Row() {
                        Column() {
                            Text(text = "Flex")
                            Text(text = "Offset")
                            Text(text = "Expense")
                        }
                        Column() {
                            Text(text = ":")
                            Text(text = ":")
                            Text(text = ":")
                        }
                        Column() {
                            Text(text = "Rs. 100")
                            Text(text = "Rs. 100")
                            Text(text = "Rs. 100")
                        }
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable {
                        navHostController.navigate("orders")
                    }
                    .background(color = MaterialTheme.colors.Items)
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column() {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Rounded.List, contentDescription = null, modifier = Modifier.padding(6.dp))
                            Text(text = "Orders", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 10.dp))
                        }
                    }
                    var size = remember {
                        mutableStateOf("")
                    }
                    db.collection("orders").whereEqualTo("status", "new").get().addOnSuccessListener {
                        size.value = it.size().toString()
                    }
                    Text(text = size.value, style = MaterialTheme.typography.h6)
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable { }
                    .background(color = MaterialTheme.colors.Items)
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.padding(6.dp))
                    Text(text = "Order Completed", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 10.dp))
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable { }
                    .background(color = MaterialTheme.colors.Items)
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.padding(6.dp))
                    Text(text = "Order Issued", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 10.dp))
                }
                Spacer(modifier = Modifier.size(10.dp))
                Row(modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable { }
                    .background(color = MaterialTheme.colors.Items)
                    .fillMaxWidth()
                    .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier.padding(6.dp))
                    Text(text = "Settings", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 10.dp))
                }
            }
        }
    )
}
