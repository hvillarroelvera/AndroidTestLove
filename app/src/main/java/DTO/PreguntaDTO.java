package DTO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hector on 11-10-2015.
 */
public class PreguntaDTO implements Parcelable {

    private String pregunta;
    private long numero;

    public PreguntaDTO() {
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Pregunta{" +
                "pregunta=" + pregunta +
                "}";
    }

    protected PreguntaDTO(Parcel in) {
        pregunta = in.readString();
        numero = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pregunta);
        dest.writeLong(numero);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PreguntaDTO> CREATOR = new Parcelable.Creator<PreguntaDTO>() {
        @Override
        public PreguntaDTO createFromParcel(Parcel in) {
            return new PreguntaDTO(in);
        }

        @Override
        public PreguntaDTO[] newArray(int size) {
            return new PreguntaDTO[size];
        }
    };
}
