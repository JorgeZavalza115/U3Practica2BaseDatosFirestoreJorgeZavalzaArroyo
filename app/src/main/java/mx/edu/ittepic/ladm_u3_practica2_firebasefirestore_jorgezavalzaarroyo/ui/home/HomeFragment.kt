package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.R
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.imagenempresa.setImageResource(R.drawable.zdesigns)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}