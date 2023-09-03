package com.example.volleyballapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.volleyballapp.Repository.TeamRepository

class TeamViewModel : ViewModel() {
    private val teamRepository : TeamRepository
    private val _allTeams = MutableLiveData<List<Team>>()
    val allTeams : LiveData<List<Team>> = _allTeams

    init {
        teamRepository = TeamRepository().getInstance()
        teamRepository.loadTeams(_allTeams)
    }
}