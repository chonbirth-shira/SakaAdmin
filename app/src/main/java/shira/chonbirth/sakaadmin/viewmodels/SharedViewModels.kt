package shira.chonbirth.sakaadmin.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import shira.chonbirth.sakaadmin.data.OrderItem

class SharedViewModel constructor(): ViewModel() {
    val date = mutableStateOf("DD/MM/YYYY")
    val selected_jobId = mutableStateOf("")
    val splashScreen = mutableStateOf(true)
    val input = mutableStateOf("")

    val order = mutableStateListOf<OrderItem>()
}