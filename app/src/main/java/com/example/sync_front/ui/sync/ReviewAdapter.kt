package com.example.sync_front.ui.sync

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sync_front.R
import com.example.sync_front.api_server.TranslationManager
import com.example.sync_front.data.model.CheckLanguageRequest
import com.example.sync_front.data.model.Review
import com.example.sync_front.data.model.TranslateRequest
import com.squareup.picasso.Picasso

class ReviewAdapter(private var reviews: List<Review>,
    private val authToken: String) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var trans = false

    class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.profile_img)
        val nameView: TextView = view.findViewById(R.id.username)
        val universityView: TextView = view.findViewById(R.id.userschool)
        val contentView: TextView = view.findViewById(R.id.tv_review)
        val dateView: TextView = view.findViewById(R.id.upload_time)
        val translateButton: LinearLayout = view.findViewById(R.id.translate_btn)
        val translate: TextView = view.findViewById(R.id.translate_text)
        val translateIc: ImageView = view.findViewById(R.id.translate_ic)
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
        Picasso.get()
            .load(review.image)
            .placeholder(R.drawable.img_profile_default)
            .error(R.drawable.img_profile_default)
            .into(holder.imageView)

        holder.translateButton.setOnClickListener {
            if (trans) {
                trans = false
                holder.contentView.text = review.content
                holder.translate.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.gray_50))
                holder.translateIc.setBackgroundResource(R.drawable.ic_trans)
            } else {
                getTranslate(review.content, holder)
            }
        }
    }

    override fun getItemCount() = reviews.size

    fun updateReviews(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    private fun getTranslate(content: String, holder: ReviewViewHolder) {
        sendTranslate(content) { transContent ->
            holder.contentView.text = transContent
            trans = true
            holder.contentView.text = transContent
            holder.translate.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.primary))
            holder.translateIc.setBackgroundResource(R.drawable.ic_trans_primary)
        }
    }

    private fun sendTranslate(text: String, callback: (String) -> Unit) {
        TranslationManager.checkLanguage(authToken!!, CheckLanguageRequest(text)) { response ->
            if (response != null && response.status == 200) {
                val target = if (response.data.langCode == "en") {
                    "ko"
                } else { "en" }

                val request = TranslateRequest(response.data.langCode, target, text)
                TranslationManager.translate(authToken!!, request) { translateResponse ->
                    if (translateResponse != null && translateResponse.status == 200) {
                        val result = translateResponse.data
                        callback(result.translatedText)
                    } else {
                        Log.e(
                            "TranslationError",
                            "Translation response is null or status is not 200"
                        )
                        callback(text)
                    }
                }
            } else {
                Log.e("TranslationError", "CheckLanguage response is null or status is not 200")
                callback(text)
            }
        }
    }
}
