package com.example.volleyballapp.Repository

import androidx.lifecycle.MutableLiveData
import com.example.volleyballapp.Models.Player
import com.google.firebase.database.*

class PlayerRepository {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("players")

    @Volatile private var INSTANCE : PlayerRepository ?= null

    fun getInstance() : PlayerRepository {
        return INSTANCE ?: synchronized(this){
            val instance = PlayerRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadPlayers(playerList : MutableLiveData<List<Player>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _playerList : List<Player> = snapshot.children.map{ dataSnapshot ->
                        dataSnapshot.getValue(Player::class.java)!!
                    }

                    playerList.postValue(_playerList)

                }catch (e : Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}