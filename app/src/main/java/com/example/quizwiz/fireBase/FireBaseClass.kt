package com.example.quizwiz.fireBase

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.quizwiz.constants.Constants
import com.example.quizwiz.models.LeaderBoardModel
import com.example.quizwiz.models.UserModel
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.tasks.Task

class FireBaseClass {
    private val mFireStore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val TAG = "FirebaseClass"

    fun registerUser(userInfo: UserModel) {
        val userId = getCurrentUserId()
        if (userId.isNotEmpty()) {
            mFireStore.collection(Constants.user)
                .document(userId)
                .set(userInfo, SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "User registered successfully.") }
                .addOnFailureListener { Log.e(TAG, "User registration failed: ${it.message}") }
        } else {
            Log.e(TAG, "User ID is empty. Cannot register user.")
        }
    }

    fun getUserInfo(callback: UserInfoCallback) {
        val userId = getCurrentUserId()
        if (userId.isNotEmpty()) {
            mFireStore.collection(Constants.user).document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val userInfo = documentSnapshot.toObject(UserModel::class.java)
                    callback.onUserInfoFetched(userInfo)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to fetch user info: ${e.message}")
                    callback.onUserInfoFetched(null)
                }
        } else {
            Log.e(TAG, "User ID is empty. Cannot fetch user info.")
            callback.onUserInfoFetched(null)
        }
    }

    fun updateProfile(name: String?, imgUri: Uri?, callback: ProfileUpdateCallback) {
        val userId = getCurrentUserId()
        if (userId.isEmpty()) {
            Log.e(TAG, "User ID is empty. Cannot update profile.")
            callback.onProfileUpdated(false)
            return
        }

        val updates = mutableMapOf<String, Any>()
        name?.takeIf { it.isNotEmpty() }?.let { updates["name"] = it }

        val userDocument = mFireStore.collection(Constants.user).document(userId)
        userDocument.update(updates).addOnSuccessListener {
            Log.d(TAG, "Profile updated successfully (without image).")
            imgUri?.let { uri ->
                uploadImage(uri) { success -> callback.onProfileUpdated(success) }
            } ?: callback.onProfileUpdated(true)
        }.addOnFailureListener {
            Log.e(TAG, "Failed to update profile: ${it.message}")
            callback.onProfileUpdated(false)
        }
    }

    fun setProfileImage(imageRef: String?, view: ShapeableImageView) {
        imageRef?.takeIf { it.isNotEmpty() }?.let { ref ->
            val storageRef = storage.reference.child(ref)
            storageRef.getBytes(1024 * 1024).addOnSuccessListener { byteArray ->
                view.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
            }.addOnFailureListener {
                Log.e(TAG, "Failed to load profile image: ${it.message}")
            }
        }
    }

    private fun uploadImage(imgUri: Uri, callback: (Boolean) -> Unit) {
        val userId = getCurrentUserId()
        if (userId.isEmpty()) {
            Log.e(TAG, "User ID is empty. Cannot upload image.")
            callback(false)
            return
        }

        val storageRef = storage.reference.child("profile_pictures/$userId")
        storageRef.putFile(imgUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                mFireStore.collection(Constants.user).document(userId)
                    .update("image", downloadUri.toString())
                    .addOnSuccessListener {
                        Log.d(TAG, "Profile image updated successfully.")
                        callback(true)
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "Failed to update image URL in Firestore: ${it.message}")
                        callback(false)
                    }
            }
        }.addOnFailureListener {
            Log.e(TAG, "Image upload failed: ${it.message}")
            callback(false)
        }
    }

    fun updateScore(newScore: Double) {
        val userId = getCurrentUserId()
        if (userId.isEmpty()) {
            Log.e(TAG, "User ID is empty. Cannot update score.")
            return
        }

        getUserInfo(object : UserInfoCallback {
            override fun onUserInfoFetched(userInfo: UserModel?) {
                userInfo?.let {
                    val updatedScores = mapOf(
                        Constants.allTimeScore to (it.allTimeScore + newScore),
                        Constants.weeklyScore to (it.weeklyScore + newScore),
                        Constants.monthlyScore to (it.monthlyScore + newScore),
                        Constants.lastGameScore to newScore
                    )

                    mFireStore.collection(Constants.user).document(userId)
                        .update(updatedScores)
                        .addOnSuccessListener { Log.d(TAG, "Score updated successfully") }
                        .addOnFailureListener { Log.e(TAG, "Score update failed: ${it.message}") }
                } ?: Log.e(TAG, "User info is null. Cannot update score.")
            }
        })
    }

    fun getUserRank(type: String, callback: UserRankCallback) {
        val userId = getCurrentUserId()
        if (userId.isEmpty()) {
            Log.e(TAG, "User ID is empty. Cannot fetch user rank.")
            callback.onUserRankFetched(null)
            return
        }

        mFireStore.collection(Constants.user)
            .orderBy(type, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val rank = result.documents.indexOfFirst { it.id == userId } + 1
                callback.onUserRankFetched(if (rank > 0) rank else null)
            }.addOnFailureListener {
                Log.e(TAG, "Failed to fetch user rank: ${it.message}")
                callback.onUserRankFetched(null)
            }
    }

    fun getLeaderBoardData(type: String, callback: LeaderBoardDataCallback) {
        mFireStore.collection(Constants.user)
            .orderBy(type, Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(UserModel::class.java)
                callback.onLeaderBoardDataFetched(
                    LeaderBoardModel(
                        users.getOrNull(0),
                        users.getOrNull(1),
                        users.getOrNull(2),
                        users.drop(3)
                    )
                )
            }.addOnFailureListener {
                Log.e(TAG, "Failed to fetch leaderboard data: ${it.message}")
                callback.onLeaderBoardDataFetched(null)
            }
    }

    fun doesDocumentExist(documentId: String): Task<Boolean> {
        return mFireStore.collection(Constants.user).document(documentId)
            .get()
            .continueWith { task -> task.isSuccessful && (task.result?.exists() ?: false) }
    }

    fun getCurrentUserId(): String {
        return Firebase.auth.currentUser?.uid ?: ""
    }

    interface UserInfoCallback {
        fun onUserInfoFetched(userInfo: UserModel?)
    }

    interface UserRankCallback {
        fun onUserRankFetched(rank: Int?)
    }

    interface LeaderBoardDataCallback {
        fun onLeaderBoardDataFetched(leaderBoardModel: LeaderBoardModel?)
    }

    interface ProfileUpdateCallback {
        fun onProfileUpdated(success: Boolean)
    }
}
