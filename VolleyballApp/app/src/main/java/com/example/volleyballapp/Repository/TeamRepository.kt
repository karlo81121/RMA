package com.example.volleyballapp.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.volleyballapp.Models.Team
import com.google.firebase.database.*

class TeamRepository {

    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("teams")

    @Volatile private var INSTANCE : TeamRepository ?= null

    fun getInstance() : TeamRepository {
        return INSTANCE ?: synchronized(this){
            val instance = TeamRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadTeams(teamList : MutableLiveData<List<Team>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _teamList : List<Team> = snapshot.children.map{ dataSnapshot ->
                        dataSnapshot.getValue(Team::class.java)!!
                    }

                    teamList.postValue(_teamList)

                }catch (e : Exception){

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}