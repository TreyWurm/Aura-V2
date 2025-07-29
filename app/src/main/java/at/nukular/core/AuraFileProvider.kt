package at.nukular.core

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import at.nukular.aura.BuildConfig
import at.nukular.aura.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


private const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"


@Singleton
class AuraFileProvider @Inject constructor(
) : FileProvider(R.xml.file_paths) {

    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    fun getUriForFile(file: File): Uri {
        return getUriForFile(appContext, AUTHORITY, file)
    }


    companion object {
        @JvmStatic
        fun getUriForFile(context: Context, file: File): Uri {
            return getUriForFile(context, AUTHORITY, file)
        }
    }
}