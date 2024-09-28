package com.stopwatch.SCT

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class LapTimesAdapter(private val context: Context, private val lapTimes: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return lapTimes.size
    }

    override fun getItem(position: Int): Any {
        return lapTimes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.lap_time_item, parent, false)
        val lapNumberTextView: TextView = view.findViewById(R.id.lap_number_text_view)
        val lapTimeTextView: TextView = view.findViewById(R.id.lap_time_text_view)

        val lapData = lapTimes[position]
        val lapParts = lapData.split(" | ")

        if (lapParts.size == 2) {
            val lapNumber = lapParts[0]
            val lapTime = lapParts[1]
            lapNumberTextView.text = lapNumber
            lapTimeTextView.text = lapTime
        }

        return view
    }
}
