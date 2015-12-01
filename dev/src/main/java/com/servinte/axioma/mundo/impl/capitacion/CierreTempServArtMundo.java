package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempServArtDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempServArtMundo;
import com.servinte.axioma.orm.CierreTempServArt;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempServArtMundo implements ICierreTempServArtMundo {
	
	ICierreTempServArtDAO dao;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempServArtMundo(){
		dao = CapitacionFabricaDAO.crearCierreTempServArtDAO();		
	}
	

	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo o servicio, según el contrato.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempServArt>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempServArt> buscarCierreTemporalServicioArticulo(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException {
		return dao.buscarCierreTemporalServicioArticulo(dtoParametros);
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempServArtHome
	 * 
	 * @param CierreTempServArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean attachDirty(CierreTempServArt cierre) throws BDException{
		return dao.attachDirty(cierre);
	}
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempServArt cierre
	 * @author, Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public void eliminarRegistro(CierreTempServArt cierre) {
		dao.eliminarRegistro(cierre);
	}

}
