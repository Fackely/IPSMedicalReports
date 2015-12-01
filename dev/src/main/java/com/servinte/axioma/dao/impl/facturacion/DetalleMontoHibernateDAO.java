package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.servinte.axioma.dao.interfaz.facturacion.IDetalleMontoDAO;
import com.servinte.axioma.orm.DetalleMonto;
import com.servinte.axioma.orm.delegate.facturacion.DetalleMontoDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos para
 * la entidad Detalle Monto
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class DetalleMontoHibernateDAO implements IDetalleMontoDAO {
	
	/**
	 * Instancia de la clase DetalleMontoDelegate
	 */
	DetalleMontoDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 * @author, Angela Maria Aguirre
	 */
	public DetalleMontoHibernateDAO(){
		
		 delegate = new DetalleMontoDelegate();
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
		return delegate.buscarDetalleMontoPorID(id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de guardar el detalle de un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarDetalleMontoCobro(DetalleMonto detalleMonto){
		return delegate.guardarDetalleMontoCobro(detalleMonto);
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
		return delegate.actualizarDetalleMontoCobro(detalleMonto);
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
		return delegate.eliminarDetalleMontoCobro(idDetalleMonto);
	}


}
