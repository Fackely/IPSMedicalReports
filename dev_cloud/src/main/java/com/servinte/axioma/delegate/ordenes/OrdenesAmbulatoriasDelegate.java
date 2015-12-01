package com.servinte.axioma.delegate.ordenes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.servinte.axioma.delegate.pyp.ActividadesProgramasPypDelegate;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.AnulacionOrdenAmbulatoriaDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAmbulatoriaDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ActProgPypPac;
import com.servinte.axioma.orm.AnulacionOrdenAmbulatorias;
import com.servinte.axioma.orm.DetOrdenAmbArticulo;
import com.servinte.axioma.orm.DetOrdenAmbArticuloId;
import com.servinte.axioma.orm.DetOrdenAmbServicio;
import com.servinte.axioma.orm.EstadosOrdAmbulatorias;
import com.servinte.axioma.orm.OrdenesAmbulatorias;
import com.servinte.axioma.orm.Usuarios;

/**
 * Clase de implementa los metodos de integración con la base de datos
 * asociados a las Ordenes Ambulatorias
 * 
 * @author ricruico
 * @version 1.0
 * @created 30-jun-2012 10:23:59 p.m.
 */
public class OrdenesAmbulatoriasDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo encargado de obtener las ordenes ambulatorias
	 * pendientes por autorizar de acuerdo a los filtros de búsqueda seleccionados
	 * 
	 * @param parametrosBusqueda
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<OrdenAutorizacionDto> obtenerOrdenesAmbulatoriasPorAutorizar(ParametroBusquedaOrdenAutorizacionDto parametrosBusqueda) 
												throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			Calendar calendar= Calendar.getInstance();
			Calendar fechaActual= Calendar.getInstance();
			fechaActual.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			List<OrdenAutorizacionDto> ordenesPorRango=new ArrayList<OrdenAutorizacionDto>();
			List<Object[]> ordenesPorAutorizar=null;
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("fechaInicio", parametrosBusqueda.getFechaInicio());
			parameters.put("fechaFin", parametrosBusqueda.getFechaFin());
			parameters.put("fechaActual", fechaActual.getTime());
			parameters.put("codigoContrato", parametrosBusqueda.getCodigoContrato());
			parameters.put("ordenAmbulatoria", ConstantesBD.claseOrdenOrdenAmbulatoria);
			if(ValoresPorDefecto.getValorTrueParaConsultas().equals("1")){
				parameters.put("valorTrueConsulta", Integer.valueOf(1));
			}
			else{
				parameters.put("valorTrueConsulta", true);
			}
			parameters.put("tipoOrdenAmbArticulo", ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos);
			parameters.put("estadoOrdenAmbPendiente", ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
			parameters.put("cubierto", ConstantesBD.acronimoSi);
			parameters.put("estadoSolHCAnulada", ConstantesBD.codigoEstadoHCAnulada);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			parameters.put("tipoOrdenAmbServicio", ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
			//Se valida si se debe realizar la búsqueda teniendo en cuenta
			//el nivel de atención asociado a los servicios o Medicamentos/Insumos de la Orden Médica
			if(parametrosBusqueda.getCodigoNivelAtencion() == ConstantesBD.codigoNuncaValido){
				ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
										.createNamedQuery("autorizacionCapitacion.obtenerOrdenesAmbulatoriasPorAutorizar", parameters);
			}
			else{
				parameters.put("codigoNivelAtencion", parametrosBusqueda.getCodigoNivelAtencion());
				ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
										.createNamedQuery("autorizacionCapitacion.obtenerOrdenesAmbulatoriasPorAutorizarPorNivel", parameters);
			}
			if(ordenesPorAutorizar != null && !ordenesPorAutorizar.isEmpty()){
			for(Object[] orden:ordenesPorAutorizar){
				OrdenAutorizacionDto ordenDto= new OrdenAutorizacionDto((Long)orden[0],
					(String)orden[1], (Integer)orden[2],(Integer)orden[3],(Date)orden[4],
					(String)orden[5], (Integer)orden[6], (String)orden[7], (Integer)orden[8],
					(String)orden[9], (Date)orden[10], (String)orden[11], (Integer)orden[12],
					(String)orden[13], (String)orden[14], (String)orden[15], (String)orden[16], 
					(String)orden[17], (String)orden[18]);
				ordenesPorRango.add(ordenDto);
			}
			}
			return ordenesPorRango;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos pendientes por autorizar para una orden ambulatoria
	 * 
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorAutorizar(Long codigoOrden) 
									throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			return (List<MedicamentoInsumoAutorizacionOrdenDto>)persistenciaSvc
								.createNamedQuery("autorizacionCapitacion.obtenerMedicamentosInsumosPorAutorizarOrdenAmbulatoria", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener los Servicios pendientes por autorizar para una orden ambulatoria
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioAutorizacionOrdenDto> obtenerServiciosPorAutorizar(Long codigoOrden) 
									throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			parameters.put("tarifarioOficial", ConstantesBD.codigoTarifarioCups);
			return (List<ServicioAutorizacionOrdenDto>)persistenciaSvc
								.createNamedQuery("autorizacionCapitacion.obtenerServiciosPorAutorizarOrdenAmbulatoria", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que permite realizar la búsqueda del detalle de la orden ambulatoria de servicios por el número de la 
	 * orden ambulatoria 
	 * @param ordenAmbulatoria
	 * @throws BDException
	 * 
	 */
	public DetOrdenAmbServicio obtenerDetalleOrdenAmbServiciosPorId(long ordenAmbulatoria)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			return persistenciaSvc.find(DetOrdenAmbServicio.class, ordenAmbulatoria);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que permite realizar la búsqueda del detalle de la orden ambulatoria de medicamentos por el número de la 
	 * orden ambulatoria
	 * @param ordenAmbulatoria
	 * @return
	 * @throws BDException
	 */
	public DetOrdenAmbArticulo obtenerDetalleOrdenAmbMedicamentosPorId(long ordenAmbulatoria, int codigoArti)throws BDException
		{
		try{
			persistenciaSvc = new PersistenciaSvc();
			OrdenesAmbulatorias ordenesAmbulatorias = new OrdenesAmbulatorias();
			ordenesAmbulatorias.setCodigo(ordenAmbulatoria);
			DetOrdenAmbArticuloId detOrdenAmbArticuloId = new DetOrdenAmbArticuloId();
			detOrdenAmbArticuloId.setCodigoOrden(ordenAmbulatoria);
			detOrdenAmbArticuloId.setArticulo(codigoArti);
			return persistenciaSvc.find(DetOrdenAmbArticulo.class, detOrdenAmbArticuloId);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que ingresa el código de la autorización de entidad subcontratada en el detalle de la orden
	 * ambulatora de servicios cuando estos quedan autorizados por autorizaciones de capita 
	 * @param detalleOrdenAmbServicio
	 * @throws BDException
	 */
	public void asociarOrdenAmbServiciosAutorizacion(DetOrdenAmbServicio detalleOrdenAmbServicio)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(detalleOrdenAmbServicio);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
	}
	
	/**
	 * Método que ingresa el código de la autorización de entidad subcontratada en el detalle de la orden
	 * ambulatora de Medicamentos cuando estos quedan autorizados por autorizaciones de capita 
	 * @param detalleOrdenAmbArticulo
	 * @throws BDException
	 */
	public void asociarOrdenAmbMedicamentosAutorizacion(DetOrdenAmbArticulo detalleOrdenAmbArticulo)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(detalleOrdenAmbArticulo);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
		}
	}
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la orden ambulatoria
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public ContratoDto obtenerConvenioContratoPorOrdenAmbulatoria(Long codigoOrden, int tipoOrden) throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("tipoContrato", ConstantesBD.codigoTipoContratoCapitado);
			parameters.put("capitacionSubcontradada", ConstantesBD.acronimoSiChar);
			String nombreQuery= null;
			if(tipoOrden==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
				nombreQuery="autorizacionCapitacion.obtenerConvenioContratoPorOrdenAmbulatoriaArticulo";
			}
			else if(tipoOrden==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
				nombreQuery="autorizacionCapitacion.obtenerConvenioContratoPorOrdenAmbulatoriaServicio";
			}
			return (ContratoDto)persistenciaSvc.createNamedQueryFirstResult(nombreQuery,parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	/**
	 * Consultar una orden ambulatoria dado su codigo
	 * 
	 * @param codigoOrden
	 * @return ordenAmbultaria
	 * @throws BDException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	public OrdenAmbulatoriaDto obtenerEstadoOrdenesAmbulatoriasPorId(long codigoOrden)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			HashMap<String, Object>parameters=new HashMap<String, Object>(0);
			parameters.put("codigoOrden", codigoOrden);
	
			List<OrdenAmbulatoriaDto>estados=(List<OrdenAmbulatoriaDto>)persistenciaSvc.createNamedQuery("catalogoManejoPaciente.consultarEstadoOrdenAmbulatoria",parameters);
			OrdenAmbulatoriaDto ordenAmbulatoriaDto=null;
			if(!estados.isEmpty()){
				int posicionEstadoOrden=0;
				ordenAmbulatoriaDto=estados.get(posicionEstadoOrden);
			}
			return ordenAmbulatoriaDto;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo que se encarga de anular la Orden Ambulatoria
	 * 
	 * @author Camilo Gómez
	 * @param dto
	 * @throws BDException
	 */
	public void anularOrdenAmbulatoria(AnulacionOrdenAmbulatoriaDto dto)throws BDException
	{
		OrdenesAmbulatorias ordenAmbulatoria		= null;
		EstadosOrdAmbulatorias estadoOrdAmbulatoria = null;
		AnulacionOrdenAmbulatorias anulacionOrdenAmbulatorias = null;
		Usuarios usuario = null;
		try{
			persistenciaSvc = new PersistenciaSvc();
			anulacionOrdenAmbulatorias = new AnulacionOrdenAmbulatorias();
			usuario				= new Usuarios();
			usuario.setLogin(dto.getUsuario());
			ordenAmbulatoria	= new OrdenesAmbulatorias();
			ordenAmbulatoria.setCodigo(dto.getCodigoOrden());
			anulacionOrdenAmbulatorias.setOrdenesAmbulatorias(ordenAmbulatoria);
			anulacionOrdenAmbulatorias.setUsuarios(usuario);
			anulacionOrdenAmbulatorias.setFecha(dto.getFecha());
			anulacionOrdenAmbulatorias.setHora(dto.getHora());
			anulacionOrdenAmbulatorias.setMotivoAnulacion(dto.getMotivoAnulacion());
			persistenciaSvc.persist(anulacionOrdenAmbulatorias);
			
			ordenAmbulatoria = persistenciaSvc.find(OrdenesAmbulatorias.class, dto.getCodigoOrden());
			estadoOrdAmbulatoria = new EstadosOrdAmbulatorias();
			estadoOrdAmbulatoria.setCodigo(dto.getCodigoEstadoOrden());
			ordenAmbulatoria.setEstadosOrdAmbulatorias(estadoOrdAmbulatoria);
			persistenciaSvc.merge(ordenAmbulatoria);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo que se encarga de actualizar Actividad Programa PYP Paciente
	 * 
	 * @author Camilo Gómez
	 * @param anulacionDto
	 * @throws BDException
	 */
	public void actualizarActividadProgramaPyPPaciente(AnulacionOrdenAmbulatoriaDto anulacionDto)throws BDException
	{
		ActProgPypPac actProgPypPac = null;
		Usuarios usuario = null;
		ActividadesProgramasPypDelegate pypDelegate = null;
		try{
			persistenciaSvc = new PersistenciaSvc();
			pypDelegate		= new ActividadesProgramasPypDelegate();
			actProgPypPac	= pypDelegate.obtenerActiProgPYPPacPorOrdenAmbulatoria(anulacionDto.getCodigoOrden());
			usuario	= new Usuarios();
			usuario.setLogin(anulacionDto.getUsuario());
			actProgPypPac.setUsuariosByUsuarioCancelar(usuario);
			actProgPypPac.setEstado(anulacionDto.getCodigoEstadoOrden());
			actProgPypPac.setFechaCancelar(anulacionDto.getFecha());
			actProgPypPac.setHoraCancelar(anulacionDto.getHora());
			persistenciaSvc.merge(actProgPypPac);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
}
