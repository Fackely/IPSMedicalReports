package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public interface ICierreTempNivelAtenArtDAO {
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * artículo según el contrato y el nivel de atención.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAtenServ>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAtenArt> buscarCierreTemporalNivelAtencion(DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException;
	
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAtenArttHome
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAtenArt cierre) throws BDException;
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAtenArt cierre
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAtenArt cierre);
	
	/**
	 * Este método obtiene el valor acumulado del Cierre Temporal
	 * para el convenio, contrato, Articulo y la fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorCierreTemporalNivelArticulos(int codContrato, Date fecha, 
							long consecutivoNivelAtencion);

}
