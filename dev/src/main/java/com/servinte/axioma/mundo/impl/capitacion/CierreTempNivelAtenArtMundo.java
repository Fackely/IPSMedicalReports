package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenArtDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenArtMundo;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenArtMundo implements
		ICierreTempNivelAtenArtMundo {
	
	
	ICierreTempNivelAtenArtDAO dao;
	
	public CierreTempNivelAtenArtMundo(){
		dao = CapitacionFabricaDAO.crearCierreTempNivelAtenArtDAO();
	}
	
	
	/**
	 * Este m�todo se encarga de consultar el cierre temporal de un 
	 * art�culo seg�n el contrato y el nivel de atenci�n.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenArt>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAtenArt> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		return dao.buscarCierreTemporalNivelAtencion(dtoParametros);
	}
	
	/**
	 * 
	 * Implementaci�n del m�todo attachDirty de la super clase  CierreTempNivelAtenArttHome
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenArt cierre) throws BDException{
		return dao.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAtenArt cierre){
		dao.eliminarRegistro(cierre);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAtenArtMundo#obtenerValorCierreTemporalNivelArticulos(int, java.util.Date, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelArticulos(int codContrato,
			Date fecha, long consecutivoNivelAtencion) {
		return dao.obtenerValorCierreTemporalNivelArticulos(codContrato, fecha, consecutivoNivelAtencion);
	}


}
