package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteGruServMundo;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteGruServServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteGruServServicio implements
		ICierreTempNivelAteGruServServicio {
	
	ICierreTempNivelAteGruServMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAteGruServServicio(){
		mundo = CapitacionFabricaMundo.crearCierreTempNivelAteGruServMundo();
	}
	
	/**
	 * Este m�todo se encarga de consultar el cierre temporal de un 
	 * servicio seg�n el contrato, el nivel de atenci�n y el grupo de servicio dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteGruServ>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAteGruServ> buscarCierreTemporalNivelAtencionGrupoServicio(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException{
		return mundo.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametros);
	}
	
	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  CierreTempNivelAteGruServHome
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteGruServ cierre){
		return mundo.sincronizarCierreTemporal(cierre); 
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @author, Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAteGruServ cierre){
		mundo.eliminarRegistro(cierre);
	}

}
