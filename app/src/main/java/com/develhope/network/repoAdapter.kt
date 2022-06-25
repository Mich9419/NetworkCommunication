package com.develhope.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.github.ui.Repo


class RepoViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val repoName: TextView

    init {
        repoName = view.findViewById(R.id.action_settings)
    }
}

abstract class RepoAdapter(val repoResults: List<Repo>): RecyclerView.Adapter<RepoViewHolder>() {
    abstract val name: CharSequence?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val repoView = LayoutInflater.from(parent.context).inflate(R.layout.repolistitem, parent, false)
        return RepoViewHolder(repoView)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.repoName.text = repoResults[position].name
    }
}

