package com.example.volleyballapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyballapp.Adapter.GameImageAdapter
import com.example.volleyballapp.Models.GameImage
import com.google.firebase.database.*

class AllGamePhotosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageList: ArrayList<GameImage>
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_game_photos)

        recyclerView = findViewById(R.id.imageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        imageList = arrayListOf()

        databaseReference = FirebaseDatabase.getInstance().getReference("userImages")
        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(dataSnapshot in snapshot.children){
                        val image = dataSnapshot.getValue(GameImage::class.java)
                        imageList.add(image!!)
                    }
                    recyclerView.adapter = GameImageAdapter(imageList, this@AllGamePhotosActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AllGamePhotosActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun finish(){
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}