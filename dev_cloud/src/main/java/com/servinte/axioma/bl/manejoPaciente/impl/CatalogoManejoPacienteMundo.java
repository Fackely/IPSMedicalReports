package com.servinte.axioma.bl.manejoPaciente.impl;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.bl.manejoPaciente.interfaz.ICatalogoManejoPacienteMundo;
import com.servinte.axioma.delegate.manejoPaciente.CatalogoManejoPacienteDelegate;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada a los
 * catalogos o paramétricas del módulo de Manejo del Paciente
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class CatalogoManejoPacienteMundo implements ICatalogoManejoPacienteMundo{

		
		
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.ICatalogoManejoPacienteMundo#consultarViasIngreso()
	 * @author ricruico
	 */
	@Override
	public List<ViaIngresoDto> consultarViasIngreso() throws IPSException {
		List<ViaIngresoDto> vias=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoManejoPacienteDelegate delegate = new CatalogoManejoPacienteDelegate();
			vias=delegate.consultarViasIngreso();
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
		return vias;
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.ICatalogoManejoPacienteMundo#consultarViaIngresoPorId(int)
	 * @author ricruico
	 */
	@Override
	public ViaIngresoDto consultarViaIngresoPorId(int codigo) throws IPSException {
		ViaIngresoDto viaIngreso=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoManejoPacienteDelegate delegate = new CatalogoManejoPacienteDelegate();
			viaIngreso=delegate.consultarViaIngresoPorId(codigo);
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
		return viaIngreso;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.ICatalogoManejoPacienteMundo#existeCentroCostoParametrizadoPorUnidadConsulta(int, int)
	 */
	@Override
	public boolean existeCentroCostoParametrizadoPorUnidadConsulta(
			int codigoServicio, int codigoCentroCosto) throws IPSException {
		boolean existe=false;
		try{
			HibernateUtil.beginTransaction();
			CatalogoManejoPacienteDelegate delegate = new CatalogoManejoPacienteDelegate();
			existe=delegate.existeCentroCostoParametrizadoPorUnidadConsulta(codigoServicio, codigoCentroCosto);
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
		return existe;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.manejoPaciente.interfaz.ICatalogoManejoPacienteMundo#existeCentroCostoParametrizadoPorGrupoServicio(java.lang.Integer, int)
	 */
	@Override
	public boolean existeCentroCostoParametrizadoPorGrupoServicio(
			Integer codigoGrupoServicio, int codigoCentroCosto)
			throws IPSException {
		boolean existe=false;
		try{
			HibernateUtil.beginTransaction();
			CatalogoManejoPacienteDelegate delegate = new CatalogoManejoPacienteDelegate();
			existe=delegate.existeCentroCostoParametrizadoPorGrupoServicio(codigoGrupoServicio, codigoCentroCosto);
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
		return existe;
	}

	
	/**
	 * Retorna los centros de atencion dependiendo su estado que tengan parametrizada una ciudad.
	 * @param Boolean estado. Si estado true retorna los activos, si estado false retorna los inactivos, si es null retorna todos los centros de atencion.
	 * @param boolean requiereTransaccion 
	 * @return
	 * @throws IPSException
	 */
	@Override
	public List<DtoCentrosAtencion> listarTodosCentrosAtencion(boolean requiereTransaccion, Boolean estado, int codigoInstitucion) throws IPSException {
		
		List<DtoCentrosAtencion> centrosAtencion = new ArrayList<DtoCentrosAtencion>();
		try {
			if(requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
			
			CatalogoManejoPacienteDelegate delegate = new CatalogoManejoPacienteDelegate();
			centrosAtencion = delegate.listarTodosCentrosAtencion(estado, codigoInstitucion);
			
			if(requiereTransaccion){
				HibernateUtil.endTransaction();
			}
						
		} catch (IPSException ipsme) {
			if(requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if(requiereTransaccion){
				HibernateUtil.abortTransaction();
			}	
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return centrosAtencion;

		
	}





}