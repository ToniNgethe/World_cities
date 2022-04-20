package com.toni.citiesoftheworld.data.repositories

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.toni.citiesoftheworld.data.local.CitiesDb
import com.toni.citiesoftheworld.data.services.CitiesService
import com.toni.citiesoftheworld.domain.model.City
import com.toni.citiesoftheworld.domain.model.RemoteKey
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CitiesRemoteMediator(
    private val db: CitiesDb,
    private val query: String? = null,
    private val apiServices: CitiesService
) : RemoteMediator<Int, City>() {

    override suspend fun initialize(): InitializeAction {
        // Require that remote REFRESH is launched on initial load and succeeds before launching
        // remote PREPEND / APPEND.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, City>): MediatorResult {
        return try {
            val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
                is MediatorResult.Success -> {
                    return pageKeyData
                }
                else -> {
                    pageKeyData as Int
                }
            }

            val response = apiServices.fetch(
                page = page, query = if (query?.isEmpty() == true) null else query
            )
            val isEndOfList = response.data?.items?.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.cityDao().deleteAll()
                    db.remoteKeyDao().deleteAll()
                }
                val previousKey = if (page == 1) null else page - 1
                val nextKey = if (isEndOfList == true) null else page + 1

                val cityData = response.data?.items?.map { it!!.toCity() }
                val keys = cityData?.map { cityItem ->
                    RemoteKey(cityItem.id, previousKey = previousKey, nextKey = nextKey)
                }
                db.remoteKeyDao().insertAll(keys!!)
                db.cityDao().insertAll(cityData)
            }

            MediatorResult.Success(endOfPaginationReached = isEndOfList == true)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: Exception){
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, City>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.APPEND -> {
                val remoteKey = getLastRemoteKey(state)
                val nextKey = remoteKey?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }

            LoadType.PREPEND -> {
                val remoteKey = getFirstRemoteKey(state)
                val prevKey = remoteKey?.previousKey ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, City>): RemoteKey? =
        state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { city -> db.remoteKeyDao().getRemoteKeyByCityId(city.id) }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, City>): RemoteKey? =
        state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { cityId ->
                db.remoteKeyDao().getRemoteKeyByCityId(cityId)
            }
        }

    private suspend fun getLastRemoteKey(state: PagingState<Int, City>): RemoteKey? =
        state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { city ->
            db.remoteKeyDao().getRemoteKeyByCityId(city.id)
        }
}