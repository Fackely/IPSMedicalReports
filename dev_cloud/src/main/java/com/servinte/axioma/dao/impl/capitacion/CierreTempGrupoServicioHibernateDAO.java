package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempGrupoServicioDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempGrupoServicioDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempGrupoServicioHibernateDAO implements ICierreTempGrupoServicioDAO {
	
	CierreTempGrupoServicioDelegate delegate;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempGrupoServicioHibernateDAO(){
		delegate = new CierreTempGrupoServicioDelegate();
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
		return delegate.buscarCierreTemporalNivelAtencion(dtoParametros);
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
		return delegate.sincronizarCierreTemporal(cierre);
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
		delegate.delete(cierre);
	}

}
