package at.nukular.aura

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import at.nukular.aura.databinding.ActivityMainBinding
import at.nukular.core.AuraFileProvider
import at.nukular.core.ui.doOnApplyWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fileWriterReader: FileWriterReader

    @Inject
    lateinit var fileProvider: AuraFileProvider


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        findViewById<View>(android.R.id.content).doOnApplyWindowInsets { view, insets, initialParams ->
            val androidContentView = view as? FrameLayout ?: return@doOnApplyWindowInsets
            val rootView = androidContentView.getChildAt(0)
            val allInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or WindowInsetsCompat.Type.ime())

            rootView?.updatePadding(top = initialParams.padding.top + allInsets.top)
            insets
        }
    }

    private fun initListeners() {
        binding.btnExport.setOnClickListener {
            try {
                val file = fileWriterReader.writeEntriesToFile() ?: return@setOnClickListener
                if (file.exists()) {
                    val uri = fileProvider.getUriForFile(file)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setType("*/*")
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        binding.btnBernhard.setOnClickListener { onTischlerClicked(Tischler.BERNHARD) }
        binding.btnCerne.setOnClickListener { onTischlerClicked(Tischler.CERNE) }
        binding.btnElli.setOnClickListener { onTischlerClicked(Tischler.ELLI) }
        binding.btnFabi.setOnClickListener { onTischlerClicked(Tischler.FABI) }
        binding.btnKo.setOnClickListener { onTischlerClicked(Tischler.KO) }
        binding.btnKrissi.setOnClickListener { onTischlerClicked(Tischler.KRISSI) }
        binding.btnMale.setOnClickListener { onTischlerClicked(Tischler.MALE) }
        binding.btnMichi.setOnClickListener { onTischlerClicked(Tischler.MICHI) }
        binding.btnNiklas.setOnClickListener { onTischlerClicked(Tischler.NIKLAS) }
        binding.btnPeter.setOnClickListener { onTischlerClicked(Tischler.PETER) }
        binding.btnSusi.setOnClickListener { onTischlerClicked(Tischler.SUSI) }
        binding.btnTrevor.setOnClickListener { onTischlerClicked(Tischler.TREVOR) }
    }


    private fun onTischlerClicked(tischler: Tischler) {
        fileWriterReader.addEntry(LocalDateTime.now(), tischler)
    }
}