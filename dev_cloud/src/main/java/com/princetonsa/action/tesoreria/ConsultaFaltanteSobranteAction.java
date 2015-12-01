package com.princetonsa.action.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.CambioResponsableFaltanteForm;
import com.princetonsa.dto.tesoreria.DTOHistoCambioResponsable;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IFaltanteSobranteServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IHistoCambioResponsableServicio;

/**
 * Clase usada para controlar los procesos de la
 * funcionalidad Cambio Responsable Faltante/Sobrante Caja en el
 * m&oacute;dulo de Tesoreria
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public class ConsultaFaltanteSobranteAction extends FaltanteSobranteAction {
	
	/**
	 * Instancia del servicio FaltanteSobranteServicio
	 */
	IFaltanteSobranteServicio faltanteSobranteServicio = TesoreriaFabricaServicio
			.crearFaltanteSobranteServicio();
	
	/**
	 * Instancia del servicio HistoCambioResponsableServicio 
	 */
	IHistoCambioResponsableServicio historicoServicio = TesoreriaFabricaServicio
			.crearHistoCambioResponsableServicio();
	
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
			
			forma.setHabilitaConsultaHistorico("true");
			request.setAttribute("habilitaConsultaHistorico", "true");
			
		
			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				if (estado.equals("cambiarCA")) {
					
					listarCajasCajeros(forma,usuario, forma.getFiltrosFaltanteSobrante().getConsecutivoCA());
				
					forward= mapping.findForward("principal");
				}

				if (estado.equals("empezar")) {
					forma.setListadoEstadoFaltanteSobrante(faltanteSobranteServicio.listarEstadoFaltanteSobrante());
					forward= empezar(forma, usuario, mapping);					

				}
				if (estado.equals("buscar")) {
					actionForward = buscarDetalleFaltanteSobrante(forma, mapping,request);
					if(actionForward.getName().equals("mostrarDetalle")){
						actionForward = buscarHistorial(forma, mapping,usuario,request);
					}
					forward= actionForward;
				}
				if (estado.equals("volver")) {
					forma.setMostrarMensaje("");
					forward= mapping.findForward("principal");
				}
				if(estado.equals("ordenar"))
				{
					forward= accionOrdenar(forma, usuario, mapping);
				}
				if (estado.equals("mostrarDetalle")) {	
					forward= buscarHistorial(forma, mapping,usuario,request);
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
	 * Este m&eacute;todo se encarga de consultar el historial del detalle de un 
	 * faltante sobrante.
	 * @param mapping
	 * @return ActionForward
	 * @author Yennifer Guerrero
	 */
	private ActionForward buscarHistorial(CambioResponsableFaltanteForm forma, 
			ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request){
		
		DTOHistoCambioResponsable dto = new DTOHistoCambioResponsable();
		DetFaltanteSobrante detalle = new DetFaltanteSobrante();
		mostrarDetalleRegistro(forma, usuario, mapping);
		detalle.setCodigoPk(forma.getDtoDetalle().getIdDetalleFaltanteSobrante());
		dto.setDetFaltanteSobrante(detalle);
				
		forma.setListaHistorico(new ArrayList<DTOHistoCambioResponsable>());
		ArrayList<DTOHistoCambioResponsable>  listaHistorico = 
			historicoServicio.consultarHistorialPorDetalleFaltanteSobranteID(dto);
		
		if(listaHistorico!=null && listaHistorico.size()>0){
			forma.getDtoDetalle().setFechaProceso(listaHistorico.get(0).getFechaModifica());
			forma.getDtoDetalle().setHoraProceso(listaHistorico.get(0).getHoraModifica());
			
			forma.setListaHistorico(listaHistorico);		
			
		}else{
			
			forma.getDtoDetalle().setFechaProceso(forma.getDtoDetalle().getFechaGeneracionInicial());
			forma.getDtoDetalle().setHoraProceso(forma.getDtoDetalle().getHoraModifica());
		}
		
		return mapping.findForward("mostrarDetalle");		
	}

}
