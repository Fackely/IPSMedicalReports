package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.SoportesFacturasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.SoportesFacturas;

/**
 * Clase para el manejo de la parametrizacion 
 * de los soportes de facturas
 * Date: 2009-01-26
 * @author jfhernandez@princetonsa.com
 */
public class SoportesFacturasAction extends Action 
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(SoportesFacturasAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{
    		if(response==null);
    		if(form instanceof SoportesFacturasForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			SoportesFacturasForm forma = (SoportesFacturasForm)form;
    			String estado = forma.getEstado();

    			logger.info("\n\n\n ESTADO ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización de soportes de facturas (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			//obtenerTiposSoporte
    			//else 
    			/*------------------------------
    			 * 		ESTADO > consultar
    			 *-------------------------------*/
    			if(estado.equals("consultar"))
    			{   
    				return accionConsultar(con, mapping, forma, request, usuario);
    			}
    			/*------------------------------
    			 * 		ESTADO > consultarIngresarModificar
    			 *-------------------------------*/
    			if(estado.equals("consultarIngresarModificar"))
    			{   
    				return accionConsultarIngresarModificar(con, mapping, forma, request, usuario);
    			}
    			/*------------------------------
    			 * 		ESTADO > ingresarModificar
    			 *-------------------------------*/
    			if(estado.equals("ingresarModificar"))
    			{   
    				return accionIngresarModificar(con, mapping, forma, request, usuario);
    			}
    			/*------------------------------
    			 * 		ESTADO > guardar
    			 *-------------------------------*/
    			if(estado.equals("guardar"))
    			{   
    				return accionGuardar(con, mapping, forma, request, usuario);
    			}
    		}
    	}catch (Exception e) {
    		Log4JManager.error(e);
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    	return null;
    }

    /**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionConsultarIngresarModificar(Connection con,ActionMapping mapping, SoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario) {
    	if(!forma.getSoportesFacturasMap("viaIngreso").toString().equals("")){
    		//forma.setSoportesFacturasMap(SoportesFacturas.consultar(con, forma.getTiposSoportes(), forma.getSoportesFacturasMap(), usuario.getCodigoInstitucionInt()));
    		//forma.setSoportesFacturasMapAnterior(organizarMapa(forma.getTiposSoportes(), forma.getSoportesFacturasMap()));
    		forma.setTiposSoportesMap(organizarTiposSoportesMap(SoportesFacturas.obtenerTiposSoporteXConvenio(con, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(forma.getSoportesFacturasMap("viaIngreso").toString()), Utilidades.convertirAEntero(forma.getSoportesFacturasMap("convenio").toString()))));
    	} else {
    		for(int i=0; i<Utilidades.convertirAEntero(forma.getTiposSoportesMap("numRegistros")+""); i++){
    			
    			forma.setTiposSoportesMap("activo_"+i, "false");
    			
    			if(!UtilidadTexto.isEmpty(forma.getTiposSoportesMap("codpadre_"+i).toString())
    					&& UtilidadTexto.getBoolean(forma.getTiposSoportesMap("mostraropcioneshijas_"+forma.getTiposSoportesMap("codpadre_"+i))+"")){
    				forma.setTiposSoportesMap("mostraropcioneshijas_"+forma.getTiposSoportesMap("codpadre_"+i), "false");
    				forma.setTiposSoportesMap("activohijo_"+forma.getTiposSoportesMap("codpadre_"+i), "");
    			}
    			
    		}	
    	}
    	
    	//Se genera el respaldo para el log 
    	forma.setRespaldoSoportes(forma.getTiposSoportesMap());
    	
    	
    	Utilidades.imprimirMapa(forma.getRespaldoSoportes());
    	
    	
    	UtilidadBD.closeConnection(con);
    	if(forma.getFuncionalidad().equals("ingresarModificar"))
    		return mapping.findForward("principal");
    	else
    		return mapping.findForward("consultar");
	}
    
    /**
     * Metodo para organizar las llaves del mapa
     * @param obtenerTiposSoporteXConvenio
     * @return
     */
	private HashMap<String, Object> organizarTiposSoportesMap(HashMap<String, Object> tiposSoporteXConvenio) {
		
		for(int i=0; i<Utilidades.convertirAEntero(tiposSoporteXConvenio.get("numRegistros").toString()); i++){
			int tipoSoporteHijo = ConstantesBD.codigoNuncaValido; 
			if (!tiposSoporteXConvenio.get("codpadre_"+i).toString().equals("")){
				tipoSoporteHijo = obtenerTipoSoporteHijo(tiposSoporteXConvenio, tiposSoporteXConvenio.get("codpadre_"+i).toString());
				if(tipoSoporteHijo==ConstantesBD.codigoNuncaValido){
					tiposSoporteXConvenio.put("mostraropcioneshijas_"+tiposSoporteXConvenio.get("codpadre_"+i), false);
					tiposSoporteXConvenio.put("activohijo_"+tiposSoporteXConvenio.get("codpadre_"+i), "");
				} else {
					tiposSoporteXConvenio.put("mostraropcioneshijas_"+tiposSoporteXConvenio.get("codpadre_"+i), true);
					tiposSoporteXConvenio.put("activohijo_"+tiposSoporteXConvenio.get("codpadre_"+i), tipoSoporteHijo);
				}
			}
		}
		
		return tiposSoporteXConvenio;
	}
	
	/**
	 * Método para obtener el hijo activo de un tipo de soporte padre
	 * @param tiposSoporteXConvenio
	 * @param string
	 * @return
	 */
	private int obtenerTipoSoporteHijo(HashMap<String, Object> tiposSoporteXConvenio, String codPadre) {
		int hijoActivo = ConstantesBD.codigoNuncaValido;
		
		for(int i=0; i<Utilidades.convertirAEntero(tiposSoporteXConvenio.get("numRegistros").toString()); i++){
			if (!tiposSoporteXConvenio.get("codpadre_"+i).toString().equals("") && tiposSoporteXConvenio.get("codpadre_"+i).toString().equals(codPadre) && UtilidadTexto.getBoolean(tiposSoporteXConvenio.get("activo_"+i)+"")){
				hijoActivo = Utilidades.convertirAEntero(tiposSoporteXConvenio.get("codtiposoporte_"+i).toString());
			}
		}	
		
		return hijoActivo;
	}

    /**
     * 
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
	private ActionForward accionConsultar(Connection con, ActionMapping mapping, SoportesFacturasForm forma,HttpServletRequest request, UsuarioBasico usuario) {
		forma.reset();
		forma.setFuncionalidad("consultar");
		
		// Cargar el contenido de los campos de selección de los convenios y las vias de ingreso
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		forma.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
		forma.setTiposSoportesMap(organizarTiposSoportesMap(SoportesFacturas.obtenerTiposSoporteXConvenio(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido)));
		
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("consultar");
	}

	/**
     * Accion Guardar
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionGuardar(Connection con, ActionMapping mapping, SoportesFacturasForm forma, HttpServletRequest request, UsuarioBasico usuario) {
    	
    	// Organizar llaves del mapa
    	HashMap<String, Object> mapaOrganizado = organizarMapa(forma.getTiposSoportesMap(), forma.getSoportesFacturasMap());

    	if(SoportesFacturas.guardar(con, mapaOrganizado, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario()))
    		forma.setMensaje("¡PROCESO REALIZADO EXITOSAMENTE!");
    	
   		//Utilidades.generarLogGenerico( mapaOrganizado,forma.getSoportesFacturasMapAnterior(), usuario.getNombreUsuario(), false, 0, ConstantesBD.logRevisionCuentaRequisitosCodigo, indices);  	
    	
    	logger.info("****EL MAPA DE LOS SOPORTES DE FACTURA****");
    	Utilidades.imprimirMapa(mapaOrganizado);
    	logger.info("****EL MAPA DE RESPALDO DE LOS SOPORTES DE FACTURA****");
    	Utilidades.imprimirMapa(forma.getRespaldoSoportes());
    	
    	//************GENERACION LOG
    	 String log = "";
    	 String separador = System.getProperty("file.separator");
    	 int tipoLog=ConstantesBD.tipoRegistroLogModificacion;
    	 log=	"\n=======LOG ARCHIVO MODIFICACION DE SOPORTES DE FACTURAS========" +
    	 		"\nFecha: "+UtilidadFecha.getFechaActual()+
    	 		"\nHora: "+UtilidadFecha.getHoraActual()+
    	 		"\nUsuario que Modifica: "+usuario.getLoginUsuario()+
    	 		"\n" +
    	 		"\n======DATOS ANTERIORES A MODIFICACIÓN===== ";
    	 		
    	 for(int i=0;i<Utilidades.convertirAEntero(forma.getRespaldoSoportes("numRegistros")+"");i++)
    	 {
    		 log+=	"\nCódigo tipo soporte: "+forma.getRespaldoSoportes("codtiposoporte_"+i)+
    		 		"\nNombre tipo soporte: "+forma.getRespaldoSoportes("nomtiposoporte_"+i)+
    		 		"\nActivo: ";
    		 		if (forma.getRespaldoSoportes("activo_"+i).toString().equals("true"))
    		 			log+="Si";
    		 		else
    		 			log+="No";
    	 }
    	 
    	 log+=	"\n\n====DATOS DESPUÉS DE MODIFICACIÓN====";
    	 for(int i=0;i<Utilidades.convertirAEntero(mapaOrganizado.get("numRegistros")+"");i++)
    	 {
    		 log+=	"\nCódigo tipo soporte: "+mapaOrganizado.get("codtiposoporte_"+i)+
    		 		"\nNombre tipo soporte: "+mapaOrganizado.get("nomtiposoporte_"+i)+
    		 		"\nActivo: ";
    		 		if (mapaOrganizado.get("activo_"+i).toString().equals("true"))
    		 			log+="Si";
    		 		else
    		 			log+="No";
    	 }
    	 
    	 log+="\n=====FIN DE LA MODIFICACIÓN=====\n\n\n";
    	 
    	 logger.info("\n\n\nSE GENERA EL LOG TIPO ARCHIVO EN: "+ConstantesBD.logFolderModuloFacturacion
					+ separador + ConstantesBD.logMantenimientoFacturacion
					+ separador + ConstantesBD.logSoportesFacturasNombre+"\n\n\n");
    	 
    	 LogsAxioma.enviarLog(ConstantesBD.logSoportesFacturasCodigo,log,tipoLog,usuario.getLoginUsuario());
    	 
    	 //**********FIN GENERACION LOG
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}

    /**
     * Organizar llaves del mapa
     * @param tiposSoportes
     * @param soportesFacturasMap
     * @return
     */
	private HashMap<String, Object> organizarMapa(HashMap<String, Object> tiposSoportes,HashMap<String, Object> soportesFacturasMap) {
		HashMap<String, Object> mapaOrganizado = tiposSoportes;
		
		/*int numTiposSoporte=0;
		
		for(int i=0; i<Utilidades.convertirAEntero(tiposSoportes.get("numRegistros")+""); i++){
    		if(soportesFacturasMap.containsKey("tipoSoporteActivo_"+i) && !soportesFacturasMap.get("tipoSoporteActivo_"+i).toString().equals("")){
    			mapaOrganizado.put("nombreTipoSoporte_"+numTiposSoporte, tiposSoportes.get("nombre_"+i));
    			mapaOrganizado.put("codigoTipoSoporte_"+numTiposSoporte, tiposSoportes.get("codigotiposoporte_"+i));
    			numTiposSoporte++;
    		}	
    		if(!soportesFacturasMap.containsKey("tipoSoporteActivo_"+i)){
    			if(!tiposSoportes.get("descpadre_"+i).toString().equals("")){
    				if(soportesFacturasMap.containsKey("tipoSoporteHijo_"+i) && !soportesFacturasMap.get("tipoSoporteHijo_"+i).toString().equals("")){
    					mapaOrganizado.put("nombreTipoSoporte_"+numTiposSoporte, tiposSoportes.get("descpadre_"+i)+" - "+tiposSoportes.get("nombre_"+i));
    	    			mapaOrganizado.put("codigoTipoSoporte_"+numTiposSoporte, tiposSoportes.get("codigotiposoporte_"+i));
    	    			numTiposSoporte++;
    				}
    			}
    		}
    	}*/
		
		if(soportesFacturasMap.containsKey("modificar"))
			mapaOrganizado.put("modificar", soportesFacturasMap.get("modificar"));
		mapaOrganizado.put("convenio", soportesFacturasMap.get("convenio"));
		mapaOrganizado.put("viaIngreso", soportesFacturasMap.get("viaIngreso"));
		
		return mapaOrganizado;
	}

	/**
     * Accion ingresar modificar
     * @param con
     * @param mapping
     * @param forma
     * @param request
     * @param usuario
     * @return
     */
	private ActionForward accionIngresarModificar(Connection con,ActionMapping mapping, SoportesFacturasForm forma,HttpServletRequest request, UsuarioBasico usuario) {
		forma.reset();
		forma.setFuncionalidad("ingresarModificar");
		// Cargar el contenido de los campos de selección de los convenios y las vias de ingreso
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		forma.setViasIngreso(Utilidades.obtenerViasIngreso(con, ConstantesBD.codigoViaIngresoAmbulatorios+","+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias));
		
		forma.setTiposSoportesMap(organizarTiposSoportesMap(SoportesFacturas.obtenerTiposSoporteXConvenio(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido)));
		
		Utilidades.imprimirMapa(forma.getTiposSoportesMap());
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}