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
    onSearch: (String) -> Unit,
    onAddItemScreenRequested: () -> Unit
) {
    var items by remember { mutableStateOf(uiState.items) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sortAttribute by remember { mutableStateOf("") }
    var isAscending by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Кнопка "Добавить"
        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = { onAddItemScreenRequested() }) {
                Text("Добавить элемент")
            }
        }

        // Поиск
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
            Button(
                onClick = { onSearch(searchQuery.text) },
            ) {
                Text("Искать")
            }
        }

        // Заголовки таблицы с сортировкой
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
                            items = if (isAscending) {
                                items.sortedBy { it.shortName }
                            } else {
                                items.sortedByDescending { it.shortName }
                            }
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
                            items = if (isAscending) {
                                items.sortedBy { it.shortInfo }
                            } else {
                                items.sortedByDescending { it.shortInfo }
                            }
                            isAscending = !isAscending
                        }
                    )
            )
        }

        // Список элементов
        LazyColumn {
            items(items) { item ->
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
