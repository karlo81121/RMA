package com.example.volleyballapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.volleyballapp.Repository.PlayerRepository

class PlayerViewModel : ViewModel() {
    private val repository : PlayerRepository
    private val _allPlayers = MutableLiveData<List<Player>>()
    val allPlayers : LiveData<List<Player>> = _allPlayers

    init {
        repository = PlayerRepository().getInstance()
        repository.loadPlayers(_allPlayers)
    }
}