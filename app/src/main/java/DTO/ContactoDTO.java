package DTO;

/**
 * Created by hector on 04-10-2015.
 */
public class ContactoDTO {

    private String contacto;
    private long id_contacto;
    private String nomUser;

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public long getId_contacto() {
        return id_contacto;
    }

    public void setId_contacto(long id_contacto) {
        this.id_contacto = id_contacto;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

}
