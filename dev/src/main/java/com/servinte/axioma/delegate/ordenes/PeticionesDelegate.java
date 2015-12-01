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

import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.dto.salascirugia.AnulacionPeticionQxDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.AnulacionPeticionQx;
import com.servinte.axioma.orm.EstadosPeticion;
import com.servinte.axioma.orm.MotivosAnulQxInst;
import com.servinte.axioma.orm.PeticionQx;
import com.servinte.axioma.orm.PeticionesServicio;
import com.servinte.axioma.orm.PeticionesServicioId;
import com.servinte.axioma.orm.Usuarios;

/**
 * Clase de implementa los metodos de integración con la base de datos
 * asociados a las Ordenes y Peticiones de Cirugía
 * 
 * @author ricruico
 * @version 1.0
 * @created 30-jun-2012 10:23:59 p.m.
 */
public class PeticionesDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo encargado de obtener las peticiones de cirugía
	 * pendientes por autorizar de acuerdo a los filtros de búsqueda seleccionados
	 * 
	 * @param parametrosBusqueda
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<OrdenAutorizacionDto> obtenerPeticionesQxPorAutorizar(ParametroBusquedaOrdenAutorizacionDto parametrosBusqueda) 
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
			parameters.put("peticion", ConstantesBD.claseOrdenPeticion);
			if(ValoresPorDefecto.getValorTrueParaConsultas().equals("1")){
				parameters.put("valorTrueConsulta", Integer.valueOf(1));
			}
			else{
				parameters.put("valorTrueConsulta", true);
			}
			parameters.put("tipoOrdenAmbServicio", ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
			List<Integer> estadosPeticiones= new ArrayList<Integer>();
			estadosPeticiones.add(ConstantesBD.codigoEstadoPeticionPendiente);
			estadosPeticiones.add(ConstantesBD.codigoEstadoPeticionProgramada);
			estadosPeticiones.add(ConstantesBD.codigoEstadoPeticionReprogramada);
			parameters.put("estadosPeticion", estadosPeticiones);
			parameters.put("cubierto", ConstantesBD.acronimoSi);
			parameters.put("estadoSolHCAnulada", ConstantesBD.codigoEstadoHCAnulada);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			//Se valida si se debe realizar la búsqueda teniendo en cuenta
			//el nivel de atención asociado a los servicios o Medicamentos/Insumos de la Orden Médica
			if(parametrosBusqueda.getCodigoNivelAtencion() == ConstantesBD.codigoNuncaValido){
				ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
										.createNamedQuery("autorizacionCapitacion.obtenerPeticionesQxPorAutorizar", parameters);
			}
			else{
				parameters.put("codigoNivelAtencion", parametrosBusqueda.getCodigoNivelAtencion());
				ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
										.createNamedQuery("autorizacionCapitacion.obtenerPeticionesQxPorAutorizarPorNivel", parameters);
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
	 * Metodo encargado de obtener los Servicios pendientes por autorizar para una petición de Cirugía
	 * @param codigoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioAutorizacionOrdenDto> obtenerServiciosPorAutorizar(int codigoPeticion) 
									throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoPeticion", codigoPeticion);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			parameters.put("tarifarioOficial", ConstantesBD.codigoTarifarioCups);
			return (List<ServicioAutorizacionOrdenDto>)persistenciaSvc
								.createNamedQuery("autorizacionCapitacion.obtenerServiciosPorAutorizarPeticion", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que permite realizar la búsqueda del detalle de los servicios para la petición por el número de la 
	 * petición
	 * @param codPeticion
	 * param codServicio
	 * @throws BDException
	 */
	public PeticionesServicio obtenerDetallePeticionPorId(int codPeticion, int codServicio)throws BDException
		{
		try{
			persistenciaSvc = new PersistenciaSvc();
			PeticionesServicioId peticionesServicioId = new PeticionesServicioId();
			peticionesServicioId.setPeticionQx(codPeticion);
			peticionesServicioId.setServicio(codServicio);
			return persistenciaSvc.find(PeticionesServicio.class, peticionesServicioId);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que ingresa el código de la autorización de entidad subcontratada en el detalle de la petición
	 * cuando los servicios quedan autorizados por autorizaciones de capita 
	 * @param peticionesServicio
	 * @throws BDException
	 */
	public void asociarPeticionAutorizacion(PeticionesServicio peticionesServicio)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(peticionesServicio);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la Petición
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public ContratoDto obtenerConvenioContratoPorPeticion(int codigoOrden) throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("tipoContrato", ConstantesBD.codigoTipoContratoCapitado);
			parameters.put("capitacionSubcontradada", ConstantesBD.acronimoSiChar);
			return (ContratoDto)persistenciaSvc
								.createNamedQueryFirstResult("autorizacionCapitacion.obtenerConvenioContratoPorPeticion", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Consultar el estado de una peticion qx dado su codigo
	 * 
	 * @param codigoPeticion
	 * @return dtoPeticion
	 * @throws BDException
	 * @author jeilones
	 * @created 17/08/2012
	 */
	public PeticionQxDto obtenerEstadoPeticionQxPorId(long codigoPeticion)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			HashMap<String, Object>parameters=new HashMap<String, Object>(0);
			parameters.put("codigoPeticion", codigoPeticion);
			
			List<PeticionQxDto>estados=(List<PeticionQxDto>)persistenciaSvc.createNamedQuery("catalogoManejoPaciente.consultarEstadoPeticion",parameters);
			PeticionQxDto dtoPeticion=null;
			if(!estados.isEmpty()){
				int posicionEstadoPeticion=0;
				dtoPeticion=estados.get(posicionEstadoPeticion);
			}
			return dtoPeticion;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

	/**
	 * Metodo que se encarga de anular la peticion.
	 * 
	 * @author Camilo Gómez
	 * @param anulacionPeticionQx
	 * @throws BDException
	 */
	public void anularPeticion(AnulacionPeticionQxDto anulacionDto)throws BDException
	{
		AnulacionPeticionQx anulacionPeticionQx	= null;
		MotivosAnulQxInst motivosAnulQxInst		= null;
		EstadosPeticion estadosPeticion	= null;
		PeticionQx peticionQx = null;
		Usuarios usuario	  = null;
		
		try{
			persistenciaSvc = new PersistenciaSvc();
			
			estadosPeticion	= new EstadosPeticion();
			estadosPeticion.setCodigo(ConstantesBD.codigoEstadoPeticionAnulada);
			peticionQx		= persistenciaSvc.find(PeticionQx.class, anulacionDto.getCodigoPeticion());
			peticionQx.setEstadosPeticion(estadosPeticion);
			peticionQx= persistenciaSvc.merge(peticionQx);
			
			usuario		= new Usuarios();
			usuario.setLogin(anulacionDto.getUsuario());
			motivosAnulQxInst = new MotivosAnulQxInst();
			motivosAnulQxInst.setCodigo(anulacionDto.getMotivoAnulacion());
			anulacionPeticionQx	= new AnulacionPeticionQx();
			anulacionPeticionQx.setPeticionQx(peticionQx);
			anulacionPeticionQx.setFecha(anulacionDto.getFecha());
			anulacionPeticionQx.setHora(anulacionDto.getHora());
			anulacionPeticionQx.setUsuarios(usuario);
			anulacionPeticionQx.setMotivosAnulQxInst(motivosAnulQxInst);
			anulacionPeticionQx.setComentario(anulacionDto.getComentarioAnulacion());
			persistenciaSvc.persist(anulacionPeticionQx);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
}
