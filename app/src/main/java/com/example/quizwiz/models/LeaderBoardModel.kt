package com.example.quizwiz.models

data class LeaderBoardModel(
    val rank1:UserModel?,
    val rank2:UserModel?,
    val rank3:UserModel?,
    val otherRanks:List<UserModel?>
)
