package com.example.quizwiz.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizwiz.R
import com.example.quizwiz.models.ResultModel

class QuizSummeryAdapter(private val list:ArrayList<ResultModel>):
    RecyclerView.Adapter<QuizSummeryAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvPosition:TextView = view.findViewById(R.id.tvSummeryPosition)
        val tvTime:TextView = view.findViewById(R.id.tvSummeryTime)
        val tvType:TextView = view.findViewById(R.id.tvSummeryType)
        val tvDifficulty:TextView = view.findViewById(R.id.tvSummeryDifficulty)
        val tvScore:TextView = view.findViewById(R.id.tvSummeryScore)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_stats_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.tvPosition.text = (position+1).toString() +"."
        holder.tvTime.text = item.time.toString()
        holder.tvType.text = item.type
        holder.tvDifficulty.text = item.difficulty
        holder.tvScore.text = item.score.toString()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}