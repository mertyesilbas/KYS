package com.example.kys.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.ActivityInfo.WindowLayout
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.kys.databinding.DialogEditProfileBinding
import java.util.zip.Inflater

class EditProfileDialog(
    private val onSubmitClickListener: (String) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogEditProfileBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogEditProfileBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        builder.setPositiveButton("Güncelle") { dialog, it ->
            onSubmitClickListener.invoke(binding.editProfileUsername.editText?.text.toString())
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