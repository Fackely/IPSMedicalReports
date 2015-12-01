package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;

/**
 * Esta clase se encarga de manejar la logica de acceso a datos
 * de la entidad CierreTempNivAteClInvArt
 * @author Ricardo Ruiz
 * @since 14/01/2012
 */
public interface ICierreTempNivelAteClaseInvArtMundo {
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivAteClInvArt
	 * 
	 * @param CierreTempNivAteClInvArt cierre
	 * @return boolean
	 * @author Ricardo Ruiz
	 * @throws BDException 
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivAteClInvArt cierre) throws BDException;
	
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * articulo según el contrato, el nivel de atención y la clase de inventario dada.
	 * 
	 * @author Ricardo Ruiz
	 * @param dtoParametros
	 * @return ArrayList<CierreTempNivAteClInvArt>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivAteClInvArt> buscarCierreTemporalNivelAtencionClaseInventarioArticulo(
			DTOBusquedaCierreTemporalArticulo dtoParametros) throws BDException;
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivAteClInvArt cierre
	 * @author Ricardo Ruiz
	 *
	 */
	public void eliminarRegistro(CierreTempNivAteClInvArt cierre);
	
	/**
	 * Este método obtiene el valor acumulado del Cierre Temporal
	 * para el Contrato, Clase de Inventario del Articulo, Nivel de Atención
	 * y la Fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param codigoClaseInventario
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorCierreTemporalNivelClaseInventarioArticulo(int codContrato, Date fecha, int codigoClaseInventario, long consecutivoNivelAtencion);

}
