package shira.chonbirth.sakaadmin

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import shira.chonbirth.sakaadmin.components.generatePDF
import shira.chonbirth.sakaadmin.ui.screen.getResizedBitmap
import shira.chonbirth.sakaadmin.ui.theme.Header
import shira.chonbirth.sakaadmin.ui.theme.Items
import shira.chonbirth.sakaadmin.ui.theme.PrimaryText
import shira.chonbirth.sakaadmin.ui.theme.Shapes
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalPermissionsApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class,
    kotlinx.coroutines.DelicateCoroutinesApi::class
)
@Composable
fun OptionDialog(
    sharedViewModel: SharedViewModel,
    openDialog: Boolean,
    onYesClicked: () -> Unit,
    db: FirebaseFirestore,
    id: Int,
    context: Context
){
//    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    if (openDialog){
        var loading by remember { mutableStateOf(true) }
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
//                jobcategory = it.get("job_category").toString()
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
                                    .fillMaxWidth(0.7f)) {
                                    Row() {
                                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 6.dp))
                                        Text(text = addr, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                            color = Color.White)
                                    }
                                    Row(Modifier.clickable {
                                        val dialIntent = Intent(Intent.ACTION_DIAL)
                                        dialIntent.data = Uri.parse("tel:" + contact)
                                        startActivity(context, dialIntent, Bundle.EMPTY)


//                                        val uri = Uri.parse("smsto:" + "9774172574")
//                                        val dialIntent = Intent(Intent.ACTION_SENDTO, uri)
////                                        dialIntent.putExtra("sms_body", "smsText")
//                                        dialIntent.setPackage("com.whatsapp")
                                        startActivity(context, dialIntent, Bundle.EMPTY)
                                    }) {
                                        Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 6.dp))
                                        Text(text = contact, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis,
                                            color = Color.White)
                                    }
//                                    TextButton(onClick = {
//                                        val dialIntent = Intent(Intent.ACTION_DIAL)
//                                        dialIntent.data = Uri.parse("tel:" + "8344814819")
//                                        startActivity(context, dialIntent, Bundle.EMPTY)
//                                    }) {
//                                        Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier
//                                            .size(24.dp)
//                                            .padding(end = 6.dp))
//                                        Text(text = contact, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis,
//                                            color = Color.White)
//                                    }
                                }
                                IconButton(onClick = {
                                    db.collection("flexorders").document(id.toString()).delete()
                                    onYesClicked()
                                }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Color.White)
