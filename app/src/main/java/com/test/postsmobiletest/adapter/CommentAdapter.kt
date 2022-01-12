package com.test.postsmobiletest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.test.postsmobiletest.R
import com.test.postsmobiletest.model.CommentPost
import com.test.postsmobiletest.model.Post

class CommentAdapter(val context: Context) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    var comments = mutableListOf<CommentPost>()

    fun setCommentList(comments: List<CommentPost>) {
        this.comments = comments.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_comment_post, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comments = comments[position]
        holder.name.text = comments.name
        holder.email.text = comments.email
        holder.body.text = comments.body

    }
        override fun getItemCount(): Int {
            return comments.size
        }

        class CommentViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
            val name: TextView = itemView!!.findViewById(R.id.name)
            val email: TextView = itemView!!.findViewById(R.id.email)
            val body: TextView = itemView!!.findViewById(R.id.body)
        }
    }
