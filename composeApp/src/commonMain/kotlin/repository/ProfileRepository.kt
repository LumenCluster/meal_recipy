package repository

import database.ProfileDao
import database.entity.Profile
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val profileDao: ProfileDao) {
    val allProfiles: Flow<List<Profile>> = profileDao.getAllProfiles()

    suspend fun upsert(profile: Profile) {
        profileDao.upsertProfile(profile)
    }
    suspend fun getProfile(): Profile? {
        return profileDao.getProfile()
    }
    suspend fun update(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    suspend fun delete(profile: Profile) {
        profileDao.deleteProfile(profile)
    }
    suspend fun deleteAllProfile() {
        profileDao.deleteAllProfile()
    }
    fun getProfileById(profileId: Int): Flow<Profile?> {
        return profileDao.getProfileById(profileId)
    }

    suspend fun getProfileCount(): Int {
        return profileDao.getProfileCount()
    }
}

