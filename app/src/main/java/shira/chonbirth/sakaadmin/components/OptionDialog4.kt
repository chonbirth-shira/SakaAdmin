package shira.chonbirth.sakaadmin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import shira.chonbirth.sakaadmin.ui.theme.PrimaryText
import shira.chonbirth.sakaadmin.ui.theme.Shapes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OptionDialog4(
    openDialog: Boolean,
    onYesClicked: () -> Unit,
    db: FirebaseFirestore,
    id: Int
){
    if (openDialog){

        var expanded by remember { mutableStateOf(false) }
        val angle: Float by animateFloatAsState(
            targetValue = if(expanded) 180f else 0f
        )
        var path by remember { mutableStateOf("") }
        var image by remember { mutableStateOf("") }

        var jobcategory by remember { mutableStateOf("") }
        var jobId by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var addr by remember { mutableStateOf("") }
        var contact by remember { mutableStateOf("") }
        var remark by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf(0) }
        var advance by remember { mutableStateOf(0) }
        var order by remember { mutableStateOf("") }
        var delivery by remember { mutableStateOf("") }
        var issue by remember { mutableStateOf("") }
        LaunchedEffect(key1 = true){
            db.collection("flexorders").document(id.toString()).get().addOnSuccessListener{
                path = it.get("img_path").toString()
                image = it.get("image").toString()

                jobcategory = it.get("job_category").toString()
                jobId = it.get("job_id").toString()
                name = it.get("customer_name").toString()
                addr = it.get("customer_address").toString()
                contact = it.get("customer_contact").toString()
                remark = it.get("remarks").toString()
                amount = it.get("amount").toString().toInt()
                advance = it.get("advance").toString().toInt()
                order = it.get("order_date").toString()
                delivery = it.get("delivery_date").toString()
                issue = it.get("issue_date").toString()
            }
        }
        AlertDialog(
            title = {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "${jobId} ${jobcategory}",
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {

                        }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    }
                }
            },
            text = {
                Column() {
                    Row() {
//                        Image(
//                            painter = rememberImagePainter(data = image, builder = {
////                                transformations(
////                                    RoundedCornersTransformation(12F)
////                                )
//                                this.size(200).placeholder(R.drawable.placeholder).error(R.drawable.error).crossfade(300)
//                            }),
//                            contentDescription = null,
//                            modifier = Modifier.size(120.dp).clip(shape = Shapes.small),
//                            alignment = Alignment.Center,
//                            contentScale = ContentScale.Crop
//                        )
                        Column(modifier = Modifier.padding(start = 6.dp)) {
                            Text(text = "${name}", style = MaterialTheme.typography.h6, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = "${addr}", style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(text = "${contact}", style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                            Text(text = "${remark}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
                    val bal = amount.minus(advance)
                    Row(modifier = Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = "Order Date : ${order}")
                            Text(text = "Delivery Date : ${delivery}")
                            Text(text = "Issue Date : ${issue}")
                        }
//                        Column() {
//                            Row(modifier = Modifier.border(width = 1.dp, color = MaterialTheme.colors.PrimaryText)) {
//                                Column(modifier = Modifier.padding(6.dp)) {
//                                    Text(text = "Total", style = MaterialTheme.typography.subtitle1)
//                                    Text(text = "Advance", style = MaterialTheme.typography.subtitle1)
//                                    Text(text = "Balance", style = MaterialTheme.typography.subtitle1)
//                                }
//                                Column(modifier = Modifier.padding(6.dp)) {
//                                    Text(text = ": ${amount}", style = MaterialTheme.typography.subtitle1)
//                                    Text(text = ": ${advance}", style = MaterialTheme.typography.subtitle1)
//                                    Text(text = ": ${bal}", style = MaterialTheme.typography.subtitle1)
//                                }
//                            }
//                        }
                    }
                }
            },
            confirmButton = {
                            Button(
                                onClick = {
//                                    val storage: FirebaseStorage = FirebaseStorage.getInstance()
//                                    val storageRef = storage.reference
//                                    val desertRef = storageRef.child(path)
//                                    desertRef.delete()
                                    db.collection("flexorders").document(id.toString()).delete()
                                    onYesClicked()
                                }
                            ) {
                                Text(text = "Delete")
                            }
            },
            dismissButton = {
//                OutlinedButton(
//                    onClick = {
//                        db.collection("flexorders").document(id.toString()).update("job_status","Completed")
//                        db.collection("flexorders").document(id.toString()).update("issue_date","--")
//                        onYesClicked()
//                    }) {
//                    Text(text = "Not Issued")
//                }
            },
            onDismissRequest = {
                onYesClicked()
            }
        )
    }
}