package reza.nejati.omdb.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

abstract class EndlessScrollViewListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun getTotalPageCount(): Int
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}