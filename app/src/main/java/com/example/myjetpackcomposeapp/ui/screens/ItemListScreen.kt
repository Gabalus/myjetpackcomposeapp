package com.example.myjetpackcomposeapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.myjetpackcomposeapp.data.db.entities.Item
import com.example.myjetpackcomposeapp.viewmodel.UiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemListScreen(
    uiState: UiState,
    onItemClick: (Item) -> Unit,
    onAddItemScreenRequested: () -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sortAttribute by remember { mutableStateOf("") }
    var isAscending by remember { mutableStateOf(true) }

    val displayedItems by remember {
        derivedStateOf {
            var filteredItems = uiState.items.filter {
                it.shortName.contains(searchQuery.text, ignoreCase = true)
            }
            if (sortAttribute == "name") {
                filteredItems = if (isAscending) {
                    filteredItems.sortedBy { it.shortName }
                } else {
                    filteredItems.sortedByDescending { it.shortName }
                }
            } else if (sortAttribute == "info") {
                filteredItems = if (isAscending) {
                    filteredItems.sortedBy { it.shortInfo }
                } else {
                    filteredItems.sortedByDescending { it.shortInfo }
                }
            }
            filteredItems
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(8.dp)) {
            if(uiState.isUserAuthenticated)
            Button(onClick = { onAddItemScreenRequested() }) {
                Text("Добавить элемент")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск по названию") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Название",
                modifier = Modifier
                    .weight(1f)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            sortAttribute = "name"
                            isAscending = !isAscending
                        }
                    )
            )
            Text(
                text = "Информация",
                modifier = Modifier
                    .weight(1f)
                    .combinedClickable(
                        onClick = { },
                        onLongClick = {
                            sortAttribute = "info"
                            isAscending = !isAscending
                        }
                    )
            )
        }

        LazyColumn {
            items(displayedItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(8.dp)
                ) {
                    Text(text = item.shortName, modifier = Modifier.weight(1f))
                    Text(text = item.shortInfo ?: "", modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
