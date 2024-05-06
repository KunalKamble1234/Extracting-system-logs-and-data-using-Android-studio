package com.example.mobilelogextract

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LogAdapter(private val entries: List<LogEntry>) : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeView: TextView = view.findViewById(R.id.log_type)
        val contentView: TextView = view.findViewById(R.id.log_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.log_item, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val entry = entries[position]
        holder.typeView.text = entry.type
        holder.contentView.text = entry.content
    }

    override fun getItemCount() = entries.size
}
