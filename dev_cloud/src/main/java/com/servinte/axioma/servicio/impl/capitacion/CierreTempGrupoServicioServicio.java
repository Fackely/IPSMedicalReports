package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempGrupoServicioMundo;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempGrupoServicioServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempGrupoServicioServicio implements
		ICierreTempGrupoServicioServicio {
	
	ICierreTempGrupoServicioMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempGrupoServicioServicio(){
		mundo = CapitacionFabricaMundo.crearCierreTempGrupoServicioMundo();
	}
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato y  el nivel de atención dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempGrupoServicio>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempGrupoServicio> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException{
		return mundo.buscarCierreTemporalNivelAtencion(dtoParametros);
	}
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempGrupoServicioHome
	 * 
	 * @param CierreTempGrupoServicio cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempGrupoServicio cierre){
		return mundo.sincronizarCierreTemporal(cierre);
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
	public void eliminarRegistro(CierreTempGrupoServicio cierre){
		mundo.eliminarRegistro(cierre);
	}

}
