package reza.nejati.omdb.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import reza.nejati.omdb.data.model.search.SearchWords
import reza.nejati.omdb.databinding.ItemSearchTextBinding
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 *
 * Adapter for search words
 */

class SearchWordsAdapter
@Inject constructor() : RecyclerView.Adapter<SearchWordsAdapter.ViewHolder>() {

    internal var collection: List<SearchWords> by Delegates.observable(emptyList()) { _, old, new ->
        notifyItemInserted(old.size)
    }

    internal var clickListener: (SearchWords) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemSearchTextBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = collection.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(collection.get(position), clickListener)

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(private val binding: ItemSearchTextBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(searchWords: SearchWords, onItemClicked: (SearchWords) -> Unit) {
            binding.item = searchWords
            binding.root.setOnClickListener {
                onItemClicked(searchWords)
            }

        }
    }
}
