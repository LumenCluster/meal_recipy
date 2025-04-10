package viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.example.compose.home.ProfileViewModel
import repository.MealPlanRepository
import repository.ProfileRepository
import ui.home.MealPlanViewModel
import kotlin.reflect.KClass

class ProfileViewModelFactory(private val repository: ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return ProfileViewModel(repository) as T
        }
    }

