package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempClaseInvArt;

/**
 * Esta clase se encarga de gestionar la persistencia de los objetos de
 * la entidad CierreTempClaseInvArt
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public interface ICierreTempClaseInvArtDAO {
	
	/**
	 * Este m�todo se encarga de consultar el cierre temporal de un 
	 * art�culo seg�n el contrato y  la clase de inventario dada.
	 * 
	 * @author Ricardo Ruiz
	 * @param dtoParametros
	 * @return ArrayList<CierreTempNaturArt>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempClaseInvArt> buscarCierreTemporalClaseInventarioArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException;
	
	
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
	public boolean sincronizarCierreTemporal(CierreTempClaseInvArt cierre) throws BDException;
	
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempClaseInvArt cierre
	 * @author Ricardo Ruiz
	 *
	 */
	public void eliminarRegistro(CierreTempClaseInvArt cierre);

}
