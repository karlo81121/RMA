package com.example.volleyballapp

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.volleyballapp.databinding.ActivityMapActivitiyBinding

class gameItemById : AppCompatActivity()  {

    private lateinit var gameId: TextView
    private lateinit var firstTeam: TextView
    private lateinit var secondTeam: TextView
    private lateinit var datePlayed: TextView
    private lateinit var location: TextView
    private lateinit var playedAt: TextView

    private lateinit var mapButton: Button

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_item_by_id)

        initView()
        setValuesToViews()

        mapButton = findViewById(R.id.mapButton)
        mapButton.setOnClickListener {
            val intent = Intent(this, mapActivitiy::class.java)
            startActivity(intent)
        }
    }

    private fun initView(){
        firstTeam = findViewById(R.id.game_first_team_id)
        secondTeam = findViewById(R.id.game_second_team_id)
        datePlayed = findViewById(R.id.game_date_played_id)
        location = findViewById(R.id.game_location_id)
        playedAt = findViewById(R.id.game_played_at_id)
    }

    private fun setValuesToViews(){
        firstTeam.text = intent.getStringExtra("firstTeam")
        secondTeam.text = intent.getStringExtra("secondTeam")
        datePlayed.text = intent.getStringExtra("datePlayed")
        location.text = intent.getStringExtra("location")
        playedAt.text = intent.getStringExtra("playedAt")
    }
}