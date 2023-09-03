package com.example.volleyballapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyballapp.Adapter.MyGameAdapter
import com.example.volleyballapp.Models.Game
import com.google.firebase.database.*

class AllGamesActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var gameRecyclerView: RecyclerView
    private lateinit var gameArrayList: ArrayList<Game>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_games)

        gameRecyclerView = findViewById(R.id.gameRecyclerView)
        gameRecyclerView.layoutManager = LinearLayoutManager(this)
        gameRecyclerView.setHasFixedSize(true)

        gameArrayList = arrayListOf<Game>()
        getUserData()

        val takePhotoButton = findViewById<Button>(R.id.uploadYourPhoto)
        val checkPhotosButton = findViewById<Button>(R.id.check_game_photos)

        takePhotoButton.setOnClickListener({
            val intent = Intent(this, savePhotoToGallery::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        checkPhotosButton.setOnClickListener({
            val intent = Intent(this, AllGamePhotosActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
    }

    override fun finish(){
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun getUserData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("games")

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                gameArrayList.clear()
                if(snapshot.exists()){
                    for(gameSnapshot in snapshot.children){
                        val game = gameSnapshot.getValue(Game::class.java)
                        gameArrayList.add(game!!)
                    }
                    val mAdapter = MyGameAdapter(gameArrayList)

                    mAdapter.setOnItemClickListener(object: MyGameAdapter.OnItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@AllGamesActivity, gameItemById::class.java)
                            intent.putExtra("gameId", gameArrayList[position].gameId)
                            intent.putExtra("firstTeam", gameArrayList[position].firstTeam)
                            intent.putExtra("secondTeam", gameArrayList[position].secondTeam)
                            intent.putExtra("datePlayed", gameArrayList[position].datePlayed)
                            intent.putExtra("location", gameArrayList[position].location)
                            intent.putExtra("playedAt", gameArrayList[position].playedAt)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }

                    })
                    gameRecyclerView.adapter = mAdapter
                    gameRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}