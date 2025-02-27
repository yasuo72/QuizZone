package com.example.quizwiz.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizwiz.R
import com.example.quizwiz.adapter.LeaderBoardAdapter
import com.example.quizwiz.constants.Base
import com.example.quizwiz.constants.Constants
import com.example.quizwiz.databinding.ActivityLeaderBoardBinding
import com.example.quizwiz.fireBase.FireBaseClass
import com.example.quizwiz.models.LeaderBoardModel

class LeaderBoardActivity : AppCompatActivity() {
    private var binding: ActivityLeaderBoardBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBoardBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.leaderBoardRecyclerView?.layoutManager = LinearLayoutManager(this)

        // Load default leaderboard (All Time)
        setLeaderBoard(Constants.allTimeScore)

        binding?.radioGroup?.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.rbAllTime -> setLeaderBoard(Constants.allTimeScore)
                R.id.rbWeekly -> setLeaderBoard(Constants.weeklyScore)
                R.id.rbMonthly -> setLeaderBoard(Constants.monthlyScore)
            }
        }
    }

    private fun setLeaderBoard(type: String) {
        FireBaseClass().getLeaderBoardData(type, object : FireBaseClass.LeaderBoardDataCallback {
            override fun onLeaderBoardDataFetched(leaderBoardModel: LeaderBoardModel?) {
                if (leaderBoardModel == null) {
                    showEmptyLeaderboard()
                    return
                }

                // ✅ Ensure top 3 ranks exist before accessing them
                val rank1 = leaderBoardModel.rank1
                val rank2 = leaderBoardModel.rank2
                val rank3 = leaderBoardModel.rank3

                if (rank1 != null) {
                    binding?.tvRank1Name?.text = rank1.name
                    binding?.tvRank1Points?.text = String.format("%.2f", Base.desiredScore(rank1, type))
                    FireBaseClass().setProfileImage(rank1.image, binding?.ivRank1!!)
                } else showEmptyRank(1)

                if (rank2 != null) {
                    binding?.tvRank2Name?.text = rank2.name
                    binding?.tvRank2Points?.text = String.format("%.2f", Base.desiredScore(rank2, type))
                    FireBaseClass().setProfileImage(rank2.image, binding?.ivRank2!!)
                } else showEmptyRank(2)

                if (rank3 != null) {
                    binding?.tvRank3Name?.text = rank3.name
                    binding?.tvRank3Points?.text = String.format("%.2f", Base.desiredScore(rank3, type))
                    FireBaseClass().setProfileImage(rank3.image, binding?.ivRank3!!)
                } else showEmptyRank(3)

                // ✅ Check if `otherRanks` is empty before setting adapter
                if (!leaderBoardModel.otherRanks.isNullOrEmpty()) {
                    val adapter = LeaderBoardAdapter(leaderBoardModel.otherRanks, type)
                    binding?.leaderBoardRecyclerView?.adapter = adapter
                } else {
                    binding?.leaderBoardRecyclerView?.adapter = null
                }
            }
        })
    }

    private fun showEmptyLeaderboard() {
        binding?.tvRank1Name?.text = "N/A"
        binding?.tvRank1Points?.text = "0.00"
        binding?.tvRank2Name?.text = "N/A"
        binding?.tvRank2Points?.text = "0.00"
        binding?.tvRank3Name?.text = "N/A"
        binding?.tvRank3Points?.text = "0.00"
        binding?.leaderBoardRecyclerView?.adapter = null
    }

    @SuppressLint("SetTextI18n")
    private fun showEmptyRank(rank: Int) {
        when (rank) {
            1 -> {
                binding?.tvRank1Name?.text = "N/A"
                binding?.tvRank1Points?.text = "0.00"
                binding?.ivRank1?.setImageResource(R.drawable.profile_pic)
            }
            2 -> {
                binding?.tvRank2Name?.text = "N/A"
                binding?.tvRank2Points?.text = "0.00"
                binding?.ivRank2?.setImageResource(R.drawable.profile_pic)
            }
            3 -> {
                binding?.tvRank3Name?.text = "N/A"
                binding?.tvRank3Points?.text = "0.00"
                binding?.ivRank3?.setImageResource(R.drawable.profile_pic)
            }
        }
    }
}
