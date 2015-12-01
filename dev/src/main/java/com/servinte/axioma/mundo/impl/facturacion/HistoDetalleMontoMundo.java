package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoDetalleMontoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IHistoMontosCobroDAO;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontoAgrupacionArticuloMundo;
import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.orm.HistoMontoAgrupServ;
import com.servinte.axioma.orm.HistoMontoArticuloEsp;
import com.servinte.axioma.orm.HistoMontoServEsp;
import com.servinte.axioma.orm.HistoMontosCobro;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 3/09/2010
 */
public class HistoDetalleMontoMundo implements IHistoDetalleMontoMundo {
	
	IHistoDetalleMontoDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public HistoDetalleMontoMundo(){
		dao=FacturacionFabricaDAO.crearHistoDetalleMontoDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar el histórico del detalle
	 * de un monto de cobro
	 * 
	 * @param HistoDetalleMonto
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(HistoDetalleMonto histoDetalleMonto){
		return dao.guardarDetalleMontoCobro(histoDetalleMonto);
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los historicos
	 * de los detalles de un monto de cobro
	 * 
	 * @param DetalleMonto, ArrayList<DTOBusquedaMontoAgrupacionArticulo>listaAgrupacionArticulo
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarHistoricosMontoCobro(String accion, int detalleCodigo,
			UsuarioBasico usuarioSesion){
		boolean proceso = false;
		IHistoMontosCobroDAO histoMontoDAO = FacturacionFabricaDAO.crearHistoMontosCobroDAO();
		MontosCobro monto = new MontosCobro();
		HashSet<HistoDetalleMonto> setHistoDetalle = new HashSet<HistoDetalleMonto>(0);
		IDetalleMontoMundo detalleMundo = FacturacionFabricaMundo.crearDetalleMontoMundo();
		Usuarios usuario= new Usuarios();
		ArrayList<DTOBusquedaMontoAgrupacionArticulo> listaAgrupacionArticulo=null;
		ArrayList<DTOBusquedaMontoAgrupacionServicio> listaAgrupacionServicio=null;
		ArrayList<DTOBusquedaMontoArticuloEspecifico> listaArticuloEsp=null;
		ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicioEsp=null;		
				
		DTOResultadoBusquedaDetalleMontos detalleMonto = detalleMundo.buscarDetalleMonto(
				detalleCodigo, usuarioSesion);
		if(detalleMonto!=null){
			listaAgrupacionArticulo =  detalleMonto.getListaAgrupacionArticulo();			
			listaAgrupacionServicio = detalleMonto.getListaAgrupacionServicios();
			listaArticuloEsp = detalleMonto.getListaArticuloEspecifico();			
			listaServicioEsp = detalleMonto.getListaServicioEspecifico();
		}		
		
		usuario.setLogin(usuarioSesion.getLoginUsuario());
		monto.setCodigo(detalleMonto.getIdMontoCobro());
		HistoMontosCobro histoMonto = poblarHistoMontoCobro(ConstantesIntegridadDominio.acronimoAccionHistoricaModificar,
				usuario, monto);			
		usuario.setLogin(usuarioSesion.getLoginUsuario());
		HistoDetalleMonto histoDetalle = poblarHistoDetalle(detalleMonto,accion,usuario);
		
		if(listaAgrupacionServicio!=null && listaAgrupacionServicio.size()>0){
			HistoMontoAgrupServ agrupacionServicio = null;
			HashSet<HistoMontoAgrupServ> setHistoAgrupacionServicio = new HashSet<HistoMontoAgrupServ>(0);
			
			for(DTOBusquedaMontoAgrupacionServicio registro :listaAgrupacionServicio){
				agrupacionServicio = poblarHistoDetalleAgrupacionServicio(registro, accion,usuario);	
				agrupacionServicio.setHistoDetalleMonto(histoDetalle);
				setHistoAgrupacionServicio.add(agrupacionServicio);
			}
			histoDetalle.setHistoMontoAgrupServs(setHistoAgrupacionServicio);
		}
		
		if(listaServicioEsp!=null && listaServicioEsp.size()>0){
			HashSet<HistoMontoServEsp> setServicioEspecifico = new HashSet<HistoMontoServEsp>(0);
			HistoMontoServEsp histoServicio = null;
			
			for(DTOBusquedaMontoServicioEspecifico registro : listaServicioEsp){
				histoServicio =poblarHistoDetalleServicioEspecifico(registro, accion, usuario);
				histoServicio.setHistoDetalleMonto(histoDetalle);
				setServicioEspecifico.add(histoServicio);				
			}
			histoDetalle.setHistoMontoServEsps(setServicioEspecifico);
		}
		
		if(listaArticuloEsp!=null && listaArticuloEsp.size()>0){
			HashSet<HistoMontoArticuloEsp> setArticuloEspecifico = new HashSet<HistoMontoArticuloEsp>(0);
			HistoMontoArticuloEsp articulo = null; 
			
			for(DTOBusquedaMontoArticuloEspecifico registro : listaArticuloEsp){
				articulo = poblarHistoArticuloEspecifico(registro, accion, usuario);
				articulo.setHistoDetalleMonto(histoDetalle);
				setArticuloEspecifico.add(articulo);
			}
			histoDetalle.setHistoMontoArticuloEsps(setArticuloEspecifico);
		}
		
		histoDetalle.setHistoMontosCobro(histoMonto);
		setHistoDetalle.add(histoDetalle);		
		histoMonto.setHistoDetalleMontos(setHistoDetalle);
		
		proceso= histoMontoDAO.guardarHistoDetalleMontoCobro(histoMonto);
		if(proceso){
			UtilidadTransaccion.getTransaccion().commit();
			
			if(listaAgrupacionArticulo!=null && listaAgrupacionArticulo.size()>0){
				IHistoMontoAgrupacionArticuloMundo mundo = FacturacionFabricaMundo.
					crearHistoMontoAgrupacionArticuloMundo();
				for(DTOBusquedaMontoAgrupacionArticulo registro : listaAgrupacionArticulo){
					mundo.insertarHistoMontoAgrupacionArticulo(registro, histoDetalle);
				}				
			}
		}		
		
		return proceso;
	}	
	
	/**
	 * 
	 * Este método se encarga de
	 * @author Angela Maria Aguirre
	 */
	private HistoDetalleMonto poblarHistoDetalle( DTOResultadoBusquedaDetalleMontos registro, String accion,Usuarios usuario){
		HistoDetalleMonto histoDetalle = new  HistoDetalleMonto();
		
		DetalleMonto detalleMonto = new DetalleMonto();
		detalleMonto.setDetalleCodigo(registro.getDetalleCodigo());
		
		histoDetalle.setAccionRealizada(accion);
		histoDetalle.setDetalleMonto(detalleMonto);
		histoDetalle.setEstratoSocialCodigo(registro.getEstratoID());		
		histoDetalle.setFechaRegistro(Calendar.getInstance().getTime());
		histoDetalle.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		histoDetalle.setUsuarios(usuario);
		if(registro.getNaturalezaID()!=null && registro.getNaturalezaID()>0 ){
			histoDetalle.setNaturalezaCodigo(registro.getNaturalezaID());			
		}
		histoDetalle.setTipoAfiliadoCodigo(registro.getTipoAfiliadoAcronimo());
		histoDetalle.setTipoDetalle(registro.getTipoDetalleAcronimo());
		histoDetalle.setTipoMontoCodigo(registro.getTipoMontoID());
		histoDetalle.setTipoPacienteCodigo(registro.getTipoPacienteAcronimo().charAt(0));
		histoDetalle.setViaIngresoCodigo(registro.getViaIngresoID());		
		
		return histoDetalle;
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private HistoMontoAgrupServ poblarHistoDetalleAgrupacionServicio(DTOBusquedaMontoAgrupacionServicio registro, 
			String accion, Usuarios usuario){
		HistoMontoAgrupServ servicio = new HistoMontoAgrupServ();
		
		servicio.setAccionRealizada(accion);
		servicio.setCantidadMonto(registro.getCantidadMonto());
		
		if(registro.getCantidadServicio()!=null && registro.getCantidadServicio()>0){
			servicio.setCantidadServicio(registro.getCantidadServicio());
		}
		
		Especialidades especialidad = null;
		GruposServicios grupoServicio = null;
		TiposServicio tiposServicio = null;
		
		if(registro.getCodigoEspecialidad()!=null){
			if(registro.getCodigoEspecialidad()>0){
				especialidad = new Especialidades();
				especialidad.setCodigo(registro.getCodigoEspecialidad());
			}
		}
		
		if(registro.getCodigoGrupoServicio()!=null){
			if(registro.getCodigoGrupoServicio()>0){
				grupoServicio = new GruposServicios();
				grupoServicio.setCodigo(registro.getCodigoGrupoServicio());
			}
		}
		
		if(registro.getAcronimoTipoServicio()!=null && !registro.getAcronimoTipoServicio().equals("")){
			tiposServicio = new TiposServicio();
			tiposServicio.setAcronimo(registro.getAcronimoTipoServicio());
		}
		
		servicio.setEspecialidades(especialidad);
		servicio.setFechaRegistro(Calendar.getInstance().getTime());
		servicio.setGruposServicios(grupoServicio);
		servicio.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		servicio.setTiposServicio(tiposServicio);
		servicio.setUsuarios(usuario);
		
		if(registro.getValorMonto()==null){
			servicio.setValorMonto(0L);
		}else{
			servicio.setValorMonto(registro.getValorMonto());
		}
		
		return servicio;	
		
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private HistoMontoServEsp poblarHistoDetalleServicioEspecifico(DTOBusquedaMontoServicioEspecifico registro, 
			String accion, Usuarios usuario){
		HistoMontoServEsp servicioEsp = new HistoMontoServEsp();
		Servicios servicio = null;
		
		servicioEsp.setAccionRealizada(accion);
		servicioEsp.setCantidadMonto(registro.getCantidadMonto());
		
		if(registro.getCantidadServicio()!=null && registro.getCantidadServicio()>0){
			servicioEsp.setCantidadServicio(registro.getCantidadServicio());
		}
		
		servicioEsp.setFechaRegistro(Calendar.getInstance().getTime());
		servicioEsp.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		
		if(registro.getCodigoServicioEspecifico()>0){
			servicio = new Servicios();
			servicio.setCodigo(registro.getCodigoServicio());
		}
		servicioEsp.setServicios(servicio);
		servicioEsp.setUsuarios(usuario);
		
		if(registro.getValorMonto()==null){
			servicioEsp.setValorMonto(0L);
		}else{
			servicioEsp.setValorMonto(registro.getValorMonto());
		}
		
		return servicioEsp;
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private HistoMontoArticuloEsp poblarHistoArticuloEspecifico(DTOBusquedaMontoArticuloEspecifico registro, 
			String accion, Usuarios usuario){
		HistoMontoArticuloEsp articuloEsp = new HistoMontoArticuloEsp();
		
		articuloEsp.setAccionRealizada(accion);
		articuloEsp.setArticulo(registro.getArticuloCodigo());
		
		if(registro.getCantidadArticulos()!=null && registro.getCantidadArticulos()>0){
			articuloEsp.setCantidadArticulos(registro.getCantidadArticulos());
		}		
		articuloEsp.setCantidadMonto(registro.getCantidadMonto());
		articuloEsp.setFechaRegistro(Calendar.getInstance().getTime());
		articuloEsp.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		articuloEsp.setUsuarios(usuario);
		if(registro.getValorMonto()==null){
			articuloEsp.setValorMonto(0L);
		}else{
			articuloEsp.setValorMonto(registro.getValorMonto());
		}
		
		return articuloEsp;
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private HistoMontosCobro poblarHistoMontoCobro(String accion,Usuarios usuario,MontosCobro montoCobro){
		HistoMontosCobro histoMonto = new HistoMontosCobro();
		histoMonto.setAccionRealizada(accion);
		histoMonto.setFechaRegistro(Calendar.getInstance().getTime());
		histoMonto.setHoraRegistro(UtilidadFecha.conversionFormatoHoraABD(Calendar
				.getInstance().getTime()));
		histoMonto.setUsuarios(usuario);
		histoMonto.setMontosCobro(montoCobro);
		
		return histoMonto;	
		
	}

}
