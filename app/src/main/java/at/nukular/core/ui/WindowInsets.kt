package at.nukular.core.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnAttach
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop


data class InitialParams(
    val margin: InitialMargin,
    val padding: InitialPadding,
)

data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)
data class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
private fun recordInitialMarginForView(view: View) = InitialMargin(view.marginLeft, view.marginTop, view.marginRight, view.marginBottom)


fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, InitialParams) -> Unit) {
    val initialPadding = recordInitialPaddingForView(this)
    val initialMargin = recordInitialMarginForView(this)
    val initialParams = InitialParams(initialMargin, initialPadding)

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, initialParams)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    doOnAttach { requestApplyInsets() }
}