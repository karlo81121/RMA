package com.example.volleyballapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.volleyballapp.Repository.GameRepository

class GameViewModel : ViewModel() {
    private val gameRepository : GameRepository
    private val _allGames = MutableLiveData<List<Game>>()

    init {
        gameRepository = GameRepository().getInstance()
        gameRepository.loadGames(_allGames)
    }
}