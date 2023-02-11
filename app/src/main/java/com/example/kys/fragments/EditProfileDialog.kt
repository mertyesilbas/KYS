package com.example.kys.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.kys.R
import com.example.kys.databinding.DialogEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

class EditProfileDialog(
    private val onSubmitClickListener: (String, String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogEditProfileBinding
    private lateinit var content: Uri
    private lateinit var takenPicture: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var database: DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditProfileBinding.inflate(layoutInflater)
        storageReference = Firebase.storage.reference
        database = FirebaseDatabase.getInstance().getReference("Users")
        content = Uri.parse("")
        takenPicture = Uri.parse("")

        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                content = uri!!
                showNewProfileImage(content)
            }
        binding.editProfileUploadBtn.setOnClickListener {
            getContent.launch("image/*")
        }

        registerTakePictureLauncher(initTempUri())

        binding.editProfileUsername.editText?.text =
            Editable.Factory.getInstance().newEditable(Firebase.auth.currentUser?.displayName)

        setProfileImage(Firebase.auth.currentUser!!.uid)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        builder.setPositiveButton("Güncelle") { dialog, it ->
            onSubmitClickListener.invoke(
                binding.editProfileUsername.editText?.text.toString(),
                content.toString()
            )
        }
        builder.setNegativeButton("İptal") { dialog, it -> }

        val dialog = builder.create()

        dialog.show()
        val layoutParameters = WindowManager.LayoutParams()
        layoutParameters.copyFrom(dialog.window?.attributes)
        layoutParameters.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParameters.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = layoutParameters
        return dialog
    }

    private fun setProfileImage(userUid: String) {
        // Get Image Url From Database
        val photoListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val photoUrl = snapshot.getValue()
                Log.d("PHOTO URL", photoUrl.toString())
                if (photoUrl == null) { // If user doesn't have an image show default image
                    storageReference =
                        FirebaseStorage.getInstance()
                            .getReference("images/ic_profile_photo_round.png")
                    Log.d("STORAGE", storageReference.toString())
                    val localFile = File.createTempFile("image", "png")
                    storageReference.getFile(localFile).addOnSuccessListener {
                        binding.profileImageView.setImageBitmap(BitmapFactory.decodeFile(localFile.absolutePath))
                    }
                    binding.profileImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.mipmap.ic_profile_photo
                        )
                    )
                } else { // Show user's image

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.child(userUid).child("photoUrl").addValueEventListener(photoListener)
    }

    private fun showNewProfileImage(uri: Uri?) {
        binding.profileImageView.setImageURI(uri)
    }

    private fun initTempUri(): Uri {
        val tempImagesDir = File(
            requireContext().filesDir,
            getString(R.string.temp_images_dir)
        )

        tempImagesDir.mkdir()

        val tempImage = File(
            tempImagesDir,
            getString(R.string.temp_image)
        )

        return FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.authorities),
            tempImage
        )
    }

    private fun registerTakePictureLauncher(path: Uri) {
        val resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            binding.profileImageView.setImageURI(null)
            binding.profileImageView.setImageURI(path)
        }

        binding.editProfileCameraBtn.setOnClickListener {
            resultLauncher.launch(path)
        }
    }
}