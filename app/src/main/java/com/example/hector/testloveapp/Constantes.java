package com.example.hector.testloveapp;

/**
 * Created by hector on 20-06-2015.
 */
public class Constantes {

    public static final short SESSION_STARTED = 1;
    public static final short SESSION_FINALIZED = 0;

    public static final String APP_NAME="TestLoveApp";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PROPERTY_EXPIRATION_TIME = "onServerExpirationTimeMs";
    public static final String PROPERTY_CONTACTO = "contacto";

    /*Properties para flujo pendientes*/
    public static final String PROPERTY_SOLICITUD_ENVIADA = "solicitudEnviada";
    public static final String PROPERTY_SOLICITUD_RECIBIDA = "solicitudRecibida";
    public static final String PROPERTY_PREGUNTA_CONTESTADA = "preguntaContestada";
    public static final String PROPERTY_PUNTUACION_RECIBIDA = "puntuacionRecibida";
    public static final String PROPERTY_PREGUNTA_ENVIADA = "preguntaEnviada";
    public static final String PROPERTY_PREGUNTA_RECIBIDA = "preguntaRecibida";
    /*Properties para flujo pendientes*/

    public static final String PROPERTY_ID_CONTACTO = "idContacto";
    public static final String PROPERTY_SOLICITANTE = "solicitante";
    public static final String PROPERTY_PREGUNTA = "pregunta";
    public static final String PROPERTY_CANTIDAD_PREGUNTA = "CantidadPreguntas";
    public static final String PROPERTY_PUNTUACION_PREGUNTADAS = "puntuacionPreguntadas";
    public static final String PROPERTY_PUNTUACION_CONTESTADAS = "puntuacionContestadas";
    public static final String PROPERTY_RESULTADO = "resultado";
    public static final String PROPERTY_USER = "user";
    public static final String PROPERTY_SESSION = "session";
    public static final String PROPERTY_CANTIDAD_SOLICITUDES_ENVIADAS = "CantidadSolicitudesEnviadas";


    public static final Long EXPIRATION_TIME_MS = Long.valueOf(604800000);
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String SENDER_ID="352339063753";

    public static final int SUCCESS = 1;
    public static final int NOT_SUCCESS = 0;

    public static final boolean SESSION_OPEN = true;

    public static final int CACHE_NOT_FOUND_USER = 2;
    public static final int CACHE_NOT_FOUND_REG_ID = 3;
    public static final int CACHE_NOT_EXPIRATION_TIME = 4;
    public static final int CACHE_NOT_APP_VERSION = 5;


    public static final String TIPO_MENSAJE_GCM_SOLICITUD_CONTACTO = "solicitudContacto";
    public static final String TIPO_MENSAJE_GCM_PREGUNTA = "pregunta";
    public static final String TIPO_MENSAJE_GCM_RESPUESTA_PREGUNTA = "respuestaPregunta";
    public static final String TIPO_MENSAJE_GCM_PUNTUACION = "puntuacion";

    public static final String SOL_CONTACTO_USER_SOLICITANTE = "nombreUsuario";

    public static final String TIPO_MENSAJE_GCM_RESULTADO_SOLICITUD_CONTACTO = "ResulSolContacto";


    public static final short HTTPCALL_TIPO_PUT = 1;
    public static final short HTTPCALL_TIPO_POST = 2;
    public static final short HTTPCALL_TIPO_DELETE = 3;


    /*Host para servidor en casa*/
    public static final String HTTPCALL_HOST_DESARROLLO1 = "http://testlove.ddns.net:8060";
    /*Host para servidor AmazonWS*/
    public static final String HTTPCALL_HOST_DESARROLLO2 = "ec2-52-38-144-57.us-west-2.compute.amazonaws.com";
    /*Host productivo para servidor AmazonWS*/
    public static final String HTTPCALL_HOST_PRODUCCION = "";
    /*Ruta del WS backend*/
    public static final String HTTPCALL_RUTA_TEST_LOVE_APP_BACKEND = "/testlovebackend/rest/";
    public static final String HTTPCALL_RUTA_PRINCIPAL = HTTPCALL_HOST_DESARROLLO1+HTTPCALL_RUTA_TEST_LOVE_APP_BACKEND;

