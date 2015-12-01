package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;

/**
 * Esta clase se encarga de manejar la logica de acceso a datos
 * de la entidad CierreTempNivAteClInvArtHome
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public class CierreTempNivelAteClaseInvArtMundo implements
		ICierreTempNivelAteClaseInvArtMundo {
	
	
	
	ICierreTempNivelAteClaseInvArtDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author Ricardo Ruiz
	 */
	public CierreTempNivelAteClaseInvArtMundo(){
		dao = CapitacionFabricaDAO.crearCierreTempNivelAteClaseInvArtDAO();
	}
	
	
	/** (non-Javadoc)
	 * @throws BDException 
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo#sincronizarCierreTemporal(com.servinte.axioma.orm.CierreTempNivAteClInvArt)
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivAteClInvArt cierre) throws BDException{
		return dao.sincronizarCierreTemporal(cierre);
	}
	
	
	/** (non-Javadoc)
	 * @throws BDException 
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo#buscarCierreTemporalNivelAtencionClaseInventarioArticulo(com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo)
	 */
	public ArrayList<CierreTempNivAteClInvArt> buscarCierreTemporalNivelAtencionClaseInventarioArticulo(
			DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		return dao.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametros);
	}
	
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo#eliminarRegistro(com.servinte.axioma.orm.CierreTempNivAteClInvArt)
	 */
	public void eliminarRegistro(CierreTempNivAteClInvArt cierre){
		dao.eliminarRegistro(cierre);		
	}


	@Override
	public Double obtenerValorCierreTemporalNivelClaseInventarioArticulo(
			int codContrato, Date fecha, int codigoClaseInventario,	long consecutivoNivelAtencion) {
		return dao.obtenerValorCierreTemporalNivelClaseInventarioArticulo(codContrato,
							fecha, codigoClaseInventario, consecutivoNivelAtencion);
	}

}
