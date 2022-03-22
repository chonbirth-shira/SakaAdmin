package shira.chonbirth.sakaadmin.components

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("SimpleDateFormat")
fun days(delivery: String): String {
    val dates = SimpleDateFormat("dd/MM/yyyy")
//    val date1 = dates.parse(order)
    val date1 = dates.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    val date2 = dates.parse(delivery)
//    val difference: Long = Math.abs(date1.time - date2.time)
    val difference: Long = (date2.time - date1.time)
    val differenceDates = difference / (24 * 60 * 60 * 1000)
    return differenceDates.toString()
}