//                                    Spacer(modifier = Modifier.width(6.dp))
//                                    Text(text = "Delete")
                                }

                                val dir = "/storage/emulated/0/Download/saka/"
                                val dir2 = File(dir)
                                IconButton(
                                    onClick = {
                                        if (dir2.exists()){
                                            generatePDF(context, dir2)
                                        }else{
                                            val f = File(File("/storage/emulated/0/Download/"), "saka")
                                            f.mkdir()
                                            generatePDF(context, dir2)
                                        }
                                        generatePDF(context, dir2)

//                                        Toast.makeText(context, file.toString(), Toast.LENGTH_LONG).show()
//                                        onYesClicked()
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
                            Column(modifier = Modifier
                                .fillMaxWidth()) {
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
                        }

//                    Text(text = "${remark}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp), color = MaterialTheme.colors.primary)
                        val bal = amount.minus(advance)
                        Row(modifier = Modifier
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                        val print = remember {
                            mutableStateOf(false)
                        }
                        val launcher = rememberLauncherForActivityResult(contract =
                            ActivityResultContracts.GetContent()) { uri ->
                            val source = ImageDecoder
                                .createSource(context.contentResolver,uri)
                            bitmap = ImageDecoder.decodeBitmap(source)
                            }
                        val launcherCam =
                            rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp: Bitmap? ->
                                bitmap = bmp
                            }
                        bitmap.let {  btm ->
                            if (btm != null) {
//                                imageUri = null
                                print.value = false
                                Image(bitmap = btm.asImageBitmap(),
                                    contentDescription =null,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .fillMaxWidth(), alignment = Alignment.Center, contentScale = ContentScale.Crop)
                            }
                        }
                        AnimatedVisibility(visible = print.value) {
                            Row(modifier = Modifier
                                .padding(6.dp)
                                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                                Button(onClick = {
                                    launcherCam.launch()
                                }) {
                                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Camera")
                                }
                                Button(onClick = {
                                    launcher.launch("image/*")
                                }) {
                                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Upload")
                                }
                            }
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp), horizontalArrangement = Arrangement.End) {
//                            Spacer(modifier = Modifier.width(10.dp))
                            if (bitmap != null){
                                OutlinedButton(onClick = {
                                    bitmap = null
                                    print.value = true
                                }) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Button(onClick = {
                                    val progressDialog = ProgressDialog(context)
                                    progressDialog.setTitle("Uploading...")
                                    progressDialog.show()

                                    var storageReference: StorageReference
                                    // get the Firebase  storage reference
                                    var storage: FirebaseStorage = FirebaseStorage.getInstance()
                                    storageReference = storage.reference

                                    val current = LocalDateTime.now()
                                    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
                                    val today = current.format(formatter)

                                    val path = "Flex/" + "${today}/" + UUID.randomUUID().toString()
                                    val ref = storageReference.child(path)

                                    bitmap.let {  btm ->
                                        if (btm != null) {
                                            val stream = ByteArrayOutputStream()
                                            btm.compress(Bitmap.CompressFormat.JPEG, 10, stream)
                                            val image = stream.toByteArray()
                                            val uploadTask2 : UploadTask = ref.putBytes(image);
                                            uploadTask2.addOnSuccessListener {
                                                progressDialog.dismiss()
                                                ref.downloadUrl.addOnCompleteListener {
                                                    db.collection("flexorders").document(id.toString()).update("job_status","Print")
                                                    db.collection("flexorders").document(id.toString()).update("image",it.result.toString())
                                                }
                                                db.collection("flexorders").document(id.toString()).update("img_path",path)
                                                Toast
                                                    .makeText(
                                                        context,
                                                        "Sent for Printing!!",
                                                        Toast.LENGTH_LONG
                                                    )
                                                    .show()
                                            }
                                                .addOnFailureListener { e -> // Error, Image not uploaded
                                                    progressDialog.dismiss()
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "Failed " + e.message,
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
                                                .addOnProgressListener { taskSnapshot ->
                                                    // Progress Listener for loading
                                                    // percentage on the dialog box
                                                    val progress = ((100.0
                                                            * taskSnapshot.bytesTransferred
                                                            / taskSnapshot.totalByteCount))
                                                    progressDialog.setMessage(
                                                        ("Uploaded "
                                                                + progress.toInt() + "%")
                                                    )
                                                }
                                        }
                                    }
                                onYesClicked()
                                }) {
                                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Send")
                                }
                            }else{
                                if (print.value){

                                }else{
                                    Button(onClick = {
//                                sharedViewModel.selected_jobId.value = jobId
////                                    db.collection("flexorders").document(id.toString()).update("job_status","Print")
//                                navHostController.navigate("dtp_done")
                                        print.value = true
//                                onYesClicked()
                                    }) {
                                        Icon(imageVector = Icons.Default.Done, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(text = "Print")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        AlertDialog(modifier = Modifier.wrapContentHeight(),
//            title = {
//                if (loading){
//                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
//                }else{
//                    Row(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 10.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
//                        Text(
//                            text = "${jobId} ${name}",
//                            fontSize = MaterialTheme.typography.h6.fontSize,
//                            fontWeight = FontWeight.Bold
//                        )
//                        IconButton(
//                            onClick = {
////                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
////                                if (ContextCompat.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED){
////                                    val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
////                                    requestPermission(permission, STORAGE_CODE)
////                                }else {
////                                    savePdf()
////                                }
////                            }else {
////                                savePdf()
////                            }
////                            savePdf()
//                                onYesClicked()
//                            }) {
//                            Icon(imageVector = Icons.Default.Share, contentDescription = null)
//                        }
//                    }
//                }
//            },
//            text = {
//                if (loading){
//                    Box(modifier = Modifier.height(400.dp))
//                }else{
//                    Column(modifier = Modifier.padding(top = 4.dp)) {
//                        Spacer(modifier = Modifier.size(2.dp))
//
////                    Text(text = name, style = MaterialTheme.typography.h6, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                        Text(text = addr, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                        Text(text = contact, style = MaterialTheme.typography.subtitle1, maxLines = 1, overflow = TextOverflow.Ellipsis)
//
//                        val sizes = remember {
//                            mutableListOf<String>()
//                        }
//                        sizes.clear()
//                        if(perticular.length > 2) {
//                            perticular.split(",").forEach {
//                                val item = it.split("_")
//                                if (item.get(0) == "FLEX"){
//                                    sizes.add( "FLEX " + item.get(1) + " - " + item.get(3) + " COPY")
//                                }
//                                if (item.get(0) == "STAR"){
//                                    sizes.add( "STAR " + item.get(1) + " - " + item.get(3) + " COPY")
//                                }
//                                if (item.get(0) == "VINYL"){
//                                    sizes.add( "VINYL " + item.get(1) + " - " + item.get(3) + " COPY")
//                                }
//                            }
//                            Row(modifier = Modifier
//                                .padding(top = 6.dp)
//                                .fillMaxWidth()) {
//                                sizes.forEach{
//                                    Box(modifier = Modifier
//                                        .padding(end = 5.dp)
//                                        .clip(shape = Shapes.small)
//                                        .background(color = Color.Gray)) {
//                                        Text(
//                                            text = it,
//                                            modifier = Modifier
//                                                .padding(start = 6.dp, end = 6.dp)
//                                                .fillMaxWidth(),
//                                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
//                                            color = MaterialTheme.colors.PrimaryText,
//                                        )
//                                    }
//                                }
//                            }
//                        }
//
////                    Text(text = "${remark}", maxLines = 1, overflow = TextOverflow.Ellipsis)
//                        Divider(Modifier.padding(top = 8.dp, bottom = 8.dp))
//                        val bal = amount.minus(advance)
//                        Row(modifier = Modifier
//                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                            Column(horizontalAlignment = Alignment.Start) {
//                                Text(text = "Ord : ${order}")
//                                Text(text = "Del : ${delivery}")
//                            }
//                            Divider(
//                                color = Color.White.copy(alpha = 0.2f),
//                                modifier = Modifier
//                                    .height(70.dp)
//                                    .width(1.dp)
//                            )
//                            Column {
////                                modifier = Modifier.border(width = 1.dp, color = MaterialTheme.colors.PrimaryText, shape = Shapes.small)
//                                Row() {
//                                    Column(modifier = Modifier.padding(6.dp)) {
//                                        Text(text = "Total", style = MaterialTheme.typography.subtitle1)
//                                        Text(text = "Advance", style = MaterialTheme.typography.subtitle1)
//                                        Text(text = "Balance", style = MaterialTheme.typography.subtitle1)
//                                    }
//                                    Column(modifier = Modifier.padding(6.dp)) {
//                                        Text(text = ": ${amount}", style = MaterialTheme.typography.subtitle1)
//                                        Text(text = ": ${advance}", style = MaterialTheme.typography.subtitle1)
//                                        Text(text = ": ${bal}", style = MaterialTheme.typography.subtitle1)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            },
//            confirmButton = {
//                if (!loading){
//                    Button(
//                        onClick = {
//                            sharedViewModel.selected_jobId.value = jobId
////                                    db.collection("flexorders").document(id.toString()).update("job_status","Print")
//                            navHostController.navigate("dtp_done")
//                            onYesClicked()
//                        }
//                    ) {
//                        Text(text = "DTP Done")
//                    }
//                }
//
//            },
//            dismissButton = {
//                if (!loading){
//                    OutlinedButton(
//                        onClick = {
//                            db.collection("flexorders").document(id.toString()).delete()
//                            onYesClicked()
//                        }) {
//                        Text(text = "Delete Job", color = Color.Red)
//                    }
//                }
//            },
//            onDismissRequest = {
//                onYesClicked()
//            }
//        )
    }
}
fun savePdf(){
    val mDoc = Document()
    val mFileName = "hello"
    val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName + ".pdf"
    try {
        PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
        mDoc.open()

        val data = "et_pdf_data".toString().trim()
        mDoc.addAuthor("Shira")
        mDoc.add(Paragraph(data))
        mDoc.close()
    }catch (e: Exception){

    }
}
//
//fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//    when(requestCode){
////        STORAGE_CODE -> {}
//    }
//}
//
//@Composable
//private fun Rationale(
//    onDoNotShowRationale: () -> Unit,
//    onRequestPermission: () -> Unit
//) {
//    Column {
//        Text("The camera is important for this app. Please grant the permission.")
//        Spacer(modifier = Modifier.height(8.dp))
//        Row {
//            Button(onClick = onRequestPermission) {
//                Text("Request permission")
//            }
//            Spacer(Modifier.width(8.dp))
//            Button(onClick = onDoNotShowRationale) {
//                Text("Don't show rationale again")
//            }
//        }
//    }
//}
//
//@Composable
//private fun PermissionDenied(
//    navigateToSettingsScreen: () -> Unit
//) {
//    Column {
//        Text(
//            "Reading Storage permission denied. See this FAQ with information about why we " +
//                    "need this permission. Please, grant us access on the Settings screen."
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = navigateToSettingsScreen) {
//            Text("Open Settings")
//        }
//    }
//}