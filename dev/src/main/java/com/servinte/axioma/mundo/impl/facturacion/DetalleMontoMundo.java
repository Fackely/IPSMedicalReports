package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.HashSet;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoServicioEspecifico;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoAgrupacionServiciosDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoServicioEspecificoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IGrupoInventarioDAO;
import com.servinte.axioma.dao.interfaz.inventario.ISubgrupoInventarioDAO;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoDetalleMontoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoAgrupacionArticuloMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoArticuloEspecificoMundo;
import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.MontoAgrupacionServicios;
import com.servinte.axioma.orm.MontoArticuloEspecifico;
import com.servinte.axioma.orm.MontoServicioEspecifico;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoMundo implements IDetalleMontoMundo {
	
	/**
	 * Instancia de la clase IDetalleMontoDAO
	 */
	IDetalleMontoDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DetalleMontoMundo(){		
		dao = FacturacionFabricaDAO.crearDetalleMontoDAO();
	}	
	
	/**
	 * 
	 * Este Método se encarga de buscar un detalle de 
	 * un monto de cobro por su id
	 * 
	 * @return DetalleMonto
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetalleMonto buscarDetalleMontoPorID(int id){
		return dao.buscarDetalleMontoPorID(id);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el detalle de un monto
	 * de cobro
	 * 
	 * @param int detalleCodigo,,UsuarioBasico usuarioSesion
	 * @return DTOResultadoBusquedaDetalleMontos
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOResultadoBusquedaDetalleMontos buscarDetalleMonto(int detalleCodigo,UsuarioBasico usuarioSesion){
		DTOResultadoBusquedaDetalleMontos detalleMonto = new DTOResultadoBusquedaDetalleMontos();
		DTOBusquedaMontoAgrupacionArticulo dtoAgrupacionArticulo = new DTOBusquedaMontoAgrupacionArticulo();
		DTOBusquedaMontoArticuloEspecifico dtoArticuloEspecifico = new DTOBusquedaMontoArticuloEspecifico();
		IMontoAgrupacionServiciosDAO agrupacionServicioDAO = FacturacionFabricaDAO.crearMontoAgrupacionServiciosDAO();
		IMontoServicioEspecificoDAO servicioEspecificoDAO = FacturacionFabricaDAO.crearMontoServicioEspecificoDAO();
		IMontoAgrupacionArticuloMundo agrupacionArticuloMundo = FacturacionFabricaMundo.crearMontoAgrupacionArticuloMundo();
		IMontoArticuloEspecificoMundo articuloEspecificoMundo = FacturacionFabricaMundo.crearMontoArticuloEspecificoMundo();
		int codigoInstitucion = usuarioSesion.getCodigoInstitucionInt();
		int codigoTipoTarifario=0;
		IDetalleMontoDAO detalleDAO = FacturacionFabricaDAO.crearDetalleMontoDAO();
		
		DetalleMonto detalle =  detalleDAO.buscarDetalleMontoPorID(detalleCodigo);
		detalleMonto.setDetalleCodigo(detalle.getDetalleCodigo());
		detalleMonto.setIdMontoCobro(detalle.getMontosCobro().getCodigo());
		if(detalle.getNaturalezaPacientes()!=null){
			detalleMonto.setNaturalezaID(detalle.getNaturalezaPacientes().getCodigo());			
		}
		detalleMonto.setEstratoID(detalle.getEstratosSociales().getCodigo());
		detalleMonto.setTipoAfiliadoAcronimo(detalle.getTiposAfiliado().getAcronimo());
		detalleMonto.setTipoDetalleAcronimo(detalle.getTipoDetalle());
		detalleMonto.setTipoMontoID(detalle.getTiposMonto().getCodigo());
		detalleMonto.setTipoPacienteAcronimo(detalle.getTiposPaciente().getAcronimo());
		detalleMonto.setViaIngresoID(detalle.getViasIngreso().getCodigo());
		
		String codigoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);
		if(UtilidadTexto.isNumber(codigoTarifario)){
			
			codigoTipoTarifario = Integer.parseInt(codigoTarifario);
		}
		
		ArrayList<DTOBusquedaMontoAgrupacionServicio> listaAgrupacionServicios = 
			agrupacionServicioDAO.obtenerServiciosPorDetalleID(detalleCodigo);		
		detalleMonto.setListaAgrupacionServicios(listaAgrupacionServicios);
		
		ArrayList<DTOBusquedaMontoServicioEspecifico> listaServicioEspecifico =  
			servicioEspecificoDAO.obtenerServiciosPorDetalleID(detalleCodigo,codigoTipoTarifario);		
		detalleMonto.setListaServicioEspecifico(listaServicioEspecifico);		
		
		dtoAgrupacionArticulo.setDetalleCodigo(detalleCodigo);
		ArrayList<DTOBusquedaMontoAgrupacionArticulo> listaAgrupacionArticulo = 
			agrupacionArticuloMundo.buscarMontoAgrupacionArticuloPorDetalleID(dtoAgrupacionArticulo);
		
		if(listaAgrupacionArticulo!=null && listaAgrupacionArticulo.size()>0){
			
			IGrupoInventarioDAO grupoInventarioDAO = 
				InventarioDAOFabrica.crearGrupoInventarioDAO();
			
			ISubgrupoInventarioDAO subgrupoInventarioDAO = 
				InventarioDAOFabrica.crearSubgrupoInventarioDAO();
			
			ArrayList<GrupoInventario> listaGrupoInventario = null;
			ArrayList<SubgrupoInventario> listaSrupoInventario=null;
			
			for(DTOBusquedaMontoAgrupacionArticulo registro : listaAgrupacionArticulo){
				if(registro.getClaseInventarioCodigo()>0){
					listaGrupoInventario = 
						grupoInventarioDAO.buscarGrupoInventarioPorClase(registro.getClaseInventarioCodigo());
					registro.setListaGrupoInventario(listaGrupoInventario);
				}					
				if(registro.getGrupoCodigo()>0){
					listaSrupoInventario = subgrupoInventarioDAO.buscarSubgrupoInventarioPorGrupoID(
							registro.getGrupoCodigo());
					registro.setListaSubgrupoInventario(listaSrupoInventario);						
				}					
			}
			detalleMonto.setListaAgrupacionArticulo(listaAgrupacionArticulo);
		}
		
		dtoArticuloEspecifico.setDetalleCodigo(detalleCodigo);
		ArrayList<DTOBusquedaMontoArticuloEspecifico> listaArticuloEspecifico = 
			articuloEspecificoMundo.buscarMontoArticuloEspecifico(dtoArticuloEspecifico);
		detalleMonto.setListaArticuloEspecifico(listaArticuloEspecifico);
		
		return detalleMonto;	
	}
	
	
	/**
	 * 
	 * Este Método se encarga de guardar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro, Usuarios
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(DTOResultadoBusquedaDetalleMontos dto, UsuarioBasico usuarioSesion){		
		DetalleMonto detalleMonto = new DetalleMonto();
		MontoAgrupacionServicios agrupacionServicio = new MontoAgrupacionServicios();
		MontoServicioEspecifico servicio = new MontoServicioEspecifico();
		MontoArticuloEspecifico articulo = new MontoArticuloEspecifico();
		boolean guardar=false;
		String accion="";
		IHistoDetalleMontoMundo histoMundo = FacturacionFabricaMundo.crearHistoDetalleMontoMundo();
		
		detalleMonto.setDetalleCodigo(dto.getDetalleCodigo());
		if(dto.getDetalleCodigo()>0){
			detalleMonto = dao.buscarDetalleMontoPorID(detalleMonto.getDetalleCodigo());
			MontosCobro monto = new MontosCobro();
			monto.setCodigo(dto.getIdMontoCobro());
			detalleMonto.setMontosCobro(monto);
		}
		
		if(dto.getListaAgrupacionServicios()!=null && dto.getListaAgrupacionServicios().size()>0){
			HashSet<MontoAgrupacionServicios> setAgrupacionServicio = new HashSet<MontoAgrupacionServicios>(0);
			
			for(DTOBusquedaMontoAgrupacionServicio agrupacion: dto.getListaAgrupacionServicios()){
				agrupacionServicio = poblarDetalleAgrupacionServicio(agrupacion);
				agrupacionServicio.setDetalleMonto(detalleMonto);
				setAgrupacionServicio.add(agrupacionServicio);				
				agrupacionServicio = new MontoAgrupacionServicios();
			}	
			detalleMonto.setMontoAgrupacionServicioses(setAgrupacionServicio);			
		}
		if(dto.getListaServicioEspecifico()!=null && dto.getListaServicioEspecifico().size()>0){
			HashSet<MontoServicioEspecifico> setServicioEspecifico = new HashSet<MontoServicioEspecifico>(0);			
			for(DTOBusquedaMontoServicioEspecifico servicioEspecifico:dto.getListaServicioEspecifico()){
				servicio = poblarDetalleServicioEspecifico(servicioEspecifico);
				servicio.setDetalleMonto(detalleMonto);
				setServicioEspecifico.add(servicio);				
			}
			detalleMonto.setMontoServicioEspecificos(setServicioEspecifico);
		}
		if(dto.getListaArticuloEspecifico()!=null && dto.getListaArticuloEspecifico().size()>0){
			HashSet<MontoArticuloEspecifico> setArticuloEspecifico = new HashSet<MontoArticuloEspecifico>(0);
			for(DTOBusquedaMontoArticuloEspecifico dtoArticulo : dto.getListaArticuloEspecifico()){
				articulo = poblarArticuloEspecifico(dtoArticulo);
				articulo.setDetalleMonto(detalleMonto);
				setArticuloEspecifico.add(articulo);
			}
			detalleMonto.setMontoArticuloEspecificos(setArticuloEspecifico);
		}		
		if(dto.getDetalleCodigo()>0){
			guardar = dao.actualizarDetalleMontoCobro(detalleMonto);
			accion=ConstantesIntegridadDominio.acronimoAccionHistoricaModificar;
		}else{
			guardar = dao.guardarDetalleMontoCobro(detalleMonto);
			
			accion=ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar;
		}
		if(guardar){
			UtilidadTransaccion.getTransaccion().commit();
			UtilidadTransaccion.getTransaccion().begin();
			if(dto.getListaAgrupacionArticulo()!=null && dto.getListaAgrupacionArticulo().size()>0){
				IMontoAgrupacionArticuloMundo agrupacionArticuloMundo = FacturacionFabricaMundo
				.crearMontoAgrupacionArticuloMundo();
				for(DTOBusquedaMontoAgrupacionArticulo registro : dto.getListaAgrupacionArticulo()){	
					if((registro.getCodigoSubgrupoInventario()!=0) &&
							(registro.getCodigoSubgrupoInventario()!=ConstantesBD.codigoNuncaValido)){
						
						registro.setGrupoCodigoConcatenado(null);
						registro.setClaseInventarioCodigo(ConstantesBD.codigoNuncaValido);
					}
					if(registro.getCodigoAgrupacionArticulo()>0){
						agrupacionArticuloMundo.actualizarMontoAgrupacionArticulo(registro);
					}else{
						registro.setDetalleCodigo(detalleMonto.getDetalleCodigo());
						agrupacionArticuloMundo.insertarMontoAgrupacionArticulo(registro);
					}	
				}						
			}						
		}	
		UtilidadTransaccion.getTransaccion().begin();	
		
		guardar = histoMundo.guardarHistoricosMontoCobro(accion,dto.getDetalleCodigo(),usuarioSesion);
		
		return guardar;
	}

	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarDetalleMontoCobro(DetalleMonto detalleMonto){
		return dao.actualizarDetalleMontoCobro(detalleMonto);
	}

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un detalle de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleMontoCobro(int idDetalleMonto){
		IMontoAgrupacionArticuloMundo agrupacionArticuloMundo = FacturacionFabricaMundo
		.crearMontoAgrupacionArticuloMundo();
		DTOBusquedaMontoAgrupacionArticulo dto = new DTOBusquedaMontoAgrupacionArticulo();
		dto.setDetalleCodigo(idDetalleMonto);
				
		agrupacionArticuloMundo.eliminarMontoAgrupacionArticuloPorDetalleID(dto);
		boolean eliminacionExitosa = dao.eliminarDetalleMontoCobro(idDetalleMonto);
		
		return eliminacionExitosa;
	}	
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private MontoAgrupacionServicios poblarDetalleAgrupacionServicio(DTOBusquedaMontoAgrupacionServicio 
			dtoAgrupacion){
			
		MontoAgrupacionServicios agrupacionServicio = null;
		
		agrupacionServicio = new MontoAgrupacionServicios();
		agrupacionServicio.setCantidadMonto(dtoAgrupacion.getCantidadMonto());
		
		if(dtoAgrupacion.getCantidadServicio()!=null && dtoAgrupacion.getCantidadServicio()>0){
			agrupacionServicio.setCantidadServicio(dtoAgrupacion.getCantidadServicio());
		}
		
		agrupacionServicio.setValorMonto(dtoAgrupacion.getValorMonto());		
		
		if(dtoAgrupacion.getCodigoAgrupacionServicio()!=null){
			if(dtoAgrupacion.getCodigoAgrupacionServicio()>0){
				agrupacionServicio.setCodigo(dtoAgrupacion.getCodigoAgrupacionServicio());
			}
		}
		
		if(dtoAgrupacion.getCodigoEspecialidad()!=null){
			if(dtoAgrupacion.getCodigoEspecialidad()>0){
				Especialidades especialidad = new Especialidades();
				especialidad.setCodigo(dtoAgrupacion.getCodigoEspecialidad());
				agrupacionServicio.setEspecialidades(especialidad);
			}
		}
		
		if(!dtoAgrupacion.getAcronimoTipoServicio().equals("")){
			TiposServicio tipoServicio = new TiposServicio();
			tipoServicio.setAcronimo(dtoAgrupacion.getAcronimoTipoServicio());
			agrupacionServicio.setTiposServicio(tipoServicio);
		}
		
		if(dtoAgrupacion.getCodigoGrupoServicio()!=null){
			if(dtoAgrupacion.getCodigoGrupoServicio()>0){
				GruposServicios grupoServicio = new GruposServicios();
				grupoServicio.setCodigo(dtoAgrupacion.getCodigoGrupoServicio());
				agrupacionServicio.setGruposServicios(grupoServicio);
			}					
		}
		return agrupacionServicio;		
	}
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private MontoServicioEspecifico poblarDetalleServicioEspecifico(DTOBusquedaMontoServicioEspecifico dtoServicio){
		MontoServicioEspecifico servicio = new MontoServicioEspecifico();
		
		if(dtoServicio.getCodigoServicioEspecifico()>0){
			servicio.setCodigo(dtoServicio.getCodigoServicioEspecifico());
		}
		if(dtoServicio.getCodigoServicio()>0){
			Servicios servicios= new Servicios(); 
			servicios.setCodigo(dtoServicio.getCodigoServicio());
			servicio.setServicios(servicios);						
		}
		if(dtoServicio.getCantidadServicio()!=null && dtoServicio.getCantidadServicio()>0){
			servicio.setCantidadServicio(dtoServicio.getCantidadServicio());			
		}				
		servicio.setCantidadMonto(dtoServicio.getCantidadMonto());
		servicio.setValorMonto(dtoServicio.getValorMonto());
		
		return servicio;
	}	
	
	/**
	 * 
	 * Este Método se encarga de
	 * @author, Angela Maria Aguirre
	 *
	 */
	private MontoArticuloEspecifico poblarArticuloEspecifico(DTOBusquedaMontoArticuloEspecifico dtoArticulo){
		MontoArticuloEspecifico articuloEspecifico = new MontoArticuloEspecifico();
		
		if(dtoArticulo.getCodigo()>0){
			articuloEspecifico.setCodigo(dtoArticulo.getCodigo());
		}
		if(dtoArticulo.getCantidadArticulos()!=null && dtoArticulo.getCantidadArticulos()>0){
			articuloEspecifico.setCantidadArticulos(dtoArticulo.getCantidadArticulos());
		}
		if(dtoArticulo.getArticuloCodigo()>0){
			articuloEspecifico.setArticulo(dtoArticulo.getArticuloCodigo());
		}		
		articuloEspecifico.setCantidadMonto(dtoArticulo.getCantidadMonto());
		articuloEspecifico.setValorMonto(dtoArticulo.getValorMonto());
		
		return articuloEspecifico;		
	}
	
	
}
