/**
 * 
 */
package com.servinte.axioma.delegate.administracion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.servinte.axioma.dto.administracion.DtoSexo;
import com.servinte.axioma.dto.administracion.EspecialidadDto;
import com.servinte.axioma.dto.administracion.FuncionalidadDto;
import com.servinte.axioma.dto.administracion.TipoAfiliadoDto;
import com.servinte.axioma.dto.administracion.TipoIdentificacionDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.CentrosCosto;

/**
 * Clase que permite el acceso a datos para las parametricas y catalogos
 * del módulo de Administración
 * 
 * @author diego
 *
 */
public class CatalogoAdministracionDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Método encargado de buscar un centro de costo por el cÃ³digo del mismo
	 * @param codigoCentroCosto
	 * @return CentrosCosto
	 * @throws BDException
	 */
	public CentrosCosto buscarCentroCostoXId(int codigoCentroCosto) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			return (CentrosCosto)persistenciaSvc.find(CentrosCosto.class, codigoCentroCosto);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de obtener el catalogo de Especialidades Válidas
	 * tomando como válido que el códigoPk de la especialidad es mayor a cero
	 * 
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<EspecialidadDto> consultarEspecialidadesValidas() throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("codigoTodas", ConstantesBD.codigoEspecialidadMedicaTodas);
			return (List<EspecialidadDto>)persistenciaSvc.createNamedQuery("catalogoAdministracion.consultarEspecialidadesValidas", parametros);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Consulta todos los tipos de identificacion para usuarios
	 * 
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 8/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<TipoIdentificacionDto> consultarTiposIdentificacion() throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("tipo1", ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionAmbos);
			parametros.put("tipo2", ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionPersona);
			
			return (List<TipoIdentificacionDto>)persistenciaSvc.createNamedQuery("catalogoAdministracion.consultaTiposIdentificacion",parametros);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Consulta todos los tipos de afiliado para usuarios
	 * 
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 8/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<TipoAfiliadoDto> consultarTiposAfiliado() throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			return (List<TipoAfiliadoDto>)persistenciaSvc.createNamedQuery("catalogoAdministracion.consultaTiposAfiliado");
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

	/**
	 * Servicio encargado de verificar si una entidad subcontratada ya tiene parametrización de entidades
	 * subcontratadas por centros de costo
	 * @param codigoEntidad
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public boolean existeParametrizacionEntidadSubcontratadaPorCentroCosto(int codigoEntidad) throws BDException{
		boolean existeParametrizacion=false;
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> params= new HashMap<String, Object>();
			params.put("codigoEntidad", codigoEntidad);
			List<Long> parametrizaciones = (List<Long>)persistenciaSvc.createNamedQuery("catalogoAdministracion.existeParametrizacionEntidadSubcontratadaPorCentroCosto", params);
			if(parametrizaciones != null && !parametrizaciones.isEmpty()){
				existeParametrizacion=true;
			}
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return existeParametrizacion;
	}
	
	/**
	 * Consulta todos los sexos
	 * @return
	 * @throws BDException
	 */
	public List<DtoSexo> consultarSexos() throws BDException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			return (List<DtoSexo>)persistenciaSvc.createNamedQuery("catalogoAdministracion.consultarSexos");
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

	/**
	 * Metodo que retorna el codigo de los profesionales de la institucion
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 * @created 21/12/2012
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> consultarDatosProfesionales(int codigoInstitucion) throws BDException {
		try{
			List<Object[]> profesionales = null;
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("institucion", codigoInstitucion);
			profesionales = (List<Object[]>) persistenciaSvc.createNamedQuery("catalogoAdministracion.consultarDatosProfesionales",parametros);
			
			return profesionales;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo que retorna las especialidades por cada profesional
	 * @param codigoProfesional
	 * @return
	 * @throws BDException
	 * @author hermorhu
	 * @created 21/12/2012
	 */
	@SuppressWarnings("unchecked")
	public List<EspecialidadDto> consultarEspecialidadesXProfesional (int codigoProfesional) throws BDException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object>parametros=new HashMap<String, Object>(0);
			parametros.put("codigoProfesional", codigoProfesional);
			return (List<EspecialidadDto>)persistenciaSvc.createNamedQuery("catalogoAdministracion.consultarEspecialidadesXProfesional", parametros);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
	}
	
	/**
	 * Consulta todas las funcionalidades asociadas a una funcionalidad padre
	 * 
	 * @param codigoFuncionalidadPadre
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @throws BDException 
	 * @created 30/10/2012
	 */
	@SuppressWarnings("unchecked")
	public List<FuncionalidadDto> consultarFuncionalidades(
			int codigoFuncionalidadPadre) throws BDException {
		try{
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object> parametros=new HashMap<String, Object>(0);
			parametros.put("codigoFunPadre", codigoFuncionalidadPadre);
			return (List<FuncionalidadDto>)persistenciaSvc.createNamedQuery("catalogoAdministracion.consultaFuncionalidades",parametros);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

}
