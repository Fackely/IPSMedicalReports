package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.servinte.axioma.orm.DetalleMonto;


/**
 * Esta clase se encarga de definir los m�todos para
 * la entidad Detalle Monto
 *  
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IDetalleMontoDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de buscar un detalle de 
	 * un monto de cobro por su id
	 * 
	 * @return DetalleMonto
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetalleMonto buscarDetalleMontoPorID(int id);
	
	/**
	 * 
	 * Este M�todo se encarga de guardar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(DetalleMonto detalleMonto);

	/**
	 * 
	 * Este M�todo se encarga de actualizar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarDetalleMontoCobro(DetalleMonto detalleMonto);

	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de un detalle de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleMontoCobro(int idDetalleMonto);
	
		
	
}
