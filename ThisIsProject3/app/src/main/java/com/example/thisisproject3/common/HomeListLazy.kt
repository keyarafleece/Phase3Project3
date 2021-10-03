package com.example.thisisproject3.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thisisproject3.home.HomeListItem
import com.example.thisisproject3.home.ListFile
import com.example.thisisproject3.home.RowType
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter


@Composable
fun HomeListLazy(
    editMode: Boolean,
    itemsSource: SnapshotStateList<ListFile>,
    clickItemHandler: (Int) -> Unit,
    deleteItemHandler: (Int) -> Unit
) {
    LazyColumn {
        when (itemsSource.size) {
            1 -> itemsSource.first().let {
                item {
                    HomeListItem(
                        it.fileName, it.created, RowType.SINGLE, editMode,
                        { clickItemHandler(0) },
                        { deleteItemHandler(0) }
                    )
                }
            }
            else -> {
                itemsIndexed(itemsSource) { index, item ->
                    val rowType = when (index) {
                        0 -> RowType.TOP
                        itemsSource.lastIndex -> RowType.BOTTOM
                        else -> RowType.MIDDLE
                    }
                    HomeListItem(
                        item.fileName, item.created, rowType, editMode,
                        { clickItemHandler(index) },
                        { deleteItemHandler(index) }
                    )
                }
            }
        }
    }
}
data class ListFile(var fileName: String, var created: String)

@Preview
@Composable
fun ListTest() {
    var editMode : Boolean by remember { mutableStateOf(false) }
    val data = remember {
        mutableStateListOf<ListFile>(
            ListFile("Reschedule dinner w/Obamas", "03/10/2021"),
            ListFile("Call Sundar back", "02/10/2021"),
            ListFile("Submit counter-offer to Google", "01/10/2021"),
            ListFile("Decline offers from Twitter & Facebook", "02/10/2021"),
            ListFile("Get Tesla washed", "03/10/2021"),
            ListFile("Go to Yin Yoga class", "02/10/2021"),
            ListFile("Start planning hubby's bday dinner", "01/10/2021"),
            ListFile("Call engraver for hubby's bday Rolex", "02/10/2021")
        )
    }
    Scaffold(
        topBar = {
            val title = if (editMode) "Cancel" else "Edit"
            Column {
                Row(Modifier.padding(16.dp)) {
                    NiceButton(title = title) {
                        editMode = !editMode
                    }
                    Spacer(Modifier.weight(1f))
                    NiceButton(title = "Add") {
                        data.add(ListFile("Another amazing list item ${data.size + 1}", "${Timestamp(System.currentTimeMillis()/1000)}"))
                    }
                }
            }
        }
    ) {
        HomeListLazy(editMode = editMode, itemsSource = data, clickItemHandler = {}) { index ->
            data.removeAt(index)
        }
    }
}