package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.adjuntos.DTOArchivoAdjunto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.tesoreria.ConsultaTrasladosCajasRecaudoForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IAdjuntoMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio;
/**
 * 
 * Esta clase se encarga de controlar los procesos de la funcionalidad 
 * Consulta de Traslados de Caja Recaudo en el m&oacute;dulo de tesorer&iacute;a
 *
 * @author Yennifer Guerrero
 * @since  02/08/2010
 *
 */
public class ConsultaTrasladosCajasRecaudoAction extends Action {
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		if (form instanceof ConsultaTrasladosCajasRecaudoForm) {
			
			ConsultaTrasladosCajasRecaudoForm forma = (ConsultaTrasladosCajasRecaudoForm) form;
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);

			try {
				UtilidadTransaccion.getTransaccion().begin();
				ActionForward forward=null;
				if (estado.equals("empezar")) {
					forward= empezar(forma, usuario, mapping);
				}
				if(estado.equals("cambiarCentroAtencion")){
					listarCajasCajeros(forma,forma.getFiltroSolicitud()
							.getConsecutivoCentroAtencion());
					forward= mapping.findForward("principal");
				}				
				if(estado.equals("buscarRegistros")){
					forward= buscarRegistrosSolicitud(request, response, forma, usuario, mapping, ins);
				}	
				if(estado.equals("volver")){
					forma.setConfirmado("false");
					forward= mapping.findForward("principal");
				}
				if (estado.equals("mostrarDetalle")) {
					forward= buscarDetalleTrasladoCaja(forma, usuario, mapping, ins);
				}
				if(estado.equals("ordenar"))
				{
					forward= accionOrdenar(forma, usuario, mapping);
				}
				if(estado.equals("confirmarAdjuntos"))
				{
					forward= confirmarAdjuntosMovimientoCaja(forma, usuario, mapping, ins);
				}
				if(estado.equals("listarCajeroAcepta")){
					String estadoTraslado = ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado;
					
					if(forma.getFiltroSolicitud().getEstadoSolicitud().equals(estadoTraslado)){						
						forma.setDeshabilitaCajeroAcepta(true);
						forma.getFiltroSolicitud().setLoginCajeroAcepta("");
					}else{
						forma.setDeshabilitaCajeroAcepta(false);
					}
					forward= mapping.findForward("principal");
				}
				UtilidadTransaccion.getTransaccion().commit();
				return forward;
				
			} catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error consultando los traslados cajas recaudo", e);
			}
		}
		return ComunAction.accionSalirCasoError(mapping, request, null, null,
				"errors.estadoInvalido", "errors.estadoInvalido", true);

	}

	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar los registros de las 
	 * solicitudes de traslado de caja recaudo seg&uacute;n los par&aacute;metros
	 * seleccionados por el usuario.
	 * @param ins 
	 * 
	 * @param ConsultaTrasladosCajasRecaudoForm, UsuarioBasico, ActionMapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionForward buscarRegistrosSolicitud(HttpServletRequest request, HttpServletResponse response,
			ConsultaTrasladosCajasRecaudoForm forma,UsuarioBasico usuario, ActionMapping mapping,
			InstitucionBasica ins)throws Exception{
		ISolicitudTrasladoCajaServicio solicitudTrasladoServicio = TesoreriaFabricaServicio.
			crearSolicitudTrasladoCaja();
		
		ArrayList<DtoConsultaTrasladosCajasRecaudo> lista = 
			solicitudTrasladoServicio.consultarRegistrosSolicitudTrasladoCaja(forma.getFiltroSolicitud());
		if(lista!=null && lista.size()>0){
			
			if (lista.size() == 1) {
				forma.setListaRegistrosSolicitud(lista);
				forma.setReglaNavegacion("volver");
				return buscarDetalleTrasladoCaja(forma, usuario, mapping, ins);
			} else {
				forma.setListaRegistrosSolicitud(lista);
				forma.setReglaNavegacion("buscarRegistros");
				return mapping.findForward("resultadoBusqueda");
			}
		}		
		ActionErrors errores=new ActionErrors();
		errores.add("No se encontraron resultados", new ActionMessage("errores.modTesoreria.consultaTrasladoCajasRecaudo"));
		saveErrors(request, errores);	
		return mapping.findForward("principal");		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de inicializar los valores para los
	 * objetos de la página de consulta
	 * 
	 * @param ConsultaTrasladosCajasRecaudoForm 
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ActionForward empezar(ConsultaTrasladosCajasRecaudoForm forma,
			UsuarioBasico usuario, ActionMapping mapping)throws Exception{
		ISolicitudTrasladoCajaServicio solicitudTrasladoServicio = TesoreriaFabricaServicio.
			crearSolicitudTrasladoCaja();		
		DtoCentrosAtencion dtoCentroAtencio = new DtoCentrosAtencion();		
		
		forma.reset();		
		Connection con=HibernateUtil.obtenerConexion();		
		forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
				ConstantesBD.codigoFuncionalidadMenuControlCaja));
				
		dtoCentroAtencio.setCodInstitucion(usuario.getCodigoInstitucionInt());
		dtoCentroAtencio.setActivo(true);		
		forma.setListaCentrosAtencion(AdministracionFabricaServicio
				.crearCentroAtencionServicio().listarCentrosAtencion(dtoCentroAtencio));		
		forma.getFiltroSolicitud().setConsecutivoCentroAtencion(usuario.getCodigoCentroAtencion());
		
		String[] filtroEstado = new String[]{ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado,
				ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado};		
		forma.setListadoEstadoTraslado(solicitudTrasladoServicio.
				listarEstadoSolicitudTrasladoCaja(filtroEstado));
		
		listarCajasCajeros(forma, usuario.getCodigoCentroAtencion());		
		
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar el detalle del registro
	 * de traslado de cajas de recaudo seleccionado por el usuario
	 * 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return 
	 * @throws Exception
	 * 
	 * @author Yennifer Guerrero
	 */
	public ActionForward buscarDetalleTrasladoCaja(ConsultaTrasladosCajasRecaudoForm forma, 
			UsuarioBasico usuario,ActionMapping mapping, InstitucionBasica ins) throws Exception{	
		
		DtoConsultaTrasladosCajasRecaudo dtoDetalle = new DtoConsultaTrasladosCajasRecaudo();
		dtoDetalle = (forma.getListaRegistrosSolicitud()).get(forma.getIndex());		
		forma.setDtoDetalle(dtoDetalle);
		
		forma.setMostrarParaSeccionEspecial("false");
		forma.setConsulta("true");
		
		IMovimientosCajaServicio movimientosCajaServicio 	= TesoreriaFabricaServicio.crearMovimientosCajaServicio();
		IAdjuntoMovimientosCajaServicio adjuntoMovimientosCajaServicio = TesoreriaFabricaServicio.crearAdjuntoMovimiento();
		
		MovimientosCaja movimientosCaja = movimientosCajaServicio.obtenerMovimientoCaja(dtoDetalle.getCodigoPk());
		
		DtoInformacionEntrega informacionEntregaDTO= movimientosCajaServicio.
		consolidarInformacionAceptacion(movimientosCaja.getCodigoPk(), usuario.getCodigoInstitucionInt());
		
		forma.setDtoInformacionEntrega(informacionEntregaDTO);
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO 	= new DtoConsolidadoMovimiento();
		forma.setConsolidadoMovimientoDTO(consolidadoMovimientoDTO);
		forma.getConsolidadoMovimientoDTO().setMovimientosCaja(movimientosCaja);
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) 
		{
			forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
		}
		
		ArrayList <DTOArchivoAdjunto> listaAdjuntos = adjuntoMovimientosCajaServicio.consultarDocumentosSoporteAdjuntos(dtoDetalle.getCodigoPk());
		
		forma.getConsolidadoMovimientoDTO().setArchivosAdjuntosMovimiento(listaAdjuntos);		
		forma.setEsMultiEmpresa(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
		forma.setUbicacionLogo(ins.getUbicacionLogo());
		
		return mapping.findForward("mostrarDetalle");	
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar las cajas y cajeros de acuerdo al
	 * centro de atenci&oacute;n seleccionado.
	 * 
	 * @param CambioResponsableFaltanteForm, codigoCentroAtencion
	 * @author, Angela Maria Aguirre
	 * 
	 */
	public void listarCajasCajeros(ConsultaTrasladosCajasRecaudoForm forma,
			int codigoCentroAtencion) throws Exception{
		
		ICajasServicio cajaServicio = TesoreriaFabricaServicio.crearCajasServicio();		
		ICajasCajerosServicio cajasCajerosServicio = TesoreriaFabricaServicio
				.crearCajasCajerosServicio();
		
		forma.setListaCajaOrigen(cajaServicio
				.listarCajasPorCentrosAtencionPorTipoCaja(codigoCentroAtencion,
						ConstantesBD.codigoTipoCajaRecaudado));
		
		forma.setListaCajaDestino(cajaServicio
				.listarCajasPorCentrosAtencionPorTipoCaja(codigoCentroAtencion,
						ConstantesBD.codigoTipoCajaRecaudado));

		forma.setListaCajeroSolicitante((cajasCajerosServicio
				.obtenerCajerosCentrosAtencion(codigoCentroAtencion)));
		
		forma.setListaCajeroAcepta((cajasCajerosServicio
				.obtenerCajerosCentrosAtencion(codigoCentroAtencion)));		
	}
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las columnas de el resultado 
	 * de la b&uacute;squeda sig&aacute;n los par&aacute;metros ingresados
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward accionOrdenar(
			ConsultaTrasladosCajasRecaudoForm forma, UsuarioBasico usuario, 
			ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaRegistrosSolicitud(),sortG);

		return mapping.findForward("resultadoBusqueda");
	}
	/**
	 * Este m&eacute;todo se encarga de almacenar en el sistema los archivos adjuntos que el 
	 * usuario ha confirmado a trav&eacute;s del checkbox.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 * @throws Exception
	 * 
	 * @author Yennifer Guerrero
	 */
	private ActionForward confirmarAdjuntosMovimientoCaja(
			ConsultaTrasladosCajasRecaudoForm forma, UsuarioBasico usuario,
			ActionMapping mapping, InstitucionBasica ins) throws Exception {
		
		IAdjuntoMovimientosCajaServicio adjuntoMovimientosCajaServicio = TesoreriaFabricaServicio.crearAdjuntoMovimiento();
		
		ArrayList<DTOArchivoAdjunto> dtoAdjuntos = new ArrayList<DTOArchivoAdjunto>();
		dtoAdjuntos= forma.getConsolidadoMovimientoDTO().getArchivosAdjuntosMovimiento();
		
		long codigoPk = forma.getDtoDetalle().getCodigoPk();
		String login = usuario.getLoginUsuario(); 
		
		@SuppressWarnings("unused")
		ArrayList<DTOArchivoAdjunto> archivosAlmacenados = adjuntoMovimientosCajaServicio.confirmarAdjuntos(dtoAdjuntos, codigoPk, login);
		
		boolean confirmado = adjuntoMovimientosCajaServicio.adjuntoConfirmado();
		forma.setConfirmado(confirmado + "");
		
		return buscarDetalleTrasladoCaja(forma, usuario, mapping, ins);
	}
}
