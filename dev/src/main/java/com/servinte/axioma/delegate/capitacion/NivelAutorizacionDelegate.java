package com.servinte.axioma.delegate.capitacion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;

/**
 * Clase de implementa los metodos de integración con la base de datos
 * asociados a Niveles de Autorización
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class NivelAutorizacionDelegate {

	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	/**
	 * Método encargado de consultar los niveles de autorización de usuario
	 * de acuerdo al login de usuario, tipo de autorización y si estan activos
	 * 
	 * @param loginUsuario
	 * @param tipoAutorizacion
	 * @param isActivo
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<NivelAutorizacionDto> consultarNivelesAutorizacionUsuario(String loginUsuario, String tipoAutorizacion, boolean isActivo) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("loginUsuario", loginUsuario);
			parameters.put("tipoAutorizacion", tipoAutorizacion);
			parameters.put("isActivo", isActivo);
			return (List<NivelAutorizacionDto>)persistenciaSvc
									.createNamedQuery("nivelAutorizacion.consultarNivelesAutorizacionUsuario", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar los niveles de autorización de usuario
	 * de acuerdo a la ocupación del usuario, tipo de autorización y si estan activos
	 * 
	 * @param codigoPersonaUsuario
	 * @param tipoAutorizacion
	 * @param isActivo
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<NivelAutorizacionDto> consultarNivelesAutorizacionOcupacionUsuario(int codigoPersonaUsuario, String tipoAutorizacion, boolean isActivo) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoPersonaUsuario", codigoPersonaUsuario);
			parameters.put("tipoAutorizacion", tipoAutorizacion);
			parameters.put("isActivo", isActivo);
			return (List<NivelAutorizacionDto>)persistenciaSvc
									.createNamedQuery("nivelAutorizacion.consultarNivelesAutorizacionOcupacionUsuario", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar las prioridades parametrizadas para cada nivel
	 * de autorizacion de acuerdo a la parametrización de usuario especifico
	 * 
	 * @param loginUsuario
	 * @param codigoNivelAutorizacion
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerPrioridadesNivelAutorizacionUsuario(String loginUsuario, int codigoNivelAutorizacion) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("loginUsuario", loginUsuario);
			parameters.put("codigoNivelAutorizacion", codigoNivelAutorizacion);
			return (List<Integer>)persistenciaSvc
									.createNamedQuery("nivelAutorizacion.obtenerPrioridadesNivelAutorizacionUsuario", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar las prioridades parametrizadas para cada nivel
	 * de autorizacion de acuerdo a la parametrización de ocupaciones médicas
	 * 
	 * @param codigoPersonaUsuario
	 * @param codigoNivelAutorizacion
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerPrioridadesNivelAutorizacionOcupacionUsuario(int codigoPersonaUsuario, int codigoNivelAutorizacion) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoPersonaUsuario", codigoPersonaUsuario);
			parameters.put("codigoNivelAutorizacion", codigoNivelAutorizacion);
			return (List<Integer>)persistenciaSvc
									.createNamedQuery("nivelAutorizacion.obtenerPrioridadesNivelAutorizacionOcupacionUsuario", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar la parametrización de niveles de autorización por
	 * Medicamento/Insumo específico
	 * 
	 * @param codigosMedicamentosInsumos
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> existeNivelAutorizacionMedicamentosInsumos(List<Integer> codigosMedicamentosInsumos) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigosMedicamentos", codigosMedicamentosInsumos);
			List<Integer> niveles=(List<Integer>)persistenciaSvc
													.createNamedQuery("nivelAutorizacion.existeNivelAutorizacionMedicamentosInsumos", parameters);
			return niveles;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Método encargado de consultar la parametrización de niveles de autorización por
	 * Servicio específico
	 * 
	 * @param codigosServicios
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> existeNivelAutorizacionServicios(List<Integer> codigosServicios) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigosServicios", codigosServicios);
			List<Integer> niveles=(List<Integer>)persistenciaSvc
													.createNamedQuery("nivelAutorizacion.existeNivelAutorizacionServicios", parameters);
			return niveles;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Método encargado de consultar la parametrización de niveles de autorización por
	 * Medicamento/Insumo específico
	 * 
	 * @param codigosMedicamentosInsumos
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerNivelAutorizacionMedicamentoInsumo(
			Integer codigoMedicamentoInsumo) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoMedicamento", codigoMedicamentoInsumo);
			return (List<Integer>)persistenciaSvc.
					createNamedQuery("nivelAutorizacion.obtenerNivelAutorizacionMedicamentoInsumo", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar la parametrización de niveles de autorización por
	 * Medicamento/Insumo Agrupado
	 * 
	 * @param codigoSubGrupoInventario
	 * @param acronimoNaturaleza
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerNivelAutorizacionMedicamentoInsumoAgrupado(
			int codigoSubGrupo, String acronimoNaturaleza) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("codigoSubGrupo", codigoSubGrupo);
			Object[] infoSubGrupo=(Object[])persistenciaSvc.createNamedQueryUniqueResult("catalogoInventario.obtenerClaseInventarioPorSubGrupo", params);
			int codigoClaseInv=(Integer)infoSubGrupo[0];
			int codigoGrupoInv=(Integer)infoSubGrupo[2];
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoSubGrupo", codigoSubGrupo);
			parameters.put("codigoClaseInv", codigoClaseInv);
			parameters.put("codigoGrupoInv", codigoGrupoInv);
			parameters.put("acronimoNaturaleza", acronimoNaturaleza);
			return (List<Integer>)persistenciaSvc.
					createNamedQuery("nivelAutorizacion.obtenerNivelAutorizacionMedicamentoInsumoAgrupado", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar la parametrización de niveles de autorización por
	 * Servicio específico
	 * 
	 * @param codigosServicios
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerNivelAutorizacionServicio(
			Integer codigoServicio) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoServicio", codigoServicio);
			return (List<Integer>)persistenciaSvc.
					createNamedQuery("nivelAutorizacion.obtenerNivelAutorizacionServicio", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de consultar la parametrización de niveles de autorización por
	 * Servicio Agrupado
	 * 
	 * @param codigosMedicamentosInsumos
	 * @param nivelesAutorizacionUsuario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerNivelAutorizacionServicioAgrupado(
			int codigoEspecialidad, String acronimoTipoServicio,
			int codigoGrupoServico) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoEspecialidad", codigoEspecialidad);
			parameters.put("acronimoTipoServicio", acronimoTipoServicio);
			parameters.put("codigoGrupoServico", codigoGrupoServico);
			return (List<Integer>)persistenciaSvc.
					createNamedQuery("nivelAutorizacion.obtenerNivelAutorizacionServicioAgrupado", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
}
