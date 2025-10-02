package mendoza.omar.preexamen_mendozaomar

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Perfil(
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val numero: String,
    val fotoUri: Uri? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(Uri::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(fechaNacimiento)
        parcel.writeString(numero)
        parcel.writeParcelable(fotoUri, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Perfil> {
        override fun createFromParcel(parcel: Parcel): Perfil {
            return Perfil(parcel)
        }

        override fun newArray(size: Int): Array<Perfil?> {
            return arrayOfNulls(size)
        }
    }
}
