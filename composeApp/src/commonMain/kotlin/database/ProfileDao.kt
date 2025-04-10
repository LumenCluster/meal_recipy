package database


import androidx.room.*
import database.entity.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Upsert
    suspend fun upsertProfile(profile: Profile)

    @Query("SELECT * FROM profile_table LIMIT 1")
    suspend fun getProfile(): Profile?
    @Update
    suspend fun updateProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Query("SELECT * FROM profile_table WHERE id = :profileId LIMIT 1")
    fun getProfileById(profileId: Int): Flow<Profile?>

    @Query("SELECT * FROM profile_table")
    fun getAllProfiles(): Flow<List<Profile>>

    @Query("DELETE FROM profile_table ")
    suspend fun deleteAllProfile() // Deletes all records from the table

    @Query("SELECT COUNT(*) FROM profile_table")
    suspend fun getProfileCount(): Int
}
