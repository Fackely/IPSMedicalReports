package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenArtDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempNivelAtenArtDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenArtHibernateDAO implements ICierreTempNivelAtenArtDAO {
	
	
	CierreTempNivelAtenArtDelegate delegate;
	
	
	public CierreTempNivelAtenArtHibernateDAO(){
		delegate = new CierreTempNivelAtenArtDelegate();
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
		return delegate.buscarCierreTemporalNivelAtencion(dtoParametros);
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
		return delegate.sincronizarCierreTemporal(cierre);
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
		delegate.delete(cierre);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenArtDAO#obtenerValorCierreTemporalNivelArticulos(int, java.util.Date, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelArticulos(int codContrato,
			Date fecha, long consecutivoNivelAtencion) {
		return delegate.obtenerValorCierreTemporalNivelArticulos(codContrato, fecha, consecutivoNivelAtencion);
	}

}
