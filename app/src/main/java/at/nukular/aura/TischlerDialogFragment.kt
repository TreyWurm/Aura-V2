package at.nukular.aura

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import at.nukular.aura.databinding.DialogTischlerBinding
import at.nukular.core.ui.BaseDialogFragmentImpl
import at.nukular.core.ui.ComponentInteractionListener
import at.nukular.core.ui.attachComponentInteractionListener

class TischlerDialogFragment : BaseDialogFragmentImpl(),
    ComponentInteractionListener<TischlerListener> {

    override val viewModel: ViewModel? = null
    override var listener: TischlerListener? = null
    private lateinit var binding: DialogTischlerBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogTischlerBinding.inflate(inflater, container, false)
        binding.btnBernhard.setOnClickListener { listener?.confirmMe(Tischler.BERNHARD); dismiss() }
        binding.btnCerne.setOnClickListener { listener?.confirmMe(Tischler.CERNE); dismiss() }
        binding.btnElli.setOnClickListener { listener?.confirmMe(Tischler.ELLI); dismiss() }
        binding.btnFabi.setOnClickListener { listener?.confirmMe(Tischler.FABI); dismiss() }
        binding.btnKo.setOnClickListener { listener?.confirmMe(Tischler.KO); dismiss() }
        binding.btnKrissi.setOnClickListener { listener?.confirmMe(Tischler.KRISSI); dismiss() }
        binding.btnMale.setOnClickListener { listener?.confirmMe(Tischler.MALE); dismiss() }
        binding.btnMichi.setOnClickListener { listener?.confirmMe(Tischler.MICHI); dismiss() }
        binding.btnNiklas.setOnClickListener { listener?.confirmMe(Tischler.NIKLAS); dismiss() }
        binding.btnPeter.setOnClickListener { listener?.confirmMe(Tischler.PETER); dismiss() }
        binding.btnSusi.setOnClickListener { listener?.confirmMe(Tischler.SUSI); dismiss() }
        binding.btnTrevor.setOnClickListener { listener?.confirmMe(Tischler.TREVOR); dismiss() }
        return binding.root
    }

    override fun attachComponentInteractionListener() {
        attachComponentInteractionListener(this)
    }

    companion object {
        private const val TAG = "dialogTischler"

        private fun newInstance(): TischlerDialogFragment {
            val fragment = TischlerDialogFragment()
            val arguments = Bundle()
            fragment.arguments = arguments
            return fragment
        }

        @JvmStatic
        @JvmOverloads
        fun startFragment(caller: Any) {
            when (caller) {
                is FragmentActivity -> startFragment(caller)
            }
        }


        @JvmStatic
        fun startFragment(activity: FragmentActivity) {
            val prev = activity.supportFragmentManager.findFragmentByTag(TAG)
            if (prev == null) {
                val dialogFragment = newInstance()
                val ft = activity.supportFragmentManager.beginTransaction()
                dialogFragment.show(ft, TAG)
            }
        }
    }
}