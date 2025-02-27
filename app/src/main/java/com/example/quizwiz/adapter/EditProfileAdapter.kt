package com.example.quizwiz.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.quizwiz.R
import com.example.quizwiz.constants.Base
import com.example.quizwiz.fireBase.FireBaseClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.imageview.ShapeableImageView

class EditProfileAdapter(private val context: Context) : BottomSheetDialogFragment() {
    private lateinit var imageView: ShapeableImageView
    private var imageUri: Uri? = null
    private var name: String? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.edit_profile_btm_sheet, container, false)

        imageView = view.findViewById(R.id.editImage)
        val editName: EditText = view.findViewById(R.id.etUserName)
        val saveButton: Button = view.findViewById(R.id.btnSave)

        // ✅ Load existing profile image safely
        val userId = FireBaseClass().getCurrentUserId()
        if (userId.isNotEmpty()) {
            FireBaseClass().setProfileImage("profile_pictures/$userId", imageView)
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.profile_pic))
        }

        imageView.setOnClickListener {
            openGallery()
        }

        saveButton.setOnClickListener {
            name = editName.text.toString().trim()

            if (name.isNullOrEmpty()) {
                Base.showToast(context, "Please enter a valid name")
                return@setOnClickListener
            }

            // ✅ Ensure FireBaseClass update does not crash
            FireBaseClass().updateProfile(name, imageUri, object : FireBaseClass.ProfileUpdateCallback {
                override fun onProfileUpdated(success: Boolean) {
                    if (success) {
                        Base.showToast(context, "Profile updated successfully")
                        dismiss()
                    } else {
                        Base.showToast(context, "Profile update failed. Try again!")
                    }
                }
            })
        }
        return view
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data?.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
}
