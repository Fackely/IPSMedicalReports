package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempClaseInvArtDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempClaseInvArt;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempClaseInvArtDelegate;

/**
 * Esta clase se encarga de gestionar la persistencia de los objetos de
 * la entidad CierreTempClaseInvArt
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public class CierreTempClaseInvArtHibernateDAO implements ICierreTempClaseInvArtDAO {
	
	CierreTempClaseInvArtDelegate delegate;
	
	
	public CierreTempClaseInvArtHibernateDAO(){
		delegate = new CierreTempClaseInvArtDelegate();
	}
	
	
	
	/**
	 * Este m�todo se encarga de consultar el cierre temporal de un 
	 * art�culo seg�n el contrato y  la clase de inventario dada.
	 * 
	 * @author Ricardo Ruiz
	 * @param dtoParametros
	 * @return ArrayList<CierreTempClaseInvArt>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempClaseInvArt> buscarCierreTemporalClaseInventarioArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		return delegate.buscarCierreTemporalClaseInventarioArticulo(dtoParametros);
	}
	
	
	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  CierreTempClaseInvArtHome
	 * 
	 * @param CierreTempClaseInvArt cierre
	 * @return boolean
	 * @author Ricardo Ruiz
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempClaseInvArt cierre) throws BDException{
		return delegate.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempClaseInvArt cierre
	 * @author Ricardo Ruiz
	 *
	 */
	public void eliminarRegistro(CierreTempClaseInvArt cierre){
		delegate.delete(cierre);
	}

}
