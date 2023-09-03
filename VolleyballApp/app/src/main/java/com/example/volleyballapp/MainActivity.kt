 package com.example.volleyballapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.volleyballapp.Models.Game
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

 class MainActivity : AppCompatActivity() {

     private lateinit var databaseReference: DatabaseReference

     override fun onBackPressed() {
         super.onBackPressed()
         overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val allPlayersButton = findViewById<Button>(R.id.players_button)
        val allGamesButton = findViewById<Button>(R.id.games_button)
        val allTeamsButton = findViewById<Button>(R.id.teams_button)

        allGamesButton.setOnClickListener({
            val intent = Intent(this, AllGamesActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        allPlayersButton.setOnClickListener({
            val intent = Intent(this, AllPlayerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        allTeamsButton.setOnClickListener({
            val intent = Intent(this, AllTeamsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

        createNotificationChannel()

        databaseReference = FirebaseDatabase.getInstance().getReference("games")

        databaseReference.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val game = snapshot.getValue(Game::class.java)

                val currentDate = getCurrentDate()
                Log.d("Notification", "Current date: $currentDate")

                if (game != null) {
                    Log.d("Notification", "Game datePlayed: ${game.datePlayed}")
                    if (game.datePlayed == currentDate) {
                        val notification = createNotification(game)
                        showNotification(notification)
                        Log.d("Notification", "Notification displayed for game: ${game.firstTeam} vs ${game.secondTeam}")
                    } else {
                        Log.d("Notification", "No matching date found for game: ${game.firstTeam} vs ${game.secondTeam}")
                    }
                } else {
                    Log.d("Notification", "Game is null")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

     private fun createNotificationChannel() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val channelId = "your_channel_id" // Choose a unique channel ID
             val channelName = "Your Channel Name" // Choose a user-friendly channel name
             val channelDescription = "Your Channel Description" // Provide a description for the channel
             val importance = NotificationManager.IMPORTANCE_DEFAULT
             val channel = NotificationChannel(channelId, channelName, importance)
             channel.description = channelDescription

             val notificationManager =
                 getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             notificationManager.createNotificationChannel(channel)
         }
     }

     private fun createNotification(game: Game): Notification {
         val channelId = "your_channel_id" // Use the same channel ID you created earlier
         val builder = NotificationCompat.Builder(this, channelId)
             .setSmallIcon(R.drawable.ic_baseline_email_24) // Set a small icon for the notification
             .setContentTitle("Game Today") // Set the title
             .setContentText("${game.firstTeam} vs ${game.secondTeam} in ${game.location}: ${game.playedAt}") // Set the content text
             .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Set the notification priority
             .setAutoCancel(true) // Close the notification when tapped

         return builder.build()
     }

     private fun showNotification(notification: Notification) {
         val notificationId = (System.currentTimeMillis() % 10000).toInt() // Use a unique ID
         val notificationManager = NotificationManagerCompat.from(this)
         notificationManager.notify(notificationId, notification)
     }

     private fun getCurrentDate(): String {
         // Get the current date in the format "d MMMM yyyy" (e.g., "10 October 2023")
         val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.US)
         return dateFormat.format(Date())
     }
}