package com.example.volleyballapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyballapp.Models.Team
import com.example.volleyballapp.R

class MyTeamAdapter : RecyclerView.Adapter<MyTeamAdapter.MyViewHolder>() {

    private val teamList = ArrayList<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.team_item,
            parent, false
        )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = teamList[position]

       holder.team.text = currentItem.team
    }

    override fun getItemCount(): Int {
        return teamList.size
    }

    fun updateTeamList(teamList : List<Team>){
        this.teamList.clear()
        this.teamList.addAll(teamList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val team : TextView = itemView.findViewById(R.id.game_team)
    }
}