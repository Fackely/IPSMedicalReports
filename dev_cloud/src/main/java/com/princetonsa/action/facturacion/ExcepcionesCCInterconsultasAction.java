package com.princetonsa.action.facturacion;
import java.sql.Connection;
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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ExcepcionesCCInterconsultasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ExcepcionesCCInterconsultas;

public class ExcepcionesCCInterconsultasAction extends Action
{
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(ExcepcionesCCInterconsultasAction.class);
	
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
    		if(form instanceof ExcepcionesCCInterconsultasForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			ExcepcionesCCInterconsultasForm forma = (ExcepcionesCCInterconsultasForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado No Valido Dentro Del Flujo de la Parametrización Excepciones CC Interconsultas (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			/*------------------------------
    			 * 		ESTADO > empezar
    			 *-------------------------------*/
    			if(estado.equals("empezar"))
    			{   
    				forma.reset();
    				forma.setCentroAtencionSeleccionado(usuario.getCodigoCentroAtencion()+"");
    				forma.setCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
    				forma.setListadoXCentroAtencion(ExcepcionesCCInterconsultas.consultarXCentroAtencion(con, forma.getCentroAtencionSeleccionado()));
    				forma.setCentrosCostoEjecutan(ExcepcionesCCInterconsultas.obtenerCentrosCostoEjecutan(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    				forma.setCentrosCosto(ExcepcionesCCInterconsultas.obtenerCentrosCosto(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    				forma.setMedicos(ExcepcionesCCInterconsultas.obtenerMedicos(con,usuario.getCodigoInstitucionInt()));
    				forma.setDatosIngresoExcepcion(new HashMap<String, Object>());
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}

    			/*------------------------------
    			 * 		ESTADO > buscarExcepciones
    			 *-------------------------------*/
    			if(estado.equals("buscarExcepciones"))
    			{   
    				forma.setMensaje("");
    				forma.setListadoXCentroAtencion(ExcepcionesCCInterconsultas.consultarXCentroAtencion(con, forma.getCentroAtencionSeleccionado()));
    				forma.setCentrosCostoEjecutan(ExcepcionesCCInterconsultas.obtenerCentrosCostoEjecutan(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    				forma.setCentrosCosto(ExcepcionesCCInterconsultas.obtenerCentrosCosto(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    				forma.setMedicos(ExcepcionesCCInterconsultas.obtenerMedicos(con,usuario.getCodigoInstitucionInt()));
    				forma.setDatosIngresoExcepcion(new HashMap<String, Object>());
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}

    			/*------------------------------
    			 * 		ESTADO > nuevaExcepcion
    			 *-------------------------------*/
    			if(estado.equals("nuevaExcepcion"))
    			{   
    				forma.setMensaje("");
    				forma.setCentrosCostoEjecutan(ExcepcionesCCInterconsultas.obtenerCentrosCostoEjecutan(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    				forma.setCentrosCosto(ExcepcionesCCInterconsultas.obtenerCentrosCosto(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    				forma.setMedicos(ExcepcionesCCInterconsultas.obtenerMedicos(con,usuario.getCodigoInstitucionInt()));
    				forma.setDatosIngresoExcepcion(new HashMap<String, Object>());
    				forma.setIndicativo(true);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}

    			/*------------------------------
    			 * 		ESTADO > ordenar
    			 *-------------------------------*/
    			if (estado.equals("ordenar")) {
    				forma.setMensaje("");
    				UtilidadBD.closeConnection(con);
    				return accionOrdenarExcepciones(con, forma, usuario,request, mapping);
    			}

    			/*------------------------------
    			 * 		ESTADO > guardarExcepcion
    			 *-------------------------------*/
    			if (estado.equals("guardarExcepcion")) 
    			{
    				Utilidades.imprimirMapa(forma.getListadoXCentroAtencion());
    				Utilidades.imprimirMapa(forma.getDatosIngresoExcepcion());
    				logger.info("centro de atencion"+forma.getCentroAtencionSeleccionado());

    				if(ExcepcionesCCInterconsultas.guardarExcepcion(con, forma.getDatosIngresoExcepcion(),usuario.getLoginUsuario(),usuario.getCodigoInstitucion(),forma.getCentroAtencionSeleccionado()))
    				{
    					forma.setMensaje("¡EXCEPCIÓN GUARDADA EXITOSAMENTE!");
    					forma.setListadoXCentroAtencion(ExcepcionesCCInterconsultas.consultarXCentroAtencion(con, forma.getCentroAtencionSeleccionado()));
    					forma.setEstado("buscarExcepciones");
    					forma.setCentrosCostoEjecutan(ExcepcionesCCInterconsultas.obtenerCentrosCostoEjecutan(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    					forma.setCentrosCosto(ExcepcionesCCInterconsultas.obtenerCentrosCosto(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    					forma.setMedicos(ExcepcionesCCInterconsultas.obtenerMedicos(con,usuario.getCodigoInstitucionInt()));
    					forma.setCodigosServiciosInsertados("");
    					forma.setDatosIngresoExcepcion(new HashMap<String, Object>());	
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("principal");
    				}
    			}
    			/*------------------------------
    			 * 		ESTADO > buscarServicio
    			 *-------------------------------*/
    			if (estado.equals("buscarServicio")) {
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}
    			/*------------------------------
    			 * 		ESTADO > eliminarExcepcion
    			 *-------------------------------*/
    			if (estado.equals("eliminarExcepcion")) 
    			{
    				//**************************Registros que se eliminan, se guardan para usarlos en el log
    				String codigo=forma.getListadoXCentroAtencion("codigo_"+forma.getIndice()).toString();
    				String centroAtencion=forma.getListadoXCentroAtencion("centroatencion_"+forma.getIndice()).toString();
    				String centrocostosolicita=forma.getListadoXCentroAtencion("centrocostosolicita_"+forma.getIndice()).toString();
    				String servicio=forma.getListadoXCentroAtencion("servicio_"+forma.getIndice()).toString();
    				String medico=forma.getListadoXCentroAtencion("medico_"+forma.getIndice()).toString();
    				String centrocostoejecuta=forma.getListadoXCentroAtencion("centrocostoejecuta_"+forma.getIndice()).toString();
    				String institucion=forma.getListadoXCentroAtencion("institucion_"+forma.getIndice()).toString();
    				String fechamodifica=forma.getListadoXCentroAtencion("fechamodifica_"+forma.getIndice()).toString();
    				String horamodifica=forma.getListadoXCentroAtencion("horamodifica_"+forma.getIndice()).toString();
    				String usuariomodifica=forma.getListadoXCentroAtencion("usuariomodifica_"+forma.getIndice()).toString();

    				if (ExcepcionesCCInterconsultas.eliminarExcepcion(con,Utilidades.convertirAEntero(forma.getListadoXCentroAtencion("codigo_"+forma.getIndice())+"")))
    				{

    					//*******************GENERACION DEL LOG
    					String log = "";
    					int tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
    					String separador = System.getProperty("file.separator");
    					log = "\n  ============LOG TIPO ARCHIVO=========== \n" +
    					"\nFecha: "+UtilidadFecha.getFechaActual()+
    					"\nHora: "+UtilidadFecha.getHoraActual()+
    					"\nUsuario: "+usuario.getLoginUsuario()+
    					"\n\nREGISTRO ELIMINADO EN exc_cc_interconsultas: "+
    					"\nCODIGO: "+codigo+
    					"\nCentro Atencion: "+centroAtencion+
    					"\nCentro de Costo Solicita: "+centrocostosolicita+
    					"\nServicio: "+servicio+
    					"\nMedico Ejecuta: "+medico+
    					"\nCentro Costo Ejecuta: "+centrocostoejecuta+
    					"\nInstitucion: "+institucion+
    					"\nFecha Modifica: "+fechamodifica+
    					"\nHora Modifica: "+horamodifica+
    					"\nUsuario Modifica: "+usuariomodifica;

    					logger.info("\n\n\nSE GENERA EL LOG TIPO ARCHIVO EN: "+ConstantesBD.logFolderModuloFacturacion
    							+ separador + ConstantesBD.logMantenimientoFacturacion
    							+ separador + ConstantesBD.logServicios
    							+ separador + ConstantesBD.logEliminarExceCCInterconsultasNombre+"\n\n\n");


    					LogsAxioma.enviarLog(ConstantesBD.logEliminarExceCCInterconsultasCodigo,log,tipoLog,usuario.getLoginUsuario());
    					//*******************FIN GENERACION DEL LOG

    					forma.setMensaje("EXCEPCIÓN ELIMINADA CORRECTAMENTE");
    					forma.setListadoXCentroAtencion(ExcepcionesCCInterconsultas.consultarXCentroAtencion(con, forma.getCentroAtencionSeleccionado()));
    					forma.setEstado("buscarExcepciones");
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("principal");
    				}
    			}

    			/*------------------------------
    			 * 		ESTADO > editarExcepcion
    			 *-------------------------------*/
    			if (estado.equals("editarExcepcion")) {
    				forma.setDatosIngresoExcepcion("codigo", forma.getListadoXCentroAtencion("codigo_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("centroCostoSolicita", forma.getListadoXCentroAtencion("centrocostosolicita_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("centroCostoEjecuta", forma.getListadoXCentroAtencion("centrocostoejecuta_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("servicio", forma.getListadoXCentroAtencion("servicio_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("medico", forma.getListadoXCentroAtencion("medico_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("especialidadservicio", forma.getListadoXCentroAtencion("especialidadservicio_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("descrefservicio", forma.getListadoXCentroAtencion("descrefservicio_"+forma.getIndice()));
    				forma.setDatosIngresoExcepcion("descripciontotalservicio",forma.getListadoXCentroAtencion("especialidadservicio_"+forma.getIndice()).toString()+"-"+
    						forma.getListadoXCentroAtencion("servicio_"+forma.getIndice()).toString()+" "+
    						forma.getListadoXCentroAtencion("descrefservicio_"+forma.getIndice()).toString());
    				forma.setDatosIngresoExcepcion("codigocups", forma.getListadoXCentroAtencion("codigocups_"+forma.getIndice()).toString());
    				Utilidades.imprimirMapa(forma.getDatosIngresoExcepcion());
    				forma.setIndicativo(false);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");

    			}

    			/*------------------------------
    			 * 		ESTADO > editarExcepcionConfirmar
    			 *-------------------------------*/
    			if (estado.equals("editarExcepcionConfirmar")) {
    				if(ExcepcionesCCInterconsultas.modificarExcepcion(con, forma.getDatosIngresoExcepcion(),usuario.getLoginUsuario(),usuario.getCodigoInstitucion(),forma.getCentroAtencionSeleccionado()))
    				{
    					forma.setMensaje("¡EXCEPCIÓN MODIFICADA EXITOSAMENTE!");
    					forma.setListadoXCentroAtencion(ExcepcionesCCInterconsultas.consultarXCentroAtencion(con, forma.getCentroAtencionSeleccionado()));
    					forma.setEstado("buscarExcepciones");
    					forma.setCentrosCostoEjecutan(ExcepcionesCCInterconsultas.obtenerCentrosCostoEjecutan(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    					forma.setCentrosCosto(ExcepcionesCCInterconsultas.obtenerCentrosCosto(con,forma.getCentroAtencionSeleccionado(),ConstantesBD.codigoTipoAreaDirecto,usuario.getCodigoInstitucionInt()));
    					forma.setMedicos(ExcepcionesCCInterconsultas.obtenerMedicos(con,usuario.getCodigoInstitucionInt()));
    					forma.setCodigosServiciosInsertados("");
    					forma.setDatosIngresoExcepcion(new HashMap<String, Object>());
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("principal");
    				}
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

	private ActionForward accionOrdenarExcepciones(Connection con,ExcepcionesCCInterconsultasForm forma, UsuarioBasico usuario,HttpServletRequest request, ActionMapping mapping) 
	{
		int numReg = Utilidades.convertirAEntero(forma.getListadoXCentroAtencion("numRegistros")+"");
		String consulta = forma.getListadoXCentroAtencion("consultasql").toString();
		Utilidades.imprimirMapa(forma.getListadoXCentroAtencion());
    	String[] indicesExcepcionesMap = {"codigo_","centroatencion_","centrocostosolicita_","servicio_","medico_","centrocostoejecuta_","institucion_","fechamodifica_","horamodifica_","usuariomodifica_","nombrecentrocostosolicita_", "nombrecentrocostoejecuta_", "nombremedico_","nombreespecialidadservicio_","descrefservicio_", "especialidadservicio_","idcentrocostoejecuta_","idcentrocostosolicita_","codigocups_",""};
    	forma.setListadoXCentroAtencion(Listado.ordenarMapa(indicesExcepcionesMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoXCentroAtencion(), numReg));
    	Utilidades.imprimirMapa(forma.getListadoXCentroAtencion());
    	forma.setUltimoPatron(forma.getPatronOrdenar());
    	forma.setListadoXCentroAtencion("numRegistros",numReg);
    	forma.setListadoXCentroAtencion("consultasql",consulta);
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}