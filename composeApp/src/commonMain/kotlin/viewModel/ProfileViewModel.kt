package org.example.compose.home


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import database.entity.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.ProfileRepository

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    val allProfiles: Flow<List<Profile>> = repository.allProfiles
    val userProfile = mutableStateOf<Profile?>(null)

    init {
        viewModelScope.launch {
            userProfile.value = repository.getProfile()
        }
    }
    fun upsert(profile: Profile) = viewModelScope.launch {
        val existingProfile = userProfile.value
        val profileToUpsert = profile.copy(id = existingProfile?.id ?: 0) // Keep same ID
        repository.upsert(profileToUpsert)
    }
    fun update(profile: Profile) = viewModelScope.launch {
        repository.update(profile)
    }

    fun delete(profile: Profile) = viewModelScope.launch {
        repository.deleteAllProfile()
    }
    suspend fun getProfileCount(): Int {
        return repository.getProfileCount()
    }
    fun deleteAllProfiles() {
        viewModelScope.launch {
            try {
                repository.deleteAllProfile() // Call repository to delete all data

            } catch (exception: Exception) {
                // Handle exception (e.g., logging)
            }
        }
    }
    fun getProfileById(profileId: Int): Flow<Profile?> {
        return repository.getProfileById(profileId)
    }
}