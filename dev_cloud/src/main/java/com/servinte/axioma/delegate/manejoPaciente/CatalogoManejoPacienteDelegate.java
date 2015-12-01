package com.servinte.axioma.delegate.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Clase que permite el acceso a datos para las parametricas y catalogos
 * del módulo de Manejo Paciente
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class CatalogoManejoPacienteDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	/**
	 * Servicio que consulta la vias de ingreso parametrizadas en el sistema
	 * 
	 * 
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<ViaIngresoDto> consultarViasIngreso() throws BDException{
		try{
			List<ViaIngresoDto> vias= null;
			persistenciaSvc= new PersistenciaSvc();
			vias=(List<ViaIngresoDto>)persistenciaSvc.createNamedQuery("catalogoManejoPaciente.consultarViasIngreso");
			return vias;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

	
	/**
	 * Servicio encargado de obtener la información de la via de ingreso
	 * @param codigo
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public ViaIngresoDto consultarViaIngresoPorId(int codigo) throws BDException{
		try{
			ViaIngresoDto viaIngresoDto= new ViaIngresoDto();
			persistenciaSvc= new PersistenciaSvc();
			ViasIngreso via=(ViasIngreso)persistenciaSvc.find(ViasIngreso.class, codigo);
			viaIngresoDto.setCodigo(via.getCodigo());
			viaIngresoDto.setNombre(via.getNombre());
			return viaIngresoDto;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de verificar si para un servicio existe parámetrización de 
	 * unidad de consulta por centro de costo
	 * @param codigoServicio
	 * @param codigoCentroCosto
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public boolean existeCentroCostoParametrizadoPorUnidadConsulta(int codigoServicio, int codigoCentroCosto) throws IPSException{
		try{
			boolean existe= false;
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("codigoServicio", codigoServicio);
			params.put("codigoCentroCosto", codigoCentroCosto);
			List<Integer> parametricas=(List<Integer>)persistenciaSvc.createNamedQuery("catalogoManejoPaciente.existeCentroCostoParametrizadoPorUnidadConsulta", params);
			if(parametricas != null && !parametricas.isEmpty()){
				existe=true;
			}
			return existe;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de verificar si para un grupo de servicio existe parámetrización de 
	 * grupo servicios por centro de costo
	 * @param codigoGrupoServicio
	 * @param codigoCentroCosto
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public boolean existeCentroCostoParametrizadoPorGrupoServicio(Integer codigoGrupoServicio, int codigoCentroCosto) throws IPSException{
		try{
			boolean existe= false;
			if(codigoGrupoServicio != null){
				persistenciaSvc= new PersistenciaSvc();
				Map<String,Object> params = new HashMap<String, Object>();
				params.put("codigoGrupoServicio", codigoGrupoServicio);
				params.put("codigoCentroCosto", codigoCentroCosto);
				List<Integer> parametricas=(List<Integer>)persistenciaSvc.createNamedQuery("catalogoManejoPaciente.existeCentroCostoParametrizadoPorGrupoServicio", params);
				if(parametricas != null && !parametricas.isEmpty()){
					existe=true;
				}
			}
			return existe;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de retornas los centros de atencion segun se estado
	 * @param estado
	 * @return
	 * @throws IPSException
	 * @author javrammo
	 */
	public List<DtoCentrosAtencion> listarTodosCentrosAtencion(Boolean estado, int codigoInstitucion)  throws IPSException{		
		try{			
			persistenciaSvc= new PersistenciaSvc();
			List<DtoCentrosAtencion> centrosAtencion = new ArrayList<DtoCentrosAtencion>();
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("codigoInstitucion", codigoInstitucion);
			
			if(estado == null){				
				centrosAtencion = (List<DtoCentrosAtencion>) persistenciaSvc.createNamedQuery("catalogoManejoPaciente.listaTodosLosCentrosAtencion", params);
			}else{				
				params.put("activo", estado);
				centrosAtencion = (List<DtoCentrosAtencion>) persistenciaSvc.createNamedQuery("catalogoManejoPaciente.listaCentrosAtencionPorEstado", params);
			}			
			return centrosAtencion;					
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
	}
	

}