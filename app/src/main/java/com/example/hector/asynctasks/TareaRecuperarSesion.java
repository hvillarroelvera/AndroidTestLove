public class TareaRecuperarSesion extends AsyncTask<String,Integer,String>{
	
	
	private ServicioRest servicio;
    private String resulTarea="0";
    private Context context;
    private Util util;
    private ProgressDialog dialogoProgreso;
    private SessionDTO sessionDTO;
	private  ContactoDTO cDTO;
	private Intent intent;

    public TareaRecuperarSesion(Context context,Intent intent) {
        this.context = context;
        this.servicio = new ServicioRest(context);
        this.util = new Util();
        this.sessionDTO = new SessionDTO();
		this.cDTO=new ContactoDTO();
		this.intent = intent;
        dialogoProgreso = new ProgressDialog(context);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialogoProgreso.setMessage(Constantes.PROGRESSBAR_LABEL_REGISTRANDO);
        dialogoProgreso.setCancelable(true);
        dialogoProgreso.setMax(100);
    }
	
	
        @Override
        protected String doInBackground(String... params)
        {
            String msg = "";
			try {
				
            sessionDTO= servicio.getSesionXUsuario(util.getUserCache(context));
            cDTO = servicio.recuperarContactoXUsuario(util.getUserCache(context));
			} catch (ConnectionException e) {
            resulTarea = e.getDescError();
			} catch (HttpCallException e) {
			resulTarea = e.getDescError();
			}
			
            return msg;
        }

        protected void onPostExecute(String result) {
            Log.d(TAG, "Entro a OnPostExecute");
            if (sessionDTO != null && sessionDTO.getEstado()==Constantes.SESSION_STARTED) {
                if(cDTO != null){
                    
                    init();
                    setToOtherActivity();
                    startActivity(intent);
                }else{
                    
                    init();
                    setToOtherActivity();
                    startActivity(intent);
                }

            } else {
                
                Toast.makeText(Inicio.this, "No se ha recuperado sesion"+resulTarea, Toast.LENGTH_SHORT).show();*/
            }

        }
}