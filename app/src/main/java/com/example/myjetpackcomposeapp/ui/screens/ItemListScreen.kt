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
import androidx.compose.ui.unit.dp
import com.example.myjetpackcomposeapp.viewmodel.UiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemListScreen(
    uiState: UiState,
    onItemClick: (Int) -> Unit,
    onSearch: (String) -> Unit,
    onAddItemScreenRequested: () -> Unit
) {
    val items = uiState.items
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Кнопка "Добавить"
        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = { onAddItemScreenRequested() }) {
                Text("Добавить элемент")
            }
        }

        // Поиск
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onSearch(it.text)
            },
            label = { Text("Поиск по названию") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Список элементов
        LazyColumn {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item.itemId) }
                        .padding(8.dp)
                ) {
                    Text(text = item.shortName, modifier = Modifier.weight(1f))
                    Text(text = item.shortInfo ?: "", modifier = Modifier.weight(1f))
                    Text(
                        text = "Сорт.",
                        modifier = Modifier
                            .combinedClickable(
                                onClick = { onItemClick(item.itemId) },)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

