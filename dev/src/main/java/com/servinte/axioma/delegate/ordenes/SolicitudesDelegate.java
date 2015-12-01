package com.servinte.axioma.delegate.ordenes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Query;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;

import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionesSolicitudesDto;
import com.servinte.axioma.dto.manejoPaciente.ParametroBusquedaOrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.AnulacionesSolicitud;
import com.servinte.axioma.orm.DetCargos;
import com.servinte.axioma.orm.DetalleSolicitudes;
import com.servinte.axioma.orm.DetalleSolicitudesId;
import com.servinte.axioma.orm.EstadosSolFact;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.SolCirugiaPorServicio;
import com.servinte.axioma.orm.SolProcedimientos;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.SolicitudesConsulta;
import com.servinte.axioma.orm.SolicitudesInter;
import com.servinte.axioma.orm.SolicitudesSubcuenta;

/**
 * Clase de implementa los metodos de integración con la base de datos
 * asociados a las Ordenes Médicas
 * 
 * @author ricruico
 * @version 1.0
 * @created 30-jun-2012 10:23:59 p.m.
 */
public class SolicitudesDelegate {

	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	/**
	 * Metodo encargado de obtener las ordenes médicas y solicitudes de cargos directos
	 * pendientes por autorizar de acuerdo a los filtros de búsqueda seleccionados
	 * 
	 * @param parametrosBusqueda
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<OrdenAutorizacionDto> obtenerOrdenesMedicasPorAutorizar(ParametroBusquedaOrdenAutorizacionDto parametrosBusqueda) 
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
			parameters.put("cargoDirecto", ConstantesBD.claseOrdenCargoDirecto);
			if(ValoresPorDefecto.getValorTrueParaConsultas().equals("1")){
				parameters.put("valorTrueConsulta", Integer.valueOf(1));
			}
			else{
				parameters.put("valorTrueConsulta", true);
			}
			parameters.put("tipoSolicitudCargoDirectoArticulo", ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos);
			parameters.put("estadoSolHCCargoDirecto", ConstantesBD.codigoEstadoHCCargoDirecto);
			parameters.put("estadoCargoCargado", ConstantesBD.codigoEstadoFCargada);
			parameters.put("cubierto", ConstantesBD.acronimoSi);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			parameters.put("tipoSolicitudCargoDirectoServicio", ConstantesBD.codigoTipoSolicitudCargosDirectosServicios);
			parameters.put("estadoAutorizacionAutorizado", ConstantesIntegridadDominio.acronimoAutorizado);
			parameters.put("tipoSolicitudCirugia", ConstantesBD.codigoTipoSolicitudCirugia);
			parameters.put("ordenMedica", ConstantesBD.claseOrdenOrdenMedica);
			parameters.put("tipoSolicitudMedicamento", ConstantesBD.codigoTipoSolicitudMedicamentos);
			parameters.put("estadoSolHCSolicitada", ConstantesBD.codigoEstadoHCSolicitada);
			parameters.put("tipoSolicitudProcedimiento", ConstantesBD.codigoTipoSolicitudProcedimiento);
			parameters.put("tipoSolicitudInterconsulta", ConstantesBD.codigoTipoSolicitudInterconsulta);
			parameters.put("tipoSolicitudConsulta", ConstantesBD.codigoTipoSolicitudCita);
			
			//Se valida si se debe realizar la búsqueda teniendo en cuenta
			//el nivel de atención asociado a los servicios o Medicamentos/Insumos de la Orden Médica
			if(parametrosBusqueda.getCodigoNivelAtencion() == ConstantesBD.codigoNuncaValido){
				ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
										.createNamedQuery("autorizacionCapitacion.obtenerOrdenesMedicasPorAutorizar", parameters);
			}
			else{
				parameters.put("codigoNivelAtencion", parametrosBusqueda.getCodigoNivelAtencion());
				ordenesPorAutorizar=(List<Object[]>)persistenciaSvc
										.createNamedQuery("autorizacionCapitacion.obtenerOrdenesMedicasPorAutorizarPorNivel", parameters);
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
	 * Metodo encargado de obtener los Medicamentos/Insumos pendientes por autorizar para una orden medica
	 * 
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorAutorizar(int codigoOrden) 
									throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
			
			return (List<MedicamentoInsumoAutorizacionOrdenDto>)persistenciaSvc
								.createNamedQuery("autorizacionCapitacion.obtenerMedicamentosInsumosPorAutorizarOrdenMedica", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener los Servicios pendientes por autorizar para una orden medica
	 * 
	 * @param codigoOrden
	 * @param claseOrden
	 * @param tipoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioAutorizacionOrdenDto> obtenerServiciosPorAutorizar(int codigoOrden, int claseOrden, int tipoOrden) 
									throws BDException{
		List<ServicioAutorizacionOrdenDto> serviciosPorAutorizar = null;
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("tarifarioOficial", ConstantesBD.codigoTarifarioCups);
			if(claseOrden == ConstantesBD.claseOrdenOrdenMedica){
				parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
				if(tipoOrden == ConstantesBD.codigoTipoSolicitudCirugia){
					serviciosPorAutorizar=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
											.createNamedQuery("autorizacionCapitacion.obtenerServiciosCirugiaPorAutorizarOrdenMedica", 
													parameters);
				}
				else if(tipoOrden == ConstantesBD.codigoTipoSolicitudProcedimiento){
					serviciosPorAutorizar=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
											.createNamedQuery("autorizacionCapitacion.obtenerProcedimientosPorAutorizarOrdenMedica", 
													parameters);
				}
				else if(tipoOrden == ConstantesBD.codigoTipoSolicitudInterconsulta){
					serviciosPorAutorizar=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
											.createNamedQuery("autorizacionCapitacion.obtenerInterconsultasPorAutorizarOrdenMedica", 
													parameters);
				}
				else if(tipoOrden == ConstantesBD.codigoTipoSolicitudCita){
					serviciosPorAutorizar=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
											.createNamedQuery("autorizacionCapitacion.obtenerConsultasPorAutorizarOrdenMedica", 
													parameters);
				}
			}
			else if(claseOrden == ConstantesBD.claseOrdenCargoDirecto){
				if(tipoOrden == ConstantesBD.codigoTipoSolicitudCirugia){
					//Se obtienen los otros servicios del cargo directo de cirugia
//					List<ServicioAutorizacionOrdenDto> otrosServicios=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
//											.createNamedQuery("autorizacionCapitacion.obtenerOtrosServiciosCirugiaPorAutorizarCargoDirecto", 
//													parameters);
					parameters.put("estadoAutorizacionAnulada", ConstantesIntegridadDominio.acronimoEstadoAnulado);
					//Se obtiene el servicio principal Es la misma consulta que para ordenes médicas de cirugia
					serviciosPorAutorizar=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
											.createNamedQuery("autorizacionCapitacion.obtenerServiciosCirugiaPorAutorizarOrdenMedica", 
													parameters);
//					if(!otrosServicios.isEmpty()){
//						for(ServicioAutorizacionOrdenDto otro:otrosServicios){
//							boolean valido=true;
//							for(ServicioAutorizacionOrdenDto principal:serviciosPorAutorizar){
//								if(principal.getCodigo()==otro.getCodigo()){
//									valido=false;
//								}
//							}
//							if(valido){
//								serviciosPorAutorizar.add(otro);
//							}
//						}
//					}
				}
				else if(tipoOrden == ConstantesBD.codigoTipoSolicitudCargosDirectosServicios){
					parameters.put("estadoAutorizacionAutorizada", ConstantesIntegridadDominio.acronimoAutorizado);
					serviciosPorAutorizar=(List<ServicioAutorizacionOrdenDto>)persistenciaSvc
											.createNamedQuery("autorizacionCapitacion.obtenerServiciosPorAutorizarCargoDirecto", 
													parameters);
				}
			}
			return serviciosPorAutorizar;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Método que se encarga de buscar la solicitud de Consulta (Cita).
	 * @param numeroSolicitud
	 * @return SolicitudesConsulta
	 * @throws BDException
	 */
	public SolicitudesConsulta obtenerSolicitudConsultaPorId(int numeroSolicitud)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			return persistenciaSvc.find(SolicitudesConsulta.class, numeroSolicitud);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Consulta (Cita) generada con la autorización.
	 * @param solicitudesConsulta
	 * @throws BDException
	 */
	public void actualizarSolicitudConsulta(SolicitudesConsulta solicitudesConsulta)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(solicitudesConsulta);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de buscar la solicitud de Medicamentos.
	 * @param numeroSolicitud
	 * @param articulo
	 * @return DetalleSolicitudes
	 * @throws BDException
	 */
	public DetalleSolicitudes obtenerDetalleSolicitudMedicamentosPorId(int numeroSolicitud,int articulo)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			DetalleSolicitudesId detalleSolicitudesId=new DetalleSolicitudesId();
			detalleSolicitudesId.setNumeroSolicitud(numeroSolicitud);
			detalleSolicitudesId.setArticulo(articulo);
			return persistenciaSvc.find(DetalleSolicitudes.class, detalleSolicitudesId);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Medicamentos generada con la autorización.
	 * @param detalleSolicitudes
	 * @throws BDException
	 */
	public void actualizarSolicitudMedicamentos(DetalleSolicitudes detalleSolicitudes)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(detalleSolicitudes);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de buscar la solicitud de Cirugia por Servicio.
	 * @param numeroSolicitud
	 * @param servicio
	 * @return SolCirugiaPorServicio
	 * @throws BDException
	 */
	public SolCirugiaPorServicio obtenerSolCirugiaPorServicioPorId(int numeroSolicitud,int servicio)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("numeroSolicitud", numeroSolicitud);
			parameters.put("servicio", servicio);
			return (SolCirugiaPorServicio) persistenciaSvc.
					createNamedQueryUniqueResult("solicitudes.obtenerSolCirugiaPorServicioPorId",parameters);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de cirugia generada con la autorización.
	 * @param solCirugiaPorServicio
	 * @throws BDException
	 */
	public void actualizarSolCirugiaPorServicio(SolCirugiaPorServicio solCirugiaPorServicio)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(solCirugiaPorServicio);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de buscar la solicitud de Cirugia por Servicio.
	 * @param numeroSolicitud
	 * @param servicio
	 * @return SolCirugiaPorServicio
	 * @throws BDException
	 */
	public SolProcedimientos obtenerSolProcedimientosPorId(int numeroSolicitud)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			return persistenciaSvc.find(SolProcedimientos.class, numeroSolicitud);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de actualizar la solicitud de procedimiento
	 * @param solProcedimientos
	 * @throws BDException
	 */
	public void actualizarSolProcedimientos(SolProcedimientos solProcedimientos)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(solProcedimientos);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la orden médica
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public ContratoDto obtenerConvenioContratoPorOrdenMedica(int codigoOrden, int claseOrden, int tipoOrden) throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			ContratoDto dtoContrato=null;
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoOrden", codigoOrden);
			parameters.put("tipoContrato", ConstantesBD.codigoTipoContratoCapitado);
			parameters.put("capitacionSubcontradada", ConstantesBD.acronimoSiChar);
			if((claseOrden==ConstantesBD.claseOrdenOrdenMedica && tipoOrden==ConstantesBD.tipoSolicitudCirugia)
					|| (claseOrden==ConstantesBD.claseOrdenCargoDirecto && tipoOrden==ConstantesBD.tipoSolicitudCirugia)){
				dtoContrato= (ContratoDto)persistenciaSvc
								.createNamedQueryFirstResult("autorizacionCapitacion.obtenerConvenioContratoPorOrdenMedicaCirugia", 
										parameters);
			}
			else{
				dtoContrato= (ContratoDto)persistenciaSvc
								.createNamedQueryFirstResult("autorizacionCapitacion.obtenerConvenioContratoPorOrdenMedica", 
										parameters);
			}
			return dtoContrato;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Retorna las solicitudes de una autorizacion y que provienen de una orden ambulatoria
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Jeison Londono
	 */
	@SuppressWarnings("unchecked")
	public List<AutorizacionesSolicitudesDto> obtenerSolicitudesAutorizacionConOrdenAmbulatoria(
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionEntSubcontratadasCapitacion) throws BDException{
		List<AutorizacionesSolicitudesDto>listaSolicitudesAutorizacion=new ArrayList<AutorizacionesSolicitudesDto>(0);

		try{
			persistenciaSvc = new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("consecutivoAutoEntSub", dtoAutorizacionEntSubcontratadasCapitacion.getCodigoAutorizacionEntSub());

			List<Object[]> resultados=(List<Object[]>)persistenciaSvc.
					createNamedQuery("solicitudes.obtenerSolicitudesAutorizadasRelacionadasOrdenAmb",parameters);
			
			for(Object[] solicitud:resultados){
				AutorizacionesSolicitudesDto autorizacionesSolicitudesDto=new AutorizacionesSolicitudesDto(
						(Long)solicitud[0],
						(Long)solicitud[1],
						(Date)solicitud[2],
						((Integer)solicitud[3]).intValue()==AutorizacionesSolicitudesDto.SOL_ORDEN_AMB,
						((Integer)solicitud[3]).intValue()==AutorizacionesSolicitudesDto.SOL_PETICION_QX);
				listaSolicitudesAutorizacion.add(autorizacionesSolicitudesDto);
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return listaSolicitudesAutorizacion;
	}
	
	
	/**
	 * Metodo que se encarga de guardar en la entidad AnulacionesSolicitud.
	 * @param anulacionesSolicitud
	 * @throws BDException
	 */
	public void guardarAnulacionesSolicitud(AnulacionAutorizacionSolicitudDto anulacionDto)throws BDException
	{
		AnulacionesSolicitud anulacionesSolicitud = null;
		Medicos medicos	= null;
		try{
			persistenciaSvc 	= new PersistenciaSvc();
			anulacionesSolicitud= new AnulacionesSolicitud();
			anulacionesSolicitud.setMotivo(anulacionDto.getMotivoAnulacion());
			anulacionesSolicitud.setFecha(anulacionDto.getFechaAnulacion());
			anulacionesSolicitud.setHora(anulacionDto.getHoraAnulacion());
			
			medicos	= new Medicos();
			medicos.setCodigoMedico(anulacionDto.getMedicoAnulacion().getCodigoMedico());
			anulacionesSolicitud.setMedicos(medicos);
			
			Solicitudes solicitudes = obtenerSolicitudesPorId(anulacionDto.getNumeroSolicitud());
			anulacionesSolicitud.setSolicitudes(solicitudes);
			
			persistenciaSvc.persist(anulacionesSolicitud);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo que se encarga de obtener la entidad de Solicitudes
	 * @param numero Solicitud
	 * @return Solicitudes
	 * @throws BDException
	 */
	public Solicitudes obtenerSolicitudesPorId(int numeroSolicitud)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			return persistenciaSvc.find(Solicitudes.class, numeroSolicitud);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo que se encarga de actualizar el estado de la Solicitud con anulacion.
	 * @param numeroSolicitud
	 * @throws BDException
	 */
	public void anularSolicitudes(int numeroSolicitud)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			Solicitudes solicitudes = obtenerSolicitudesPorId(numeroSolicitud);
			solicitudes.setEstadoHistoriaClinica(ConstantesBD.codigoEstadoHCAnulada);
			persistenciaSvc.merge(solicitudes);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Metodo que se encarga de anular la entidad DetCargos por Solicitud.
	 * @param numeroSolicitud
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public void anularDetCargosPorSolicitud(int numeroSolicitud)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("numeroSolicitud", numeroSolicitud);
			List<DetCargos> detCargos=(List<DetCargos>) persistenciaSvc.
					createNamedQuery("solicitudes.obtenerDetCargosPorSolicitud",parameters);
			
			for (DetCargos detCargos2 : detCargos) {
				EstadosSolFact estadosSolFact	= new EstadosSolFact();
				estadosSolFact.setCodigo(ConstantesBD.codigoEstadoFAnulada);
				detCargos2.setEstadosSolFact(estadosSolFact);
				persistenciaSvc.merge(detCargos2);
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	 * Método que se encarga de buscar la solicitud de Consulta (Cita).
	 * @param numeroSolicitud
	 * @return SolicitudesConsulta
	 * @throws BDException
	 */
	public SolicitudesInter obtenerSolicitudInterConsultaPorId(int numeroSolicitud)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			return persistenciaSvc.find(SolicitudesInter.class, numeroSolicitud);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	 * Método que se encarga de asociar la solicitud de Interconsulta generada con la autorización.
	 * @param solicitudesConsulta
	 * @throws BDException
	 */
	public void actualizarSolicitudInterconsulta(SolicitudesInter SolicitudesInterConsulta)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			persistenciaSvc.merge(SolicitudesInterConsulta);
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	
	/**
	 * Metodo encargado de obtener los Medicamentos/Insumos con o sin autorización asociada
	 * @param codigoOrden
	 * @return List<MedicamentoInsumoAutorizacionOrdenDto>
	 * @author DiaRuiPe
	 * @throws IPSException
	 */
	@SuppressWarnings("unchecked")
	public List<MedicamentoInsumoAutorizacionOrdenDto> obtenerMedicamentosInsumosPorSolicitud(int codigoOrden) 
			throws BDException{
		try{
		Map<String,Object> parameters = new HashMap<String, Object>();
		persistenciaSvc = new PersistenciaSvc();
		parameters.put("codigoOrden", codigoOrden);
		
		return (List<MedicamentoInsumoAutorizacionOrdenDto>)persistenciaSvc
				.createNamedQuery("autorizacionCapitacion.obtenerMedicamentosInsumosPorSolicitud", 
						parameters);
		}
		catch (Exception e) {
		Log4JManager.error(e);
		throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Retorna La entidad Ingreso por numero de solicitud 
	 * @param idSolicitud
	 * @return {@link com.servinte.axioma.orm.Ingresos}
	 * @throws BDException
	 * @author javrammo
	 */
	public  Ingresos  obtenerIngresoPorNumeroSolicitud(int idSolicitud) throws BDException{		
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("numeroSolicitud", idSolicitud);		
			return (Ingresos)persistenciaSvc.createNamedQueryFirstResult("solicitudes.obtenerIngresoPorNumeroSolicitud",parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}	
	
	/**
	 * Permite actualizar la cobertura de la solicitud sub cuenta
	 * 
	 * @throws BDException
	 * @author jeilones
	 * @created 5/02/2013
	 */
	public void actualizarSolicitudSubcuenta(SolicitudesSubcuenta solicitudesSubcuenta)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			
			String consultarSolicitudSubCuenta="SELECT solSubCuenta FROM "+SolicitudesSubcuenta.class.getName()+" solSubCuenta ";
			String whereSolicitudesSubCuenta="WHERE ";
			
			
			if(solicitudesSubcuenta.getSolicitudes().getNumeroSolicitud()!=ConstantesBD.codigoNuncaValido){
				consultarSolicitudSubCuenta+="INNER JOIN solSubCuenta.solicitudes sol ";
				whereSolicitudesSubCuenta+="sol.numeroSolicitud = "+solicitudesSubcuenta.getSolicitudes().getNumeroSolicitud()+" AND ";
			}
			if(solicitudesSubcuenta.getArticulo().getCodigo()!=ConstantesBD.codigoNuncaValido){
				consultarSolicitudSubCuenta+="INNER JOIN solSubCuenta.articulo art ";
				whereSolicitudesSubCuenta+=" art.codigo = "+solicitudesSubcuenta.getArticulo().getCodigo()+" AND ";
			}
			if(solicitudesSubcuenta.getServiciosByServicio().getCodigo()!=ConstantesBD.codigoNuncaValido){
				consultarSolicitudSubCuenta+="INNER JOIN solSubCuenta.serviciosByServicio serv ";
				whereSolicitudesSubCuenta+=" serv.codigo= "+solicitudesSubcuenta.getServiciosByServicio().getCodigo()+" AND ";
			}
			if(solicitudesSubcuenta.getSubCuentas().getSubCuenta()!=ConstantesBD.codigoNuncaValido){
				consultarSolicitudSubCuenta+="INNER JOIN solSubCuenta.subCuentas subCuenta ";
				whereSolicitudesSubCuenta+=" subCuenta.subCuenta= "+solicitudesSubcuenta.getSubCuentas().getSubCuenta()+" AND ";
			}
			if(solicitudesSubcuenta.getCodigo()!=ConstantesBD.codigoNuncaValido){
				whereSolicitudesSubCuenta+=" solSubCuenta.codigo= "+solicitudesSubcuenta.getCodigo()+" AND ";
			}
			if(solicitudesSubcuenta.getCuenta()!=ConstantesBD.codigoNuncaValido){
				whereSolicitudesSubCuenta+=" solSubCuenta.cuenta= "+solicitudesSubcuenta.getCuenta();
			}
			consultarSolicitudSubCuenta+=whereSolicitudesSubCuenta;
			/*String actualizarStr="UPDATE ordenes.solicitudes_subcuenta SET cobertura = '"+solicitudesSubcuenta.getCubierto()+"', " +
					"usuario_modifica= '"+solicitudesSubcuenta.getUsuarios().getLogin()+"' "+
					"WHERE ";
			if(solicitudesSubcuenta.getSolicitudes().getNumeroSolicitud()!=ConstantesBD.codigoNuncaValido)
				actualizarStr+=" solicitud = "+solicitudesSubcuenta.getSolicitudes().getNumeroSolicitud()+" AND ";
			if(solicitudesSubcuenta.getArticulo().getCodigo()!=ConstantesBD.codigoNuncaValido)
				actualizarStr+=" articulo= "+solicitudesSubcuenta.getArticulo().getCodigo()+" AND ";
			if(solicitudesSubcuenta.getServiciosByServicio().getCodigo()!=ConstantesBD.codigoNuncaValido)
				actualizarStr+=" servicio= "+solicitudesSubcuenta.getServiciosByServicio().getCodigo()+" AND ";
			if(solicitudesSubcuenta.getSubCuentas().getSubCuenta()!=ConstantesBD.codigoNuncaValido)
				actualizarStr+=" sub_cuenta= "+solicitudesSubcuenta.getSubCuentas().getSubCuenta()+" AND ";
			if(solicitudesSubcuenta.getCodigo()!=ConstantesBD.codigoNuncaValido)
				actualizarStr+=" codigo= "+solicitudesSubcuenta.getCodigo()+" AND ";
			if(solicitudesSubcuenta.getCuenta()!=ConstantesBD.codigoNuncaValido)
				actualizarStr+=" cuenta= "+solicitudesSubcuenta.getCuenta();*/
			
			if(consultarSolicitudSubCuenta.trim().endsWith(" AND ")){
				consultarSolicitudSubCuenta=consultarSolicitudSubCuenta.substring(0,consultarSolicitudSubCuenta.length()-5);
			}
			
			Query query=persistenciaSvc.getSession().createQuery(consultarSolicitudSubCuenta);
			SolicitudesSubcuenta solicitudesSubcuenta2=(SolicitudesSubcuenta)query.uniqueResult();
			
			solicitudesSubcuenta2.setCubierto(solicitudesSubcuenta.getCubierto());
			solicitudesSubcuenta2.setUsuarios(solicitudesSubcuenta.getUsuarios());
			
			persistenciaSvc.merge(solicitudesSubcuenta2);
			
			//SQLQuery sqlQuery= persistenciaSvc.getSession().createSQLQuery(consultarSolicitudSubCuenta);
			
			//sqlQuery.executeUpdate();
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	 * Permite actualizar la cobertura de la solicitud sub cuenta
	 * 
	 * @throws BDException
	 * @author jeilones
	 * @created 5/02/2013
	 */
	@SuppressWarnings("unchecked")
	public void actualizarDetCargo(DetCargos detCargos)throws BDException
	{
		try{
			persistenciaSvc = new PersistenciaSvc();
			
			String consultaDetCargos="SELECT detCargo FROM "+DetCargos.class.getName()+" detCargo ";
			String whereSolicitudesSubCuenta="WHERE detCargo.facturado='"+ConstantesBD.acronimoNo+"' ";
			
			if(detCargos.getSolicitudes().getNumeroSolicitud()!=ConstantesBD.codigoNuncaValido){
				consultaDetCargos+="INNER JOIN detCargo.solicitudes sol ";
				whereSolicitudesSubCuenta+="AND sol.numeroSolicitud = "+detCargos.getSolicitudes().getNumeroSolicitud();
			}
			if(detCargos.getArticulo().getCodigo()!=ConstantesBD.codigoNuncaValido){
				consultaDetCargos+="INNER JOIN detCargo.articulo art ";
				whereSolicitudesSubCuenta+="AND art.codigo = "+detCargos.getArticulo().getCodigo();
			}
			
			consultaDetCargos+=whereSolicitudesSubCuenta;
			
			Query query=persistenciaSvc.getSession().createQuery(consultaDetCargos);
			List<DetCargos>listaDetCargos=(List<DetCargos>)query.list();
			
			/*
			 * MT 5880, debe evaluar la cobertura para todos los articulos 
			 * segun DCU 437 v1.3 - PROCESO GENERAL, DE LA VALIDACIÓN DE COBERTURA DE SERVICIOS ARTÍCULOS
			 * incluyendo los que existen desde la creacion de la solicitud como los que se agregan en la modificacion
			 * 
			 * jeilones
			 */
			
			for(DetCargos detCargos2:listaDetCargos){
				detCargos2.setCubierto(detCargos.getCubierto());
				detCargos2.setUsuarios(detCargos.getUsuarios());
				detCargos2.setFechaModifica(detCargos.getFechaModifica());
				detCargos2.setHoraModifica(detCargos.getHoraModifica());
				persistenciaSvc.merge(detCargos2);
			}
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}

}