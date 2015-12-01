package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ICierreTempNivelAteGruServDAO;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteGruServMundo;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public class CierreTempNivelAteGruServMundo implements
		ICierreTempNivelAteGruServMundo {
	
	
	ICierreTempNivelAteGruServDAO dao;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public CierreTempNivelAteGruServMundo(){
		dao = CapitacionFabricaDAO.crearCierreTempNivelAteGruServDAO();
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
		return dao.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametros);
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
		return dao.sincronizarCierreTemporal(cierre); 
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
		dao.eliminarRegistro(cierre);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteGruServMundo#obtenerValorCierreTemporalNivelGrupoServicios(int, java.util.Date, int, long)
	 */
	@Override
	public Double obtenerValorCierreTemporalNivelGrupoServicios(
			int codContrato, Date fecha, int codigoGrupoServicio,
			long consecutivoNivelAtencion) {
		return dao.obtenerValorCierreTemporalNivelGrupoServicios(codContrato, 
					fecha, codigoGrupoServicio, consecutivoNivelAtencion);
	}

}
