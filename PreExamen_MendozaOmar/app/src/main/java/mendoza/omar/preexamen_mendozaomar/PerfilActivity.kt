package mendoza.omar.preexamen_mendozaomar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val perfil = intent.getSerializableExtra("perfil") as Perfil

        val nombreTextView: TextView = findViewById(R.id.tvNombre)
        val apellidoTextView: TextView = findViewById(R.id.tvApellido)
        val fechaNacimientoTextView: TextView = findViewById(R.id.tvFechaNacimiento)
        val numeroTextView: TextView = findViewById(R.id.tvNumero)

        nombreTextView.text = perfil.nombre
        apellidoTextView.text = perfil.apellido
        fechaNacimientoTextView.text = perfil.fechaNacimiento
        numeroTextView.text = perfil.numero
    }
}
