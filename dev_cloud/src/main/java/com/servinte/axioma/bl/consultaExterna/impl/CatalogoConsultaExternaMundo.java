package com.servinte.axioma.bl.consultaExterna.impl;


import java.util.ArrayList;
import java.util.List;
import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo;
import com.servinte.axioma.delegate.consultaExterna.ParametrizacionValorEstandarOrdenadoresDelegate;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.ValorEstandarOrdenadoresDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;


/**
 * Clase que implementa los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Consulta Externa
 * 
 * @author ginsotfu
 * @version 1.0
 * @created 26/10/2012
 */

public class CatalogoConsultaExternaMundo implements ICatalogoConsultaExternaMundo {
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo#obtenerParametrica()
	 */
	@Override
	public List<ValorEstandarOrdenadoresDto> obtenerParametrica(int tipoOrden) throws IPSException{
		List<ValorEstandarOrdenadoresDto> listaordenadores=new ArrayList<ValorEstandarOrdenadoresDto>(0);
		List<ValorEstandarOrdenadoresDto> servicios=new ArrayList<ValorEstandarOrdenadoresDto>(0);
		List<ValorEstandarOrdenadoresDto> medicamentos=new ArrayList<ValorEstandarOrdenadoresDto>(0);
		try
		{
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			
			servicios=delegate.obtenerServicios(tipoOrden);
			medicamentos=delegate.obtenerMedicamentos(tipoOrden);
			
			listaordenadores.addAll(servicios);
			listaordenadores.addAll(medicamentos);
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaordenadores;
	}
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo#consultarUnidadAgenda()
	 */
	public List<DtoUnidadesConsulta> consultarUnidadAgenda() throws IPSException {
		List<DtoUnidadesConsulta> listaUnidadAgenda=null;
		try{
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			listaUnidadAgenda=delegate.consultarUnidadAgenda();
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
		return listaUnidadAgenda;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo#ingresarOrden()
	 */
	
	@Override
	public void ingresarOrden(ValorEstandarOrdenadoresDto valor)  throws IPSException {
		try{
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			delegate.ingresarOrden(valor);
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
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo#modificarOrden()
	 */
	
	public void modificarOrden(ValorEstandarOrdenadoresDto valor) throws IPSException{
		try{
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			delegate.modificarOrden(valor);
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
	}
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo#eliminarOrden()
	 */
	public void eliminarOrden(ValorEstandarOrdenadoresDto valor ) throws IPSException{
		try{
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			delegate.eliminarOrden(valor);
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
	}
	
	public boolean consultarValidacionServicio(Integer codigoParametrica, int codigoGrupoServicio, int codigoUnidadAgenda) throws IPSException{
		boolean hayParametros=false;
		try {
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			hayParametros=delegate.consultarValidacionServicio(codigoParametrica, codigoGrupoServicio, codigoUnidadAgenda);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme){
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return hayParametros;
	}
	
	public boolean consultarValidacionMedicamento(Integer codigoParametrica, int codigoClaseInventario, int codigoUnidadAgenda) throws IPSException{
		boolean hayParametros=false;
		try {
			HibernateUtil.beginTransaction();
			ParametrizacionValorEstandarOrdenadoresDelegate delegate = new ParametrizacionValorEstandarOrdenadoresDelegate();
			hayParametros=delegate.consultarValidacionMedicamento(codigoParametrica, codigoClaseInventario, codigoUnidadAgenda);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme){
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return hayParametros;
	}
	
}