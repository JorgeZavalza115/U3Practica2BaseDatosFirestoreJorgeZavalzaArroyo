package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.ActivityAsignationEditBinding

class AsignationEdit : AppCompatActivity() {
    lateinit var binding: ActivityAsignationEditBinding
    lateinit var idSeleccionado : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAsignationEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Editar Asignación")

        // Recuperar datos
        idSeleccionado = intent.extras!!.getString("idseleccionado")!!
        val baseRemota = FirebaseFirestore.getInstance()
        baseRemota.collection("ASIGNACION")
            .document( idSeleccionado )
            .get()
            .addOnSuccessListener {
                binding.nombre.setText( it.getString("nombre_empleado") )
                binding.area.setText( it.getString("area_trabajo") )
                binding.fecha.setText( it.getString("fecha_asig") )
                binding.codigo.setText( it.getString("codigobarras") )
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage( it.message )
                    .show()
            }

        // Actualizar
        binding.actualizar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            baseRemota.collection("ASIGNACION")
                .document( idSeleccionado )
                .update( "nombre_empleado", binding.nombre.text.toString(),
                    "area_trabajo", binding.area.text.toString(),
                    "fecha_asig", binding.fecha.text.toString()
                )
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