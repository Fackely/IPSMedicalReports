package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteNatArtDAO;
import com.servinte.axioma.orm.CierreTempNivelAteNatArt;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempNivelAteNatArtDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteNatArtHibernateDAO implements
		ICierreTempNivelAteNatArtDAO {
	
	
	CierreTempNivelAteNatArtDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAteNatArtHibernateDAO(){
		delegate = new CierreTempNivelAteNatArtDelegate();
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
		return delegate.buscarCierreTemporalNivelAtencionNaturServicio(dtoParametros);
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
		return delegate.sincronizarCierreTemporal(cierre);
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
		delegate.delete(cierre);		
	}

}
