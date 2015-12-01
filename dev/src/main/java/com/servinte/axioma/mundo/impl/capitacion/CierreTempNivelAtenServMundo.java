package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenServDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenServMundo;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenServMundo implements
		ICierreTempNivelAtenServMundo {
	
	ICierreTempNivelAtenServDAO dao;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAtenServMundo(){
		dao = CapitacionFabricaDAO.crearCierreTempNivelAtenServDAO();
	}
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato y  el nivel de atención dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenServ>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAtenServ> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException{
		return dao.buscarCierreTemporalNivelAtencion(dtoParametros);
	}
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenServHome
	 * 
	 * @param CierreTempNivelAtenServ cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenServ cierre) throws BDException{
		return dao.sincronizarCierreTemporal(cierre);
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
	public void eliminarRegistro(CierreTempNivelAtenServ cierre){
		dao.eliminarRegistro(cierre);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenServMundo#obtenerValorCierreTemporalNivelServicios(int, java.util.Date, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelServicios(int codContrato,
			Date fecha, long consecutivoNivelAtencion) {
		return dao.obtenerValorCierreTemporalNivelServicios(codContrato, fecha, consecutivoNivelAtencion);
	}

}
