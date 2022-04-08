package shira.chonbirth.sakaadmin.data

data class OrdersIssuedData(
    val job_id: Int,
    val customer_name: String,
    val customer_address: String,
    val delivery_date: String,
    val job_status: String,
    val issue_date: String,
    val amount : Int
)