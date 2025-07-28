package at.nukular.aura

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.nukular.aura.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private enum class Tischler(val string: String) {
        BERNHARD("Bernhard"),
        CERNE("Cerne"),
        ELLI("Elli"),
        FABI("Fabi"),
        KO("Ko"),
        KRISSI("Krissi"),
        MALE("Male"),
        MICHI("Michi"),
        NIKLAS("Niklas"),
        PETER("Peter"),
        SUSI("Susi"),
        TREVOR("Trevor"),
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
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

    }
}