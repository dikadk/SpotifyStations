package com.coolcodezone.carousel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StationsRecyclerAdapter : RecyclerView.Adapter<StationsRecyclerAdapter.StationViewHolder>() {

    private var recyclerView: RecyclerView? = null

    var stationsList = mutableListOf(
        Station("Jungle", Color.parseColor("#F06292"), ""),
        Station("Elder Island", Color.parseColor("#BA68C8"), ""),
        Station("Chemical Brothers", Color.parseColor("#4FC3F7"), ""),
        Station("The Beatles", Color.parseColor("#2E7D32"), ""),
        Station("Elder Island", Color.parseColor("#BF360C"), ""),
        Station("The Foals", Color.parseColor("#4FC3F7"), ""),
        Station("Franc Moody", Color.parseColor("#F06292"), ""),
        Station("Jungle & more", Color.parseColor("#311B92"), ""),
        Station("Childish Gambino", Color.parseColor("#004D40"), ""),
        Station("Savages", Color.parseColor("#D84315"), ""),
        Station("Elder Island", Color.parseColor("#BA68C8"), ""),
        Station("The Foals", Color.parseColor("#4FC3F7"), ""),
        Station("Jungle", Color.parseColor("#F06292"), ""),
        Station("Elder Island", Color.parseColor("#BA68C8"), ""),
        Station("The Foals", Color.parseColor("#4FC3F7"), "")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val stationView =
            LayoutInflater.from(parent.context).inflate(R.layout.station_item, parent, false)
        return StationViewHolder(stationView)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    override fun getItemCount(): Int = stationsList.size


    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.buildData(stationsList[position])
    }

    fun swapData(newStationsList: MutableList<Station>) {
        stationsList = newStationsList
        notifyDataSetChanged()
        //todo DiffUtil
    }


    inner class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val stationNameTextView =
            itemView.findViewById(R.id.stationNameTextView) as TextView

        fun buildData(station: Station) {
            station.run {
                stationNameTextView.text = name
            }
        }
    }

}
