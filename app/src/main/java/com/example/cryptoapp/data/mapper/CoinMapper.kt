package com.example.cryptoapp.data.mapper

import com.example.cryptoapp.data.database.CoinInfoDbModel
import com.example.cryptoapp.data.network.model.CoinInfoDto
import com.example.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.example.cryptoapp.data.network.model.CoinNamesListDto
import com.example.cryptoapp.domain.entity.CoinInfo
import com.google.gson.Gson

class CoinMapper {

    fun mapDtoToDbModel(dto: CoinInfoDto) = CoinInfoDbModel(
        fromSymbol = dto.fromSymbol,
        toSymbol = dto.toSymbol,
        highDay = dto.highDay,
        lowDay = dto.lowDay,
        imageUrl = dto.imageUrl,
        price = dto.price,
        lastMarket = dto.lastMarket,
        lastUpdate = dto.lastUpdate
    )

    fun mapJsonContainerToListInfo(json: CoinInfoJsonContainerDto):List<CoinInfoDto>{
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = json.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinInfoDto::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(namesList: CoinNamesListDto):String{
        return namesList.names?.map {
            it.coinNameDto?.name
        }?.joinToString(",") ?: ""
    }

    fun mapDbModelToEntity(dbModel: CoinInfoDbModel) = CoinInfo(
        fromSymbol = dbModel.fromSymbol,
        toSymbol = dbModel.toSymbol,
        highDay = dbModel.highDay,
        lowDay = dbModel.lowDay,
        imageUrl = dbModel.imageUrl,
        price = dbModel.price,
        lastMarket = dbModel.lastMarket,
        lastUpdate = dbModel.lastUpdate
    )

}