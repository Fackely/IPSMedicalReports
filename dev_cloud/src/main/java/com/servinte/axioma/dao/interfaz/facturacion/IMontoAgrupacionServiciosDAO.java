package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionServicio;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IMontoAgrupacionServiciosDAO {
	
	/**
	 * 
	 * Este m�todo se encarga de consultar todos las agrupaciones
	 * de servicios relacionadas a un detalle espec�fico
	 * 
	 * @param int idDetalle
	 * @return ArrayList<DTOBusquedaMontoAgrupacionServicio>
	 * @author Angela Aguirre
	 * 
	 */
	public ArrayList<DTOBusquedaMontoAgrupacionServicio> obtenerServiciosPorDetalleID(int idDetalle);
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de una agrupaci�n de servicios de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarAgrupacionServicio(int id);

}
