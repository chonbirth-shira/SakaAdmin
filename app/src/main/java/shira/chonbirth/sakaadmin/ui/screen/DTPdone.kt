package shira.chonbirth.sakaadmin.ui.screen

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import shira.chonbirth.sakaadmin.ui.theme.*
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.io.ByteArrayOutputStream
import java.util.*


@Composable
fun DTPdone(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val id = sharedViewModel.selected_jobId.value

    Scaffold (
        topBar = {
            Surface(elevation = 1.dp, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.Texture) {
            Column() {
                Text(text = "SAKA Offset Printing", modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 2.dp), style = MaterialTheme.typography.body2, color = MaterialTheme.colors.primary)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(text = "DTP...", modifier = Modifier.padding(start = 10.dp, bottom = 14.dp, end = 20.dp), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.PrimaryText)
                    Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                        TextButton(onClick = {
                            val progressDialog = ProgressDialog(context)
                                progressDialog.setTitle("Uploading...")
                                progressDialog.show()

                            var storageReference: StorageReference
                            // get the Firebase  storage reference
                            var storage: FirebaseStorage = FirebaseStorage.getInstance()
                            storageReference = storage.reference

                            val path = "Flex/" + UUID.randomUUID().toString()
                            val ref = storageReference.child(path)

                            bitmap.let {  btm ->
                                if (btm != null) {
                                    val stream = ByteArrayOutputStream()
                                    btm.compress(Bitmap.CompressFormat.PNG, 50, stream)
                                    val image = stream.toByteArray()
                                    val uploadTask2 : UploadTask = ref.putBytes(image);
                                    uploadTask2.addOnSuccessListener {
                                        progressDialog.dismiss()
                                        ref.downloadUrl.addOnCompleteListener {
                                            db.collection("flexorders").document(id).update("job_status","Print")
                                            db.collection("flexorders").document(id).update("image",it.result.toString())
                                        }
                                        db.collection("flexorders").document(id).update("img_path",path)
                                        Toast
                                            .makeText(
                                                context,
                                                "Sent for Printing!!",
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                        navHostController.navigateUp()
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

//                            imageUri?.let {
//                                val source = ImageDecoder
//                                    .createSource(context.contentResolver,it)
//                                bitmap = getResizedBitmap(ImageDecoder.decodeBitmap(source), 300)
//
//                                bitmap.let {  btm ->
//                                    if (btm != null) {
//                                        val stream = ByteArrayOutputStream()
//                                        btm.compress(Bitmap.CompressFormat.PNG, 50, stream)
//                                        val image = stream.toByteArray()
//                                        var uploadTask2 : UploadTask = ref.putBytes(image);
//                                        uploadTask2.addOnSuccessListener {
//                                            progressDialog.dismiss()
//                                        ref.downloadUrl.addOnCompleteListener {
//                                            db.collection("flexorders").document(id).update("job_status","Print")
//                                            db.collection("flexorders").document(id).update("image",it.result.toString())
//                                        }
//                                        db.collection("flexorders").document(id).update("img_path",path)
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                "Sent for Printing!!",
//                                                Toast.LENGTH_LONG
//                                            )
//                                            .show()
//                                    }
//                                    .addOnFailureListener { e -> // Error, Image not uploaded
//                                        progressDialog.dismiss()
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                "Failed " + e.message,
//                                                Toast.LENGTH_SHORT
//                                            )
//                                            .show()
//                                    }
//                                            .addOnProgressListener { taskSnapshot ->
//                                        // Progress Listener for loading
//                                        // percentage on the dialog box
//                                        val progress = ((100.0
//                                                * taskSnapshot.bytesTransferred
//                                                / taskSnapshot.totalByteCount))
//                                        progressDialog.setMessage(
//                                            ("Uploaded "
//                                                    + progress.toInt() + "%")
//                                        )
//                                    }
//                                    }
//                                }
//
//                            }
//                            if (imageUri != null) {
//
//                                // Code for showing progressDialog while uploading
//                                val progressDialog = ProgressDialog(context)
//                                progressDialog.setTitle("Uploading...")
//                                progressDialog.show()
//
//                                // Defining the child of storageReference
//                                val pathString = "Flex/" + UUID.randomUUID().toString()
//                                val ref = storageReference
//                                    .child(
//                                        pathString
//                                    )
//
//                                // adding listeners on upload
//                                // or failure of image
//                                ref.putFile(imageUri!!)
//                                    .addOnSuccessListener { // Image uploaded successfully
//                                        // Dismiss dialog
//                                        ref.downloadUrl.addOnCompleteListener {
//                                            db.collection("flexorders").document(id).update("job_status","Print")
//                                            db.collection("flexorders").document(id).update("image",it.result.toString())
//                                        }
//                                        db.collection("flexorders").document(id).update("img_path",pathString)
//                                        progressDialog.dismiss()
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                "Sent for Printing!!",
//                                                Toast.LENGTH_LONG
//                                            )
//                                            .show()
//                                    }
//                                    .addOnFailureListener { e -> // Error, Image not uploaded
//                                        progressDialog.dismiss()
//                                        Toast
//                                            .makeText(
//                                                context,
//                                                "Failed " + e.message,
//                                                Toast.LENGTH_SHORT
//                                            )
//                                            .show()
//                                    }
//                                    .addOnProgressListener { taskSnapshot ->
//
//                                        // Progress Listener for loading
//                                        // percentage on the dialog box
//                                        val progress = ((100.0
//                                                * taskSnapshot.bytesTransferred
//                                                / taskSnapshot.totalByteCount))
//                                        progressDialog.setMessage(
//                                            ("Uploaded "
//                                                    + progress.toInt() + "%")
//                                        )
//                                    }
//                            }
                        }
                        ) {
                            Text(text = "Send for Printing")
                        }
                    }
                }
            }
        }},
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.Background)) {

//                val CamImg = remember { mutableStateOf<Bitmap>(null) }
                val launcherCam =
                    rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bmp: Bitmap? ->
                        bitmap = bmp
                    }


                val launcher = rememberLauncherForActivityResult(contract =
                ActivityResultContracts.GetContent()) { uri: Uri? ->
                    imageUri = uri
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {

                    bitmap.let {  btm ->
                        if (btm != null) {
                            imageUri = null
                            Image(bitmap = btm.asImageBitmap(),
                                contentDescription =null,
                                modifier = Modifier.size(400.dp))
                        }
                    }

                    imageUri?.let {
                        val source = ImageDecoder
                            .createSource(context.contentResolver,it)
                        bitmap = getResizedBitmap(ImageDecoder.decodeBitmap(source), 500)

                        bitmap.let {  btm ->
                            if (btm != null) {
                                Image(bitmap = btm.asImageBitmap(),
                                    contentDescription =null,
                                    modifier = Modifier.size(400.dp))
                            }
                        }

                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = { launcherCam.launch() },
                        contentPadding = PaddingValues()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.BottomCenter)
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Click photo")
                        }
                    }

                    Button(
                        onClick = { launcher.launch("image/*") },
                        contentPadding = PaddingValues()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.BottomCenter)
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Select Photo")
                        }
                    }
                }

            }
        }
    )
}
fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}