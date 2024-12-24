package com.example.myjetpackcomposeapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
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

    val displayedItems by remember {
        derivedStateOf {
            uiState.items.filter {
                it.shortName.contains(searchQuery.text, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(8.dp)) {
            if (uiState.isUserAuthenticated)
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
        }

        LazyColumn {
            items(displayedItems) { item ->
                var isDoseFirst by remember { mutableStateOf(true) }

                val sortedFields by remember {
                    derivedStateOf {
                        if (isDoseFirst) {
                            listOf(
                                "Доза" to (item.numberOrDose ?: "-"),
                                "Цена" to (item.price?.toString() ?: "-")
                            )
                        } else {
                            listOf(
                                "Цена" to (item.price?.toString() ?: "-"),
                                "Доза" to (item.numberOrDose ?: "-")
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { onItemClick(item) },
                            onLongClick = { isDoseFirst = !isDoseFirst }
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Название: ${item.shortName}",
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    sortedFields.forEach { (label, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = label, modifier = Modifier.weight(1f))
                            Text(text = value, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
