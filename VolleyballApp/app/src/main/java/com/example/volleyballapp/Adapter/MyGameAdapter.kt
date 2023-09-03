package com.example.volleyballapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.volleyballapp.Models.Game
import com.example.volleyballapp.R

class MyGameAdapter(private val gameList: ArrayList<Game>) : RecyclerView.Adapter<MyGameAdapter.MyViewHolder>() {

    private lateinit var mListener: OnItemClickListener

     interface OnItemClickListener {
         fun onItemClick(position: Int)
     }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.game_item,
            parent, false
        )
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = gameList[position]

        holder.firstTeam.text = currentItem.firstTeam
        holder.secondTeam.text = currentItem.secondTeam
    }

    override fun getItemCount(): Int {
        return gameList.size
    }

    fun updateGameList(gameList : List<Game>){
        this.gameList.clear()
        this.gameList.addAll(gameList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView : View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val firstTeam : TextView = itemView.findViewById(R.id.game_first_team)
        val secondTeam : TextView = itemView.findViewById(R.id.game_second_team)

        init {
            itemView.setOnClickListener({
                clickListener.onItemClick(adapterPosition)
            })
        }
    }
}