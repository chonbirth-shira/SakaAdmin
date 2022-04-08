package shira.chonbirth.sakaadmin.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.AddItem
import shira.chonbirth.sakaadmin.data.Data
import shira.chonbirth.sakaadmin.components.showDatePicker
import shira.chonbirth.sakaadmin.data.AddOrder
import shira.chonbirth.sakaadmin.ui.theme.Background
import shira.chonbirth.sakaadmin.ui.theme.PrimaryText
import shira.chonbirth.sakaadmin.ui.theme.Texture
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewTask(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var advance by remember { mutableStateOf("") }
    var particulars by remember { mutableStateOf(ArrayList<String>(emptyList())) }
    var description by remember { mutableStateOf("") }
    var date by sharedViewModel.date
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if(expanded) 180f else 0f
    )

    val scrollState = rememberScrollState()
    val appbarElevation = if (scrollState.value > 1) 2.dp else 0.dp
    var onoff by remember {
        mutableStateOf(false)
    }
    AddItem(onoff, onYesClicked = {onoff = false}, sharedViewModel = sharedViewModel, 1 )

    val order = sharedViewModel.order
    BackHandler(enabled = true) {
        navHostController.navigateUp()
        sharedViewModel.order.clear()
    }
    Scaffold (
        topBar = {
            Surface(elevation = 1.dp, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.Texture) {
            Column() {
                Text(text = "SAKA Offset Printing", modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 2.dp), style = MaterialTheme.typography.body2, color = MaterialTheme.colors.primary)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Add Job", modifier = Modifier.padding(start = 10.dp, bottom = 14.dp, end = 20.dp), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.PrimaryText)
                    Row(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)) {
                        TextButton(onClick = {
                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val today = current.format(formatter)
                            var description = ""
                            order.forEach {
                                if (description.isEmpty()){
                                    description = it.job_category
                                }else{
                                    description = description +", "+ it.job_category
                                }
                            }
                            db.collection("orders").document(id).set(AddOrder(job_id = id, customer_name = name, customer_contact = contact, customer_address = address, status = "new", delivery_date = date, total = amount.toInt(), advance = advance.toInt(), order_date = today, description = description, particulars = particulars, flex="", press="", digital=""))
                            navHostController.navigateUp()
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }},
        content = {
            LazyColumn(modifier = Modifier
                .background(color = MaterialTheme.colors.Background)
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp)){
                item {
                    OutlinedTextField(value = id, onValueChange = {id = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "JOB ID")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None, autoCorrect = false, keyboardType = KeyboardType.Number))

                    OutlinedTextField(value = name, onValueChange = {name = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "NAME")})
                    OutlinedTextField(value = address, onValueChange = {address = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "ADDRESS")})
                    OutlinedTextField(value = contact, onValueChange = {contact = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "CONTACT")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None, autoCorrect = false, keyboardType = KeyboardType.Number))
                    showDatePicker(context = context, sharedViewModel = sharedViewModel)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Orders (${order.size})", style = MaterialTheme.typography.h6)
                        TextButton(onClick = {
                            onoff = !onoff
                        }) {
                            Text(text = "Add")
                        }
                    }
                }
                items(order){
                    Surface(elevation = 1.dp, modifier = Modifier.padding(bottom = 4.dp)) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column() {
                                Text(text = "${order.indexOf(it)+1} - ${it.job_category}", style = MaterialTheme.typography.subtitle1)
                                Text(text = it.description, modifier = Modifier.padding(start = 18.dp), style = MaterialTheme.typography.subtitle2)
                                Text(text = "Rs. ${it.amount}", modifier = Modifier.padding(start = 18.dp), style = MaterialTheme.typography.subtitle2)
                            }
                            TextButton(onClick = { order.remove(it) }) {
                                Text(text = "Remove")
                            }
                        }
                    }
                }
                item {
                    var total = 0
                    order.forEach{
                        total = total + it.amount
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Total : ${total}", style = MaterialTheme.typography.h6)
                    }
                    amount = total.toString()
                    OutlinedTextField(value = advance, onValueChange = {advance = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "ADVANCE")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None, autoCorrect = false, keyboardType = KeyboardType.Number))
                    Spacer(modifier = Modifier.size(60.dp))
                }
            }
        }
    )
}
