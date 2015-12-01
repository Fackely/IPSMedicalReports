package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IDetalleMontoMundo;
import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.servicio.interfaz.facturacion.IDetalleMontoServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad Detalle Monto
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoServicio implements IDetalleMontoServicio {
	
	IDetalleMontoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public DetalleMontoServicio(){
		mundo = FacturacionFabricaMundo.crearDetalleMontoMundo();
	}
		
	
	/**
	 * 
	 * Este Método se encarga de buscar un detalle de 
	 * un monto de cobro por su id
	 * 
	 * @return DetalleMonto
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetalleMonto buscarDetalleMontoPorID(int id){
		return mundo.buscarDetalleMontoPorID(id);
	}
	
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
	public boolean guardarDetalleMontoCobro(DTOResultadoBusquedaDetalleMontos dto, UsuarioBasico usuarioSesion){
		return mundo.guardarDetalleMontoCobro(dto, usuarioSesion);
	}

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
	public boolean actualizarDetalleMontoCobro(DetalleMonto detalleMonto){
		return mundo.actualizarDetalleMontoCobro(detalleMonto);
	}

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
	public boolean eliminarDetalleMontoCobro(int idDetalleMonto){
		return mundo.eliminarDetalleMontoCobro(idDetalleMonto);
	}
	
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
	public DTOResultadoBusquedaDetalleMontos buscarDetalleMonto(int detalleCodigo,UsuarioBasico usuarioSesion){
		return mundo.buscarDetalleMonto(detalleCodigo, usuarioSesion);
	}
	
	
}
