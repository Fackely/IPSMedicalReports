package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.inventarios.FormatoJustServNoposForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.AuxiliarDiagnosticos;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.historiaClinica.Evoluciones;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.servinte.axioma.servicio.formatoJust.ComportamientoCampo;


/**
 * Clase para el manejo del workflow de FormatoJustServNopos
 * Date: 2008-03-25
 * @author garias@princetonsa.com
 */
public class FormatoJustServNoposAction extends Action 
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(FormatoJustServNoposAction.class);
	
	/**
	 * Metodo execute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try{

    		if(response==null);
    		if(form instanceof FormatoJustServNoposForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			FormatoJustServNoposForm forma = (FormatoJustServNoposForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n ESTADO JUSTIFICACIÓN SERVICIO NO POS ---->"+estado+"\n\n");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			if(estado == null)
    			{
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{
    				return this.accionEmpezar(forma, con, mapping, usuario, paciente, request);    			
    			}
    			
    			else if(estado.equals("empezar2"))
    			{
    				 this.accionEmpezar2(forma, con, mapping, usuario, paciente, request);    			
    			}
    			else if(estado.equals("empezarotravez")){
    				return this.accionEmpezarOtraVez(request, forma, mapping, usuario, con);
    			}
    			else if (estado.equals("mostrarSubCuentas"))
    			{
    				return this.accionMostrarSubCuentas(forma,mapping,con);
    			}
    			else if(estado.equals("guardar"))
    			{       		    
    				String retorno= "formato";
    		    	//Organizar Mapa
    				HashMap justificacion = new HashMap();
    		    	justificacion = organizarMapa(forma);
    		    	request.getSession().setAttribute(forma.getServicio()+"MAPAJUSSERV", justificacion);
    		    	request.getSession().setAttribute(forma.getServicio()+"MAPAJUSSERV_NORMAL", forma.getFormatoJustNoposMap());
    		    	
    		    	((HashMap)forma.getDiagnosticosDefinitivos()).put("DIAG_COMPLICACION", forma.getDiagnosticoComplicacion_1());
    		    	
    		    	request.getSession().setAttribute(forma.getServicio()+"MAPA_DIAG_DEF_NORMAL", forma.getDiagnosticosDefinitivos());
    				request.getSession().setAttribute(forma.getServicio()+"MAPA_DIAG_PRESUNTIVOS_NORMAL", forma.getDiagnosticosPresuntivos());
    		    	
    		    	
    				if (forma.getFuncionalidad().equals("ingresarJusPendiente") || forma.getFuncionalidad().equals("ingresarJusPendienteRango")){
    					errores=ingresarDeUnaJusPendiente(forma, con, mapping, usuario, paciente, request, errores,justificacion);
    				}else 
    				if (forma.getFuncionalidad().equals("consultarModificarP") || forma.getFuncionalidad().equals("consultarModificarR") || forma.getFuncionalidad().equals("consultarModificarEstadosP") || forma.getFuncionalidad().equals("consultarModificarEstadosR") || forma.getFuncionalidad().equals("consultarModificarTodoR") || forma.getFuncionalidad().equals("consultarModificarTodoP")){
    					errores=actualizarDeUnaJus(forma, con, mapping, usuario, paciente, request, errores,justificacion);
    				}else
    				if(forma.getFuncionalidad().equals("ordenAmbulatoriaServicio")||forma.getFuncionalidad().trim().equals("") || forma.getFuncionalidad().equals("modificarSolicitudCX")){
    					retorno="mensajefin";
    				}
    				
    				
    				
    				
    				if(!errores.isEmpty())
    				{
    					forma.setEstado("error");
    					saveErrors(request,errores);
    				}
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward(retorno);
    			}
    			else if(estado.equals("consultarJustificacionHistorica"))
    			{
    				return this.accionConsultarJustificacionHistorica(con, forma, mapping, request);
    			}
    			else if(estado.equals("imprimirJustificacionHistorica"))
    			{
    				return this.accionImprimirJustificacionHistorica(con, forma, mapping, request);
    			}else if(estado.equals("accionCampo")){
					return ComportamientoCampo.accionCampo(con, forma, mapping, request, usuario, paciente);
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
    
    private ActionForward accionImprimirJustificacionHistorica(Connection con, FormatoJustServNoposForm forma, ActionMapping mapping, HttpServletRequest request) {
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("justHistorica");
	}

	/**
     * Consultar Justificacion Historica
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @return
     */
    private ActionForward accionConsultarJustificacionHistorica(Connection con, FormatoJustServNoposForm forma, ActionMapping mapping, HttpServletRequest request) {
    	forma.setJustificacionHistorica(FormatoJustServNopos.consultarJustificacionHistorica(con, forma.getSolicitud(), forma.getServicio()));
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("justHistorica");
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param errores
	 * @param justificacion2 
     * @return
     */
    private ActionErrors actualizarDeUnaJus(FormatoJustServNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores, HashMap justificacion) {
    	FormatoJustServNopos fjsn = new FormatoJustServNopos();
    	
    	if(errores.isEmpty()){
    		String subcuentas[] = forma.getSubcuentasAux().toString().split(", ");
    		//Actualizamos la justificación No POS
    		fjsn.actualizarJustificacion(
            		con,
            		usuario.getCodigoInstitucionInt(), 
            		usuario.getLoginUsuario(), 
            		justificacion, 
            		Integer.parseInt(forma.getSolicitud()), 
            		Integer.parseInt(forma.getServicio()));
    	}
    	return errores;
	}

    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param errores
     * @param justificacion 
     * @return
     */
    @SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	private ActionErrors ingresarDeUnaJusPendiente(FormatoJustServNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores, HashMap justificacion) {
    	FormatoJustServNopos fjsn = new FormatoJustServNopos();
    	
    	HashMap responsables = UtilidadJustificacionPendienteArtServ.obtenerResponsablesJusPendiente(con, Integer.parseInt(forma.getSolicitudAux().toString()), Integer.parseInt(forma.getServicio().toString()), false);
    	String consecutivoSolicitud="";
		int codigoSolicitud=ConstantesBD.codigoNuncaValido;
		int aux;
    	
    	//Datos del primer responsable
    	justificacion.put(forma.getServicio().toString()+"_subcuenta", responsables.get("subcuenta_0")!=null?responsables.get("subcuenta_0"):"");
    	justificacion.put(forma.getServicio().toString()+"_cantidad", responsables.get("cantidad_0")!=null?responsables.get("cantidad_0"):"");
		
		if(errores.isEmpty()){
			//Ingresamos la justificación No POS
			consecutivoSolicitud = fjsn.ingresarJustificacion(
	        		con,
	        		usuario.getCodigoInstitucionInt(),
	        		usuario.getLoginUsuario(), 
	        		justificacion, 
	        		Integer.parseInt(forma.getSolicitudAux()),
	        		ConstantesBD.codigoNuncaValido,
	        		Integer.parseInt(forma.getServicio().toString()),
	        		usuario.getCodigoPersona());
			// Eliminamos el registro de la justificacion pendiente
			UtilidadJustificacionPendienteArtServ.eliminarJusNoposPendiente(con,Integer.parseInt(forma.getSolicitudAux().toString()),Integer.parseInt(forma.getServicio().toString()), false);
			// Ingresamos el registro para otro responsable si lo tiene
			for(int j=1; j<Utilidades.convertirAEntero(responsables.get("numRegistros").toString()); j++){
				fjsn.ingresarResponsable(con, consecutivoSolicitud, Utilidades.convertirAEntero(responsables.get("subcuenta_"+j)+""), Utilidades.convertirAEntero(responsables.get("cantidad_"+j)+""));
			}
		}	
		return errores;
	}

	/**
	 * 
	 * @param forma
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap organizarMapa(FormatoJustServNoposForm forma) {
		HashMap  mapa=new HashMap();
		int x=0;
		mapa.put(forma.getServicio()+"_servicio", forma.getServicio());
		HashMap formulario=forma.getFormatoJustNoposMap();
		HashMap mapaSecciones=(HashMap) formulario.get("mapasecciones");
		int numSecciones=Integer.parseInt(mapaSecciones.get("numRegistros").toString());
		for(int numSec=0;numSec<numSecciones;numSec++){
			String codigoSeccion=mapaSecciones.get("codigo_"+numSec).toString();
			int numCampos=Integer.parseInt((String)formulario.get("numRegistrosXSec_"+codigoSeccion));
			for(int numCampo=0;numCampo<numCampos;numCampo++){
				
				mapa.put(forma.getServicio()+"_requerido_"+codigoSeccion+"_"+numCampo,formulario.get("requerido_"+codigoSeccion+"_"+numCampo));
				mapa.put(forma.getServicio()+"_tipo_"+codigoSeccion+"_"+numCampo,formulario.get("tipo_"+codigoSeccion+"_"+numCampo));
				mapa.put(forma.getServicio()+"_mostrar_"+codigoSeccion+"_"+numCampo,formulario.get("mostrar_"+codigoSeccion+"_"+numCampo));
				mapa.put(forma.getServicio()+"_etiquetacampo_"+codigoSeccion+"_"+numCampo,formulario.get("etiquetacampo_"+codigoSeccion+"_"+numCampo));
				mapa.put(forma.getServicio()+"_etiquetaseccion_"+codigoSeccion+"_"+numCampo,formulario.get("etiquetaseccion_"+codigoSeccion));
				mapa.put(forma.getServicio()+"_codigocampo_"+codigoSeccion+"_"+numCampo,formulario.get("codigocampo_"+codigoSeccion+"_"+numCampo).toString());
				mapa.put(forma.getServicio()+"_codigoparametrizacion_"+codigoSeccion+"_"+numCampo,formulario.get("codigoparametrizacion_"+codigoSeccion+numCampo).toString());
				mapa.put(forma.getServicio()+"_valorcampo_"+codigoSeccion+"_"+numCampo,formulario.get("valorcampo_"+codigoSeccion+"_"+numCampo));
				
				if (forma.getFormatoJustNoposMap().containsKey("numHijos_"+codigoSeccion+"_"+numCampo)) {
					for (int h=0; h<Integer.parseInt(forma.getFormatoJustNoposMap("numHijos_"+codigoSeccion+"_"+numCampo).toString()); h++){
						mapa.put(forma.getServicio()+"_etiquetacampohijo_"+codigoSeccion+"_"+numCampo+"_"+h, forma.getFormatoJustNoposMap("etiquetacampohijo_"+codigoSeccion+"_"+numCampo+"_"+h));
						mapa.put(forma.getServicio()+"_valorcampohijo_"+codigoSeccion+"_"+numCampo+"_"+h, forma.getFormatoJustNoposMap("valorcampohijo_"+codigoSeccion+"_"+numCampo+"_"+h));
						mapa.put(forma.getServicio()+"_codigocampohijo_"+codigoSeccion+"_"+numCampo+"_"+h, forma.getFormatoJustNoposMap("codigocampohijo_"+codigoSeccion+"_"+numCampo+"_"+h));
						mapa.put(forma.getServicio()+"_codigoparamjuscampohijo_"+codigoSeccion+"_"+numCampo+"_"+h, forma.getFormatoJustNoposMap("codigoparamjuscampohijo_"+codigoSeccion+"_"+numCampo+"_"+h));	
					}
					mapa.put(forma.getServicio()+"_numhijos_",forma.getFormatoJustNoposMap().get("numHijos_"+codigoSeccion+"_"+numCampo));
				}
			}
			mapa.put(forma.getServicio()+"_numRegistrosXSec_"+codigoSeccion,formulario.get("numRegistrosXSec_"+codigoSeccion));
		}
		
		mapa.put(forma.getServicio()+"_mapasecciones",mapaSecciones);
		mapa.put(forma.getServicio()+"_descjustificacion", forma.getFormatoJustNoposMap("descjustificacion"));
		mapa.put(forma.getServicio()+"_descjustificacion", forma.getFormatoJustNoposMap("descjustificacion"));
		mapa.put(forma.getServicio()+"_etiguetaseccion_"+ConstantesBD.JusSeccionPaciente, forma.getFormatoJustNoposMap("etiquetaseccion_"+ConstantesBD.JusSeccionPaciente));
		//mapa.put(forma.getServicio()+"_numhijos_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo, forma.getFormatoJustNoposMap("numHijos_"+ConstantesBD.JusSeccionPaciente+"_"+ConstantesBD.JusOrdenCampoRiesgo));
		mapa.put(forma.getServicio()+"_diagnosticoPrincipal", forma.getDiagnosticoDefinitivo("principal"));
		mapa.put(forma.getServicio()+"_nombreConvenio", forma.getFormatoJustNoposMap("nombreConvenio"));
		mapa.put(forma.getServicio()+"_nombreTipoUsuario", forma.getFormatoJustNoposMap("nombreTipoUsuario"));
		mapa.put(forma.getServicio()+"_cantidad", forma.getFormatoJustNoposMap("cantidad"));
		mapa.put(forma.getServicio()+"_estado", forma.getFormatoJustNoposMap("estado"));
		mapa.put(forma.getServicio()+"_acronimo_estado", forma.getFormatoJustNoposMap("acronimo_estado"));
		mapa.put(forma.getServicio()+"_servicio", forma.getServicio());
		mapa.put(forma.getServicio()+"_convenio", forma.getConvenio());
		mapa.put(forma.getServicio()+"_subcuenta", forma.getSubcuenta());
		mapa.put(forma.getServicio()+"_numjustificacion", forma.getFormatoJustNoposMap("numjustificacion"));
		for (int h=0; h<forma.getNumDiagnosticosDefinitivos(); h++){
			if((forma.getDiagnosticoDefinitivo("checkbox_"+h)+"").equals("true")){ 
				x++;
				mapa.put(forma.getServicio()+"_diagnosticoRelacionado_"+h, forma.getDiagnosticoDefinitivo("relacionado_"+h));
			} 
		}	
		mapa.put(forma.getServicio()+"_numDiagRela", x);
		if (!forma.getDiagnosticoComplicacion_1().equals(""))
			mapa.put(forma.getServicio()+"_diagnosticoComplicacion", forma.getDiagnosticoComplicacion_1());
		
		
		return mapa;
	}


	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionMostrarSubCuentas(FormatoJustServNoposForm forma, ActionMapping mapping, Connection con)
    {
		forma.resetSubCuentas();
		
		if(forma.isProvieneOrdenAmbulatoria())
			forma.setSubCuentasMap(FormatoJustArtNopos.SubCuentas(con, forma.getCodigoOrden(), Utilidades.convertirAEntero(forma.getServicio()+""), false,true));
		else
			forma.setSubCuentasMap(FormatoJustArtNopos.SubCuentas(con, Integer.parseInt(forma.getSolicitud().toString()), Utilidades.convertirAEntero(forma.getServicio()+""), false,false));
		
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("selectSubCuenta");
    }

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	private ActionForward accionEmpezar(FormatoJustServNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws SQLException 
	{
		/*HashMap formatoJus=(HashMap) request.getSession().getAttribute(forma.getServicio()+"MAPAJUSSERV_NORMAL");
		boolean ingresado=false;
		if(formatoJus!=null){
			forma.setFormatoJustNoposMap(formatoJus);
			ingresado=true;
		}
		
		HashMap diag=(HashMap)request.getSession().getAttribute(forma.getServicio()+"MAPA_DIAG_DEF_NORMAL");
		if(diag!=null){
			forma.setDiagnosticosDefinitivos(diag);
		}
		
		if(diag!=null&&diag.get("DIAG_COMPLICACION")!=null){
			forma.setDiagnosticoComplicacion_1((String) diag.get("DIAG_COMPLICACION"));
		}
		
		diag=(HashMap)request.getSession().getAttribute(forma.getServicio()+"MAPA_DIAG_PRESUNTIVOS_NORMAL");
		if(diag!=null){
			forma.setDiagnosticosPresuntivos(diag);
		}*/
		/*HashMap formatoJus=(HashMap) request.getSession().getAttribute("MAPAJUS");
		boolean ingresado=false;
		if(formatoJus!=null)
		{
			if(formatoJus.containsKey(forma.getNumServicio()+"_mapajustservform"))
			{
				ingresado=true;
				forma.setFormatoJustNoposMap((HashMap)formatoJus.get(forma.getNumServicio()+"_mapajustservform"));
			}
			//request.getSession().removeAttribute("MAPAJUSSERVFORM");
		}*/
		
		HashMap aux = new HashMap();
		FormatoJustServNopos mundo = new FormatoJustServNopos();
		mundo.setServicio(forma.getServicio());
		mundo.setCantServicio(forma.getCantServicio());
		
		forma.setNumJus(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, usuario.getCodigoInstitucionInt()));
		logger.info("Consecutivo Actual - "+forma.getNumJus());
		
		// Preguntamos si se tiene el codigo de solicitud o el codigo de la Orden; si se tiene se verifica que se tenga el codigo de servicio 
		if (forma.getSolicitud().equals("") && forma.getCodigoOrden()==ConstantesBD.codigoNuncaValido){
			logger.info("NO LLEGA SOLICITUD");
			if(!forma.getServicio().equals("")){
				
				//if(!ingresado)
					forma.setFormatoJustNoposMap(mundo.consultar(con,mundo,paciente,usuario,true));
				
				forma.getFormatoJustNoposMap().put("estado", "Justificado");
				forma.getFormatoJustNoposMap().put("cantidad", forma.getCantServicio());
				forma.getFormatoJustNoposMap().put("subcuenta", forma.getSubcuenta());
				
				//logger.info("Recordar -> "+forma.getRecordar());
				
				/*if(forma.getRecordar().equals("false"))
				{*/
					forma.resetDiag();
					preCargarDiagnosticos(con, paciente.getCodigoCuenta(), forma, request);
					logger.info("num diagnosticos definitivos -> "+forma.getNumDiagnosticosDefinitivos());
					logger.info("diag complicacion -> "+forma.getDiagnosticoComplicacion_1());
					logger.info("diag definitivos -> "+forma.getDiagnosticosDefinitivos());
					
					logger.info("FECHA SOLICITUD - "+forma.getFecha_solicitud());
					
					if(request.getParameter("fecha_solicitud")!=null)
						forma.setFormatoJustNoposMap("fecha", request.getParameter("fecha_solicitud"));
					
					logger.info("FECHA SOLICITUD - "+forma.getFecha_solicitud());
				//}
				
			}
		}
		else {
			logger.info("LLEGA SOLICITUD/ORDEN AMBULATORIA Y SERVICIO");
			logger.info("Subcuenta --- "+forma.getSubcuenta());
			mundo.setSolicitud(forma.getSolicitud());
			mundo.setSubcuenta(forma.getSubcuenta());
			//Datos para consultar la Justificacion desde Ordenes Ambulatorias
			mundo.setCodigoOrden(forma.getCodigoOrden());
			mundo.setProvieneOrdenAmbulatoria(forma.isProvieneOrdenAmbulatoria());
			
			forma.setFormatoJustNoposMap(mundo.consultar(con,mundo,paciente,usuario,false));
			
			if(forma.getFormatoJustNoposMap().get("esOrdenAmbulatoria")!= null){
				forma.setProvieneOrdenAmbulatoria(UtilidadTexto.getBoolean(forma.getFormatoJustNoposMap().get("esOrdenAmbulatoria")));
			}else{
				forma.setProvieneOrdenAmbulatoria(false);
			}
			
			
			forma.setConvenio(forma.getFormatoJustNoposMap("convenio")+"");
			
			forma.setFormatoJustNoposMap("cantservicio",forma.getFormatoJustNoposMap("cantidad"));
			
			// consultar diagnosticos
			
			aux=mundo.consultarDiagnosticos(con, Integer.parseInt(forma.getFormatoJustNoposMap("numjustificacion").toString()));
			forma.setDiagnosticoComplicacion_1(aux.get("diagnosticoComplicacion").toString());
			forma.setDiagnosticosDefinitivos((HashMap)aux.get("diagnosticosDefinitivos"));
			forma.setNumDiagnosticosDefinitivos(Integer.parseInt(aux.get("numDiagnosticosDefinitivos").toString()));
			
			logger.info("FUNCIONALIDAD-----------------"+forma.getFuncionalidad());
		}
		
		logger.info("CONVENIO - "+forma.getConvenio());
		if (forma.getConvenio()!=null && !forma.getConvenio().equals("")){
			forma.getFormatoJustNoposMap().put("nombreTipoUsuario", UtilidadesFacturacion.consultarNombreTipoRegimen(con, Integer.parseInt(forma.getConvenio().toString())));
			forma.getFormatoJustNoposMap().put("nombreConvenio", UtilidadesFacturacion.consultarNombreConvenio(con, Integer.parseInt(forma.getConvenio().toString())));
			
		}
		
		try{
			ComportamientoCampo.cargarValoresDefectoAccionesCampoServicios(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		
		forma.setCodigoOrden(ConstantesBD.codigoNuncaValido);
		
		if (forma.getImprimir().equals(ConstantesBD.acronimoSi)){
			forma.setImprimir(ConstantesBD.acronimoNo);
			forma.setSubCuentas("");
			return mapping.findForward("imprimir");
		}
		else{
			forma.setImprimir(ConstantesBD.acronimoNo);
			return mapping.findForward("formato");
		}
	}	
	
	
	
	
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	@SuppressWarnings({ "unchecked", "static-access", "rawtypes" })
	private void accionEmpezar2(FormatoJustServNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws SQLException 
	{
		/*HashMap formatoJus=(HashMap) request.getSession().getAttribute(forma.getServicio()+"MAPAJUSSERV_NORMAL");
		boolean ingresado=false;
		if(formatoJus!=null){
			forma.setFormatoJustNoposMap(formatoJus);
			ingresado=true;
		}
		
		HashMap diag=(HashMap)request.getSession().getAttribute(forma.getServicio()+"MAPA_DIAG_DEF_NORMAL");
		if(diag!=null){
			forma.setDiagnosticosDefinitivos(diag);
		}
		
		if(diag!=null&&diag.get("DIAG_COMPLICACION")!=null){
			forma.setDiagnosticoComplicacion_1((String) diag.get("DIAG_COMPLICACION"));
		}
		
		diag=(HashMap)request.getSession().getAttribute(forma.getServicio()+"MAPA_DIAG_PRESUNTIVOS_NORMAL");
		if(diag!=null){
			forma.setDiagnosticosPresuntivos(diag);
		}*/
		/*HashMap formatoJus=(HashMap) request.getSession().getAttribute("MAPAJUS");
		boolean ingresado=false;
		if(formatoJus!=null)
		{
			if(formatoJus.containsKey(forma.getNumServicio()+"_mapajustservform"))
			{
				ingresado=true;
				forma.setFormatoJustNoposMap((HashMap)formatoJus.get(forma.getNumServicio()+"_mapajustservform"));
			}
			//request.getSession().removeAttribute("MAPAJUSSERVFORM");
		}*/
		
		HashMap aux = new HashMap();
		FormatoJustServNopos mundo = new FormatoJustServNopos();
		mundo.setServicio(forma.getServicio());
		mundo.setCantServicio(forma.getCantServicio());
		
		forma.setNumJus(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, usuario.getCodigoInstitucionInt()));
		logger.info("Consecutivo Actual - "+forma.getNumJus());
		
		// Preguntamos si se tiene el codigo de solicitud; si se tiene se verifica que se tenga el codigo de servicio 
		if (forma.getSolicitud().equals("")){
			logger.info("NO LLEGA SOLICITUD");
			if(!forma.getServicio().equals("")){
				
				//if(!ingresado)
					forma.setFormatoJustNoposMap(mundo.consultar(con,mundo,paciente,usuario,true));
				
				forma.getFormatoJustNoposMap().put("estado", "Justificado");
				forma.getFormatoJustNoposMap().put("cantidad", forma.getCantServicio());
				forma.getFormatoJustNoposMap().put("subcuenta", forma.getSubcuenta());
				
				//logger.info("Recordar -> "+forma.getRecordar());
				
				/*if(forma.getRecordar().equals("false"))
				{*/
					forma.resetDiag();
					preCargarDiagnosticos(con, paciente.getCodigoCuenta(), forma, request);
					logger.info("num diagnosticos definitivos -> "+forma.getNumDiagnosticosDefinitivos());
					logger.info("diag complicacion -> "+forma.getDiagnosticoComplicacion_1());
					logger.info("diag definitivos -> "+forma.getDiagnosticosDefinitivos());
					
					logger.info("FECHA SOLICITUD - "+forma.getFecha_solicitud());
					
					if(request.getParameter("fecha_solicitud")!=null)
						forma.setFormatoJustNoposMap("fecha", request.getParameter("fecha_solicitud"));
					
					logger.info("FECHA SOLICITUD - "+forma.getFecha_solicitud());
				//}
				
			}
		}
		else {
			logger.info("LLEGA SOLICITUD Y SERVICIO");
			logger.info("Subcuenta --- "+forma.getSubcuenta());
			mundo.setSolicitud(forma.getSolicitud());
			mundo.setSubcuenta(forma.getSubcuenta());
			
			forma.setFormatoJustNoposMap(mundo.consultar(con,mundo,paciente,usuario,false));
			
			if(forma.getFormatoJustNoposMap().get("esOrdenAmbulatoria")!= null){
				forma.setProvieneOrdenAmbulatoria(UtilidadTexto.getBoolean(forma.getFormatoJustNoposMap().get("esOrdenAmbulatoria")));
			}else{
				forma.setProvieneOrdenAmbulatoria(false);
			}
			
			
			forma.setConvenio(forma.getFormatoJustNoposMap("convenio")+"");
			
			forma.setFormatoJustNoposMap("cantservicio",forma.getFormatoJustNoposMap("cantidad"));
			
			// consultar diagnosticos
			
			aux=mundo.consultarDiagnosticos(con, Integer.parseInt(forma.getFormatoJustNoposMap("numjustificacion").toString()));
			forma.setDiagnosticoComplicacion_1(aux.get("diagnosticoComplicacion").toString());
			forma.setDiagnosticosDefinitivos((HashMap)aux.get("diagnosticosDefinitivos"));
			forma.setNumDiagnosticosDefinitivos(Integer.parseInt(aux.get("numDiagnosticosDefinitivos").toString()));
			
			logger.info("FUNCIONALIDAD-----------------"+forma.getFuncionalidad());
		}
		
		logger.info("CONVENIO - "+forma.getConvenio());
		if (forma.getConvenio()!=null && !forma.getConvenio().equals("")){
			forma.getFormatoJustNoposMap().put("nombreTipoUsuario", UtilidadesFacturacion.consultarNombreTipoRegimen(con, Integer.parseInt(forma.getConvenio().toString())));
			forma.getFormatoJustNoposMap().put("nombreConvenio", UtilidadesFacturacion.consultarNombreConvenio(con, Integer.parseInt(forma.getConvenio().toString())));
			
		}
		
		try{
			ComportamientoCampo.cargarValoresDefectoAccionesCampoServicios(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		
	
	}	
	
	
	
	
	
	
	public ActionForward accionEmpezarOtraVez(HttpServletRequest request, FormatoJustServNoposForm forma, ActionMapping mapping, UsuarioBasico usuario, Connection con){
		HashMap formatoJus=(HashMap) request.getSession().getAttribute(forma.getServicio()+"MAPAJUSSERV_NORMAL");
		//boolean ingresado=false;
		if(formatoJus!=null){
			forma.setFormatoJustNoposMap(formatoJus);
			//ingresado=true;
		}
		
		HashMap diag=(HashMap)request.getSession().getAttribute(forma.getServicio()+"MAPA_DIAG_DEF_NORMAL");
		if(diag!=null){
			forma.setDiagnosticosDefinitivos(diag);
		}
		
		if(diag!=null&&diag.get("DIAG_COMPLICACION")!=null){
			forma.setDiagnosticoComplicacion_1((String) diag.get("DIAG_COMPLICACION"));
		}
		
		diag=(HashMap)request.getSession().getAttribute(forma.getServicio()+"MAPA_DIAG_PRESUNTIVOS_NORMAL");
		if(diag!=null){
			forma.setDiagnosticosPresuntivos(diag);
		}
		
		
		HashMap aux = new HashMap();
		FormatoJustServNopos mundo = new FormatoJustServNopos();
		mundo.setServicio(forma.getServicio());
		mundo.setCantServicio(forma.getCantServicio());
		
		forma.setNumJus(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSServicios, usuario.getCodigoInstitucionInt()));
		logger.info("Consecutivo Actual - "+forma.getNumJus());
		
		forma.getFormatoJustNoposMap().put("estado", "Justificado");
		forma.getFormatoJustNoposMap().put("cantidad", forma.getCantServicio());
		forma.getFormatoJustNoposMap().put("subcuenta", forma.getSubcuenta());
		
		return mapping.findForward("formato");
	}
	
	/**
	 * Este método precarga los diagnosticos de la evolución (Diagnosticos
	 * sugeridos) en la forma de Evolucion (EvolucionForm), utilizando la
	 * clase AuxiliarDiagnosticos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroCuenta Número de la cuenta a la que pertenece esta
	 * evolución
	 * @param bean Objeto de tipo FormatoJustServNoposForm, donde se almaceran los
	 * diagnosticos
	 * @param request Objeto request donde se van a poner el número de
	 * diagnosticos presuntivos y definitivos que se cargaron
	 * @throws SQLException
	 */
	public void preCargarDiagnosticos (Connection con, int numeroCuenta, FormatoJustServNoposForm bean, HttpServletRequest request) throws SQLException
	{
		int i;
		//Inicializamos un objeto AuxiliarDiagnosticos para
		//saber que debemos proponer
		AuxiliarDiagnosticos auxiliarDiagnosticosEvolucion=new AuxiliarDiagnosticos(numeroCuenta, true);
		auxiliarDiagnosticosEvolucion.cargar(con);
		//Definimos los tamaños
		bean.setNumDiagnosticosDefinitivos(auxiliarDiagnosticosEvolucion.getNumeroDiagnosticosDefinitivos());
		request.setAttribute("numDiagnosticosDefinitivos", ""+bean.getNumDiagnosticosDefinitivos());
		bean.setNumDiagnosticosPresuntivos(auxiliarDiagnosticosEvolucion.getNumeroDiagnosticosPresuntivos());
		//Este atributo me permite saber si lo debo dejar chequear/deschequear
		bean.setNumDiagnosticosPresuntivosOriginal(auxiliarDiagnosticosEvolucion.getNumeroDiagnosticosPresuntivos());
		request.setAttribute("numDiagnosticosPresuntivos", ""+bean.getNumDiagnosticosPresuntivos());
		
		//Si es el caso precargamos el diagnostico de complicación
		
		Diagnostico diagnosticoComplicacion=auxiliarDiagnosticosEvolucion.getDiagnosticoComplicacion();
		
		logger.info("-------------------------------7-7-7- "+ diagnosticoComplicacion.getNombre());
		
		//Solo lo vamos a hacer si es un diagnostico no vacio (Tipo Cie !=0)
		if (diagnosticoComplicacion.getTipoCIE()!=0)
		{
			bean.setDiagnosticoComplicacion_1(diagnosticoComplicacion.getAcronimo() +ConstantesBD.separadorSplit+ diagnosticoComplicacion.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoComplicacion.getNombre());
		}
		
		boolean centinelaAsignoPrincipal=false;
		int indexRel=0;
		//Vamos a llenar el bean con los datos de estos diagnosticos
		for (i=0;i<bean.getNumDiagnosticosPresuntivos();i++)
		{
			Diagnostico diagnosticoTemporal=auxiliarDiagnosticosEvolucion.getDiagnosticoPresuntivo(i);
			
			//aqui se carga solo el principal
			if(!centinelaAsignoPrincipal && diagnosticoTemporal.isPrincipal())
			{
				bean.setDiagnosticoPresuntivo("principal", diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				centinelaAsignoPrincipal=true;
			}	
			else
			{	
				bean.setDiagnosticoPresuntivo("relacionado_" + indexRel, diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				bean.setDiagnosticoPresuntivo("checkbox_"+indexRel, "true");
				if(!bean.getDiagnosticosDefinitivos().containsKey("seleccionados"))
					bean.setDiagnosticoDefinitivo("seleccionados", "'"+diagnosticoTemporal.getAcronimo()+"'");
				else
					bean.setDiagnosticoDefinitivo("seleccionados", bean.getDiagnosticoDefinitivo("seleccionados")+",'"+diagnosticoTemporal.getAcronimo()+"'");
				indexRel++;
			}	
		}
		
		centinelaAsignoPrincipal=false;
		indexRel=0;
		
		for (i=0;i<bean.getNumDiagnosticosDefinitivos();i++)
		{
			Diagnostico diagnosticoTemporal=auxiliarDiagnosticosEvolucion.getDiagnosticoDefinitivo(i);
			//aqui se carga solo el principal
			if(!centinelaAsignoPrincipal && diagnosticoTemporal.isPrincipal())
			{
				bean.setDiagnosticoDefinitivo("principal", diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				centinelaAsignoPrincipal=true;
			}	
			else
			{
				bean.setDiagnosticoDefinitivo("relacionado_" +indexRel, diagnosticoTemporal.getAcronimo() + ConstantesBD.separadorSplit+ diagnosticoTemporal.getTipoCIE() + ConstantesBD.separadorSplit + diagnosticoTemporal.getNombre());
				bean.setDiagnosticoDefinitivo("checkbox_"+indexRel, "true");
				if(!bean.getDiagnosticosDefinitivos().containsKey("seleccionados"))
					bean.setDiagnosticoDefinitivo("seleccionados", "'"+diagnosticoTemporal.getAcronimo()+"'");
				else
					bean.setDiagnosticoDefinitivo("seleccionados", bean.getDiagnosticoDefinitivo("seleccionados")+",'"+diagnosticoTemporal.getAcronimo()+"'");
				indexRel++;
			}	
		}
		
		//el numero de diagnostico definitivo son solo los que pertenecen a los relacionados
		if(bean.getNumDiagnosticosDefinitivos()>0)
			bean.setNumDiagnosticosDefinitivos(bean.getNumDiagnosticosDefinitivos()-1);
		if(bean.getNumDiagnosticosPresuntivos()>0)
			bean.setNumDiagnosticosPresuntivos(bean.getNumDiagnosticosPresuntivos()-1);
	}
	
	/**
	 * Sin uso pendiente por ajustar ....
	 * @param con
	 * @param numeroCuenta
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param paciente
	 */
	public void preCargarDiagnosticos2 (Connection con, int numeroCuenta, FormatoJustServNoposForm forma, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente){
		Evoluciones evoluciones= new Evoluciones();
		int codigoEvolucion = evoluciones.obtenerCodigoUltimaEvolucion(con, paciente.getCodigoCuenta());
		//if(codigoEvolucion>0)
		//{
			//forma.setCodigoUltimaEvolucion(codigoEvolucion);
			//this.cargarDatosUltimaEvolucion(con, forma, evoluciones, usuario, paciente);
		//}
		//else
		//{	
			//this.cargarDiagnosticosValoracion(con, forma, usuario, paciente);
		//}	
	}	
}
