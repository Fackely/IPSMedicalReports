package com.servinte.axioma.bl.facturacion.impl;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.facturacion.interfaz.IConvenioContratoMundo;
import com.servinte.axioma.delegate.facturacion.ConvenioContratoDelegate;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la lógica asociada a los
 * Convenios y los Contratos
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public class ConvenioContratoMundo implements IConvenioContratoMundo{

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.IConvenioContratoMundo#consultarContratosVigentesPorConvenio(int)
	 * @author ricruico
	 */
	@Override
	public List<ContratoDto> consultarContratosVigentesPorConvenio(int codigoConvenio) throws IPSException {
		List<ContratoDto> listaContratos=null;
		try{
			HibernateUtil.beginTransaction();
			ConvenioContratoDelegate delegate = new ConvenioContratoDelegate();
			listaContratos=delegate.consultarContratosVigentesPorConvenio(codigoConvenio);
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
		return listaContratos;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.IConvenioContratoMundo#consultarConveniosPorInstitucion(int, int, char, boolean)
	 * @author ricruico
	 */
	@Override
	public List<ConvenioDto> consultarConveniosPorInstitucion(int institucion, Integer tipoContrato, 
			Character manejaCapitacionSubcontratada,	boolean isActivo) throws IPSException {
		List<ConvenioDto> listaConvenios=null;
		try{
			HibernateUtil.beginTransaction();
			ConvenioContratoDelegate delegate = new ConvenioContratoDelegate();
			listaConvenios=delegate.consultarConveniosPorInstitucion(institucion, tipoContrato, 
										manejaCapitacionSubcontratada, isActivo);
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
		return listaConvenios;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.IConvenioContratoMundo#consultarTodosConveniosPorInstitucion(int)
	 */
	public List<ConvenioDto> consultarTodosConveniosPorInstitucion(int codigoInstitucion) throws IPSException{
		List<ConvenioDto> listaConvenios=null;
		try{
			HibernateUtil.beginTransaction();
			ConvenioContratoDelegate delegate = new ConvenioContratoDelegate();
			listaConvenios=delegate.consultarTodosConveniosPorInstitucion(codigoInstitucion);
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
		return listaConvenios;
	}


}