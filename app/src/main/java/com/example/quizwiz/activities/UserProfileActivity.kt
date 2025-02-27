package com.example.quizwiz.activities

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizwiz.adapter.EditProfileAdapter
import com.example.quizwiz.constants.Constants
import com.example.quizwiz.databinding.ActivityUserProfileBinding
import com.example.quizwiz.fireBase.FireBaseClass
import com.example.quizwiz.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserProfileActivity : AppCompatActivity() {
    private var binding:ActivityUserProfileBinding? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        FireBaseClass().getUserInfo(object :FireBaseClass.UserInfoCallback{
            override fun onUserInfoFetched(userInfo: UserModel?) {
                binding?.tvUserPoints?.text = String.format("%.2f",userInfo?.allTimeScore)
                binding?.tvLastGameScore?.text = String.format("%.2f",userInfo?.lastGameScore)
                binding?.tvWeeklyScore?.text = String.format("%.2f",userInfo?.weeklyScore)
                binding?.tvMonthlyScore?.text = String.format("%.2f",userInfo?.monthlyScore)
                binding?.tvEmailId?.text = userInfo?.emailId
                binding?.tvUserName?.text = userInfo?.name

                FireBaseClass().setProfileImage(userInfo?.image,binding?.userProfilePic!!)
            }

        })

        updateScoreView(Constants.allTimeScore,binding?.tvUserRanking!!)
        updateScoreView(Constants.weeklyScore,binding?.tvWeeklyRank!!)
        updateScoreView(Constants.monthlyScore,binding?.tvMonthlyRank!!)

        auth = Firebase.auth
        binding?.cvSignOut?.setOnClickListener {
            if (auth.currentUser!= null)
            {
                auth.signOut()
                val intent = Intent(this,GetStartedActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding?.cvEditProfile?.setOnClickListener {
            val bottomSheetFragment = EditProfileAdapter(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun updateScoreView(type:String,textView: TextView){
        FireBaseClass().getUserRank(type,
            object :FireBaseClass.UserRankCallback{
                override fun onUserRankFetched(rank: Int?) {
                    if (rank!=null)
                        textView.text = rank.toString()
                }

            })
    }
}