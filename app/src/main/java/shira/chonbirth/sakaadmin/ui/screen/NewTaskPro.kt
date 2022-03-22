package shira.chonbirth.sakaadmin.ui.screen

import android.widget.Toast
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
import shira.chonbirth.sakaadmin.OptionDialog4
import shira.chonbirth.sakaadmin.data.Data
import shira.chonbirth.sakaadmin.components.showDatePicker
import shira.chonbirth.sakaadmin.data.CustDetail
import shira.chonbirth.sakaadmin.data.OrderItem
import shira.chonbirth.sakaadmin.ui.theme.Background
import shira.chonbirth.sakaadmin.ui.theme.PrimaryText
import shira.chonbirth.sakaadmin.ui.theme.Texture
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NewTaskPro(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var advance by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }
    var date by sharedViewModel.date
    val context = LocalContext.current

//    val order = sharedViewModel.order

    var onoff by remember {
        mutableStateOf(false)
    }
    AddItem(onoff, onYesClicked = {onoff = false}, sharedViewModel = sharedViewModel, 1 )

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
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        TextButton(onClick = {
                            if (id.length < 1 || name.length < 1 || contact.length < 1 || address.length < 1 || amount.length < 1 || advance.length < 1 || date == "DD/MM/YYYY"){
                                Toast.makeText(context,"Fill Required Details", Toast.LENGTH_SHORT).show()
                            } else {
                                val orderitem = sharedViewModel.order
                                val current = LocalDateTime.now()
                                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                val today = current.format(formatter)
                                db.collection("flexorders").document(id).set(CustDetail(job_id = id.toInt(), customer_name = name, customer_contact = contact, customer_address = address, job_status = "DTP", delivery_date = date, amount = amount.toInt(), advance = advance.toInt(), order_date = today , remarks = remark))
                                var perticular = ""
                                orderitem.forEach {
//                                db.collection("flexorders").document(id).collection("particulars").add(OrderItem(job_category = it.job_category, size = it.size, quantity = it.quantity, amount = it.amount))
//                                    perticular = perticular + if(perticular.length>2){","} else{""} + it.job_category + "_" + it.size + "_COPY_" + it.quantity
                                }
                                db.collection("flexorders").document(id).update("perticular",perticular)
//                            db.collection("ordersTest").document(id).collection("perticulars").add(OrderItem(job_category = orderitem[0].job_category, size = orderitem[0].size, quantity = orderitem[0].quantity, amount = orderitem[0].amount))
                                navHostController.navigateUp()
                                orderitem.clear()
                            }
                        }) {
                            Text(text = "ADD")
                        }
                    }
                }
            }
        }},
        content = {
            val order = sharedViewModel.order
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
                                Text(text = it.job_category)
//                                Text(text = "Size: ${it.size}")
//                                Text(text = "Quantity: ${it.quantity}")
                                Text(text = "Amount: ${it.amount}")
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
