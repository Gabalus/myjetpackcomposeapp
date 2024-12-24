package com.example.myjetpackcomposeapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myjetpackcomposeapp.data.db.entities.Category
import com.example.myjetpackcomposeapp.viewmodel.UiState

@Composable
fun CategoryScreen(
    uiState: UiState,
    onCategoryClick: (Category) -> Unit,
    onLogin: (String, String) -> Unit,
    onLogout: () -> Unit,
    onAddCategory: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
/*            var loginText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = loginText,
                onValueChange = { loginText = it },
                label = { Text("Логин") },
                modifier = Modifier.weight(1f)
            )

            var passText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = passText,
                onValueChange = { passText = it },
                label = { Text("Пароль") },
                modifier = Modifier.weight(1f)
            )*/

            if (!uiState.isUserAuthenticated) {
                Button(onClick = { onLogin("loginText", "passText") }) {
                    Text("Войти")
                }
            } else {
                Button(onClick = { onLogout() }) {
                    Text("Выйти")
                }
            }
        }

        var newCategoryName by remember { mutableStateOf("") }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text("Название новой категории") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newCategoryName.isNotBlank()) {
                        onAddCategory(newCategoryName)
                        newCategoryName = ""
                    }
                }
            ) {
                Text("Добавить")
            }
        }

        Text(
            text = "Выберите категорию:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn {
            items(uiState.categories) { cat ->
                Text(
                    text = cat.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategoryClick(cat) }
                        .padding(8.dp)
                )
            }
        }
    }
}
