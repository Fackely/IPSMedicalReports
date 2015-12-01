package util.Busqueda;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.BusquedaTercerosGenerica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Juan Sebastian Castaño
 *Clase que controla todas las acciones y opciones realizadas sobre la 
 *funcionalidad de busqueda de terceros generica
 */
public class BusquedaTercerosGenericaAction extends Action{
	
	 /**
     * Objeto para manejar los logs de esta clase
     */
     private Logger logger = Logger.getLogger(BusquedaTercerosGenericaAction.class);
     
     
     /**
      * Capturador de ejecuciones
      */
     public ActionForward execute(   ActionMapping mapping,
                                                     ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response ) throws Exception
                                                     {

    	 Connection con=null;
    	 try {
    		 if (response==null); //Para evitar que salga el warning
    		 if(form instanceof BusquedaTercerosGenericaForm)
    		 {

    			 try
    			 {
    				 con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    			 }
    			 catch(SQLException e)
    			 {
    				 logger.warn("No se pudo abrir la conexión"+e.toString());
    			 }

    			 UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			 BusquedaTercerosGenericaForm busqTercerosForm = (BusquedaTercerosGenericaForm)form;//new BusquedaTercerosGenericaForm();
    			 BusquedaTercerosGenerica mundoTerceros = new BusquedaTercerosGenerica();

    			 String estado=busqTercerosForm.getEstado();
    			 logger.info("Estado -->"+estado);
    			 if(estado == null)
    			 {
    				 busqTercerosForm.clean(); 
    				 UtilidadBD.cerrarConexion(con);
    				 logger.warn("Estado no valido dentro del flujo de Busqueda de Terceros (null) ");
    				 request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				 return mapping.findForward("paginaError");             
    			 }

    			 else if(estado.equals("empezar"))
    			 {
    				 busqTercerosForm.clean();
    				 recibirXRequest(busqTercerosForm, request);
    				 if (busqTercerosForm.getMostrarTodosCampos().equals(ConstantesBD.acronimoSi))
    					 busqTercerosForm.setListTipoTer(mundoTerceros.obtenerTiposTerceros(con));

    				 logger.info("=====>Llega al estado de inicioDeBusqueda valores NIT: "+busqTercerosForm.getNit()+ 
    						 " DES: "+busqTercerosForm.getDescripcionTercero()+
    						 " Número de Registros: "+busqTercerosForm.getNumeroResultados()+" Estado Activo: "+busqTercerosForm.getFiltrarXEstadoActivo()+ 
    						 " Filtrar por Empresa: " +busqTercerosForm.getFiltrarXRelacionEmpresa()  +
    						 " Flitrar por deudore: " +busqTercerosForm.getFiltrarXDeudor()+
    						 " Flitrar por es empresa: " +busqTercerosForm.getEsEmpresa()+
    						 " Filtrar por tipoEmpresa= " +busqTercerosForm.getTipoTercero() );
    				 UtilidadBD.cerrarConexion(con);
    				 return mapping.findForward("principal");

    			 }
    			 else if(estado.equals("inicioBusquedaAvdaTerceros"))
    			 {
    				 //busqTercerosForm.clean();
    				 return this.accionBusquedaAvanzadaTerceros(con, busqTercerosForm, mapping, usuario,request);
    			 }
    			 else if(estado.equals("ordenar"))
    			 {
    				 return this.accionOrdenar(busqTercerosForm, mapping, request, con);
    			 }
    			 else 
    				 /*----------------------------------------------------------------------------------------------------
    				  * en este estado se complementa los filtros de la busqueda, se añaden los siguientes filtros:
    				  * -- Razon Social
    				  * -- Tipo Deudor 
            	  ----------------------------------------------------------------------------------------------------*/
    				 if(estado.equals("busquedaAvanzada"))
    				 {


    					 return accionBusquedaAvanzadaTerceros(con, busqTercerosForm, mapping, usuario, request);
    				 }

    				 else
    				 {
    					 busqTercerosForm.clean(); 
    					 UtilidadBD.cerrarConexion(con);
    					 logger.warn("Estado no valido dentro del flujo de Busqueda de Terceros (null) ");
    					 request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    					 return mapping.findForward("paginaError");
    				 }
    		 }

    		 return null;

    	 } catch (Exception e) {
    		 Log4JManager.error(e);
    		 return null;
    	 }
    	 finally{
    		 UtilidadBD.closeConnection(con);
    	 }
    }

  
	@SuppressWarnings("unchecked")
	private ActionForward accionOrdenar(BusquedaTercerosGenericaForm busqTercerosForm, ActionMapping mapping, HttpServletRequest request, Connection con) {
    	//columnas del listado
		String[] indices = (String[])busqTercerosForm.getMapaTerceros("INDICES");
 
		int temp = busqTercerosForm.getNumeroResultados();
		
		busqTercerosForm.setMapaTerceros(Listado.ordenarMapa(indices,
				busqTercerosForm.getIndice(),
				busqTercerosForm.getUltimoIndice(),
				busqTercerosForm.getMapaTerceros(),
				temp));
			
		busqTercerosForm.setMapaTerceros("numRegistros",temp+"");
		
		busqTercerosForm.setUltimoIndice(busqTercerosForm.getIndice());
		
		busqTercerosForm.setMapaTerceros("INDICES", indices);
		UtilidadBD.closeConnection(con);
		
		busqTercerosForm.setEstado("inicioBusquedaAvdaTerceros");
		return mapping.findForward("principal");
		
	}

