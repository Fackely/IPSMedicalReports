package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public interface ICierreTempNivelAtenServDAO {
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato y  el nivel de atención dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenServ>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAtenServ> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException;
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenServHome
	 * 
	 * @param CierreTempServArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenServ cierre) throws BDException;
	
	
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
	public void eliminarRegistro(CierreTempNivelAtenServ cierre);

	/**
	 * Este método obtiene el valor acumulado del Cierre Temporal
	 * para el convenio, contrato, Servicio y la fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorCierreTemporalNivelServicios(int codContrato, Date fecha, 
							long consecutivoNivelAtencion);
}
