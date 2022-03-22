package shira.chonbirth.sakaadmin.data

data class Data(
    val job_id: Int,
    val customer_name: String,
    val customer_contact: String,
    val customer_address: String,
    val job_category: String,
    val job_description: String,
    val size: String,
    val order_date: String,
    val delivery_date: String,
    val amount: Int,
    val advance: Int,
    val job_status: String,
    val remarks: String,
    val case: Boolean,
    val image: String,
    val issue_date: String
)