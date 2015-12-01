package com.princetonsa.action.glosas;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.glosas.ConceptosGeneralesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConceptosGenerales;

/**
 * @author Mauricio Jaramillo
 * Fecha: Septiembre de 2008
 */

public class ConceptosGeneralesAction extends Action {

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ConceptosGeneralesAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof ConceptosGeneralesForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConceptosGeneralesForm forma = (ConceptosGeneralesForm) form;
				ConceptosGenerales mundo = new ConceptosGenerales();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				forma.setMensaje(new ResultadoBoolean(false));
				String estado = forma.getEstado();
				logger.warn("[ConceptosGeneralesForm]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mundo, usuario, mapping, request);
				}
				else if(estado.equals("ordenar"))
				{
					return accionOrdenar(con, forma, mapping);
				}
				else if(estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					return accionGuardar(con, forma, mundo, usuario, mapping, request);
				}
				else if(estado.equals("cargarConceptoGeneral"))
				{
					return accionCargarConcepto(con, forma, mundo, usuario, mapping, request);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con, forma, mundo, usuario, mapping, request);
				}
				else if(estado.equals("guardarModificar"))
				{
					return accionGuardarModificar(con, forma, mundo, usuario, mapping, request);
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConceptosGeneralesForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Método que carga en la parte superior el Concepto General de Glosas seleccionado, el cual válida si el Concepto 
	 * General está siendo utilizado en otra parte, con el fin de permitir modificar los campos correspondientes
	 * 
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionCargarConcepto(Connection con, ConceptosGeneralesForm forma, ConceptosGenerales mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {

		logger.info("===>Posición Seleccionada: "+forma.getPosicion());

		//Llenamos en un indice del mapa el consecutivo seleccionado con el fin de validar si sufrio modificaciones o no al momento de guardar
		forma.setConceptosGenerales("consecutivoSeleccionado", forma.getConceptosGenerales("consecutivo_"+forma.getPosicion())+"");
		forma.setConceptosGenerales("descripcionSeleccionado", forma.getConceptosGenerales("descripcion_"+forma.getPosicion())+"");
		forma.setConceptosGenerales("tipoSeleccionado", forma.getConceptosGenerales("tipoglosa_"+forma.getPosicion())+"");
		forma.setConceptosGenerales("activoSeleccionado", forma.getConceptosGenerales("activo_"+forma.getPosicion())+"");

		//Consultamos si el registro de Concepto General es utilizado en otra parte
		UtilidadBD.iniciarTransaccion(con);
		forma.setEsUtilizado(mundo.eliminarConceptoGeneral(con, Utilidades.convertirAEntero(forma.getConceptosGenerales("codigo_"+forma.getPosicion())+"")));
		UtilidadBD.abortarTransaccion(con);
		
		//Guardamos la información original del Concepto General seleccionado en la variable LOG en la variable de la forma
		String log = "\n       ====INFORMACIÓN ORIGINAL=====       " +
					 "\n*  Código ["+forma.getConceptosGenerales("consecutivo_"+forma.getPosicion())+"] "+
					 "\n*  Descripción ["+forma.getConceptosGenerales("descripcion_"+forma.getPosicion())+"] "+
					 "\n*  Tipo Glosa ["+ValoresPorDefecto.getIntegridadDominio(forma.getConceptosGenerales("tipoglosa_"+forma.getPosicion())+"")+"] "+
					 "\n*  Activo ["+ValoresPorDefecto.getIntegridadDominio(forma.getConceptosGenerales("activo_"+forma.getPosicion())+"")+"] ";

		forma.setLog(log);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionEliminar(Connection con, ConceptosGeneralesForm forma, ConceptosGenerales mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		logger.info("===>Posición Seleccionada: "+forma.getPosicion());
		logger.info("===>Código eliminar: "+forma.getConceptosGenerales("codigo_"+forma.getPosicion()));
		UtilidadBD.iniciarTransaccion(con);
		boolean transaccion = mundo.eliminarConceptoGeneral(con, Utilidades.convertirAEntero(forma.getConceptosGenerales("codigo_"+forma.getPosicion())+""));
		if(transaccion) {
			UtilidadBD.finalizarTransaccion(con);
			forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));
			//Genramos el LOG Tipo Archivo en el momento de la modificación
			LogsAxioma.enviarLog(ConstantesBD.logConceptosGeneralesGlosasCodigo, forma.getLog(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
			this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		else {
			UtilidadBD.abortarTransaccion(con);
			errores.add("consecutivoConcepto", new ActionMessage("error.glosas.glosaNoBorrable", "Conceptos Generales"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}
	
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ConceptosGeneralesForm forma, ConceptosGenerales mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		logger.info("====>Codigo: "+forma.getCodigoConcepto());
		logger.info("====>Descripcion: "+forma.getDescripcionConcepto());
		logger.info("====>Tipo: "+forma.getTipoConcepto());
		logger.info("====>Activo: "+forma.getActivoConcepto());
		ActionErrors errores = new ActionErrors();
		//Método que evaluara si el consecutivo no se esta usando sino se devuelve un error para ser mostrado en la vista
		errores = consultarConsecutivoConceptosGenerales(forma); 
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return mapping.findForward("principal");
		}
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			boolean transaccion = mundo.insertarConceptoGeneral(con, forma, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
			if(transaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTARÓN INCONVENIENTES EN EL ALMACENAMIENTO DEL CONCEPTO GENERAL DE LA GLOSA."));
				UtilidadBD.abortarTransaccion(con);
			}
			this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}

	/**
	 * Método implementado con el fin de averiguar si
	 * el consecutivo ingresado esta disponible para
	 * realizar la inserción ó modificación
	 * @param forma
	 * @return
	 */
	private ActionErrors consultarConsecutivoConceptosGenerales(ConceptosGeneralesForm forma)
	{
		ActionErrors errores = new ActionErrors();
		int numRegistros = Utilidades.convertirAEntero(forma.getConceptosGenerales("numRegistros")+"");
		for(int i=0; i<numRegistros; i++)
		{
			//Se válida cuando ingresemos un nuevo registro
			if(forma.getEstado().equals("guardar"))
			{
				//Validamos si el código del concepto a ingresar esta siendo ya utilizado
				if(forma.getCodigoConcepto().equals(forma.getConceptosGenerales("consecutivo_"+i)+""))
					errores.add("consecutivoConcepto", new ActionMessage("error.glosas.consecutivoNoDisponible", "agregar", "Concepto General", forma.getCodigoConcepto()));
			}


			//Se valida si se está modificando el registro
			else if(forma.getEstado().equals("guardarModificar")) {
				
				logger.info("<<<<<<<<<<<<<<<<<<");
				logger.info("<<<<< Se esta guardando la modificacion.");
				
				//Validamos que el consecutivo seleccionado no haya sufrido modificaciones en caso tal validamos si el consecutivo esta siendo usado
				if(!(forma.getConceptosGenerales("consecutivoSeleccionado")+"").equals(forma.getConceptosGenerales("consecutivo_"+forma.getPosicion())+"")) {
					logger.info("<< El Codigo Cambio.");
					
					//Validamos si el código del concepto a ingresar esta siendo ya utilizado
					if((forma.getConceptosGenerales("consecutivo_"+forma.getPosicion())+"").equals(forma.getConceptosGenerales("consecutivo_"+i)+""))
					{
						logger.info("<< El Codigo Ya existe.");
						
						if(i != forma.getPosicion()){
							logger.info("<< la posicion es distinta hay error");
							errores.add("consecutivoConcepto", new ActionMessage("error.glosas.consecutivoNoDisponible", "actualizar", "Concepto General", forma.getConceptosGenerales("consecutivo_"+forma.getPosicion())));
							forma.setConceptosGenerales("consecutivo_"+forma.getPosicion(), forma.getConceptosGenerales("consecutivoSeleccionado"));
							forma.setConceptosGenerales("descripcion_"+forma.getPosicion(), forma.getConceptosGenerales("descripcionSeleccionado"));
							forma.setConceptosGenerales("tipoglosa_"+forma.getPosicion(), forma.getConceptosGenerales("tipoSeleccionado"));
							forma.setConceptosGenerales("activo_"+forma.getPosicion(), forma.getConceptosGenerales("activoSeleccionado"));

							i = numRegistros;
						}
						else
							logger.info("<< la posicion es diferente no hay problema");
					}
				}
			}
		
		}
		return errores;
	}

	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificar(Connection con, ConceptosGeneralesForm forma, ConceptosGenerales mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();

		//Método que evaluara si el consecutivo no se esta usando sino se devuelve un error para ser mostrado en la vista
		errores = consultarConsecutivoConceptosGenerales(forma); 

		if(!errores.isEmpty()) {
			saveErrors(request, errores);
			return mapping.findForward("principal");
		}
		else {
			UtilidadBD.iniciarTransaccion(con);
			boolean transaccion = mundo.modificarConceptoGeneral(con, forma, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());

			if(transaccion) {
				forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));
				//Genramos el LOG Tipo Archivo en el momento de la modificación
				String log = forma.getLog() +
							 "\n       ====INFORMACIÓN DESPUÉS DE LA MODIFICACIÓN=====       " +
							 "\n*  Código ["+forma.getConceptosGenerales("consecutivo_"+forma.getPosicion())+"] "+
							 "\n*  Descripción ["+forma.getConceptosGenerales("descripcion_"+forma.getPosicion())+"] "+
							 "\n*  Tipo Glosa ["+ValoresPorDefecto.getIntegridadDominio(forma.getConceptosGenerales("tipoglosa_"+forma.getPosicion())+"")+"] "+
							 "\n*  Activo ["+ValoresPorDefecto.getIntegridadDominio(forma.getConceptosGenerales("activo_"+forma.getPosicion())+"")+"] "+
							 "\n*  Código de la Institución ["+usuario.getCodigoInstitucion()+"] "+
							 "\n========================================================\n\n\n ";		
				LogsAxioma.enviarLog(ConstantesBD.logConceptosGeneralesGlosasCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				UtilidadBD.finalizarTransaccion(con);
			}
			else {
				forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTARÓN INCONVENIENTES EN LA MODIFICACIÓN DEL CONCEPTO GENERAL DE LA GLOSA."));
				UtilidadBD.abortarTransaccion(con);
			}
			this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}
	
	/**
	 * Método que ordena el listado mostrado en la vista
	 * según el criterio de ordenamiento establecido
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ConceptosGeneralesForm forma, ActionMapping mapping)
	{
		String[] indices={
				
				"codigo_",
				"consecutivo_",
				"descripcion_",
				"tipoglosa_",
				"activo_"
			};
		int numReg = Integer.parseInt(forma.getConceptosGenerales("numRegistros")+"");
		forma.setConceptosGenerales(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getConceptosGenerales(), numReg));
		forma.setConceptosGenerales("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método inicial de la funcionalidad, la cual se
	 * encarga de cargar en un mapa los resultados arrojados
	 * por la consulta de Conceptos Generales de Glosas
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConceptosGeneralesForm forma, ConceptosGenerales mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		forma.reset();
		forma.setConceptosGenerales(mundo.consultarConceptosGenerales(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}