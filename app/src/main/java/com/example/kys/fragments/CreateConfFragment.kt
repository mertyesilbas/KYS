package com.example.kys.fragments


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import com.example.kys.DBHelper
import com.example.kys.MainActivity
import com.example.kys.R
import com.example.kys.databinding.FragmentCreateConfBinding
import com.example.kys.model.ConferenceListModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_create_conf.view.*
import java.text.SimpleDateFormat
import java.util.*


class CreateConfFragment : Fragment(R.layout.fragment_create_conf) {
    private var _binding: FragmentCreateConfBinding? = null
    private val binding get() = _binding!!
    lateinit var conferenceSave: Button
    lateinit var conferenceDelete: Button
    lateinit var conferenceName: EditText
    lateinit var conferenceTitle: EditText
    lateinit var mail: EditText
    lateinit var conferenceDate: EditText
    lateinit var conferenceTime: EditText
    lateinit var conferenceDuration: EditText
    lateinit var estimatedCallers: EditText
    lateinit var conferenceType: RadioGroup
    lateinit var onlineLink: EditText
    lateinit var address: EditText
    var dbHelper: DBHelper? = null
    var isEditMode: Boolean = false


    override fun onResume() {
        super.onResume()
        binding.apply {
            val durations = resources.getStringArray(R.array.durations)
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

        val user = FirebaseAuth.getInstance()

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
            conferenceSave = confSendButton
            conferenceDelete = confDeleteButton
            conferenceName = confNameTextInputLayout.editText!!
            conferenceTitle = confTopicTextInputLayout.editText!!
            mail = confMailTextInputLayout.editText!!
            conferenceDate = confDateTextInputLayout.editText!!
            conferenceTime = confTimeTextInputLayout.editText!!
            conferenceDuration = confDurationTextInputLayout.editText!!
            estimatedCallers = confEstTextInputLayout.editText!!
            conferenceType = confRadioGroup
            onlineLink = confOnlineTextInputLayout.editText!!
            address = confAddressTextInputLayout.editText!!

            dbHelper = DBHelper(requireContext(), null)

            val mode = arguments?.getString("Mode")

            if ((arguments != null) && (mode == "E")) {
                    // Update Data
                    isEditMode = true
                    conferenceSave.text = "Güncelle"
                    conferenceDelete.visibility = View.VISIBLE
                    val conferences: ConferenceListModel =
                        dbHelper!!.getConference(arguments?.getString("Id")!!.toInt())
                    conferenceName.setText(conferences.conference_name)
                    conferenceTitle.setText(conferences.conference_title)
                    mail.setText(conferences.mail)
                    conferenceDate.setText(conferences.conference_date)
                    conferenceTime.setText(conferences.conference_time)
                    conferenceDuration.setText(conferences.conference_duration)
                    estimatedCallers.setText(conferences.estimated_callers)
                    onlineLink.setText(conferences.online_link)
                    address.setText(conferences.conference_address)

                } else {
                // Insert Data
                isEditMode = false
                conferenceSave.text = "Kaydet"
                conferenceDelete.visibility = View.GONE
            }

            conferenceSave.setOnClickListener {
                var success: Boolean = false
                val conferences: ConferenceListModel = ConferenceListModel()
                if (isEditMode) {
                    // Update
                    conferences.id = requireActivity().intent.getIntExtra("Id", 0)
                    conferences.conference_name = conferenceName.text.toString()
                    conferences.conference_title = conferenceTitle.text.toString()
                    conferences.mail = mail.text.toString()
                    conferences.conference_date = conferenceDate.text.toString()
                    conferences.conference_time = conferenceTime.text.toString()
                    conferences.conference_duration = conferenceDuration.text.toString()
                    conferences.estimated_callers = estimatedCallers.text.toString()
                    conferences.conference_type =
                        conferenceType.resources.getResourceEntryName(confRadioGroup.checkedRadioButtonId)
                            .toString()
                    conferences.online_link = onlineLink.text.toString()
                    conferences.conference_address = address.text.toString()

                    if (conferences.conference_type == "confRadioButton1") {
                        conferences.conference_type = "1"
                    }
                    if (conferences.conference_type == "confRadioButton2") {
                        conferences.conference_type = "2"
                    }
                    if (conferences.conference_type == "confRadioButton3") {
                        conferences.conference_type = "3"
                    }

                    val sdfDate = SimpleDateFormat("dd/M/yyyy", Locale("tr"))
                    val sdfTime = SimpleDateFormat("HH:mm:ss", Locale("tr"))
                    conferences.create_date = sdfDate.toString()
                    conferences.create_time = sdfTime.toString()

                    success = dbHelper?.updateConference(conferences) as Boolean
                } else {
                    // New Data
                    conferences.conference_name = conferenceName.text.toString()
                    conferences.conference_title = conferenceTitle.text.toString()
                    conferences.mail = mail.text.toString()
                    conferences.conference_date = conferenceDate.text.toString()
                    conferences.conference_time = conferenceTime.text.toString()
                    conferences.conference_duration = conferenceDuration.text.toString()
                    conferences.estimated_callers = estimatedCallers.text.toString()
                    conferences.conference_type =
                        conferenceType.resources.getResourceEntryName(confRadioGroup.checkedRadioButtonId)
                            .toString()
                    conferences.online_link = onlineLink.text.toString()
                    conferences.conference_address = address.text.toString()

                    if (conferences.conference_type == "confRadioButton1") {
                        conferences.conference_type = "1"
                    }
                    if (conferences.conference_type == "confRadioButton2") {
                        conferences.conference_type = "2"
                    }
                    if (conferences.conference_type == "confRadioButton3") {
                        conferences.conference_type = "3"
                    }

                    val sdfDate = SimpleDateFormat("dd/M/yyyy", Locale("tr"))
                    val sdfTime = SimpleDateFormat("HH:mm:ss", Locale("tr"))
                    conferences.create_date = sdfDate.toString()
                    conferences.create_time = sdfTime.toString()

                    success = dbHelper?.addConference(conferences) as Boolean
                }
                if (success) {
                    val i = Intent(requireContext(), HomeFragment::class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(requireContext(), "Konferans eklenenemedi!", Toast.LENGTH_SHORT)
                }
            }

            conferenceDelete.setOnClickListener {
                val dialog = AlertDialog.Builder(requireContext()).setTitle("Konferansı Sil")
                    .setMessage("Bu konfreansı silmek istiyor musunuz?")
                    .setPositiveButton("Evet", { dialog, i ->
                        val success = dbHelper?.deleteConference(
                                arguments?.getString("Id")!!.toInt()
//                            requireActivity().intent.getIntExtra(
//                                "Id",
//                                0
//                            )
                        ) as Boolean
                        if (success) {
                            requireActivity().finish()
                        }
                        dialog.dismiss()
                    })
                    .setNegativeButton("Hayır", { dialog, i ->
                        dialog.dismiss()
                    })
                dialog.show()
            }

            confSendButton.setOnClickListener{
                // Get Current User id
                val db = DBHelper(requireActivity(), null)

                val dbQuery = db.readableDatabase
                val userUid = user.currentUser?.uid.toString()
                val query = "SELECT id FROM profile WHERE profile.user_uid = " + "'" + userUid + "'"

                val getProfileId = dbQuery.rawQuery(query, null)
                getProfileId.moveToFirst()

                val profileId = getProfileId.getInt(0)
                db.close()

                val conferences: ConferenceListModel = ConferenceListModel()
                conferences.profile_id = profileId
                conferences.conference_name = conferenceName.text.toString()
                conferences.conference_title = conferenceTitle.text.toString()
                conferences.mail = mail.text.toString()
                conferences.conference_date = conferenceDate.text.toString()
                conferences.conference_time = conferenceTime.text.toString()
                conferences.conference_duration = conferenceDuration.text.toString()
                conferences.estimated_callers = estimatedCallers.text.toString()
                conferences.conference_type =
                    conferenceType.resources.getResourceEntryName(confRadioGroup.checkedRadioButtonId)
                        .toString()
                conferences.online_link = onlineLink.text.toString()
                conferences.conference_address = address.text.toString()

                if (conferences.conference_type == "confRadioButton1") {
                    conferences.conference_type = "1"
                }
                if (conferences.conference_type == "confRadioButton2") {
                    conferences.conference_type = "2"
                }
                if (conferences.conference_type == "confRadioButton3") {
                    conferences.conference_type = "3"
                }

                val sdfDate = SimpleDateFormat("dd/M/yyyy", Locale("tr"))
                val sdfTime = SimpleDateFormat("HH:mm:ss", Locale("tr"))
                conferences.create_date = sdfDate.toString()
                conferences.create_time = sdfTime.toString()

                dbHelper?.addConference(conferences)

                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
        }

//        binding.apply {
////            confSendButton.setOnClickListener {
////                // Variables from createConferenceForm form
////                val conferenceName = confNameTextInputLayout.editText?.text.toString()
////                val conferenceTitle = confTopicTextInputLayout.editText?.text.toString()
////                val mail = confMailTextInputLayout.editText?.text.toString()
////                val conferenceDate = confDateTextInputLayout.editText?.text.toString()
////                val conferenceTime = confTimeTextInputLayout.editText?.text.toString()
////                val conferenceDuration = confDurationTextInputLayout.editText?.text.toString()
////                var estimatedCallers: Int = 0
////                if (confEstTextInputLayout.editText?.text.toString().trim().length > 0) {
////                    estimatedCallers = confEstTextInputLayout.editText?.text.toString().toInt()
////                }
////                val conferenceType =
////                    confRadioGroup.resources.getResourceEntryName(confRadioGroup.checkedRadioButtonId)
////                        .toString()
////                var conferenceType1: Int = 0
////                var onlineLink: String = ""
////                var address: String = ""
////                if (conferenceType == "confRadioButton1") {
////                    conferenceType1 = 1
////                    onlineLink = confOnlineTextInputLayout.editText?.text.toString()
////                    address = ""
////                }
////                if (conferenceType == "confRadioButton2") {
////                    conferenceType1 = 2
////                    onlineLink = ""
////                    address = confAddressTextInputLayout.editText?.text.toString()
////                }
////                if (conferenceType == "confRadioButton3") {
////                    conferenceType1 = 3
////                    onlineLink = confOnlineTextInputLayout.editText?.text.toString()
////                    address = confAddressTextInputLayout.editText?.text.toString()
////                }
////
////                val sdfDate = SimpleDateFormat("dd/M/yyyy", Locale("tr"))
////                val sdfTime = SimpleDateFormat("HH:mm:ss", Locale("tr"))
////                val createDate = sdfDate.format(Date())
////                val createTime = sdfTime.format(Date())
////
////                // Database
////                val db = DBHelper(requireActivity(), null)
////
////                val dbQuery = db.readableDatabase
////                val userUid = user.currentUser?.uid.toString()
////                val query = "SELECT id FROM profile WHERE profile.user_uid = " + "'" + userUid + "'"
////
////                val getProfileId = dbQuery.rawQuery(query, null)
////                getProfileId.moveToFirst()
////
////                val profileId = getProfileId.getInt(0)
////
////
//////                if ((conferenceName.trim().length > 0) && (conferenceTitle.trim().length > 0) && (mail.trim().length > 0) && (conferenceDate.trim().length > 0) && (conferenceTime.trim().length > 0) && (conferenceDuration.trim().length>0) && ((onlineLink.trim().length>0) || (address.trim().length>0))
//////                ) {
//////                    db.addConference(
//////                        profileId,
//////                        conferenceName,
//////                        conferenceTitle,
//////                        mail,
//////                        conferenceDate,
//////                        conferenceTime,
//////                        conferenceDuration,
//////                        estimatedCallers,
//////                        conferenceType1,
//////                        onlineLink,
//////                        address,
//////                        createDate,
//////                        createTime
//////                    )
//////                    Toast.makeText(
//////                        requireActivity(),
//////                        "Koferans Başarıyla Oluşturuldu!",
//////                        Toast.LENGTH_SHORT
//////                    ).show()
//////                    startActivity(Intent(requireActivity(), MainActivity::class.java))
//////                } else {
//////                    Toast.makeText(
//////                        requireActivity(),
//////                        "Lütfen Tüm Alanları Doldurunuz!",
//////                        Toast.LENGTH_SHORT
//////                    ).show()
//////
//////                }
////
////            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}