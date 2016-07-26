package DTO;

/**
 * Created by hectorfrancisco on 03-07-2016.
 */
public class RespuestaPendienteDTO {

    private long id;
    private short estado;
    private String respuesta;
    private long numero;
    private String contacto;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getRespuesta() {
        return respuesta;
    }
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
    public short getEstado() {
        return estado;
    }
    public void setEstado(short estado) {
        this.estado = estado;
    }
    public long getNumero() {
        return numero;
    }
    public void setNumero(long numero) {
        this.numero = numero;
    }
    public String getContacto() {
        return contacto;
    }
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }


}
