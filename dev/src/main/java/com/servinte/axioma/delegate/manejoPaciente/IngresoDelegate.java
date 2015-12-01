package com.servinte.axioma.delegate.manejoPaciente;
import java.util.HashMap;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;

/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class IngresoDelegate {

	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Método encargado de obtener la información del centro de atención
	 * asigando al paciente
	 * 
	 * @param codigoPaciente
	 * @author ricruico
	 */
	public CentroAtencionDto obtenerCentroAtencionAsignadoPaciente(int codigoPaciente) throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoPaciente", codigoPaciente);
			return (CentroAtencionDto)persistenciaSvc
								.createNamedQueryUniqueResult("ingreso.obtenerCentroAtencionAsignadoPaciente", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de obtener la información mas reciente de la subcuenta de acuerdo a
	 * el código de ingreso por convenio
	 * 
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public InfoSubCuentaDto consultarInfoSubCuentaPorIngresoPorConvenio(int codigoIngreso, int codigoConvenio) throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoIngreso", codigoIngreso);
			parameters.put("codigoConvenio", codigoConvenio);
			return (InfoSubCuentaDto)persistenciaSvc
								.createNamedQueryFirstResult("ingreso.consultarInfoSubCuentaPorIngresoPorConvenio", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de obtener la información mas reciente de la cuenta de acuerdo a
	 * el código de ingreso
	 * 
	 * @param codigoContrato
	 * @return
	 * @throws IPSException
	 * @author ricruico
	 */
	public DtoCheckBox consultarInfoCuentaPorIngreso(int codigoIngreso) throws BDException{
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("codigoIngreso", codigoIngreso);
			return (DtoCheckBox)persistenciaSvc
								.createNamedQueryFirstResult("ingreso.consultarInfoCuentaPorIngreso", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Obtiene informacion relevante de un ingreso referente a su id 
	 * @param idIngreso
	 * @return
	 * @throws IPSException
	 * @author javrammo
	 */
	public InfoIngresoDto obtenerInfoIngreso(int idIngreso) throws BDException{
		
		try{
			Map<String,Object> parameters = new HashMap<String, Object>();
			persistenciaSvc = new PersistenciaSvc();
			parameters.put("idIngreso", idIngreso);
			return (InfoIngresoDto)persistenciaSvc.createNamedQueryFirstResult("ingreso.consultaInformacionIngresoPorId",	parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}		
	}

}