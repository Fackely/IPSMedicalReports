package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempNivelAteClaseInvArtDelegate;

/**
 * Esta clase se encarga de manejar la logica de acceso a datos
 * de la entidad CierreTempNivAteClInvArt
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public class CierreTempNivelAteClaseInvArtHibernateDAO implements
		ICierreTempNivelAteClaseInvArtDAO {
	
	
	CierreTempNivelAteClaseInvArtDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author Ricardo Ruiz
	 */
	public CierreTempNivelAteClaseInvArtHibernateDAO(){
		delegate = new CierreTempNivelAteClaseInvArtDelegate();
	}
	
	
	/** (non-Javadoc)
	 * @throws BDException 
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO#buscarCierreTemporalNivelAtencionClaseInventarioArticulo(com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo)
	 */
	public ArrayList<CierreTempNivAteClInvArt> buscarCierreTemporalNivelAtencionClaseInventarioArticulo(
			DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException{
		return delegate.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametros);
	}
	
	
	/** (non-Javadoc)
	 * @throws BDException 
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO#sincronizarCierreTemporal(com.servinte.axioma.orm.CierreTempNivAteClInvArt)
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivAteClInvArt cierre) throws BDException{
		return delegate.sincronizarCierreTemporal(cierre);
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO#eliminarRegistro(com.servinte.axioma.orm.CierreTempNivAteClInvArt)
	 */
	public void eliminarRegistro(CierreTempNivAteClInvArt cierre){
		delegate.delete(cierre);		
	}


	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteClaseInvArtDAO#obtenerValorCierreTemporalNivelClaseInventarioArticulo(int, java.util.Date, int, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelClaseInventarioArticulo(
			int codContrato, Date fecha, int codigoClaseInventario,	long consecutivoNivelAtencion) {
		return delegate.obtenerValorCierreTemporalNivelClaseInventarioArticulo(codContrato,
							fecha, codigoClaseInventario, consecutivoNivelAtencion);
	}

}
