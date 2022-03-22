package shira.chonbirth.sakaadmin.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.util.*

@Composable
fun showDatePicker(context: Context, sharedViewModel: SharedViewModel){
    val year: Int
    val month: Int
    val day: Int

    val calender = Calendar.getInstance()
    year = calender.get(Calendar.YEAR)
    month = calender.get(Calendar.MONTH)
    day = calender.get(Calendar.DAY_OF_MONTH)
    calender.time = Date()

    val date = remember {
        mutableStateOf("")
    }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "${dayOfMonth}/${month+1}/${year}"
            sharedViewModel.date.value = "${dayOfMonth}/${month+1}/${year}"
        }, year, month, day
    )
    Row(modifier = Modifier.fillMaxWidth().height(64.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        TextButton(onClick = { datePickerDialog.show() }) {
            Text(text = if (date.value == ""){"Select Delivery Date"}else{"Delivery Date:"})
        }
        Text(text = "${date.value}", style = MaterialTheme.typography.h6)
    }
}
@Composable
fun showDatePicker1(context: Context, sharedViewModel: SharedViewModel, del_date: String){
    val year: Int
    val month: Int
    val day: Int

    val calender = Calendar.getInstance()
    year = calender.get(Calendar.YEAR)
    month = calender.get(Calendar.MONTH)
    day = calender.get(Calendar.DAY_OF_MONTH)
    calender.time = Date()

    val date = remember {
        mutableStateOf(del_date)
    }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "${dayOfMonth}/${month+1}/${year}"
//            sharedViewModel.date.value = "${dayOfMonth}/${month+1}/${year}"
        }, year, month, day
    )
    Row(modifier = Modifier.fillMaxWidth().height(64.dp).padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Delivery Date: ${date.value}")
        Spacer(modifier = Modifier.size(16.dp))
        TextButton(onClick = { datePickerDialog.show() }) {
            Text(text = "Select Date")
        }
    }
}