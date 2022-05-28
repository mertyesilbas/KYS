package com.example.kys.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kys.AddConferenceActivity
import com.example.kys.R
import com.example.kys.model.ConferenceListModel
import com.google.api.Context
import kotlinx.android.synthetic.main.recycler_conference_list.view.*

class ConferenceListAdapter(
    conferenceList: List<ConferenceListModel>,
    internal var context: android.content.Context
) : RecyclerView.Adapter<ConferenceListAdapter.ConferenceViewHolder>() {

    internal var conferenceList : List<ConferenceListModel> = ArrayList()
    init {
        this.conferenceList = conferenceList
    }

    inner class ConferenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var conferenceName: TextView = view.homeConfName
        var conferenceTitle: TextView = view.homeConfTitle
        var conferenceDate: TextView = view.homeConfDate
        var conferenceEdit: Button = view.homeConfEditButton
        var conferenceDelete: Button = view.homeConfCancelButton
        var conferenceDetails: Button = view.homeConfDetailsButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConferenceViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_conference_list, parent,false)
        return ConferenceViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ConferenceViewHolder, position: Int) {
        val conferences = conferenceList[position]
        holder.conferenceName.text = conferences.conference_name
        holder.conferenceTitle.text = conferences.conference_title
        holder.conferenceDate.text = conferences.conference_date + conferences.conference_time

        holder.conferenceEdit.setOnClickListener{
            val i = Intent(context, AddConferenceActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", conferences.id)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return conferenceList.size
    }
}