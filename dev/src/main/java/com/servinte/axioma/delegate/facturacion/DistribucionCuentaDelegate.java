package com.servinte.axioma.delegate.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dto.facturacion.BackupDetCargosArtConsumoDto;
import com.servinte.axioma.dto.facturacion.BackupDetCargosDto;
import com.servinte.axioma.dto.facturacion.BackupSolicitudesSubCuentaDto;
import com.servinte.axioma.dto.facturacion.BackupSubCuentaDto;
import com.servinte.axioma.dto.facturacion.InfoCreacionHistoricoCargosDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.BackupDetCargos;
import com.servinte.axioma.orm.BackupDetCargosArtConsumo;
import com.servinte.axioma.orm.BackupSolicitudesSubcuenta;
import com.servinte.axioma.orm.BackupSubCuentas;
import com.servinte.axioma.orm.LogDistribucionCuenta;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Usuarios;


/**
 *  Clase que permite el acceso a datos de la Distribucion de Cuenta
 * 
 * @author hermorhu
 * @created 24-Nov-2012 
 */
public class DistribucionCuentaDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Metodo encargado de guardar la cabecera para el backup
	 * @param infoCreacionHistoricoCargosDto
	 * @return idLogDistribucionCuenta
	 * @throws BDException
	 * @author hermorhu
	 */
	public Long  guardarLogDistribucionCuenta(InfoCreacionHistoricoCargosDto infoCreacionHistoricoCargosDto) throws BDException{
		LogDistribucionCuenta logDistribucionCuenta = null;
		Long idLogDistribucionCuenta;
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			
			logDistribucionCuenta = new LogDistribucionCuenta();
			
			logDistribucionCuenta.setFechaModifica(infoCreacionHistoricoCargosDto.getFecha());
			logDistribucionCuenta.setHoraModifica(infoCreacionHistoricoCargosDto.getHora());
			logDistribucionCuenta.setDescripcion(infoCreacionHistoricoCargosDto.getDescripcion());
			logDistribucionCuenta.setTipoDistribucion(infoCreacionHistoricoCargosDto.getTipoDistribucion());
			logDistribucionCuenta.setClaseDistribucion(infoCreacionHistoricoCargosDto.getClaseDistribucion());
			
			Usuarios usuario = new Usuarios();
			usuario.setLogin(infoCreacionHistoricoCargosDto.getUsuario());
			logDistribucionCuenta.setUsuarios(usuario);
			
			Pacientes paciente = new Pacientes();
			paciente.setCodigoPaciente(infoCreacionHistoricoCargosDto.getCodigoPaciente());
			logDistribucionCuenta.setPacientes(paciente);
			
			Ingresos ingreso = new Ingresos();
			ingreso.setId(infoCreacionHistoricoCargosDto.getCodigoIngreso());
			logDistribucionCuenta.setIngresos(ingreso);
			
			logDistribucionCuenta = persistenciaSvc.merge(logDistribucionCuenta);
			idLogDistribucionCuenta = logDistribucionCuenta.getId();
		
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
		return idLogDistribucionCuenta;
	}
	
	
	/**
	* Método que obtiene toda la información de las sub cuentas 
	* asociadas a un ingreso para generar el correspondiente backUp
	* @author ricruico
	* @param codigoIngreso
	* @return List<BackupSubCuentaDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public List<BackupSubCuentaDto> obtenerInfoBackUpSubCuentas(int codigoIngreso) throws IPSException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			List<BackupSubCuentaDto> backUpSubCuentas= new ArrayList<BackupSubCuentaDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoIngreso", codigoIngreso);
			backUpSubCuentas=(List<BackupSubCuentaDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpSubCuentas", params);
			return backUpSubCuentas;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	* Método que genera toda la información de los det_cargos 
	* asociados a una subcuenta para generar el correspondiente backUp
	* @author ricruico
	* @param codigoSubCuenta
	* @return List<BackupDetCargosDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean generarInfoBackUpDetCargos(int codigoSubCuenta, Long idLogDistribucion) throws IPSException {
		try{
			List<BackupDetCargosDto> backUpDetCargos= new ArrayList<BackupDetCargosDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoSubCuenta", codigoSubCuenta);
			backUpDetCargos=(List<BackupDetCargosDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpDetCargos", params);
			if(backUpDetCargos != null && !backUpDetCargos.isEmpty()){
				persistenciaSvc= new PersistenciaSvc();
				for(BackupDetCargosDto dto:backUpDetCargos){
					BackupDetCargos entity= new BackupDetCargos();
					LogDistribucionCuenta logDistribucion= new LogDistribucionCuenta();
					logDistribucion.setId(idLogDistribucion);
					entity.setLogDistribucionCuenta(logDistribucion);
					entity.setSubCuenta(dto.getSubCuenta());
					entity.setConvenio(dto.getConvenio());
					entity.setContrato(dto.getContrato());
					entity.setEsquemaTarifario(dto.getEsquemaTarifario());
					entity.setCantidadCargada(dto.getCantidadCargada());
					entity.setValorUnitarioTarifa(dto.getValorUnitarioTarifa());
					entity.setValorUnitarioCargado(dto.getValorUnitarioCargado());
					entity.setValorTotalCargado(dto.getValorTotalCargado());
					entity.setPorcentajeCargado(dto.getPorcentajeCargado());
					entity.setPorcentajeRecargo(dto.getPorcentajeRecargo());
					entity.setValorUnitarioRecargo(dto.getValorUnitarioRecargo());
					entity.setPorcentajeDcto(dto.getPorcentajeDcto());
					entity.setValorUnitarioDcto(dto.getValorUnitarioDcto());
					entity.setValorUnitarioIva(dto.getValorUnitarioIva());
					entity.setRequiereAutorizacion(dto.getRequiereAutorizacion());
					entity.setNroAutorizacion(dto.getNroAutorizacion());
					entity.setEstado(dto.getEstado());
					entity.setCubierto(dto.getCubierto());
					entity.setTipoDistribucion(dto.getTipoDistribucion());
					entity.setSolicitud(dto.getSolicitud());
					entity.setServicio(dto.getServicio());
					entity.setArticulo(dto.getArticulo());
					entity.setServicioCx(dto.getServicioCx());
					entity.setTipoAsocio(dto.getTipoAsocio());
					entity.setFacturado(dto.getFacturado());
					entity.setTipoSolicitud(dto.getTipoSolicitud());
					entity.setPaquetizado(dto.getPaquetizado());
					entity.setCargoPadre(dto.getCargoPadre());
					entity.setUsuarioModifica(dto.getUsuarioModifica());
					entity.setFechaModifica(dto.getFechaModifica());
					entity.setHoraModifica(dto.getHoraModifica());
					entity.setCodSolSubcuenta(dto.getCodSolSubcuenta());
					entity.setObservaciones(dto.getObservaciones());
					entity.setCodigoFactura(dto.getCodigoFactura());
					entity.setEliminado(dto.getEliminado());
					entity.setDetCxHonorarios(dto.getDetCxHonorarios());
					entity.setDetAsocioCxSalasMat(dto.getDetAsocioCxSalasMat());
					entity.setEsPortatil(dto.getEsPortatil());
					entity.setDejarExcento(dto.getDejarExcento());
					entity.setPorcentajeDctoPromServ(dto.getPorcentajeDctoPromServ());
					entity.setValorDescuentoPromServ(dto.getValorDescuentoPromServ());
					entity.setPorcHonorarioPromServ(dto.getPorcHonorarioPromServ());
					entity.setValorHonorarioPromServ(dto.getValorHonorarioPromServ());
					entity.setPrograma(dto.getPrograma());
					entity.setPorcentajeDctoBonoServ(dto.getPorcentajeDctoBonoServ());
					entity.setValorDescuentoBonoServ(dto.getValorDescuentoBonoServ());
					entity.setPorcentajeDctoOdontologico(dto.getPorcentajeDctoOdontologico());
					entity.setValorDescuentoOdontologico(dto.getValorDescuentoOdontologico());
					entity.setDetPaqOdonConvenio(dto.getDetPaqOdonConvenio());
					entity.setIdDetCargo(dto.getIdDetCargo());
					persistenciaSvc.merge(entity);
				}
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	* Método que genera toda la información de los det_cargos_art_consumo 
	* asociados a una subcuenta para generar el correspondiente backUp
	* @author ricruico
	* @param codigoSubCuenta
	* @return List<BackupDetCargosArtConsumoDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean generarInfoBackUpDetCargosArtConsumo(int codigoSubCuenta, Long idLogDistribucion) throws IPSException {
		try{
			List<BackupDetCargosArtConsumoDto> backUpDetCargosArtConsumo= new ArrayList<BackupDetCargosArtConsumoDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoSubCuenta", codigoSubCuenta);
			backUpDetCargosArtConsumo=(List<BackupDetCargosArtConsumoDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpDetCargosArtConsumo", params);
			if(backUpDetCargosArtConsumo != null && !backUpDetCargosArtConsumo.isEmpty()){
				persistenciaSvc= new PersistenciaSvc();
				for(BackupDetCargosArtConsumoDto dto:backUpDetCargosArtConsumo){
					BackupDetCargosArtConsumo entity= new BackupDetCargosArtConsumo();
					LogDistribucionCuenta logDistribucion = new LogDistribucionCuenta();
					logDistribucion.setId(idLogDistribucion);
					entity.setLogDistribucionCuenta(logDistribucion);
					entity.setDetCargo(dto.getDetCargo());
					entity.setArticulo(dto.getArticulo());
					entity.setCantidad(dto.getCantidad());
					entity.setValorUnitario(dto.getValorUnitario());
					entity.setValorTotal(dto.getValorTotal());
					entity.setPorcentaje(dto.getPorcentaje());
					entity.setUsuarioModifica(dto.getUsuarioModifica());
					entity.setFechaModifica(dto.getFechaModifica());
					entity.setHoraModifica(dto.getHoraModifica());
					entity.setIdDetCargosArtConsumo(dto.getCodigo());					
					persistenciaSvc.merge(entity);
				}
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	* Método que genera toda la información de las solicutudes_subcuenta
	* asociados a una subcuenta para generar el correspondiente backUp
	* @author ricruico
	* @param codigoSubCuenta
	* @return List<BackupSolicitudesSubCuentaDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean generarInfoBackUpSolicitudesSubCuenta(int codigoSubCuenta, Long idLogDistribucion) throws IPSException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			List<BackupSolicitudesSubCuentaDto> backUpSolicitudesSubCuenta= new ArrayList<BackupSolicitudesSubCuentaDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoSubCuenta", codigoSubCuenta);
			backUpSolicitudesSubCuenta=(List<BackupSolicitudesSubCuentaDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpSolicitudesSubCuenta", params);
			if(backUpSolicitudesSubCuenta != null && !backUpSolicitudesSubCuenta.isEmpty()){
				persistenciaSvc= new PersistenciaSvc();
				for(BackupSolicitudesSubCuentaDto dto:backUpSolicitudesSubCuenta){
					BackupSolicitudesSubcuenta entity= new BackupSolicitudesSubcuenta();
					LogDistribucionCuenta logDistribucion = new LogDistribucionCuenta();
					logDistribucion.setId(idLogDistribucion);
					entity.setLogDistribucionCuenta(logDistribucion);
					entity.setIdSolicitudesSubcuenta(dto.getIdSolicitudesSubcuenta());
					entity.setSolicitud(dto.getSolicitud());
					entity.setSubCuenta(dto.getSubCuenta());
					entity.setServicio(dto.getServicio());
					entity.setArticulo(dto.getArticulo());
					entity.setPorcentaje(dto.getPorcentaje());
					entity.setCantidad(dto.getCantidad());
					entity.setMonto(dto.getMonto());
					entity.setCubierto(dto.getCubierto());
					entity.setCuenta(dto.getCuenta());
					entity.setTipoSolicitud(dto.getTipoSolicitud());
					entity.setPaquetizada(dto.getPaquetizada());
					entity.setSolSubcuentaPadre(dto.getSolSubcuentaPadre());
					entity.setServicioCx(dto.getServicioCx());
					entity.setTipoAsocio(dto.getTipoAsocio());
					entity.setTipoDistribucion(dto.getTipoDistribucion());
					entity.setFechaModifica(dto.getFechaModifica());
					entity.setHoraModifica(dto.getHoraModifica());
					entity.setUsuarioModifica(dto.getUsuarioModifica());
					entity.setFacturado(dto.getFacturado());
					entity.setEliminado(dto.getEliminado());
					entity.setActualizado(dto.getActualizado());
					entity.setDetCxHonorarios(dto.getDetCxHonorarios());
					entity.setDetAsocioCxSalasMat(dto.getDetAsocioCxSalasMat());					
					persistenciaSvc.merge(entity);
				}
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	* Método que guarda toda la información de las subcuentas
	* a las cuales se les debe realizar el backUp
	* @author ricruico
	* @param codigoSubCuenta
	* @return List<BackupDetCargosArtConsumoDto>
	* @throws IPSException
	*
	*/
	public boolean generarInfoBackUpSubCuenta(BackupSubCuentaDto dtoBackUpSubCuenta, Long idLogDistribucion) throws IPSException {
		try{
			if(dtoBackUpSubCuenta != null && idLogDistribucion != null){
				persistenciaSvc= new PersistenciaSvc();
				BackupSubCuentas entity = new BackupSubCuentas();
				LogDistribucionCuenta logDistribucionCuenta = new LogDistribucionCuenta();
				logDistribucionCuenta.setId(idLogDistribucion);
				entity.setLogDistribucionCuenta(logDistribucionCuenta);
				entity.setIdSubCuenta(dtoBackUpSubCuenta.getIdSubCuenta());
				entity.setConvenio(dtoBackUpSubCuenta.getConvenio());
				entity.setNaturalezaPaciente(dtoBackUpSubCuenta.getNaturalezaPaciente());
				entity.setMontoCobro(dtoBackUpSubCuenta.getMontoCobro());
				entity.setNroCarnet(dtoBackUpSubCuenta.getNroCarnet());
				entity.setNroPoliza(dtoBackUpSubCuenta.getNroPoliza());
				entity.setFechaModifica(dtoBackUpSubCuenta.getFechaModifica());
				entity.setUsuarioModifica(dtoBackUpSubCuenta.getUsuarioModifica());
				entity.setContrato(dtoBackUpSubCuenta.getContrato());
				entity.setIngreso(dtoBackUpSubCuenta.getIngreso());
				entity.setTipoAfiliado(dtoBackUpSubCuenta.getTipoAfiliado());
				entity.setClasificacionSocioeconomica(dtoBackUpSubCuenta.getClasificacionSocioeconomica());
				entity.setNroAutorizacion(dtoBackUpSubCuenta.getNroAutorizacion());
				entity.setFechaAfiliacion(dtoBackUpSubCuenta.getFechaAfiliacion());
				entity.setSemanasCotizacion(dtoBackUpSubCuenta.getSemanasCotizacion());
				entity.setCodigoPaciente(dtoBackUpSubCuenta.getCodigoPaciente());
				entity.setValorUtilizadoSoat(dtoBackUpSubCuenta.getValorUtilizadoSoat());
				entity.setNroPrioridad(dtoBackUpSubCuenta.getNroPrioridad());
				entity.setPorcentajeAutorizado(dtoBackUpSubCuenta.getPorcentajeAutorizado());
				entity.setMontoAutorizado(dtoBackUpSubCuenta.getMontoAutorizado());
				entity.setObsParametrosDistribucion(dtoBackUpSubCuenta.getObsParametrosDistribucion());
				entity.setFacturado(dtoBackUpSubCuenta.getFacturado());
				entity.setHoraModifica(dtoBackUpSubCuenta.getHoraModifica());
				entity.setEmpresasInstitucion(dtoBackUpSubCuenta.getEmpresasInstitucion());
				entity.setNumeroSolicitudVolante(dtoBackUpSubCuenta.getNumeroSolicitudVolante());
				entity.setMesesCotizacion(dtoBackUpSubCuenta.getMesesCotizacion());
				entity.setTipoCobertura(dtoBackUpSubCuenta.getTipoCobertura());
				entity.setValorAutorizacion(dtoBackUpSubCuenta.getValorAutorizacion());
				entity.setMedioAutorizacion(dtoBackUpSubCuenta.getMedioAutorizacion());
				entity.setBono(dtoBackUpSubCuenta.getBono());
				entity.setTipoCobroPaciente(dtoBackUpSubCuenta.getTipoCobroPaciente());
				entity.setTipoMontoCobro(dtoBackUpSubCuenta.getTipoMontoCobro());
				entity.setPorcentajeMontoCobro(dtoBackUpSubCuenta.getPorcentajeMontoCobro());
				entity.setMigrado(dtoBackUpSubCuenta.getMigrado());				
				persistenciaSvc.merge(entity);
			}
			else{
				return false;
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	* Método que genera toda la información de los det_cargos 
	* asociados a una o mas solicitudes para generar el correspondiente backUp
	* @author ricruico
	* @param List<Long>
	* @return List<BackupDetCargosDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean generarInfoBackUpDetCargosPorSolicitudes(List<Integer> numeroSolicitudes, Long idLogDistribucion) throws IPSException {
		try{
			List<BackupDetCargosDto> backUpDetCargos= new ArrayList<BackupDetCargosDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("numeroSolicitudes", numeroSolicitudes);
			backUpDetCargos=(List<BackupDetCargosDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpDetCargosPorSolicitudes", params);
			if(backUpDetCargos != null && !backUpDetCargos.isEmpty()){
				persistenciaSvc= new PersistenciaSvc();
				for(BackupDetCargosDto dto:backUpDetCargos){
					BackupDetCargos entity= new BackupDetCargos();
					LogDistribucionCuenta logDistribucion= new LogDistribucionCuenta();
					logDistribucion.setId(idLogDistribucion);
					entity.setLogDistribucionCuenta(logDistribucion);
					entity.setSubCuenta(dto.getSubCuenta());
					entity.setConvenio(dto.getConvenio());
					entity.setContrato(dto.getContrato());
					entity.setEsquemaTarifario(dto.getEsquemaTarifario());
					entity.setCantidadCargada(dto.getCantidadCargada());
					entity.setValorUnitarioTarifa(dto.getValorUnitarioTarifa());
					entity.setValorUnitarioCargado(dto.getValorUnitarioCargado());
					entity.setValorTotalCargado(dto.getValorTotalCargado());
					entity.setPorcentajeCargado(dto.getPorcentajeCargado());
					entity.setPorcentajeRecargo(dto.getPorcentajeRecargo());
					entity.setValorUnitarioRecargo(dto.getValorUnitarioRecargo());
					entity.setPorcentajeDcto(dto.getPorcentajeDcto());
					entity.setValorUnitarioDcto(dto.getValorUnitarioDcto());
					entity.setValorUnitarioIva(dto.getValorUnitarioIva());
					entity.setRequiereAutorizacion(dto.getRequiereAutorizacion());
					entity.setNroAutorizacion(dto.getNroAutorizacion());
					entity.setEstado(dto.getEstado());
					entity.setCubierto(dto.getCubierto());
					entity.setTipoDistribucion(dto.getTipoDistribucion());
					entity.setSolicitud(dto.getSolicitud());
					entity.setServicio(dto.getServicio());
					entity.setArticulo(dto.getArticulo());
					entity.setServicioCx(dto.getServicioCx());
					entity.setTipoAsocio(dto.getTipoAsocio());
					entity.setFacturado(dto.getFacturado());
					entity.setTipoSolicitud(dto.getTipoSolicitud());
					entity.setPaquetizado(dto.getPaquetizado());
					entity.setCargoPadre(dto.getCargoPadre());
					entity.setUsuarioModifica(dto.getUsuarioModifica());
					entity.setFechaModifica(dto.getFechaModifica());
					entity.setHoraModifica(dto.getHoraModifica());
					entity.setCodSolSubcuenta(dto.getCodSolSubcuenta());
					entity.setObservaciones(dto.getObservaciones());
					entity.setCodigoFactura(dto.getCodigoFactura());
					entity.setEliminado(dto.getEliminado());
					entity.setDetCxHonorarios(dto.getDetCxHonorarios());
					entity.setDetAsocioCxSalasMat(dto.getDetAsocioCxSalasMat());
					entity.setEsPortatil(dto.getEsPortatil());
					entity.setDejarExcento(dto.getDejarExcento());
					entity.setPorcentajeDctoPromServ(dto.getPorcentajeDctoPromServ());
					entity.setValorDescuentoPromServ(dto.getValorDescuentoPromServ());
					entity.setPorcHonorarioPromServ(dto.getPorcHonorarioPromServ());
					entity.setValorHonorarioPromServ(dto.getValorHonorarioPromServ());
					entity.setPrograma(dto.getPrograma());
					entity.setPorcentajeDctoBonoServ(dto.getPorcentajeDctoBonoServ());
					entity.setValorDescuentoBonoServ(dto.getValorDescuentoBonoServ());
					entity.setPorcentajeDctoOdontologico(dto.getPorcentajeDctoOdontologico());
					entity.setValorDescuentoOdontologico(dto.getValorDescuentoOdontologico());
					entity.setDetPaqOdonConvenio(dto.getDetPaqOdonConvenio());
					entity.setIdDetCargo(dto.getIdDetCargo());
					persistenciaSvc.merge(entity);
				}
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	/**
	* Método que genera toda la información de los det_cargos_art_consumo 
	* asociados a una o más solicitudes para generar el correspondiente backUp
	* @author ricruico
	* @param List<Long>
	* @return List<BackupDetCargosArtConsumoDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean generarInfoBackUpDetCargosArtConsumoPorSolicitudes(List<Integer> numeroSolicitudes, Long idLogDistribucion) throws IPSException {
		try{
			List<BackupDetCargosArtConsumoDto> backUpDetCargosArtConsumo= new ArrayList<BackupDetCargosArtConsumoDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("numeroSolicitudes", numeroSolicitudes);
			backUpDetCargosArtConsumo=(List<BackupDetCargosArtConsumoDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpDetCargosArtConsumoPorSolicitudes", params);
			if(backUpDetCargosArtConsumo != null && !backUpDetCargosArtConsumo.isEmpty()){
				persistenciaSvc= new PersistenciaSvc();
				for(BackupDetCargosArtConsumoDto dto:backUpDetCargosArtConsumo){
					BackupDetCargosArtConsumo entity= new BackupDetCargosArtConsumo();
					LogDistribucionCuenta logDistribucion = new LogDistribucionCuenta();
					logDistribucion.setId(idLogDistribucion);
					entity.setLogDistribucionCuenta(logDistribucion);
					entity.setDetCargo(dto.getDetCargo());
					entity.setArticulo(dto.getArticulo());
					entity.setCantidad(dto.getCantidad());
					entity.setValorUnitario(dto.getValorUnitario());
					entity.setValorTotal(dto.getValorTotal());
					entity.setPorcentaje(dto.getPorcentaje());
					entity.setUsuarioModifica(dto.getUsuarioModifica());
					entity.setFechaModifica(dto.getFechaModifica());
					entity.setHoraModifica(dto.getHoraModifica());
					entity.setIdDetCargosArtConsumo(dto.getCodigo());					
					persistenciaSvc.merge(entity);
				}
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
	
	
	/**
	* Método que genera toda la información de las solicitudes_subcuenta
	* asociados a una o más solicitudes para generar el correspondiente backUp
	* @author ricruico
	* @param List<Long>
	* @return List<BackupSolicitudesSubCuentaDto>
	* @throws IPSException
	*
	*/
	@SuppressWarnings("unchecked")
	public boolean generarInfoBackUpSolicitudesSubCuentaPorSolicitudes(List<Integer> numeroSolicitudes, Long idLogDistribucion) throws IPSException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			List<BackupSolicitudesSubCuentaDto> backUpSolicitudesSubCuenta= new ArrayList<BackupSolicitudesSubCuentaDto>();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("numeroSolicitudes", numeroSolicitudes);
			backUpSolicitudesSubCuenta=(List<BackupSolicitudesSubCuentaDto>)persistenciaSvc.createNamedQuery("distribucionCuenta.obtenerInfoBackUpSolicitudesSubCuentaPorSolicitudes", params);
			if(backUpSolicitudesSubCuenta != null && !backUpSolicitudesSubCuenta.isEmpty()){
				persistenciaSvc= new PersistenciaSvc();
				for(BackupSolicitudesSubCuentaDto dto:backUpSolicitudesSubCuenta){
					BackupSolicitudesSubcuenta entity= new BackupSolicitudesSubcuenta();
					LogDistribucionCuenta logDistribucion = new LogDistribucionCuenta();
					logDistribucion.setId(idLogDistribucion);
					entity.setLogDistribucionCuenta(logDistribucion);
					entity.setIdSolicitudesSubcuenta(dto.getIdSolicitudesSubcuenta());
					entity.setSolicitud(dto.getSolicitud());
					entity.setSubCuenta(dto.getSubCuenta());
					entity.setServicio(dto.getServicio());
					entity.setArticulo(dto.getArticulo());
					entity.setPorcentaje(dto.getPorcentaje());
					entity.setCantidad(dto.getCantidad());
					entity.setMonto(dto.getMonto());
					entity.setCubierto(dto.getCubierto());
					entity.setCuenta(dto.getCuenta());
					entity.setTipoSolicitud(dto.getTipoSolicitud());
					entity.setPaquetizada(dto.getPaquetizada());
					entity.setSolSubcuentaPadre(dto.getSolSubcuentaPadre());
					entity.setServicioCx(dto.getServicioCx());
					entity.setTipoAsocio(dto.getTipoAsocio());
					entity.setTipoDistribucion(dto.getTipoDistribucion());
					entity.setFechaModifica(dto.getFechaModifica());
					entity.setHoraModifica(dto.getHoraModifica());
					entity.setUsuarioModifica(dto.getUsuarioModifica());
					entity.setFacturado(dto.getFacturado());
					entity.setEliminado(dto.getEliminado());
					entity.setActualizado(dto.getActualizado());
					entity.setDetCxHonorarios(dto.getDetCxHonorarios());
					entity.setDetAsocioCxSalasMat(dto.getDetAsocioCxSalasMat());					
					persistenciaSvc.merge(entity);
				}
			}
			return true;
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
	}
}
