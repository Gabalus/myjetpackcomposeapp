import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myjetpackcomposeapp.data.db.entities.Category

@Composable
fun AddItemScreen(
    currentCategory: Category?,
    onAddClick: (categoryId: Int, shortName: String, shortInfo: String?) -> Unit,
    onCancelClick: () -> Unit
) {
    val (shortName, setShortName) = remember { mutableStateOf("") }
    val (shortInfo, setShortInfo) = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Добавить новый элемент в \"${currentCategory?.name ?: "Не выбрано"}\"",
            style = MaterialTheme.typography.h3
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = shortName,
            onValueChange = setShortName,
            label = { Text("Короткое название") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = shortInfo,
            onValueChange = setShortInfo,
            label = { Text("Короткая информация") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                if (currentCategory != null) {
                    onAddClick(
                        currentCategory.categoryId,
                        shortName,
                        shortInfo
                    )
                }
            }) {
                Text("Добавить")
            }
            OutlinedButton(onClick = { onCancelClick() }) {
                Text("Отмена")
            }
        }
    }
}
