package com.example.kys.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kys.AddConferenceActivity
import com.example.kys.MainActivity
import com.example.kys.R
import com.example.kys.fragments.CreateConfFragment
import com.example.kys.model.ConferenceListModel
import com.google.api.Context
import kotlinx.android.synthetic.main.recycler_conference_list.view.*

class ConferenceListAdapter(
    conferenceList: List<ConferenceListModel>,
    internal var context: android.content.Context
) : RecyclerView.Adapter<ConferenceListAdapter.ConferenceViewHolder>() {

    internal var conferenceList: List<ConferenceListModel> = ArrayList()

    init {
        this.conferenceList = conferenceList
    }

    inner class ConferenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var conferenceName: TextView = view.homeConfName
        var conferenceTitle: TextView = view.homeConfTitle
        var conferenceDate: TextView = view.homeConfDate
        var conferenceDuration: TextView = view.homeConfDuration
        var conferenceEdit: Button = view.homeConfEditButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConferenceViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recycler_conference_list, parent, false)
        return ConferenceViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ConferenceViewHolder, position: Int) {
        val conferences = conferenceList[position]
        holder.conferenceName.text = conferences.conference_name
        holder.conferenceTitle.text = "Konu: " + conferences.conference_title
        holder.conferenceDate.text ="Tarih: " + conferences.conference_date + " Saat" +conferences.conference_time
        holder.conferenceDuration.text = "Süre: " + conferences.conference_duration

        holder.conferenceEdit.setOnClickListener {
            val i = Intent(context, MainActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", conferences.id)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return conferenceList.size
    }
}