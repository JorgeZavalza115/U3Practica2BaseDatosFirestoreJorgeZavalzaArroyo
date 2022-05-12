package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.ActivityEquipmentEditBinding

class EquipmentEdit : AppCompatActivity() {
    lateinit var binding: ActivityEquipmentEditBinding
    lateinit var idSeleccionado : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEquipmentEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Editar Equipo")

        // Recuperar datos
        idSeleccionado = intent.extras!!.getString("idseleccionado")!!
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("INVENTARIO")
            .document( idSeleccionado )
            .get()
            .addOnSuccessListener {
                binding.tipo.setText( it.getString("tipoequipo"))
                binding.caracteristicas.setText( it.getString("caracteristicas"))
                binding.fecha.setText( it.getString("fechacompra"))
                binding.codigo.setText( it.id.toString() )
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage( it.message )
                    .show()
            }

        // Actualizar
        binding.actualizar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("INVENTARIO")
                .document( idSeleccionado )
                .update( "tipoequipo", binding.tipo.text.toString(),
                    "caracteristicas", binding.caracteristicas.text.toString(),
                    "fechacompra", binding.fecha.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Se actualizó con éxito, puede pulsar REGRESAR", Toast.LENGTH_LONG)
                        .show()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage( it.message )
                        .show()
                }
        }

        // Regresar
        binding.regresar.setOnClickListener {
            finish()
        }
    }
}