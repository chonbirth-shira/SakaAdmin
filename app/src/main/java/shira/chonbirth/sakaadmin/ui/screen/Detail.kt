package shira.chonbirth.sakaadmin.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import shira.chonbirth.sakaadmin.components.showDatePicker
import shira.chonbirth.sakaadmin.components.showDatePicker1
import shira.chonbirth.sakaadmin.data.Data
import shira.chonbirth.sakaadmin.ui.theme.Background
import shira.chonbirth.sakaadmin.ui.theme.PrimaryText
import shira.chonbirth.sakaadmin.ui.theme.Texture
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun Detail(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){

    val jobId = sharedViewModel.selected_jobId.value

    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var jobtype by remember { mutableStateOf("Select") }
    var amount by remember { mutableStateOf("") }
    var advance by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }
    var deldate by remember { mutableStateOf("") }
    var case by remember { mutableStateOf(false) }
    var width by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if(expanded) 180f else 0f
    )

    val scrollState = rememberScrollState()
    val appbarElevation = if (scrollState.value > 1) 2.dp else 0.dp

    db.collection("orders").document(jobId).get().addOnSuccessListener {
        id = it.get("job_id").toString()
        name = it.get("customer_name").toString()
        contact = it.get("customer_name").toString()
        address = it.get("customer_address").toString()
        jobtype = it.get("job_category").toString()
        amount = it.get("amount").toString()
        advance = it.get("advance").toString()
        remark = it.get("remark").toString()
        deldate = it.get("delivery_date").toString()
        case = it.get("case") == true
        width = it.get("size").toString().split("x")[0]
        height = it.get("size").toString().split("x")[1]
    }

    Scaffold (
        topBar = {
            Surface(elevation = 1.dp, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.Texture) {
            Column() {
                Text(text = "SAKA Offset Printing", modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 2.dp), style = MaterialTheme.typography.body2, color = MaterialTheme.colors.primary)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Details", modifier = Modifier.padding(start = 10.dp, bottom = 14.dp, end = 20.dp), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.PrimaryText)
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        TextButton(onClick = {
                            val current = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            val today = current.format(formatter)
                            db.collection("orders").document(id).set(Data(job_id = id.toInt(), job_category = jobtype, customer_name = name, customer_contact = contact, customer_address = address, job_status = "DTP", delivery_date = deldate, amount = amount.toInt(), advance = advance.toInt(), order_date = today , remarks = remark, case = case, job_description = "", size = width+"x"+height, image = "", issue_date = ""))
                            navHostController.navigateUp()
                        }) {
                            Text(text = "UPDATE")
                        }
                    }
                }
            }
        }},
        content = {
            Column(modifier = Modifier
                .background(color = MaterialTheme.colors.Background)
                .verticalScroll(state = scrollState)
                .fillMaxWidth()
                .padding(10.dp)) {
                Row() {
                    OutlinedTextField(value = id, onValueChange = {id = it}, modifier = Modifier.fillMaxWidth(0.3F), label = { Text(text = "JOB ID")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number))
                    Spacer(modifier = Modifier.size(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1F)
                            .padding(top = 8.dp)
                            .height(56.dp)
                            .clickable { expanded = true }
                            .border(
                                1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                                shape = MaterialTheme.shapes.small
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = jobtype, style = MaterialTheme.typography.subtitle1, modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(
                                1f
                            ))
                        IconButton(modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .rotate(angle), onClick = {expanded = true}) {
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
                            DropdownMenuItem(onClick = {
                                jobtype = "Flex"
                                expanded = false
                            }) {
                                Text(text = "Flex")
                            }
                            DropdownMenuItem(onClick = {
                                jobtype = "Star Flex"
                                expanded = false
                            }) {
                                Text(text = "Star Flex")
                            }
                            DropdownMenuItem(onClick = {
                                jobtype = "Vinyl"
                                expanded = false
                            }) {
                                Text(text = "Vinyl")
                            }
                        }
                    }
                }
                OutlinedTextField(value = name, onValueChange = {name = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "NAME")})
                OutlinedTextField(value = address, onValueChange = {address = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "ADDRESS")})
                OutlinedTextField(value = contact, onValueChange = {contact = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "CONTACT")}, keyboardOptions = KeyboardOptions(
                    KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number))
                Text(text = "SIZE", modifier = Modifier.padding(top = 10.dp))
                Row() {
                    OutlinedTextField(value = width, onValueChange = {width = it}, modifier = Modifier.fillMaxWidth(0.48F), label = { Text(text = "Width")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number))
                    Spacer(modifier = Modifier.size(8.dp))
                    OutlinedTextField(value = height, onValueChange = {height = it}, modifier = Modifier.fillMaxWidth(1F), label = { Text(text = "Height")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number))
                }
                Text(text = "PRICE", modifier = Modifier.padding(top = 10.dp))
                Row() {
                    OutlinedTextField(value = amount, onValueChange = {amount = it}, modifier = Modifier.fillMaxWidth(0.48F), label = { Text(text = "Amount")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number))
                    Spacer(modifier = Modifier.size(8.dp))
                    OutlinedTextField(value = advance, onValueChange = {advance = it}, modifier = Modifier.fillMaxWidth(1F), label = { Text(text = "Advance")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number))
                }
//        OutlinedTextField(value = date, onValueChange = {date = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "DELIVERY DATE")})
                showDatePicker1(context = context, sharedViewModel = sharedViewModel, del_date = deldate)
                OutlinedTextField(value = remark, onValueChange = {remark = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Remarks")})
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Emergency", style = MaterialTheme.typography.subtitle1)
                    Switch(checked = case, onCheckedChange = {case = !case})
                }
            }
        }
    )
}
