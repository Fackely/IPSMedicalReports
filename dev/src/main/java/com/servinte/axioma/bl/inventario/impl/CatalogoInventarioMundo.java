package com.servinte.axioma.bl.inventario.impl;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.inventario.interfaz.ICatalogoInventarioMundo;
import com.servinte.axioma.delegate.inventario.CatalogoInventarioDelegate;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.delegate.inventario.ClaseInventarioDelegate;

/**
 * Clase que implementa los servicios de Negocio correspondientes a los
 * catalogos o par�metricas del modulo de Inventario
 * 
 * @author ricruico
 * @version 1.0
 * @created 14-ago-2012 02:23:59 p.m.
 *
 */
public class CatalogoInventarioMundo implements ICatalogoInventarioMundo{

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.inventario.interfaz.ICatalogoInventarioMundo#obtenerClaseInventarioPorSubGrupo(int)
	 * @author ricruico
	 */
	@Override
	public ClaseInventarioDto obtenerClaseInventarioPorSubGrupo(int codigoSubGrupo) throws IPSException {
		ClaseInventarioDto claseInventario=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoInventarioDelegate delegate = new CatalogoInventarioDelegate();
			claseInventario=delegate.obtenerClaseInventarioPorSubGrupo(codigoSubGrupo);
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
		return claseInventario;
	}
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.inventario.interfaz.ICatalogoInventarioMundo#consultarClaseInventario()
	 * @author ginsotfu
	 */
	public List<ClaseInventarioDto> consultarClaseInventario() throws IPSException {
		List<ClaseInventarioDto> listaClaseInventario=null;
		try{
			HibernateUtil.beginTransaction();
			ClaseInventarioDelegate delegate = new ClaseInventarioDelegate ();
			listaClaseInventario=delegate.consultarClaseInventario();
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
		return listaClaseInventario;
	}
	

}
