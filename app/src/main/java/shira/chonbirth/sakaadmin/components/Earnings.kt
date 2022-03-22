package shira.chonbirth.sakaadmin.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import shira.chonbirth.sakaadmin.data.OrdersListData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TodaysEarning(db : FirebaseFirestore){
    val profit = remember {
        mutableStateOf(0)
    }
    val total = remember {
        mutableStateOf(0)
    }
    val loading = remember {
        mutableStateOf(true)
    }
    val value = remember {
        mutableStateOf("")
    }
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val today = current.format(formatter)
    db.collection("flexorders").whereEqualTo("order_date", today).get().addOnSuccessListener { result ->
        var sum = 0
        for (document in result) {
            var advance: Int = document.get("advance").toString().toInt()
//                                    var balance: Int = document.get("balance").toString().toInt()
            sum += advance
        }
        profit.value = sum
    }
    db.collection("flexorders").whereEqualTo("issue_date", today).get().addOnSuccessListener { result ->
        var sum = 0
        for (document in result) {
            var advance: Int = document.get("balance").toString().toInt()
//                                    var balance: Int = document.get("balance").toString().toInt()
            sum += advance
        }
        total.value = profit.value + sum
    }.addOnCompleteListener{
        loading.value = false
        value.value = "0"
    }

//    db.collection("flexorders").whereEqualTo("order_date", today).addSnapshotListener { value, e ->
//        if (e != null) {
//            return@addSnapshotListener
//        }
//        var sum = 0
//        if (value != null) {
//            for (document in value) {
//                var advance: Int = document.get("advance").toString().toInt()
//    //                                    var balance: Int = document.get("balance").toString().toInt()
//                sum += advance
//            }
//        }
//        profit.value = sum
//    }
//    db.collection("flexorders").whereEqualTo("issue_date", today).addSnapshotListener { value, e ->
//        if (e != null) {
//            return@addSnapshotListener
//        }
//        var sum = 0
//        if (value != null) {
//            for (document in value) {
//                var advance: Int = document.get("balance").toString().toInt()
//    //                                    var balance: Int = document.get("balance").toString().toInt()
//                sum += advance
//            }
//        }
//        total.value = profit.value + sum
//    }

    Text(text = "Today : Rs." + if(total.value!=0){total.value}else{value.value})
//                            IconButton(onClick = { navHostController1.navigate("new_task_pro") }, ) {
//                                Icon(imageVector = Icons.Outlined.Add, contentDescription = null, tint = MaterialTheme.colors.primary)
//                            }
    if (loading.value){
        CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(4.dp), strokeWidth = 2.dp)
    }
}