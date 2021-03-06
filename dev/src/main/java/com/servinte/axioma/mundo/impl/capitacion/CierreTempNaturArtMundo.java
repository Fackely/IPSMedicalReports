package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNaturArtDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNaturArtMundo;
import com.servinte.axioma.orm.CierreTempNaturArt;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNaturArtMundo implements ICierreTempNaturArtMundo {
	
	 ICierreTempNaturArtDAO dao;
	 
	 /**
	  * 
	  * M?todo constructor de la clase
	  * @author, Angela Maria Aguirre
	  */
	 public CierreTempNaturArtMundo(){
		 dao = CapitacionFabricaDAO.crearCierreTempNaturArtDAO();
	 }
	
	
	/**
	 * Este m?todo se encarga de consultar el cierre temporal de un 
	 * art?culo seg?n el contrato y  la naturaleza de art?culo dada.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNaturArt>
	 */
	public ArrayList<CierreTempNaturArt> buscarCierreTemporalNaturalezaArticulo(DTOBusquedaCierreTemporalArticulo dtoParametros){
		return dao.buscarCierreTemporalNaturalezaArticulo(dtoParametros);
	}
	
	
	/**
	 * 
	 * Implementaci?n del m?todo attachDirty de la super clase  CierreTempNaturArtHome
	 * 
	 * @param CierreTempNaturArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNaturArt cierre){
		return dao.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * 
	 * Este M?todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNaturArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNaturArt cierre){
		dao.eliminarRegistro(cierre);
	}
	


}
