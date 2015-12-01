package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNaturArtDAO;
import com.servinte.axioma.orm.CierreTempNaturArt;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempNaturArtDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNaturArtHibernateDAO implements ICierreTempNaturArtDAO {
	
	CierreTempNaturArtDelegate delegate;
	
	
	public CierreTempNaturArtHibernateDAO(){
		delegate = new CierreTempNaturArtDelegate();
	}
	
	
	
	/**
	 * Este m�todo se encarga de consultar el cierre temporal de un 
	 * art�culo seg�n el contrato y  la naturaleza de art�culo dada.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNaturArt>
	 */
	public ArrayList<CierreTempNaturArt> buscarCierreTemporalNaturalezaArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros){
		return delegate.buscarCierreTemporalNaturalezaArticulo(dtoParametros);
	}
	
	
	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  CierreTempNaturArtHome
	 * 
	 * @param CierreTempNaturArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNaturArt cierre){
		return delegate.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNaturArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNaturArt cierre){
		delegate.delete(cierre);
	}

}
