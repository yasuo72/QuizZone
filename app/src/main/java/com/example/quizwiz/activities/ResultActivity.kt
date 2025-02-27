package com.example.quizwiz.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizwiz.adapter.QuizSummeryAdapter
import com.example.quizwiz.constants.Constants
import com.example.quizwiz.databinding.ActivityResultBinding
import com.example.quizwiz.fireBase.FireBaseClass
import com.example.quizwiz.models.ResultModel
import com.example.quizwiz.models.UserModel

class ResultActivity : AppCompatActivity() {
    private var binding: ActivityResultBinding? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val resultList: ArrayList<ResultModel> =
            intent.getParcelableArrayListExtra<ResultModel>("resultList") as ArrayList<ResultModel>

        FireBaseClass().updateScore(getFinalScore(resultList))
        FireBaseClass().getUserInfo(object :FireBaseClass.UserInfoCallback{
            override fun onUserInfoFetched(userInfo: UserModel?) {
                if (userInfo!=null)
                    binding?.tvUserPoints?.text = String.format("%.2f",userInfo.allTimeScore)
            }

        })
        FireBaseClass().getUserRank(Constants.allTimeScore,
            object :FireBaseClass.UserRankCallback{
                override fun onUserRankFetched(rank: Int?) {
                    if (rank!=null)
                        binding?.tvUserRanking?.text = rank.toString()
                }

            })

        binding?.rvSummery?.layoutManager = LinearLayoutManager(this)
        val adapter = QuizSummeryAdapter(resultList)
        binding?.rvSummery?.adapter = adapter

        binding?.tvTotalScore?.text = "Total Score: " +
                String.format("%.2f",getFinalScore(resultList))
        binding?.btnHome?.setOnClickListener {
            finish()
        }
    }

    private fun getFinalScore(list: ArrayList<ResultModel>): Double {
        var result = 0.0
        for (i in list)
            result += i.score
        return String.format("%.2f",result).toDouble()
    }
}