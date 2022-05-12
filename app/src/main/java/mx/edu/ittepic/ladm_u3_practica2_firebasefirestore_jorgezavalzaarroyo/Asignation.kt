package mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.ittepic.ladm_u3_practica2_firebasefirestore_jorgezavalzaarroyo.databinding.ActivityAsignationBinding

class Asignation : AppCompatActivity() {
    lateinit var binding: ActivityAsignationBinding
    lateinit var idSeleccionado : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAsignationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("Asignar Equipo")

        // Recuperar datos
        idSeleccionado = intent.extras!!.getString("codigobarras")!!
        binding.codigo.setText(idSeleccionado)

        // Asignar
        binding.asignar.setOnClickListener {
            val baseRemota = FirebaseFirestore.getInstance()
            val datos = hashMapOf(
                "nombre_empleado" to binding.nombre.text.toString(),
                "area_trabajo" to binding.area.text.toString(),
                "fecha_asig" to binding.fecha.text.toString(),
                "codigobarras" to idSeleccionado
            )
            baseRemota.collection("ASIGNACION")
                .add(datos)
                .addOnSuccessListener {
                    Toast.makeText(this, "Se asignó con éxito", Toast.LENGTH_LONG)
                        .show()
                    binding.nombre.setText("")
                    binding.area.setText("")
                    binding.fecha.setText("")
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage( it.message )
                        .show()
                }
        }

        // Regresar
        binding.regresar.setOnClickListener {
            onBackPressed()
        }
    }
}