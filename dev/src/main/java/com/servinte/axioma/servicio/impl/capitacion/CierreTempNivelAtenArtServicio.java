package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenArtMundo;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenArtServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenArtServicio implements
		ICierreTempNivelAtenArtServicio {
	
	ICierreTempNivelAtenArtMundo mundo;
	
	
	public CierreTempNivelAtenArtServicio(){
		mundo = CapitacionFabricaMundo.crearCierreTempNivelAtenArtMundo();		
	}
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo según el contrato y el nivel de atención.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenArt>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAtenArt> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		return mundo.buscarCierreTemporalNivelAtencion(dtoParametros);
	}
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenArttHome
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenArt cierre) throws BDException{
		return mundo.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAtenArt cierre){
		mundo.eliminarRegistro(cierre);
	}


}
