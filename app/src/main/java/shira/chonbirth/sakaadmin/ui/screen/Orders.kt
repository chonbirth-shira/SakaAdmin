package shira.chonbirth.sakaadmin.ui.screen

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import shira.chonbirth.sakaadmin.OptionDialog
import shira.chonbirth.sakaadmin.components.TodaysEarning
import shira.chonbirth.sakaadmin.components.days
import shira.chonbirth.sakaadmin.data.OrderList
import shira.chonbirth.sakaadmin.data.OrdersListData
import shira.chonbirth.sakaadmin.ui.theme.*
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterialApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun Orders(
    db: FirebaseFirestore,
    navHostController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    var onoff by remember {
        mutableStateOf(false)
    }
    var id by remember {
        mutableStateOf(0)
    }
    OptionDialog(
        sharedViewModel = sharedViewModel,
        onoff,
        onYesClicked = { onoff = false },
        db = db,
        id,
        context = context
    )
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
                        Row(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)) {
                            IconButton(onClick = { navHostController.navigate("new_task") }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    ) {
        val datas = mutableListOf<OrderList>()
        val allList = MutableStateFlow<List<OrderList>>(emptyList())
        val dd: StateFlow<List<OrderList>> = allList

        LaunchedEffect(key1 = true){
            db.collection("orders").whereEqualTo("status", "new")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        var job_id: String = document.get("job_id").toString()
                        var customer: String = document.getString("customer_name").toString()
                        var description: String = document.getString("description").toString()
                        val delivery: String = document.getString("delivery_date").toString()
                        datas.add(
                            OrderList(
                                job_id = job_id,
                                customer_name = customer,
                                description = description,
                                delivery_date = delivery
                            )
                        )
                    }
                    allList.value = datas
                }
        }

        // Change Listener
        db.collection("orders").whereEqualTo("status", "new").addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            val cities = ArrayList<OrderList>()
            for (document in value!!) {
                var job_id: String = document.get("job_id").toString()
                var customer: String = document.getString("customer_name").toString()
                var description: String = document.getString("description").toString()
                val delivery: String = document.getString("delivery_date").toString()
                cities.add(
                    OrderList(
                        job_id = job_id,
                        customer_name = customer,
                        description = description,
                        delivery_date = delivery
                    )
                )
            }
            allList.value = cities
        }

        val ss = dd.collectAsState()
//        if (ss.value.isEmpty()){
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                Text(text = "No Job")
//            }
//        }else {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(bottom = 50.dp)
                .background(MaterialTheme.colors.Background)
        ) {
            items(ss.value) {
                val day = days(it.delivery_date).toInt()
                val color: Color = if (day in 0..1 || day < 0) {
                    Color(0xFFFF5252)
                } else if (day in 2..3) {
                    Color(0xFFFFD740)
                } else {
                    Color(0xFF69F0AE)
                }
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .combinedClickable(onClick = {
                        id = it.job_id.toInt()
                        onoff = true
                    }, onLongClick = {
                        sharedViewModel.selected_jobId.value = it.job_id.toString()
                    }), shape = Shapes.small
                )
                {
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
//                                Canvas(modifier = Modifier.size(16.dp)) {
//                                    drawCircle(color = color)
//                                }
//                                Spacer(modifier = Modifier.size(6.dp))
                                Text(
                                    text = "${it.job_id} ${it.customer_name}",
                                    style = MaterialTheme.typography.h6,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = MaterialTheme.colors.primary
                                )
                            }
                            Spacer(modifier = Modifier.size(2.dp))
                            Text(
                                text = it.description,
                                style = MaterialTheme.typography.subtitle1,
                                color = MaterialTheme.colors.PrimaryText,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
//                            val sizes = remember {
//                                mutableListOf<String>()
//                            }
//                            sizes.clear()
//                            it.perticular.split(",").forEach {
//                                sizes.add( it.split("_").get(1))
//                            }
//                            Row(modifier = Modifier.padding(top = 6.dp)) {
//                                sizes.forEach{
//                                    Box(modifier = Modifier
//                                        .padding(end = 5.dp)
//                                        .clip(shape = Shapes.small)
//                                        .background(color = MaterialTheme.colors.Pill)) {
//                                        Text(
//                                            text = it,
//                                            modifier = Modifier.padding(start = 6.dp, end = 6.dp),
//                                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
//                                            color = MaterialTheme.colors.Texture,
//                                        )
//                                    }
//                                }
//                            }
//                            Text(
//                                text = sizes,
//                                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
//                                color = MaterialTheme.colors.PrimaryText,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.25f)
                                .padding(all = 10.dp),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                when {
                                    day == 0 -> {
                                        Box(
                                            modifier = Modifier
//                                                .padding(4.dp)
                                                .background(
                                                    color = Color(
                                                        0xFFFFFF8D
                                                    ), shape = Shapes.small
                                                )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 8.dp,
                                                    end = 8.dp,
                                                    top = 4.dp,
                                                    bottom = 4.dp
                                                ),
                                                text = "TODAY",
                                                style = MaterialTheme.typography.subtitle2.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color(
                                                    0xFFBF360C
                                                )
                                            )
                                        }
                                    }
                                    day == 1 -> {
                                        Box(
                                            modifier = Modifier
//                                                .padding(4.dp)
                                                .background(
                                                    color = Color(
                                                        0xFFFFFF8D
                                                    ), shape = Shapes.small
                                                )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 8.dp,
                                                    end = 8.dp,
                                                    top = 4.dp,
                                                    bottom = 4.dp
                                                ),
                                                text = "TOMORROW",
                                                style = MaterialTheme.typography.overline.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color(
                                                    0xFFBF360C
                                                )
                                            )
                                        }
                                    }
                                    day > 0 -> {
                                        Text(
                                            text = day.toString(),
                                            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = " Days",
                                            style = MaterialTheme.typography.overline,
                                            color = MaterialTheme.colors.SecondaryText,
                                            modifier = Modifier.padding(bottom = 10.dp)
                                        )
                                    }
                                    else -> {
                                        Box(
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .background(
                                                    color = Color(0xFFFFD8D5),
                                                    shape = Shapes.small
                                                )
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(
                                                    start = 8.dp,
                                                    end = 8.dp,
                                                    top = 4.dp,
                                                    bottom = 4.dp
                                                ),
                                                text = "LATE",
                                                style = MaterialTheme.typography.subtitle2.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Color(0xFFD50000)
                                            )
                                        }
                                    }
                                }
                            }
                            Canvas(modifier = Modifier
                                .size(18.dp)
                                .clip(shape = CircleShape)
                                .border(2.dp, color = Color.White, shape = CircleShape)){
                                drawCircle(color = color)
                            }
//                            Text(
//                                text = it.size,
//                                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
//                                color = MaterialTheme.colors.primary
//                            )
                        }
                    }
                }
            }
        }
//        }
    }
}
