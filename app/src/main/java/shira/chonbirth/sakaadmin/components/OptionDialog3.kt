package shira.chonbirth.sakaadmin

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import shira.chonbirth.sakaadmin.components.days
import shira.chonbirth.sakaadmin.ui.theme.Header
import shira.chonbirth.sakaadmin.ui.theme.PrimaryText
import shira.chonbirth.sakaadmin.ui.theme.SecondaryText
import shira.chonbirth.sakaadmin.ui.theme.Shapes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OptionDialog3(
    openDialog: Boolean,
    onYesClicked: () -> Unit,
    db: FirebaseFirestore,
    id: Int
){
    if (openDialog){
        var loading by remember { mutableStateOf(true) }

        var expanded by remember { mutableStateOf(false) }
        val angle: Float by animateFloatAsState(
            targetValue = if(expanded) 180f else 0f
        )
        var path by remember { mutableStateOf("") }
        var image by remember { mutableStateOf("") }

//        var jobcategory by remember { mutableStateOf("") }
        var jobId by remember { mutableStateOf("") }
        var name by remember { mutableStateOf("") }
        var addr by remember { mutableStateOf("") }
        var contact by remember { mutableStateOf("") }
        var perticular by remember { mutableStateOf("") }
        var amount by remember { mutableStateOf(0) }
        var advance by remember { mutableStateOf(0) }
        var order by remember { mutableStateOf("") }
        var delivery by remember { mutableStateOf("") }
        LaunchedEffect(key1 = true){
            db.collection("flexorders").document(id.toString()).get().addOnSuccessListener{
                path = it.get("img_path").toString()
                image = it.get("image").toString()
                jobId = it.get("job_id").toString()
                name = it.get("customer_name").toString()
                addr = it.get("customer_address").toString()
                contact = it.get("customer_contact").toString()
                perticular = it.get("perticular").toString()
                amount = it.get("amount").toString().toInt()
                advance = it.get("advance").toString().toInt()
                order = it.get("order_date").toString()
                delivery = it.get("delivery_date").toString()
            }.addOnSuccessListener {
                loading = false
            }
        }

        Dialog(onDismissRequest = { onYesClicked() }, properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true, usePlatformDefaultWidth = false)) {
            Column(modifier = Modifier
                .padding(10.dp)
                .clip(shape = Shapes.small)
                .background(color = MaterialTheme.colors.background)
                .border(width = 1.dp, color = Color.Gray, shape = Shapes.small)) {
                if (loading){
                    Box(modifier = Modifier
                        .height(360.dp)
                        .fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.size(64.dp))
                    }
                }else{
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = MaterialTheme.colors.Header)
                        .padding(10.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Column() {
                            Text(
                                text = "${jobId} ${name}",
                                fontSize = MaterialTheme.typography.h6.fontSize,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                            Row() {
                                Column(modifier = Modifier
                                    .fillMaxWidth(0.85f)) {
                                    Text(text = addr, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                        color = Color.White)
                                    Text(text = contact, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                        color = Color.White)
                                }
                                IconButton(
                                    onClick = {
                                        onYesClicked()
                                    }) {
                                    Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.White)
                                }
                            }
                        }
                    }
                    Column(modifier = Modifier.padding(10.dp)) {
                        Spacer(modifier = Modifier.size(2.dp))
                        val sizes = remember {
                            mutableListOf<String>()
                        }
                        sizes.clear()
                        if(perticular.length > 2) {
                            perticular.split(",").forEach {
                                val item = it.split("_")
                                if (item.get(0) == "FLEX"){
                                    sizes.add( "FLEX " + item.get(1) + " - " + item.get(3) + " COPY")
                                }
                                if (item.get(0) == "STAR"){
                                    sizes.add( "STAR " + item.get(1) + " - " + item.get(3) + " COPY")
                                }
                                if (item.get(0) == "VINYL"){
                                    sizes.add( "VINYL " + item.get(1) + " - " + item.get(3) + " COPY")
                                }
                            }
                            Row(Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier
                                    .fillMaxWidth(0.6f)) {
                                    sizes.forEach{
                                        Row(modifier = Modifier
                                            .padding(end = 5.dp)) {
                                            Text(
                                                text = "${(sizes.indexOf(it)+1)}" + ". " + it,
                                                modifier = Modifier
                                                    .padding(start = 6.dp, end = 6.dp)
                                                    .fillMaxWidth(),
                                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colors.PrimaryText,
                                            )
                                        }
                                    }
                                }
                                Column() {
                                    Image(
                                        painter = rememberImagePainter(data = image, builder = {
//                                    transformations(
//                                        RoundedCornersTransformation(12F)
//                                    )
                                            this.size(200).placeholder(R.drawable.placeholder).error(R.drawable.error).crossfade(300)
                                        }),
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp).clip(shape = Shapes.small),
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

//                    Text(text = "${remark}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp), color = MaterialTheme.colors.primary)
                        val bal = amount.minus(advance)
                        Row(modifier = Modifier
                            .fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(text = "Ord : ${order}")
                                Text(text = "Del : ${delivery}")
                            }
                            Divider(
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier
                                    .height(70.dp)
                                    .width(1.dp)
                            )
                            Column {
//                                modifier = Modifier.border(width = 1.dp, color = MaterialTheme.colors.PrimaryText, shape = Shapes.small)
                                Row() {
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Text(text = "Total", style = MaterialTheme.typography.subtitle1)
                                        Text(text = "Advance", style = MaterialTheme.typography.subtitle1)
                                        Text(text = "Balance", style = MaterialTheme.typography.subtitle1)
                                    }
                                    Column(modifier = Modifier.padding(6.dp)) {
                                        Text(text = ": ${amount}", style = MaterialTheme.typography.subtitle1)
                                        Text(text = ": ${advance}", style = MaterialTheme.typography.subtitle1)
                                        Text(text = ": ${bal}", style = MaterialTheme.typography.subtitle1)
                                    }
                                }
                            }
                        }
                        // BUTTONS
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            OutlinedButton(onClick = {
                                db.collection("flexorders").document(id.toString()).update("job_status","Print")
                                onYesClicked()
                            }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Re-print")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = {
                                    val current = LocalDateTime.now()
                                    val storage: FirebaseStorage = FirebaseStorage.getInstance()
                                    val storageRef = storage.reference
                                    val desertRef = storageRef.child(path)
                                    desertRef.delete()
                                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    val today = current.format(formatter)
                                    db.collection("flexorders").document(id.toString()).update("job_status","Issued")
                                    db.collection("flexorders").document(id.toString()).update("issue_date", today)
                                    db.collection("flexorders").document(id.toString()).update("balance", amount - advance)
//                                db.collection("flexorders").document(id.toString()).update("job_status","Issued")
                                onYesClicked()
                            }) {
                                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Issued")
                            }
                        }
                    }
                }
            }
        }
    }
}
//        AlertDialog(
//            title = {
//                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
//                    Text(
//                        text = "${jobId} ${jobcategory}",
//                        fontSize = MaterialTheme.typography.h6.fontSize,
//                        fontWeight = FontWeight.Bold
//                    )
//                    IconButton(
//                        onClick = {
//
//                        }) {
//                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
//                    }
//                }
//            },
//            text = {
//                Column() {
//                    Row() {
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
//                        Column(modifier = Modifier.padding(start = 6.dp)) {
//                            Text(text = "${name}", style = MaterialTheme.typography.h6, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                            Text(text = "${addr}", style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                            Text(text = "${contact}", style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis)
////                            Text(text = "${remark}", maxLines = 1, overflow = TextOverflow.Ellipsis)
//                        }
//                    }
//                    Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
//                    val bal = amount.minus(advance)
//                    Row(modifier = Modifier
//                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                        Column(horizontalAlignment = Alignment.Start) {
//                            Text(text = "Ord : ${order}")
//                            Text(text = "Del : ${delivery}")
//                        }
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
//                    }
//                }
//            },
//            confirmButton = {
//                            Button(
//                                onClick = {
//                                    val current = LocalDateTime.now()
//                                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//                                    val today = current.format(formatter)
//                                    db.collection("flexorders").document(id.toString()).update("job_status","Issued")
//                                    db.collection("flexorders").document(id.toString()).update("issue_date", today)
//                                    val day = days(delivery).toInt()
//                                    db.collection("flexorders").document(id.toString()).update("time",
//                                        when {
//                                            day == 0 -> {
//                                                "Delivered on Time"
//                                            }
//                                            day > 0 -> {
//                                                "Delivered before Time"
//                                            }
//                                            else -> {
//                                                "Delivered Late"
//                                            }
//                                        }
//                                    )
//                                    onYesClicked()
//                                }
//                            ) {
//                                Text(text = "Issued")
//                            }
//            },
//            dismissButton = {
//                OutlinedButton(
//                    onClick = {
//                        db.collection("flexorders").document(id.toString()).update("job_status","Print")
//                        onYesClicked()
//                    }) {
//                    Text(text = "Re-print")
//                }
//            },
//            onDismissRequest = {
//                onYesClicked()
//            }
//        )
//    }
//}