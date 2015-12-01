package com.princetonsa.action.glosas;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Octubre de 2008
 */


import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.glosas.ParametrosFirmasImpresionRespForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ParametrosFirmasImpresionResp;


public class ParametrosFirmasImpresionRespAction extends Action {

	// Objeto para manejar los logs de esta clase
	Logger logger = Logger.getLogger(ParametrosFirmasImpresionRespAction.class);
	

	/**	 * Método execute del Action	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

		Connection con = null;
		try{
			if (form instanceof ParametrosFirmasImpresionRespForm) {



				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();

				if(con == null)	{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ParametrosFirmasImpresionRespForm forma = (ParametrosFirmasImpresionRespForm) form;
				ParametrosFirmasImpresionResp mundo = new ParametrosFirmasImpresionResp();

				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();

				logger.warn("[ParametrosFirmasImpresionResp]--->Estado: "+estado);

				if(estado == null) {
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if( (estado.equals("empezar")) || (estado.equals("consultaFirma"))) {
					return accionEmpezar(con, forma, mundo, usuario, mapping, request);
				}

				else if(estado.equals("empezar")) {
					return accionEmpezar(con, forma, mundo, usuario, mapping, request);
				}


				// agrega una nueva firma a la BD
				else if(estado.equals("addFirma")) {
					return accionGuardar(con, forma, mundo, usuario, mapping, request);
				}

				//elimina una firma de la bd
				else if(estado.equals("delFirma")) {
					return accionEliminar(con, forma, mundo, usuario, mapping, request);
				}

				//carga los datos de una firma para su modificacion
				else if(estado.equals("loadFirma")) {
					return accionCargarFirma(con, forma, mundo, usuario, mapping, request);
				}

				// guardar la modificacion de una firma
				else if(estado.equals("modFirma")) {
					return accionGuardarModificar(con, forma, mundo, usuario, mapping, request);
				}

				else {
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else {
				logger.error("El form no es compatible con el form de ParametrosFirmasImpresionResp");
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

	
	/** Guardar unan nueva firma
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return  */
	private ActionForward accionGuardar(Connection con, ParametrosFirmasImpresionRespForm forma, ParametrosFirmasImpresionResp mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		//validar que el usuario y el cargo no se repitan ya que son unique 
		
		
		UtilidadBD.iniciarTransaccion(con);

		boolean transaccion = mundo.insertarFirmaImpresion(con, forma.getFrmFirmasImpresion("tmpUsuario").toString(), forma.getFrmFirmasImpresion("tmpCargo").toString(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
		if(transaccion) {
			forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));
			UtilidadBD.finalizarTransaccion(con);
		}
		else {
			forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTARÓN INCONVENIENTES EN EL ALMACENAMIENTO DE LA FIRMA EN IMPRESION RESPUESTA DE GLOSA."));
			UtilidadBD.abortarTransaccion(con);
		}

		this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/** Para eliminar una firma seleccionada
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionEliminar(Connection con, ParametrosFirmasImpresionRespForm forma, ParametrosFirmasImpresionResp mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		
		logger.info("===>Código Secuencia a eliminar: " + forma.getFrmCodSecuen());
		UtilidadBD.iniciarTransaccion(con);

		boolean transaccion = mundo.eliminarFirmaImpresion(con, forma.getFrmCodSecuen());
		if(transaccion)	{
			UtilidadBD.finalizarTransaccion(con);
			forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));

			//Generamos el LOG Tipo Archivo en el momento de la modificación
			//LogsAxioma.enviarLog(ConstantesBD.logConceptosGeneralesGlosasCodigo, forma.getLog(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());

			this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		else {
			UtilidadBD.abortarTransaccion(con);
			errores.add("secuenciaFirma", new ActionMessage("error.glosas.firmaNoBorrable", "Firmas Impresion Respuesta Glosa"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}


	
	/**	 * Metodo que carga los datos almacenados al inicio 
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionEmpezar(Connection con, ParametrosFirmasImpresionRespForm forma, ParametrosFirmasImpresionResp mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		forma.reset();
		//Se lista la informacion almacenada de firmas de impresion en respuesta glosa
		forma.setFrmFirmasImpresion(mundo.listadoFirmas(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/** Se cargan los datos de la firma para ser modificados
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionCargarFirma(Connection con, ParametrosFirmasImpresionRespForm forma, ParametrosFirmasImpresionResp mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		logger.info("Cargar Datos de la firma para modificarla");
		logger.info("===>Posición Seleccionada: "+forma.getFrmCodSecuen());
		forma.setFrmFirmasImpresion("tmpCodMod", forma.getFrmFirmasImpresion("codsecuencia_"+forma.getFrmCodSecuen()));
		forma.setFrmFirmasImpresion("tmpUsuario", forma.getFrmFirmasImpresion("usuario_"+forma.getFrmCodSecuen()));
		forma.setFrmFirmasImpresion("tmpCargo", forma.getFrmFirmasImpresion("cargo_"+forma.getFrmCodSecuen()));
		Utilidades.imprimirMapa(forma.getFrmFirmasImpresion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/** almacena los datos de la modificacion de la firma seleccionada
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionGuardarModificar(Connection con, ParametrosFirmasImpresionRespForm forma, ParametrosFirmasImpresionResp mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();

		//validar que los datos modificados no existan 
		
		UtilidadBD.iniciarTransaccion(con);
		boolean transaccion = mundo.modificarFirmaImpresion(con, forma, usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario());

		if(transaccion) {
			forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO."));

			//Genramos el LOG Tipo Archivo en el momento de la modificación
			//LogsAxioma.enviarLog(ConstantesBD.logConceptosGeneralesGlosasCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());

			UtilidadBD.finalizarTransaccion(con);
		}
		else {
			forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTARÓN INCONVENIENTES EN LA MODIFICACIÓN DE LA FIRMA EN IMPRESION RESPUESTA DE GLOSA."));
			UtilidadBD.abortarTransaccion(con);
		}
		this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
}