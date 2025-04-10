package org.example.compose.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.compose.data.network.models.Meal
import utils.Response

class HomeViewModel(
    private val repository: Repository = Graph0.repository
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        fetchMeals("French") // Default category
    }

    fun fetchMeals(category: String) {
        viewModelScope.launch {
            repository.fetchMeals(category)
                .onStart {
                    _homeState.update { it.copy(isLoading = true, error = null, meals = emptyList()) }
                }
                .collect { result ->
                    when (result) {
                        is Response.Loading -> {
                            _homeState.update { it.copy(isLoading = true, error = null) }
                        }
                        is Response.Success -> {
                            _homeState.update { it.copy(isLoading = false, meals = result.data.meals, error = null) }
                        }
                        is Response.Error -> {
                            _homeState.update { it.copy(isLoading = false, error = result.error?.message) }
                        }
                    }
                }
        }
    }
}

data class HomeState(
    val meals: List<Meal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
