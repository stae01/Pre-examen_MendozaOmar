package mendoza.omar.preexamen_mendozaomar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegistroActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var fechaNacimientoEditText: EditText
    private lateinit var numeroEditText: EditText

    private val REQUEST_CODE_GALLERY = 1
    private var imagenUri: Uri? = null // Esta variable guardará la URI de la imagen seleccionada

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Inicializar vistas
        imageView = findViewById(R.id.imageView2)
        nombreEditText = findViewById(R.id.editTextText2)
        apellidoEditText = findViewById(R.id.editTextText3)
        fechaNacimientoEditText = findViewById(R.id.editTextDate)
        numeroEditText = findViewById(R.id.editTextNumber)

        // Configurar el clic en la imagen para seleccionar la foto
        imageView.setOnClickListener {
            seleccionarImagen()
        }

        // Configurar el botón de registro
        val btnRegistrar: Button = findViewById(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            registrarPerfil()
        }

        btnRegistrar.setOnClickListener {
            // Aquí va el código para registrar el perfil, que ya tienes
            // Una vez registrado el perfil, puedes agregarlo a la lista y regresar
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }

        val btnVolver: Button = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    // Manejo del resultado de la selección de la imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            val imageUri: Uri? = data?.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageView.setImageBitmap(bitmap) // Mostrar la imagen seleccionada en el ImageView
                this.imagenUri = imageUri // Guardar la URI de la imagen seleccionada
            } catch (e: Exception) {
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registrarPerfil() {
        val nombre = nombreEditText.text.toString()
        val apellido = apellidoEditText.text.toString()
        val fechaNacimiento = fechaNacimientoEditText.text.toString()
        val numero = numeroEditText.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || fechaNacimiento.isEmpty() || numero.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val perfil = Perfil(
            nombre = nombre,
            apellido = apellido,
            fechaNacimiento = fechaNacimiento,
            numero = numero,
            fotoUri = imagenUri
        )

        val sharedPrefs: SharedPreferences = getSharedPreferences("perfiles", Context.MODE_PRIVATE)
        val perfilesList = getPerfilesFromSharedPreferences(sharedPrefs)

        // Agregar el nuevo perfil a la lista
        perfilesList.add(perfil)

        // Guardar los perfiles actualizados en SharedPreferences
        actualizarPerfilesSharedPreferences(perfilesList)

        // Volver al RecyclerViewActivity
        val intent = Intent(this, RecyclerViewActivity::class.java)
        startActivity(intent)

    }

    private fun getPerfilesFromSharedPreferences(sharedPrefs: SharedPreferences): MutableList<Perfil> {
        val perfilesString = sharedPrefs.getString("perfiles", "")
        val perfilesList = perfilesString?.split("|")?.mapNotNull { perfilString ->
            val perfilData = perfilString.split(",")
            if (perfilData.size == 5) {
                Perfil(
                    nombre = perfilData[0],
                    apellido = perfilData[1],
                    fechaNacimiento = perfilData[2],
                    numero = perfilData[3],
                    fotoUri = Uri.parse(perfilData[4])
                )
            } else {
                null
            }
        }?.toMutableList() ?: mutableListOf()
        return perfilesList
    }

    private fun actualizarPerfilesSharedPreferences(perfiles: MutableList<Perfil>) {
        val sharedPrefs: SharedPreferences = getSharedPreferences("perfiles", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        val perfilesString = perfiles.joinToString(separator = "|") { perfil ->
            "${perfil.nombre},${perfil.apellido},${perfil.fechaNacimiento},${perfil.numero},${perfil.fotoUri}"
        }
        editor.putString("perfiles", perfilesString)
        editor.apply()
    }
}
