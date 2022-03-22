package shira.chonbirth.sakaadmin.components

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generatePDF(context: Context, directory: File) {
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()
    val paint = Paint()
    val title = Paint()
    val myPageInfo = PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage = pdfDocument.startPage(myPageInfo)
    val canvas: Canvas = myPage.canvas
    val bitmap: Bitmap? = drawableToBitmap(context.resources.getDrawable(R.drawable.icon))
    val scaleBitmap: Bitmap? = Bitmap.createScaledBitmap(bitmap!!, 80, 80, false)
    canvas.drawBitmap(scaleBitmap!!, -20f, -10f, paint)
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    title.textSize = 24f
    title.color = ContextCompat.getColor(context, R.color.black)
    canvas.drawText("SAKA OFFSET PRINTING", 170f, 95f, title)
    canvas.drawText("Kusimkolgre, Williamnagar", 170f, 130f, title)
    canvas.drawLine(0f,200f, 792f, 200f, paint)
    canvas.drawText("Name: Chonbirth D. Shira", 60f, 250f, title)
    canvas.drawText("Address: DC Old Colony", 60f, 290f, title)
    title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    title.textSize = 18f
    title.color = ContextCompat.getColor(context, R.color.black)
    canvas.drawText("Order Date: dd/MM/yyyy", 500f, 250f, title)
    canvas.drawText("Delivery Date: dd/MM/yyyy", 500f, 290f, title)
    canvas.drawLine(60f,320f, 732f, 320f, paint)
    title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    title.color = ContextCompat.getColor(context, R.color.black)
    title.textSize = 15f
    title.textAlign = Paint.Align.CENTER
    canvas.drawText("This is computer generated Order Slip.", 396f, 560f, title)
    pdfDocument.finishPage(myPage)
    val file = File(directory, "12_Will_Order_Slip.pdf")

    try {
        pdfDocument.writeTo(FileOutputStream(file))
        Toast.makeText(context, "PDF file generated successfully", Toast.LENGTH_SHORT).show()
    } catch (ex: IOException) {
        ex.printStackTrace()
    }
    pdfDocument.close()
}

fun drawableToBitmap(drawable: Drawable): Bitmap? {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }
    val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}