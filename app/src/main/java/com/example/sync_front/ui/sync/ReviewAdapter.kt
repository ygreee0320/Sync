package com.example.sync_front.ui.sync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.data.model.Review
import com.squareup.picasso.Picasso

class ReviewAdapter(private var reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.profile_img)
        val nameView: TextView = view.findViewById(R.id.username)
        val universityView: TextView = view.findViewById(R.id.userschool)
        val contentView: TextView = view.findViewById(R.id.tv_review)
        val dateView: TextView = view.findViewById(R.id.upload_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.nameView.text = review.name
        holder.universityView.text = review.university
        holder.contentView.text = review.content
        holder.dateView.text = review.date
        Picasso.get().load(review.image).into(holder.imageView)
    }

    override fun getItemCount() = reviews.size

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}
