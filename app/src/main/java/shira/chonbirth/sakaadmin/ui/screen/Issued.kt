package shira.chonbirth.sakaadmin.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.GrayscaleTransformation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import shira.chonbirth.sakaadmin.OptionDialog4
import shira.chonbirth.sakaadmin.R
import shira.chonbirth.sakaadmin.data.OrdersIssuedData
import shira.chonbirth.sakaadmin.ui.theme.*
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Issued(navHostController: NavHostController, sharedViewModel: SharedViewModel, db: FirebaseFirestore){
    var id by remember {
        mutableStateOf(0)
    }
    var onoff by remember {
        mutableStateOf(false)
    }
    OptionDialog4(onoff, onYesClicked = {onoff = false}, db = db, id )
    Scaffold(
        topBar = {
            Surface(elevation = 1.dp, modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colors.Texture) {
                Column {
                    Text(text = "SAKA Offset Printing", modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 2.dp), style = MaterialTheme.typography.body2, color = MaterialTheme.colors.primary)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Issued", modifier = Modifier.padding(start = 10.dp, bottom = 14.dp), style = MaterialTheme.typography.h4, color = MaterialTheme.colors.PrimaryText)
                    }
                }
            }
        },
        content = {
            val datas = mutableListOf<OrdersIssuedData>()
            val allList = MutableStateFlow<List<OrdersIssuedData>>(emptyList())
            val dd : StateFlow<List<OrdersIssuedData>> = allList
            LaunchedEffect(key1 = true){
                db.collection("flexorders").whereEqualTo("job_status","Issued")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val job_id: Int = document.get("job_id").toString().toInt()
                            val customer: String = document.getString("customer_name").toString()
                            val customer_addr: String = document.getString("customer_address").toString()
                            val status: String = document.getString("job_status").toString()
                            val dorder: String = document.getString("delivery_date").toString()
                            val issue: String = document.getString("issue_date").toString()
                            val amount: Int = document.get("amount").toString().toInt()
                            datas.add(OrdersIssuedData(job_id = job_id, customer_name = customer, customer_address = customer_addr, job_status = status, delivery_date = dorder, issue_date = issue, amount = amount))
                        }
                        allList.value = datas
                    }
            }

            // Change Listener
            db.collection("flexorders").whereEqualTo("job_status","Issued").addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

//                val cities = ArrayList<OrdersIssuedData>()
                for (document in value!!) {
                    val job_id: Int = document.get("job_id").toString().toInt()
                    val customer: String = document.getString("customer_name").toString()
                    val customer_addr: String = document.getString("customer_address").toString()
                    val status: String = document.getString("job_status").toString()
                    val dorder: String = document.getString("delivery_date").toString()
                    val issue: String = document.getString("issue_date").toString()
                    val amount: Int = document.get("amount").toString().toInt()
                    datas.add(
                        OrdersIssuedData(
                            job_id = job_id,
                            customer_name = customer,
                            customer_address = customer_addr,
                            job_status = status,
                            delivery_date = dorder,
                            issue_date = issue,
                            amount = amount
                        )
                    )
                }
                allList.value = datas
            }

            val ss = dd.collectAsState()
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.Background)){
                items(ss.value){
                    Surface(modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp, top = 6.dp),
                        shape = Shapes.small,
                        elevation = 1.dp,
                        onClick = {
                            id = it.job_id
                            onoff = !onoff
                        }
                    )
                    {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                MaterialTheme.colors.Items, shape = Shapes.small
                            )) {
                            Column(modifier = Modifier
                                .padding(start = 10.dp, bottom = 10.dp, end = 10.dp, top = 6.dp)
                                .weight(0.7f)) {
                                Text(text = "${it.job_id} ${it.customer_name}", style = MaterialTheme.typography.h6, maxLines = 1, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colors.primary)
                                Text(text = it.customer_address, style = MaterialTheme.typography.caption, color = MaterialTheme.colors.PrimaryText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Spacer(modifier = Modifier.size(6.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(text = "Amount: ${it.amount}", style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colors.PrimaryText, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Text(text = "Issued on: ${it.issue_date}", style = MaterialTheme.typography.subtitle1, color = MaterialTheme.colors.PrimaryText, maxLines = 2, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}