package com.example.kys.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.example.kys.databinding.DialogEditProfileBinding

class EditProfileDialog(
    private val onSubmitClickListener: (String, String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogEditProfileBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditProfileBinding.inflate(LayoutInflater.from(context))

        val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? -> }


        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        binding.editProfileUploadBtn.setOnClickListener {
            getContent.launch("image/*")
        }

        builder.setPositiveButton("Güncelle") { dialog, it ->
            onSubmitClickListener.invoke(
                binding.editProfileUsername.editText?.text.toString(),
                getContent.toString()
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

}