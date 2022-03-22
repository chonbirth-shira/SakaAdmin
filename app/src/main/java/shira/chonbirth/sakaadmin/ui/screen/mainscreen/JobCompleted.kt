package shira.chonbirth.sakaadmin.ui.screen.mainscreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import shira.chonbirth.sakaadmin.OptionDialog3
import shira.chonbirth.sakaadmin.R
import shira.chonbirth.sakaadmin.components.days
import shira.chonbirth.sakaadmin.data.OrdersPrintData
import shira.chonbirth.sakaadmin.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@Composable
fun JobComplete(navHostController: NavHostController, db: FirebaseFirestore) {
    var id by remember {
        mutableStateOf(0)
    }
    var onoff by remember {
        mutableStateOf(false)
    }
    OptionDialog3(onoff, onYesClicked = { onoff = false }, db = db, id)
    Scaffold(
        topBar = {
            Surface(
                elevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.Texture
            ) {
                Column {
                    Text(
                        text = "SAKA Offset Printing",
                        modifier = Modifier.padding(start = 10.dp, top = 6.dp, bottom = 2.dp),
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.primary
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Completed",
                            modifier = Modifier.padding(start = 10.dp, bottom = 14.dp),
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.colors.PrimaryText
                        )
                        Row(modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)) {
                            IconButton(
                                onClick = {
                                    navHostController.navigate("issued")
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        content = {
            val datas = mutableListOf<OrdersPrintData>()
            val allList = MutableStateFlow<List<OrdersPrintData>>(emptyList())
            val dd: StateFlow<List<OrdersPrintData>> = allList

            db.collection("flexorders").whereEqualTo("job_status", "Completed")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val job_id: Int = document.get("job_id").toString().toInt()
                        val category: String = document.getString("job_category").toString()
                        val customer: String = document.getString("customer_name").toString()
                        val customer_addr: String =
                            document.getString("customer_address").toString()
                        val status: String = document.getString("job_status").toString()
                        val dorder: String = document.getString("delivery_date").toString()
                        val size: String = document.getString("size").toString()
                        val image: String = document.getString("image").toString()
                        var perticular: String = document.getString("perticular").toString()
                        datas.add(
                            OrdersPrintData(
                                job_id = job_id,
                                job_category = category,
                                customer_name = customer,
                                customer_address = customer_addr,
                                job_status = status,
                                size = size,
                                delivery_date = dorder,
                                image = image,
                                perticular = perticular
                            )
                        )
                    }
                    allList.value = datas
                }

            // Change Listener
            db.collection("flexorders").whereEqualTo("job_status", "Completed")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    val cities = ArrayList<OrdersPrintData>()
                    for (document in value!!) {
                        val job_id: Int = document.get("job_id").toString().toInt()
                        val category: String = document.getString("job_category").toString()
                        val customer: String = document.getString("customer_name").toString()
                        val customer_addr: String =
                            document.getString("customer_address").toString()
                        val status: String = document.getString("job_status").toString()
                        val dorder: String = document.getString("delivery_date").toString()
                        val size: String = document.getString("size").toString()
                        val image: String = document.getString("image").toString()
                        var perticular: String = document.getString("perticular").toString()
                        cities.add(
                            OrdersPrintData(
                                job_id = job_id,
                                job_category = category,
                                customer_name = customer,
                                customer_address = customer_addr,
                                job_status = status,
                                size = size,
                                delivery_date = dorder,
                                image = image,
                                perticular = perticular
                            )
                        )
                    }
                    allList.value = cities
                }
            //

            val ss = dd.collectAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp)
                    .background(
                        MaterialTheme.colors.Background
                    )
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
                        .padding(start = 8.dp, end = 8.dp, top = 6.dp),
                        shape = Shapes.small,
                        elevation = 1.dp,
                        onClick = {
                            id = it.job_id
                            onoff = true
                        }
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(
                                    MaterialTheme.colors.Items, shape = Shapes.small
                                )
                        ) {
                            Box(
                                modifier = Modifier.padding(
                                    start = 2.dp,
                                    top = 3.dp,
                                    bottom = 3.dp
                                )
                            ) {
                                Image(
                                    painter = rememberImagePainter(data = it.image, builder = {
//                                        transformations(
//                                            RoundedCornersTransformation(8F)
////                                            CircleCropTransformation()
//                                        )
                                        this.size(94).placeholder(R.drawable.placeholder)
                                            .error(R.drawable.error)
                                    }),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(94.dp)
                                        .padding(start = 2.dp, top = 3.dp, bottom = 3.dp)
                                        .clip(shape = Shapes.small),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )
//                                Canvas(
//                                    modifier = Modifier
//                                        .size(18.dp)
//                                        .clip(shape = CircleShape)
//                                        .border(2.dp, color = Color.White, shape = CircleShape)
//                                ) {
//                                    drawCircle(color = color)
//                                }
                            }
//                            Image(painter = painterResource(id = R.drawable.banner_pic), modifier = Modifier.size(100.dp), alignment = Alignment.CenterEnd, contentDescription = null, contentScale = ContentScale.FillBounds)
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp, bottom = 10.dp, end = 10.dp, top = 6.dp)
                                    .weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Canvas(modifier = Modifier.size(16.dp)){
//                                        drawCircle(color = color)
//                                    }
                                    Spacer(modifier = Modifier.size(6.dp))
                                    Text(
                                        text = "${it.job_id} ${it.customer_name}",
                                        style = MaterialTheme.typography.h6,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                                Spacer(modifier = Modifier.size(6.dp))
//                                Text(
//                                    text = it.customer_name,
//                                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
//                                    color = MaterialTheme.colors.PrimaryText,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis
//                                )
//                                Text(
//                                    text = it.customer_address,
//                                    style = MaterialTheme.typography.subtitle2,
//                                    color = MaterialTheme.colors.PrimaryText,
//                                    maxLines = 1,
//                                    overflow = TextOverflow.Ellipsis
//                                )
                                val sizes = remember {
                                    mutableListOf<String>()
                                }
                                sizes.clear()
                                it.perticular.split(",").forEach {
                                    sizes.add( it.split("_").get(1))
                                }
                                LazyVerticalGrid(cells = GridCells.Fixed(3),
                                    content = {
                                        items(sizes.size) {
                                            Box(modifier = Modifier
                                                .padding(end = 5.dp, bottom = 6.dp)
                                                .clip(shape = Shapes.small)
                                                .background(color = MaterialTheme.colors.Pill)) {
                                                Text(
                                                    text = sizes[it],
                                                    modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                                                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
                                                    color = MaterialTheme.colors.Texture,
                                                )
                                            }
                                        }
                                    })
                            }
//                            Column(
//                                modifier = Modifier
//                                    .fillMaxHeight()
//                                    .weight(0.35f)
//                                    .padding(all = 10.dp),
//                                horizontalAlignment = Alignment.End,
//                                verticalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Row(verticalAlignment = Alignment.Bottom) {
//                                    when {
//                                        day == 0 -> {
//                                            Box(
//                                                modifier = Modifier
//                                                    .padding(4.dp)
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
//                                Canvas(modifier = Modifier
//                                    .size(18.dp)
//                                    .clip(shape = CircleShape)
//                                    .border(2.dp, color = Color.White, shape = CircleShape)){
//                                    drawCircle(color = color)
//                                }
////                                Text(text = "--", style = MaterialTheme.typography.overline)
////                                Text(
////                                    text = it.size,
////                                    style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
////                                    color = MaterialTheme.colors.primary
////                                )
//                            }
                        }
                    }
                }
            }
        }
    )
}