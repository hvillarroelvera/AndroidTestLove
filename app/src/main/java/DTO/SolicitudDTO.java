package DTO;

/**
 * Created by hectorfrancisco on 13-06-2016.
 */


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SolicitudDTO implements Parcelable {

    private long numero;
    private String userEmisor;
    private String userReceptor;
    private short estado;
    private short tipo;

    public SolicitudDTO() {

    }

    public SolicitudDTO(String userReceptor) {

        this.userReceptor = userReceptor;
    }

    public SolicitudDTO(short estado, long numero, short tipo, String userEmisor, String userReceptor) {
        this.estado = estado;
        this.numero = numero;
        this.tipo = tipo;
        this.userEmisor = userEmisor;
        this.userReceptor = userReceptor;
    }

    public long getNumero() {
        return numero;
    }
    public void setNumero(long numero) {
        this.numero = numero;
    }
    public String getUserEmisor() {
        return userEmisor;
    }
    public void setUserEmisor(String userEmisor) {
        this.userEmisor = userEmisor;
    }
    public String getUserReceptor() {
        return userReceptor;
    }
    public void setUserReceptor(String userReceptor) {
        this.userReceptor = userReceptor;
    }
    public short getEstado() {
        return estado;
    }
    public void setEstado(short estado) {
        this.estado = estado;
    }
    public short getTipo() {
        return tipo;
    }
    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    protected SolicitudDTO(Parcel in) {
        numero = in.readLong();
        userEmisor = in.readString();
        userReceptor = in.readString();
        estado = (short) in.readValue(short.class.getClassLoader());
        tipo = (short) in.readValue(short.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(numero);
        dest.writeString(userEmisor);
        dest.writeString(userReceptor);
        dest.writeValue(estado);
        dest.writeValue(tipo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SolicitudDTO> CREATOR = new Parcelable.Creator<SolicitudDTO>() {
        @Override
        public SolicitudDTO createFromParcel(Parcel in) {
            return new SolicitudDTO(in);
        }

        @Override
        public SolicitudDTO[] newArray(int size) {
            return new SolicitudDTO[size];
        }
    };
}
