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
	  * Método constructor de la clase
	  * @author, Angela Maria Aguirre
	  */
	 public CierreTempNaturArtMundo(){
		 dao = CapitacionFabricaDAO.crearCierreTempNaturArtDAO();
	 }
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo según el contrato y  la naturaleza de artículo dada.
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
	 * Implementación del método attachDirty de la super clase  CierreTempNaturArtHome
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
	 * Este Método se encarga de eliminar un registro de la tabla de 
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
