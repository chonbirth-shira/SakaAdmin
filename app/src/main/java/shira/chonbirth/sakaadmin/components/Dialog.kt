package shira.chonbirth.sakaadmin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import shira.chonbirth.sakaadmin.ui.theme.Shapes
import shira.chonbirth.sakaadmin.viewmodels.SharedViewModel

@Composable
fun errorDialog(
    sharedViewModel: SharedViewModel,
    openDialog: Boolean,
    onYesClicked: () -> Unit
){
    if (openDialog){
        Dialog(onDismissRequest = { onYesClicked() }) {
            Column(modifier = Modifier.clip(shape = Shapes.small).background(color = MaterialTheme.colors.background)) {
                Text(text = "Please try again!", style = MaterialTheme.typography.h6, modifier = Modifier.padding(10.dp))
                sharedViewModel.input.value = ""
            }
        }
    }
}