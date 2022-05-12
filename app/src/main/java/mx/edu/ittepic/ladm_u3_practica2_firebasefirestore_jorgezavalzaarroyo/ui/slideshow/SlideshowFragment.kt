package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.Asignation
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.AsignationEdit
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.EquipmentEdit
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.FragmentSlideshowBinding

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    var arreglo = ArrayList<String>()
    var listaID = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
            ViewModelProvider(this).get(SlideshowViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Mostrar
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("ASIGNACION")
            .addSnapshotListener { query, error ->
                if (error != null) {
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }
                arreglo.clear()
                listaID.clear()
                for ( documento in query!! ) {
                    var cadena = "Nombre: ${documento.getString("nombre_empleado")}\n" +
                            "Area: ${documento.getString("area_trabajo")}\n" +
                            "Fecha asig: ${documento.getString("fecha_asig")}\n" +
                            "Código: ${documento.getString("codigobarras")}\n"
                    arreglo.add(cadena)
                    listaID.add(documento.id.toString())
                    binding.listaempleados.adapter = ArrayAdapter<String>( requireContext(), android.R.layout.simple_list_item_1, arreglo )
                }
            }

        // Actualizar o Eliminar Dialog
        binding.listaempleados.setOnItemClickListener { adapterView, view, indice, l ->
            dialogoEliminaActualiza( indice )
        }

        return root
    }

    private fun dialogoEliminaActualiza (indice: Int ) {
        val idElegido = listaID.get( indice )
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Atención")
            .setMessage("¿Qué desea hacer con\n${arreglo.get(indice)}?")
            .setPositiveButton("Eliminar"){ d, i ->
                eliminar( idElegido )
            }.setNeutralButton("Actualizar"){ d, i ->
                actualizar( idElegido )
            }.setNegativeButton("Cerrar"){ d, i ->  }
            .show()
    }

    private fun actualizar( idElegido: String ) {
        var otraVentana = Intent(requireContext(), AsignationEdit::class.java)
        otraVentana.putExtra("idseleccionado", idElegido)
        startActivity(otraVentana)
    }

    private fun eliminar( idElegido: String ) {
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("ASIGNACION")
            .document( idElegido )
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Se eliminó con éxito", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                AlertDialog.Builder( requireContext() )
                    .setTitle( "Error" )
                    .setMessage( it.message )
                    .show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}