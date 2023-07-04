package com.example.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.map
import com.example.cryptoapp.data.database.AppDatabase
import com.example.cryptoapp.data.mapper.CoinMapper
import com.example.cryptoapp.data.network.ApiFactory
import com.example.cryptoapp.domain.entity.CoinInfo
import com.example.cryptoapp.domain.repository.CoinRepository
import kotlinx.coroutines.delay

class CoinRepositoryImp(private val application: Application):CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()

    private val mapper = CoinMapper()

    private val apiService = ApiFactory.apiService

    override fun getCoinInfoList(): LiveData<List<CoinInfo>>{
        return MediatorLiveData<List<CoinInfo>>().apply {
            addSource(coinInfoDao.getPriceList()){
                it.map {coinInfoDbModel ->
                    mapper.mapDbModelToEntity(coinInfoDbModel)
                }
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return MediatorLiveData<CoinInfo>().apply {
            addSource(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)){
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override suspend fun loadData() {
       while (true){
           val topCoins = apiService.getTopCoinsInfo(limit = 50)
           val fSyms = mapper.mapNamesListToString(topCoins)
           val jsonContainer = apiService.getFullPriceList(fSyms = fSyms )
           val coinInfoDtoList = mapper.mapJsonContainerToListInfo(jsonContainer)
           val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }

           coinInfoDao.insertPriceLis(dbModelList)

           delay(10000)
       }
    }
}