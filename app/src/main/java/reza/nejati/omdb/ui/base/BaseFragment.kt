package reza.nejati.omdb.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import reza.nejati.omdb.ui.dialog.LoadingDialog
import javax.inject.Inject

/**
 * Mahtab Azizi <mahtab.azizy@gmail.com>
 */

/**
 *Base Fragment with ViewModel and ViewBinding
 *
 * @param VM ViewBinding
 * @param VB ViewModel
 */
abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

    @Inject
    lateinit var dialog: LoadingDialog

    protected abstract val mViewModel: VM

    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mViewBinding.root


    }

    protected fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            dialog.showProgress()

        } else {
            dialog.dismissProgress()

        }
    }

    protected abstract fun onViewCreated(savedInstanceState: Bundle?)

    abstract fun getViewBinding(): VB


}
