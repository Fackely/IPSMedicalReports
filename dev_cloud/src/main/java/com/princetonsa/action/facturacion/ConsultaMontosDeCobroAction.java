package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsultaMontosDeCobroForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITiposMontoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IEstratoSocialServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.INaturalezaPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposAfiliadoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 14/09/2010
 */
public class ConsultaMontosDeCobroAction extends Action {
	

	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de Consulta de Montos de Cobro
	 * 
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		Connection conn = null;
		try{
			if (form instanceof ConsultaMontosDeCobroForm) {
				ConsultaMontosDeCobroForm forma = (ConsultaMontosDeCobroForm)form;
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
						.getSession());
				String estado = forma.getEstado();
				Log4JManager.info("estado " + estado);
				forma.setMostrarMensaje("");

				conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
				try{
					if(estado.equals("empezar")){
						return empezar(forma,mapping);
					}else{
						if(estado.equals("consultarMontos")){
							return buscarMontos(forma,mapping);						
						}else{
							if(estado.equals("consultarDetalleMonto")){
								return buscarDetallesMonto(forma,mapping,usuario);
							}else{
								if(estado.equals("ordenar")){
									return accionOrdenar(forma,usuario,mapping);
								}else{
									if(estado.equals("cambiarEstratoSocial")){
										return cambiarEstratoSocial(forma,mapping);
									}
								}
							}
						}
					}
				}catch (Exception e) {
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					Log4JManager.error("Error en la transaccion de Monto Detalle", e);
				}finally{
					if(conn!=null){
						try {
							conn.commit();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}			
			}	
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(conn);
		}
		return null;

	}
	
	
	/**
	 * 
	 * Este Método se encarga de incializar los valores para la página
	 * de consulta de montos de cobro
	 * 
	 * @param ConsultaMontosDeCobroForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward empezar(ConsultaMontosDeCobroForm forma, ActionMapping mapping){
		forma.reset();
		IEstratoSocialServicio estratoServicio = ManejoPacienteServicioFabrica.
		crearEstratoSocialServicio();
		
		IConvenioServicio convenioServicio = FacturacionServicioFabrica.
		crearConvenioServicio();
		
		IViasIngresoServicio viasIngresoServicio = ManejoPacienteServicioFabrica.
			crearViasIngresoServicio();
		ITiposPacienteServicio tipoPacienteServicio = ManejoPacienteServicioFabrica.
			crearTiposPacienteServicio();
	
		forma.setListadoViasIngreso(viasIngresoServicio.buscarViasIngreso());	
		
		forma.setListadoTipoPaciente(tipoPacienteServicio.buscarTiposPaciente());
		
		forma.setListaConvenios(convenioServicio.obtenerConveniosManejanMonto());	
		
		INaturalezaPacienteServicio naturalezaServicio = ManejoPacienteServicioFabrica.crearNaturalezaPacienteServicio();
	
		ITiposAfiliadoServicio tiposAfiliadoServicio = ManejoPacienteServicioFabrica.crearTiposAfiliadoServicio();
		
		ITiposMontoServicio tipoMontoServicio = FacturacionServicioFabrica.crearTiposMontoServicio();
				
		Connection conn=UtilidadBD.abrirConexion();
				
		String[] listadoIntegridadDOminio=new String[]{ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroDET,
				ConstantesIntegridadDominio.acronimoTipoDetalleMontoCobroGEN};
		
		ArrayList<DtoIntegridadDominio> listaTiposDetalleMonto=Utilidades.
		generarListadoConstantesIntegridadDominio(conn, listadoIntegridadDOminio, false);
		
		if(listaTiposDetalleMonto!=null && listaTiposDetalleMonto.size()>0){
			forma.setListaTiposDetalleMonto(listaTiposDetalleMonto);
		}		
		
		UtilidadBD.closeConnection(conn);	
		
		forma.setListadoTipoAfiliado(tiposAfiliadoServicio.obtenerTiposAfiliado());
		
		forma.setListadoEstratoSocial(estratoServicio.consultarEstratoSocial());
		
		forma.setListaNaturalezaPacientes(naturalezaServicio.consultarNaturalezaPacientes());		
		
		forma.setListaTiposMonto(tipoMontoServicio.consultarTiposMonto());	
		
		return mapping.findForward("consultarMontos");
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los montos de cobro
	 * que cumplan con los filtros ingresados por el usuario
	 * 
	 * @param ConsultaMontosDeCobroForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward buscarMontos(ConsultaMontosDeCobroForm forma, ActionMapping mapping){
		IMontosCobroServicio montoServicio = FacturacionServicioFabrica.crearMontosCobroServicio();
		
		ArrayList<DTOResultadoBusquedaDetalleMontos> listaFiltro = new ArrayList<DTOResultadoBusquedaDetalleMontos>();
		listaFiltro.add(forma.getDtoFiltro());
		forma.getDtoMontoFiltro().setListaDetalles(listaFiltro);
		forma.getDtoMontoFiltro().setTipoTransaccion("consultaMontosCobro");
		
		ArrayList<DTOMontosCobro> listaResultado = montoServicio
			.consultaMontosCobroEstructurado(forma.getDtoMontoFiltro());
		
		if(listaResultado!=null && listaResultado.size()>0){
			forma.setListaMontosCobro(listaResultado);
			return mapping.findForward("mostrarMontos");
		}else{
			forma.setListaMontosCobro(null);
			forma.setMostrarMensaje("resumen");
			return mapping.findForward("consultarMontos");
		}				
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los detalles de un monto de cobro
	 * seleccionado por el usuario
	 * 
	 * @param ConsultaMontosDeCobroForm forma, 
			  ActionMapping mapping, UsuarioBasico usuarioSesion
     * @return 	ActionForward		
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward buscarDetallesMonto(ConsultaMontosDeCobroForm forma, 
			ActionMapping mapping, UsuarioBasico usuarioSesion){
		IDetalleMontoServicio detalleServicio = FacturacionServicioFabrica.crearDetalleMontoServicio();		
		
		DTOMontosCobro montoSeleccionado = obtenerDetalleMontoSeleccionado(forma);		
				
		DTOResultadoBusquedaDetalleMontos dtoDetalles = detalleServicio.buscarDetalleMonto(
				montoSeleccionado.getListaDetalles().get(0).getDetalleCodigo(), usuarioSesion);
		boolean tieneDetalle=false;
		
		if(dtoDetalles!=null){
			if(dtoDetalles.getListaAgrupacionArticulo()!=null && 
				dtoDetalles.getListaAgrupacionArticulo().size()>0){
				tieneDetalle=true;
				montoSeleccionado.getListaDetalles().get(0).setListaAgrupacionArticulo(
				dtoDetalles.getListaAgrupacionArticulo());
			}
			if(dtoDetalles.getListaAgrupacionServicios()!=null &&
					dtoDetalles.getListaAgrupacionServicios().size()>0){
				tieneDetalle=true;
				montoSeleccionado.getListaDetalles().get(0).setListaAgrupacionServicios(
						dtoDetalles.getListaAgrupacionServicios());
			}
			if(dtoDetalles.getListaArticuloEspecifico()!=null &&
				dtoDetalles.getListaArticuloEspecifico().size()>0){
				tieneDetalle=true;
				montoSeleccionado.getListaDetalles().get(0).setListaArticuloEspecifico(
				dtoDetalles.getListaArticuloEspecifico());
			}
			if(dtoDetalles.getListaServicioEspecifico()!=null &&
				dtoDetalles.getListaServicioEspecifico().size()>0){
				tieneDetalle=true;
				montoSeleccionado.getListaDetalles().get(0).setListaServicioEspecifico(
				dtoDetalles.getListaServicioEspecifico());					
			}				
			if(!tieneDetalle){
				montoSeleccionado = null;
			}
		}else{
			montoSeleccionado = null;
		}		
		forma.setMontoSeleccionado(montoSeleccionado);			
				
		return mapping.findForward("mostrarDetallesMonto");		
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
	public ActionForward accionOrdenar(ConsultaMontosDeCobroForm forma, UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		for(DTOMontosCobro monto :forma.getListaMontosCobro()){
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(monto.getListaDetalles(),sortG);
		}	

		return mapping.findForward("mostrarMontos");
	}
		
	/**
	 * 
	 * Este Método se encarga de obtener el detalle y el monto 
	 * monto seleccionado por el usuario
	 * 
	 * @param ConsultaMontosDeCobroForm
	 * @return DTOMontosCobro
	 * @author, Angela Maria Aguirre
	 *
	 */
	private DTOMontosCobro obtenerDetalleMontoSeleccionado(ConsultaMontosDeCobroForm forma){
		DTOMontosCobro montoSeleccionado = new DTOMontosCobro();
		int codigoDetalle = forma.getCodigoDetalle();
		boolean terminar=false;
		
		for(DTOMontosCobro monto: forma.getListaMontosCobro()){
			if(terminar){
				break;
			}
			for(DTOResultadoBusquedaDetalleMontos detalle : monto.getListaDetalles()){
				if(codigoDetalle == detalle.getDetalleCodigo()){
					
					DtoConvenio convenio = new DtoConvenio();
					convenio.setDescripcion(monto.getConvenio().getDescripcion());
					montoSeleccionado.setConvenio(convenio);					
					montoSeleccionado.setFechaVigenciaConvenio(monto.getFechaVigenciaConvenio());
					
					ArrayList<DTOResultadoBusquedaDetalleMontos> listaDetalle = new ArrayList<DTOResultadoBusquedaDetalleMontos>();
					listaDetalle.add(detalle);
					montoSeleccionado.setListaDetalles(listaDetalle);
					terminar=true;
					break;					
				}
			}
		}	
		return montoSeleccionado;		
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward cambiarEstratoSocial(ConsultaMontosDeCobroForm forma, ActionMapping mapping){
		
		IEstratoSocialServicio estratoServicio = ManejoPacienteServicioFabrica.
		crearEstratoSocialServicio();
		if((forma.getDtoMontoFiltro().getConvenio().getCodigo())!=
			ConstantesBD.codigoNuncaValido){
			IConvenioServicio convenioServicio = FacturacionServicioFabrica.
			crearConvenioServicio();
			
			DtoConvenio convenio = convenioServicio.buscarConvenio(
					forma.getDtoMontoFiltro().getConvenio().getCodigo());
			
			if(convenio!=null){
				String acronimoRegimenConvenio=convenio.getAcronimoTipoRegimen();
				forma.setListadoEstratoSocial(estratoServicio
						.consultarEstratosSocilaesPorRegimen(acronimoRegimenConvenio));
			}
		}else{
			forma.setListadoEstratoSocial(estratoServicio.consultarEstratoSocial());
		}
		
		return mapping.findForward("consultarMontos");
	}

}
