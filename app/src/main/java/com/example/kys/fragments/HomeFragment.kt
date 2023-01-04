package com.example.kys.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.kys.MainActivity
import com.example.kys.R
import com.example.kys.databinding.FragmentHomeBinding
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var parentLinearLayout: LinearLayout? = null

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val user = FirebaseAuth.getInstance()

        binding.apply {
            val userUid = user.currentUser?.uid.toString()
            val queryUsername =
                "SELECT username FROM profile WHERE profile.user_uid = " + "'" + userUid + "'"
            val queryAll =
                "SELECT * FROM conference WHERE conference.profile_id = (SELECT id FROM profile WHERE profile.user_uid = " + "'" + userUid + "'" + ")"


//            if (getProfileName.count.toString() != "0") {
//                homeWelcomeTextView.text = "Hoşgeldin " + getProfileName.getString(0) + "!"
//            }
//
//            if (getIsEmpty.count.toString() == "0") {
//                homeConfCardView.removeAllViews()
//
//                val noConfTextView = MaterialTextView(requireActivity())
//                noConfTextView.text = "Koferans Yok!"
//                noConfTextView.textSize = 20f
//                noConfTextView.setTextColor(Color.WHITE)
//                noConfTextView.gravity = Gravity.CENTER
//                homeConfCardView.addView(noConfTextView)
//
//            } else {
//                homeNestedLinearLayout.removeAllViews()
//
//                var i = 0
//                getIsEmpty.moveToFirst()
//                while (i < 1) {
//
//                    var conferenceName = "Koferans Adı: " +
//                            getIsEmpty.getString(getIsEmpty.getColumnIndex("conference_name"))
//                    var conferenceTitle = "Konferans Konusu: " +
//                            getIsEmpty.getString(getIsEmpty.getColumnIndex("conference_title"))
//                    var conferenceDate = "Konferans Tarihi: " +
//                            getIsEmpty.getString(getIsEmpty.getColumnIndex("conference_date")) + " Saat: " + getIsEmpty.getString(
//                        getIsEmpty.getColumnIndex("conference_time")
//                    )
//
////                    Toast.makeText(
////                        requireActivity(),
////                        getIsEmpty.count.toString(),
////                        Toast.LENGTH_SHORT
////                    ).show()
//                    getIsEmpty.moveToNext()


//                    var cardView = homeConfCardView
//                    cardView.homeConfName.text = conferenceName
//                    cardView.homeConfTitle.text = conferenceTitle
//                    cardView.homeConfDate.text = conferenceDate
//                    cardView.homeConfJoinButton.visibility = View.GONE
//
//                    homeNestedLinearLayout.addView(cardView)
//                    i++
//                }
//            }

//            homeConfCancelButton.setOnClickListener{
//                db.deleteConference()
////                Toast.makeText(requireActivity(), "Konferans Silindi!", Toast.LENGTH_SHORT)
//                val intent = Intent(requireActivity(), MainActivity::class.java)
//                startActivity(intent)
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
