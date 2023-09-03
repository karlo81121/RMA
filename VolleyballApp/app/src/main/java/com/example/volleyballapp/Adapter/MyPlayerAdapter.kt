package com.example.volleyballapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyballapp.Models.Player
import com.example.volleyballapp.R

class MyPlayerAdapter : RecyclerView.Adapter<MyPlayerAdapter.MyViewHolder>() {

    private val playerList = ArrayList<Player>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.player_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = playerList[position]

        holder.firstName.text = currentItem.firstName
        holder.lastName.text = currentItem.lastName
        holder.age.text = currentItem.age
        holder.position.text = currentItem.position
        holder.nationalTeam.text = currentItem.nationalTeam
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    fun updatePlayerList(playerList : List<Player>){
        this.playerList.clear()
        this.playerList.addAll(playerList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val firstName : TextView = itemView.findViewById(R.id.player_first_name)
        val lastName : TextView = itemView.findViewById(R.id.player_last_name)
        val age : TextView = itemView.findViewById(R.id.age)
        val position : TextView = itemView.findViewById(R.id.position)
        val nationalTeam : TextView = itemView.findViewById(R.id.nationalTeam)
    }
}