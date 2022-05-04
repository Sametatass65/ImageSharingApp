package com.smt.imageshared.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smt.imageshared.R
import com.smt.imageshared.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recyclerview_row.view.*

class RecyclerViewAdapter(var postList : ArrayList<Post>): RecyclerView.Adapter<RecyclerViewAdapter.PostVH>() {
    class PostVH(itemView : View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_row,parent,false)
        return PostVH(view)
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        holder.itemView.user_email.text = postList[position].userEmail
        holder.itemView.user_comment.text = postList[position].userComment
        Picasso.get().load(postList[position].imageUri).into(holder.itemView.recycler_row_imageview)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun UpdateListPost(newListPost : ArrayList<Post>){
        postList.clear()
        postList.addAll(newListPost)
        notifyDataSetChanged()
    }
}