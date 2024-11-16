package com.dicoding.picodiploma.loginwithanimation.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dicoding.picodiploma.loginwithanimation.data.DetailStory
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ListItemBinding
import com.dicoding.picodiploma.loginwithanimation.loadImage
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity

class ListStoryAdapter(private val listStory: List<ListStoryItem>) :
    RecyclerView.Adapter<ListStoryAdapter.ListStoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount(): Int = listStory.size

    inner class ListStoryViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem) {
            with(binding as ListItemBinding) {
                ivItemPhoto.loadImage(listStoryItem.photoUrl)

                tvItemName.text = listStoryItem.name
                tvItemDescription.text = listStoryItem.description

                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "photo"),
                            Pair(tvItemName, "name"),
                            Pair(tvItemDescription, "description")
                        )
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(
                        DetailActivity.EXTRA_STORY,
                        DetailStory(
                            listStoryItem.id,
                            listStoryItem.name,
                            listStoryItem.description,
                            listStoryItem.photoUrl
                        )
                    )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}