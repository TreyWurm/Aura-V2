package at.nukular.aura

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import at.nukular.aura.databinding.DialogConfirmBinding
import at.nukular.core.extensions.getSerializableCompat
import at.nukular.core.ui.BaseDialogFragmentImpl
import at.nukular.core.ui.ComponentInteractionListener
import at.nukular.core.ui.attachComponentInteractionListener

class ConfirmDialogFragment : BaseDialogFragmentImpl(),
    ComponentInteractionListener<ProceedListener> {

    override val viewModel: ViewModel? = null
    override var listener: ProceedListener? = null
    private lateinit var binding: DialogConfirmBinding
    private lateinit var tischler: Tischler


    override fun parseBundle(bundle: Bundle?): Boolean {
        tischler = bundle?.getSerializableCompat("Tischler", Tischler::class.java) ?: return false
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmBinding.inflate(inflater, container, false)

        binding.tvText.text = "Möchtest du einen Gedanken an folgende Person notieren?\n\n${tischler.string}"
        binding.tvConfirm.setOnClickListener {
            listener?.confirmTischler(tischler)
            dismiss()
        }
        binding.tvCancel.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun attachComponentInteractionListener() {
        attachComponentInteractionListener(this)
    }

    companion object {
        private const val TAG = "dialogConfirm"

        private fun newInstance(data: Tischler): ConfirmDialogFragment {
            val fragment = ConfirmDialogFragment()
            val arguments = Bundle()
            arguments.putSerializable("Tischler", data)
            fragment.arguments = arguments
            return fragment
        }

        @JvmStatic
        @JvmOverloads
        fun startFragment(caller: Any, tischler: Tischler) {
            when (caller) {
                is FragmentActivity -> startFragment(caller, tischler)
            }
        }


        @JvmStatic
        fun startFragment(activity: FragmentActivity, tischler: Tischler) {
            val prev = activity.supportFragmentManager.findFragmentByTag(TAG)
            if (prev == null) {
                val dialogFragment = newInstance(tischler)
                val ft = activity.supportFragmentManager.beginTransaction()
                dialogFragment.show(ft, TAG)
            }
        }
    }
}