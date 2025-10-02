package mendoza.omar.preexamen_mendozaomar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PerfilAdapter(private val perfiles: List<Perfil>, private val listener: (Perfil) -> Unit) :
    RecyclerView.Adapter<PerfilAdapter.PerfilViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerfilViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_perfil, parent, false)
        return PerfilViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerfilViewHolder, position: Int) {
        val perfil = perfiles[position]
        holder.bind(perfil, listener)
    }

    override fun getItemCount(): Int {
        return perfiles.size
    }

    class PerfilViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreTextView: TextView = itemView.findViewById(R.id.tvNombre)
        private val apellidoTextView: TextView = itemView.findViewById(R.id.tvApellido)
        private val fotoImageView: ImageView = itemView.findViewById(R.id.ivFoto)

        fun bind(perfil: Perfil, listener: (Perfil) -> Unit) {
            nombreTextView.text = perfil.nombre
            apellidoTextView.text = perfil.apellido
            // Aquí puedes cargar la foto, por ahora dejamos una imagen por defecto
            fotoImageView.setImageResource(R.drawable.ic_launcher_background) // Usar una imagen predeterminada

            itemView.setOnClickListener { listener(perfil) }
        }
    }
}
