package mendoza.omar.preexamen_mendozaomar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.net.Uri
import android.view.View

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PerfilAdapter
    private var perfiles: MutableList<Perfil> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Cargar los perfiles desde SharedPreferences
        val sharedPrefs: SharedPreferences = getSharedPreferences("perfiles", Context.MODE_PRIVATE)
        perfiles = getPerfilesFromSharedPreferences(sharedPrefs)

        adapter = PerfilAdapter(perfiles) { perfil ->
            val intent = Intent(this, PerfilActivity::class.java)
            intent.putExtra("perfil", perfil)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Configurar el botón para agregar nuevos perfiles
        val btnAddPerfil: Button = findViewById(R.id.btnAgregarPerfil)
        btnAddPerfil.setOnClickListener {
            // Llamar a la actividad de Registro cuando se presione el botón
            val intent = Intent(this, RegistroActivity::class.java)
            // Pasar la lista actual de perfiles para mantenerla
            intent.putExtra("perfiles", ArrayList(perfiles))
            startActivity(intent)
        }
    }

    // Recuperar los perfiles desde SharedPreferences
    private fun getPerfilesFromSharedPreferences(sharedPrefs: SharedPreferences): MutableList<Perfil> {
        val perfilesString = sharedPrefs.getString("perfiles", "")
        return if (perfilesString.isNullOrEmpty()) {
            mutableListOf() // Si no hay perfiles guardados, devolver lista vacía
        } else {
            perfilesString.split("|").mapNotNull { perfilString ->
                val perfilData = perfilString.split(",")
                if (perfilData.size == 5) {
                    // Crear un perfil con los datos desglosados
                    try {
                        Perfil(
                            nombre = perfilData[0],
                            apellido = perfilData[1],
                            fechaNacimiento = perfilData[2],
                            numero = perfilData[3],
                            fotoUri = Uri.parse(perfilData[4]) // Convertir el URI a Uri
                        )
                    } catch (e: Exception) {
                        null // En caso de error en el formato, retornar null
                    }
                } else {
                    null // Si no hay 5 elementos, devolver null
                }
            }.toMutableList()
        }
    }

    //REFRESCAR O GUARDAR LOS PROFILEEEEES
    override fun onResume() {
        super.onResume()
        // Cargar los perfiles más recientes
        val sharedPrefs: SharedPreferences = getSharedPreferences("perfiles", Context.MODE_PRIVATE)
        perfiles = getPerfilesFromSharedPreferences(sharedPrefs)

        // Actualizar el adaptador con la nueva lista de perfiles
        adapter.notifyDataSetChanged()
    }


    // Actualizar los perfiles en SharedPreferences
    fun actualizarPerfilesSharedPreferences(perfiles: MutableList<Perfil>) {
        val sharedPrefs: SharedPreferences = getSharedPreferences("perfiles", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        // Convertir los perfiles a String y agregarlos a SharedPreferences
        val perfilesString = perfiles.joinToString(separator = "|") { perfil ->
            "${perfil.nombre},${perfil.apellido},${perfil.fechaNacimiento},${perfil.numero},${perfil.fotoUri}"
        }
        editor.putString("perfiles", perfilesString)
        editor.apply()
    }

    // Función para navegar al RegistroActivity
    fun irARegistro(view: View) {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }
}

