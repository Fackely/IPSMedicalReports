package com.servinte.axioma.bl.manejoPaciente.interfaz;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public interface IIngresoMundo {

	/**
	 * M�todo encargado de obtener la informaci�n del centro de atenci�n
	 * asigando al paciente
	 * 
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 */
	CentroAtencionDto obtenerCentroAtencionAsignadoPaciente(int codigoPaciente) throws IPSException;
	
	
	/**
	 * Servicio encargado de obtener la informaci�n mas reciente de la subcuenta de acuerdo a
	 * el c�digo de ingreso
	 * 
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 * @throws IPSException
	 */
	InfoSubCuentaDto consultarInfoSubCuentaPorIngresoPorConvenio(int codigoIngreso, int codigoConvenio, boolean requiereTransaccion) throws IPSException;
	
	/**
	 * Servicio encargado de obtener la informaci�n mas reciente de la cuenta de acuerdo a
	 * el c�digo de ingreso
	 * 
	 * @param codigoIngreso
	 * @return
	 * @throws IPSException
	 */
	DtoCheckBox consultarInfoCuentaPorIngreso(int codigoIngreso) throws IPSException;
	

	/**
	 * Obtiene informacion relevante de un ingreso referente a su id 
	 * @param idIngreso
	 * @return
	 * @throws IPSException
	 * @author javrammo
	 */
	InfoIngresoDto obtenerInfoIngreso(int idIngreso) throws IPSException;
}