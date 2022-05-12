package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.ui.gallery

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
import com.google.firebase.firestore.QuerySnapshot
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.Asignation
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.Equipment
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.EquipmentEdit
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var arreglo = ArrayList<String>()
    var listaID = ArrayList<String>()
    lateinit var triggerDocumentos : QuerySnapshot

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Mostrar datos
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("INVENTARIO" )
            .addSnapshotListener { query, error ->
                if ( error != null ) {
                    AlertDialog.Builder( requireContext() )
                        .setMessage( error.message )
                        .show()
                    return@addSnapshotListener
                }
                arreglo.clear()
                listaID.clear()
                for ( documento in query!! ) {
                    var cadena = "Código: ${documento.id.toString()}\n" +
                            "Tipo: ${documento.getString("tipoequipo")}\n" +
                            "Características: ${documento.getString("caracteristicas")}\n" +
                            "Fecha compra: ${documento.getString("fechacompra")}\n"
                    arreglo.add(cadena)
                    listaID.add(documento.id.toString())
                    binding.listainventario.adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arreglo)
                }
            }

        // Agregar
        binding.agregarequipo.setOnClickListener {
            var otraVentana = Intent(requireContext(), Equipment::class.java)
            startActivity(otraVentana)
        }

        // Actualizar o Eliminar Dialog
        binding.listainventario.setOnItemClickListener { adapterView, view, indice, l ->
            dialogoEliminaActualiza( indice )
        }

        return root
    }

    private fun dialogoEliminaActualiza( indice: Int ) {
        var idElegido = listaID.get( indice )

        AlertDialog.Builder( requireContext() )
            .setTitle("Atención")
            .setMessage("Qué desea hacer con\n${arreglo.get(indice)}?")
            .setPositiveButton("Eliminar"){ d, i ->
                eliminar( idElegido )
            }.setNeutralButton("Actualizar"){ d, i ->
                actualizar( idElegido )
            }.setNegativeButton("Asignar"){ d, i ->
                var otraVentana = Intent(requireContext(), Asignation::class.java)
                otraVentana.putExtra("codigobarras", idElegido )
                startActivity(otraVentana)
            }
            .show()
    }

    private fun actualizar( idElegido: String ) {
        var otraVentana = Intent(activity, EquipmentEdit::class.java)
        otraVentana.putExtra("idseleccionado", idElegido)
        startActivity(otraVentana)
    }

    private fun eliminar( idElegido: String ) {
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("INVENTARIO" )
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