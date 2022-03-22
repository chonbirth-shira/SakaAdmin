package shira.chonbirth.sakaadmin.data

data class OrdersPrintData(
    val job_id: Int,
    val customer_name: String,
    val customer_address: String,
    val job_category: String,
    val size: String,
    val delivery_date: String,
    val image: String,
    val perticular: String,
    val job_status: String
)