	/**
     * Metodo que controla la accion del inicio de la busqueda de terceros
     * @param con
     * @param busqTercerosForm
     * @param mapping
     * @param usuario 
     * @return
     */ 
	private ActionForward accionBusquedaAvanzadaTerceros(Connection con, BusquedaTercerosGenericaForm busqTercerosForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//instanciar el mundo
		BusquedaTercerosGenerica mundoTerceros = new BusquedaTercerosGenerica();
		//cargar institucion
		mundoTerceros.setInstitucion(usuario.getCodigoInstitucionInt());
		//se reciben los nuevos filtros por request
		recibirXRequest(busqTercerosForm, request);
		
		if (!busqTercerosForm.getMostrarTodosCampos().equals(ConstantesBD.acronimoSi))
			busqTercerosForm.setMapaTerceros(mundoTerceros.consultarTerceros(con, busqTercerosForm.getNit(), busqTercerosForm.getDescripcionTercero(), busqTercerosForm.getFiltrarXEstadoActivo(), busqTercerosForm.getFiltrarXRelacionEmpresa(), busqTercerosForm.getFiltrarXDeudor(), busqTercerosForm.getTipoTercero() ));
		else
			busqTercerosForm.setMapaTerceros(mundoTerceros.consultarTercerosAvan(con, usuario.getCodigoInstitucionInt(),  busqTercerosForm.getNit(), busqTercerosForm.getDescripcionTercero(), busqTercerosForm.getFiltrarXEstadoActivo(), busqTercerosForm.getTipoTercero(),busqTercerosForm.getEsEmpresa()));
		
		// cargar el numero de resultados arrojados por la consulta
		busqTercerosForm.setNumeroResultados(Integer.parseInt(busqTercerosForm.getMapaTerceros("numRegistros").toString()));
		
		logger.info("=====>Llega al estado de inicioDeBusqueda valores NIT: "+busqTercerosForm.getNit()+ 
					" DES: "+busqTercerosForm.getDescripcionTercero()+
					" Número de Registros: "+busqTercerosForm.getNumeroResultados()+" Estado Activo: "+busqTercerosForm.getFiltrarXEstadoActivo()+ 
					" Filtrar por Empresa: " +busqTercerosForm.getFiltrarXRelacionEmpresa()  +
					" Flitrar por deudore: " +busqTercerosForm.getFiltrarXDeudor()+
					" Flitrar por es empresa: " +busqTercerosForm.getEsEmpresa());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * @param forma
	 * @param request
	 */
	public static void recibirXRequest (BusquedaTercerosGenericaForm forma, HttpServletRequest request)
	{
		//2) tipoTercero
		if (!(request.getParameter("tipoTercero")+"").equals("") && !(request.getParameter("tipoTercero")+"").equals("null"))
			forma.setRazonSocial(request.getParameter("tipoTercero")+"");
		
		//3) MostrarTodosCampos
		if (!(request.getParameter("mostrarTodosCampos")+"").equals("") && !(request.getParameter("mostrarTodosCampos")+"").equals("null"))
			forma.setMostrarTodosCampos(request.getParameter("mostrarTodosCampos")+"");
		
		//3) MostrarTodosCampos
		Logger.getLogger(BusquedaTercerosGenericaAction.class).info("ES EMPRESA DESDE EL REQUEST: "+request.getParameter("esEmpresa"));
		if (!(request.getParameter("esEmpresa")+"").equals("") && !(request.getParameter("esEmpresa")+"").equals("null"))
			forma.setEsEmpresa(request.getParameter("esEmpresa")+"");
	}

}