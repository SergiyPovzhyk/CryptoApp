package com.example.cryptoapp.domain.usecases

import com.example.cryptoapp.domain.repository.CoinRepository

class LoadDataUseCase(private val repository: CoinRepository) {
    suspend operator fun invoke(){
        repository.loadData()
    }
}