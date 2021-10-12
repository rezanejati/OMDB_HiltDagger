package reza.nejati.omdb.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.bakhtiyor.gradients.Gradients
import reza.nejati.omdb.utils.observe
import reza.nejati.omdb.utils.shareText
import com.shreyaspatil.MaterialDialog.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import reza.nejati.omdb.R
import reza.nejati.omdb.data.model.detail.DetailResponse
import reza.nejati.omdb.data.remote.State
import reza.nejati.omdb.databinding.ActivityMovieDetailsBinding
import reza.nejati.omdb.ui.base.BaseActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MovieDetailsActivity : BaseActivity<MovieDetailViewModel, ActivityMovieDetailsBinding>() {

    @Inject
    lateinit var viewModelFactory: MovieDetailViewModel.MovieDetailViewModelFactory

    override val mViewModel: MovieDetailViewModel by viewModels {
        val movieId = intent.extras?.getString(MOVIE_ID)
            ?: throw IllegalArgumentException("`movie Id` must be non-null")

        MovieDetailViewModel.provideFactory(viewModelFactory, movieId)
    }

    override fun getViewBinding(): ActivityMovieDetailsBinding =
        ActivityMovieDetailsBinding.inflate(layoutInflater)


    override fun onCreated(savedInstanceState: Bundle?) {
        setupActionBar()
        setupBinding()
        observeData()
        setupView()
        mViewModel.getMovieDetail()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            R.id.action_share -> {
                share()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeData() {
        with(mViewModel) {
            observe(apiResponse, ::renderApiData)
        }
    }

    private fun renderApiData(state: State<DetailResponse?>?) {
        when (state) {
            is State.Loading -> {
                showLoading(true)
            }
            is State.Success -> {
                mViewModel.setDetailMovie(state.data)
                showLoading(false)
            }
            is State.Error -> {
                showLoading(false)
                MaterialDialog.Builder(this)
                    .setTitle(getString(R.string.error))
                    .setMessage(getString(R.string.netwotk_error))
                    .setPositiveButton(getString(R.string.ok)) { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        super.onBackPressed()
                    }
                    .build()
                    .show()
            }
        }

    }

    private fun setupView() {
        mViewBinding.rootView.background = Gradients.find(Gradients.names()[intent.extras?.getInt(COLOR_ID) ?: 0])
        mViewBinding.imageView.loadImage(intent.extras?.getString(IMAGE_URL))
    }


    private fun setupBinding() {
        mViewBinding.lifecycleOwner = this
        mViewBinding.viewModel = mViewModel
    }

    private fun setupActionBar() {
        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun share() {
        val movie = mViewBinding.detailContent
        val shareMsg = movie.movieTitle.text.toString() + "\n" + movie.movieDescription.text.toString()
        shareText(shareMsg)
    }

    companion object {
        private const val MOVIE_ID = "movieId"
        private const val COLOR_ID = "colorId"
        private const val IMAGE_URL = "imageURL"
        fun getStartIntent(
            context: Context,
            omdbId: String,
            colorId: Int,
            imageURL: String
        ) = Intent(context, MovieDetailsActivity::class.java).apply {
            putExtra(MOVIE_ID, omdbId)
            putExtra(COLOR_ID, colorId)
            putExtra(IMAGE_URL, imageURL)
        }
    }

    override fun onNetworkChanges(isConnected: Boolean) {

    }
}
