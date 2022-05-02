package com.smt.imageshared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile_recycler_view.view.*

class ProfileRecyclerViewAdaptor(var postList: ArrayList<Post>) : RecyclerView.Adapter<ProfileRecyclerViewAdaptor.PostVH>() {
    class PostVH (itemView : View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.profile_recycler_view,parent,false)
        return PostVH(view)
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        holder.itemView.user_email.text = postList[position].userEmail
        holder.itemView.user_comment.text = postList[position].userComment
        Picasso.get().load(postList[position].imageUri).into(holder.itemView.recycler_row_imageview)
    }

    override fun getItemCount(): Int {
       return  postList.size
    }
}