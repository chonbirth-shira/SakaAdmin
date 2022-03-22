package shira.chonbirth.sakaadmin.data

data class CustDetail(
    val job_id: Int,
    val customer_name: String,
    val customer_address: String,
    val customer_contact: String,
    val order_date: String,
    val delivery_date: String,
    val amount: Int,
    val advance: Int,
    val job_status: String,
    val remarks: String,
)