    public static final String HTTPCALL_RUTA_USUARIO_REGISTRAR = "usuario/registrarWithJson";
    public static final String HTTPCALL_RUTA_CONTACTO_SOLICITUD_RECUPERAR = "contacto/getSolicitudWithJson";
    public static final String HTTPCALL_RUTA_CONTACTO_SOLICITUD_ELIMINAR = "contacto/eliminarSolicitudContactoJson";
    public static final String HTTPCALL_RUTA_CONTACTO_REGISTRAR = "contacto/registrarWithJson";
    public static final String HTTPCALL_RUTA_CONTACTO_ELIMINAR = "contacto/eliminarContactoJson";
    public static final String HTTPCALL_RUTA_CONTACTO_MODIFICAR = "contacto/modificarContactoJson";
    public static final String HTTPCALL_RUTA_USUARIO_LOGUEAR = "usuario/loguearWithJson";
    public static final String HTTPCALL_RUTA_GCM_OBTENER = "gcm/getGcmXUsuarioWithJson";
    public static final String HTTPCALL_RUTA_GCM_ACTUALIZAR = "gcm/actualizarGcmWithJson";
    public static final String HTTPCALL_RUTA_CONTACTO_OBTENER = "contacto/getContactoXUsuarioWithJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_OBTENER_CANTIDAD = "pregunta/getCountPreguntasXUsuarioWithJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_OBTENER_PREGUNTAS = "pregunta/getPreguntasXUsuarioWithJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_PREGUNTAR = "pregunta/preguntarWithJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_REGISTRAR = "pregunta/registrarWithJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_MODIFICAR = "pregunta/modificarPreguntaJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_ELIMINAR = "pregunta/eliminarPreguntaJson";
    public static final String HTTPCALL_RUTA_CONTACTO_ENVIAR_SOLICITUD = "contacto/enviarSolicitudWithJson";
    public static final String HTTPCALL_RUTA_CONTACTO_REENVIAR_SOLICITUD = "contacto/reenviarSolicitudJson";
    public static final String HTTPCALL_RUTA_PREGUNTA_RESPONDER = "pregunta/responderPreguntaWithJson";
    public static final String HTTPCALL_RUTA_GCM_PUNTUACION = "pregunta/puntuacionWithJson";
    public static final String HTTPCALL_RUTA_USUARIO_CERRAR_SESION = "usuario/logoutWithJson";
    public static final String HTTPCALL_RUTA_SESION_OBTENER_SESION_BY_USUARIO = "sesion/getSesionByUsuarioWithJson";
    public static final String HTTPCALL_RUTA_PREGUNTAPENDIENTE_ELIMINAR_ALL_BY_CONTACTO = "preguntaPendiente/eliminarAllPreguntaPendienteByContactoJson";
    public static final String HTTPCALL_RUTA_RESPUESTAPENDIENTE_ELIMINAR_ALL_BY_CONTACTO = "respuestaPendiente/eliminarAllRespuestaPendienteByContactoJson";
    public static final String HTTPCALL_RUTA_PUNTUACIONPENDIENTE_ELIMINAR_ALL_BY_CONTACTO = "puntuacionPendiente/eliminarAllPuntuacionPendienteByContactoJson";
    public static final String HTTPCALL_RUTA_PUNTUACION_ELIMINAR_BY_IDCONTACTO = "puntuacion/eliminarPuntuacionJson";




    public static final String PUNTUACION_RESPUESTA_CORRECTA= "1";
    public static final String PUNTUACION_RESPUESTA_INCORRECTA = "0";

    public static final String PROGRESSBAR_LABEL_REGISTRANDO = "Registrando...";
    public static final String PROGRESSBAR_LABEL_LOGIN = "Logueando...";
    public static final short TIPO_DIALOGO_AGREGAR_CONTACTO = 1;
    public static final short TIPO_DIALOGO_AGREGAR_PREGUNTA = 2;
    public static final short TIPO_DIALOGO_ENVIAR_PREGUNTA = 1;
    public static final short TIPO_DIALOGO_ELIMINAR_PREGUNTA = 2;

    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_MUYFELIZ = 0;
    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_BIENFELIZ = 0;
    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_FELIZ = 0;
    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_NORMAL = 0;
    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_TRISTE = 0;
    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_BIENTRISTE = 0;
    public static final short ANIMACION_MENU_PRINCIPAL_PUNTUACION_MUYTRISTE = 0;

    public static final String RESULTADO_STATUS_200 = "1";
    public static final String RESULTADO_STATUS_409 = "2";

    public static final short ERROR_GENERAL_CONEXION = 10;
    public static final short ERROR_GENERAL_REST_SERVICE = 20;

    public static final String ERROR_NO_EXISTE_CONEXION = "No estas conectado a internet";

    public static final String PARCEL_LISTA_SOL_ENVIADA = "LISTA_SOL_ENVIADA";
    public static final String PARCEL_LISTA_SOL_RECIBIDA = "LISTA_SOL_RECIBIDA";
    public static final String PARCEL_LISTA_SOL_CONTESTADA = "LISTA_SOL_CONTESTADA";
    public static final String PARCEL_LISTA_PREG_RECIBIDA = "LISTA_PREG_RECIBIDA";
    public static final String PARCEL_LISTA_PREG_ENVIADA = "LISTA_PREG_ENVIADA";
    public static final String PARCEL_LISTA_PREG_CONTESTADA = "LISTA_PREG_CONTESTADA";
    public static final String PARCEL_LISTA_PUNT_RECIBIDA = "LISTA_PUNT_RECIBIDA";

}
