package com.example.myjetpackcomposeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myjetpackcomposeapp.data.db.entities.Item
import com.example.myjetpackcomposeapp.viewmodel.UiState

@Composable
fun DetailScreen(
    uiState: UiState,
    onSave: (Item) -> Unit,
    onDelete: (Int) -> Unit,
    onCancelClick: () -> Unit,
) {
    val selectedItem = uiState.selectedItem
    if (selectedItem == null) {
        Text("Нет данных для отображения")
        return
    }

    val canEdit = uiState.isUserAuthenticated

    var numberOrDose by remember { mutableStateOf(selectedItem.numberOrDose ?: "") }
    var volumeOrCapacity by remember { mutableStateOf(selectedItem.volumeOrCapacity ?: "") }
    var price by remember { mutableStateOf(selectedItem.price?.toString() ?: "") }
    var isPrescription by remember { mutableStateOf(selectedItem.isPrescription ?: false) }
    var additionalInfo by remember { mutableStateOf(selectedItem.additionalInfo ?: "") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Детализация: ${selectedItem.shortName}",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = numberOrDose,
            onValueChange = { if (canEdit) numberOrDose = it },
            label = { Text("Номер / Дозировка") },
            enabled = canEdit,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = volumeOrCapacity,
            onValueChange = { if (canEdit) volumeOrCapacity = it },
            label = { Text("Объём / Ёмкость") },
            enabled = canEdit,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { if (canEdit) price = it },
            label = { Text("Цена") },
            enabled = canEdit,
            modifier = Modifier.fillMaxWidth()
        )


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Требует рецепт (для лекарств): ")
            Checkbox(
                checked = isPrescription,
                onCheckedChange = { if (canEdit) isPrescription = it },
                enabled = canEdit
            )
        }

        OutlinedTextField(
            value = additionalInfo,
            onValueChange = { if (canEdit) additionalInfo = it },
            label = { Text("Доп. информация") },
            enabled = canEdit,
            modifier = Modifier.fillMaxWidth()
        )

        if (canEdit) {
            Button(
                onClick = {
                    val parsedPrice = price.toDoubleOrNull() ?: 0.0
                    val updatedItem = selectedItem.copy(
                        numberOrDose = numberOrDose,
                        volumeOrCapacity = volumeOrCapacity,
                        price = parsedPrice,
                        isPrescription = isPrescription,
                        additionalInfo = additionalInfo
                    )
                    onSave(updatedItem)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Сохранить")
            }
            Button(
                onClick = {
                    val deletedItem = selectedItem.itemId
                    onDelete(deletedItem)
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Удалить")
            }
        }
        OutlinedButton(onClick = { onCancelClick() }) {
            androidx.compose.material.Text("Отмена")
        }

        uiState.errorMessage?.let { msg ->
            if (msg.isNotBlank()) {
                Text(text = "Ошибка: $msg", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
