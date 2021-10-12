package reza.nejati.omdb.utils

import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import coil.load
import reza.nejati.omdb.R
import reza.nejati.omdb.def.Const

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun ImageView.loadImage(uri: String?) {
    load(uri) {
        placeholder(R.drawable.ic_photo)
        error(R.drawable.ic_broken_image)
    }
}

fun AppCompatActivity.shareText(text: String) {
    val intent = ShareCompat.IntentBuilder.from(this)
        .setType(Const.SHARE_TYPE)
        .setText(text)
        .intent
    startActivity(Intent.createChooser(intent, getString(R.string.share_message)))
}

fun AppCompatActivity.setSubTitle(text: String) {
    supportActionBar?.subtitle = text
}