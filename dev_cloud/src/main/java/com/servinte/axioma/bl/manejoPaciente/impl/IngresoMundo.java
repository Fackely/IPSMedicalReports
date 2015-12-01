package com.servinte.axioma.bl.manejoPaciente.impl;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.comun.DtoCheckBox;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IIngresoMundo;
import com.servinte.axioma.delegate.manejoPaciente.IngresoDelegate;
import com.servinte.axioma.dto.administracion.CentroAtencionDto;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada a los 
 * Ingresos del Paciente
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class IngresoMundo implements IIngresoMundo{

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IIngresoMundo#obtenerCentroAtencionAsignadoPaciente(int)
	 * @author ricruico
	 */
	@Override
	public CentroAtencionDto obtenerCentroAtencionAsignadoPaciente(
			int codigoPaciente)  throws IPSException{
		CentroAtencionDto centroAtencion=null;
		try{
			HibernateUtil.beginTransaction();
			IngresoDelegate ingresoDelegate = new IngresoDelegate();
			centroAtencion=ingresoDelegate.obtenerCentroAtencionAsignadoPaciente(codigoPaciente);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return centroAtencion;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IIngresoMundo#consultarInfoSubCuentaPorIngresoPorContrato(int)
	 * @author ricruico
	 */
	@Override
	public InfoSubCuentaDto consultarInfoSubCuentaPorIngresoPorConvenio(
							int codigoIngreso, int codigoConvenio, boolean requiereTransaccion) throws IPSException {
		InfoSubCuentaDto infoSubCuenta=null;
		try{
			
			if(requiereTransaccion){
			HibernateUtil.beginTransaction();
			}
			
			IngresoDelegate ingresoDelegate = new IngresoDelegate();
			infoSubCuenta=ingresoDelegate.consultarInfoSubCuentaPorIngresoPorConvenio(codigoIngreso, codigoConvenio);
			
			if(requiereTransaccion){
			HibernateUtil.endTransaction();
		}
			
		}
		catch (IPSException ipsme) {
			if(requiereTransaccion){
			HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if(requiereTransaccion){
			HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return infoSubCuenta;
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.IIngresoMundo#consultarInfoCuentaPorIngreso(int)
	 * @author ricruico
	 */
	@Override
	public DtoCheckBox consultarInfoCuentaPorIngreso(int codigoIngreso) throws IPSException {
		DtoCheckBox infoCuenta=null;
		try{
			HibernateUtil.beginTransaction();
			IngresoDelegate ingresoDelegate = new IngresoDelegate();
			infoCuenta=ingresoDelegate.consultarInfoCuentaPorIngreso(codigoIngreso);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return infoCuenta;
	}

	/**
	 * Obtiene informacion relevante de un ingreso referente a su id 
	 * @param idIngreso
	 * @return
	 * @throws IPSException
	 * @author javrammo
	 */
	@Override
	public InfoIngresoDto obtenerInfoIngreso(int idIngreso) throws IPSException {
		InfoIngresoDto infoIngreso=null;
		try{
			HibernateUtil.beginTransaction();
			IngresoDelegate ingresoDelegate = new IngresoDelegate();
			infoIngreso=ingresoDelegate.obtenerInfoIngreso(idIngreso);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return infoIngreso;
	}
	

}