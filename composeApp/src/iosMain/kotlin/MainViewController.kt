import androidx.compose.ui.window.ComposeUIViewController
import dev.rivu.courses.medicinereminder.DIProvider
import dev.rivu.courses.medicinereminder.data.db.DBInitializer

fun MainViewController() = ComposeUIViewController { App(DIProvider.getOrCreateInstance(DBInitializer)) }