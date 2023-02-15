package com.example.kys.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.example.kys.R
import com.example.kys.databinding.DialogEditProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@Suppress("DEPRECATION")
class EditProfileDialog(
    private val onSubmitClickListener: (String, String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogEditProfileBinding
    private lateinit var content: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var database: DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditProfileBinding.inflate(layoutInflater)
        storageReference = Firebase.storage.reference
        database = FirebaseDatabase.getInstance().getReference("Users")
        content = Uri.parse("")

        // Profile Image
        setProfileImage(Firebase.auth.currentUser!!.uid)

        registerGetContentLauncher()
        registerTakePictureLauncher(initTempUri(Firebase.auth.uid!!))


        // Profile Username
        binding.editProfileUsername.editText?.text =
            Editable.Factory.getInstance().newEditable(Firebase.auth.currentUser?.displayName)

        return createBuilder()

    }

    private fun reduceImageSize(uri: Uri): Uri {
        // Creating temp dir
        val tempDir = File(requireContext().filesDir, getString(R.string.temp_images_dir))
        tempDir.mkdir()
        val tempFile = File(tempDir, "temp.jpg")
        val bytes = ByteArrayOutputStream()
        val fos = FileOutputStream(tempFile)

        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        }

        val newBitmap = Bitmap.createScaledBitmap(bitmap, 250, 250, false)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val bitmapData = bytes.toByteArray()
        fos.write(bitmapData)
        fos.flush()
        fos.close()
        return Uri.fromFile(tempFile)
    }

    private fun setProfileImage(userUid: String) {
        storageReference = Firebase.storage.reference
        val imageStorageRef = storageReference.child("images/$userUid.jpg")
        imageStorageRef.getBytes(1024 * 1024 * 1024).addOnSuccessListener {
            val imageBitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            binding.profileImageView.setImageBitmap(imageBitmap)
        }
    }

    private fun showNewProfileImage(uri: Uri?) {
        binding.profileImageView.setImageURI(uri)
    }

    private fun initTempUri(userUid: String): Uri {
        val tempImagesDir = File(
            requireContext().filesDir,
            getString(R.string.temp_images_dir)
        )

        tempImagesDir.mkdir()

        val tempImage = File(
            tempImagesDir,
            "$userUid.jpg"
        )

        return FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.authorities),
            tempImage
        )
    }

    private fun registerGetContentLauncher() {
        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                content = reduceImageSize(uri!!)
                showNewProfileImage(content)
                Log.d("CONTENT URI", content.toString())
            }
        binding.editProfileUploadBtn.setOnClickListener {
            getContent.launch("image/jpeg")
        }
    }

    private fun registerTakePictureLauncher(path: Uri) {
        val resultLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
//            binding.profileImageView.setImageURI(null)
//            binding.profileImageView.setImageURI(path)
            content = reduceImageSize(path)
            Log.d("CONTENT URI", content.toString())
        }

        binding.editProfileCameraBtn.setOnClickListener {
            resultLauncher.launch(path)
        }
    }

    private fun createBuilder(): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        builder.setPositiveButton("Güncelle") { _, _ ->
            onSubmitClickListener.invoke(
                binding.editProfileUsername.editText?.text.toString(),
                content.toString()
            )
        }
        builder.setNegativeButton("İptal") { _, _ -> }

        val dialog = builder.create()

        dialog.show()
        val layoutParameters = WindowManager.LayoutParams()
        layoutParameters.copyFrom(dialog.window?.attributes)
        layoutParameters.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParameters.height = WindowManager.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = layoutParameters
        return dialog
    }
}