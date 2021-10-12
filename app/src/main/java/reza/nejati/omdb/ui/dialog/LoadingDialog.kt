package reza.nejati.omdb.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import dagger.hilt.android.qualifiers.ActivityContext
import reza.nejati.omdb.R
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

class LoadingDialog @Inject constructor(@ActivityContext context: Context) : Dialog(context) {

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        this.window?.setGravity(Gravity.CENTER)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.setCancelable(false)
        this.setContentView(R.layout.layout_dialog_loading)

    }

    fun showProgress() {
        if (!this.isShowing) {
            this.show()
        }
    }

    fun dismissProgress() {
        if (this.isShowing) {
            this.dismiss()
        }
    }

}