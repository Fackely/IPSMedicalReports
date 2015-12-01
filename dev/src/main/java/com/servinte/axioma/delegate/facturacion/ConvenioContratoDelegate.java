package com.servinte.axioma.delegate.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;

/**
 * @author ricruico
 * @version 1.0
 * @created 03-jul-2012 03:34:35 p.m.
 */
public class ConvenioContratoDelegate {

	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	
	public ConvenioContratoDelegate(){

	}

	/**
	 * Servicio que obtiene la lista de contratos parametrizados
	 * en el sistema que estan asociados al convenio pasado por parámetro
	 * 
	 * @param codigoConvenio
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoDto> consultarContratosVigentesPorConvenio(int codigoConvenio) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoConvenio", codigoConvenio);
			return (List<ContratoDto>)persistenciaSvc.createNamedQuery("convenioContrato.consultarContratosVigentesPorConvenio", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

	/**
	 * Servicio que obtiene la lista de convenios parametrizados
	 * en el sistema que cumplan con los parámetros de búsqueda
	 * 
	 * @param codigoInstitucion
	 * @param tipoContrato
	 * @param manejaCapitacionSub
	 * @param isActivo
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<ConvenioDto> consultarConveniosPorInstitucion(int codigoInstitucion, Integer tipoContrato, 
								Character manejaCapitacionSub, boolean isActivo) throws BDException{
		List<ConvenioDto> listaConvenios = new ArrayList<ConvenioDto>(0);
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoInstitucion", codigoInstitucion);
			if(tipoContrato != null){
				parameters.put("tipoContrato", tipoContrato);
			}
			if(manejaCapitacionSub != null){
				parameters.put("manejaCapitacionSub", manejaCapitacionSub);
			}
			parameters.put("isActivo", isActivo);
			if(tipoContrato != null && manejaCapitacionSub != null){
				listaConvenios=(List<ConvenioDto>)persistenciaSvc.createNamedQuery("convenioContrato.consultarConveniosPorInstitucionPorTipoContrato", parameters);
			}
			else{
				listaConvenios=(List<ConvenioDto>)persistenciaSvc.createNamedQuery("convenioContrato.consultarConveniosPorInstitucion", parameters);
			}
			return listaConvenios;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

	/**
	 * Servicio que obtiene la lista de todos convenios parametrizados
	 * en el sistema que cumplan con los parámetros de búsqueda
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 14/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<ConvenioDto> consultarTodosConveniosPorInstitucion(int codigoInstitucion) throws BDException{
		List<ConvenioDto> listaConvenios = new ArrayList<ConvenioDto>(0);
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoInstitucion", codigoInstitucion);
			listaConvenios=(List<ConvenioDto>)persistenciaSvc.createNamedQuery("convenioContrato.consultarTodosConveniosPorInstitucion", parameters);
			return listaConvenios;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

}