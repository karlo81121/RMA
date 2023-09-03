package com.example.volleyballapp.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.volleyballapp.Adapter.MyGameAdapter
import com.example.volleyballapp.Models.Game
import com.google.firebase.database.*

class GameRepository {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("games")

    @Volatile private var INSTANCE : GameRepository ?= null

    fun getInstance() : GameRepository {
        return INSTANCE ?: synchronized(this){
            val instance = GameRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadGames(gameList : MutableLiveData<List<Game>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _gameList : List<Game> = snapshot.children.map{ dataSnapshot ->
                        dataSnapshot.getValue(Game::class.java)!!
                    }

                    gameList.postValue(_gameList)

                }catch (e : Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}