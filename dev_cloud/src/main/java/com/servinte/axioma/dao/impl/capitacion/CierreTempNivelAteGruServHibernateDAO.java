package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteGruServDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.orm.delegate.capitacion.CierreTempNivelAteGruServDelegate;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteGruServHibernateDAO implements
		ICierreTempNivelAteGruServDAO {
	
	CierreTempNivelAteGruServDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAteGruServHibernateDAO(){
		delegate = new CierreTempNivelAteGruServDelegate();
	}
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato, el nivel de atención y el grupo de servicio dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteGruServ>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAteGruServ> buscarCierreTemporalNivelAtencionGrupoServicio(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException{
		return delegate.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametros);
	}
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAteGruServHome
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteGruServ cierre){
		return delegate.sincronizarCierreTemporal(cierre); 
	}
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @author, Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAteGruServ cierre){
		delegate.delete(cierre);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteGruServDAO#obtenerValorCierreTemporalNivelGrupoServicios(int, java.util.Date, int, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelGrupoServicios(
			int codContrato, Date fecha, int codigoGrupoServicio,
			long consecutivoNivelAtencion) {
		return delegate.obtenerValorCierreTemporalNivelGrupoServicios(codContrato, 
							fecha, codigoGrupoServicio, consecutivoNivelAtencion);
	}

}
