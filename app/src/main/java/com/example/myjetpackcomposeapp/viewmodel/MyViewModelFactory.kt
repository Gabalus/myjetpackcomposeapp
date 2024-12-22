import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.myjetpackcomposeapp.data.db.AppDatabase
import com.example.myjetpackcomposeapp.data.repository.MainRepository
import com.example.myjetpackcomposeapp.viewmodel.MainViewModel

class MyViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)
        val repo = MainRepository(db)
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(repo) as T
    }
}