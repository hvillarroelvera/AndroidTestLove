package DTO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hectorfrancisco on 03-07-2016.
 */
public class PuntuacionDTO implements Parcelable {

    private long idContacto;
    private int puntos;

    public PuntuacionDTO() {
    }

    public long getIdContacto() {
        return idContacto;
    }
    public void setIdContacto(long idContacto) {
        this.idContacto = idContacto;
    }
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    protected PuntuacionDTO(Parcel in) {
        idContacto = in.readLong();
        puntos = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(idContacto);
        dest.writeInt(puntos);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PuntuacionDTO> CREATOR = new Parcelable.Creator<PuntuacionDTO>() {
        @Override
        public PuntuacionDTO createFromParcel(Parcel in) {
            return new PuntuacionDTO(in);
        }

        @Override
        public PuntuacionDTO[] newArray(int size) {
            return new PuntuacionDTO[size];
        }
    };
}
