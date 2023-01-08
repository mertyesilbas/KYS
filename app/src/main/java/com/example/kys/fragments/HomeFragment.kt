package com.example.kys.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kys.R
import com.example.kys.adapter.ConferenceListAdapter
import com.example.kys.databinding.FragmentHomeBinding
import com.example.kys.model.ConferenceListModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
//    private var parentLinearLayout: LinearLayout? = null
    lateinit var recycler_conference: RecyclerView
    var conferenceListAdapter: ConferenceListAdapter ?= null
    var conferenceList: List<ConferenceListModel> = ArrayList<ConferenceListModel>()
    var linearLayoutManager: LinearLayoutManager ?= null


    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val user = FirebaseAuth.getInstance()

        recycler_conference = rv_list

        binding.apply {


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
//
//
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

    private fun fetchList() {
        conferenceListAdapter = ConferenceListAdapter(conferenceList,requireContext())
        linearLayoutManager = LinearLayoutManager(requireContext())
        recycler_conference.layoutManager = linearLayoutManager
        recycler_conference.adapter = conferenceListAdapter
        conferenceListAdapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}