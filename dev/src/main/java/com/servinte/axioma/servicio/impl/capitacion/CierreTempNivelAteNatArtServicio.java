package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteNatArtMundo;
import com.servinte.axioma.orm.CierreTempNivelAteNatArt;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteNatArtServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteNatArtServicio implements
		ICierreTempNivelAteNatArtServicio {
	
	
	ICierreTempNivelAteNatArtMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAteNatArtServicio(){
		mundo = CapitacionFabricaMundo.crearCierreTempNivelAteNatArtMundo();
	}
		
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAteNatArtHome
	 * 
	 * @param CierreTempNivelAteNatArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteNatArt cierre){
		return mundo.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato, el nivel de atención y el grupo de servicio dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteNatArt>
	 */
	public ArrayList<CierreTempNivelAteNatArt> buscarCierreTemporalNivelAtencionNaturServicio(
			DTOBusquedaCierreTemporalArticulo dtoParametros){
		return mundo.buscarCierreTemporalNivelAtencionNaturServicio(dtoParametros);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAteNatArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAteNatArt cierre){
		mundo.eliminarRegistro(cierre);		
	}

}
