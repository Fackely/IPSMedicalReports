package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.orm.CierreTempNivelAteNatArt;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public interface ICierreTempNivelAteNatArtServicio {
	
	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  CierreTempNivelAteNatArtHome
	 * 
	 * @param CierreTempNivelAteNatArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteNatArt cierre);
	
	
	/**
	 * Este m�todo se encarga de consultar el cierre temporal de un 
	 * servicio seg�n el contrato, el nivel de atenci�n y el grupo de servicio dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteNatArt>
	 */
	public ArrayList<CierreTempNivelAteNatArt> buscarCierreTemporalNivelAtencionNaturServicio(
			DTOBusquedaCierreTemporalArticulo dtoParametros);
	
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAteNatArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAteNatArt cierre);

}
