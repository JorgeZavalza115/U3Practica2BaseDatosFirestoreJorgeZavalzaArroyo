package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.ActivityEquipmentBinding

class Equipment : AppCompatActivity() {
    lateinit var binding: ActivityEquipmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Agregar Equipo")

        // Agregar Equipo
        binding.agregarequipo.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            val datos = hashMapOf(
                "tipoequipo" to binding.tipo.text.toString(),
                "caracteristicas" to binding.caracteristicas.text.toString(),
                "fechacompra" to binding.fecha.text.toString()
            )
            baseRemota.collection("INVENTARIO").document(binding.codigo.text.toString())
                .set(datos)
                .addOnSuccessListener {
                    Toast.makeText(this, "Éxito! Se insertó", Toast.LENGTH_LONG)
                        .show()
                    binding.codigo.setText("")
                    binding.tipo.setText("")
                    binding.caracteristicas.setText("")
                    binding.fecha.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }

        // Regresar
        binding.regresar.setOnClickListener {
            onBackPressed()
        }
    }
}