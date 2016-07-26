package DTO;

import java.util.Calendar;

/**
 * Created by hectorfrancisco on 22-06-2016.
 */
public class SessionDTO {

    private long id;
    private String usuario;
    private short estado;
    private String ultimaFechaLogin;

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUltimaFechaLogin() {
        return ultimaFechaLogin;
    }

    public void setUltimaFechaLogin(String ultimaFechaLogin) {
        this.ultimaFechaLogin = ultimaFechaLogin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
