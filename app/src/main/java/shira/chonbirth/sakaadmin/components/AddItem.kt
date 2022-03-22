package shira.chonbirth.sakaadmin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import shira.chonbirth.sakaadmin.data.OrderItem
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel

@Composable
fun AddItem(
    openDialog: Boolean,
    onYesClicked: () -> Unit,
    sharedViewModel: SharedViewModel,
    id: Int
){
    if (openDialog){
        var expanded by remember { mutableStateOf(false) }
        val angle: Float by animateFloatAsState(
            targetValue = if(expanded) 180f else 0f
        )
        var width by remember {
            mutableStateOf("")
        }
        var height by remember {
            mutableStateOf("")
        }
        var jobtype by remember {
            mutableStateOf("FLEX")
        }
        var quantity by remember {
            mutableStateOf("1")
        }
        var amount by remember {
            mutableStateOf("")
        }

        AlertDialog(
            title = {
                Text(
                    text = "Add Item",
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column() {
                    Text(text = "TYPE", modifier = Modifier.padding(top = 10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(1F)
                            .padding(top = 8.dp)
                            .height(56.dp)
                            .border(
                                1.dp,
                                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                                shape = MaterialTheme.shapes.small
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(value = jobtype, onValueChange = {jobtype = it},
                            modifier = Modifier.weight(1f)
                        )
//                        Text(text = jobtype, style = MaterialTheme.typography.subtitle1, modifier = Modifier
//                            .padding(start = 20.dp)
//                            .weight(
//                                1f
//                            ))
                        IconButton(modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .rotate(angle), onClick = {expanded = true}) {
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()) {
                            DropdownMenuItem(onClick = {
                                jobtype = "FLEX"
                                expanded = false
                            }) {
                                Text(text = "Flex Banner")
                            }
                            DropdownMenuItem(onClick = {
                                jobtype = "STAR"
                                expanded = false
                            }) {
                                Text(text = "Star Flex Banner")
                            }
                            DropdownMenuItem(onClick = {
                                jobtype = "VINYL"
                                expanded = false
                            }) {
                                Text(text = "Vinyl Sticker")
                            }
                        }
                    }
                    Text(text = "Description", modifier = Modifier.padding(top = 10.dp))
                    OutlinedTextField(value = width, onValueChange = {width = it}, modifier = Modifier.fillMaxWidth(), label = { Text(text = "Description")}, keyboardOptions = KeyboardOptions(
                        KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Text)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                        OutlinedTextField(value = amount, onValueChange = {amount = it}, modifier = Modifier.fillMaxWidth(1F), label = { Text(text = "Amount")}, keyboardOptions = KeyboardOptions(
                            KeyboardCapitalization.None,autoCorrect = false, keyboardType = KeyboardType.Number)
                        )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sharedViewModel.order.add(OrderItem(job_category = jobtype, description = width,amount = amount.toInt()))
                        onYesClicked()
                    }
                ) {
                    Text(text = "Add Item")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        onYesClicked()
                    }) {
                    Text(text = "Cancel")
                }
            },
            onDismissRequest = {
                onYesClicked()
            }
        )
    }
}