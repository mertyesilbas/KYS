package com.example.kys.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kys.MainActivity
import com.example.kys.R
import com.example.kys.data.Conference
import com.example.kys.databinding.FragmentCreateConfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_create_conf.*
import kotlinx.android.synthetic.main.fragment_create_conf.view.*
import java.text.SimpleDateFormat
import java.util.*


class CreateConfFragment : Fragment(R.layout.fragment_create_conf) {
    private var _binding: FragmentCreateConfBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference

    override fun onResume() {
        super.onResume()
        binding.apply {
            val durations =
                resources.getStringArray(R.array.durations) // Süreleri arrray olarak getirdik.
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.conf_duration, durations)
            binding.confDurationTextView.setAdapter(arrayAdapter)
        }
        binding.apply {
            confRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == R.id.confRadioButton2) {
                    confOnlineEditText.isEnabled = false
                    confAddressEditText.isEnabled = true
                }
                if (checkedId == R.id.confRadioButton1) {
                    confAddressEditText.isEnabled = false
                    confOnlineEditText.isEnabled = true
                }
                if (checkedId == R.id.confRadioButton3) {
                    confAddressEditText.isEnabled = true
                    confOnlineEditText.isEnabled = true
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateConfBinding.bind(view)

        binding.apply {
            confDateTextInputLayout.setEndIconOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        confDateTextInputLayout.confDateEditText.setText(date)
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }

        binding.apply {
            confTimeTextInputLayout.setEndIconOnClickListener {
                val timePickerFragment = TimePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY1",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY1") {
                        val time = bundle.getString("SELECTED_TIME")
                        confTimeTextInputLayout.confTimeEditText.setText(time)
                    }
                }

                timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
            }
        }

        binding.apply {
            confSendButton.setOnClickListener {
                // Variables from createConferenceForm form
                val conferenceName = confNameTextInputLayout.editText?.text.toString()
                val conferenceTitle = confTopicTextInputLayout.editText?.text.toString()
                val mail = confMailTextInputLayout.editText?.text.toString()
                val conferenceDate = confDateTextInputLayout.editText?.text.toString()
                val conferenceTime = confTimeTextInputLayout.editText?.text.toString()
                val conferenceDuration = confDurationTextInputLayout.editText?.text.toString()
                var estimatedCallers: String = "0"
                if (confEstTextInputLayout.editText?.text.toString().trim().length > 0) {
                    estimatedCallers = confEstTextInputLayout.editText?.text.toString()
                }
                val conferenceType =
                    confRadioGroup.resources.getResourceEntryName(confRadioGroup.checkedRadioButtonId)
                        .toString()
                var conferenceType1: String = "Online"
                var onlineLink: String = ""
                var address: String = ""
                if (conferenceType == "confRadioButton1") {
                    conferenceType1 = "Online"
                    onlineLink = confOnlineTextInputLayout.editText?.text.toString()
                    address = ""
                }
                if (conferenceType == "confRadioButton2") {
                    conferenceType1 = "Yüz Yüze"
                    onlineLink = ""
                    address = confAddressTextInputLayout.editText?.text.toString()
                }
                if (conferenceType == "confRadioButton3") {
                    conferenceType1 = "Karma"
                    onlineLink = confOnlineTextInputLayout.editText?.text.toString()
                    address = confAddressTextInputLayout.editText?.text.toString()
                }

                val sdfDate = SimpleDateFormat("dd/M/yyyy", Locale("tr"))
                val sdfTime = SimpleDateFormat("HH:mm:ss", Locale("tr"))
                val createDate = sdfDate.format(Date())
                val createTime = sdfTime.format(Date())

                // Database


                if ((conferenceName.trim().length > 0) && (conferenceTitle.trim().length > 0) && (mail.trim().length > 0) && (conferenceDate.trim().length > 0) && (conferenceTime.trim().length > 0) && (conferenceDuration.trim().length > 0) && ((onlineLink.trim().length > 0) || (address.trim().length > 0))
                ) {
                    // Database'e konferansın yazılacağı alan
                    database = FirebaseDatabase.getInstance().getReference("Conferences")
                    val Conference = Conference(
                        conferenceName,
                        conferenceTitle,
                        mail,
                        conferenceDate,
                        conferenceTime,
                        conferenceDuration,
                        estimatedCallers,
                        conferenceType1,
                        onlineLink,
                        address,
                        createDate,
                        createTime
                    )
                    database.child(conferenceName).setValue(Conference).addOnSuccessListener {

                        Toast.makeText(
                            requireActivity(),
                            "Koferans Başarıyla Oluşturuldu!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener{
                        Toast.makeText(
                            requireActivity(),
                            "Konferans Oluşturulamadı",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    startActivity(
                        Intent(requireActivity(), MainActivity::class.java)
                    )
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Lütfen Tüm Alanları Doldurunuz!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}