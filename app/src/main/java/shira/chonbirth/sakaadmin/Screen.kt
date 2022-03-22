package shira.chonbirth.sakaadmin

sealed class Screen(val route: String) {
    object Dashboard: Screen(route = "dashboard")
    object NewTask: Screen(route = "new_task")
    object Orders: Screen(route = "orders")
}
