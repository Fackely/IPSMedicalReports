package com.princetonsa.action.tesoreria;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.CambioResponsableFaltanteForm;
import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IDetFaltanteSobranteServicio;

/**
 * Clase usada para controlar los procesos de la
 * funcionalidad Cambio Responsable Faltante/Sobrante Caja en el
 * m&oacute;dulo de Tesorer&iacute;a
 * 
 * @author Yennifer Guerrero 
 */
public class CambioResponsableFaltanteAction extends FaltanteSobranteAction {

	/**
	 * M&eacute;todo execute de la clase.
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (form instanceof CambioResponsableFaltanteForm) {
			ActionForward actionForward=null;
			CambioResponsableFaltanteForm forma = (CambioResponsableFaltanteForm) form;
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

			forma.setHabilitaConsultaHistorico("false");			
			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				if (estado.equals("cambiarCA")) {

					listarCajasCajeros(forma, usuario, forma
							.getFiltrosFaltanteSobrante().getConsecutivoCA());

					forward= mapping.findForward("principal");
				}

				if (estado.equals("empezar")) {

					forward= empezar(forma, usuario, mapping);

				}
				if (estado.equals("buscar")) {
					forward= buscarDetalleFaltanteSobrante(forma, mapping,request);					
				}
				if (estado.equals("volver")) {
					forma.setMostrarMensaje("");
					forward= mapping.findForward("principal");
				}
				if(estado.equals("ordenar"))
				{
					forward= accionOrdenar(forma, usuario, mapping);
				}

				if(estado.equals("mostrarDetalle")){
					forma.setMostrarMensaje("");
					forma.setResumen(false);					
					forward= mostrarDetalleRegistro(forma,usuario,mapping);
				}
				if (estado.equals("cambiarResponsable")) {
					forma.setMostrarMensaje("");
					forward= cambiarResponsable(forma, usuario, mapping,request);					
				}
				if(estado.equals("mostrarPopUpConfirmacion")){
					forward= mapping.findForward("mostrarPopUpConfirmacion");
				}				
				UtilidadTransaccion.getTransaccion().commit();
				return forward;
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error registrando el faltante sobrante", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);

	}

	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el responsable de un registro
	 * faltante sobrante y guardar su hist&aacute;rico
	 * 
	 * @param CambioResponsableFaltanteForm
	 * @param UsuarioBasico
	 * @param ActionMapping
	 * @return ActionForward
	 * 
	 * @author, Angela Maria Aguirre
	 * 
	 */
	private ActionForward cambiarResponsable(
			CambioResponsableFaltanteForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request) throws Exception{
		
		IDetFaltanteSobranteServicio detalleServicio = TesoreriaFabricaServicio.crearDetFaltanteSobranteServicio();

		
		if(validarDatosCambioResponsable(forma,request)){
			DTOCambioResponsableDetFaltanteSobrante dto = forma.getDtoDetalle();
			dto.setFechaProceso(Calendar.getInstance().getTime());
			dto.setHoraProceso(UtilidadFecha.conversionFormatoHoraABD(Calendar
					.getInstance().getTime()));
			dto.setLoginUsuarioModifica(usuario.getLoginUsuario());
			dto.setConsecutivoCA(forma.getFiltrosFaltanteSobrante()
					.getConsecutivoCA());
			forma.setCajeroNuevoResponsable(dto.getLoginUsuarioResponsable());
			int limite1 = dto.getLoginUsuarioResponsable().indexOf("(");
			int limite2 = dto.getLoginUsuarioResponsable().indexOf(")");
			String loginUsuarioResponsable= dto.getLoginUsuarioResponsable().substring(limite1+1, limite2);
					
			dto.setLoginUsuarioResponsable(loginUsuarioResponsable);
			
			boolean procesoExitoso = detalleServicio
					.actualizarResponsableDetFaltanteSobrante(dto);
			if(procesoExitoso){
				forma.setMostrarMensaje("resumen");
				forma.setResumen(true);				
			}else{
				mostrarErrores(forma, request);
			}			
		}
		return mostrarDetalleRegistro(forma,usuario,mapping);
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de retornar error
	 * si la actualizaci&aacute;n del cambio responsable del
	 * detalle faltante / sobrante no se pudo ejecutar
	 * 
	 * @param CambioResponsableFaltanteForm
	 * @param HttpServletRequest
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void mostrarErrores(CambioResponsableFaltanteForm forma, HttpServletRequest request){
		ActionErrors errores = new ActionErrors();
		errores.add("error cambio responsable", new ActionMessage("errores.modTesoreria.cambioResponsableDetFaltanteSobrante"));
		saveErrors(request, errores);
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de validar los datos
	 * para el cambio de responsable del detallde de
	 * faltante/sobrante
	 * 
	 * @param CambioResponsableFaltanteForm forma, HttpServletRequest request
	 * @return boolean 
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	private boolean validarDatosCambioResponsable(
			CambioResponsableFaltanteForm forma, HttpServletRequest request){
		
		ActionErrors errores = new ActionErrors();
		if(forma.getDtoDetalle().getLoginUsuarioResponsable().equals("") ||
				forma.getDtoDetalle().getLoginUsuarioResponsable()==null){				
				errores.add("Nuevo Responsable Requerido.", new ActionMessage(
						 "errores.modTesoreria.loginNuevoResponsableRequerido"));
		}
		if(forma.getDtoDetalle().getMotivo().equals("")||
				forma.getDtoDetalle().getMotivo()==null){
			errores.add("Motivo Requerido.", new ActionMessage(
			 "errores.modTesoreria.motivoRequerido"));
		}else{
			if(forma.getDtoDetalle().getMotivo().length()>255){
				errores.add("Motivo Requerido.", new ActionMessage(
				 "errores.modTesoreria.longitudMotivoNoPermitida"));
			}
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
			return false;
		}else{
			return true;
		}		
	}
	
}
