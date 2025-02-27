package com.example.quizwiz.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizwiz.R
import com.example.quizwiz.constants.Base
import com.example.quizwiz.fireBase.FireBaseClass
import com.example.quizwiz.models.LeaderBoardModel
import com.example.quizwiz.models.UserModel
import com.google.android.material.imageview.ShapeableImageView

class LeaderBoardAdapter(private val itemList: List<UserModel?>, private val type:String) :
    RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRank: TextView = itemView.findViewById(R.id.tvLeaderboardRank)
        val ivImage: ShapeableImageView = itemView.findViewById(R.id.ivLeaderBoardProfilePic)
        val tvName: TextView = itemView.findViewById(R.id.tvLeaderboardName)
        val tvPoints: TextView = itemView.findViewById(R.id.tvLeaderboardScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.leader_board_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userInfo = itemList[position]!!
        holder.tvRank.text = (position + 4).toString()
        holder.tvName.text = userInfo.name
        holder.tvPoints.text = String.format("%.2f",Base.desiredScore(userInfo,type))
        FireBaseClass().setProfileImage(userInfo.image,holder.ivImage)
    }
}