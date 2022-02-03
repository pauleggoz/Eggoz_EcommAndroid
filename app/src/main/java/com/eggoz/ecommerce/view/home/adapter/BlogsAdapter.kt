package com.eggoz.ecommerce.view.home.adapter

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eggoz.ecommerce.R
import com.eggoz.ecommerce.databinding.ItemBlogsBinding
import com.eggoz.ecommerce.network.model.Blogs

class BlogsAdapter : ListAdapter<Blogs.Result, BlogsAdapter.BlogsRecyclerViewHolder>(
    BlogsCallBack()
) {

    class BlogsRecyclerViewHolder(
        private val binding: ItemBlogsBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Blogs.Result) {
            binding.apply {

                itemdata = item

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    txtItemHomeSuggestionSliderTitle2.text= Html.fromHtml(item.description, Html.FROM_HTML_MODE_COMPACT)
                }else{
                    txtItemHomeSuggestionSliderTitle2.text= Html.fromHtml(item.description)
                }

                root.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("url", item.link)
                    Navigation.findNavController(root)
                        .navigate(R.id.action_nav_home_to_nav_web_search, bundle)
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlogsRecyclerViewHolder {
        val binding = ItemBlogsBinding.inflate(LayoutInflater.from(parent.context))
        return BlogsRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogsRecyclerViewHolder, position: Int) {
        val currentArticle = getItem(position)
        holder.bind(currentArticle)
    }


}

class BlogsCallBack : DiffUtil.ItemCallback<Blogs.Result>() {
    override fun areItemsTheSame(oldItem: Blogs.Result, newItem: Blogs.Result): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Blogs.Result, newItem: Blogs.Result): Boolean =
        oldItem.title == newItem.title

}