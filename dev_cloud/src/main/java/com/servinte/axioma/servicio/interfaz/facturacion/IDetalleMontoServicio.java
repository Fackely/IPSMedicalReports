package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.DetalleMonto;

/**
 * Esta clase se encarga de definir los métodos para
 * la entidad Detalle Monto
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IDetalleMontoServicio {
	
	/**
	 * 
	 * Este Método se encarga de buscar un detalle de 
	 * un monto de cobro por su id
	 * 
	 * @return DetalleMonto
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetalleMonto buscarDetalleMontoPorID(int id);
	
	/**
	 * 
	 * Este Método se encarga de guardar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro, Usuarios
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(DTOResultadoBusquedaDetalleMontos dto,UsuarioBasico usuarioSesion);

	/**
	 * 
	 * Este Método se encarga de actualizar el detalle de un monto de
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
	 * Este método se encarga de eliminar el registro
	 * de un detalle de un monto de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarDetalleMontoCobro(int idDetalleMonto);
	
	/**
	 * 
	 * Este Método se encarga de consultar el detalle de un monto
	 * de cobro
	 * 
	 * @param int detalleCodigo,,UsuarioBasico usuarioSesion
	 * @return DTOResultadoBusquedaDetalleMontos
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOResultadoBusquedaDetalleMontos buscarDetalleMonto(int detalleCodigo,UsuarioBasico usuarioSesion);
	
	
}
