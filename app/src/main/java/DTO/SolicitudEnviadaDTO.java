package DTO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hectorfrancisco on 09-07-2016.
 */
public class SolicitudEnviadaDTO implements Parcelable {

    private long numero;
    private String nombreContacto;

    public SolicitudEnviadaDTO() {
    }

    public SolicitudEnviadaDTO(Parcel in) {
        readFromParcel(in);
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(numero);
        dest.writeString(nombreContacto);
    }

    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        numero = in.readLong();
        nombreContacto = in.readString();
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public SolicitudEnviadaDTO createFromParcel(Parcel in) {
                    return new SolicitudEnviadaDTO(in);
                }

                public SolicitudEnviadaDTO[] newArray(int size) {
                    return new SolicitudEnviadaDTO[size];
                }
            };
}
