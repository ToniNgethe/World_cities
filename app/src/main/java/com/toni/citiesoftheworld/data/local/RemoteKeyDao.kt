package com.toni.citiesoftheworld.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.toni.citiesoftheworld.domain.model.RemoteKey

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE cityId = :id")
    suspend fun getRemoteKeyByCityId(id: Int): RemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}