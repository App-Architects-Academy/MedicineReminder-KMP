import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.rivu.courses.medicinereminder.DIProvider
import dev.rivu.courses.medicinereminder.data.db.DBInitializer

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "MedicineReminder") {
        App(DIProvider.getOrCreateInstance(DBInitializer))
    }
}