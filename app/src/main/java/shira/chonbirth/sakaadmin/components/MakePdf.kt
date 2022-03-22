package shira.chonbirth.sakaadmin.components
//
//import android.Manifest.permission.READ_EXTERNAL_STORAGE
//import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//import android.R
//import android.content.Context
//import android.content.pm.PackageManager
//import android.content.res.Resources
//import android.graphics.*
//import android.graphics.pdf.PdfDocument
//import android.graphics.pdf.PdfDocument.PageInfo
//import android.os.Environment
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import java.io.File
//import java.io.FileOutputStream
//import java.io.IOException
//
//
//fun MakePdf(context: Context){
//    // below code is used for
//    // checking our permissions.
//    if (checkPermission()) {
////        Toast.makeText(LocalContext.current, "Permission Granted", Toast.LENGTH_SHORT).show();
//    } else {
//        requestPermission();
//    }
//}
//
//fun generatePdf(){
//    val pdfDocument = PdfDocument()
//
//    val pageHeight = 1120
//    val pagewidth = 792
//
//    val bmp: Bitmap
//    val scaledbmp: Bitmap
//
//    val PERMISSION_REQUEST_CODE = 200;
//
//    bmp = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_delete);
//    scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);
//
//    // two variables for paint "paint" is used
//    // for drawing shapes and we will use "title"
//    // for adding text in our PDF file.
//
//    // two variables for paint "paint" is used
//    // for drawing shapes and we will use "title"
//    // for adding text in our PDF file.
//    val paint = Paint()
//    val title = Paint()
//
//    // we are adding page info to our PDF file
//    // in which we will be passing our pageWidth,
//    // pageHeight and number of pages and after that
//    // we are calling it to create our PDF.
//
//    // we are adding page info to our PDF file
//    // in which we will be passing our pageWidth,
//    // pageHeight and number of pages and after that
//    // we are calling it to create our PDF.
//    val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
//
//    // below line is used for setting
//    // start page for our PDF file.
//
//    // below line is used for setting
//    // start page for our PDF file.
//    val myPage = pdfDocument.startPage(mypageInfo)
//
//    // creating a variable for canvas
//    // from our page of PDF.
//
//    // creating a variable for canvas
//    // from our page of PDF.
//    val canvas: Canvas = myPage.canvas
//
//    // below line is used to draw our image on our PDF file.
//    // the first parameter of our drawbitmap method is
//    // our bitmap
//    // second parameter is position from left
//    // third parameter is position from top and last
//    // one is our variable for paint.
//
//    // below line is used to draw our image on our PDF file.
//    // the first parameter of our drawbitmap method is
//    // our bitmap
//    // second parameter is position from left
//    // third parameter is position from top and last
//    // one is our variable for paint.
//    canvas.drawBitmap(scaledbmp, 56F, 40F, paint)
//
//    // below line is used for adding typeface for
//    // our text which we will be adding in our PDF file.
//
//    // below line is used for adding typeface for
//    // our text which we will be adding in our PDF file.
//    title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
//
//    // below line is used for setting text size
//    // which we will be displaying in our PDF file.
//
//    // below line is used for setting text size
//    // which we will be displaying in our PDF file.
//    title.setTextSize(15F)
//
//    // below line is sued for setting color
//    // of our text inside our PDF file.
//
//    // below line is sued for setting color
//    // of our text inside our PDF file.
//    title.setColor(ContextCompat.getColor(this, R.color.background_dark))
//
//    // below line is used to draw text in our PDF file.
//    // the first parameter is our text, second parameter
//    // is position from start, third parameter is position from top
//    // and then we are passing our variable of paint which is title.
//
//    // below line is used to draw text in our PDF file.
//    // the first parameter is our text, second parameter
//    // is position from start, third parameter is position from top
//    // and then we are passing our variable of paint which is title.
//    canvas.drawText("A portal for IT professionals.", 209F, 100F, title)
//    canvas.drawText("Geeks for Geeks", 209F, 80F, title)
//
//    // similarly we are creating another text and in this
//    // we are aligning this text to center of our PDF file.
//
//    // similarly we are creating another text and in this
//    // we are aligning this text to center of our PDF file.
//    title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
//    title.setColor(ContextCompat.getColor(this, R.color.background_dark))
//    title.setTextSize(15F)
//
//    // below line is used for setting
//    // our text to center of PDF.
//
//    // below line is used for setting
//    // our text to center of PDF.
//    title.setTextAlign(Paint.Align.CENTER)
//    canvas.drawText("This is sample document which we have created.", 396F, 560F, title)
//
//    // after adding all attributes to our
//    // PDF file we will be finishing our page.
//
//    // after adding all attributes to our
//    // PDF file we will be finishing our page.
//    pdfDocument.finishPage(myPage)
//
//    // below line is used to set the name of
//    // our PDF file and its path.
//
//    // below line is used to set the name of
//    // our PDF file and its path.
//    val file = File(Environment.getExternalStorageDirectory(), "GFG.pdf")
//
//    try {
//        // after creating a file name we will
//        // write our PDF file to that location.
//        pdfDocument.writeTo(FileOutputStream(file))
//
//        // below line is to print toast message
//        // on completion of PDF generation.
////        Toast.makeText(this@MainActivity, "PDF file generated successfully.", Toast.LENGTH_SHORT)
////            .show()
//    } catch (e: IOException) {
//        // below line is used
//        // to handle error
//        e.printStackTrace()
//    }
//    // after storing our pdf to that
//    // location we are closing our PDF file.
//    // after storing our pdf to that
//    // location we are closing our PDF file.
//    pdfDocument.close()
//}
//
//private fun checkPermission(): Boolean {
//    // checking of permissions.
//    val permission1 = ContextCompat.checkSelfPermission(
////        ApplicationProvider.getApplicationContext<Context>(),
//        WRITE_EXTERNAL_STORAGE
//    )
//    val permission2 = ContextCompat.checkSelfPermission(
////        ApplicationProvider.getApplicationContext<Context>(),
//        READ_EXTERNAL_STORAGE
//    )
//    return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
//}
//
//private fun requestPermission() {
//    // requesting permissions if not provided.
//    ActivityCompat.requestPermissions(
//        this,
//        arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE),
//        200
//    )
//}
//
//fun onRequestPermissionsResult(
//    requestCode: Int,
//    permissions: Array<String?>,
//    grantResults: IntArray
//) {
//    if (requestCode == 200) {
//        if (grantResults.isNotEmpty()) {
//
//            // after requesting permissions we are showing
//            // users a toast message of permission granted.
//            val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
//            val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
//            if (writeStorage && readStorage) {
////                Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
//            } else {
////                Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show()
////                finish()
//            }
//        }
//    }
//}