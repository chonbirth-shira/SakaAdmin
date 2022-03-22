package shira.chonbirth.sakaadmin.data

data class AddOrder(
    val job_id: String,
    val customer_name: String,
    val customer_contact: String,
    val customer_address: String,
    val order_date: String,
    val delivery_date: String,
    val total: Int,
    val advance: Int,
    val particulars: ArrayList<String>,
    val description: String,
    val status: String
)