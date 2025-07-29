package at.nukular.core.ui

import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat

interface WindowInsetListener {

    fun addWindowInsetListeners()

    fun addWindowInsetListener(view: View, applyInsets: (view: View, insets: Insets, initialParams: InitialParams) -> Unit) {
        view.doOnApplyWindowInsets { v, windowInsets, initialParams ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime())
            applyInsets(v, insets, initialParams)
            windowInsets
        }
    }
}