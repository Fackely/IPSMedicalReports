package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenServDAO;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempNivelAtenServDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAtenServHibernateDAO implements ICierreTempNivelAtenServDAO {
	
	
	CierreTempNivelAtenServDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAtenServHibernateDAO(){
		delegate = new  CierreTempNivelAtenServDelegate();
	}
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato y  el nivel de atención dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenServ>
	 */
	public ArrayList<CierreTempNivelAtenServ> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalServicio dtoParametros){
		return delegate.buscarCierreTemporalNivelAtencion(dtoParametros);
	}
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenServHome
	 * 
	 * @param CierreTempServArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenServ cierre){
		return delegate.sincronizarCierreTemporal(cierre);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempServArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAtenServ cierre){
		delegate.delete(cierre);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAtenServDAO#obtenerValorCierreTemporalNivelServicios(int, java.util.Date, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelServicios(int codContrato,
			Date fecha, long consecutivoNivelAtencion) {
		return delegate.obtenerValorCierreTemporalNivelServicios(codContrato, 
							fecha, consecutivoNivelAtencion);
	}

}
