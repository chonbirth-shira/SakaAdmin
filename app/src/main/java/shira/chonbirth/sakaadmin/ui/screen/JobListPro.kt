package shira.chonbirth.sakaadmin.ui.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.onEach
import shira.chonbirth.sakaadmin.data.Data
import shira.chonbirth.sakaadmin.OptionDialog
import shira.chonbirth.sakaadmin.components.days
import shira.chonbirth.sakaadmin.data.OrdersListData
import shira.chonbirth.sakaadmin.data.OrdersListDataDetailPro
import shira.chonbirth.sakaadmin.data.OrdersListDataPro
import shira.chonbirth.sakaadmin.ui.theme.*
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun JobListPro(
    db: FirebaseFirestore,
    navHostController: NavHostController,
//    navHostController1: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            Surface(
                elevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.Texture
            ) {
                Column() {
                    Text(
                        text = "SAKA Offset Printing",
                        modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 2.dp),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Orders",
                        modifier = Modifier.padding(start = 10.dp, bottom = 14.dp),
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.PrimaryText
                    )
                        Row(modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)) {
                        }
                    }
                }
            }
        }
    ) {
        val datas = mutableListOf<OrdersListDataPro>()
        val allList = MutableStateFlow<List<OrdersListDataPro>>(emptyList())
        val dd: StateFlow<List<OrdersListDataPro>> = allList

        LaunchedEffect(key1 = true){
            db.collection("ordersTest").whereEqualTo("job_status", "DTP")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val job_id: Int = document.get("job_id").toString().toInt()
                        val customer: String = document.getString("customer_name").toString()
                        val customer_addr: String = document.getString("customer_address").toString()
                        val dorder: String = document.getString("delivery_date").toString()
                        datas.add(
                            OrdersListDataPro(
                                job_id = job_id,
                                customer_name = customer,
                                customer_address = customer_addr,
                                delivery_date = dorder
                            )
                        )
                    }
                    allList.value = datas
                }
        }

        // Change Listener
        db.collection("ordersTest").whereEqualTo("job_status", "DTP").addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val cities = ArrayList<OrdersListDataPro>()
            for (document in value!!) {
                val job_id: Int = document.get("job_id").toString().toInt()
                val customer: String = document.getString("customer_name").toString()
                val customer_addr: String = document.getString("customer_address").toString()
                val dorder: String = document.getString("delivery_date").toString()
                cities.add(
                    OrdersListDataPro(
                        job_id = job_id,
                        customer_name = customer,
                        customer_address = customer_addr,
                        delivery_date = dorder
                    )
                )
            }
            allList.value = cities
        }

        val ss = dd.collectAsState()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 50.dp)
                .background(MaterialTheme.colors.Background)
        ) {
            items(ss.value) {
//                val day = days(it.delivery_date).toInt()
//                val color: Color = if (day in 0..1 || day < 0) {
//                    Color(0xFFFF5252)
//                } else if (day in 2..3) {
//                    Color(0xFFFFD740)
//                } else {
//                    Color(0xFF69F0AE)
//                }
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .combinedClickable(onClick = {
                    }, onLongClick = {
                    }), shape = Shapes.small
                )
                {
                    Column() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.Items)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp, bottom = 10.dp, end = 10.dp, top = 6.dp)
                                    .weight(0.75f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Canvas(modifier = Modifier.size(16.dp)) {
//                                        drawCircle(color = color)
//                                    }
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text(
                                        text = "${it.job_id}",
                                        style = MaterialTheme.typography.h6,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = it.customer_name,
                                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colors.PrimaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = it.customer_address,
                                    style = MaterialTheme.typography.subtitle2,
                                    color = MaterialTheme.colors.PrimaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxHeight()
//                                    .weight(0.25f)
//                                    .padding(all = 10.dp),
//                                horizontalAlignment = Alignment.End,
//                                verticalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Row(verticalAlignment = Alignment.Bottom) {
//                                    when {
//                                        day == 0 -> {
//                                            Box(
//                                                modifier = Modifier
////                                                .padding(4.dp)
//                                                    .background(
//                                                        color = Color(
//                                                            0xFFFFFF8D
//                                                        ), shape = Shapes.small
//                                                    )
//                                            ) {
//                                                Text(
//                                                    modifier = Modifier.padding(
//                                                        start = 8.dp,
//                                                        end = 8.dp,
//                                                        top = 4.dp,
//                                                        bottom = 4.dp
//                                                    ),
//                                                    text = "TODAY",
//                                                    style = MaterialTheme.typography.subtitle2.copy(
//                                                        fontWeight = FontWeight.Bold
//                                                    ),
//                                                    color = Color(
//                                                        0xFFBF360C
//                                                    )
//                                                )
//                                            }
//                                        }
//                                        day > 0 -> {
//                                            Text(
//                                                text = day.toString(),
//                                                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
//                                            )
//                                            Text(
//                                                text = " Days",
//                                                style = MaterialTheme.typography.overline,
//                                                color = MaterialTheme.colors.SecondaryText,
//                                                modifier = Modifier.padding(bottom = 10.dp)
//                                            )
//                                        }
//                                        else -> {
//                                            Box(
//                                                modifier = Modifier
//                                                    .padding(4.dp)
//                                                    .background(
//                                                        color = Color(0xFFFFD8D5),
//                                                        shape = Shapes.small
//                                                    )
//                                            ) {
//                                                Text(
//                                                    modifier = Modifier.padding(
//                                                        start = 8.dp,
//                                                        end = 8.dp,
//                                                        top = 4.dp,
//                                                        bottom = 4.dp
//                                                    ),
//                                                    text = "LATE",
//                                                    style = MaterialTheme.typography.subtitle2.copy(
//                                                        fontWeight = FontWeight.Bold
//                                                    ),
//                                                    color = Color(0xFFD50000)
//                                                )
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                        }
                        val datas1 = mutableListOf<OrdersListDataDetailPro>()
                        val allList1 = MutableStateFlow<List<OrdersListDataDetailPro>>(emptyList())
                        val dd1: StateFlow<List<OrdersListDataDetailPro>> = allList1
                        db.collection("ordersTest").document(it.job_id.toString()).collection("particulars")
                            .get().addOnSuccessListener {
                                for (doc in it){
                                    val job_category: String = doc.getString("job_category").toString()
                                    val quantity: Int = doc.get("quantity").toString().toInt()
                                    val size: String = doc.getString("size").toString()
                                    datas1.add(
                                        OrdersListDataDetailPro(
                                            job_category = job_category,
                                            quantity = quantity,
                                            size = size
                                        )
                                    )
                                }
                                allList1.value = datas1
                            }
                        val ss1 = dd1.collectAsState()
                        LazyRow( modifier = Modifier.fillMaxWidth()
                        ) {
                            items(ss1.value) {
                                Box(modifier = Modifier.padding(4.dp).background(color = Color.Blue)) {
                                    Row() {
                                        Text(text = "${it.job_category}${it.size}", modifier = Modifier.padding(4.dp).background(color = Color.Gray))
                                        Text(text = "x${it.quantity}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
//        }
    }
}
