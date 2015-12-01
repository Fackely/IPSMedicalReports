package com.servinte.axioma.servicio.interfaz.facturacion;

import com.princetonsa.dto.facturacion.DTOMontosCobroDetalleGeneral;
import com.servinte.axioma.orm.DetalleMontoGeneral;
import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad DetalleMontoGeneral
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IDetalleMontoGeneralServicio {
	
	/**
	 * 
	 * Este Método se encarga de guardar los datos de un detalle
	 * general de un monto de cobor
	 * @return boolean
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleGeneral(DetalleMontoGeneral detalle,Usuarios usuario);
	
	/**
	 * 
	 * Este Método se encarga de buscar un detalle general de un monto de cobro
	 * por su id
	 * 
	 * @param int
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetalleMontoGeneral buscarPorID(int id);
	
	/**
	 * 
	 * Este Método se encarga de actualizar el detalle general de un monto de
	 * cobro
	 * 
	 * @param DetalleMontoGeneral
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarDetalleGeneralMontoCobro(DetalleMontoGeneral detalleMonto);

	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un detalle general de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleGeneralMontoCobro(int idDetalleMonto);
	
	/**
	 * 
	 * Este Método se encarga de consultar el valor y tipo de un monto de
	 * cobro
	 * @param int idDetalleMonto
	 * @return DTOMontosCobroDetalleGeneral
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobroDetalleGeneral obtenerValorTipoMonto(int idDetalleMonto );
	
}
